package com.example.brotoapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TelaResumo(
    modifier: Modifier = Modifier,
    plantas: List<Plant>,
    tarefas: List<CareTask>,
    onRegarAtrasadas: () -> Unit
) {
    val atrasadas = plantas.count { it.precisaRegar() }
    val pendentes = tarefas.count { !it.done }
    val concluidas = tarefas.count { it.done }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CartaoNumero("Plantas cadastradas", plantas.size)
        CartaoNumero("Plantas pra regar", atrasadas)
        CartaoNumero("Tarefas pendentes", pendentes)
        CartaoNumero("Tarefas concluídas", concluidas)

        Button(
            onClick = onRegarAtrasadas,
            enabled = atrasadas > 0,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Regar todas as atrasadas")
        }
    }
}

@Composable
private fun CartaoNumero(titulo: String, valor: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = titulo)
            Text(
                text = valor.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
