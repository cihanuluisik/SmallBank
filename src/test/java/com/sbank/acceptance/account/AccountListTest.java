package com.sbank.acceptance.account;

import com.sbank.acceptance.account.base.AcceptanceTestBase;
import com.sbank.controller.request.NewAccountRequest;
import com.sbank.domain.Account;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountListTest extends AcceptanceTestBase {

    @Test
    void givenNoAccountExistsThenReturnsEmptyList() {
        // given empty repo
        accountRepo.deleteAll();

        // when
        ResponseEntity<Account[]> response = restTemplate.getForEntity(getFullUri(), Account[].class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        Account[] accounts = response.getBody();
        assertThat(accounts).isEmpty();
    }

    @Test
    void givenTwoAccountsThenReturnsThem() {

        // given 2 accounts saved already
        Account account1 = accountRepo.save(new NewAccountRequest("c u", "Dubai, UAE", BigDecimal.TEN).toNewAccount());
        Account account2 = accountRepo.save(new NewAccountRequest("a k", "Abu Dhabi, UAE", BigDecimal.TEN).toNewAccount());

        // when
        ResponseEntity<Account[]> response = restTemplate.getForEntity(getFullUri(), Account[].class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        Account[] accounts = response.getBody();
        assertThat(accounts).hasSize(2);
        assertThat(Arrays.stream(accounts).collect(Collectors.toSet())).hasSameElementsAs(Set.of(account1, account2));
    }


}
