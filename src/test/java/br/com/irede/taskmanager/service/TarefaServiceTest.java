package br.com.irede.taskmanager.service;

import br.com.irede.taskmanager.model.Tarefa;
import br.com.irede.taskmanager.repository.Repositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TarefaServiceTest {
    private RepositorioEmMemoria repositorio;
    private TarefaService service;

    @BeforeEach
    void preparar() {
        repositorio = new RepositorioEmMemoria();
        service = new TarefaService(repositorio);
    }

    @Test
    void deveValidarEPrepararDadosAntesDeSalvar() throws SQLException {
        Tarefa tarefa = service.salvar(new Tarefa("  Estudar JDBC  ", "  Fazer exercícios  ", false));

        assertEquals(1L, tarefa.getId());
        assertEquals("Estudar JDBC", tarefa.getTitulo());
        assertEquals("Fazer exercícios", tarefa.getDescricao());
    }

    @Test
    void naoDeveSalvarTituloVazio() {
        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> service.salvar(new Tarefa("   ", "Descrição", false)));

        assertEquals("Informe o título da tarefa.", erro.getMessage());
    }

    @Test
    void naoDeveSalvarTituloComMaisDeCemCaracteres() {
        Tarefa tarefa = new Tarefa("a".repeat(101), "", false);

        assertThrows(IllegalArgumentException.class, () -> service.salvar(tarefa));
    }

    @Test
    void deveAlternarConclusao() throws SQLException {
        Tarefa tarefa = service.salvar(new Tarefa("Teste", "", false));

        service.alternarConclusao(tarefa);

        assertTrue(repositorio.buscarPorId(tarefa.getId()).orElseThrow().isConcluida());
    }

    private static class RepositorioEmMemoria implements Repositorio<Tarefa, Long> {
        private final List<Tarefa> tarefas = new ArrayList<>();
        private long proximoId = 1;

        @Override
        public Tarefa salvar(Tarefa tarefa) {
            tarefa.setId(proximoId++);
            tarefas.add(tarefa);
            return tarefa;
        }

        @Override
        public Optional<Tarefa> buscarPorId(Long id) {
            return tarefas.stream().filter(tarefa -> tarefa.getId().equals(id)).findFirst();
        }

        @Override
        public List<Tarefa> listar() {
            return List.copyOf(tarefas);
        }

        @Override
        public void atualizar(Tarefa tarefaAtualizada) {
            remover(tarefaAtualizada.getId());
            tarefas.add(tarefaAtualizada);
        }

        @Override
        public void remover(Long id) {
            tarefas.removeIf(tarefa -> tarefa.getId().equals(id));
        }
    }
}

