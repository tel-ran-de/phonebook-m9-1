CREATE TABLE IF NOT EXISTS my_contact
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    first_name  VARCHAR(255),
    last_name   VARCHAR(255),
    description VARCHAR(255),
    address     VARCHAR(255),
    phone       VARCHAR(255),
    email       VARCHAR(255),
    user_email  VARCHAR(255),
    CONSTRAINT pk_mycontact PRIMARY KEY (id)
);

ALTER TABLE my_contact
    ADD CONSTRAINT FK_MYCONTACT_ON_USER_EMAIL FOREIGN KEY (user_email) REFERENCES users (email);
