package main.grp.model;

import javax.validation.constraints.*;

import java.math.BigDecimal;

public class Amount {
    @NotBlank(message = "Нужно указать валюту")
    private String currency;

    @NotNull(message = "Нужно заполнить")
    @DecimalMin(value = "0.01", message = "Сумма не указана не верно!")
    private BigDecimal value;
    // Constructors, getters and setters
    public Amount() {}

    public Amount(BigDecimal value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}