DROP TABLE IF EXISTS ingredients;
DROP TABLE IF EXISTS steps;
DROP TABLE IF EXISTS recipes;

CREATE TABLE IF NOT EXISTS recipes (
   id INT NOT NULL AUTO_INCREMENT,
   user_id INT NOT NULL,
   name varchar(250) NOT NULL,
   description varchar(500),
   creation_date timestamp NOT NULL,
   date_modified timestamp NOT NULL,
   PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS ingredients (
   id INT NOT NULL AUTO_INCREMENT,
   recipe_id INT NOT NULL,
   name VARCHAR(250) NOT NULL,
   measurement VARCHAR(250) NOT NULL,
   PRIMARY KEY (id),
   FOREIGN KEY (recipe_id) REFERENCES recipes(id)
   ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS steps (
   id INT NOT NULL AUTO_INCREMENT,
   recipe_id INT NOT NULL,
   step_number INT NOT NULL,
   description VARCHAR(500) NOT NULL,
   PRIMARY KEY (id),
   FOREIGN KEY (recipe_id) REFERENCES recipes(id)
   ON DELETE CASCADE
);