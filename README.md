# Setup simple postgres docker container
docker run -d
--name my_postgres_container
-e POSTGRES_USER=myuser
-e POSTGRES_PASSWORD=mypassword
-e POSTGRES_DB=mydatabase
-p 5432:5432
postgres:latest

# Setup postgres UUID
docker container start my_postgres_container
docker exec -it my_postgres_container bash
psql -U mydatabase
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

# Requirements
sudo apt install openjdk-21-jdk maven

# Run/Build app
## RUN
mvn spring-boot:run
## OR
## BUILD
mvn clean package

# DEFAULT PORT
8080

# WHEN STARTING
1. adjust application.properties based on your local setup (in directory /src/main/resources)
2. create env.properties
3. set CLOUDINARY_URL -> contact @me if want to use my cloudinary
4. super user admin account: email = admin@example.com, password = Password
5. contact @me to get full APISpecs (not creating swagger yet)

# Things to consider:
1. Better error handling
2. Build to docker
3. Swagger (OpenAPI)
