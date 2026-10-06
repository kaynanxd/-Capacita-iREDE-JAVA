package br.com.irede.taskmanager.dao;

import br.com.irede.taskmanager.database.Conexao;
import br.com.irede.taskmanager.database.DatabaseInitializer;
import br.com.irede.taskmanager.model.Tarefa;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TarefaDAOTest {
    private Connection mantenedora;
    private TarefaDAO dao;

    @BeforeEach
    void prepararBanco() throws SQLException {
        String nome = "taskmanager-" + UUID.randomUUID();
        String url = "jdbc:sqlite:file:" + nome + "?mode=memory&cache=shared";
        mantenedora = DriverManager.getConnection(url);
        Conexao conexao = new Conexao(url);
        new DatabaseInitializer(conexao).inicializar();
        dao = new TarefaDAO(conexao);
    }

    @AfterEach
    void fecharBanco() throws SQLException {
        mantenedora.close();
    }

    @Test
    void deveExecutarCrudCompleto() throws SQLException {
        Tarefa tarefa = dao.salvar(new Tarefa("Implementar DAO", "Usar SQLite", false));

        assertTrue(tarefa.getId() > 0);
        assertEquals(1, dao.listar().size());
        assertEquals("Implementar DAO", dao.buscarPorId(tarefa.getId()).orElseThrow().getTitulo());

        tarefa.setTitulo("Testar DAO");
        tarefa.concluir();
        dao.atualizar(tarefa);

        Tarefa atualizada = dao.buscarPorId(tarefa.getId()).orElseThrow();
        assertEquals("Testar DAO", atualizada.getTitulo());
        assertTrue(atualizada.isConcluida());

        dao.remover(tarefa.getId());
        assertFalse(dao.buscarPorId(tarefa.getId()).isPresent());
    }
}

