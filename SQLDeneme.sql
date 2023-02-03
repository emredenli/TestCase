CREATE TABLE users_default
(
  id int auto_increment PRIMARY KEY,
  user_email varchar(100) NULL,
  user_name varchar(100) NULL,
  user_surname varchar(50) NULL
)
charset = utf8

INSERT INTO users_default VALUES ('denli.emree@gmail.com', 'emre', 'denli')

SELECT * FROM users_default ORDER BY user_email DESC

UPDATE users_default SET user_surname='denli'

DELETE FROM users_default WHERE id='1'