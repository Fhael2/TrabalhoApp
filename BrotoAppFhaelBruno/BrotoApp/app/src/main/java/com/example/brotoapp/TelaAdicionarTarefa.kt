package com.example.brotoapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TelaAdicionarTarefa(
    navController: NavController,
    modifier: Modifier = Modifier,
    plantas: List<Plant>,
    onSalvar: (titulo: String, descricao: String, plantaId: Int) -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var plantaId by remember { mutableStateOf(-1) }
    var erro by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título da tarefa") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Text(text = "Planta", style = MaterialTheme.typography.titleMedium)

        if (plantas.isEmpty()) {
            Text("Cadastre uma planta primeiro")
        }

        plantas.forEach { planta ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { plantaId = planta.id },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = plantaId == planta.id,
                    onClick = { plantaId = planta.id }
                )
                Text(planta.name)
            }
        }

        if (erro.isNotEmpty()) {
            Text(text = erro, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                when {
                    titulo.isBlank() -> erro = "Dê um título pra tarefa"
                    plantaId == -1 -> erro = "Escolha uma planta"
                    else -> {
                        erro = ""
                        onSalvar(titulo, descricao, plantaId)
                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar")
        }
    }
}
