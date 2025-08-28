-- Operações Relacionadas a Usuário seguindo as melhoras práticas

-- Operação de salvamento de um usuário

CREATE OR ALTER PROCEDURE sp_SalvarUsuario
    (
    @p_nome VARCHAR(20),
    @p_email VARCHAR(255),
    @p_senha_hash VARCHAR(255),
    @p_roles ListaDeIds READONLY,
    @p_usuario_id UNIQUEIDENTIFIER OUTPUT
)
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        -- PASSO 1: VALIDAR TODOS OS INPUTS PRIMEIRO
        IF EXISTS (SELECT 1
    FROM usuario
    WHERE email = @p_email)
            THROW 50001, 'O e-mail informado já está cadastrado no sistema.', 1;

        DECLARE @countRolesEnviadas INT = (SELECT COUNT(*)
    FROM @p_roles);
        IF @countRolesEnviadas > 0 -- Só valida se alguma role foi enviada
        BEGIN
        DECLARE @countRolesValidas INT = (SELECT COUNT(*)
        FROM roles
        WHERE id IN (SELECT id
        FROM @p_roles));
        IF @countRolesEnviadas <> @countRolesValidas
                THROW 50012, 'Um ou mais IDs de roles fornecidos são inválidos.', 1;
    END

        -- PASSO 2: EXECUTAR AS INSERÇÕES
        DECLARE @novoId UNIQUEIDENTIFIER;

        INSERT INTO usuario
        (nome, email, senha)
    OUTPUT INSERTED.id INTO @novoId
    VALUES
        (@p_nome, @p_email, @p_senha_hash);

        -- Atribui as roles ao usuário (se houver alguma)
        IF @countRolesEnviadas > 0
        BEGIN
        INSERT INTO usuarios_roles
            (usuario_id, roles_id)
        SELECT @novoId, id
        FROM @p_roles;
    END

        SET @p_usuario_id = @novoId;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END;
GO

CREATE FUNCTION ufn_BuscarRolesDeUsuario (
    @p_usuario_id UNIQUEIDENTIFIER
)
RETURNS TABLE
AS
RETURN (
    SELECT r.id, r.nome
FROM usuarios_roles ur
    INNER JOIN roles r ON ur.roles_id = r.id
WHERE ur.usuario_id = @p_usuario_id
);
GO

-- Busca de um usuário por Email

CREATE OR ALTER PROCEDURE sp_BuscarUsuarioComRolesPorEmail
    (
    @p_email VARCHAR(255)
)
AS
BEGIN
    SET NOCOUNT ON;
    -- serve para evitar mensagens desnecessárias
    SET XACT_ABORT ON;
    -- serve para garantir que a transação seja abortada em caso de erro

    BEGIN TRY

    DECLARE @usuarioId UNIQUEIDENTIFIER;

    -- Passo 1: Apenas atribui o valor à variável, sem retornar dados.
    SELECT
        @usuarioId = id
    FROM
        usuario
    WHERE
        email = @p_email;

    -- Agora, se um usuário foi encontrado, retornamos os dois conjuntos de resultados.
    IF @usuarioId IS NULL
    BEGIN
        ;THROW 50005, 'Usuário não encontrado.', 1;
    END

    -- Passo 2: Retorna os dados completos do usuário como o primeiro resultado.
    SELECT
        id,
        nome,
        email,
        senha
    FROM
        usuario
    WHERE
        id = @usuarioId;

    -- Passo 3: Retorna as roles do usuário como o segundo resultado.
    SELECT
        *
    FROM
        ufn_BuscarRolesDeUsuario(@usuarioId);

    END TRY
    BEGIN CATCH
        IF XACT_STATE() <> 0
        ROLLBACK TRANSACTION;
        THROW;
    END CATCH;
END
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
