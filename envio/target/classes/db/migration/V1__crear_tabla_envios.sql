CREATE TABLE envios (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    direccion       VARCHAR(200)    NOT NULL,
    costo_envio     DOUBLE          NOT NULL,
    fecha_envio     DATE            NOT NULL,
    fecha_recibido  DATE,
    estado_envio    VARCHAR(20)     NOT NULL,
    pedido_id       INT             NOT NULL
);
