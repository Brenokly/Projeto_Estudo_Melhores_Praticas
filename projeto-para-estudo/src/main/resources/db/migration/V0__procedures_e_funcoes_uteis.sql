-- Funções e Procedimentos que podem ser úteis na aplicação inteira!

-- Procedimento para Relatar Erros
CREATE PROCEDURE sp_report_erro
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    BEGIN TRY
        DECLARE @ErrorNumber INT = ERROR_NUMBER();
        DECLARE @ErrorSeverity INT = ERROR_SEVERITY();
        DECLARE @ErrorState INT = ERROR_STATE();
        DECLARE @ErrorLine INT = ERROR_LINE();
        DECLARE @ErrorProcedure NVARCHAR(128) = ERROR_PROCEDURE();
        DECLARE @ErrorMessage NVARCHAR(4000) = ERROR_MESSAGE();

        SELECT
        @ErrorNumber AS ErrorNumber,
        @ErrorSeverity AS ErrorSeverity,
        @ErrorState AS ErrorState,
        @ErrorLine AS ErrorLine,
        @ErrorProcedure AS ErrorProcedure,
        @ErrorMessage AS ErrorMessage;

    END TRY
    BEGIN CATCH
        THROW;
    END CATCH;
END;
GO