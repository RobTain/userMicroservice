# A microservice for user administration

## build:

mvn clean install


## run from the command line:

java -jar target/userMicroservice-0.0.1-SNAPSHOT.jar


## run from Eclipse

Right click on project -> Run as "Maven build..." Goals clean install
Right click on project -> Run as "Spring Boot App"


## access user administration:

http://localhost:8080/users/


## functionality:

###Use Postman for Requests###

**show all users:** 
http://localhost:8080/users/

**show single user with certain id x:** 
http://localhost:8080/users/x 

**delete all users:** 
$ curl -X DELETE http://localhost:8080/users/

**delete single user with certain id x:** 
$ curl -X DELETE http://localhost:8080/users/x

**create user:** 
$ curl -H "Content-Type: application/json" -X POST -d '{"forename": "forename", "surname": "surname", "position": "position", "link": "https://github.com/x"}' http://localhost:8080/users/
    
**update user with certain id x:** 
$ curl -H 'Content-Type: application/json' -X PUT -d 
'{"forename":"forename","surname":"surname","position":"position",
"link":"https://github.com/x"}' 
http://localhost:8080/users/x


## access data returned from Github:

**show Github-Repositories and coding language from a certain user with id x:**

http://localhost:8080/users/x/repositories


## tests:

+ **test01_checkInitialState():**
+ **test02_createValidUser()**
+ **test03_createDublicate()**
+ **test04_createDublicateExceptLinkValue()**
+ **test05_createNewUserWithExistingLink()**
+ **test06_createUserWithMissingInputs()**
+ **test07_createMultipleUsers()**
+ **test08_deleteValidUser()**
+ **test09_deleteDeletedUser()**
+ **test10_deleteNotValidUser()**
+ **test11_updateValidUser()**
+ **test12_updateDublicate()**
+ **test13_updateDeletedUser()**
+ **test14_updateNonExistingUser()**
+ **test15_deleteAllUser()**
+ **test16_getGitHubInformationFromNotValidUser()**
+ **test17_getGitHubInformationFromValidUser()**
