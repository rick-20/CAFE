package mx.edu.utng.cafe.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mx.edu.utng.cafe.ui.components.PromotionCard
import mx.edu.utng.cafe.ui.viewmodel.homeViewmodel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: homeViewmodel = viewModel()
) {

    // 🔥 NO LO QUITES
    // Ejecuta cargarDatos() automáticamente al entrar
    LaunchedEffect(Unit) {
    }

    val usuario = viewModel.usuario
    val promociones = viewModel.promos
    val loading = viewModel.loading
    val error = viewModel.error

    /* ================= LOADING ================= */
    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    /* ================= ERROR ================= */
    if (error != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Error: $error", color = Color.Red)
        }
        return
    }

    /* ================= PANTALLA PRINCIPAL ================= */
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        /* ========= TARJETA DE USUARIO / PUNTOS ========= */
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF6200EE),
                                    Color(0xFF9C27B0)
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Text(
                            text = "Hola, ${usuario?.nombre?.toString()}", // <-- Muestra el nombre
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Mis puntos:",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${ usuario?.puntosAcumulados?.toString() }",
                            color = Color.White,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        /* ========= PROMOCIONES ========= */
        item {
            Text(
                "Promociones destacadas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(promociones) { promo ->
            PromotionCard(
                nombre = promo.nombre,
                descripcion = promo.descripcion,
                puntos = promo.puntosRequeridos.toInt()
            )
        }
    }
}
