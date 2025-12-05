package com.paymybuddy.web.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDto {

    // Initialisé à "-1" pour faire en sorte que th:field=*{friendId} dans le form. 
    // permette le fonctionnement du placeholder
    @NotNull(message = "{validation.friendId.notnull}")
    @Min(value = 1, message = "{validation.friendId.startsAtOne")
    private Integer friendId = -1;

    @Size(max=150, message = "{validation.description.size}")
    private String description;

    @Positive(message = "{validation.amount.positive}")
    @NotNull(message = "{validation.amount.notnull}")
    private BigDecimal amount;
}
