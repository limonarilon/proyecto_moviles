Proyecto Desarrollo de aplicaciones móviles EcoMarket_móvil

1. Sobre el proyecto: 
Este proyecto consiste en el desarrollo de una aplicativo móvil basado en el patrón de arquitectura Modelo, Vista y Modelo de Vista (MVVM) que busca satisfacer la necesidad de gestionar operaciones internas de información dentro de la empresa ficticia EcoMarket.

2. Sobre el equipo:
El equipo está constituido por dos desarrolladores, Carolina Garrido (moonsline) y Ariel Silva (limonarilon) quienes se encargaron de todo el proceso de desarrollo de esta solución de software, desde la planeación y recuperación de requisitos, pasando por el desarrollo y los testeos necesarios para esta primera versión.

3. Funcionalidades:
El uso de esta aplicación se sustenta en una interfaz gráfica que permite acceder por medio de un login con autentificación usando tokens a una serie de funcionalidades necesarias para la gestión interna del negocio.
Este sistema de inicio de sesión se implementó por medio del uso de las librerias del backend springboot security y JWT. Mientras que en el frontend se gestiona el envío de solicitudes y recepción de respuestas por medio del protocolo HTTP.
En cuanto a las funcionalidades específicas del sistema estas se pueden seccionar entre utilidades de gestión y utilidades secundarias para el beneficio de los usuarios finales, entendidos como, colaboradores y administradores del negocio con acceso a dispositivos móviles y credenciales.

   3.1 Funcionalidades de gestión

     3.1.1 Gestión de usuarios
           Consiste en la implementación de operaciones CRUD (CREATE, READ, UPDATE y DELETE) de los usuarios del sistema. Comienza desde la creación de usuarios nuevos, antes del login, así como dentro del aplicativo una vez que se inició la sesión. En segundo lugar, la posibiliad de editar a los usuarios previamente existentes, siempre que se posean los permisos de Administrador correspondientes, siendo algunos de los campos posibles de editar, el correo, el rut o el rol. Como tercera posibilidad de uso se encuentra la visualización de los usuarios existentes en la base de datos con sus datos más relevantes en un listado ordenado, como el id, nombre o correo, ocultando información sensible como la contraseña (password). Dentro de esta visualización se encuentra la posibiidad de busqueda por medio la cual se puede filtrar la lista en tiempo real, haciendo más expedito el resultado buscado. En cuarto y último lugar desde la pantalla de usuarios es posible eliminar a los usuarios existenes permitiendo mantener la base de datos limpia de usuarios innecesarios, obsoletos o expirados.
   
     3.1.2 Gestión de productos
           La gestión de productos satisface una necesidad directa del negocio similar a la gestión de usuarios, permitiendo acceder a un menú en el cual se indexan los productos disponibles en la base de datos con sus respeciva información relevante, como precio, stock, id, nombre, imágen asociada, etc. Por medio de este menú se pueden crear nuevos productos siempre y cuando se cumplan las validaciones de producto, editar productos existentes subiendo imagenes al servidor y asociando el nombre del archivo a la fila correspondiente dentro de la tabla de productos, de tal manera que el frontend puede asociar una imagen con su correspondiente producto. También está disponible la opcion de eliminar productos si asi se requiere, borrando efectivamente el registro de la base de datos.
   
     3.1.3 Gestión de pedidos
           En cuanto a la gestión de pedidos, se presenta inicialmente con un sistema de gestión similar, permitiendo crear nuevo pedido, editar existente, listar pedidos y eliminar un pedido por medio de su id. Sin embargo esta aplicación se distingue en la facilidad y eficacia de presentar el detalle de los productos que un pedido contiene, especificando la información relevante como el nombre, el id, precio, etc. Permitiendo esto, que al editar un pedido se pueda agregar un nuevo producto, eliminar uno del pedido o incluso cambiar el estadod el pedido a uno de tres posibles estados: "PENDIENTE", "ENVIADO" o  "ENTREGADO".
   
   3.2 Funcionalidades secundarias
   
     3.2.1 Linterna
           El uso de hardware interno como la linterna incluida en la mayoría de cámaras de dispositivos móviles, permite el acceso rápido a una utilidad relevante en logística dentro del negocio.
   
     3.2.1 Indicador climático
           El indicador climático consume la API externa de clima Meteosource permitiendo desplegar la temperatura ambiental según las coordenadas geográficas del usuario, permitiendo así el acceso rápido a información útil para la toma rápida de decisiones de negocio como por ejemplo, la urgencia de refrigerar un producto determinado.

5. Endpoints utilizados (API externa y microservicios)

     5.1 Funcionalidad de indicador climpático
           Esta API es consumida por medio de una API KEY gratuita y limitada, generada por el sitio Meteosoruce API.
           URL base: https://www.meteosource.com/api/v1/free/
           Endpoint :  @GET /point
   
     5.2 API interna de microservicios
           Consiste en el consumo de una API interna desarrollada por nosotros que constituye el backend de la aplicación. Esta desarrollada con Java springboot basada en la arquitectura MVC (Modelo, Vista y Controllador), docuementada por medio de HATEOAS y por el uso de SWAGGER. Lo que permite consultar los endpoints independientemente de la aplicación de front que la consuma. La URL base dependerá del equipo y locación en que se ejecute el backend, puesto que en esta versión preliminar esta API utiliza ejecución local. Aunque se prevee que en la proximidad se suba a la nube y recurra al servicio de Amazon, ejecutandose entonces en una máquina virtual del tipo EC2. Los endpoints utilizados consisten en los necesarios para una gestión CRUD funcional, utilizandose para esta aplicación solo los necesarios y relativos a Usuarios, Autentificación, Productos y Pedidos. El backend se conecta y recupera a su vez la información almacenada en la base de datos del negocio, que utiliza una base de datos autónoma relacional del proveedor Oracle.
           URL base: http://10.0.2.2:8080/ -- Cuando el backend es ejecutado en la misma máquina.
                     http://<IP_MAQUINA_LOCAL>:8080/ -- cuando se ejecuta en una máquina diferente pero dentro de la misma red LAN (es necesario reemplazar el texto entre "<>" por la ip del equipo utilizado)
           Endpoints :
   
                     Usuarios
                     GET /usuarios :
                     GET /usuarios/{id} :
                     POST /usuarios :
                     PUT /usuarios/{id} :
                     DELETE /usuarios/{id} :
   
                     Productos
                     GET /productos : 
                     GET  /productos/{id} :
                     POST /productos :
                     PUT /productos/{id} :
                     DELETE /productos/{id} :

                     Pedidos
                     GET /pedidos : 
                     GET  /pedidos/{id} :
                     POST /pedidos :
                     PUT /pedidos/{id} :
                     DELETE /pedidos/{id} :
                     POST /pedidos/{idPedido}/productos/{idProducto} :
                     DELETE /pedidos/{idPedido}/productos/{idProducto} :

                     Autentificación:
                     POST /auth/login :   
7. Pasos para ejecutar
8. Anexos: capturas

   
