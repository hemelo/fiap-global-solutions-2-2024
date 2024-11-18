ALTER TABLE polo_fornecedor
    ADD id_energia INTEGER;

ALTER TABLE polo_fornecedor
    ADD capacidade_populacional_maxima NUMBER DEFAULT 0;

ALTER TABLE polo_fornecedor
    ADD capacidade_populacional NUMBER DEFAULT 0;

ALTER TABLE polo_fornecedor
    ADD CONSTRAINT fk_id_energia  FOREIGN KEY  (id_energia) REFERENCES energia (id);


CREATE TABLE fornecimento_energetico
(
    id            NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    comunidade_id NUMBER NOT NULL,
    polo_id       NUMBER NOT NULL,
    populacao     NUMBER NOT NULL,

    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_comunidade_id FOREIGN KEY  (comunidade_id) REFERENCES comunidade (id),
    CONSTRAINT fk_polo_id FOREIGN KEY  (polo_id) REFERENCES polo_fornecedor (id)
);

-- Triggers

CREATE OR REPLACE TRIGGER fornecimento_energetico_before_update BEFORE UPDATE ON fornecimento_energetico FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/