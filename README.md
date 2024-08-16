# Simple server with simple REST API

## Requirements

- Scala
- Sbt
- PostgreSQL Database

# Run

The server will run on `localhost:8080`.
Steps to run the project:

```
$ sbt
$ sbt run
```

Database connection details are found in `src/main/scala/connection/ConnectionProvider.scala`.

## Endpoints available for this API:

### Room

`GET /room` -- get all rooms in the database

`GET /room/id` -- get room by id

`PUT /room/id` -- update room by id

`DELETE /room/id` -- delete room by id

`POST /room` -- add new room

---

### Guest

`GET /guest` -- get all guests in the database

`GET /guest/id` -- get guest by id

`PUT /guest/id` -- update guest by id

`DELETE /guest/id` -- delete guest by id

`POST /guest` -- add new guest

---

### Reservation

`GET /reservation/occupancy?date=date` -- get all occupancies in the database by date

`POST /reservation` -- add new reservation

`DELETE /reservation/id` -- delete reservation by id

---

This project uses Akka Http for the server and PostgreSQL JDBC Driver for database access.

---

## Observations

1. The architecture is simple: Resource -> Service -> DAO, and the return is Resource <- Service <- DAO
2. There is an exception handler and exception reject to catch exceptions and rejections
3. `main.scala` contains the server
4. All routes are in `RoutesResources.scala`
5. Only daos contains database interaction behavior
6. Use script-db.sql to create the architecture of database

---

## This project contains postman collection at: `src/main/resources/Scala Challenge.postman_collection.json`