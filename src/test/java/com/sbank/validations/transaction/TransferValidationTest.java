package com.sbank.validations.transaction;


import com.sbank.validations.base.ValidationTestBase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.sbank.exception.base.ValidationMessages.MSG_INVALID_ACCOUNT_NUMBER;
import static com.sbank.exception.base.ValidationMessages.MSG_MUST_BE_POSITIVE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;

public class TransferValidationTest extends ValidationTestBase {

    @Test
    public void givenNegativeSourceAccountNoThenGivesValidationError() throws Exception {
        assertErrorMessageInResponseAndConstraintException("/api/v1/accounts/-1/transfer/100", TEN, MSG_INVALID_ACCOUNT_NUMBER);
    }

    @Test
    public void givenZeroSourceAccountNoThenGivesValidationError() throws Exception {
        assertErrorMessageInResponseAndConstraintException("/api/v1/accounts/0/transfer/100", TEN, MSG_INVALID_ACCOUNT_NUMBER);
    }

    @Test
    public void givenNegativeTargetAccountNoThenGivesValidationError() throws Exception {
        assertErrorMessageInResponseAndConstraintException("/api/v1/accounts/100/transfer/-100", TEN, MSG_INVALID_ACCOUNT_NUMBER);
    }

    @Test
    public void givenZeroTargetAccountNoThenGivesValidationError() throws Exception {
        assertErrorMessageInResponseAndConstraintException("/api/v1/accounts/1000/transfer/0", TEN, MSG_INVALID_ACCOUNT_NUMBER);
    }

    @Test
    public void givenNegativeAmountToDepositThenGivesValidationError() throws Exception {
        assertErrorMessageInResponseAndConstraintException("/api/v1/accounts/2000/transfer/100", new BigDecimal("-1"), MSG_MUST_BE_POSITIVE);
    }

    @Test
    public void givenZeroAmountToDepositThenGivesValidationError() throws Exception {
        assertErrorMessageInResponseAndConstraintException("/api/v1/accounts/2000/transfer/100", ZERO, MSG_MUST_BE_POSITIVE);
    }


}
