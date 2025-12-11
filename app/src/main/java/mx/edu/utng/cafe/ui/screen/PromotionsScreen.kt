package mx.edu.utng.cafe.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.edu.utng.cafe.model.Promocion
import mx.edu.utng.cafe.ui.components.PromoCard
import mx.edu.utng.cafe.ui.viewmodel.PromocionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromotionsScreen(
    viewModel: PromocionViewModel = viewModel()
) {
    val promociones by viewModel.promos.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var promoEdit by remember { mutableStateOf<Promocion?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    promoEdit = null
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
                    "Promociones",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(promociones, key = { it.id }) { promo ->
                PromoCard(
                    promocion = promo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    onClick = {
                        promoEdit = promo
                        showDialog = true
                    }
                )
            }
        }
    }

    if (showDialog) {
        PromotionDialog(
            promo = promoEdit,
            onDismiss = { showDialog = false },
            onSave = { promo ->

                if (promo.id.isBlank()) {
                    // CREAR promoción
                    viewModel.addPromocion(
                        nombre = promo.nombre,
                        descripcion = promo.descripcion,
                        puntosRequeridos = promo.puntosRequeridos,
                        fechaInicio = promo.fechaInicio,
                        fechaFin = promo.fechaFin
                    )
                } else {
                    // ACTUALIZAR promoción
                    viewModel.updatePromocion(promo)
                }

                showDialog = false
            },
            onDelete = { id ->
                viewModel.deletePromocion(id)
                showDialog = false
            }
        )
    }

}

/* -------------------------
   DIALOG PARA CREAR / EDITAR
   Incluye: nombre, descripción, puntos, fechaInicio, fechaFin
   ------------------------- */
@Composable
fun PromotionDialog(
    promo: Promocion?,
    onDismiss: () -> Unit,
    onSave: (Promocion) -> Unit,
    onDelete: (String) -> Unit
) {
    // Campos del formulario
    var nombre by remember { mutableStateOf(promo?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(promo?.descripcion ?: "") }
    var puntosRequeridos by remember { mutableStateOf(promo?.puntosRequeridos ?: "") }
    var fechaInicio by remember { mutableStateOf(promo?.fechaInicio ?: "") }
    var fechaFin by remember { mutableStateOf(promo?.fechaFin ?: "") }

    // Validación mínima: nombre y puntos
    val canConfirm = nombre.isNotBlank() && puntosRequeridos.filter { it.isDigit() }.isNotEmpty()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Nueva Promoción") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = puntosRequeridos,
                    onValueChange = { new ->
                        // permitir solo dígitos
                        puntosRequeridos = new.filter { it.isDigit() }
                    },
                    label = { Text("Puntos requeridos") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Fechas
                OutlinedTextField(
                    value = fechaInicio,
                    onValueChange = { fechaInicio = it },
                    label = { Text("Fecha inicio (yyyy-mm-dd)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = fechaFin,
                    onValueChange = { fechaFin = it },
                    label = { Text("Fecha fin (yyyy-mm-dd)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Construimos el objeto Promocion con los datos del formulario
                    val newPromo = Promocion(
                        id = promo?.id ?: "", // vacío = crear
                        nombre = nombre.trim(),
                        descripcion = descripcion.trim(),
                        puntosRequeridos = puntosRequeridos.trim(),
                        fechaInicio = fechaInicio.trim(),
                        fechaFin = fechaFin.trim()
                    )
                    onSave(newPromo)
                },
                enabled = canConfirm
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Row {
                if (promo != null) {
                    TextButton(
                        onClick = { onDelete(promo.id) },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Eliminar")
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    )
}
