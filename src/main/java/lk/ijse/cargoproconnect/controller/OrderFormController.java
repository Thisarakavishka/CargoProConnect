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
import javafx.scene.Node;
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
import lk.ijse.cargoproconnect.controller.popup.MailFormController;
import lk.ijse.cargoproconnect.controller.popup.ViewOrderFormController;
import lk.ijse.cargoproconnect.dto.Order;
import lk.ijse.cargoproconnect.dto.tm.OrderTM;
import lk.ijse.cargoproconnect.model.CustomerModel;
import lk.ijse.cargoproconnect.model.OrderDeliveryModel;
import lk.ijse.cargoproconnect.model.OrderModel;
import lk.ijse.cargoproconnect.util.Colors;
import lk.ijse.cargoproconnect.util.NotificationUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static lk.ijse.cargoproconnect.controller.BatchFormController.ROWS_PER_PAGE;

public class OrderFormController implements Initializable {

    @FXML
    public AnchorPane rootChange;

    @FXML
    public JFXButton btnMailSelected;

    @FXML
    private Pagination pagination;

    @FXML
    private TableView<OrderTM> tableOrder;

    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private TableColumn<?, ?> colCustomerId;

    @FXML
    private TableColumn<?, ?> colBatchId;

    @FXML
    private TableColumn<?, ?> colOrderDate;

    @FXML
    private TableColumn<?, ?> colIsChecked;

    @FXML
    private TableColumn<?, ?> colCheckBy;

    @FXML
    private TableColumn<?, ?> colCheckTime;

    @FXML
    private TableColumn<?, ?> colIsDeliver;

    @FXML
    private TableColumn<?, ?> colDeliverDate;

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
    private JFXButton btnAddOrder;

    @FXML
    private HBox btnDeleteHBox;

    @FXML
    private Label lblDelete;

    @FXML
    private JFXButton btnDeleteSelected;

    private JFXCheckBox checkBox;
    private ObservableList<OrderTM> list;

    @FXML
    void btnAddOrderOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/add/AddNewOrderForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnExportOnAction(ActionEvent event) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Data");

        ObservableList<OrderTM> data = tableOrder.getItems();

