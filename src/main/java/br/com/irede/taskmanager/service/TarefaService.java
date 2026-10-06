package br.com.irede.taskmanager.service;

import br.com.irede.taskmanager.model.Tarefa;
import br.com.irede.taskmanager.repository.Repositorio;

import java.sql.SQLException;
import java.util.List;

public class TarefaService {
    private final Repositorio<Tarefa, Long> repositorio;

    public TarefaService(Repositorio<Tarefa, Long> repositorio) {
        this.repositorio = repositorio;
    }

    public List<Tarefa> listar() throws SQLException {
        return repositorio.listar();
    }

    public Tarefa salvar(Tarefa tarefa) throws SQLException {
        validar(tarefa);
        return repositorio.salvar(tarefa);
    }

    public void atualizar(Tarefa tarefa) throws SQLException {
        if (tarefa.getId() == null) {
            throw new IllegalArgumentException("A tarefa precisa estar cadastrada para ser atualizada.");
        }
        validar(tarefa);
        repositorio.atualizar(tarefa);
    }

    public void alternarConclusao(Tarefa tarefa) throws SQLException {
        tarefa.setConcluida(!tarefa.isConcluida());
        atualizar(tarefa);
    }

    public void remover(Tarefa tarefa) throws SQLException {
        if (tarefa == null || tarefa.getId() == null) {
            throw new IllegalArgumentException("Selecione uma tarefa cadastrada.");
        }
        repositorio.remover(tarefa.getId());
    }

    private void validar(Tarefa tarefa) {
        if (tarefa == null) {
            throw new IllegalArgumentException("A tarefa é obrigatória.");
        }
        String titulo = tarefa.getTitulo() == null ? "" : tarefa.getTitulo().trim();
        if (titulo.isEmpty()) {
            throw new IllegalArgumentException("Informe o título da tarefa.");
        }
        if (titulo.length() > 100) {
            throw new IllegalArgumentException("O título deve ter no máximo 100 caracteres.");
        }
        tarefa.setTitulo(titulo);
        tarefa.setDescricao(tarefa.getDescricao() == null ? "" : tarefa.getDescricao().trim());
    }
}

