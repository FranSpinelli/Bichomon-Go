/*DROP SCHEMA IF EXISTS epers_ejemplo_jdbc;*/
/* Lo Comente por que tecnicamente como vamos a tener el Data Service, no hace falta (no estoy seguro) */

CREATE SCHEMA epers_persistiendoConEstilo_jdbc;

USE epers_persistiendoConEstilo_jdbc;
/* esta sentencia indica a mySql que use esa BD por defecto para las prox sentencias*/

CREATE TABLE especie (
  id SERIAL NOT NULL UNIQUE PRIMARY KEY ,
  nombre varchar(30) NOT NULL UNIQUE,
  altura int NOT NULL,
  peso int NOT NULL,
  tipoDeBicho varchar(30) NOT NULL,
  energiaInicial int NOT NULL,
  urlFoto varchar(50),
  cantidadDeBichos int NOT NULL
)
ENGINE = InnoDB;