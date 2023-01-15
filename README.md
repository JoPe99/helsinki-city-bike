# Helsinki City Bike App

# Description of the project: 

The purpose of this app is to showcase data from Helsinki city bikes from the year 2021. 

This repository contains a Kotlin/Spring/PostGreSQL backend which can be run in a Docker container or by itself, and a Vue/Typescript/Electron frontend application.

#### Data used in the project

Journey data is from these files:
- https://dev.hsl.fi/citybikes/od-trips-2021/2021-05.csv
- https://dev.hsl.fi/citybikes/od-trips-2021/2021-06.csv
- https://dev.hsl.fi/citybikes/od-trips-2021/2021-07.csv

The data is owned by City Bike Finland. 

Station data is from this file:
- https://opendata.arcgis.com/datasets/726277c507ef4914b0aec3cbcfcbfafc_0.csv

License and information can be found [here](https://www.avoindata.fi/data/en/dataset/hsl-n-kaupunkipyoraasemat/resource/a23eef3a-cc40-4608-8aa2-c730d17e8902).


# Prerequisites: 

Should the reviewer install something on their computer before they can compile and run the project? Does the project only work on Windows or Linux? List all steps that need to be done before trying to run the project. If versions are important, remember to mention those as well.

# Configurations: 

Do you have to configure for example database connections locally? Provide clear instructions on what needs to be changed and where. In case you have an .env file which you, of course, should not add to GitHub, you can send that file to the reviewers by other means. How to run the project? Do you have to install some packages or compile the code? If you have separate services for example for backend and frontend, remember to write instructions for all needed services.

#### Frontend

#### Backend


# Tests 

<span style="color:red">Tests clear the backend database when run!</span>

##### Frontend

Frontend tests include some very simple tests of data formatting functions, and an integration test with the backend API. The tests require that the backend is running. 

To run the frontend tests, open command line in the frontend directory and run:

Unit tests:
```
npm run test:unit
```

E2E tests:
```
npm run test:e2e
```

##### Backend

Backend tests are _very_ limited and only test parsing and validation of journeys/stations from CSV files. 

To run the backend tests, open terminal in the backend directory and run:
```
./gradlew test
```

# Features

#### Data import

- Script to download the CSV files
- Importing data from CSV files to PostGreSQL DB
- Data validation

#### Journey list view

- Pagination, sorting, filtering and search implemented server-side
- Selecting a journey displays departure & return station on a map
- Displays basic journey information on the data table

#### Station list view

- Pagination is programmed into backend, but not used. Instead the frontend calls for all station data. Pagination, searching and sorting is done client-side
- Displays all the stations on a map. Map supports selection by clicking a marker
- Displays basic station information on the data table

#### Single station view

- Single station view is available after selecting a station on the stations tab
- Includes:
    - Date pickers for filtering data from custom time period
    - Total journeys, total departure journeys, and total return journeys
    - Average distance & duration for both departures and returns
    - Top 5 departure/return stations

#### Insert view

- Includes insert forms for journeys & stations
- Validation for both forms
- Journey insert features worth mentioning:
    - Date/timepickers for departure/return times
    - Departure/return station picker
- Station insert features worth mentioning:
    - Station ID defaults to first available one
    - Location picker


# Technology choices: 

#### Frontend

Frontend is created with Typescript/VueJS accompanied by Vuetify component library. 

I chose Vue as I have prior experience with it, and it has been very enjoyable and flexible. 

Electron is used for packaging the app, making it possible to distribute the frontend app as a single executable/installer.

Pinia was chosen as a state management for the frontend, and Vue Router for routing.
For linting I used ESLint/Prettier.

#### Backend

Backend is Kotlin with Spring and PostGreSQL as database. 

I chose Kotlin as I had some experience with Android Development with it before, and wanted to get backend experience with it. 

PostGreSQL is something I have used before and felt it would work fine here too.

Project includes instructions for running the backend in Docker.


#### Testing
Frontend unit testing is done with Mocha/Chai, E2E-testing with Cypress, and frontend code coverage is provided by Coveralls. 

# Lessons learned

#### Overall

Overall the project was a great way to learn about Typescript, Kotlin, Spring, Docker and REST APIs in general. This was the first project of this size which I did from the start to finish by myself. Most important lesson from perspective of design would be that I should have planned the architechture more beforehand, and not just jumped into it. 


#### Frontend

This was my first "real" project with Typescript, and TS was really nice to work with. There is lots of things I would do differently if I was to do this project again, and a _lot_ to learn with Typescript. Especially efficient use of Vue and TS together is something which I need to improve.

First time doing state management with Pinia. Before this project I had experience with Vuex store, but Pinia replaced Vuex as the official state management library for Vue. Felt very straight-forward and simple, most likely will keep on using Pinia in the future.

#### Backend

First time creating a REST API and first time using Kotlin for backend. Kotlin is very enjoyable, and even though the Exposed SQL Library wasn't probably the optimal one, it was absolutely usable for these purposes. Not much to say about Spring, for this project it was very simple to use. Lots to learn there as well.

This was my first time working with Docker, and I was impressed at how simple it was to use. Will absolutely learn more and utilize it in the next projects.

#### Testing

I had very limited experience with testing before the project, only one project where I used Mocha/Chai. For those reasons Mocha/Chai were used in this project too. For the very small amount of backend tests created, JUnit 5 was used. Testing obviously is extremely important in software development, and I am very happy to learn more as time goes on.

The Github Actions pipeline takes a long time to execute the tests, and I'm sure it could be optimized some way. I had used Github Actions once before.

#### TL;DR

Very rewarding project to have do, and I feel like I learned a lot during it. Lots of things to improve upon, but still fairly happy with the end result, taking into account that it was basically created in under a month on my free time.






