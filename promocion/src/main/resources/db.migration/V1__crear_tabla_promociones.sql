CREATE TABLE promociones (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             codigo VARCHAR(30) NOT NULL UNIQUE,
                             porcentaje_descuento DECIMAL(5, 2) NOT NULL,
                             fecha_inicio DATE NOT NULL,
                             fecha_expiracion DATE NOT NULL,
                             activo BOOLEAN NOT NULL
);