# SolveAnything
This project deals with the evaluation of mathematical expressions

How to run ?
- Download Java JDK
- Install Maven
- `git clone project` then `cd SolveAnything` then `mvn clean install` 
- run `java -jar target\SolveAnything-1.0-jar-with-dependencies.jar`
- or directly git clone into IDE and run RunServer Class


# How to check the APIs

- First API is to evaluate an expression 

api  
`curl --location --request GET 'localhost:8001/evaluate' \
--header 'Content-Type: application/json' \
--data-raw '{
    "expression":"3 + 5 + (10 - 8)*4/2"
}'`

output
`{
    "status_code": 200,
    "id": 1,
    "value": 12.0,
    "status": true
}`

- Second API is get the audit details using above id

api
`curl --location --request GET 'localhost:8001/details' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id":1
}'`

output
`{
    "expression": "3 + 5 + (10 - 8)*4/2",
    "status_code": 200,
    "time_taken_ms": 22,
    "id": 1
}`
