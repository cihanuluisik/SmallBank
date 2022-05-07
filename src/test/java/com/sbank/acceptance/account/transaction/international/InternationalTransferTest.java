package com.sbank.acceptance.account.transaction.international;

import com.sbank.acceptance.account.base.AcceptanceTestBase;
import com.sbank.controller.error.ApiError;
import com.sbank.controller.request.InternationalTransferRequest;
import com.sbank.controller.request.NewAccountRequest;
import com.sbank.domain.Account;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.sbank.exception.base.ValidationMessages.FIELD_BALANCE;
import static com.sbank.exception.base.ValidationMessages.MSG_ACCOUNT_BALANCE_INSUFFICIENT;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class InternationalTransferTest extends AcceptanceTestBase {

	@Test
	void givenTransferAmountMoreThanSourceAccountBalanceThenTransferFails() {
		// given source account with balance
		Account sourceAccount = accountRepo.save(new NewAccountRequest("c u", "Dubai, UAE", new BigDecimal(BigInteger.ONE)).toNewAccount());
		InternationalTransferRequest internationalTransferRequest = new InternationalTransferRequest(TEN, "AE550030010974907920001", "ADCBAEAA");

		// when
		ResponseEntity<ApiError[]> response = restTemplate.exchange(
				getFullUri() + "/" + sourceAccount.getAccountNo() + "/transfer-international",
				HttpMethod.PATCH, new HttpEntity(internationalTransferRequest), ApiError[].class);

		//then
		acceptanceTestAssertionHelper.assertStatusAndMessageAndField(response, BAD_REQUEST, MSG_ACCOUNT_BALANCE_INSUFFICIENT, FIELD_BALANCE);
	}

	@Test
	void givenFullBalanceOfSurceAccounThenTransferSucceeds() {

		// given source account with balance
		Account sourceAccount = accountRepo.save(new NewAccountRequest("c u", "Dubai, UAE", TEN).toNewAccount());
		InternationalTransferRequest internationalTransferRequest = new InternationalTransferRequest(TEN, "AE550030010974907920001", "ADCBAEAA");


		// when
		ResponseEntity<BigDecimal> response = restTemplate.exchange(
				getFullUri() + "/" + sourceAccount.getAccountNo() + "/transfer-international" ,
				HttpMethod.PATCH, new HttpEntity(internationalTransferRequest), BigDecimal.class);

		//then
		acceptanceTestAssertionHelper.assertResponseBody(response, ZERO);
		assertThat(accountRepo.findById(sourceAccount.getAccountNo()).get().getBalance()).isEqualTo(ZERO);

		acceptanceTestAssertionHelper.assertMessageReceivedBySwift(internationalTransferRequest);
	}


	@Test
	void givenAmountAmountLessThanBalanceThenTransferSucceeds() {
		// given source account with balance
		Account sourceAccount = accountRepo.save(new NewAccountRequest("c u", "Dubai, UAE", new BigDecimal("20")).toNewAccount());
		InternationalTransferRequest internationalTransferRequest = new InternationalTransferRequest(TEN, "AE550030010974907920001", "ADCBAEAA");

		// when
		ResponseEntity<BigDecimal> response = restTemplate.exchange(
				getFullUri() + "/" + sourceAccount.getAccountNo() + "/transfer-international" ,
				HttpMethod.PATCH, new HttpEntity(internationalTransferRequest), BigDecimal.class);

		//then
		acceptanceTestAssertionHelper.assertResponseBody(response, TEN);
		assertThat(accountRepo.findById(sourceAccount.getAccountNo()).get().getBalance()).isEqualTo(TEN);

		acceptanceTestAssertionHelper.assertMessageReceivedBySwift(internationalTransferRequest);
	}

}
