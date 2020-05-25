# On Site Management

Application to manage on site employees after COVID-19 pandemic. 

The purpose of this application is to let HR departments: 

- define sites, and restaurants.
- define what they want to track: arrivals, departures, lunch time slots, parkings,...
- fix limits on these features for each site (I do not want more than 25 peoples to arrive between 9.00 and 9.30, by instance).
- track usages, and availabilities.

After that, users can access the application, and declare the days they want to be on site. 

![theme light](https://live.staticflickr.com/65535/49934851166_a70d074fd9_k.jpg)


# Technical Overview

Application based on Spring Boot + Vue.js, packaged in a single jar using Maven.



- [x] **mvn clean install** build the full packaged jar, including the ui part.
- [x] Vue cli is used on the project: node js server will be started on **port 9000**, and proxify api calls to the backend part on **port 8080**.


# Getting started


Installation can be done following theses steps: 
- [x] use **mvn clean install** to create the jar file. If you want to use an embedded H2 database, use Maven profile 'local', which is activated by default. Otherwise, add to pom.xml the driver you'll use (Amundi uses MariaDB for test and production environments).
- [x] to start the app, you can simply use:


`java -jar  onSiteManagement.jar`

