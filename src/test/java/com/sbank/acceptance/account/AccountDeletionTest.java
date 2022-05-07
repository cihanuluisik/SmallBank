package com.sbank.acceptance.account;

import com.sbank.acceptance.account.base.AcceptanceTestBase;
import com.sbank.controller.error.ApiError;
import com.sbank.controller.request.NewAccountRequest;
import com.sbank.domain.Account;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static com.sbank.exception.base.ValidationMessages.MSG_ACCOUNT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.NOT_FOUND;

class AccountDeletionTest extends AcceptanceTestBase {

	@Test
	void givenNonExistingAccountNoToDeleteThenRejects() {
		//given repo cleared
		accountRepo.deleteAll();

		//when
		ResponseEntity<ApiError[]> responseEntity = restTemplate.exchange(getFullUri() + "/100" ,
				HttpMethod.DELETE, null, ApiError[].class);

		//then
		acceptanceTestAssertionHelper.assertStatusAndMessage(responseEntity, NOT_FOUND, MSG_ACCOUNT_NOT_FOUND);
	}

	@Test
	void givenAnExistingAccountNoToDeleteThenSucceeds() {

		//given
		Account account = accountRepo.save(new NewAccountRequest("mark twain", "address x", BigDecimal.TEN).toNewAccount());

		//when
		ResponseEntity result = restTemplate.exchange(getFullUri() + "/" + account.getAccountNo(),
				HttpMethod.DELETE, null, Object.class);

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(accountRepo.findById(account.getAccountNo())).isEmpty();
	}

}
