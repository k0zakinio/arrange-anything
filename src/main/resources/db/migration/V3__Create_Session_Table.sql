CREATE TABLE SESSION (
  id SERIAL PRIMARY KEY,
  session_id VARCHAR NOT NULL,
  created_at TIMESTAMP DEFAULT NOW(),
  user_id INTEGER,
  FOREIGN KEY (user_id) REFERENCES USERS(id)
);