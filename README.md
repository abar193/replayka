# Replayka

I got tired of not knowing who visits my static blog (on GitHub pages) and what are their thoughts, so I created this service.

It records how many times which page was visited, sorts visitors into genuine users and web crawlers and provides users
with a feedback form.

While this project could be easily implemented with running grep on access.log and PHP for form input, being a true 
Java Senior I made it with Quarkus, QueryDSL and Java 17.

Project features:

- A [simple web form](src/main/resources/minification-sources/form.html) for feedback collection
- [Flyway for schema migration](src/main/resources/db/migration/)
- Somewhat [complicated SQL queries](src/main/java/me/mrabar/sq/service/BlogService.java) using QueryDSL 
- [A GitHub OIDC for (blog owner) authentication](src/main/java/me/mrabar/sq/security/GithubAuthentication.java)
- 0% code coverage 😎
- [Stored pl/pgSQL functions and custom aggregation function in postgres](src/main/resources/db/migration/V1.0.3__Bot_Estimation.sql) for visitor type evaluation

# Deployment

I used fly.io to get my free hosting for the application. 

## DB

As for the database, I had to enter my credit card (hope I won't regret it) to 
get a whole 3GB of storage. Plan is to see how long it will last and then start
optimizations, unless I'll want to, well, pay (eugh!)

```flyctl pg create
? Choose an app name (leave blank to generate one): replayka-db
automatically selected personal organization: %me%
Some regions require a paid plan (fra, maa).
See https://fly.io/plans to set up a plan.

? Select region: Amsterdam, Netherlands (ams)
? Select configuration: Specify custom configuration
? Initial cluster size - Specify at least 3 for HA 1
? Select VM size: shared-cpu-1x - 256
? Volume size 3
```

Store credentials and whatever output you got, you will need it later.

Proxy connection to DB: `fly proxy 5432 -a replayka-db`.

Connect to postgres (on `localhost:5432`) using credentials that were provided to you in `pg create`, and do:
```
    CREATE USER quarkus WITH PASSWORD 'Something Secure Here, Please';
    GRANT CONNECT ON DATABASE postgres TO quarkus;
    CREATE SCHEMA replayka AUTHORIZATION quarkus;
```

## App

Deploying application can be done using `fly.toml` and `flyctl deploy` (or `launch` for the first run?) 

Create your own GitHub App for authentication. 

Set secrets for `GITHUB_ID`, `GITHUB_SECRET` and `DB_PASS` (the one you created for your quarkus user).

# Generated boilerplate text from Quarkus below

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/survey-quarkus-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Provided Code

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
