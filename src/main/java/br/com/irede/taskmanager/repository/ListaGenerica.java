package br.com.irede.taskmanager.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListaGenerica<T> {
    private final List<T> elementos = new ArrayList<>();

    public void adicionar(T elemento) {
        elementos.add(elemento);
    }

    public void adicionarTodos(Collection<? extends T> novosElementos) {
        elementos.addAll(novosElementos);
    }

    public void copiarPara(Collection<? super T> destino) {
        destino.addAll(elementos);
    }

    public List<T> listar() {
        return List.copyOf(elementos);
    }
}

