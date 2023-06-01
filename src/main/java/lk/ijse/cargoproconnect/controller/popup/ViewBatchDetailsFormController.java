package lk.ijse.cargoproconnect.controller.popup;

import com.jfoenix.controls.JFXTextField;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.util.Duration;
import lk.ijse.cargoproconnect.dto.BatchDTO;
import lk.ijse.cargoproconnect.util.Colors;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewBatchDetailsFormController implements Initializable {

    @FXML
    private Label lblBatchId;

    @FXML
    private JFXTextField txtTotalWeight;

    @FXML
    private JFXTextField txtCurrentWeight;

    @FXML
    private JFXTextField txtAvailableWeight;

    @FXML
    private JFXTextField txtDeliverAddress;

    @FXML
    private JFXTextField txtNumberOfOrders;

    @FXML
    private JFXTextField txtShipmentType;

    @FXML
    private JFXTextField txtShipmentDate;

    @FXML
    private JFXTextField txtDeliverDate;

    @FXML
    private Label lblStatus;

    @FXML
    private ProgressIndicator progressInDicator;

    private static BatchDTO batch;

    public static void setBatchDetail(BatchDTO batch) {
        ViewBatchDetailsFormController.batch = batch;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setData();
        setAnimation();
    }

    private void setAnimation() {
        double startValue = 0.0; // Starting value of the progress indicator
        double availableWeight = batch.getTotalWeight() - batch.getCurrentWeight() ;
        double availablePercentage = (availableWeight / batch.getTotalWeight()) * 100;
        double percentage = 100 - availablePercentage;
        double endValue = percentage / 100.0;

        progressInDicator.setStyle("-fx-progress-color: "+ Colors.BLUE +";");
        // Ending value of the progress indicator
        Duration duration = Duration.seconds(0.5); // Duration of the animation (in seconds)
        KeyFrame startFrame = new KeyFrame(Duration.ZERO, new KeyValue(progressInDicator.progressProperty(), startValue));
        KeyFrame endFrame = new KeyFrame(duration, new KeyValue(progressInDicator.progressProperty(), endValue));
        Timeline timeline = new Timeline(startFrame, endFrame);
        timeline.play();
    }

    private void setData() {
        lblBatchId.setText(batch.getId());
        lblStatus.setText(batch.getAvailableStatus() == 1 ? "Available" : "Unavailable");
        lblStatus.setStyle(lblStatus.getText().equalsIgnoreCase("Available") ? "-fx-font-weight: bold;-fx-text-fill: "+ Colors.GREEN +";" : "-fx-font-weight: bold;-fx-text-fill: "+ Colors.RED +";-fx-font-size: 15;");
        txtDeliverAddress.setText(batch.getDeliveryAddress());
        txtShipmentType.setText(batch.getShipmentType());
        txtShipmentDate.setText(batch.getSDate());
        txtDeliverDate.setText(batch.getDDate());
        txtNumberOfOrders.setText(String.valueOf(batch.getNoOfOrders()));
        txtTotalWeight.setText(batch.getTotalWeight() + " Kg");
        txtCurrentWeight.setText(batch.getCurrentWeight() + " Kg");
        txtAvailableWeight.setText(batch.getTotalWeight() - batch.getCurrentWeight() + " Kg");
    }
}