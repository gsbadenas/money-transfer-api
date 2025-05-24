## Project Name: money-transfer-api 
Description: 
•	Money can be transferred to an account only in its base currency.
•	Money can only be transferred from an account in its base currency
•	The FX conversion rate
•	A transaction fee of 1% applies to all transfers and is charged to transaction initiator
•	Implement a mechanism to handle concurrent transactions safely.

## Technologies:
- Java version 17
- Spring boot 3
- Spring Rest API
- Sping Data JPA
- Junit 4 and Mockito
- H2 Database (In memory db only)
- Maven 3.9.9
- Jakarta persistence API
- Google API - gson
- Testing Tool: Postman - Testing Endpoint
- Java IDE (IntelliJ/Eclipse/SpringToolSuite4)
- Github 

## Getting Started

**1. Clone this repository to your local machine **  
   git clone https://github.com/gsbadenas/money-transfer-api.git


**2. Go to the repository money-transfer-api**
   money-transfer-api

   
**3. How to run the project using maven (USING YOUR IDE OR MAVEN COMMAND)**

   - maven clean and install
   - run spring-boot using maven or using IDE
   
   <img width="559" alt="image" src="https://github.com/user-attachments/assets/f2345859-0a46-44cd-a682-fc202fbd880c" />

Application Started At port 8080

<img width="1409" alt="image" src="https://github.com/user-attachments/assets/ddd05d56-0d14-402a-b4bc-1f26ba9ba8d3" />


**4. Open H2 Database using the url and Connect**
   *Note: the h2 console configuration can be found in application.properties file

   -JDBC url: jdbc:h2:mem:MTAPIdb
   -username: sa
   -password:

   http://localhost:8080/h2-console/

  <img width="768" alt="image" src="https://github.com/user-attachments/assets/04e95b2f-bad2-4f72-b38a-2167250b2703" />



**5. Set up your test data for account details using the sql below:**
  -  run the sql insert data


-INSERT INTO ACCOUNT (ID, ACCOUNT_NAME, ACCOUNT_NO, ACCOUNT_BALANCE, CURRENCY) VALUES
    (1, 'Joe', 1001, 300, 'USD'),
    (2,'Alice',1002, 400, 'USD'),
    (3,'Goe',1003, 500, 'JPY'),
    (4,'Lexter',1004, 500, 'AUD');

    select * from ACCOUNT;
    
 <img width="710" alt="image" src="https://github.com/user-attachments/assets/fbe018af-162e-4b68-b507-78c241143cb5" />


**6. Test your API End points:**

   a. Retrieve your account balance
       - API endpoint url: http://localhost:8080/v1/accounts/balances/{accountNo}
       - method: GET

   http://localhost:8080/v1/accounts/balances/1002
   
   <img width="601" alt="image" src="https://github.com/user-attachments/assets/cf54f8d4-54df-4fb5-8f72-8cbad71e86fb" />

   b. Transffer your money to a different Account
      - API endpoint url: http://localhost:8080/v1/transaction
      - method: POST

    Your Json Test Data: 
    {
      "accountNumberTransferFrom": "1001",
      "accountNumberTransferTo": "1003",
      "amount": "3",
      "recurringNumber": "1",
      "chargeFee": "0.01"
    }

  Result:

  <img width="629" alt="image" src="https://github.com/user-attachments/assets/cda63290-a771-487f-b37b-d2055f95f450" />


-Verify Account Balance (Database)

<img width="481" alt="image" src="https://github.com/user-attachments/assets/d529e224-011f-4f9f-a8e8-3e1c7e01ee66" />

-Verify Account Balance through Endpont to retrieve balance where amount has been transffered to another account from 1001 to 1003

<img width="546" alt="image" src="https://github.com/user-attachments/assets/e7664eb1-8b73-4cfb-9382-738a9756196a" />

-Verify account balance where Balance has been deducted with charge and transfer fee from account = 1001 

<img width="425" alt="image" src="https://github.com/user-attachments/assets/69eaf16f-06da-4a1e-9b1e-7da58cac5994" />

-Verify New Account 1002 and transfer to 1004 account with recurring for no of times (Ex. Recurring of 2 times) = "recurringNumber": "2",

<img width="629" alt="image" src="https://github.com/user-attachments/assets/4d2d9773-ab7e-472f-b0ba-834324cd719a" />



** 7. Global Exception hander and Validation Error**
   a.Account No Does not Exist
  - GET http://localhost:8080/v1/accounts/balances/100222222
  
<img width="601" alt="image" src="https://github.com/user-attachments/assets/b0b9d52e-e787-4160-b7f3-37c05b1db7aa" />

    b.Account No Does not Exist If No Value of Account Number in the Json data
    POST http://localhost:8080/v1/transaction
    {
      "accountNumberTransferFrom": "",
      "accountNumberTransferTo": "1003",
      "amount": "3",
      "recurringNumber": "1",
      "chargeFee": "0.01"
    }

  <img width="642" alt="image" src="https://github.com/user-attachments/assets/849fa8e9-2daa-44b0-892a-7a31efbb0b1f" />

    3. Check Balance Exception (Error in Internal API)
    5. Overdraft Exception - Negative or Zero Balance
    6. Global Exception Ex. (Non Found) - Server Error (500)/Internal Error (400)
   Ex. Invalid URL or Invalid endpoint

   <img width="597" alt="image" src="https://github.com/user-attachments/assets/6a8a52e1-38bd-4b57-aa10-4cb45d6906ea" />


    7. SystemException - Encounter internal server error, please check with system administrator.


 ** 8. Other Configuration and Connection**
 - application.properties
  <img width="583" alt="image" src="https://github.com/user-attachments/assets/2a988b6d-dfe1-4dc7-a7e3-8593b722791a" />

#Internal API to call and retrieve balance

  -endpoint.accountBalance=http://localhost:8080/v1/accounts/balances/{id}

 #Exchange rate external API - exchange rate to get the rate based on the currency
 
  -external.api.exchange_rate=https://api.exchangerate-api.com/v4/latest/{currencypair}

Ex. Conversation Rate of USD (External API to retrieve the USD conversion)

<img width="1792" alt="image" src="https://github.com/user-attachments/assets/e2d41db1-fa7d-4a79-a9bb-dfbab8d37159" />


  9. Other Test (TEST CASES Added)
     
<img width="1410" alt="image" src="https://github.com/user-attachments/assets/2ba863d9-771b-4a9d-a89b-0cfb118759b1" />

