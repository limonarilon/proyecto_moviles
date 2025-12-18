package com.example.ecomarketmovil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ecomarketmovil.ui.Routes
import com.example.ecomarketmovil.ui.auth.RequireAnyRole
import com.example.ecomarketmovil.ui.auth.RequireRole
import com.example.ecomarketmovil.ui.screens.*
import com.example.ecomarketmovil.viewmodels.PedidoViewModel
import com.example.ecomarketmovil.viewmodels.ProductoViewModel
import com.example.ecomarketmovil.viewmodels.UsuarioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val productoViewModel: ProductoViewModel = viewModel()
            val usuarioViewModel: UsuarioViewModel = viewModel()
            val pedidoViewModel: PedidoViewModel = viewModel()


            NavHost(navController = navController, startDestination = Routes.Login, builder = {
                composable(Routes.Login,) { 
                    Scaffold(Modifier.fillMaxSize()) { innerPadding ->
                        Login(paddingValues = innerPadding, navController)
                    }
                }
                composable (Routes.Register ){
                    FormularioScreen(navController)
                }

                composable(Routes.OlvidarPassword) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        OlvidarPassword(paddingValues = innerPadding, navController = navController)
                    }
                }

                // Ruta simplificada para el Menú Principal
                composable(Routes.MainMenu + "/{user}") { backStackEntry ->
                    val user = backStackEntry.arguments?.getString("user") ?: "Usuario"
                    Scaffold(Modifier.fillMaxSize()) { innerPadding ->
                        MainMenu(paddingValues = innerPadding, user = user, navController = navController)
                    }
                }

                composable( Routes.Mision, ) { 
                    Scaffold (Modifier.fillMaxSize()){ innerPadding ->
                        Mision(paddingValues = innerPadding, navController)
                    }
                }
                composable (Routes.Vision) {
                    Scaffold(Modifier.fillMaxSize()) { innerPadding ->
                        Vision(paddingValues = innerPadding, navController)
                    }
                }

                composable( Routes.MenuProducto){
                    Scaffold (Modifier.fillMaxSize()){ innerPadding ->
                        MenuProducto(paddingValues = innerPadding, navController)
                    }
                }
                composable(Routes.ListaProductos) {
                    ListaProductosScreen(navController, productoViewModel)
                }
                composable(
                    route = Routes.FormularioProducto + "/{id}",
                    arguments = listOf(navArgument("id") { 
                        type = NavType.IntType
                        defaultValue = -1
                    })
                ) {
                    val id = it.arguments?.getInt("id") ?: -1
                    RequireAnyRole(navController, setOf("ADMIN", "GERENTE")) {
                        FormProductoScreen(navController, productoViewModel, id)
                    }
                }

                composable( Routes.MenuUsuario){
                    Scaffold (Modifier.fillMaxSize()){ innerPadding ->
                        RequireRole(navController, "ADMIN") { // MODIFICADO: Solo ADMIN
                            MenuUsuario(paddingValues = innerPadding, navController)
                        }
                    }
                }

                composable(Routes.ListaUsuarios) {
                    RequireRole(navController, "ADMIN") { // MODIFICADO: Solo ADMIN
                        LaunchedEffect(Unit) {
                            usuarioViewModel.cargarUsuarios()
                        }
                        val usuarios by usuarioViewModel.usuariosFiltrados.collectAsState()
                        ListaUsuarioScreen(
                            usuarios = usuarios,
                            navController = navController,
                            onEditar = { usuario ->
                                navController.navigate(Routes.FormularioUsuario + "/${usuario.id}")
                            },
                            onEliminar = { usuario ->
                                usuarioViewModel.eliminar(usuario.id)
                            }
                        )
                    }
                }
                composable( Routes.MenuPedido){
                    Scaffold (Modifier.fillMaxSize()){ innerPadding ->
                        RequireAnyRole(navController, setOf("ADMIN", "GERENTE", "LOGISTICA")) {
                            MenuPedido(paddingValues = innerPadding, navController)
                        }
                    }
                }
                composable(Routes.ListaPedidos) {
                     RequireAnyRole(navController, setOf("ADMIN", "GERENTE", "LOGISTICA")) {
                        ListaPedidoScreen(navController, pedidoViewModel)
                    }
                }
                //Ruta para crear un nuevo pedido (id es nulo)
                composable(Routes.FormularioPedido) {
                    RequireAnyRole(navController, setOf("ADMIN", "GERENTE", "LOGISTICA")) {
                        FormPedidoScreen(navController, pedidoViewModel, productoViewModel, id = null)
                    }
                }
                //Ruta para editar un pedido existente (se pasa el id)
                composable(
                    route = Routes.FormularioPedido + "/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id")
                    RequireAnyRole(navController, setOf("ADMIN", "GERENTE", "LOGISTICA")) {
                        FormPedidoScreen(navController, pedidoViewModel, productoViewModel, id)
                    }
                }

                // Ruta para crear un nuevo usuario (id es nulo)
                composable(Routes.FormularioUsuario) {
                    RequireRole(navController, "ADMIN") {
                        FormUsuarioScreen(navController, usuarioViewModel, idUsuario = null)
                    }
                }

                // Ruta para editar un usuario existente (se pasa el id)
                composable(
                    route = Routes.FormularioUsuario + "/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id")
                    RequireRole(navController, "ADMIN") {
                        FormUsuarioScreen(navController, usuarioViewModel, idUsuario = id)
                    }
                }
            })
        }
    }
}