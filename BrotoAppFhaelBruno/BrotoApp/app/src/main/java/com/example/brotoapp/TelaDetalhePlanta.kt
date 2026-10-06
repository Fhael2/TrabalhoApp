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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TelaDetalhePlanta(
    navController: NavController,
    modifier: Modifier = Modifier,
    planta: Plant,
    tarefas: List<CareTask>,
    onRegarAgora: () -> Unit,
    onMudarFrequencia: (Int) -> Unit,
    onMarcarTarefa: (Int) -> Unit
) {
    val ultimaRega = if (planta.daysSinceWatered == 0) "hoje" else "há ${planta.daysSinceWatered} dia(s)"

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = planta.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(text = planta.species, fontStyle = FontStyle.Italic)
        Text(text = "Última rega: $ultimaRega")


        Text(
            text = planta.textoRega(),
            fontWeight = FontWeight.Bold,
            color = if (planta.precisaRegar()) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.primary
            }
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Regar a cada ${planta.wateringFrequencyDays} dia(s)",
                modifier = Modifier.weight(1f)
            )
            OutlinedButton(onClick = {
                if (planta.wateringFrequencyDays > 1) {
                    onMudarFrequencia(planta.wateringFrequencyDays - 1)
                }
            }) {
                Text("-")
            }
            OutlinedButton(
                onClick = { onMudarFrequencia(planta.wateringFrequencyDays + 1) },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("+")
            }
        }

        Button(
            onClick = onRegarAgora,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Regar agora")
        }

        Text(
            text = "Tarefas desta planta",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp)
        )

        if (tarefas.isEmpty()) {
            Text("Nenhuma tarefa pra essa planta")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = tarefas, key = { it.id }) { tarefa ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate(Rotas.detalheTarefa(tarefa.id)) }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = tarefa.done,
                                onCheckedChange = { onMarcarTarefa(tarefa.id) }
                            )
                            Text(
                                text = tarefa.title,
                                textDecoration = if (tarefa.done) {
                                    TextDecoration.LineThrough
                                } else {
                                    TextDecoration.None
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
