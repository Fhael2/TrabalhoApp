package com.example.brotoapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

private data class ItemAba(val rota: String, val titulo: String, val icone: ImageVector)

private val abas = listOf(
    ItemAba(Rotas.PLANTAS, "Plantas", Icons.Default.Home),
    ItemAba(Rotas.TAREFAS, "Tarefas", Icons.Default.Check),
    ItemAba(Rotas.RESUMO, "Resumo", Icons.Default.Info)
)

private fun tituloDaTela(rota: String?) = when (rota) {
    Rotas.PLANTAS -> "Minhas Plantas"
    Rotas.TAREFAS -> "Tarefas de Cuidado"
    Rotas.RESUMO -> "Resumo"
    Rotas.NOVA_PLANTA -> "Nova Planta"
    Rotas.NOVA_TAREFA -> "Nova Tarefa"
    Rotas.DETALHE_PLANTA -> "Detalhes da Planta"
    Rotas.DETALHE_TAREFA -> "Detalhes da Tarefa"
    else -> "BrotoApp"
}


private fun plantasIniciais() = listOf(
    Plant(1, "Samambaia", "Nephrolepis exaltata", 3, 2),
    Plant(2, "Suculenta", "Echeveria", 10, 12),
    Plant(3, "Espada-de-São-Jorge", "Sansevieria trifasciata", 14, 6)
)

private fun tarefasIniciais() = listOf(
    CareTask(1, "Adubar", "Usar adubo líquido diluído em água", 1),
    CareTask(2, "Podar folhas secas", "Tirar as folhas marrons com tesoura limpa", 1),
    CareTask(3, "Trocar o vaso", "Escolher um vaso maior e com furo pra drenagem", 3),
    CareTask(4, "Mudar de lugar", "Deixar mais perto da janela, com sol da manhã", 2)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrotoApp() {
    val navController = rememberNavController()

    val plantas = remember { mutableStateListOf<Plant>().apply { addAll(plantasIniciais()) } }
    val tarefas = remember { mutableStateListOf<CareTask>().apply { addAll(tarefasIniciais()) } }
    var proximoIdPlanta by remember { mutableStateOf(4) }
    var proximoIdTarefa by remember { mutableStateOf(5) }

    val entradaAtual by navController.currentBackStackEntryAsState()
    val rotaAtual = entradaAtual?.destination?.route
    val naAbaPrincipal = abas.any { it.rota == rotaAtual }

    fun regar(id: Int) {
        val i = plantas.indexOfFirst { it.id == id }
        if (i >= 0) plantas[i] = plantas[i].copy(daysSinceWatered = 0)
    }

    fun mudarFrequencia(id: Int, novaFrequencia: Int) {
        val i = plantas.indexOfFirst { it.id == id }
        if (i >= 0) plantas[i] = plantas[i].copy(wateringFrequencyDays = novaFrequencia)
    }

    fun alternarTarefa(id: Int) {
        val i = tarefas.indexOfFirst { it.id == id }
        if (i >= 0) tarefas[i] = tarefas[i].copy(done = !tarefas[i].done)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tituloDaTela(rotaAtual)) },
                navigationIcon = {
                    if (!naAbaPrincipal) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            if (naAbaPrincipal) {
                NavigationBar {
                    abas.forEach { aba ->
                        NavigationBarItem(
                            selected = rotaAtual == aba.rota,
                            onClick = {
                                navController.navigate(aba.rota) {
                                    popUpTo(Rotas.PLANTAS) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(aba.icone, contentDescription = aba.titulo) },
                            label = { Text(aba.titulo) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rotas.PLANTAS,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rotas.PLANTAS) {
                TelaListaPlantas(
                    navController = navController,
                    plantas = plantas,
                    onRegar = { id -> regar(id) },
                    onRemover = { id ->
                        plantas.removeAll { it.id == id }
                        tarefas.removeAll { it.plantId == id }
                    }
                )
            }

            composable(Rotas.TAREFAS) {
                TelaListaTarefas(
                    navController = navController,
                    tarefas = tarefas,
                    plantas = plantas,
                    onMarcar = { id -> alternarTarefa(id) },
                    onRemover = { id -> tarefas.removeAll { it.id == id } }
                )
            }

            composable(Rotas.RESUMO) {
                TelaResumo(
                    plantas = plantas,
                    tarefas = tarefas,
                    onRegarAtrasadas = {
                        for (i in plantas.indices) {
                            if (plantas[i].precisaRegar()) {
                                plantas[i] = plantas[i].copy(daysSinceWatered = 0)
                            }
                        }
                    }
                )
            }

            composable(Rotas.NOVA_PLANTA) {
                TelaAdicionarPlanta(
                    navController = navController,
                    onSalvar = { nome, especie, frequencia ->
                        plantas.add(Plant(proximoIdPlanta, nome, especie, frequencia, 0))
                        proximoIdPlanta++
                    }
                )
            }

            composable(Rotas.NOVA_TAREFA) {
                TelaAdicionarTarefa(
                    navController = navController,
                    plantas = plantas,
                    onSalvar = { titulo, descricao, plantaId ->
                        tarefas.add(CareTask(proximoIdTarefa, titulo, descricao, plantaId))
                        proximoIdTarefa++
                    }
                )
            }

            composable(route = Rotas.DETALHE_PLANTA) { backStackEntry ->

                val id = backStackEntry.arguments?.getString("id")?.toInt() ?: -1
                val planta = plantas.find { it.id == id }
                if (planta != null) {
                    TelaDetalhePlanta(
                        navController = navController,
                        planta = planta,
                        tarefas = tarefas.filter { it.plantId == id },
                        onRegarAgora = { regar(id) },
                        onMudarFrequencia = { nova -> mudarFrequencia(id, nova) },
                        onMarcarTarefa = { tarefaId -> alternarTarefa(tarefaId) }
                    )
                }
            }

            composable(route = Rotas.DETALHE_TAREFA) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toInt() ?: -1
                val tarefa = tarefas.find { it.id == id }
                if (tarefa != null) {
                    TelaDetalheTarefa(
                        navController = navController,
                        tarefa = tarefa,
                        planta = plantas.find { it.id == tarefa.plantId },
                        onAlternar = { alternarTarefa(id) }
                    )
                }
            }
        }
    }
}
