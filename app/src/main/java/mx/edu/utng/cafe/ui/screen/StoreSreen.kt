package mx.edu.utng.cafe.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.edu.utng.cafe.model.Producto
import mx.edu.utng.cafe.ui.components.ProductCard
import mx.edu.utng.cafe.viewModell.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(
    viewModel: ProductoViewModel = viewModel()
) {
    val productos by viewModel.productos.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var productoEdit by remember { mutableStateOf<Producto?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    productoEdit = null
                    showDialog = true
                }
            ) {
                Text("+", fontSize = 24.sp)
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Text(
                    "Catálogo de Productos",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(productos, key = { it.id }) { producto ->
                ProductCard(
                    producto = producto,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        productoEdit = producto
                        showDialog = true
                    },
                    onDelete = {producto.id}
                )

        }
    }
}

            if (showDialog) {
        ProductDialog(
            producto = productoEdit,
            onDismiss = { showDialog = false },
            onSave = {
                if (it.id.isBlank())
                    viewModel.addProducto(it)
                else
                    viewModel.updateProducto(it)

                showDialog = false
            },
            onDelete = {
                viewModel.deleteProducto(it)
                showDialog = false
            }
        )
    }
}
@Composable
fun ProductDialog(
    producto: Producto?,
    onDismiss: () -> Unit,
    onSave: (Producto) -> Unit,
    onDelete: (String) -> Unit
) {

    var nombre by remember { mutableStateOf(producto?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(producto?.descripcion ?: "") }
    var precio by remember { mutableStateOf(producto?.precio?.toString() ?: "") }
    var disponible by remember { mutableStateOf(producto?.disponible ?: true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Producto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(descripcion, { descripcion = it }, label = { Text("Descripción") })
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("Precio") }
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = disponible, onCheckedChange = { disponible = it })
                    Text("Disponible")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    Producto(
                        id = producto?.id ?: "",
                        nombre = nombre,
                        descripcion = descripcion,
                        precio = precio.toDoubleOrNull() ?: 0.0,
                        disponible = disponible
                    )
                )
            }) {
                Text("Guardar")
            }
        }
    )
}
