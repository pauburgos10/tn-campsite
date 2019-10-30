# tn-campsite
REST service to handle reservations on single campsite
# Regarding the Database:
It's configured to use Mysql database. Spring boot application will create the schema and tables. Its default values are for 
hostname: localhost
port:3306
username:root
password: root
To configure username and password, please modify the file \src\main\resources\application.properties 
spring.datasource.username=root
spring.datasource.password=root
### Running Project
The REST service is built on Spring Boot. To have it running you can use a command line standing in path where you have downloaded the project in folder "tn-campsite" run "mvn spring-boot:root".
#### With Maven
```bash
$ git clone https://github.com/pauburgos10/tn-campsite.git
$ cd tn-campsite
$ mvn spring-boot:run
```
Service has an endpoint that can be used to insert data into de database. 
Once the service is started you can use Postman or similar to use the endpoints. Use the following url: http://localhost:8080/campsite/demo with a POST method and it will load campsite and slots objects to save data on database.
The model considers Campsite object to be the one to make reservations for, and Slots are the dates linked to a particular campsite which can be linked to reservations with different statuses (ACTIVE or CANCELLED). So it considers adding new campsites to make reservations to.

To check availability you need to have slots saved. For demo purposes the "/demo" endpoint will save slots from 2018-12-06 to 2019-01-18 for campsite id: 1, name: "Pacific Ocean volcano". This is made taking as input a json file uploaded to an AWS S3 bucket. And with propper access it can be modified to load new slots and campsites. 
And you can save more slots with "http://localhost:8080/campsite/slots/{campsiteId}" endpoint and POST method passing a json object array with the following format:
```bash
[  
  {  
     "campsite":{ "id": 1},
     "date":"2019-01-19"
  },
  {  
     "campsite":{ "id": 1},
     "date":"2019-01-20"
  }
]
```

This endpoint will not save slots for same date and campsite.

The endpoint to check availability is the following: localhost:8080/campsite/availability. If start and end dates are not set it will check the default next 30 days starting from tomorrow and only the loaded slots. And the optional parameters can be added like this: localhost:8080/campsite/availability?start_date=2018-12-06&end_date=2018-12-16

With the url http://localhost:8080/campsite/reservation/{campsite_id} POST method you can create a reservation using a request body like the following:
```bash
{
  "email": "john.doe@email.com",
  "fullName": "John Doe",
  "arrivalDate": "2018-12-16",
  "departureDate": "2018-12-17"
}
```

It will return the information for reservation created including reservation Id. If dates requested are not available it will return the corresponding message. Also will return the error messages from the dates validations requested. 

Notice that this will make a reservation for 1 day (1 slot), having in mind checkout is the next day at 12 a.m. (check in and check out hours are not handled).

With the url http://localhost:8080/campsite/reservation PUT method you will be able to update the reservation with the ID passed in path variable. Try changing dates. It will also check the dates validations coresponding.
```bash
{
  "id": 1,
  "email": "john.doe@email.com",
  "fullName": "John Doe",
  "arrivalDate": "2018-12-16",
  "departureDate": "2018-12-17"
}
```

With the url http://localhost:8080/campsite/reservation/{id} DELETE method you will be available to cancell a reservation. Have in mind that it will be a logical delete, so it will just update the status of the reservations and it's slots and not delete the records from database. Also means that afted cancellation the reservation can be consulted again and it will show the current status CANCELLED.