        for (int i = 0; i < data.size(); i++) {
            OrderTM rowData = data.get(i);
            Row row = sheet.createRow(i);

            org.apache.poi.ss.usermodel.Cell cell1 = row.createCell(0);
            cell1.setCellValue(rowData.getId());

            org.apache.poi.ss.usermodel.Cell cell2 = row.createCell(1);
            cell2.setCellValue(rowData.getOrderDate());

            org.apache.poi.ss.usermodel.Cell cell3 = row.createCell(2);
            cell3.setCellValue(rowData.getIsChecked().getText());

            org.apache.poi.ss.usermodel.Cell cell4 = row.createCell(3);
            cell4.setCellValue(rowData.getCheckBy());

            org.apache.poi.ss.usermodel.Cell cell5 = row.createCell(4);
            cell5.setCellValue(rowData.getCheckTime());

            org.apache.poi.ss.usermodel.Cell cell6 = row.createCell(5);
            cell6.setCellValue(rowData.getIsDeliver().getText());

            org.apache.poi.ss.usermodel.Cell cell7 = row.createCell(6);
            cell7.setCellValue(rowData.getDeliverDate());
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
                System.err.println("Error closing workbook: " + e.getMessage());
            }
        }
    }

    @FXML
    void btnMailSelectedOnAction(ActionEvent event) {
        try {
            List<String> ids = new ArrayList<>();
            for (OrderTM orderTM : tableOrder.getItems()) {
                if (orderTM.getCheckBox().isSelected()) {
                    ids.add(orderTM.getCustomerId());
                }
            }
            List<String> emails = CustomerModel.getEmails(ids);
            MailFormController.setMails(emails);

            Stage stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/popups/MailForm.fxml"))));
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.setY(250);
            stage.setX(1220);
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hideDeleteSelectedHbox();
        setHeader();
        setCellValueFactory();
        setTableData();
        setSearchFilter();
        setActionForDoubleClick();
    }

    private void setActionForDoubleClick() {
        tableOrder.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {

                ViewOrderFormController.setOrderDetails(list.get(tableOrder.getSelectionModel().getSelectedIndex()));
                Stage stage = new Stage();
                try {
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/popups/ViewOrderForm.fxml"))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage.setX(510);
                stage.setY(250);
                stage.setResizable(false);
                stage.show();
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
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colBatchId.setCellValueFactory(new PropertyValueFactory<>("batchId"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colIsChecked.setCellValueFactory(new PropertyValueFactory<>("isChecked"));
        colCheckBy.setCellValueFactory(new PropertyValueFactory<>("checkBy"));
        colCheckTime.setCellValueFactory(new PropertyValueFactory<>("checkTime"));
        colIsDeliver.setCellValueFactory(new PropertyValueFactory<>("isDeliver"));
        colDeliverDate.setCellValueFactory(new PropertyValueFactory<>("DeliverDate"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("actions"));
        colEdit.setCellValueFactory(new PropertyValueFactory<>("checkBox"));


    }

    private void setTableData() {
        try {
            list = FXCollections.observableArrayList();
            List<Order> orders = OrderModel.getOrders();

            for (Order order : orders) {
                JFXCheckBox checkBox = new JFXCheckBox();
                setCheckBoxOnAction(checkBox);

                JFXButton btnMail = new JFXButton();
                ImageView mailIcon = new ImageView(new Image("/lk/ijse/cargoproconnect/img/mail.png"));
                mailIcon.setFitHeight(20);
                mailIcon.setFitWidth(20);
                btnMail.setGraphic(mailIcon);
                btnMail.setCursor(Cursor.HAND);
                setMailBtnOnAction(btnMail, order);

                JFXButton btnView = new JFXButton();
                ImageView viewIcon = new ImageView(new Image("/lk/ijse/cargoproconnect/img/view.png"));
                viewIcon.setFitHeight(20);
                viewIcon.setFitWidth(20);
                btnView.setGraphic(viewIcon);
                btnView.setCursor(Cursor.HAND);
                setViewBtnOnAction(btnView, order);

                HBox hBox = new HBox(btnMail, btnView);
                hBox.setAlignment(Pos.CENTER);
                hBox.setSpacing(10);

                Label labelIsChecked = new Label(order.getIsChecked() == 1 ? "CHECKED" : "UNCHECKED");
                labelIsChecked.setStyle(labelIsChecked.getText().equalsIgnoreCase("CHECKED") ? "-fx-font-weight: bold;-fx-text-fill: "+Colors.GREEN+";-fx-font-size: 15;" : "-fx-font-weight: bold;-fx-text-fill: "+ Colors.RED +";-fx-font-size: 15;");

                Label labelIsDeliver = new Label(order.getIsDeliver() == 1 ? "DELIVER" : "NOT YET");
                labelIsDeliver.setStyle(labelIsDeliver.getText().equalsIgnoreCase("DELIVER") ? "-fx-font-weight: bold;-fx-text-fill: "+Colors.GREEN+";-fx-font-size: 15;" : "-fx-font-weight: bold;-fx-text-fill:"+ Colors.RED +";-fx-font-size: 15;");

                list.add(new OrderTM(
                        order.getId(),
                        order.getCustomerId(),
                        order.getBatchId(),
                        order.getOrderDate(),
                        labelIsChecked,
                        order.getCheckBy(),
                        order.getCheckTime(),
                        labelIsDeliver,
                        order.getDeliverDate(),
                        hBox,
                        checkBox
                ));
            }
            pagination.setPageCount((int) Math.ceil(list.size() / 10.0));
            pagination.setPageFactory(this::createPage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, list.size());
        tableOrder.setItems(FXCollections.observableArrayList(list.subList(fromIndex, toIndex)));

        return tableOrder;
    }

    private void setMailBtnOnAction(JFXButton btnMail, Order order) {
        btnMail.setOnAction(event -> {
            try {
                String email = CustomerModel.getEmail(order.getCustomerId());
                MailFormController.setMail(email);

                Stage stage = new Stage();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/popups/MailForm.fxml"))));
                stage.setResizable(false);
                stage.setAlwaysOnTop(true);
                stage.setY(250);
                stage.setX(1220);
                stage.show();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setViewBtnOnAction(JFXButton btnView, Order order) {
        btnView.setOnAction(event -> {
            Stage stage = new Stage();
            try {
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/popups/ViewOrderForm.fxml"))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setX(550);
            stage.setY(250);
            stage.setResizable(false);
            stage.show();
        });
    }

    private void setCheckBoxOnAction(JFXCheckBox checkBox) {
        checkBox.setOnAction(event -> {
            if (!checkBox.isSelected()) {
                setSelectedCount();
                int count = 0;
                for (OrderTM orderTM : list) {
                    if (orderTM.getCheckBox().isSelected()) {
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
            for (OrderTM orderTM : list) {
                if (orderTM.getCheckBox().isSelected()) {
                    total += 1;
                }
            }
            if (total == list.size()) {
                this.checkBox.setSelected(true);
            }
        });
    }

    private void setCheckBoxOnAction() {
        checkBox.setOnAction(event -> {
            btnAddHBox.setVisible(!checkBox.isSelected());
            btnDeleteHBox.setVisible(checkBox.isSelected());
            for (OrderTM orderTM : tableOrder.getItems()) {
                orderTM.getCheckBox().setSelected(checkBox.isSelected());
            }
            setSelectedCount();
        });
    }

    private void setSelectedCount() {
        int count = 0;
        for (OrderTM orderTM : list) {
            if (orderTM.getCheckBox().isSelected()) {
                count++;
            }
        }
        lblDelete.setText(count + "  selected");
        lblDelete.setStyle("-fx-font-weight: bold;-fx-text-fill: "+Colors.BLUE+";-fx-font-size: 15;");
    }

    private void setSearchFilter() {
        FilteredList<OrderTM> filteredData = new FilteredList<>(list, b -> true);

        txtSearch.setOnKeyPressed(keyEvent -> {
            txtSearch.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate(orderTM -> {
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                    String searchKeyWord = newValue.toLowerCase();
                    if (orderTM.getId().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (orderTM.getOrderDate().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (orderTM.getBatchId().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (orderTM.getCustomerId().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (orderTM.getDeliverDate().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (orderTM.getIsChecked().getText().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (orderTM.getIsDeliver().getText().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (orderTM.getCheckBy() != null) {
                        if (orderTM.getCheckBy().toLowerCase().indexOf(searchKeyWord) > -1) {
                            return true;
                        }
                        return false;
                    } else if (orderTM.getCheckTime() != null) {
                        if (orderTM.getCheckTime().toLowerCase().indexOf(searchKeyWord) > -1) {
                            return true;
                        }
                        return false;
                    } else {
                        return false;
                    }
                });
            }));

            SortedList<OrderTM> sortedList = new SortedList<>(filteredData);
            sortedList.comparatorProperty().bind(tableOrder.comparatorProperty());
            tableOrder.setItems(sortedList);
        });
    }
}
