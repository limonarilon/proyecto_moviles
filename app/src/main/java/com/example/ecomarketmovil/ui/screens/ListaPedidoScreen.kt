package com.example.ecomarketmovil.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ecomarketmovil.data.Pedido
import com.example.ecomarketmovil.data.Producto
import com.example.ecomarketmovil.ui.Routes
import com.example.ecomarketmovil.viewmodels.PedidoViewModel
import com.example.ecomarketmovil.ui.components.BackIconButton


@Composable
fun ListaPedidoScreen(navController: NavController, viewModel: PedidoViewModel) {

    LaunchedEffect(Unit) {
        viewModel.cargarPedidos()
    }

    val textoBusqueda by viewModel.textoBusqueda.collectAsState()
    val pedidos by viewModel.pedidosFiltrados.collectAsState()

    Scaffold(
        floatingActionButton = {
            // Navega al formulario en modo "crear" (id -1)
            FloatingActionButton(onClick = { navController.navigate(Routes.formularioPedidoConId(-1)) }) {
                Text("+")
            }
        }
    ) { padding ->
        BackIconButton(navController = navController)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Lista de Pedidos", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = viewModel::onTextoBusquedaChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar por ID o estado") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (pedidos.isEmpty()) {
                Text("No hay pedidos registrados o que coincidan con la búsqueda.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(pedidos) { pedido ->
                        PedidoItem(
                            pedido = pedido,
                            onEditar = { navController.navigate(Routes.formularioPedidoConId(pedido.idPedido)) },
                            onEliminar = { viewModel.deletePedido(pedido.idPedido) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PedidoItem(pedido: Pedido, onEditar: () -> Unit, onEliminar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = "Pedido #${pedido.idPedido}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Estado: ${pedido.estado ?: "No especificado"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Total: $${pedido.total}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Fecha: ${pedido.fecha ?: "No especificada"}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Productos:", style = MaterialTheme.typography.titleMedium)
            Column(modifier = Modifier.padding(start = 8.dp)) {
                pedido.productos.forEach { producto ->
                    Text("- ${producto.nombre ?: "N/A"} ($${producto.precio})")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = onEditar) {
                    Text("Editar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onEliminar,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewListaPedidoScreen() {
    val navController = rememberNavController()
    // ViewModel de previsualización con datos de ejemplo
    val fakeViewModel = PedidoViewModel()

    // No podemos cargar datos reales en preview, pero podemos simularlos
    // para el propósito de la vista previa del diseño.

    ListaPedidoScreen(navController, fakeViewModel)
}