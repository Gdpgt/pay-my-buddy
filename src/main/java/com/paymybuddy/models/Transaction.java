package com.paymybuddy.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "transactions")
public class Transaction {

    private static final int MONETARY_SCALE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Size(max = 150)
    @Column(name = "description", length = 150)
    private String description;

    @Positive
    @NotNull
    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    @PreUpdate
    void normalizeAmount() {
        amount = normalize(amount);
    }

    private BigDecimal normalize(BigDecimal value) {
        if (value == null) {
            throw new IllegalStateException("Transaction amount must not be null");
        }
        return value.setScale(MONETARY_SCALE, RoundingMode.HALF_EVEN);
    }

    public String getFormattedAmount() {

        if (amount.stripTrailingZeros().scale() <= 0) {
            return amount.intValue() + "€";
        }
        return amount + "€";
    }
}