-- Arquivo de criação da tabelas (Versões Iniciais)

CREATE TABLE usuario
(
  id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
  nome VARCHAR(20) NOT NULL CHECK (LEN(nome) >= 3 AND nome <> ''),
  email VARCHAR(255) NOT NULL CHECK (email LIKE '%_@__%.__%'),
  senha VARCHAR(255) NOT NULL,
  CONSTRAINT unique_email_usuario UNIQUE (email)
);

CREATE TABLE tarefa
(
  id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
  titulo VARCHAR(100) NOT NULL CHECK (LEN(titulo) >= 3 AND titulo <> ''),
  descricao VARCHAR(500) NOT NULL CHECK (LEN(descricao) >= 10 AND descricao <> ''),
  data_vencimento DATE NOT NULL CHECK (data_vencimento >= GETDATE()),
  concluida BIT NOT NULL,
  usuario_id UNIQUEIDENTIFIER NOT NULL,
  FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);