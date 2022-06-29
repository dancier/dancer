# Dancer
This is the backend for dancier.

## Working locally

### Setting up the database

Just start the database with the provided docker-compose.yml.
It will expose the port locally and will also setup a GUI via pg-admin.
You set up the database and close it to change admin rights. Then you open it again.
````sh
docker-compose up -d
docker-compose down
cd volumes/
ls -l
docker logs dancer_pg-admin_1
sudo chown 5050:5050 -Rv pg-admin-data/
cd ..
docker-compose up -d

````
You can now access the database GUI with your browser:

[PG-Admin](http://localhost:5050)

|User| Pass   |
|----|--------|
|admin@dancier.net| secret |

Here you can configure the connection to the postgres instance:

|Hostname|database|user|pass|
|--------|--------|----|----|
|dancer-db|dancer|dancer|dancer|

### Run the dancer locally
````shell
./mvnw spring-boot:run
````
This will bootstrap the database. You can start using it.
See the api-documentation to see what you can do:

[OpenApi](https://editor.swagger.io/?url=https%3A%2F%2Fraw.githubusercontent.com%2Fdancier%2Fdancer%2Fmaster%2Fopenapi.yml)

### Building

``./mvnw clean install`

This will also run the test _and_ integration tests.

You can then inspect the test-coverage:

[Show test coverage in target/site/jacoco/index.html](.target/site/jacoco/index.html)

### Local Mailing
When working locally the mailing-system of the backend is configured to _not_ send the mails, but to dump them only to the log.
This is achieved by using spring profiles. The profile here is 'dev'.
So make sure to not change the profile unintentionally.
## About the staging environment
As long as we are not live, this is the only other environment next to working locally.
The corresponding Profile is 'staging'

### Mailing in the Staging Environment
Mail will be send via an SMTP-Server. But the target addresses of all mails are rewritten to always go to a configurable mail-address.
