CREATE TABLE person (
  id SERIAL PRIMARY KEY,
  last_name TEXT NOT NULL,
  first_name TEXT NOT NULL,
  email TEXT NULL
);

CREATE TABLE incident (
  id SERIAL PRIMARY KEY,
  title TEXT NOT NULL,
  description TEXT NOT NULL,
  severity TEXT NOT NULL,
  owner_id INTEGER NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  FOREIGN KEY (owner_id) REFERENCES person(id)
);

CREATE INDEX idx_person_id ON person(id);
CREATE INDEX idx_incident_id ON incident(id);

CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE INDEX idx_incident_trgm ON incident USING gin (title gin_trgm_ops, description gin_trgm_ops, severity gin_trgm_ops);
CREATE INDEX idx_person_trgm ON person USING gin (last_name gin_trgm_ops, first_name gin_trgm_ops, email gin_trgm_ops);
CREATE INDEX idx_incident_created_at ON incident(created_at DESC);
CREATE INDEX idx_incident_owner_id ON incident(owner_id);