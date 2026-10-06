package br.com.irede.taskmanager;

import br.com.irede.taskmanager.controller.TarefaListController;
import br.com.irede.taskmanager.dao.TarefaDAO;
import br.com.irede.taskmanager.database.Conexao;
import br.com.irede.taskmanager.database.DatabaseInitializer;
import br.com.irede.taskmanager.service.TarefaService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        Conexao conexao = new Conexao("jdbc:sqlite:taskmanager.db");
        new DatabaseInitializer(conexao).inicializar();

        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/br/com/irede/taskmanager/view/main-view.fxml"));
        Scene scene = new Scene(loader.load(), 850, 560);
        scene.getStylesheets().add(MainApplication.class
                .getResource("/br/com/irede/taskmanager/css/style.css").toExternalForm());

        TarefaListController controller = loader.getController();
        controller.configurar(new TarefaService(new TarefaDAO(conexao)));

        stage.setTitle("TaskManager");
        stage.setMinWidth(720);
        stage.setMinHeight(480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

