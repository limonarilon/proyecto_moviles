package com.example.ferreteriahogar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ferreteriahogar.data.Producto
import com.example.ferreteriahogar.ui.viewmodels.ProductoViewModel

@Composable
fun FormProductoScreen(navController: NavController, viewModel: ProductoViewModel, id: Int) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }

    // Recogemos los estados del ViewModel
    val mensajeError by viewModel.mensajeError.collectAsState()
    val navegacionExitosa by viewModel.navegacionExitosa.collectAsState()

    val producto: Producto? = if (id != -1) {
        viewModel.obtenerPorId(id)
    } else {
        null
    }

    // Si la operación es exitosa, navegamos hacia atrás
    LaunchedEffect(navegacionExitosa) {
        if (navegacionExitosa) {
            navController.popBackStack()
            viewModel.onNavegacionCompleta() // Resetea el estado para evitar re-navegación
        }
    }

    // Si encontramos un producto (estamos editando), llenamos los campos
    LaunchedEffect(producto) {
        if (producto != null) {
            nombre = producto.nombre
            precio = producto.precio.toString()
            stock = producto.stock.toString()
        }
    }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            val titulo = if (id != -1) "Editar Producto" else "Agregar Producto"
            Text(titulo, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Campo de ID (solo visible en modo edición)
            if (id != -1) {
                OutlinedTextField(
                    value = id.toString(),
                    onValueChange = { },
                    label = { Text("ID del Producto") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false, // Hace que el campo no sea editable
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del producto") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stock") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Muestra el mensaje de error si no es nulo
            mensajeError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    // Llama a la función de validación del ViewModel
                    viewModel.validarYGuardar(id, nombre, precio, stock)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewFormProducto() {
    val navController = rememberNavController()
    val viewModel = ProductoViewModel()
    FormProductoScreen(navController, viewModel, -1)

}