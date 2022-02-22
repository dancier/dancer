# Dancer
This is the backend for dancier.

## Working locally

### Setting up database

Just start the database with the provided docker-compose.yml.
It will expose the port locally and will also setup a GUI via pg-admin.
````sh
docker-compose up -d
````
you can now access the database gui with your browser:

[PG-Admin](http://localhost:5050)

|User| Pass   |
|----|--------|
|admin@dancier.net| secret |

Here can configure the connection the postgres instance:

|Hostname|database|user|pass|
|--------|--------|----|----|
|dancer-db|dancer|dancer|dancer|

### Run the dancer locally
````shell
mvn spring-boot:run
````
This will bootstrap the database. You can start using it.
See the api-documentation to see what you can do:

[OpenApi](https://editor.swagger.io/?url=https%3A%2F%2Fraw.githubusercontent.com%2Fdancier%2Fdancer%2Fmaster%2Fopenapi.yml)

### Building

``mvn clean install`

This will also run the test _and_ integration tests.

You can then inspect the test-coverage:

[Show test coverage](.target/site/jacoco/index.html)

### local mailing
Wenn working locally the mailing-system of the backend is configured to _not_ send the mails, but to dump them only to the log.
This is being achieved by using spring profiles. The profile here is 'dev'.
So make sure, to not change the profile unintentionally.
## About the staging environment
Until we are not live, this is the only other environment next to working locally.
The corresponding Profile is 'staging'
### mailing in the staging environment
Mail will be send via an SMTP-Server. But the target address of all mails are rewritten to always go to one configurable mail-address.