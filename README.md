# A microservice for user administration

## build:

mvn clean install

## run from the command line:

java -jar target/userMicroservice-0.0.1-SNAPSHOT.jar

## access user administration:

http://localhost:12000/users/

## functionality:

**show all users:** http://localhost:12000/users/

**show single user with certain id:** http://localhost:12000/users/x 

**delete all users:** $ curl -X DELETE localhost:8080/users/

**delete single user with certain id:** $ curl -X DELETE localhost:8080/users/x

**create user:** $ curl -H "Content-Type: application/json" -X POST -d 
'{"forename": "forname", "surname": "surname", "position": "position",
    "link": "https://github.com/x"}' localhost:8080/users/
    
**two users can not have the same githublink!**



## access data returned from github:

http://localhost:12000/externalservice

## tests:

missing tests!

