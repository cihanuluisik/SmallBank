package com.sbank.acceptance.account.base;

import com.sbank.acceptance.account.AcceptanceTestAssertionHelper;
import com.sbank.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.repository.CrudRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTestBase {

	@LocalServerPort
	protected int port;

	@Autowired
	protected CrudRepository<Account, Long> accountRepo;

	protected String getFullUri() {
		return "http://localhost:"+ port+ "/api/v1/accounts";
	}
	@Autowired
	protected TestRestTemplate restTemplate;

	@Autowired
	protected AcceptanceTestAssertionHelper acceptanceTestAssertionHelper;

}
