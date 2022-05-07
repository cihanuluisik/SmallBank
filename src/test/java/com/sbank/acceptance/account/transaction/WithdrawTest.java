package com.sbank.acceptance.account.transaction;

import com.sbank.acceptance.account.base.AcceptanceTestBase;
import com.sbank.controller.error.ApiError;
import com.sbank.controller.request.NewAccountRequest;
import com.sbank.domain.Account;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static com.sbank.exception.base.ValidationMessages.*;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

class WithdrawTest extends AcceptanceTestBase {

	@Test
	void givenNonExistingAccountNoThenWithdrawCallFailsWithNotFound() {
		//given repo cleared
		accountRepo.deleteAll();

		// when
		ResponseEntity<ApiError[]> result = restTemplate.exchange(getFullUri() + "/100/withdraw" ,
				HttpMethod.PATCH, new HttpEntity<BigDecimal>(TEN), ApiError[].class);

		//then
		acceptanceTestAssertionHelper.assertStatusAndMessage(result, NOT_FOUND, MSG_ACCOUNT_NOT_FOUND);
	}

	@Test
	void givenAmountMoreThanBalanceThenWithdrawCallFails() {
		// given an existing account
		Account account1 = accountRepo.save(new NewAccountRequest("c u", "Dubai, UAE", new BigDecimal("20")).toNewAccount());

		// when
		ResponseEntity<ApiError[]> response = restTemplate.exchange(getFullUri() + "/" + account1.getAccountNo() + "/withdraw",
				HttpMethod.PATCH, new HttpEntity<BigDecimal>(new BigDecimal("30")), ApiError[].class);

		//then
		acceptanceTestAssertionHelper.assertStatusAndMessageAndField(response, BAD_REQUEST, MSG_ACCOUNT_BALANCE_INSUFFICIENT, FIELD_BALANCE);
	}

	@Test
	void givenFullBalanceAmountToWithdrawThenSucceeds() {
		// given an existing account
		Account account1 = accountRepo.save(new NewAccountRequest("c u", "Dubai, UAE", new BigDecimal("20")).toNewAccount());

		// when
		ResponseEntity<BigDecimal> response = restTemplate.exchange(getFullUri() + "/" + account1.getAccountNo() + "/withdraw",
				HttpMethod.PATCH, new HttpEntity<BigDecimal>(account1.getBalance()), BigDecimal.class);

		//then
		acceptanceTestAssertionHelper.assertResponseBody(response, ZERO);
		assertThat(accountRepo.findById(account1.getAccountNo()).get().getBalance()).isEqualTo(ZERO);
	}

	@Test
	void givenPositiveAmountLessThanBalanceThenSucceeds() {
		// given an existing account
		Account account1 = accountRepo.save(new NewAccountRequest("c u", "Dubai, UAE", new BigDecimal("20")).toNewAccount());

		// when
		ResponseEntity<BigDecimal> response = restTemplate.exchange(getFullUri() + "/" + account1.getAccountNo() + "/withdraw",
				HttpMethod.PATCH, new HttpEntity<BigDecimal>(TEN), BigDecimal.class);

		//then
		acceptanceTestAssertionHelper.assertResponseBody(response, TEN);
		assertThat(accountRepo.findById(account1.getAccountNo()).get().getBalance()).isEqualTo(TEN);
	}

}
