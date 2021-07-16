package fxui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        App.launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hageavfall teller");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("src/main/java/fxui/GUI.fxml"))));
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
}
