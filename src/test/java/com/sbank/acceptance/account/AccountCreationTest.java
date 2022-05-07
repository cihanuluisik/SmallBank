package com.sbank.acceptance.account;

import com.sbank.acceptance.account.base.AcceptanceTestBase;
import com.sbank.controller.error.ApiError;
import com.sbank.controller.request.NewAccountRequest;
import com.sbank.domain.Account;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.sbank.exception.base.ValidationMessages.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class AccountCreationTest extends AcceptanceTestBase {

	@Test
	void givenEmptyNameAddressAndZeroBalanceInAccountThenCreationRejectedWithApiErrors() {
		// given
		NewAccountRequest newAccountRequest = new NewAccountRequest(" ", " ", BigDecimal.ZERO);

		// when
		ResponseEntity<ApiError[]> result = restTemplate.postForEntity(getFullUri(), newAccountRequest, ApiError[].class);

		// then
		assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
		assertThat(result.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
		ApiError[] apiErrors = result.getBody();
		assertThat(Arrays.stream(apiErrors).map(ApiError::getField)).containsExactlyInAnyOrder("name", "address","balance");
		assertThat(Arrays.stream(apiErrors).map(ApiError::getMessage).collect(Collectors.toSet())).containsExactlyInAnyOrder(MSG_MUST_NOT_BE_EMPTY, MSG_MUST_BE_POSITIVE);
	}

	@Test
	void givenAProperAccountThenCreationSucceeds() {
		//given
		NewAccountRequest newAccountRequest = new NewAccountRequest("bill gates", "Palm Jumairah, Dubai, UAE", BigDecimal.TEN);

		//when
		ResponseEntity<Long> result = restTemplate.postForEntity(getFullUri(), newAccountRequest, Long.class);

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(result.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
		Long newAccountNumber = result.getBody();
		Account accountFromRepo = accountRepo.findById(newAccountNumber).get();
		assertThat(accountFromRepo.getName()).isEqualTo(newAccountRequest.getName());
		assertThat(accountFromRepo.getAddress()).isEqualTo(newAccountRequest.getAddress());
		assertThat(accountFromRepo.getBalance()).isEqualTo(newAccountRequest.getBalance());
	}

}
