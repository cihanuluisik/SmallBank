package com.sbank.exception.base;

public final class ValidationMessages {

    // fields
    public final static String FIELD_ACCOUNT = "accountNo";
    public final static String FIELD_BALANCE = "balance";
    public final static String FIELD_NAME = "name";
    public final static String FIELD_ADDRESS = "address";

    // validations error messages
    public final static String MSG_INVALID_ACCOUNT_NUMBER = "Invalid Account Number";
    public final static String MSG_ACCOUNT_BALANCE_INSUFFICIENT  = "Insufficient amount";
    public final static String MSG_MUST_NOT_BE_EMPTY = "Must not be empty";
    public final static String MSG_MUST_BE_POSITIVE = "must be greater than 0";
    public final static String MSG_ACCOUNT_NOT_FOUND  = "Account not found";

    private ValidationMessages() {
        throw new UnsupportedOperationException("This class is not supposed to be instantiated.");
    }

}
