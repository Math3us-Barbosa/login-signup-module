CREATE TABLE usuarios (
    id             UUID PRIMARY KEY,
    nome           VARCHAR(150) NOT NULL,
    email          VARCHAR(180) NOT NULL,
    senha_hash     VARCHAR(60) NOT NULL,
    telefone       VARCHAR(20),
    role           VARCHAR(20) NOT NULL,
    ativo          BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em      TIMESTAMP NOT NULL,
    atualizado_em  TIMESTAMP NOT NULL,
    CONSTRAINT uk_usuarios_email UNIQUE (email)
);
