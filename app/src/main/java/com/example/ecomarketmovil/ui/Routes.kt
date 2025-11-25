package com.example.ecomarketmovil.ui

object Routes {
    var Login = "login"
    var MainMenu = "mainMenu"
    var Mision = "mision"
    var Vision = "vision"
    var Register = "register"

    // --- Rutas de Usuario ---
    var MenuUsuario = "menu_usuario"
    var ListaUsuarios = "lista_usuarios"
    var FormularioUsuario = "formulario_usuario" // Ruta base para crear
    fun formularioUsuarioConRut(rut: String) = "$FormularioUsuario/$rut" // Función para editar

    // --- Rutas de Producto ---
    var MenuProducto = "menu_producto"
    var ListaProductos = "lista_productos"
    var FormularioProducto = "formulario_producto"
    fun formularioProductoConId(id: Int) = "$FormularioProducto/$id"

    // --- Rutas de Pedido ---
    var MenuPedido = "menu_pedido"
    var ListaPedidos = "lista_pedidos"
    var FormularioPedido = "formulario_pedido"
    fun formularioPedidoConId(id: Int) = "$FormularioPedido/$id"
}