SET SESSION innodb_lock_wait_timeout = 5;

START TRANSACTION;

UPDATE Pedido SET total = total + 1 WHERE id = 50;

COMMIT;