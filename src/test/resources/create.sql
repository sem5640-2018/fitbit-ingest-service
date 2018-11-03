create sequence hibernate_sequence;

CREATE TABLE token_map (
  id bigint NOT NULL,
  user_id VARCHAR(50),
  access_token VARCHAR(50),
  refresh_token VARCHAR(50),
  PRIMARY KEY (id)
);

CREATE TABLE client_credentials (
  id bigint NOT NULL,
  client_id VARCHAR(50),
  client_secret VARCHAR(50),
  service VARCHAR(50),
  PRIMARY KEY (id)
);