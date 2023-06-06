package lk.ijse.cargoproconnect.controller.popup;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.cargoproconnect.bo.bos.BatchBO;
import lk.ijse.cargoproconnect.bo.bos.impl.BatchBOImpl;
import lk.ijse.cargoproconnect.dto.BatchDTO;
import lk.ijse.cargoproconnect.dto.tm.ViewBatchTM;
import lk.ijse.cargoproconnect.model.BatchModel;
import lk.ijse.cargoproconnect.util.Colors;
import lk.ijse.cargoproconnect.util.NotificationUtil;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ViewBatchFormController implements Initializable {

    @FXML
    private AnchorPane popupRoot;

    @FXML
    private TableView<ViewBatchTM> tableBatch;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colShipmentType;

    @FXML
    private TableColumn<?, ?> colAvailableWeight;

    @FXML
    private TableColumn<?, ?> colSDate;

    @FXML
    private TableColumn<?, ?> colDDate;

    @FXML
    private TableColumn<?, ?> colTo;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colSelect;

    private static ObservableList<ViewBatchTM> list;
    private static LocalDate value;
    private static LocalDate value1;
    static String batchId;

    //Dependency Injection (Property Injection)
    BatchBO batchBO = new BatchBOImpl();

    public static void setSelectedDates(LocalDate value, LocalDate value1) {
        ViewBatchFormController.value = value;
        ViewBatchFormController.value1 = value1;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        setTableData();
    }

    private void setTableData() {
        try {
            list = FXCollections.observableArrayList();
            List<BatchDTO> batches = batchBO.getAvailableBatches();

            for(BatchDTO batch: batches){
                JFXButton button = new JFXButton("SELECT");
                button.setStyle("-fx-background-color: "+ Colors.GREEN +";-fx-border-radius: 10px;-fx-text-fill: "+ Colors.WHITE +";-fx-font-weight: bold;-fx-font-size: 15px");
                setSelectButtonOnAction(button, batch);

                Label label = new Label(batch.getAvailableStatus() == 1 ? "Available" : "Unavailable");
                label.setStyle(label.getText().equalsIgnoreCase("Available") ? "-fx-font-weight: bold;-fx-text-fill: "+ Colors.GREEN +";-fx-font-size: 15;" : "-fx-font-weight: bold;-fx-text-fill: "+ Colors.RED +";-fx-font-size: 15;");

                LocalDate shipmentDate = LocalDate.parse(batch.getSDate());
                LocalDate deliverDate = LocalDate.parse(batch.getDDate());

                if(shipmentDate.isAfter(LocalDate.now()) && deliverDate.isAfter(value) && deliverDate.isBefore(value1)){
                    list.add(new ViewBatchTM(
                            batch.getId(),
                            batch.getShipmentType(),
                            batch.getTotalWeight() - batch.getCurrentWeight(),
                            batch.getSDate(),
                            batch.getDDate(),
                            batch.getDeliveryAddress(),
                            label,
                            button
                    ));
                }
            }
            tableBatch.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tableBatch.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setSelectButtonOnAction(JFXButton button, BatchDTO batch) {
        button.setOnAction(event -> {
            batchId = batch.getId();
            Stage stage = (Stage) popupRoot.getScene().getWindow();
            stage.close();
            NotificationUtil.showNotification("Success", "Successfully "+batch.getId()+" Batch added ", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
        });
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colShipmentType.setCellValueFactory(new PropertyValueFactory<>("shipmentType"));
        colAvailableWeight.setCellValueFactory(new PropertyValueFactory<>("availableWeight"));
        colSDate.setCellValueFactory(new PropertyValueFactory<>("sDate"));
        colDDate.setCellValueFactory(new PropertyValueFactory<>("dDate"));
        colTo.setCellValueFactory(new PropertyValueFactory<>("to"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colSelect.setCellValueFactory(new PropertyValueFactory<>("select"));
    }

    public static String getBatchId(){
        return batchId;
    }
}
