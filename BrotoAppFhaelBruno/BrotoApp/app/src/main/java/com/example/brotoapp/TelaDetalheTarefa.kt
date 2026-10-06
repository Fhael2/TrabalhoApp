package com.example.brotoapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TelaDetalheTarefa(
    navController: NavController,
    modifier: Modifier = Modifier,
    tarefa: CareTask,
    planta: Plant?,
    onAlternar: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = tarefa.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        if (tarefa.description.isNotBlank()) {
            Text(text = tarefa.description)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = tarefa.done, onCheckedChange = { onAlternar() })
            Text(if (tarefa.done) "Concluída" else "Pendente")
        }

        if (planta != null) {

            Card(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Planta", style = MaterialTheme.typography.labelLarge)
                    Text(text = planta.name, fontWeight = FontWeight.Bold)
                    Text(text = planta.species, fontStyle = FontStyle.Italic)
                    Text(text = planta.textoRega())
                    Button(
                        onClick = { navController.navigate(Rotas.detalhePlanta(planta.id)) },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Ver planta")
                    }
                }
            }
        }
    }
}
