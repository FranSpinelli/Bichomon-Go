DROP SCHEMA IF EXISTS epers_persistiendoConEstilo_jdbc;
CREATE SCHEMA epers_persistiendoConEstilo_jdbc;

USE epers_persistiendoConEstilo_jdbc;

CREATE TABLE especie (
  id int NOT NULL UNIQUE PRIMARY KEY ,
  nombre varchar(30) NOT NULL UNIQUE,
  altura int NOT NULL,
  peso int NOT NULL,
  tipoDeBicho varchar(30) NOT NULL,
  energiaInicial int NOT NULL,
  urlFoto varchar(100),
  cantidadDeBichos int NOT NULL
)
ENGINE = InnoDB;