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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TelaListaTarefas(
    navController: NavController,
    modifier: Modifier = Modifier,
    tarefas: List<CareTask>,
    plantas: List<Plant>,
    onMarcar: (Int) -> Unit,
    onRemover: (Int) -> Unit
) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        if (tarefas.isEmpty()) {
            Text(
                text = "Nenhuma tarefa cadastrada ainda",
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = tarefas, key = { it.id }) { tarefa ->
                    val nomePlanta = plantas.find { it.id == tarefa.plantId }?.name ?: "-"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate(Rotas.detalheTarefa(tarefa.id)) },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = tarefa.done,
                                onCheckedChange = { onMarcar(tarefa.id) }
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = tarefa.title,
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = if (tarefa.done) {
                                        TextDecoration.LineThrough
                                    } else {
                                        TextDecoration.None
                                    }
                                )
                                Text(text = "Planta: $nomePlanta")
                            }
                            IconButton(onClick = { onRemover(tarefa.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remover tarefa")
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = { navController.navigate(Rotas.NOVA_TAREFA) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("+ Nova Tarefa")
        }
    }
}
