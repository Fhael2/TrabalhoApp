package com.example.brotoapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TelaAdicionarPlanta(
    navController: NavController,
    modifier: Modifier = Modifier,
    onSalvar: (nome: String, especie: String, frequencia: Int) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var frequenciaTexto by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf("") }

    val coresInputs = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface
    )

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome da planta") },
            modifier = Modifier.fillMaxWidth(),
            colors = coresInputs
        )

        OutlinedTextField(
            value = especie,
            onValueChange = { especie = it },
            label = { Text("Espécie") },
            modifier = Modifier.fillMaxWidth(),
            colors = coresInputs
        )

        OutlinedTextField(
            value = frequenciaTexto,
            onValueChange = { frequenciaTexto = it },
            label = { Text("Frequência de rega (dias)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = coresInputs
        )

        if (erro.isNotEmpty()) {
            Text(text = erro, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                val frequencia = frequenciaTexto.toIntOrNull()
                when {
                    nome.isBlank() || especie.isBlank() -> erro = "Preencha todos os campos"
                    frequencia == null || frequencia <= 0 -> erro = "Frequência de rega inválida"
                    else -> {
                        erro = ""
                        onSalvar(nome, especie, frequencia)
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
