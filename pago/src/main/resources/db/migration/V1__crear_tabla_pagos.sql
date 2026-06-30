CREATE TABLE IF NOT EXISTS pagos (
     id INT AUTO_INCREMENT PRIMARY KEY,
     monto_total DOUBLE NOT NULL,
     metodo_pago VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
    fecha_pago DATE NOT NULL,
    estado_pago VARCHAR(50) NOT NULL,
    pedido_id INT NOT NULL
    );