package br.com.irede.taskmanager.controller;

import br.com.irede.taskmanager.model.Tarefa;
import br.com.irede.taskmanager.service.TarefaService;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class TarefaFormController {
    @FXML
    private TextField campoTitulo;
    @FXML
    private TextArea campoDescricao;
    @FXML
    private CheckBox campoConcluida;
    @FXML
    private Label mensagemErro;

    private TarefaService service;
    private Tarefa tarefa;

    public void configurar(TarefaService service, Tarefa tarefa) {
        this.service = service;
        this.tarefa = tarefa;
        if (tarefa != null) {
            campoTitulo.setText(tarefa.getTitulo());
            campoDescricao.setText(tarefa.getDescricao());
            campoConcluida.setSelected(tarefa.isConcluida());
        }
    }

    @FXML
    private void salvar() {
        try {
            Tarefa dadosFormulario = new Tarefa(
                    tarefa == null ? null : tarefa.getId(),
                    campoTitulo.getText(),
                    campoDescricao.getText(),
                    campoConcluida.isSelected());
            if (tarefa == null) {
                service.salvar(dadosFormulario);
            } else {
                service.atualizar(dadosFormulario);
            }
            fechar();
        } catch (IllegalArgumentException exception) {
            mensagemErro.setText(exception.getMessage());
        } catch (SQLException exception) {
            mensagemErro.setText("Erro ao acessar o banco de dados: " + exception.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        fechar();
    }

    private void fechar() {
        ((Stage) campoTitulo.getScene().getWindow()).close();
    }
}

