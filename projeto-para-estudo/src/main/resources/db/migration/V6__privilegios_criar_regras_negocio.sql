CREATE OR ALTER PROCEDURE sp_SalvarPrivilegios
  (
  @p_nome VARCHAR(255),
  @p_privilegios_id VARCHAR(36) OUTPUT
)
AS
BEGIN
  SET NOCOUNT ON;
  SET XACT_ABORT ON;

  BEGIN TRY
        BEGIN TRANSACTION;

        IF EXISTS (SELECT 1
  FROM privilegios
  WHERE nome = @p_nome)
          THROW 50008, 'Já existe um privilégio cadastrada com esse nome.', 1;

          DECLARE @novoId UNIQUEIDENTIFIER

        INSERT INTO privilegios
    (nome)
  OUTPUT INSERTED.id INTO @novoId
  VALUES
    (@p_nome);

        SET @p_privilegios_id = CAST(@novoID AS VARCHAR(36));

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END
GO

CREATE OR ALTER PROCEDURE sp_DeletePrivilegiosPorNome
  (
  @p_nome VARCHAR(255)
)
AS
BEGIN
  SET NOCOUNT ON;
  SET XACT_ABORT ON;

  BEGIN TRY
        BEGIN TRANSACTION;

        -- 2. Validação movida para dentro da transação
        IF NOT EXISTS (SELECT 1
  FROM privilegios
  WHERE nome = @p_nome)
            THROW 50009, 'Privilégio não encontrado.', 1;

        DELETE FROM privilegios
        WHERE nome = @p_nome;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END
GO
