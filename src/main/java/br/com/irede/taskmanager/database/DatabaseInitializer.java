package br.com.irede.taskmanager.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final String CRIAR_TABELA = """
            CREATE TABLE IF NOT EXISTS tarefas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo VARCHAR(100) NOT NULL,
                descricao TEXT,
                concluida BOOLEAN NOT NULL DEFAULT 0
            )
            """;

    private final Conexao conexao;

    public DatabaseInitializer(Conexao conexao) {
        this.conexao = conexao;
    }

    public void inicializar() throws SQLException {
        try (Connection connection = conexao.abrir();
             Statement statement = connection.createStatement()) {
            statement.execute(CRIAR_TABELA);
        }
    }
}

