package com.example.ecomarketmovil.ui

object Routes {
    var Login = "login"
    var MainMenu = "mainMenu"

    var Mision = "mision"

    var Vision = "vision"

    var Register = "register"

    var MenuUsuario = "menuUsuario"

    var MenuProducto = "menuProducto"

    var ListaProductos = "listaproductos"
    var FormularioProducto = "formularioproducto"

    fun formularioProductoConId(id: Int) = "$FormularioProducto/$id"

    var FormularioUsuario = "formulariousuario"

    var FormUsuarioScreen = "formUsuarioScreen"
    fun formularioUsuarioConRut(rut: String)= "$FormUsuarioScreen/$rut"

    var ListaUsuarios = "listaUsuarios"


}