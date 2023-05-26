package lk.ijse.cargoproconnect;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
//        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/AdminDashBoardForm.fxml"))));
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/LoginForm.fxml"))));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }
}