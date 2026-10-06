package br.com.irede.taskmanager.controller;

import br.com.irede.taskmanager.MainApplication;
import br.com.irede.taskmanager.model.Tarefa;
import br.com.irede.taskmanager.repository.ListaGenerica;
import br.com.irede.taskmanager.service.TarefaService;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class TarefaListController {
    @FXML
    private TableView<Tarefa> tabelaTarefas;
    @FXML
    private TableColumn<Tarefa, Number> colunaId;
    @FXML
    private TableColumn<Tarefa, String> colunaTitulo;
    @FXML
    private TableColumn<Tarefa, String> colunaDescricao;
    @FXML
    private TableColumn<Tarefa, String> colunaSituacao;

    private final ObservableList<Tarefa> tarefas = FXCollections.observableArrayList();
    private TarefaService service;

    @FXML
    private void initialize() {
        colunaId.setCellValueFactory(item -> new ReadOnlyLongWrapper(item.getValue().getId()));
        colunaTitulo.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getTitulo()));
        colunaDescricao.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getDescricao()));
        colunaSituacao.setCellValueFactory(item -> new ReadOnlyStringWrapper(
                item.getValue().isConcluida() ? "Concluída" : "Pendente"));
        tabelaTarefas.setItems(tarefas);
        tabelaTarefas.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && selecionada() != null) {
                editar();
            }
        });
    }

    public void configurar(TarefaService service) {
        this.service = service;
        carregarTarefas();
    }

    @FXML
    private void nova() {
        abrirFormulario(null);
    }

    @FXML
    private void editar() {
        Tarefa tarefa = selecionada();
        if (tarefa == null) {
            avisarSelecao();
            return;
        }
        abrirFormulario(tarefa);
    }

    @FXML
    private void alternarConclusao() {
        Tarefa tarefa = selecionada();
        if (tarefa == null) {
            avisarSelecao();
            return;
        }
        try {
            service.alternarConclusao(tarefa);
            carregarTarefas();
        } catch (SQLException | IllegalArgumentException exception) {
            mostrarErro("Não foi possível atualizar a tarefa.", exception);
        }
    }

    @FXML
    private void remover() {
        Tarefa tarefa = selecionada();
        if (tarefa == null) {
            avisarSelecao();
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Remover tarefa");
        confirmacao.setHeaderText("Deseja remover “" + tarefa.getTitulo() + "”? ");
        confirmacao.setContentText("Esta ação não poderá ser desfeita.");
        if (confirmacao.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        try {
            service.remover(tarefa);
            carregarTarefas();
        } catch (SQLException | IllegalArgumentException exception) {
            mostrarErro("Não foi possível remover a tarefa.", exception);
        }
    }

    private void carregarTarefas() {
        try {
            ListaGenerica<Tarefa> lista = new ListaGenerica<>();
            lista.adicionarTodos(service.listar());
            tarefas.clear();
            lista.copiarPara(tarefas);
        } catch (SQLException exception) {
            mostrarErro("Não foi possível carregar as tarefas.", exception);
        }
    }

    private void abrirFormulario(Tarefa tarefa) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class
                    .getResource("/br/com/irede/taskmanager/view/tarefa-form.fxml"));
            Parent conteudo = loader.load();
            TarefaFormController controller = loader.getController();
            controller.configurar(service, tarefa);

            Stage janela = new Stage();
            janela.setTitle(tarefa == null ? "Nova tarefa" : "Editar tarefa");
            janela.initOwner(tabelaTarefas.getScene().getWindow());
            janela.initModality(Modality.WINDOW_MODAL);
            janela.setResizable(false);
            janela.setScene(new javafx.scene.Scene(conteudo));
            janela.getScene().getStylesheets().add(MainApplication.class
                    .getResource("/br/com/irede/taskmanager/css/style.css").toExternalForm());
            janela.showAndWait();
            carregarTarefas();
        } catch (IOException exception) {
            mostrarErro("Não foi possível abrir o formulário.", exception);
        }
    }

    private Tarefa selecionada() {
        return tabelaTarefas.getSelectionModel().getSelectedItem();
    }

    private void avisarSelecao() {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Nenhuma tarefa selecionada");
        alerta.setHeaderText(null);
        alerta.setContentText("Selecione uma tarefa na tabela.");
        alerta.showAndWait();
    }

    private void mostrarErro(String mensagem, Exception exception) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro");
        alerta.setHeaderText(mensagem);
        alerta.setContentText(exception.getMessage());
        alerta.showAndWait();
    }
}

