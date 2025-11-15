USE tpi;

SET SESSION innodb_lock_wait_timeout = 5;

CALL UpdatePedidoWithRetry(50, 1000.00);

SELECT total FROM Pedido WHERE id = 50;