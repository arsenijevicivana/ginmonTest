# Create Database
DROP DATABASE IF EXISTS github_stats;
CREATE DATABASE IF NOT EXISTS github_stats;
USE github_stats;

CREATE TABLE IF NOT EXISTS users (id INT NOT NULL, username VARCHAR(45) NOT NULL, url VARCHAR(45) NOT NULL, PRIMARY KEY ( id ));
CREATE TABLE IF NOT EXISTS user_language_count (user_id INT NOT NULL, lang VARCHAR(45) NOT NULL, counts INT NOT NULL); 

INSERT INTO users (id, username, url) VALUES (1, "test", "test");
INSERT INTO user_language_count (user_id, lang, counts) VALUES (1, "Java", 10);