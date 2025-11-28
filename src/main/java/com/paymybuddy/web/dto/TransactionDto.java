package com.paymybuddy.web.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDto {

    @NotNull(message = "{validation.friendId.notnull}")
    private Integer friendId;

    @Size(max=150, message = "{validation.description.size}")
    private String description;

    @Positive(message = "{validation.amount.positive}")
    @NotNull(message = "{validation.amount.notnull}")
    private BigDecimal amount;
}
