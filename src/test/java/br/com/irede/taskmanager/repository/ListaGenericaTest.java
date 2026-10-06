package br.com.irede.taskmanager.repository;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListaGenericaTest {
    @Test
    void deveAdicionarSubtiposECopiarParaUmSupertipo() {
        ListaGenerica<Number> numeros = new ListaGenerica<>();
        List<Integer> inteiros = List.of(1, 2, 3);
        List<Object> destino = new ArrayList<>();

        numeros.adicionarTodos(inteiros);
        numeros.copiarPara(destino);

        assertEquals(List.of(1, 2, 3), destino);
    }

    @Test
    void deveFornecerUmaListaImutavel() {
        ListaGenerica<String> lista = new ListaGenerica<>();
        lista.adicionar("Java");

        assertEquals(List.of("Java"), lista.listar());
    }
}

