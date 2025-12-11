package mx.edu.utng.cafe.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.edu.utng.cafe.ui.components.OrderCard
import mx.edu.utng.cafe.viewModell.PedidoViewModel

@Composable
fun OrdersScreen(
    userId: String
) {
    val viewModel: PedidoViewModel = viewModel(
        factory = PedidoViewModel.PedidoViewModelFactory(userId)
    )

    val pedidos = viewModel.pedidos.collectAsState().value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            Text(
                text = "Mis Pedidos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        if (pedidos.isEmpty()) {
            item {
                Text(
                    text = "Aún no tienes pedidos",
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        items(pedidos, key = { it.id }) { pedido ->
            OrderCard(pedido)
        }
    }
}
