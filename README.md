Descripción del dominio

El sistema implementado gestiona Pedidos y Envios, manteniendo una relación 1 a 1 entre ambas entidades. Cada Pedido puede tener asociado un único Envío, y cada Envío pertenece exclusivamente a un Pedido.
El objetivo del proyecto es demostrar un flujo CRUD completo (crear, leer, actualizar y eliminar) junto con la correcta administración de la relación entre tablas y el uso de operaciones transaccionales para garantizar la integridad de los datos.


Requisitos técnicos

Java:
• JDK 17 o superior
• NetBeans / IntelliJ / Eclipse
• Conector JDBC para MySQL (mysql-connector-j) agregado al ClassPath

Base de datos:
• MySQL Server 8+
• Usuario con permisos de creación y modificación de tablas
• Motor InnoDB para soportar claves foráneas y transacciones


Creación de la base de datos

El proyecto incluye un archivo tpi.sql que contiene:
• Creación de la base tpi
• Definición de tablas pedido y envio
• Relaciones 1→1
• Autoincrement, claves primarias y foráneas


Pasos para importar la base

1.	Abrir MySQL Workbench o phpMyAdmin.
2.	Crear la base manualmente o ejecutar directamente:
SOURCE tpi.sql;
3.	Verificar que las tablas estén creadas y con motor InnoDB.


Cómo compilar y ejecutar el proyecto

1.	Clonar o descargar el repositorio.
2.	Abrirlo en NetBeans.
3.	Verificar que el archivo JAR del conector MySQL esté en el Classpath.
4.	Configurar las credenciales en PedidoDAO (o donde esté el DriverManager):
private static final String URL = "jdbc:mysql://localhost:3306/tpi?serverTimezone=UTC";
private static final String USER = "root";
private static final String PASSWORD = "1234";
5.	Ejecutar el proyecto y utilizar el menú de consola que permite:
• Crear pedidos
• Crear envíos
• Listar pedidos
• Buscar por ID
• Eliminar y actualizar
• Probar la relación 1→1
• Ejecutar la operación transaccional

Credenciales de ejemplo
Usuario: root
Contraseña: 1234


Flujo recomendado para probar

1.	Crear un pedido desde el menú
2.	Crear un envío asociado al pedido
3.	Consultar el pedido y ver el envío vinculado
4.	Ejecutar operación transaccional:
• Se intenta crear un pedido y un envío juntos
• Se simula un fallo
• Se muestra que el rollback revierte todo correctamente

Esto se refleja en la ejecución y también en la base de datos.

Link Video Youtube
https://www.youtube.com/watch?v=NGb2ze71KVw
