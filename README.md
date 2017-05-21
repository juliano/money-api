Restful API for money transfer
==============================

Running the application
---------
`sbt run` will run the application in the port 8000

Running the tests
---------
`sbt test`

Available endpoints
---------
`GET /api/v1/accounts`: list all accounts

`POST /api/v1/accounts`: create new account

`GET /api/v1/accounts/:id`: return specific account

`POST /api/v1/accounts/:id/transfer`: execute a transfer

`GET /api/v1/accounts/:id/transactions`: list all transaction from that accounts

`GET /api/v1/accounts/:id/transactions/:tid`: show specific transaction from that accounts

Check out the tests and use it as examples: https://github.com/juliano/money-api/tree/master/src/test/scala/app/routes