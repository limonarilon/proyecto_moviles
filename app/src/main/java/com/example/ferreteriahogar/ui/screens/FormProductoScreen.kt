package com.example.ferreteriahogar.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.ferreteriahogar.ui.viewmodels.ProductoViewModel

@Composable
fun FormProductoScreen(navController: NavController, viewModel: ProductoViewModel) {
    Text(text = "Formulario de Producto")
}
