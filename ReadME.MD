# Data Ware House Project

# Environment 
- mysql  Ver 8.0.27 for Linux on x86_64 (MySQL Community Server - GPL)
- Eclipse Version: 2021-03 (4.19)
- Java SE1.8

# Setting up the environment and necessary changes in the code
Assuming Master And Transaction data has been loaded and database has been selected
## Step 1: Create the tables for the star Schema
- run the provided createDW.sql file to create the star schema


## Step 2: Loading the project into Eclipse
- Copy the bin and src folders to your project


## Step 3 : Ensuring successfull connection to the MySQL
- Open DB_Handle.java
- Edit line 15,16,17 respectively
- Ensure that the correct Database name, Username and password have been entered
- Build Project


## Step 4: Run as Java application
- If done correctly the code will prompt with successfull connection of the stream handlers.
- if the above step were successful the application will simulate the Mesh Join and start loading data in to the star schema


## Step 5: Running OLAP Queries
- After the application prompts for completion
- Run the OLAP queries using the mysql Command line or the workbench 
