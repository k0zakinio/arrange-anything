CREATE TABLE EVENTS (
  id SERIAL PRIMARY KEY,
  title TEXT NOT NULL,
  owner_name TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT NOW()
);