INSERT INTO energia (nome, tipo, descricao, unidade) VALUES ('Energia Solar', 'Renovável', 'Energia obtida a partir do sol', 'kWh');
INSERT INTO energia (nome, tipo, descricao, unidade) VALUES ('Energia Eólica', 'Renovável', 'Energia obtida a partir do vento', 'kWh');
INSERT INTO energia (nome, tipo, descricao, unidade) VALUES ('Energia Hidrelétrica', 'Renovável', 'Energia obtida a partir da água', 'kWh');
INSERT INTO energia (nome, tipo, descricao, unidade) VALUES ('Energia Nuclear', 'Não Renovável', 'Energia obtida a partir de reações nucleares', 'kWh');
INSERT INTO energia (nome, tipo, descricao, unidade) VALUES ('Energia Geotérmica', 'Renovável', 'Energia obtida a partir do calor da Terra', 'kWh');
INSERT INTO energia (nome, tipo, descricao, unidade) VALUES ('Energia de Biomassa', 'Renovável', 'Energia obtida a partir de matéria orgânica', 'kWh');
INSERT INTO energia (nome, tipo, descricao, unidade) VALUES ('Energia das Marés', 'Renovável', 'Energia obtida a partir do movimento das marés', 'kWh');
INSERT INTO energia (nome, tipo, descricao, unidade) VALUES ('Energia de Ondas', 'Renovável', 'Energia obtida a partir do movimento das ondas', 'kWh');
INSERT INTO energia (nome, tipo, descricao, unidade) VALUES ('Energia de Carvão', 'Não Renovável', 'Energia obtida a partir da queima de carvão', 'kWh');
INSERT INTO energia (nome, tipo, descricao, unidade) VALUES ('Energia de Gás Natural', 'Não Renovável', 'Energia obtida a partir da queima de gás natural', 'kWh');

INSERT INTO fornecedor (nome, cnpj, endereco, descricao) VALUES ('Fornecedor Solar', '34567890000123', 'Praça do Sol, 789', 'Fornecedor de energia solar');
INSERT INTO fornecedor (nome, cnpj, endereco, descricao) VALUES ('Fornecedor Oceânico', '56789012000145', 'Alameda do Mar, 202', 'Fornecedor de energia das ondas');
INSERT INTO fornecedor (nome, cnpj, endereco, descricao) VALUES ('Fornecedor Vulcânico', '67890123000156', 'Estrada do Vulcão, 303', 'Fornecedor de energia geotérmica');
INSERT INTO fornecedor (nome, cnpj, endereco, descricao) VALUES ('Fornecedor Eólico', '78901234000167', 'Rodovia dos Ventos, 404', 'Fornecedor de energia eólica');
INSERT INTO fornecedor (nome, cnpj, endereco, descricao) VALUES ('Fornecedor Hidrelétrico', '89012345000178', 'Rua da Água, 505', 'Fornecedor de energia hidrelétrica');
INSERT INTO fornecedor (nome, cnpj, endereco, descricao) VALUES ('Fornecedor Biomassa', '90123456000189', 'Avenida da Floresta, 606', 'Fornecedor de energia de biomassa');
INSERT INTO fornecedor (nome, cnpj, endereco, descricao) VALUES ('Fornecedor Nuclear', '01234567000190', 'Praça do Átomo, 707', 'Fornecedor de energia nuclear');

INSERT INTO polo_fornecedor (nome, endereco, latitude, longitude, id_fornecedor, id_energia, capacidade_populacional, capacidade_populacional_maxima)
VALUES ('Polo Solar', 'Praça do Sol, 789', -19.916681, -43.934493, 1, 1, 2000000, 2000000);

INSERT INTO polo_fornecedor (nome, endereco, latitude, longitude, id_fornecedor, id_energia, capacidade_populacional, capacidade_populacional_maxima)
VALUES ('Polo Oceânico', 'Alameda do Mar, 202', -12.971399, -38.501305, 2, 7, 180000, 180000);

