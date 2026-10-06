package com.example.brotoapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TelaListaPlantas(
    navController: NavController,
    modifier: Modifier = Modifier,
    plantas: List<Plant>,
    onRegar: (Int) -> Unit,
    onRemover: (Int) -> Unit
) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        if (plantas.isEmpty()) {
            Text(
                text = "Nenhuma planta cadastrada ainda",
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = plantas, key = { it.id }) { planta ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate(Rotas.detalhePlanta(planta.id)) },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = planta.name, fontWeight = FontWeight.Bold)
                                Text(
                                    text = planta.textoRega(),
                                    color = if (planta.precisaRegar()) {
                                        MaterialTheme.colorScheme.error
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    }
                                )
                                Button(
                                    onClick = { onRegar(planta.id) },
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Text("Regar")
                                }
                            }
                            IconButton(onClick = { onRemover(planta.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remover planta")
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = { navController.navigate(Rotas.NOVA_PLANTA) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("+ Nova Planta")
        }
    }
}
