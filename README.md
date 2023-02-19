# Average Time Service

## The Problem

+ Create a REST-ful MicroService which will continuously calculate the average time it takes to
  perform a named task.


+ There are N tasks that need to be tracked, each with a unique identifier.


+ You are asked to implement two methods:
    + **Task performed**: It accepts the task identifier and the duration in milliseconds. It
  performs the necessary calculations, data storing and it returns ok or an error. 
    + **Current average time**: It accepts the task identifier and it returns the task identifier
  and the average duration in milliseconds.
    
---
## The Solution

### Api

Note: _Content will be returned in a JSON format_

### Operations

### Task Performed Endpoint

> [POST] /taskPerformed

+ This endpoint allows you to pass a body detailing a task identifier and a duration for said task to be added.

#### Example Request Body
```json
{
  "taskIdentifier" : "A",
  "duration" : 5.0
}
```

#### Example Output
```json
"true/false"
```

### Current Average Time Endpoint

> [POST] /currentAverageTime

+ This endpoint allows you to pass a body detailing a task identifier and get the current average duration for task of said type.

#### Example Request Body
```json
{
  "taskIdentifier" : "A"
}
```

#### Example Output
```json
{
  "taskIdentifier": "A",
  "averageDuration": 5.0
}
```
---

## Testing

+ Units tests have been added to test all logic.

+ As well as unit tests, Pitest was introduced.

+ [Pitest](https://pitest.org/) is a state-of-the-art mutation testing system.

#### Pitest

+ Pitest will automatically put mutations into the code and then run the tests and show if the tests kill the mutants.

+ mvn goal command for pitest: **mvn pitest:mutationCoverage**

+ A html mutation coverage report is generated. (This task can take a while to run)

+ Report path: **target/pit-reports/index.html** - note open with a browser for best effect


#### Note: These maven goals can also be run via the UI in IntelliJ in the Maven tab.

---

## Local Setup

### Database Setup

#### Create docker container with postgres DB:

```
> docker create --name cool-planet-tester -e POSTGRES_PASSWORD=passwordXX -p 5432:5432 postgres:12.2-alpine 
```

#### Start/Stop container:
```
> docker start/stop cool-planet-tester
```

#### DB Connection Info:

```
JDBC URL: 'jdbc:postgresql://localhost:5432/postgres'

Username: 'postgres'

Password: 'passwordXX'
```

> Note: The role might not exist in the db for this - you may need to create an admin role named **postgres**
> 
#### Connect to Database

+ Use the universal DB tool DBeaver - https://dbeaver.io/

+ Once downloaded, open DBeaver and setup a Postgres DB connection using the DB connection info above.

NOTE: *Make sure the Docker container is running*

+ Once connected create a new database called: **_cool_planet_**

+ Switch to the new database and run the **_data.sql_** script in this project to populate the database with seed data.

---

### IntelliJ

+ Once the project has loaded, click the green play button on the top right of the screen.

+ When the API is running, navigate to **Postman**.

+ Open a new API request tab and set the request type to **POST**.

**Task Performed Endpoint** 

+ Set the request URL to **localhost:8080/taskPerformed**.

+ Set the request body to raw, then select JSON from the dropdown and enter the sample body above.

+ Click the send button to complete the request.

**Current Average Time Endpoint**

+ Set the request URL to **localhost:8080/currentAverageTime**.

+ Set the request body to raw, then select JSON from the dropdown and enter the sample body above.

+ Click the send button to complete the request.

---

## Possible Future Features

+ Add additional endpoint to add new task types for processing (currently manual task)
+ Add Jacoco for better test coverage

---

Created by DF (2023).