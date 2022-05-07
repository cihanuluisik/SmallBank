package com.sbank.validations.transaction;


import com.sbank.validations.base.ValidationTestBase;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static com.sbank.exception.base.ValidationMessages.MSG_INVALID_ACCOUNT_NUMBER;
import static com.sbank.exception.base.ValidationMessages.MSG_MUST_BE_POSITIVE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;

public class WithdrawalValidationTest  extends ValidationTestBase {

    @Test
    public void givenNegativeAccountNoThenGivesValidationError() throws Exception {
        assertErrorMessageInResponseAndConstraintException("/api/v1/accounts/-1/withdraw", TEN, MSG_INVALID_ACCOUNT_NUMBER);
    }

    @Test
    public void givenZeroAccountNoThenGivesValidationError() throws Exception {
        assertErrorMessageInResponseAndConstraintException("/api/v1/accounts/0/withdraw", TEN, MSG_INVALID_ACCOUNT_NUMBER);
    }

    @Test
    public void givenNegativeAmountToDepositThenGivesValidationError() throws Exception {
        assertErrorMessageInResponseAndConstraintException("/api/v1/accounts/1000/withdraw", new BigDecimal("-1"), MSG_MUST_BE_POSITIVE);
    }

    @Test
    public void givenZeroAmountToDepositThenGivesValidationError() throws Exception {
        assertErrorMessageInResponseAndConstraintException("/api/v1/accounts/1000/withdraw", ZERO, MSG_MUST_BE_POSITIVE);
    }

}
