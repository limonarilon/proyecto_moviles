package com.example.ecomarketmovil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ecomarketmovil.ui.Routes
import com.example.ecomarketmovil.ui.screens.*
import com.example.ecomarketmovil.ui.viewmodels.ProductoViewModel
import com.example.ecomarketmovil.ui.viewmodels.UsuarioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val productoViewModel: ProductoViewModel = viewModel()
            val usuarioViewModel: UsuarioViewModel = viewModel()

            NavHost(navController = navController, startDestination = Routes.Login, builder = {
                composable(Routes.Login,){
                    Scaffold(Modifier.fillMaxSize()) { innerPadding ->
                        Login(paddingValues = innerPadding, navController)
                    }
                }
                composable (Routes.Register ){
                    FormularioScreen()
                }
                composable(Routes.MainMenu+"/{user}"+"/{passwordHashed}",){
                    val user = it.arguments?.getString("user")
                    val passwordHashed = it.arguments?.getString("passwordHashed")
                    Scaffold(Modifier.fillMaxSize()) { innerPadding ->
                        MainMenu(paddingValues = innerPadding, user?:"Error", passwordHashed?:"No" , navController)
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
                    FormProductoScreen(navController, productoViewModel, id)
                }

                composable( Routes.MenuUsuario){
                    Scaffold (Modifier.fillMaxSize()){ innerPadding ->
                        MenuUsuario(paddingValues = innerPadding, navController)
                    }
                }

                composable(Routes.ListaUsuarios) {
                    ListaUsuarioScreen(navController, usuarioViewModel)
                }

                // Ruta para crear un nuevo usuario (rut es nulo)
                composable(Routes.FormularioUsuario) {
                    FormUsuarioScreen(navController, usuarioViewModel, rut = null)
                }

                // Ruta para editar un usuario existente (se pasa el rut)
                composable(
                    route = Routes.FormularioUsuario + "/{rut}",
                    arguments = listOf(navArgument("rut") { type = NavType.StringType })
                ) { backStackEntry ->
                    val rut = backStackEntry.arguments?.getString("rut")
                    FormUsuarioScreen(navController, usuarioViewModel, rut = rut)
                }
            })
        }
    }
}