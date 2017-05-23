package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Glowny plik projektu, uruchamia GUI i przekazuje kontrole do odpowiednich klas
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        primaryStage.setTitle("Kryptografia");
        primaryStage.setScene(new Scene(root, 850, 800));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
