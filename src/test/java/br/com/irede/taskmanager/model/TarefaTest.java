package br.com.irede.taskmanager.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TarefaTest {
    @Test
    void deveConcluirEReabrirTarefa() {
        Tarefa tarefa = new Tarefa("Estudar", "Revisar Java", false);

        tarefa.concluir();
        assertTrue(tarefa.isConcluida());

        tarefa.reabrir();
        assertFalse(tarefa.isConcluida());
    }
}

