package com.sbank.validations.transaction;


import com.sbank.controller.request.InternationalTransferRequest;
import com.sbank.validations.base.ValidationTestBase;
import org.junit.jupiter.api.Test;

import static com.sbank.exception.base.ValidationMessages.MSG_INVALID_ACCOUNT_NUMBER;
import static com.sbank.exception.base.ValidationMessages.MSG_MUST_NOT_BE_EMPTY;
import static java.math.BigDecimal.TEN;

public class InternationalTransferValidationTest extends ValidationTestBase {

    private final InternationalTransferRequest transferRequest =
            new InternationalTransferRequest(TEN, "AE550030010974907920001", "ADCBAEAA");

    @Test
    public void givenNegativeSourceAccountNoThenGivesValidationError() throws Exception {
        assertErrorMessageInResponseAndConstraintException("/api/v1/accounts/-1/transfer-international", transferRequest, MSG_INVALID_ACCOUNT_NUMBER);
    }

    @Test
    public void givenZeroSourceAccountNoThenGivesValidationError() throws Exception {
        assertErrorMessageInResponseAndConstraintException("/api/v1/accounts/0/transfer-international", transferRequest, MSG_INVALID_ACCOUNT_NUMBER);
    }

    @Test
    public void givenNullOrBlankTargetAccountNoThenGivesValidationError() throws Exception {
        assertErrorMessageInResponse("/api/v1/accounts/100/transfer-international",
                new InternationalTransferRequest(TEN, null, "ADCBAEAA"), MSG_MUST_NOT_BE_EMPTY);

        assertErrorMessageInResponse("/api/v1/accounts/100/transfer-international",
                new InternationalTransferRequest(TEN, "", "ADCBAEAA"), MSG_MUST_NOT_BE_EMPTY);

        assertErrorMessageInResponse("/api/v1/accounts/100/transfer-international",
                new InternationalTransferRequest(TEN, " ", "ADCBAEAA"), MSG_MUST_NOT_BE_EMPTY);
    }

    @Test
    public void givenNullOrBlankSwiftcodeThenGivesValidationError() throws Exception {
        assertErrorMessageInResponse("/api/v1/accounts/100/transfer-international",
                new InternationalTransferRequest(TEN, "AE550030010974907920001", null), MSG_MUST_NOT_BE_EMPTY);

        assertErrorMessageInResponse("/api/v1/accounts/100/transfer-international",
                new InternationalTransferRequest(TEN, "AE550030010974907920001", ""), MSG_MUST_NOT_BE_EMPTY);

        assertErrorMessageInResponse("/api/v1/accounts/100/transfer-international",
                new InternationalTransferRequest(TEN, "AE550030010974907920001", "  "), MSG_MUST_NOT_BE_EMPTY);
    }

}
