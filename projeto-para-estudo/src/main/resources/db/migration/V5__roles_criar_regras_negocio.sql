CREATE OR ALTER PROCEDURE sp_SalvarRoleComPrivilegios
  (
  @p_nome VARCHAR(255),
  @p_privilegios_ids ListaDeIds READONLY,
  @p_roles_id VARCHAR(36) OUTPUT
)
AS
BEGIN
  SET NOCOUNT ON;
  SET XACT_ABORT ON;

  BEGIN TRY
      BEGIN TRANSACTION;

        -- 1. Valida se o nome da role já existe
        IF EXISTS (SELECT 1
  FROM roles
  WHERE nome = @p_nome)
            THROW 50006, 'Já existe uma role cadastrada com esse nome.', 1;

        -- 2. Valida se TODOS os privilégios enviados realmente existem
        DECLARE @countPrivilegiosEnviados INT = (SELECT COUNT(*)
  FROM @p_privilegios_ids);
        DECLARE @countPrivilegiosValidos INT = (SELECT COUNT(*)
  FROM privilegios
  WHERE id IN (SELECT id
  FROM @p_privilegios_ids));

        IF @countPrivilegiosEnviados <> @countPrivilegiosValidos
            THROW 50010, 'Um ou mais IDs de privilégios fornecidos são inválidos.', 1;

        -- 3. Insere a nova role e captura o ID gerado
        DECLARE @novoRoleId UNIQUEIDENTIFIER;
        INSERT INTO roles
    (nome)
  OUTPUT INSERTED.id INTO @novoRoleId
  VALUES
    (@p_nome);

        -- 4. Insere as associações na tabela roles_privilegios
        --    Este é o passo chave: inserimos múltiplos registros de uma vez
        --    selecionando diretamente do nosso parâmetro de tabela.
        INSERT INTO roles_privilegios
    (roles_id, privilegio_id)
  SELECT @novoRoleId, id
  FROM @p_privilegios_ids;

        -- 5. Retorna o ID da nova role como parâmetro de saída
        SET @p_roles_id = CAST(@novoRoleId AS VARCHAR(36));

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        THROW; -- Re-lança o erro original para a aplicação cliente
    END CATCH
END
GO

CREATE OR ALTER PROCEDURE sp_DeleteRolesPorNome
  (
  @p_nome VARCHAR(255)
)
AS
BEGIN
  SET NOCOUNT ON;
  SET XACT_ABORT ON;

  BEGIN TRY
        BEGIN TRANSACTION;

  IF NOT EXISTS (SELECT 1
  FROM roles
  WHERE nome = @p_nome)
    THROW 50007, 'Roles não encontrada.', 1;

        DELETE FROM roles
        WHERE nome = @p_nome;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END
GO

-- deletar por id

CREATE OR ALTER PROCEDURE sp_DeleteRolesPorId
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
  FROM roles
  WHERE id = @p_id)
        THROW 50007, 'Roles não encontrada.', 1;

        DELETE FROM roles
        WHERE id = @p_id;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END
GO

CREATE OR ALTER PROCEDURE sp_AdicionarPrivilegioARole
  (
  @p_roles_id UNIQUEIDENTIFIER,
  @p_privilegio_id UNIQUEIDENTIFIER
)
AS
BEGIN
  SET NOCOUNT ON;
  SET XACT_ABORT ON;

  BEGIN TRY
      BEGIN TRANSACTION;

    -- 1. Validar se a Role informada existe
    IF NOT EXISTS (SELECT 1
  FROM roles
  WHERE id = @p_roles_id)
          THROW 50007, 'Roles não encontrada.', 1;

    -- 2. Validar se o Privilégio informado existe
    IF NOT EXISTS (SELECT 1
  FROM privilegios
  WHERE id = @p_privilegio_id)
          THROW 50009, 'Privilégio não encontrada.', 1;

    -- 3. Inserir a associação APENAS SE ela já não existir
    IF NOT EXISTS (SELECT 1
  FROM roles_privilegios
  WHERE roles_id = @p_roles_id AND privilegio_id = @p_privilegio_id)
      BEGIN
    INSERT INTO roles_privilegios
      (roles_id, privilegio_id)
    VALUES
      (@p_roles_id, @p_privilegio_id);
  END;

    COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END
GO

CREATE OR ALTER PROCEDURE sp_RemoverPrivilegioDeRole
  (
  @p_roles_id UNIQUEIDENTIFIER,
  @p_privilegio_id UNIQUEIDENTIFIER
)
AS
BEGIN
  SET NOCOUNT ON;

  BEGIN TRY
      BEGIN TRANSACTION;

    -- 1. Validar se a Role informada existe
    IF NOT EXISTS (SELECT 1
  FROM roles
  WHERE id = @p_roles_id)
          THROW 50007, 'Roles não encontrada.', 1;

    -- 2. Validar se o Privilégio informado existe
    IF NOT EXISTS (SELECT 1
  FROM privilegios
  WHERE id = @p_privilegio_id)
          THROW 50009, 'Privilégio não encontrada.', 1;

  DELETE FROM roles_privilegios
    WHERE
        roles_id = @p_roles_id AND
    privilegio_id = @p_privilegio_id;

    COMMIT TRANSACTION;
  END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END
GO

CREATE OR ALTER PROCEDURE sp_AdicionaRoleAUsuario
  (
  @p_usuario_id UNIQUEIDENTIFIER,
  @p_roles_id UNIQUEIDENTIFIER
)
AS
BEGIN
  SET NOCOUNT ON;
  SET XACT_ABORT ON;

  BEGIN TRY
        BEGIN TRANSACTION;

                IF NOT EXISTS (SELECT 1
  FROM usuario
  WHERE id = @p_usuario_id)
        THROW 50002, 'Usuário não encontrado.', 1;

          IF NOT EXISTS (SELECT 1
  FROM roles
  WHERE id = @p_roles_id)
    THROW 50007, 'Roles não encontrada.', 1;

    -- 3. Inserir a associação APENAS SE ela já não existir
    IF NOT EXISTS (SELECT 1
  FROM usuarios_roles
  WHERE roles_id = @p_roles_id AND usuario_id = @p_usuario_id)
      BEGIN
    INSERT INTO usuarios_roles
      (usuario_id, roles_id)
    VALUES
      (@p_usuario_id, @p_roles_id);
  END;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END
GO

-- remover role de usuário

CREATE OR ALTER PROCEDURE sp_RemoverRoleDeUsuario
  (
  @p_usuario_id UNIQUEIDENTIFIER,
  @p_roles_id UNIQUEIDENTIFIER
)
AS
BEGIN
  SET NOCOUNT ON;
  SET XACT_ABORT ON;

  BEGIN TRY
        BEGIN TRANSACTION;

        IF NOT EXISTS (SELECT 1
  FROM usuario
  WHERE id = @p_usuario_id)
            THROW 50002, 'Usuário não encontrado.', 1;

        IF NOT EXISTS (SELECT 1
  FROM roles
  WHERE id = @p_roles_id)
            THROW 50007, 'Roles não encontrada.', 1;

        -- 3. Remover a associação
        DELETE FROM usuarios_roles
        WHERE roles_id = @p_roles_id AND usuario_id = @p_usuario_id;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END
GO