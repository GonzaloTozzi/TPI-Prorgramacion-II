DELIMITER //

CREATE PROCEDURE UpdatePedidoWithRetry (
    IN p_id BIGINT,
    IN p_aumento DECIMAL(12, 2)
)
BEGIN
    -- 1. TODAS LAS DECLARACIONES DE VARIABLES PRIMERO
    DECLARE v_retry_count INT DEFAULT 0;
    DECLARE v_done INT DEFAULT 0;
    
    -- 2. TODOS LOS HANDLERS DESPUÉS DE LAS VARIABLES
    -- Handler específico para DEADLOCK
    DECLARE CONTINUE HANDLER FOR 1213
    BEGIN
        SET v_retry_count = v_retry_count + 1;
        IF v_retry_count < 3 THEN
            SELECT CONCAT('Deadlock detectado. Reintentando (', v_retry_count, '/2)...') AS Log;
            ROLLBACK;
            DO SLEEP(0.5);
        ELSE
            SELECT 'Fallo al actualizar después de 2 reintentos.' AS Error;
            SIGNAL SQLSTATE '40001' SET MESSAGE_TEXT = 'Deadlock retry limit exceeded.';
        END IF;
    END;
    
    -- Handler para otros errores SQL
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'Error en la transacción. Rollback ejecutado.' AS Error;
        RESIGNAL;
    END;
    
    -- 3. CÓDIGO EJECUTABLE AL FINAL
    retry_loop: WHILE v_retry_count < 3 DO
        START TRANSACTION;
        
        UPDATE Pedido
        SET total = total + p_aumento
        WHERE id = p_id;
        
        COMMIT;
        
        -- Si llegamos aquí, la transacción fue exitosa
        LEAVE retry_loop;
        
    END WHILE retry_loop;
    
END//

DELIMITER ;


SET SESSION innodb_lock_wait_timeout = 5;



