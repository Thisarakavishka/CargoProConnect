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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import lk.ijse.cargoproconnect.controller.update.UpdateTaxFormController;
import lk.ijse.cargoproconnect.dto.Tax;
import lk.ijse.cargoproconnect.dto.tm.TaxTM;
import lk.ijse.cargoproconnect.model.TaxModel;
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
import java.util.*;

public class TaxFormController implements Initializable {

    @FXML
    public AnchorPane rootChange;

    @FXML
    public HBox btnAddHBox;

    @FXML
    public HBox btnDeleteHBox;

    @FXML
    public Label lblDelete;

    @FXML
    public JFXButton btnDeleteSelected;

    @FXML
    public JFXButton btnExport;

    @FXML
    private TableView<TaxTM> tableTax;

    @FXML
    private TableColumn<?, ?> colTaxId, colName, colPercentage, colDescription, colAction, colEdit;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private Pagination pagination;

    @FXML
    private JFXButton btnAddTax;

    private JFXCheckBox checkBox;

    private static ObservableList<TaxTM> list;
    private static int ROWS_PER_PAGE = 10;

    @FXML
    void btnAddTaxOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/add/AddNewTaxForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnDeleteSelectedOnAction(ActionEvent event) {
        List<String> ids = new ArrayList<>();
        for (TaxTM taxTM : list) {
            if (taxTM.getCheckBox().isSelected()) {
                ids.add(taxTM.getId());
            }
        }
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to delete?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            try {
                boolean isDeleted = TaxModel.deleteSelectedTaxes(ids);
                tableTax.refresh();
                if (isDeleted) {
                    setTableData();
                    tableTax.refresh();
                    NotificationUtil.showNotification("Success", "Taxes  Deleted Successfully", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                } else {
                    NotificationUtil.showNotification("OOPS!", "OOPS! something happened !", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        this.checkBox.setSelected(false);
        btnAddHBox.setVisible(true);
        btnDeleteHBox.setVisible(false);
        for (TaxTM taxTM : list) {
            taxTM.getCheckBox().setSelected(false);
        }
    }

    @FXML
    void btnExportOnAction(ActionEvent event) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Data");

        ObservableList<TaxTM> data = tableTax.getItems();

        for (int i = 0; i < data.size(); i++) {
            TaxTM rowData = data.get(i);
            Row row = sheet.createRow(i);

            Cell cell1 = row.createCell(0);
            cell1.setCellValue(rowData.getId());

            Cell cell2 = row.createCell(1);
            cell2.setCellValue(rowData.getName());

            Cell cell3 = row.createCell(2);
            cell3.setCellValue(rowData.getPercentage());

            Cell cell4 = row.createCell(3);
            cell4.setCellValue(rowData.getDescription());
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
        colEdit.setGraphic(checkBox);   //set checkbox on header of tableview
        setCheckBoxOnAction();
    }

    private void setCellValueFactory() {
        colTaxId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPercentage.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("actions"));
        colEdit.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
    }

    private void setTableData() {
        try {

            list = FXCollections.observableArrayList();
            List<Tax> taxes = TaxModel.getTaxes();
            for (Tax tax : taxes) {

                JFXCheckBox checkBox = new JFXCheckBox();
                setCheckBoxOnAction(checkBox, tax);

                JFXButton btnEdit = new JFXButton();
                ImageView editIcon = new ImageView(new Image("/lk/ijse/cargoproconnect/img/edit.png"));
                editIcon.setFitHeight(20);
                editIcon.setFitWidth(20);
                btnEdit.setGraphic(editIcon);
                btnEdit.setCursor(Cursor.HAND);
                setBtnEditOnAction(btnEdit, tax);

                JFXButton btnDelete = new JFXButton();
                ImageView deleteIcon = new ImageView(new Image("/lk/ijse/cargoproconnect/img/delete.png"));
                deleteIcon.setFitHeight(20);
                deleteIcon.setFitWidth(20);
                btnDelete.setGraphic(deleteIcon);
                btnDelete.setCursor(Cursor.HAND);
                setBtnDeleteOnAction(btnDelete, tax);

                HBox hBox = new HBox(btnEdit, btnDelete);
                hBox.setAlignment(Pos.CENTER);
                hBox.setSpacing(15);

                list.add(new TaxTM(
                        tax.getId(),
                        tax.getName(),
                        tax.getPercentage(),
                        tax.getDescription(),
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
        tableTax.setItems(FXCollections.observableArrayList(list.subList(fromIndex, toIndex)));

        return tableTax;
    }

    private void setBtnDeleteOnAction(JFXButton btnDelete, Tax tax) {
        btnDelete.setOnAction(event -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to delete?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {
                try {
                    boolean isDeleted = TaxModel.deleteTax(tax.getId());
                    tableTax.refresh();
                    if (isDeleted) {
                        setTableData();
                        tableTax.refresh();
                        NotificationUtil.showNotification("Success", "Tax " + tax.getName() + " deleted successfully", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                    } else {
                        new Alert(Alert.AlertType.ERROR, "something happened !").show();
                        NotificationUtil.showNotification("OOPS!", "Something happen", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setBtnEditOnAction(JFXButton btnEdit, Tax tax) {
        btnEdit.setOnAction(event -> {
            try {
                rootChange.getChildren().clear();
                UpdateTaxFormController.setTaxDetails(tax);
                rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/update/UpdateTaxForm.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setCheckBoxOnAction(JFXCheckBox checkBox, Tax tax) {
        checkBox.setOnAction(event -> {
            if (!checkBox.isSelected()) {
                setSelectedCount();
                int count = 0;
                for (TaxTM taxTM : list) {
                    if (taxTM.getCheckBox().isSelected()) {
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
            for (TaxTM taxTM : list) {
                if (taxTM.getCheckBox().isSelected()) {
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
            for (TaxTM taxTM : list) {
                taxTM.getCheckBox().setSelected(checkBox.isSelected());
            }
            setSelectedCount();
        });
    }

    private void setSearchFilter() {
        FilteredList<TaxTM> filteredData = new FilteredList<>(list, b -> true);

        txtSearch.setOnKeyPressed(keyEvent -> {
            txtSearch.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate(taxTM -> {
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                    String searchKeyWord = newValue.toLowerCase();
                    if (taxTM.getId().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (taxTM.getName().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (taxTM.getDescription().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }));

            SortedList<TaxTM> sortedList = new SortedList<>(filteredData);
            sortedList.comparatorProperty().bind(tableTax.comparatorProperty());
            tableTax.setItems(sortedList);
        });
    }

    private void setSelectedCount() {
        int count = 0;
        for (TaxTM taxTM : list) {
            if (taxTM.getCheckBox().isSelected()) {
                count++;
            }
        }
        lblDelete.setText(count + "  selected");
        lblDelete.setStyle("-fx-font-weight: bold;-fx-text-fill: #ff0000;-fx-font-size: 15;");
    }
}