INSERT INTO polo_fornecedor (nome, endereco, latitude, longitude, id_fornecedor, id_energia, capacidade_populacional, capacidade_populacional_maxima)
VALUES ('Polo Vulcânico', 'Estrada do Vulcão, 303', -8.047562, -34.877001, 3, 5, 1400000, 1400000);

INSERT INTO polo_fornecedor (nome, endereco, latitude, longitude, id_fornecedor, id_energia, capacidade_populacional, capacidade_populacional_maxima)
VALUES ('Polo Eólico', 'Rodovia dos Ventos, 404', -3.717220, -38.543369, 4, 2, 1600, 1600);

INSERT INTO polo_fornecedor (nome, endereco, latitude, longitude, id_fornecedor, id_energia, capacidade_populacional, capacidade_populacional_maxima)
VALUES ('Polo Hidrelétrico', 'Rua da Água, 505', -1.455833, -48.490197, 5, 3, 1700000, 1700000);

INSERT INTO polo_fornecedor (nome, endereco, latitude, longitude, id_fornecedor, id_energia, capacidade_populacional, capacidade_populacional_maxima)
VALUES ('Polo Biomassa', 'Avenida da Floresta, 606', -25.428954, -49.267137, 6, 6, 130000, 130000);

INSERT INTO polo_fornecedor (nome, endereco, latitude, longitude, id_fornecedor, id_energia, capacidade_populacional, capacidade_populacional_maxima)
VALUES ('Polo Nuclear', 'Praça do Átomo, 707', -30.034647, -51.217658, 7, 4, 19000000, 19000000);

INSERT INTO comunidade (nome, descricao, populacao, localizacao, latitude, longitude) VALUES ('Comunidade Solar', 'Comunidade que utiliza energia solar', 5000, 'Região Norte', -3.119027, -60.021731); -- Petrópolis AM
INSERT INTO comunidade (nome, descricao, populacao, localizacao, latitude, longitude) VALUES ('Comunidade Eólica', 'Comunidade que utiliza energia eólica', 3000, 'Região Nordeste', -8.047562, -34.877001); -- Recife BA
INSERT INTO comunidade (nome, descricao, populacao, localizacao, latitude, longitude) VALUES ('Comunidade Hidrelétrica', 'Comunidade que utiliza energia hidrelétrica', 7000, 'Região Sudeste', -23.550520, -46.633308); -- São Paulo SP
INSERT INTO comunidade (nome, descricao, populacao, localizacao, latitude, longitude) VALUES ('Comunidade Nuclear', 'Comunidade que utiliza energia nuclear', 2000, 'Região Sul', -30.034647, -51.217658); -- Porto Alegre RS
INSERT INTO comunidade (nome, descricao, populacao, localizacao, latitude, longitude) VALUES ('Comunidade Geotérmica', 'Comunidade que utiliza energia geotérmica', 1500, 'Região Centro-Oeste', -15.780148, -47.929170); -- Brasilia
INSERT INTO comunidade (nome, descricao, populacao, localizacao, latitude, longitude) VALUES ('Comunidade de Biomassa', 'Comunidade que utiliza energia de biomassa', 4000, 'Região Norte', -1.455833, -48.490197);
INSERT INTO comunidade (nome, descricao, populacao, localizacao, latitude, longitude) VALUES ('Comunidade das Marés', 'Comunidade que utiliza energia das marés', 2500, 'Região Nordeste', -12.971399, -38.501305);
INSERT INTO comunidade (nome, descricao, populacao, localizacao, latitude, longitude) VALUES ('Comunidade de Ondas', 'Comunidade que utiliza energia das ondas', 3500, 'Região Sudeste', -22.906847, -43.172896);
INSERT INTO comunidade (nome, descricao, populacao, localizacao, latitude, longitude) VALUES ('Comunidade de Carvão', 'Comunidade que utiliza energia de carvão', 6000, 'Região Sul', -25.428954, -49.267137);
INSERT INTO comunidade (nome, descricao, populacao, localizacao, latitude, longitude) VALUES ('Comunidade de Gás Natural', 'Comunidade que utiliza energia de gás natural', 4500, 'Região Centro-Oeste', -20.469710, -54.620121);
