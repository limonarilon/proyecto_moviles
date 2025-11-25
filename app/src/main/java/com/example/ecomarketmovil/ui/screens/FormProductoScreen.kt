package com.example.ecomarketmovil.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.ecomarketmovil.R
import com.example.ecomarketmovil.viewmodels.ProductoViewModel
import java.time.Instant
import java.time.ZoneId

private const val BASE_IMAGE_URL = "http://10.0.2.2:8080/productos/uploads/"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormProductoScreen(navController: NavController, viewModel: ProductoViewModel, id: Int?) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var img by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }

    var categoria by remember { mutableStateOf("Seleccionar…") }
    var destacado by remember { mutableStateOf(false) }
    var stockMinimo by remember { mutableStateOf("") }

    var imageModel by remember { mutableStateOf<Any?>(null) }
    val openDialog = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var expanded by remember { mutableStateOf(false) }

    val esNuevo = id == null
    val context = LocalContext.current

    val mensajeError by viewModel.mensajeError.collectAsState()
    val navegacionExitosa by viewModel.navegacionExitosa.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageModel = uri
            img = uri.lastPathSegment?.substringAfterLast('/') ?: "imagen.jpg"
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) imagePickerLauncher.launch("image/*")
    }

    LaunchedEffect(navegacionExitosa) {
        if (navegacionExitosa) {
            navController.popBackStack()
            viewModel.onNavegacionCompleta()
        }
    }

    LaunchedEffect(id) {
        if (!esNuevo) {
            val producto = viewModel.obtenerPorIdLocal(id!!)
            if (producto != null) {
                // --- LÓGICA CORREGIDA ANTI-NULL ---
                nombre = producto.nombre ?: ""
                precio = producto.precio.toString()
                stock = producto.stock.toString()
                img = producto.img ?: ""
                expirationDate = producto.expirationDate ?: ""

                if (img.isNotBlank()) {
                    imageModel = BASE_IMAGE_URL + img
                }
            }
        }
    }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val titulo = if (esNuevo) "Agregar Producto" else "Editar Producto"
            Text(titulo, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            AsyncImage(
                model = imageModel,
                contentDescription = "Imagen del producto",
                modifier = Modifier.size(150.dp).padding(bottom = 16.dp),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.logo)
            )

            Button(onClick = {
                when (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    PackageManager.PERMISSION_GRANTED -> imagePickerLauncher.launch("image/*")
                    else -> permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }) {
                Text("Seleccionar Imagen")
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del producto") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            Spacer(modifier = Modifier.height(16.dp))

            Text("Fecha de expiración", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(value = expirationDate, onValueChange = {}, readOnly = true, modifier = Modifier.fillMaxWidth().clickable { openDialog.value = true }, label = { Text("Seleccionar fecha") })
            if (openDialog.value) {
                DatePickerDialog(
                    onDismissRequest = { openDialog.value = false },
                    confirmButton = {
                        Button(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                expirationDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate().toString()
                            }
                            openDialog.value = false
                        }) { Text("Aceptar") }
                    }
                ) { DatePicker(state = datePickerState) }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Categoría del producto", style = MaterialTheme.typography.titleMedium)
            Box {
                OutlinedTextField(value = categoria, onValueChange = {}, readOnly = true, modifier = Modifier.fillMaxWidth().clickable { expanded = true }, label = { Text("Categoría") })
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf("Abarrotes", "Lácteos", "Frutas", "Bebidas").forEach { opt ->
                        DropdownMenuItem(text = { Text(opt) }, onClick = { categoria = opt; expanded = false })
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = destacado, onCheckedChange = { destacado = it })
                Text("¿Producto destacado?")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = stockMinimo, onValueChange = { stockMinimo = it }, label = { Text("Stock mínimo sugerido") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(24.dp))

            mensajeError?.let { Text(text = it, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp)) }
            Button(
                onClick = { viewModel.validarYGuardar(id, nombre, precio, stock, img, expirationDate) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF053900), contentColor = Color.White)
            ) { Text("Guardar") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFormProducto() {
    val navController = rememberNavController()
    val viewModel = viewModel<ProductoViewModel>()
    FormProductoScreen(navController, viewModel, null)
}
