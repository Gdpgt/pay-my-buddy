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

    @NotNull
    private Integer friendId;

    @Size(max=150)
    private String description;

    @Positive
    @NotNull
    private BigDecimal amount;
}
