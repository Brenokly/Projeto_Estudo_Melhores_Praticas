-- Operações Relacionadas a Usuário seguindo as melhoras práticas

-- Operação de salvamento de um usuário

CREATE OR ALTER PROCEDURE sp_SalvarUsuario
    (
    @p_nome VARCHAR(20),
    @p_email VARCHAR(255),
    @p_senha_hash VARCHAR(255),
    @p_usuario_id VARCHAR(36) OUTPUT
)
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        IF EXISTS (SELECT 1
    FROM usuario
    WHERE email = @p_email)
            THROW 50001, 'O e-mail informado já está cadastrado no sistema.', 1;

        DECLARE @novoId UNIQUEIDENTIFIER;

        INSERT INTO usuario
        (nome, email, senha)
    OUTPUT INSERTED.id INTO @novoId
    VALUES
        (@p_nome, @p_email, @p_senha_hash);

        -- Atribui o ID gerado (como string) ao parâmetro de saída
        SET @p_usuario_id = CAST(@novoId AS VARCHAR(36));

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        THROW;
    END CATCH
END;
GO

-- Busca de um usuário por Email

CREATE FUNCTION ufn_BuscarUsuarioPorEmail (
    @p_email VARCHAR(255)
)
RETURNS TABLE
AS
RETURN (
    SELECT id, nome, email, senha
FROM usuario
WHERE email = @p_email
);
GO

-- Buscar Usuário por ID

CREATE FUNCTION ufn_BuscarUsuarioPorId (
    @p_id UNIQUEIDENTIFIER
)
RETURNS TABLE
AS
RETURN (
    SELECT id, nome, email, senha
FROM usuario
WHERE id = @p_id
);
GO

-- Delete Usuário por ID

CREATE PROCEDURE sp_DeleteUsuarioPorId
    (
    @p_id UNIQUEIDENTIFIER
)
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        IF NOT EXISTS (SELECT 1
    FROM usuario
    WHERE id = @p_id)
        THROW 50002, 'Usuário não encontrado.', 1;

        DELETE FROM usuario WHERE id = @p_id;

        COMMIT TRANSACTION;

    END TRY
    BEGIN CATCH
        IF XACT_STATE() <> 0
        ROLLBACK TRANSACTION;
        THROW;
    END CATCH;
END;
GO

-- Atualizar Usuário

CREATE PROCEDURE sp_AtualizarDadosUsuario
    (
    @p_id UNIQUEIDENTIFIER,
    @p_nome VARCHAR(20)
)
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        IF NOT EXISTS (SELECT 1
    FROM usuario
    WHERE id = @p_id)
        THROW 50002, 'Usuário não encontrado.', 1;

        UPDATE usuario
        SET nome = @p_nome
        WHERE id = @p_id;

        COMMIT TRANSACTION;

    END TRY
    BEGIN CATCH
        IF XACT_STATE() <> 0
        ROLLBACK TRANSACTION;

        THROW;
    END CATCH;
END;
GO

-- Atualizar senha do Usuário

CREATE PROCEDURE sp_AtualizarSenhaUsuario
    (
    @p_id UNIQUEIDENTIFIER,
    @p_senha_atual_parametro VARCHAR(255),
    @p_nova_senha_hash VARCHAR(255),
    @p_confirmacao_senha_hash VARCHAR(255)
)
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        DECLARE @p_senha_atual_buscada VARCHAR(255);

        SELECT @p_senha_atual_buscada = senha
    FROM usuario
    WHERE id = @p_id;

        IF @p_senha_atual_parametro <> @p_senha_atual_buscada
        THROW 50004, 'A senha atual está incorreta.', 1;

        IF NOT EXISTS (SELECT 1
    FROM usuario
    WHERE id = @p_id)
        THROW 50002, 'Usuário não encontrado.', 1;

        IF @p_nova_senha_hash <> @p_confirmacao_senha_hash
            THROW 50003, 'A nova senha e a confirmação da senha não coincidem.', 1;

        UPDATE usuario
        SET senha = @p_nova_senha_hash
        WHERE id = @p_id;

        COMMIT TRANSACTION;

    END TRY
    BEGIN CATCH
        IF XACT_STATE() <> 0
        ROLLBACK TRANSACTION;

        THROW;
    END CATCH;
END;
GO

-- Verificar se o usuário existe por Email retorna um boolean

CREATE FUNCTION ufn_UsuarioExistePorEmail (
    @p_email VARCHAR(255)
)
RETURNS BIT
AS
BEGIN
    DECLARE @resultado BIT;

    IF EXISTS (SELECT 1
    FROM usuario
    WHERE email = @p_email)
    BEGIN
        SET @resultado = 1;
    END
    ELSE
    BEGIN
        SET @resultado = 0;
    END

    RETURN @resultado;
END;
GO

-- Verifica se o Usuário existe pelo ID retorna um boolean

CREATE FUNCTION ufn_UsuarioExistePorId (
    @p_id UNIQUEIDENTIFIER
)
RETURNS BIT
AS
BEGIN
    DECLARE @resultado BIT;

    IF EXISTS (SELECT 1
    FROM usuario
    WHERE id = @p_id)
    BEGIN
        SET @resultado = 1;
    END
    ELSE
    BEGIN
        SET @resultado = 0;
    END

    RETURN @resultado;
END;
GO

-- Busca Todos os Usuários do Sistema

CREATE FUNCTION ufn_BuscarTodosUsuarios ()
RETURNS TABLE
AS
RETURN (
    SELECT id, nome, email
FROM usuario
);
GO

-- Busca Todos os Usuários do Sistema com paginação

CREATE FUNCTION ufn_BuscarTodosUsuariosComPaginacao (
    @p_pagina INT,
    @p_tamanho INT
)
RETURNS TABLE
AS
RETURN (
    SELECT id, nome, email
FROM usuario
ORDER BY nome
    OFFSET (@p_pagina - 1) * @p_tamanho ROWS
    FETCH NEXT @p_tamanho ROWS ONLY
);
GO