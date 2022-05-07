package com.sbank.acceptance.account;

import com.sbank.acceptance.account.base.AcceptanceTestBase;
import com.sbank.controller.error.ApiError;
import com.sbank.controller.request.NewAccountRequest;
import com.sbank.domain.Account;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static com.sbank.exception.base.ValidationMessages.MSG_ACCOUNT_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class AccountGetBalanceTest extends AcceptanceTestBase {


    @Test
    void givenNonExistingAccountThenReturnsNotFoundError() {
        // given a non existing account
        accountRepo.deleteAll();
        Long accountNone = Long.MAX_VALUE;

        // when
        ResponseEntity<ApiError[]> responseEntity = restTemplate.getForEntity(getFullUri() + "/" + accountNone + "/balance", ApiError[].class);

        //then
        acceptanceTestAssertionHelper.assertStatusAndMessage(responseEntity, NOT_FOUND, MSG_ACCOUNT_NOT_FOUND);
    }

    @Test
    void givenAnExistingAccountThenReturnsItsBalance() {

        // given an existing account
        Account account1 = accountRepo.save(new NewAccountRequest("c u", "Dubai, UAE", BigDecimal.TEN).toNewAccount());

        // when
        ResponseEntity<BigDecimal> response = restTemplate.getForEntity(getFullUri() + "/" + account1.getAccountNo() + "/balance", BigDecimal.class);

        //then
        acceptanceTestAssertionHelper.assertResponseBody(response, account1.getBalance() );
    }

    

}
