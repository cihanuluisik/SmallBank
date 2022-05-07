package com.sbank.acceptance.account.transaction;

import com.sbank.acceptance.account.base.AcceptanceTestBase;
import com.sbank.controller.error.ApiError;
import com.sbank.controller.request.NewAccountRequest;
import com.sbank.domain.Account;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.math.BigDecimal;

import static com.sbank.exception.base.ValidationMessages.MSG_ACCOUNT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.NOT_FOUND;

class DepositTest extends AcceptanceTestBase {

	@Test
	void givenNonExistingAccountNoThenDepositCallFailsWithNotFound() {
		//given repo cleared
		accountRepo.deleteAll();

		// when
		ResponseEntity<ApiError[]> result = restTemplate.exchange(getFullUri() + "/100/deposit" ,
				HttpMethod.PATCH, new HttpEntity<BigDecimal>(BigDecimal.TEN), ApiError[].class);

		//then
		testHelper.assertStatusAndMessage(result, NOT_FOUND, MSG_ACCOUNT_NOT_FOUND);
	}


	@Test
	void givenAnExistingAccountAndAmountThenDepositSucceeds() {
		// given an existing account
		Account account1 = accountRepo.save(new NewAccountRequest("c u", "Dubai, UAE", BigDecimal.TEN).toNewAccount());

		// when
		ResponseEntity<BigDecimal> response = restTemplate.exchange(getFullUri() + "/" + account1.getAccountNo() + "/deposit",
				HttpMethod.PATCH, new HttpEntity<BigDecimal>(BigDecimal.TEN), BigDecimal.class);

		//then
		testHelper.assertResponseBody(response,new BigDecimal("20") );
		assertThat(accountRepo.findById(account1.getAccountNo()).get().getBalance()).isEqualTo(new BigDecimal("20"));
	}

}
