-- Tabla: Perfiles
CREATE TABLE perfiles (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- Tabla: Usuarios
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo_electronico VARCHAR(150) UNIQUE NOT NULL,
    contrasena TEXT NOT NULL
);

-- Tabla intermedia: usuarios_perfiles (relación N:M)
CREATE TABLE usuarios_perfiles (
    usuario_id INTEGER REFERENCES usuarios (id) ON DELETE CASCADE,
    perfil_id INTEGER REFERENCES perfiles (id) ON DELETE CASCADE,
    PRIMARY KEY (usuario_id, perfil_id)
);

-- Tabla: Cursos
CREATE TABLE cursos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(100) NOT NULL
);

-- Tabla: Tópicos
CREATE TABLE topicos (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    autor_id INTEGER REFERENCES usuarios (id) ON DELETE
    SET NULL,
        curso_id INTEGER REFERENCES cursos (id) ON DELETE
    SET NULL
);

-- Tabla: Respuestas
CREATE TABLE respuestas
(
    id             SERIAL PRIMARY KEY,
    mensaje        TEXT    NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    solucion       BOOLEAN   DEFAULT FALSE,
    autor_id       INTEGER REFERENCES usuarios (id) ON DELETE SET NULL,
    topico_id      INTEGER REFERENCES topicos (id) ON DELETE CASCADE
);