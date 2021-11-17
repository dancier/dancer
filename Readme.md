# Setup local Database

1. Run the database:

    docker run --publish 5432:5432 --name dancer-db  -e POSTGRES_PASSWORD=password -d postgres
3. 