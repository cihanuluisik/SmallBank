package com.sbank.domain.international;

import java.math.BigDecimal;

public class InternationalTransferEvent {

    private final Long from;
    private final BigDecimal amount;
    private final String  toIban, toSwiftCode;

    public InternationalTransferEvent(Long from, BigDecimal amount, String toIban, String toSwiftCode) {
        this.from = from;
        this.amount = amount;
        this.toIban = toIban;
        this.toSwiftCode = toSwiftCode;
    }

    public Long getFrom() {
        return from;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getToIban() {
        return toIban;
    }

    public String getToSwiftCode() {
        return toSwiftCode;
    }
}
