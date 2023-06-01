package lk.ijse.cargoproconnect.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.cargoproconnect.controller.popup.ViewBatchDetailsFormController;
import lk.ijse.cargoproconnect.controller.update.UpdateBatchFormController;
import lk.ijse.cargoproconnect.dto.BatchDTO;
import lk.ijse.cargoproconnect.dto.tm.BatchTM;
import lk.ijse.cargoproconnect.model.BatchModel;
import lk.ijse.cargoproconnect.util.Colors;
import lk.ijse.cargoproconnect.util.NotificationUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class BatchFormController implements Initializable {

    @FXML
    public AnchorPane rootChange;

    @FXML
    private Pagination pagination;

    @FXML
    private TableView<BatchTM> tableBatch;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableColumn<?, ?> colWeight;

    @FXML
    private TableColumn<?, ?> colCurrentWeight;

    @FXML
    private TableColumn<?, ?> colSDate;

    @FXML
    private TableColumn<?, ?> colDDate;

    @FXML
    private TableColumn<?, ?> colTo;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colEdit;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private HBox btnAddHBox;

    @FXML
    private JFXButton btnExport;

    @FXML
    private JFXButton btnAddTax;

    @FXML
    private HBox btnDeleteHBox;

    @FXML
    private Label lblDelete;

    @FXML
    private JFXButton btnDeleteSelected;

    private JFXCheckBox checkBox;
    private JFXCheckBox checkBoxList;
    private static ObservableList<BatchTM> list;
    private static List<BatchDTO> batches;
    static int ROWS_PER_PAGE = 10;

    @FXML
    void btnAddBatchOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/add/AddNewBatchForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnDeleteSelectedOnAction(ActionEvent event) {
        List<String> ids = new ArrayList<>();
        for (BatchTM batchTM : list) {
            if (batchTM.getCheckBox().isSelected() && batchTM.getCheckBox().isVisible()) {
                ids.add(batchTM.getId());
            }
        }
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to delete?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            try {
                boolean isDeleted = BatchModel.deleteSelectedBatches(ids);
                tableBatch.refresh();
                if (isDeleted) {
                    setTableData();
                    tableBatch.refresh();
                    NotificationUtil.showNotification("Success", "Batches deleted Successfully", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                } else {
                    NotificationUtil.showNotification("Error", "OOPS! Something happen", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        this.checkBox.setSelected(false);
        btnAddHBox.setVisible(true);
        btnDeleteHBox.setVisible(false);
        for (BatchTM batchTM : list) {
            batchTM.getCheckBox().setSelected(false);
        }
    }

    @FXML
    void btnExportOnAction(ActionEvent event) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Data");

        ObservableList<BatchTM> data = tableBatch.getItems();

        for (int i = 0; i < data.size(); i++) {
            BatchTM rowData = data.get(i);
            Row row = sheet.createRow(i);

            org.apache.poi.ss.usermodel.Cell cell1 = row.createCell(0);
            cell1.setCellValue(rowData.getId());

            org.apache.poi.ss.usermodel.Cell cell2 = row.createCell(1);
            cell2.setCellValue(rowData.getShipmentType());

            org.apache.poi.ss.usermodel.Cell cell3 = row.createCell(2);
            cell3.setCellValue(rowData.getTotalWeight());

            org.apache.poi.ss.usermodel.Cell cell4 = row.createCell(3);
            cell4.setCellValue(rowData.getSDate());

            org.apache.poi.ss.usermodel.Cell cell5 = row.createCell(4);
            cell5.setCellValue(rowData.getDDate());

            Cell cell6 = row.createCell(5);
            cell6.setCellValue(rowData.getAvailableStatus().getText());
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fileChooser.showSaveDialog(btnExport.getScene().getWindow());
        if (file == null) {
            return;
        }

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
            NotificationUtil.showNotification("Success", "Data exported to Excel successfully", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
        } catch (IOException ex) {
            NotificationUtil.showNotification("Error", "Error exporting data to Excel:", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                // Ignore error when closing workbook
                System.err.println("Error closing workbook: " + e.getMessage());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hideDeleteSelectedHbox();
        setHeader();
        setCellValueFactory();
        setTableData();
        setSearchFilter();
        setDoubleClickOnAction();
    }

    private void setDoubleClickOnAction() {
        tableBatch.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                try {
                    ViewBatchDetailsFormController.setBatchDetail(batches.get(tableBatch.getSelectionModel().getSelectedIndex()));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/popups/ViewBatchDetailsForm.fxml"))));
                    stage.setResizable(false);
                    stage.setY(225);
                    stage.setX(450);
                    stage.show();
                    stage.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
                        if (!newValue) {
                            stage.close();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void hideDeleteSelectedHbox() {
        btnDeleteHBox.setLayoutX(btnAddHBox.getLayoutX());
        btnDeleteHBox.setLayoutY(btnAddHBox.getLayoutY());
        btnDeleteHBox.setVisible(false);    //hide delete selected button
    }

    private void setHeader() {
        this.checkBox = new JFXCheckBox();
        colEdit.setGraphic(checkBox);
        setCheckBoxOnAction();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("shipmentType"));
        colWeight.setCellValueFactory(new PropertyValueFactory<>("totalWeight"));
        colCurrentWeight.setCellValueFactory(new PropertyValueFactory<>("currentWeight"));
        colSDate.setCellValueFactory(new PropertyValueFactory<>("sDate"));
        colDDate.setCellValueFactory(new PropertyValueFactory<>("dDate"));
        colTo.setCellValueFactory(new PropertyValueFactory<>("to"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("availableStatus"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("Actions"));
        colEdit.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
    }

    private void setTableData() {
        try {
            list = FXCollections.observableArrayList();
            batches = BatchModel.getBatches();

            for (BatchDTO batch : batches) {

                checkBoxList = new JFXCheckBox();
                setCheckBoxOnAction(checkBoxList);

                JFXButton btnView = new JFXButton();
                ImageView viewIcon = new ImageView(new Image("/lk/ijse/cargoproconnect/img/view.png"));
                viewIcon.setFitHeight(20);
                viewIcon.setFitWidth(20);
                btnView.setGraphic(viewIcon);
                btnView.setCursor(Cursor.HAND);
                setViewBtnOnAction(btnView, batch);

                JFXButton btnEdit = new JFXButton();
                ImageView editIcon = new ImageView(new Image("/lk/ijse/cargoproconnect/img/edit.png"));
                editIcon.setFitHeight(20);
                editIcon.setFitWidth(20);
                btnEdit.setGraphic(editIcon);
                btnEdit.setCursor(Cursor.HAND);
                setEditBtnOnAction(btnEdit, batch);

                JFXButton btnDelete = new JFXButton();
                ImageView deleteIcon = new ImageView(new Image("/lk/ijse/cargoproconnect/img/delete.png"));
                deleteIcon.setFitHeight(20);
                deleteIcon.setFitWidth(20);
                btnDelete.setGraphic(deleteIcon);
                btnDelete.setCursor(Cursor.HAND);
                setDeleteBtnOnAction(btnDelete, batch);

                HBox hBox = new HBox(btnView, btnEdit, btnDelete);
                hBox.setAlignment(Pos.CENTER);
                hBox.setSpacing(15);

                Label label = new Label(batch.getAvailableStatus() == 1 ? "Available" : "Unavailable");
                label.setStyle(label.getText().equalsIgnoreCase("Available") ? "-fx-font-weight: bold;-fx-text-fill: "+ Colors.GREEN +";-fx-font-size: 15;" : "-fx-font-weight: bold;-fx-text-fill: "+ Colors.RED +";-fx-font-size: 15;");

                list.add(new BatchTM(
                        batch.getId(),
                        batch.getShipmentType(),
                        batch.getTotalWeight(),
                        batch.getCurrentWeight(),
                        batch.getSDate(),
                        batch.getDDate(),
                        batch.getDeliveryAddress(),
                        label,
                        hBox,
                        checkBoxList
                ));
            }
            pagination.setPageCount((int) Math.ceil(list.size() / 10.0));
            pagination.setPageFactory(this::createPage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private TableView createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, list.size());
        tableBatch.setItems(FXCollections.observableArrayList(list.subList(fromIndex, toIndex)));

        return tableBatch;
    }

    private void setViewBtnOnAction(JFXButton btnView, BatchDTO batch) {
        btnView.setOnAction(event -> {
            try {
                ViewBatchDetailsFormController.setBatchDetail(batch);
                Stage stage = new Stage();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/popups/ViewBatchDetailsForm.fxml"))));
                stage.setResizable(false);
                stage.setY(225);
                stage.setX(450);
                stage.show();
                stage.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
                    if (!newValue) {
                        stage.close();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setDeleteBtnOnAction(JFXButton btnDelete, BatchDTO batch) {
        if (LocalDate.parse(batch.getSDate()).isBefore(LocalDate.now()) || batch.getCurrentWeight() > 0) {
            btnDelete.setVisible(false);
            checkBoxList.setVisible(false);
        } else {
            btnDelete.setOnAction(event -> {
                ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to delete?", yes, no).showAndWait();

                if (result.orElse(no) == yes) {
                    try {
                        boolean isDeleted = BatchModel.deleteBatch(batch.getId());
                        if (isDeleted) {
                            setTableData();
                            tableBatch.refresh();
                            NotificationUtil.showNotification("Success", "batch " + batch.getId() + " deleted Successfully", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                        } else {
                            NotificationUtil.showNotification("Error", "OOPS! Something happen", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void setEditBtnOnAction(JFXButton btnEdit, BatchDTO batch) {
        if (LocalDate.parse(batch.getSDate()).isBefore(LocalDate.now()) || batch.getCurrentWeight() == batch.getTotalWeight()) {
            btnEdit.setVisible(false);
        } else {
            btnEdit.setOnAction(event -> {
                try {
                    rootChange.getChildren().clear();
                    UpdateBatchFormController.setBatchDetails(batch);
                    rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/update/UpdateBatchForm.fxml")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void setCheckBoxOnAction(JFXCheckBox checkBox) {
        checkBox.setOnAction(event -> {
            if (!checkBox.isSelected()) {
                setSelectedCount();
                int count = 0;
                for (BatchTM batchTM : list) {
                    if (batchTM.getCheckBox().isSelected()) {
                        count += 1;
                    }
                }
                if (count == 0) {
                    btnAddHBox.setVisible(true);
                    btnDeleteHBox.setVisible(false);
                    this.checkBox.setSelected(false);
                }
            } else {
                btnAddHBox.setVisible(false);
                btnDeleteHBox.setVisible(true);
                setSelectedCount();
            }
            int total = 0;
            for (BatchTM batchTM : list) {
                if (batchTM.getCheckBox().isSelected()) {
                    total += 1;
                }
            }
            if (total == list.size()) {
                this.checkBox.setSelected(true);
            }
        });
    }

    private void setCheckBoxOnAction() {
        this.checkBox.setOnAction(event -> {
            btnAddHBox.setVisible(!this.checkBox.isSelected());
            btnDeleteHBox.setVisible(this.checkBox.isSelected());
            for (BatchTM batchTM : list) {
                batchTM.getCheckBox().setSelected(checkBox.isSelected());
            }
            setSelectedCount();
        });
    }

    void setSearchFilter() {
        FilteredList<BatchTM> filteredData = new FilteredList<>(list, b -> true);

        txtSearch.setOnKeyPressed(keyEvent -> {
            txtSearch.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate(batchTM -> {
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                    String searchKeyWord = newValue.toLowerCase();
                    if (batchTM.getId().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (batchTM.getShipmentType().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (batchTM.getSDate().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (batchTM.getDDate().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (batchTM.getAvailableStatus().getText().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }));

            SortedList<BatchTM> sortedList = new SortedList<>(filteredData);
            sortedList.comparatorProperty().bind(tableBatch.comparatorProperty());
            tableBatch.setItems(sortedList);
        });
    }

    private void setSelectedCount() {
        int count = 0;
        for (BatchTM batchTM : list) {
            if (batchTM.getCheckBox().isSelected() && batchTM.getCheckBox().isVisible()) {
                count++;
            }
        }
        lblDelete.setText(count + "  selected");
        lblDelete.setStyle("-fx-font-weight: bold;-fx-text-fill: "+ Colors.RED +";-fx-font-size: 15;");
    }
}
