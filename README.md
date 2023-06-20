# Games Library

## Description

The following application, using the public Steam API, collects (regularly) and stores data about applications published on Steam. Users of the web application, after registering and logging in to the website, have the option of rating and reviewing these applications.
An additional function is to create a list of the user's favorite applications.

## Component applications and development

### Component applications:
* React frontend app (TypeScript, React, Node.js, MUI)
    - https://github.com/damianboguszpl/gameslibrary-frontend
* Spring backend app (Java, Spring, Auth0, Hibernate, Spring Security, Lombok, MySQL database)
    - https://github.com/kardahim/gameslibrary

### Created as a small college project through the collaboration of:
* https://github.com/kardahim
* https://github.com/damianboguszpl

### Division of work

#### Joint work:
* Server application:
    - database modeling
    - preparing API for frontend app

#### kardahim
* Server application:
    - planned tasks module
    - creation of unit tests and an integration test
* Web application:
    - displaying the application catalog (including division into categories) and user reviews
    - management of user's 'favorite' applications

#### damianboguszpl
* Server application:
    - collecting and storing Steam API data
    - authorization, authentication and securing access to the API with rules based on user rights
* Web application:
    - Preparation of registration, login and session service
    - user reviews' management
