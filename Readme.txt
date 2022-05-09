
HOW TO BUILD and RUN LOCALLY WITHOUT DOCKER
./gradlew build
java -jar build/libs/SmallBank-0.0.1-SNAPSHOT.jar

HOW TO BUILD DOCKER IMAGE
./build-docker.sh

HOW TO RUN DOCKER IMAGE
./run-docker.sh

TECHNOLOGY ------------------------------------------
- Java 17
- Spring Boot 2.6.7, Spring REST, Spring Data at spec level
- JSR/Hibernate validation
- Gradle 7.4

DESIGN NOTES ------------------------------------------
- All designed with SOLID, YAGNI, KISS principles.
- Repository interface extends Spring Data CrudRepository to ease switching databases as instructed.
- Repository saves are atomic and thread safe. At the edge, multi account transfers repeatedly tries to acquire
the locks of involved entities before operation.
- Transaction history on accounts have not been kept separately.
- Domain classes are simplified. Like, account only includes name and address skipping all date fields. Like wise international
account number fields are reduced to IBAN and SWIFT CODE only.
- Accounts' currency unit AED and thats the only allowed one. Being not required, currency unit is hidden everywhere.

TODOS ------------------------------------------
- Repo layer should return copies of objects to separate the layer. But if done, object level locking will break.
If done, one solution to go further might be a lock manager locking entities during monetary operations
all of which is considered 'too much' for this test.
- Concurrency tests. There should not be any concurrency bug but due to enough time already spent on the test, concurrency
testing account withdraw and deposits and transfers is left but noted here.
- 'Record updated after fetch' issue which could have been fixed by employing a date field for versioning the entities
to implement optimistic locking at API call level, has been left but noted here.
- load tests

API USAGE ------------------------------------------

1. Creation of account
http method : POST http://HOST:PORT/api/v1/accounts  '{"name": "..", "address": "..", "balance": "XXXX"}'
example :
curl -X POST http://localhost:8080/api/v1/accounts -H 'Content-Type: application/json' -d '{"name": "bill gate", "address": "uae", "balance": "10"}'

2. List of accounts
http Method : http GET http://HOST:PORT/api/v1/accounts
example :
command : curl http://localhost:8080/api/v1/accounts
result : [{"name":"cihan 1","address":"uae","balance":10,"accountNo":1,"currency":"AED"}]

3. Deletion of account
http method : DELETE http://HOST:PORT/api/v1/accounts/{account number}
example :
command : curl --request DELETE http://localhost:8080/api/v1/accounts/1
result : OK

4. Getting the balance
http method :  GET http://HOST:PORT/api/v1/accounts/{account number}/balance
example :
command : curl  http://localhost:8080/api/v1/accounts/1/balance
result : 10

5. Money deposit
http method : PATCH http://HOST:PORT/api/v1/accounts/{accountNo}/deposit AMOUNT_IN_BODY

6. Money withdrawal
http method : PATCH http://HOST:PORT/api/v1/accounts/{accountNo}/withdraw AMOUNT_IN_BODY

7. Money transfer
http method : PATCH http://HOST:PORT/api/v1/accounts/{sourceAccountNo}/transfer/{targetAccountNo} AMOUNT_IN_BODY

8. International transfer
http method : PATCH http://HOST:PORT/api/v1/accounts/{sourceAccountNo}/transfer-international '{"targetAccountIBAN": "..", "targetAccountSwiftCode": "..", "amount": "XXXX"}'


