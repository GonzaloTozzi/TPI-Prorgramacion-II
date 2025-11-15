1. Introducción al Proyecto
Este repositorio contiene el código fuente (SQL) del Trabajo Final Integrador (TFI) de la asignatura Bases de Datos I (UTN). El proyecto simula un Sistema de Gestión de Pedidos y Logística de Envíos, diseñado para ser robusto, escalable y seguro.

El desarrollo se organizó en cinco etapas, demostrando el dominio en el modelado, la performance, la seguridad y el manejo avanzado de la concurrencia en entornos de gran volumen de datos (200.000 registros).

2. Guía de Instalación y Ejecución

El proyecto fue desarrollado y probado en MySQL 8.0 (compatible con MariaDB).

---Prerrequisitos

Servidor MySQL o MariaDB instalado y en ejecución.
Cliente SQL (ej., MySQL Workbench o DBeaver) con permisos de administrador root para crear la base de datos y usuarios.

---Pasos de Ejecución Secuencial

Para correr el proyecto completo, ejecute los scripts en el siguiente orden, utilizando la conexión de administrador:

--Configuración Inicial:

Ejecutar TP PARTE 1.sql (Crea la estructura de tablas y las restricciones).

--Carga de Datos:

Ejecutar TP PARTE 2.sql (Carga los 200.000 registros).

--Análisis y Optimización:

Ejecutar TP PARTE 3.sql.

--Seguridad:

Ejecutar TP PARTE 4.sql (Crea el usuario 'user_reporter' y las vistas).

Ejecutar TP PARTE 5.sql (Crea el procedimiento anti-inyección).




Link del video: https://www.youtube.com/watch?v=I-QsK3rxaC0 .