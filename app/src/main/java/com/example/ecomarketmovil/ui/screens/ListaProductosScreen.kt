package com.example.ecomarketmovil.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ecomarketmovil.ui.Routes
import com.example.ecomarketmovil.viewmodels.ProductoViewModel

@Composable
fun ListaProductosScreen(navController: NavController, viewModel: ProductoViewModel) {

    LaunchedEffect(Unit) {
        viewModel.cargarProductos()
    }

    val textoBusqueda by viewModel.textoBusqueda.collectAsState()
    val productos by viewModel.productosFiltrados.collectAsState()

    Scaffold(
        floatingActionButton = {
            // Navega al formulario en modo "crear" (id nulo)
            FloatingActionButton(onClick = { navController.navigate(Routes.FormularioProducto + "/null") }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Lista de Productos", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = viewModel::onTextoBusquedaChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar por nombre") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (productos.isEmpty()) {
                Text("No hay productos registrados o que coincidan con la búsqueda.")
            } else {
                LazyColumn {
                    items(productos) { producto ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = producto.nombre ?: "", style = MaterialTheme.typography.bodyLarge) // Corregido: Manejo de null
                                    Text(text = "$${producto.precio}", style = MaterialTheme.typography.bodyMedium)
                                    Text(text = "Stock: ${producto.stock}", style = MaterialTheme.typography.bodySmall)
                                }

                                Row {
                                    Button(
                                        onClick = { navController.navigate(Routes.formularioProductoConId(producto.id)) }
                                    ) {
                                        Text("Editar")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = {
                                            viewModel.eliminar(producto.id) // Correcto: pasar solo el ID
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.errorContainer,
                                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                    ) {
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewListaProductos() {
    val navController = rememberNavController()
    val viewModel = ProductoViewModel().apply {
        cargarProductos()
    }
    ListaProductosScreen(navController, viewModel)
}
