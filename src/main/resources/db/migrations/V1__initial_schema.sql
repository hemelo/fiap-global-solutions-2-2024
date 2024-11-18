-- Flyway Migration Script: V1__initial_schema.sql

CREATE TABLE contato
(
    id       INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    telefone VARCHAR2(255) NOT NULL,
    email    VARCHAR2(255) NOT NULL
);

CREATE TABLE energia
(
    id         INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome       VARCHAR2(255) NOT NULL,
    tipo       VARCHAR2(255),
    descricao  VARCHAR2(255),
    unidade    VARCHAR2(255),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE comunidade
(
    id         INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome       VARCHAR2(255) NOT NULL,
    descricao  VARCHAR2(255),
    latitude   NUMBER,
    longitude  NUMBER,
    populacao  NUMBER,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE usuario
(
    id         INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    login      VARCHAR2(255) UNIQUE NOT NULL,
    nome       VARCHAR2(255) NOT NULL,
    email      VARCHAR2(255) UNIQUE NOT NULL,
    senha      VARCHAR2(255) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE fornecedor
(
    id         INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome       VARCHAR2(255) NOT NULL,
    descricao  VARCHAR2(255) NOT NULL,
    endereco   VARCHAR2(255) NOT NULL,
    cnpj       VARCHAR2(255) UNIQUE NOT NULL,
    id_contato INTEGER NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_id_contato FOREIGN KEY  (id_contato) REFERENCES contato (id)

);

CREATE TABLE polo_fornecedor
(
    id            INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome          VARCHAR2(255) NOT NULL,
    endereco      VARCHAR2(255) NOT NULL,
    latitude      NUMBER,
    longitude     NUMBER,
    id_fornecedor INTEGER NOT NULL,

    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_id_fornecedor FOREIGN KEY  (id_fornecedor) REFERENCES fornecedor (id)
);

-- Triggers

CREATE
OR REPLACE TRIGGER energia_before_update
BEFORE
UPDATE ON energia
    FOR EACH ROW
BEGIN
    :NEW
.updated_at := CURRENT_TIMESTAMP;
END;
/

CREATE
OR REPLACE TRIGGER comunidade_before_update
BEFORE
UPDATE ON comunidade
    FOR EACH ROW
BEGIN
    :NEW
.updated_at := CURRENT_TIMESTAMP;
END;
/

CREATE
OR REPLACE TRIGGER usuario_before_update
BEFORE
UPDATE ON usuario
    FOR EACH ROW
BEGIN
    :NEW
.updated_at := CURRENT_TIMESTAMP;
END;
/

CREATE
OR REPLACE TRIGGER fornecedor_before_update
BEFORE
UPDATE ON fornecedor
    FOR EACH ROW
BEGIN
    :NEW
.updated_at := CURRENT_TIMESTAMP;
END;
/

CREATE
OR REPLACE TRIGGER polo_fornecedor_before_update
BEFORE
UPDATE ON polo_fornecedor
    FOR EACH ROW
BEGIN
    :NEW
.updated_at := CURRENT_TIMESTAMP;
END;
/
