INSERT INTO users (id, email, password, name, date_of_birth) 
VALUES ('1', 'user1@example.com', 'password', 'John Doe', '1990-01-01');

INSERT INTO user_tokens (id, user_id, token, revoked) 
VALUES ('1', '1', 'token123', false);

INSERT INTO training_session (id, user_id, start_date) 
VALUES ('1', '1', '2023-12-01');

