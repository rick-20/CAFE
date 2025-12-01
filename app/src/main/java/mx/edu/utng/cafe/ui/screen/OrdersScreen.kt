package mx.edu.utng.cafe.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.cafe.model.Pedido
import mx.edu.utng.cafe.ui.components.OrderCard

@Composable
fun OrdersScreen() {
    val pedidos = listOf(
        Pedido(1, "2025-01-15", 14298.0, "Entregado"),
        Pedido(2, "2025-02-20", 6798.0, "En proceso"),
        Pedido(5, "2025-04-05", 3299.0, "En tránsito")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Mis Pedidos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(pedidos) { pedido ->
            OrderCard(pedido)
        }
    }
}