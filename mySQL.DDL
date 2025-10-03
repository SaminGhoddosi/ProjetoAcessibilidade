-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`Papel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Papel` (
  `id` INT NOT NULL,
  `Nome` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Usuario` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `cpf` VARCHAR(11) NOT NULL,
  `dataNascimento` DATETIME NOT NULL,
  `telefone` VARCHAR(11) NOT NULL,
  `senha` VARCHAR(255) NOT NULL,
  `Papel_id` INT NOT NULL,
  PRIMARY KEY (`idUsuario`),
  UNIQUE INDEX `Cpf_UNIQUE` (`cpf` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  INDEX `fk_Usuario_Papel_idx` (`Papel_id` ASC) VISIBLE,
  CONSTRAINT `fk_Usuario_Papel`
    FOREIGN KEY (`Papel_id`)
    REFERENCES `mydb`.`Papel` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`TipoAcessibilidade`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`TipoAcessibilidade` (
  `idTipo` INT NOT NULL,
  `Tipo` VARCHAR(45) NULL,
  PRIMARY KEY (`idTipo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`ponto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`ponto` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NULL,
  `latitude` VARCHAR(45) NULL,
  `longitude` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Rotas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Rotas` (
  `idRotas` INT NOT NULL,
  `caminho` VARCHAR(45) NULL,
  `duracao` VARCHAR(45) NULL,
  `distancia` VARCHAR(45) NULL,
  `criadoEm` VARCHAR(45) NULL,
  `ponto_origemid` INT NOT NULL,
  `ponto_destinoid` INT NOT NULL,
  `Usuario_idUsuario` INT NOT NULL,
  PRIMARY KEY (`idRotas`),
  INDEX `fk_Rotas_ponto1_idx` (`ponto_origemid` ASC) VISIBLE,
  INDEX `fk_Rotas_ponto2_idx` (`ponto_destinoid` ASC) VISIBLE,
  INDEX `fk_RotasFavoritas_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Rotas_ponto1`
    FOREIGN KEY (`ponto_origemid`)
    REFERENCES `mydb`.`ponto` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Rotas_ponto2`
    FOREIGN KEY (`ponto_destinoid`)
    REFERENCES `mydb`.`ponto` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RotasFavoritas_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `mydb`.`Usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Destino`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Destino` (
  `idDestino` INT NOT NULL,
  `latitude` VARCHAR(45) NULL,
  `longitude` VARCHAR(45) NULL,
  PRIMARY KEY (`idDestino`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Origem`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Origem` (
  `idOrigem` INT NOT NULL,
  `latitude` VARCHAR(45) NULL,
  `longitude` VARCHAR(45) NULL,
  PRIMARY KEY (`idOrigem`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`TipoPonto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`TipoPonto` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `tipo` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`PontosAcessibilidade`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`PontosAcessibilidade` (
  `id` INT NOT NULL,
  `Status` VARCHAR(45) NOT NULL,
  `dataCriacao` DATETIME NOT NULL,
  `TipoAcessibilidade_idTipo` INT NOT NULL,
  `Usuario_idUsuario` INT NOT NULL,
  `TipoPonto_id` INT NOT NULL,
  `ponto_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_pontosAcessibilidade_TipoAcessibilidade1_idx` (`TipoAcessibilidade_idTipo` ASC) VISIBLE,
  INDEX `fk_pontosAcessibilidade_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  INDEX `fk_pontosAcessibilidade_TipoPonto1_idx` (`TipoPonto_id` ASC) VISIBLE,
  INDEX `fk_PontosAcessibilidade_ponto1_idx` (`ponto_id` ASC) VISIBLE,
  CONSTRAINT `fk_pontosAcessibilidade_TipoAcessibilidade1`
    FOREIGN KEY (`TipoAcessibilidade_idTipo`)
    REFERENCES `mydb`.`TipoAcessibilidade` (`idTipo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_pontosAcessibilidade_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `mydb`.`Usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_pontosAcessibilidade_TipoPonto1`
    FOREIGN KEY (`TipoPonto_id`)
    REFERENCES `mydb`.`TipoPonto` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PontosAcessibilidade_ponto1`
    FOREIGN KEY (`ponto_id`)
    REFERENCES `mydb`.`ponto` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Feedback`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Feedback` (
  `idfeedback` INT NOT NULL,
  `comentario` VARCHAR(45) NOT NULL,
  `avaliacao` VARCHAR(45) NOT NULL,
  `pontosAcessibilidade_id` INT NOT NULL,
  `Usuario_idUsuario` INT NOT NULL,
  PRIMARY KEY (`idfeedback`),
  INDEX `fk_feedback_pontosAcessibilidade1_idx` (`pontosAcessibilidade_id` ASC) VISIBLE,
  INDEX `fk_feedback_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_feedback_pontosAcessibilidade1`
    FOREIGN KEY (`pontosAcessibilidade_id`)
    REFERENCES `mydb`.`PontosAcessibilidade` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_feedback_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `mydb`.`Usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Usuario_has_TipoAcessibilidade`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Usuario_has_TipoAcessibilidade` (
  `Usuario_idUsuario` INT NOT NULL,
  `TipoAcessibilidade_idTipo` INT NOT NULL,
  PRIMARY KEY (`Usuario_idUsuario`, `TipoAcessibilidade_idTipo`),
  INDEX `fk_Usuario_has_TipoAcessibilidade_TipoAcessibilidade1_idx` (`TipoAcessibilidade_idTipo` ASC) VISIBLE,
  INDEX `fk_Usuario_has_TipoAcessibilidade_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Usuario_has_TipoAcessibilidade_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `mydb`.`Usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Usuario_has_TipoAcessibilidade_TipoAcessibilidade1`
    FOREIGN KEY (`TipoAcessibilidade_idTipo`)
    REFERENCES `mydb`.`TipoAcessibilidade` (`idTipo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`HistoricoRotas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`HistoricoRotas` (
  `Usuario_idUsuario` INT NOT NULL,
  `Rotas_idRotas` INT NOT NULL,
  `dataUso` DATETIME NULL,
  PRIMARY KEY (`Usuario_idUsuario`, `Rotas_idRotas`),
  INDEX `fk_Usuario_has_Rotas_Rotas1_idx` (`Rotas_idRotas` ASC) VISIBLE,
  INDEX `fk_Usuario_has_Rotas_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Usuario_has_Rotas_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `mydb`.`Usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Usuario_has_Rotas_Rotas1`
    FOREIGN KEY (`Rotas_idRotas`)
    REFERENCES `mydb`.`Rotas` (`idRotas`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`RotasFavoritas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`RotasFavoritas` (
  `Usuario_idUsuario` INT NOT NULL,
  `Rotas_idRotas` INT NOT NULL,
  `nome` VARCHAR(45) NULL,
  PRIMARY KEY (`Usuario_idUsuario`, `Rotas_idRotas`),
  INDEX `fk_Usuario_has_Rotas_Rotas2_idx` (`Rotas_idRotas` ASC) VISIBLE,
  INDEX `fk_Usuario_has_Rotas_Usuario2_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Usuario_has_Rotas_Usuario2`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `mydb`.`Usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Usuario_has_Rotas_Rotas2`
    FOREIGN KEY (`Rotas_idRotas`)
    REFERENCES `mydb`.`Rotas` (`idRotas`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
