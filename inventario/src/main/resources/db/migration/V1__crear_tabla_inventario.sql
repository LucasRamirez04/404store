CREATE TABLE IF NOT EXISTS inventario (
          id INT AUTO_INCREMENT PRIMARY KEY,
          producto_id INT NOT NULL UNIQUE,
          ubicacion VARCHAR(255) NOT NULL
    );