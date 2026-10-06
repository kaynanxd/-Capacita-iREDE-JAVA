package br.com.irede.taskmanager.model;

import java.util.Objects;

public class Tarefa {
    private Long id;
    private String titulo;
    private String descricao;
    private boolean concluida;

    public Tarefa() {
    }

    public Tarefa(String titulo, String descricao, boolean concluida) {
        this(null, titulo, descricao, concluida);
    }

    public Tarefa(Long id, String titulo, String descricao, boolean concluida) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.concluida = concluida;
    }

    public void concluir() {
        concluida = true;
    }

    public void reabrir() {
        concluida = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof Tarefa tarefa)) return false;
        return id != null && Objects.equals(id, tarefa.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
}

