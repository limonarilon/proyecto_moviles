package com.example.ecomarketmovil.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecomarketmovil.data.PedidoRegistro
import com.example.ecomarketmovil.data.Producto
import com.example.ecomarketmovil.viewmodels.PedidoViewModel
import com.example.ecomarketmovil.viewmodels.ProductoViewModel
import java.time.LocalDateTime
import com.example.ecomarketmovil.ui.components.BackIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormPedidoScreen(
    navController: NavController,
    pedidoViewModel: PedidoViewModel,
    productoViewModel: ProductoViewModel,
    id: Int?
) {
    // --- Estados del Formulario ---
    var estado by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var productosEnPedido by remember { mutableStateOf<List<Producto>>(emptyList()) }
    var productoSeleccionadoParaAnadir by remember { mutableStateOf<Producto?>(null) }

    // Dropdowns
    var isEstadoDropdownExpanded by remember { mutableStateOf(false) }

    // --- Estados VM ---
    val pedidoId = id ?: -1
    val esNuevo = pedidoId == -1
    val todosLosProductos by productoViewModel.productosFiltrados.collectAsState()
    val mensajeError by pedidoViewModel.mensajeError.collectAsState()
    val navegacionExitosa by pedidoViewModel.navegacionExitosa.collectAsState()

    // Navegación post acción
    LaunchedEffect(navegacionExitosa) {
        if (navegacionExitosa) {
            navController.popBackStack()
            pedidoViewModel.onNavegacionCompleta()
        }
    }

    // Cargar productos
    LaunchedEffect(Unit) {
        productoViewModel.cargarProductos()
    }

    // Cargar info si editando
    LaunchedEffect(id) {
        if (esNuevo) {
            fecha = LocalDateTime.now().toString()
            estado = "PENDIENTE"
        } else {
            val pedido = pedidoViewModel.obtenerPedidoPorIdLocal(pedidoId)
            if (pedido != null) {
                estado = pedido.estado ?: "PENDIENTE"
                fecha = pedido.fecha ?: LocalDateTime.now().toString()
                productosEnPedido = pedido.productos
            }
        }
    }

    Scaffold { paddingValues ->
        BackIconButton(navController = navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(if (esNuevo) "Agregar Pedido" else "Editar Pedido",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Dropdown Estado ---
            val estadosPosibles = listOf("PENDIENTE", "ENVIADO", "ENTREGADO")

            Text("Estado del Pedido", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())

            ExposedDropdownMenuBox(
                expanded = isEstadoDropdownExpanded,
                onExpandedChange = { isEstadoDropdownExpanded = !isEstadoDropdownExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = estado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Seleccionar estado") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = isEstadoDropdownExpanded,
                    onDismissRequest = { isEstadoDropdownExpanded = false }
                ) {
                    estadosPosibles.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                estado = opcion
                                isEstadoDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = fecha,
                onValueChange = {},
                label = { Text("Fecha del pedido") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Productos en el Pedido", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            if (productosEnPedido.isEmpty()) {
                Text("Aún no hay productos en este pedido.")
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    productosEnPedido.forEach { producto ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "- ${producto.nombre ?: "N/A"} ($${producto.precio})",
                                modifier = Modifier.weight(1f)
                            )
                            if (!esNuevo) {
                                Button(
                                    onClick = { pedidoViewModel.removeProductoFromPedido(pedidoId, producto.id) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Text("Quitar")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- ADD PRODUCTO (solo si editando) ---
            if (!esNuevo) {
                Text("Añadir Producto al Pedido", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                ProductoDropdown(
                    productos = todosLosProductos,
                    seleccionado = productoSeleccionadoParaAnadir,
                    onSeleccion = { productoSeleccionadoParaAnadir = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        productoSeleccionadoParaAnadir?.let { productToAdd ->
                            pedidoViewModel.addProductoToPedido(pedidoId, productToAdd.id)
                            productosEnPedido = productosEnPedido + productToAdd // Actualización local
                            productoSeleccionadoParaAnadir = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = productoSeleccionadoParaAnadir != null
                ) {
                    Text("Añadir Producto")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            mensajeError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    val totalCalculado = productosEnPedido.sumOf { it.precio }
                    val pedidoRegistro = PedidoRegistro(
                        fecha = if (fecha.isBlank()) null else fecha,
                        estado = if (estado.isBlank()) null else estado,
                        total = totalCalculado,
                        productos = productosEnPedido
                    )
                    if (esNuevo) {
                        pedidoViewModel.createPedido(pedidoRegistro)
                    } else {
                        pedidoViewModel.updatePedido(pedidoId, pedidoRegistro)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF053900),
                    contentColor = Color.White
                )
            ) {
                Text("Guardar Cambios")
            }
        }
    }
}


// ---------------------------------------------------------------------
//  COMPONENTE DROPDOWN (Material3 OFICIAL) PARA SELECCIONAR PRODUCTO
// ---------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoDropdown(
    productos: List<Producto>,
    seleccionado: Producto?,
    onSeleccion: (Producto) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            readOnly = true,
            value = seleccionado?.nombre ?: "Seleccionar producto...",
            onValueChange = {},
            label = { Text("Producto") },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            productos.forEach { producto ->
                DropdownMenuItem(
                    text = { Text("${producto.nombre} ($${producto.precio})") },
                    onClick = {
                        onSeleccion(producto)
                        expanded = false
                    }
                )
            }
        }
    }
}
