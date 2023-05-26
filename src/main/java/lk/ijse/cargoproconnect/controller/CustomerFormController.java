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
import lk.ijse.cargoproconnect.controller.popup.MailFormController;
import lk.ijse.cargoproconnect.controller.update.UpdateCustomerFormController;
import lk.ijse.cargoproconnect.dto.Customer;
import lk.ijse.cargoproconnect.dto.tm.CustomerTM;
import lk.ijse.cargoproconnect.dto.tm.OrderTM;
import lk.ijse.cargoproconnect.model.CustomerModel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {

    @FXML
    public AnchorPane rootChange;

    @FXML
    public JFXButton btnMailSelected;

    @FXML
    private Pagination pagination;

    @FXML
    private TableView<CustomerTM> tableCustomer;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colFName;

    @FXML
    private TableColumn<?, ?> colLName;

    @FXML
    private TableColumn<?, ?> colCNumber1;

    @FXML
    private TableColumn<?, ?> colCNumber2;

    @FXML
    private TableColumn<?, ?> colDType;

    @FXML
    private TableColumn<?, ?> colDNumber;

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
    private JFXButton btnAddCustomer;

    @FXML
    private HBox btnDeleteHBox;

    @FXML
    private Label lblDelete;

    @FXML
    private JFXButton btnDeleteSelected;

    private JFXCheckBox checkBox;

    private static ObservableList<CustomerTM> list;
    private static int ROWS_PER_PAGE = 10;

    @FXML
    void btnAddCustomerOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/add/AddNewCustomerForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnDeleteSelectedOnAction(ActionEvent event) {
        List<String> ids = new ArrayList<>();
        for (CustomerTM customerTM : list) {
            if (customerTM.getCheckBox().isSelected()) {
                ids.add(customerTM.getId());
            }
        }
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to delete?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            try {
                boolean isDeleted = CustomerModel.deleteSelectedCustomers(ids);
                tableCustomer.refresh();
                if (isDeleted) {
                    setTableData();
                    tableCustomer.refresh();
                    NotificationUtil.showNotification("Success", "Customers deleted Successfully", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));

                } else {
                    NotificationUtil.showNotification("OOPS! ", "Something happen", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        btnAddHBox.setVisible(true);
        btnDeleteHBox.setVisible(false);
        this.checkBox.setSelected(false);
        for (CustomerTM customerTM : list) {
            customerTM.getCheckBox().setSelected(false);
        }
    }

    public void btnMailSelectedOnAction(ActionEvent event) {
        try {
            List<String> ids = new ArrayList<>();
            for (CustomerTM customerTM : list) {
                if (customerTM.getCheckBox().isSelected()) {
                    ids.add(customerTM.getId());
                }
            }
            List<String> emails = CustomerModel.getEmails(ids);
            MailFormController.setMails(emails);

            System.out.println(emails.size());

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

    @FXML
    void btnExportOnAction(ActionEvent event) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Data");

        ObservableList<CustomerTM> data = tableCustomer.getItems();

        for (int i = 0; i < data.size(); i++) {
            CustomerTM rowData = data.get(i);
            Row row = sheet.createRow(i);

            org.apache.poi.ss.usermodel.Cell cell1 = row.createCell(0);
            cell1.setCellValue(rowData.getId());

            org.apache.poi.ss.usermodel.Cell cell2 = row.createCell(1);
            cell2.setCellValue(rowData.getFName());

            org.apache.poi.ss.usermodel.Cell cell3 = row.createCell(2);
            cell3.setCellValue(rowData.getLName());

            org.apache.poi.ss.usermodel.Cell cell4 = row.createCell(3);
            cell4.setCellValue(rowData.getCNumber1());

            org.apache.poi.ss.usermodel.Cell cell5 = row.createCell(4);
            cell5.setCellValue(rowData.getCNumber2());

            Cell cell6 = row.createCell(5);
            cell6.setCellValue(rowData.getDocumentType());

            Cell cell7 = row.createCell(6);
            cell7.setCellValue(rowData.getDocumentNumber());
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hideDeleteSelectedHbox();
        setHeader();
        setCellValueFactory();
        setTableData();
        setSearchFilter();
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
        colFName.setCellValueFactory(new PropertyValueFactory<>("fName"));
        colLName.setCellValueFactory(new PropertyValueFactory<>("lName"));
        colCNumber1.setCellValueFactory(new PropertyValueFactory<>("cNumber1"));
        colCNumber2.setCellValueFactory(new PropertyValueFactory<>("cNumber2"));
        colDType.setCellValueFactory(new PropertyValueFactory<>("documentType"));
        colDNumber.setCellValueFactory(new PropertyValueFactory<>("documentNumber"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("actions"));
        colEdit.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
    }

    private void setTableData() {
        try {
            list = FXCollections.observableArrayList();
            List<Customer> customers = CustomerModel.getCustomers();

            for (Customer customer : customers) {

                JFXCheckBox checkBox = new JFXCheckBox();
                setCheckBoxOnAction(checkBox, customer);

                JFXButton btnMail = new JFXButton();
                ImageView mailIcon = new ImageView(new Image("/lk/ijse/cargoproconnect/img/mail.png"));
                mailIcon.setFitHeight(20);
                mailIcon.setFitWidth(20);
                btnMail.setGraphic(mailIcon);
                btnMail.setCursor(Cursor.HAND);
                setMailBtnOnAction(btnMail, customer);

                JFXButton btnEdit = new JFXButton();
                ImageView editIcon = new ImageView(new Image("/lk/ijse/cargoproconnect/img/edit.png"));
                editIcon.setFitHeight(20);
                editIcon.setFitWidth(20);
                btnEdit.setGraphic(editIcon);
                btnEdit.setCursor(Cursor.HAND);
                setEditBtnOnAction(btnEdit, customer);

                JFXButton btnDelete = new JFXButton();
                ImageView deleteIcon = new ImageView(new Image("/lk/ijse/cargoproconnect/img/delete.png"));
                deleteIcon.setFitHeight(20);
                deleteIcon.setFitWidth(20);
                btnDelete.setGraphic(deleteIcon);
                btnDelete.setCursor(Cursor.HAND);
                setDeleteBtnOnAction(btnDelete, customer);

                HBox hBox = new HBox(btnMail, btnEdit, btnDelete);
                hBox.setAlignment(Pos.CENTER);
                hBox.setSpacing(15);

                list.add(new CustomerTM(
                        customer.getId(),
                        customer.getFName(),
                        customer.getLName(),
                        customer.getContactN1(),
                        customer.getContactN2(),
                        customer.getDocumentType(),
                        customer.getDocumentNumber(),
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

    private TableView createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, list.size());
        tableCustomer.setItems(FXCollections.observableArrayList(list.subList(fromIndex, toIndex)));

        return tableCustomer;
    }

    private void setMailBtnOnAction(JFXButton btnMail, Customer customer) {
        btnMail.setOnAction(event -> {
            try {
                MailFormController.setMail(customer.getEmail());
                Stage stage = new Stage();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/popups/MailForm.fxml"))));
                stage.setResizable(false);
                stage.setAlwaysOnTop(true);
                stage.setY(250);
                stage.setX(1220);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    private void setDeleteBtnOnAction(JFXButton btnDelete, Customer customer) {
        btnDelete.setOnAction(event -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to delete?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {
                try {
                    boolean isDeleted = CustomerModel.deleteCustomer(customer.getId());
                    if (isDeleted) {
                        setTableData();
                        tableCustomer.refresh();
                        NotificationUtil.showNotification("Success", "Customer " + customer.getFName() + " deleted Successfully", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                    } else {
                        NotificationUtil.showNotification("OOPS!", "Something happen", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setEditBtnOnAction(JFXButton btnEdit, Customer customer) {
        btnEdit.setOnAction(event -> {
            try {
                rootChange.getChildren().clear();
                UpdateCustomerFormController.setCustomerDetails(customer);
                rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/update/UpdateCustomerForm.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setCheckBoxOnAction(JFXCheckBox checkBox, Customer customer) {
        checkBox.setOnAction(event -> {
            if (!checkBox.isSelected()) {
                setSelectedCount();
                int count = 0;
                for (CustomerTM customerTM : list) {
                    if (customerTM.getCheckBox().isSelected()) {
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
            for (CustomerTM customerTm : list) {
                if (customerTm.getCheckBox().isSelected()) {
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
            if (txtSearch.getText().isEmpty() || txtSearch.getText().isBlank() || txtSearch.getText() == null) {
                for (CustomerTM customerTM : list) {
                    customerTM.getCheckBox().setSelected(checkBox.isSelected());
                }
            } else if (txtSearch.getText() != null) {
                for (CustomerTM customerTM : tableCustomer.getItems()) {
                    customerTM.getCheckBox().setSelected(checkBox.isSelected());
                }
            }
            setSelectedCount();
        });
    }

    void setSearchFilter() {
        FilteredList<CustomerTM> filteredData = new FilteredList<>(list, b -> true);

        txtSearch.setOnKeyPressed(keyEvent -> {
            txtSearch.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate(customerTM -> {
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                    String searchKeyWord = newValue.toLowerCase();
                    if (customerTM.getId().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (customerTM.getFName().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (customerTM.getLName().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (customerTM.getCNumber1().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (customerTM.getCNumber2().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (customerTM.getDocumentType().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (customerTM.getDocumentNumber().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }));

            SortedList<CustomerTM> sortedList = new SortedList<>(filteredData);
            sortedList.comparatorProperty().bind(tableCustomer.comparatorProperty());
            tableCustomer.setItems(sortedList);
        });
    }

    private void setSelectedCount() {
        int count = 0;
        for (CustomerTM customerTM : list) {
            if (customerTM.getCheckBox().isSelected()) {
                count++;
            }
        }
        lblDelete.setText(count + "  selected");
        lblDelete.setStyle("-fx-font-weight: bold;-fx-text-fill: "+ Colors.RED +";-fx-font-size: 15;");
    }
}
