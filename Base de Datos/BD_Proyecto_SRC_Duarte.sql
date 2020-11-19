-- CREAMOS LA BASE DE DATOS
CREATE DATABASE src_duarte;

-- DROPEAR EN CASO DE SER NECESARIO
DROP DATABASE src_duarte;

-- ACTIVAMOS LA BASE DE DATOS
USE src_duarte;

-- CREACION DE TABLAS
CREATE TABLE clientes
(
cli_id INT NOT NULL,
cli_nombre VARCHAR(50) NOT NULL unique,
cli_celular INT NOT NULL,
cli_direccion VARCHAR(100) NOT NULL
);
CREATE TABLE pedidos
(
ped_id INT NOT NULL,
ped_tipo VARCHAR(13) NOT NULL,
ped_categoria VARCHAR(13) NOT NULL,
ped_precio DOUBLE NOT NULL,
ped_fechaEntrega DATE NOT NULL,
ped_descripcion VARCHAR(500) NOT NULL,
ped_comentario VARCHAR(300) default(null),
ped_cli_id INT NOT NULL,
ped_estado varchar(11) NOT NULL
);

-- CREACION DE LAS PK
ALTER TABLE clientes ADD CONSTRAINT pk_cliente_cli_id PRIMARY KEY(cli_id);
ALTER TABLE pedidos ADD CONSTRAINT pk_pedidos_ped_id PRIMARY KEY(ped_id);

-- CREACION DE LAS FK
ALTER TABLE pedidos ADD FOREIGN KEY (ped_cli_id) REFERENCES clientes(cli_id);
