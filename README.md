# phonebook-m9-1
The project of 9-1 morning group

Tutorial "Connection to Postgres Database"

1.  Install PostgreSQL to your local computer and connect to the PostgreSQL database server from a client application such as psql or pgAdmin.
2. Using psql or pgAdmin:
    Create a new 'phonebook' database using basic details (username=postgres; password=123456) or your own. 
    If you want to use your own details, it is necessary to update it in phonebook-api project (phonebook-api/src/main/resources/application.properties).
    spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
    spring.datasource.username=your_user_name
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=create_OR_none
3. Run phonebook-m9-1 project

#fields validation
User registration flow is validated. All fields must not be empty.
Field email is validated with pattern.
