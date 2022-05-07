package com.sbank.acceptance.account.transaction;

import com.sbank.acceptance.account.base.AcceptanceTestBase;
import com.sbank.controller.error.ApiError;
import com.sbank.controller.request.NewAccountRequest;
import com.sbank.domain.Account;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.math.BigDecimal;

import static com.sbank.exception.base.ValidationMessages.*;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

class TransferTest extends AcceptanceTestBase {


	@Test
	void given2NonExistingAccountsThenTramnsferFailsWithAccountNotFoundError() {
		//given repo cleared
		accountRepo.deleteAll();

		// when
		ResponseEntity<ApiError[]> result = restTemplate.exchange(getFullUri() + "/100/transfer/200" ,
				HttpMethod.PATCH, new HttpEntity<BigDecimal>(TEN), ApiError[].class);

		//then
		testHelper.assertStatusAndMessage(result, NOT_FOUND, MSG_ACCOUNT_NOT_FOUND);
	}

	@Test
	void givenOneNonExistingAccountThenTramnsferFailsWithAccountNotFoundError() {
		//given repo cleared
		accountRepo.deleteAll();
		Account targetAccount = accountRepo.save(new NewAccountRequest("jon mckane", "Dubai, UAE", new BigDecimal("20")).toNewAccount());


		// when
		ResponseEntity<ApiError[]> result = restTemplate.exchange(getFullUri() + "/100/transfer/" + targetAccount.getAccountNo(),
				HttpMethod.PATCH, new HttpEntity<BigDecimal>(TEN), ApiError[].class);

		//then
		testHelper.assertStatusAndMessage(result, NOT_FOUND, MSG_ACCOUNT_NOT_FOUND);
	}

	@Test
	void givenTransferAmountMoreThanSourceAccountBalanceThenTransferFails() {
		// given source account with balance
		Account sourceAccount = accountRepo.save(new NewAccountRequest("c u", "Dubai, UAE", new BigDecimal("20")).toNewAccount());
		Account targetAccount = accountRepo.save(new NewAccountRequest("jon mckane", "Dubai, UAE", new BigDecimal("20")).toNewAccount());

		// when
		ResponseEntity<ApiError[]> response = restTemplate.exchange(
				getFullUri() + "/" + sourceAccount.getAccountNo() + "/transfer/" + targetAccount.getAccountNo(),
				HttpMethod.PATCH, new HttpEntity<BigDecimal>(new BigDecimal("30")), ApiError[].class);

		//then
		testHelper.assertStatusAndMessageAndField(response, BAD_REQUEST, MSG_ACCOUNT_BALANCE_INSUFFICIENT, FIELD_BALANCE);

		assertThat(accountRepo.findById(sourceAccount.getAccountNo()).get().getBalance()).isEqualTo(sourceAccount.getBalance());
		assertThat(accountRepo.findById(targetAccount.getAccountNo()).get().getBalance()).isEqualTo(sourceAccount.getBalance());
	}

	@Test
	void givenFullBalanceOfSurceAccounThenTransferSucceeds() {

		// given source account with balance
		Account sourceAccount = accountRepo.save(new NewAccountRequest("c u", "Dubai, UAE", new BigDecimal("20")).toNewAccount());
		Account targetAccount = accountRepo.save(new NewAccountRequest("jon mckane", "Dubai, UAE", new BigDecimal("20")).toNewAccount());

		// when
		ResponseEntity<BigDecimal> response = restTemplate.exchange(
				getFullUri() + "/" + sourceAccount.getAccountNo() + "/transfer/" + targetAccount.getAccountNo(),
				HttpMethod.PATCH, new HttpEntity<BigDecimal>(sourceAccount.getBalance()), BigDecimal.class);

		//then
		testHelper.assertResponseBody(response, ZERO);

		assertThat(accountRepo.findById(sourceAccount.getAccountNo()).get().getBalance()).isEqualTo(ZERO);
		assertThat(accountRepo.findById(targetAccount.getAccountNo()).get().getBalance()).isEqualTo(sourceAccount.getBalance().add(targetAccount.getBalance()));
	}

	@Test
	void givenAmountAmountLessThanBalanceThenTransferSucceeds() {
		// given source account with balance
		Account sourceAccount = accountRepo.save(new NewAccountRequest("c u", "Dubai, UAE", new BigDecimal("20")).toNewAccount());
		Account targetAccount = accountRepo.save(new NewAccountRequest("jon mckane", "Dubai, UAE", new BigDecimal("0")).toNewAccount());

		// when
		ResponseEntity<BigDecimal> response = restTemplate.exchange(
				getFullUri() + "/" + sourceAccount.getAccountNo() + "/transfer/" + targetAccount.getAccountNo(),
				HttpMethod.PATCH, new HttpEntity<BigDecimal>(TEN), BigDecimal.class);

		//then
		testHelper.assertResponseBody(response, TEN);
		assertThat(accountRepo.findById(sourceAccount.getAccountNo()).get().getBalance()).isEqualTo(TEN);
		assertThat(accountRepo.findById(targetAccount.getAccountNo()).get().getBalance()).isEqualTo(TEN);
	}

}
