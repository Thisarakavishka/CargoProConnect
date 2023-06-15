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
import lk.ijse.cargoproconnect.bo.BOFactory;
import lk.ijse.cargoproconnect.bo.bos.EmployeeBO;
import lk.ijse.cargoproconnect.controller.popup.MailFormController;
import lk.ijse.cargoproconnect.controller.update.UpdateEmployeeFormController;
import lk.ijse.cargoproconnect.dto.EmployeeDTO;
import lk.ijse.cargoproconnect.dto.tm.EmployeeTM;
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

public class EmployeeFormController implements Initializable {

    @FXML
    public AnchorPane rootChange;

    @FXML
    public JFXButton btnExport;

    @FXML
    public HBox btnAddHbox;

    @FXML
    public JFXButton btnMailSelected;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private TableView<EmployeeTM> tableEmployee;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colUserName;

    @FXML
    private TableColumn<?, ?> colPassword;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colDType;

    @FXML
    private TableColumn<?, ?> colDNumber;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colEdit;

    @FXML
    private Pagination pagination;

    @FXML
    private JFXButton btnAddEmployee;

    @FXML
    private HBox btnDeleteHBox;

    @FXML
    private Label lblDelete;

    @FXML
    private JFXButton btnDeleteSelected;

    private JFXCheckBox checkBox;
    private static ObservableList<EmployeeTM> list;
    private static int ROWS_PER_PAGE = 10;

    //Dependency Injection (Property Injection)
    EmployeeBO employeeBO = (EmployeeBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.EMPLOYEE);

    @FXML
    void btnAddEmployeeOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/add/AddNewEmployeeForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnExportOnAction(ActionEvent event) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Data");

        ObservableList<EmployeeTM> data = tableEmployee.getItems();

