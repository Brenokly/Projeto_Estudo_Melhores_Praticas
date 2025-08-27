-- Arquivo de criação de regras de negócio para a tabela tarefa

-- Salvar uma tarefa

CREATE PROCEDURE sp_SalvarTarefa(
    @p_titulo VARCHAR(100),
    @p_descricao VARCHAR(500),
    @p_data_vencimento DATE,
    @p_concluida BIT,
    @p_usuario_id UNIQUEIDENTIFIER,
    @p_tarefa_id VARCHAR(36) OUTPUT
)
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        -- Validação de existência do usuário
        IF NOT EXISTS (SELECT 1
    FROM usuario
    WHERE id = @p_usuario_id)
        THROW 50002, 'Usuário não encontrado.', 1;

        -- Gera o novo ID e insere o registro
        DECLARE @novoId UNIQUEIDENTIFIER = NEWID();

        INSERT INTO tarefa
        (titulo, descricao, data_vencimento, concluida, usuario_id, id)
    VALUES
        (@p_titulo, @p_descricao, @p_data_vencimento, @p_concluida, @p_usuario_id, @novoId);

        -- Atribui o ID gerado (como string) ao parâmetro de saída
        SET @p_tarefa_id = CAST(@novoId AS VARCHAR(36));

        COMMIT TRANSACTION;

    END TRY
    BEGIN CATCH
        -- Garante que a transação seja revertida em caso de erro
        IF XACT_STATE() <> 0
        ROLLBACK TRANSACTION;

        THROW;
    END CATCH;
END;
GO

-- Buscar uma tarefa por ID

CREATE FUNCTION ufn_BuscarTarefaPorId (
    @p_id UNIQUEIDENTIFIER
)
RETURNS TABLE
AS
RETURN (
    SELECT id, titulo, descricao, data_vencimento, concluida, usuario_id
FROM tarefa
WHERE id = @p_id
);
GO

-- Deletar uma tarefa por ID

CREATE PROCEDURE sp_DeleteTarefaPorId
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
    FROM tarefa
    WHERE id = @p_id)
        THROW 50005, 'Tarefa não encontrada.', 1;

        DELETE FROM tarefa WHERE id = @p_id;

        COMMIT TRANSACTION;

    END TRY
    BEGIN CATCH
        IF XACT_STATE() <> 0
        ROLLBACK TRANSACTION;

        THROW;
    END CATCH;
END;
GO

-- Atualizar uma tarefa

CREATE PROCEDURE sp_AtualizarTarefa
    (
    @p_id UNIQUEIDENTIFIER,
    @p_titulo VARCHAR(100),
    @p_descricao VARCHAR(500),
    @p_data_vencimento DATE,
    @p_concluida BIT,
    @p_usuario_id UNIQUEIDENTIFIER
)
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        IF NOT EXISTS (SELECT 1
    FROM tarefa
    WHERE id = @p_id)
        THROW 50005, 'Tarefa não encontrada.', 1;

        UPDATE tarefa
        SET titulo = @p_titulo,
            descricao = @p_descricao,
            data_vencimento = @p_data_vencimento,
            concluida = @p_concluida,
            usuario_id = @p_usuario_id
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

-- Verificar se a tarefa existe por ID retorna um boolean

CREATE FUNCTION ufn_TarefaExistePorId (
    @p_id UNIQUEIDENTIFIER
)
RETURNS BIT
AS
BEGIN
    DECLARE @resultado BIT;

    IF EXISTS (SELECT 1
    FROM tarefa
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

-- Busca Todas as Tarefas do Sistema

CREATE FUNCTION ufn_BuscarTodasTarefasDeUmUsuario (
    @p_usuario_id UNIQUEIDENTIFIER
)
RETURNS TABLE
AS
RETURN (
    SELECT id, titulo, descricao, data_vencimento, concluida, usuario_id
FROM tarefa
WHERE usuario_id = @p_usuario_id
);
GO

-- Busca Todas as Tarefas de um usuário do Sistema com paginação

CREATE FUNCTION ufn_BuscarTodasTarefasDeUmUsuarioComPaginacao (
    @p_usuario_id UNIQUEIDENTIFIER,
    @p_pagina INT,
    @p_tamanho INT
)
RETURNS TABLE
AS
RETURN (
    SELECT id, titulo, descricao, data_vencimento, concluida, usuario_id
FROM tarefa
WHERE usuario_id = @p_usuario_id
ORDER BY titulo
    OFFSET (@p_pagina - 1) * @p_tamanho ROWS
    FETCH NEXT @p_tamanho ROWS ONLY
);
GO