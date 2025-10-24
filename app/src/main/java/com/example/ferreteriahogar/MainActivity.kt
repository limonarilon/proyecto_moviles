package com.example.ferreteriahogar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ferreteriahogar.ui.Routes


import com.example.ferreteriahogar.ui.screens.*
import com.example.ferreteriahogar.ui.theme.FerreteriaHogarTheme
import com.example.ferreteriahogar.ui.viewmodels.ProductoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val productoViewModel: ProductoViewModel = viewModel()

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
                composable( Routes.MenuUsuario){
                    Scaffold (Modifier.fillMaxSize()){ innerPadding ->
                        MenuUsuario(paddingValues = innerPadding, navController)
                    }
                }
                composable( Routes.MenuProducto){
                    Scaffold (Modifier.fillMaxSize()){ innerPadding ->
                        MenuProductos(paddingValues = innerPadding, navController)
                    }
                }
                composable(Routes.ListaProductos) {
                    ListaProductosScreen(navController, productoViewModel)
                }
                composable(Routes.FormularioProducto) {
                    FormProductoScreen(navController, productoViewModel)
                }
            })
        }
    }
}
