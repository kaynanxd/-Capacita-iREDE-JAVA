package br.com.irede.taskmanager.dao;

import br.com.irede.taskmanager.database.Conexao;
import br.com.irede.taskmanager.model.Tarefa;
import br.com.irede.taskmanager.repository.Repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TarefaDAO implements Repositorio<Tarefa, Long> {
    private final Conexao conexao;

    public TarefaDAO(Conexao conexao) {
        this.conexao = conexao;
    }

    @Override
    public Tarefa salvar(Tarefa tarefa) throws SQLException {
        String sql = "INSERT INTO tarefas (titulo, descricao, concluida) VALUES (?, ?, ?)";
        try (Connection connection = conexao.abrir();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencher(statement, tarefa);
            statement.executeUpdate();
            try (ResultSet chaves = statement.getGeneratedKeys()) {
                if (chaves.next()) {
                    tarefa.setId(chaves.getLong(1));
                }
            }
            return tarefa;
        }
    }

    @Override
    public Optional<Tarefa> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, titulo, descricao, concluida FROM tarefas WHERE id = ?";
        try (Connection connection = conexao.abrir();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultado = statement.executeQuery()) {
                return resultado.next() ? Optional.of(mapear(resultado)) : Optional.empty();
            }
        }
    }

    @Override
    public List<Tarefa> listar() throws SQLException {
        String sql = "SELECT id, titulo, descricao, concluida FROM tarefas ORDER BY id DESC";
        List<Tarefa> tarefas = new ArrayList<>();
        try (Connection connection = conexao.abrir();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultado = statement.executeQuery()) {
            while (resultado.next()) {
                tarefas.add(mapear(resultado));
            }
        }
        return tarefas;
    }

    @Override
    public void atualizar(Tarefa tarefa) throws SQLException {
        String sql = "UPDATE tarefas SET titulo = ?, descricao = ?, concluida = ? WHERE id = ?";
        try (Connection connection = conexao.abrir();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            preencher(statement, tarefa);
            statement.setLong(4, tarefa.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void remover(Long id) throws SQLException {
        String sql = "DELETE FROM tarefas WHERE id = ?";
        try (Connection connection = conexao.abrir();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    private void preencher(PreparedStatement statement, Tarefa tarefa) throws SQLException {
        statement.setString(1, tarefa.getTitulo());
        statement.setString(2, tarefa.getDescricao());
        statement.setBoolean(3, tarefa.isConcluida());
    }

    private Tarefa mapear(ResultSet resultado) throws SQLException {
        return new Tarefa(
                resultado.getLong("id"),
                resultado.getString("titulo"),
                resultado.getString("descricao"),
                resultado.getBoolean("concluida")
        );
    }
}