        for (int i = 0; i < data.size(); i++) {
            EmployeeTM rowData = data.get(i);
            Row row = sheet.createRow(i);

            org.apache.poi.ss.usermodel.Cell cell1 = row.createCell(0);
            cell1.setCellValue(rowData.getId());

            org.apache.poi.ss.usermodel.Cell cell2 = row.createCell(1);
            cell2.setCellValue(rowData.getUsername());

            org.apache.poi.ss.usermodel.Cell cell3 = row.createCell(2);
            cell3.setCellValue(rowData.getPassword());

            Cell cell4 = row.createCell(3);
            cell4.setCellValue(rowData.getEmail());

            Cell cell5 = row.createCell(4);
            cell5.setCellValue(rowData.getDType());

            Cell cell6 = row.createCell(5);
            cell6.setCellValue(rowData.getDNumber());
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
            NotificationUtil.showNotification("Error", "Error exporting data to Excel: ", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                System.err.println("Error closing workbook: " + e.getMessage());
            }
        }
    }

    @FXML
    void btnDeleteSelectedOnAction(ActionEvent event) {
        ArrayList<String> ids = new ArrayList<>();
        for (EmployeeTM employeeTM : list) {
            if (employeeTM.getCheckBox().isSelected()) {
                ids.add(employeeTM.getId());
            }
        }
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to delete?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            try {
                boolean isDeleted = employeeBO.deleteSelectedEmployees(ids);
                tableEmployee.refresh();
                if (isDeleted) {
                    setTableData();
                    tableEmployee.refresh();
                    NotificationUtil.showNotification("Success", "Employees deleted successfully", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                } else {
                    NotificationUtil.showNotification("Error", "OOPS! Something happen ", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        this.checkBox.setSelected(false);
        btnAddHbox.setVisible(true);
        btnDeleteHBox.setVisible(false);
        for (EmployeeTM employeeTM : list) {
            employeeTM.getCheckBox().setSelected(false);
        }
    }

    public void btnMailSelectedOnAction(ActionEvent event) {
        try {
            List<String> emails = new ArrayList<>();
            for (EmployeeTM employeeTM : tableEmployee.getItems()) {
                if (employeeTM.getCheckBox().isSelected()) {
                    emails.add(employeeTM.getEmail());
                }
            }
            MailFormController.setMails(emails);

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
        btnDeleteHBox.setLayoutX(btnAddHbox.getLayoutX());
        btnDeleteHBox.setLayoutY(btnAddHbox.getLayoutY());
        btnDeleteHBox.setVisible(false);    //hide delete selected button
    }

    private void setHeader() {
        this.checkBox = new JFXCheckBox();
        colEdit.setGraphic(checkBox);
        setCheckBoxOnAction();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDType.setCellValueFactory(new PropertyValueFactory<>("dType"));
        colDNumber.setCellValueFactory(new PropertyValueFactory<>("dNumber"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("actions"));
        colEdit.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
    }

    private void setTableData() {
        try {
            list = FXCollections.observableArrayList();
            List<EmployeeDTO> employees = employeeBO.getEmployees(1);

            for (EmployeeDTO employee : employees) {

                JFXCheckBox checkBox = new JFXCheckBox();
                setCheckBoxOnAction(checkBox);

                JFXButton btnMail = new JFXButton();
                ImageView mailIcon = new ImageView(new Image("/lk/ijse/cargoproconnect/img/mail.png"));
                mailIcon.setFitHeight(20);
                mailIcon.setFitWidth(20);
                btnMail.setGraphic(mailIcon);
                btnMail.setCursor(Cursor.HAND);
                setMailBtnOnAction(btnMail, employee);

                JFXButton btnEdit = new JFXButton();
                ImageView editIcon = new ImageView(new Image("/lk/ijse/cargoproconnect/img/edit.png"));
                editIcon.setFitHeight(20);
                editIcon.setFitWidth(20);
                btnEdit.setGraphic(editIcon);
                btnEdit.setCursor(Cursor.HAND);
                setEditBtnOnAction(btnEdit, employee);

                JFXButton btnDelete = new JFXButton();
                ImageView deleteIcon = new ImageView(new Image("/lk/ijse/cargoproconnect/img/delete.png"));
                deleteIcon.setFitHeight(20);
                deleteIcon.setFitWidth(20);
                btnDelete.setGraphic(deleteIcon);
                btnDelete.setCursor(Cursor.HAND);
                setDeleteBtnOnAction(btnDelete, employee);

                HBox hBox = new HBox(btnMail, btnEdit, btnDelete);
                hBox.setAlignment(Pos.CENTER);
                hBox.setSpacing(15);

                list.add(new EmployeeTM(
                        employee.getId(),
                        employee.getUsername(),
                        employee.getPassword(),
                        employee.getEmail(),
                        employee.getDocumentType(),
                        employee.getDocumentNumber(),
                        hBox,
                        checkBox
                ));
            }
            pagination.setPageCount((int) Math.ceil(list.size() / 10.0));
            pagination.setPageFactory(this::createPage);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private TableView createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, list.size());
        tableEmployee.setItems(FXCollections.observableArrayList(list.subList(fromIndex, toIndex)));

        return tableEmployee;
    }

    private void setMailBtnOnAction(JFXButton btnMail, EmployeeDTO employee) {
        btnMail.setOnAction(event -> {
            try {
                MailFormController.setMail(employee.getEmail());

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

    private void setDeleteBtnOnAction(JFXButton btnDelete, EmployeeDTO employee) {
        btnDelete.setOnAction(event -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to delete?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {
                try {
                    boolean isDeleted = employeeBO.deleteEmployee(employee.getId());
                    if (isDeleted) {
                        setTableData();
                        tableEmployee.refresh();
                        NotificationUtil.showNotification("Success", "Employee " + employee.getUsername() + " deleted successfully", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                    } else {
                        NotificationUtil.showNotification("Error", "OOPS! Something happen ", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setEditBtnOnAction(JFXButton btnEdit, EmployeeDTO employee) {
        btnEdit.setOnAction(event -> {
            try {
                rootChange.getChildren().clear();
                UpdateEmployeeFormController.setEmployeeDetails(employee);
                rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/update/UpdateEmployeeForm.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setCheckBoxOnAction(JFXCheckBox checkBox) {
        checkBox.setOnAction(event -> {
            if (!checkBox.isSelected()) {
                setSelectedCount();
                int count = 0;
                for (EmployeeTM employeeTM : list) {
                    if (employeeTM.getCheckBox().isSelected()) {
                        count += 1;
                    }
                }
                if (count == 0) {
                    btnAddHbox.setVisible(true);
                    btnDeleteHBox.setVisible(false);
                    this.checkBox.setSelected(false);
                }
            } else {
                btnAddHbox.setVisible(false);
                btnDeleteHBox.setVisible(true);
                setSelectedCount();
            }
            int total = 0;
            for (EmployeeTM employeeTM : list) {
                if (employeeTM.getCheckBox().isSelected()) {
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
            btnAddHbox.setVisible(!this.checkBox.isSelected());
            btnDeleteHBox.setVisible(this.checkBox.isSelected());
            for (EmployeeTM employeeTM : tableEmployee.getItems()) {
                employeeTM.getCheckBox().setSelected(checkBox.isSelected());
            }
            setSelectedCount();
        });
    }

    void setSearchFilter() {
        FilteredList<EmployeeTM> filteredData = new FilteredList<>(list, b -> true);

        txtSearch.setOnKeyPressed(keyEvent -> {
            txtSearch.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate(employeeTM -> {
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                    String searchKeyWord = newValue.toLowerCase();
                    if (employeeTM.getId().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (employeeTM.getEmail().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (employeeTM.getUsername().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (employeeTM.getDType().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (employeeTM.getDNumber().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }));

            SortedList<EmployeeTM> sortedList = new SortedList<>(filteredData);
            sortedList.comparatorProperty().bind(tableEmployee.comparatorProperty());
            tableEmployee.setItems(sortedList);
        });
    }

    private void setSelectedCount() {
        int count = 0;
        for (EmployeeTM employeeTM : list) {
            if (employeeTM.getCheckBox().isSelected()) {
                count++;
            }
        }
        lblDelete.setText(count + "  selected");
        lblDelete.setStyle("-fx-font-weight: bold;-fx-text-fill: "+ Colors.RED +";-fx-font-size: 15;");
    }

}
