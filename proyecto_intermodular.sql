create database if not exists proyecto;
use proyecto;

Create table if not exists usuario(
	nºusuario int primary key Auto_Increment,
    nombre_usuario varchar(50) UNIQUE,
    correo varchar(100) UNIQUE,
    contraseña varchar(30) NOT NULL,
    fecha_nacimiento Date NOT NULL
);
create table if not exists ranking(
	id_ranking int primary key Auto_Increment NOT NULL,
    nombre_ranking varchar(100) NOT NULL UNIQUE
);
create table if not exists posicion_ranking(
	nºusuario int,
    id_ranking int,
    posicion int NOT NULL UNIQUE,
    FOREIGN KEY (nºusuario) REFERENCES usuario(nºusuario),
    FOREIGN KEY (id_ranking) REFERENCES ranking(id_ranking)
);
create table recetas(
	id_receta int primary key,
    nºusuario int,
    nombre_receta varchar(100) NOT NULL,
    ingredientes varchar(100) NOT NULL,
    instrucciones text NOT NULL,
    FOREIGN KEY (nºusuario) REFERENCES usuario(nºusuario)
);

create table comentarios(
	nºusuario int,
    nombre_usuario varchar(50),
    id_receta int,
    FOREIGN KEY (nºusuario) REFERENCES usuario(nºusuario),
    FOREIGN KEY (nombre_usuario) REFERENCES usuario(nombre_usuario),
    FOREIGN KEY (id_recetas) REFERENCES recetas(id_receta)
);




