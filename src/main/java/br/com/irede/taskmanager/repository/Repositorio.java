package br.com.irede.taskmanager.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repositorio<T, ID> {
    T salvar(T entidade) throws SQLException;

    Optional<T> buscarPorId(ID id) throws SQLException;

    List<T> listar() throws SQLException;

    void atualizar(T entidade) throws SQLException;

    void remover(ID id) throws SQLException;
}

