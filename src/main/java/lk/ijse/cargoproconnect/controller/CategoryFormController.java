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
import lk.ijse.cargoproconnect.bo.BOFactory;
import lk.ijse.cargoproconnect.bo.bos.ItemCategoryBO;
import lk.ijse.cargoproconnect.controller.update.UpdateCategoryFormController;
import lk.ijse.cargoproconnect.dto.CategoryDTO;
import lk.ijse.cargoproconnect.dto.tm.CategoryTM;
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

public class CategoryFormController implements Initializable {

    public TableView<CategoryTM> tableCategory;
    @FXML
    private AnchorPane rootChange;

    @FXML
    private Pagination pagination;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colCategory;

    @FXML
    private TableColumn<?, ?> colAvailableTax;

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
    private JFXButton btnAddCategory;

    @FXML
    private HBox btnDeleteHBox;

    @FXML
    private Label lblDelete;

    @FXML
    private JFXButton btnDeleteSelected;

    private JFXCheckBox checkBox;
    private ObservableList<CategoryTM> list;

    //Dependency Injection (Property Injection)
    ItemCategoryBO itemCategoryBO = (ItemCategoryBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM_CATEGORY);

    @FXML
    void btnAddCategoryOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/add/AddNewCategoryForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnDeleteSelectedOnAction(ActionEvent event) {
        ArrayList<String> ids = new ArrayList<>();
        for (CategoryTM categoryTM : list) {
            if (categoryTM.getCheckBox().isSelected()) {
                ids.add(categoryTM.getId());
            }
        }
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to delete?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            try {
//                boolean isDeleted = CategoryTaxModel.deleteSelectedCategories(ids);
                boolean isDeleted = itemCategoryBO.deleteSelectedCategories(ids);
                tableCategory.refresh();
                if (isDeleted) {
                    setTableData();
                    tableCategory.refresh();
                    new Alert(Alert.AlertType.CONFIRMATION, "Category deleted !").show();
                    NotificationUtil.showNotification("Success", "Categories delete successfully", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
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
        for (CategoryTM categoryTM : list) {
            categoryTM.getCheckBox().setSelected(false);
        }
    }

    @FXML
    void btnExportOnAction(ActionEvent event) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Data");

        ObservableList<CategoryTM> data = tableCategory.getItems();

        for (int i = 0; i < data.size(); i++) {
            CategoryTM rowData = data.get(i);
            Row row = sheet.createRow(i);

            org.apache.poi.ss.usermodel.Cell cell1 = row.createCell(0);
            cell1.setCellValue(rowData.getId());

            org.apache.poi.ss.usermodel.Cell cell2 = row.createCell(1);
            cell2.setCellValue(rowData.getId());

            org.apache.poi.ss.usermodel.Cell cell3 = row.createCell(2);
            cell3.setCellValue(rowData.getName());
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
        colCategory.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAvailableTax.setCellValueFactory(new PropertyValueFactory<>("availableTax"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("actions"));
        colEdit.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
    }

    private void setTableData() {
        try {
            list = FXCollections.observableArrayList();
//            List<CategoryDTO> categories = CategoryModel.getCategories();
            ArrayList<CategoryDTO> categories = itemCategoryBO.getAllCategories();

            for (CategoryDTO category : categories) {

                JFXCheckBox checkBox = new JFXCheckBox();
                setCheckBoxOnAction(checkBox);

                JFXButton btnEdit = new JFXButton();
                ImageView editIcon = new ImageView(new Image("/lk/ijse/cargoproconnect/img/edit.png"));
                editIcon.setFitHeight(20);
                editIcon.setFitWidth(20);
                btnEdit.setGraphic(editIcon);
                btnEdit.setCursor(Cursor.HAND);
                setEditBtnOnAction(btnEdit, category);

                JFXButton btnDelete = new JFXButton();
                ImageView deleteIcon = new ImageView(new Image("/lk/ijse/cargoproconnect/img/delete.png"));
                deleteIcon.setFitHeight(20);
                deleteIcon.setFitWidth(20);
                btnDelete.setGraphic(deleteIcon);
                btnDelete.setCursor(Cursor.HAND);
                setDeleteBtnOnAction(btnDelete, category);

                HBox hBox = new HBox(btnEdit, btnDelete);
                hBox.setAlignment(Pos.CENTER);
                hBox.setSpacing(15);
                int taxes = 0;
                try {
//                    taxes = CategoryTaxModel.taxCount(category.getName());
                    taxes = itemCategoryBO.taxCount(category.getName());
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                list.add(new CategoryTM(
                        category.getId(),
                        category.getName(),
                        taxes,
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
        tableCategory.setItems(FXCollections.observableArrayList(list.subList(fromIndex, toIndex)));

        return tableCategory;
    }

    private void setDeleteBtnOnAction(JFXButton btnDelete, CategoryDTO category) {
        btnDelete.setOnAction(event -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to delete?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {
                try {
//                    boolean isDeleted = CategoryTaxModel.deleteCategory(category.getId());
                    boolean isDeleted = itemCategoryBO.deleteCategory(category.getId());
                    if (isDeleted) {
                        setTableData();
                        tableCategory.refresh();
                        NotificationUtil.showNotification("Success", "Category " + category.getName() + " Delete SuccessFully", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                    } else {
                        NotificationUtil.showNotification("Error", "OOPS! Something happen", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setEditBtnOnAction(JFXButton btnEdit, CategoryDTO category) {
        btnEdit.setOnAction(event -> {
            try {
                rootChange.getChildren().clear();
                UpdateCategoryFormController.setCategoryDetails(category);
                rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/update/UpdateCategoryForm.fxml")));
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
                for (CategoryTM categoryTM : list) {
                    if (categoryTM.getCheckBox().isSelected()) {
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
            for (CategoryTM categoryTM : list) {
                if (categoryTM.getCheckBox().isSelected()) {
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
            for (CategoryTM categoryTM : list) {
                categoryTM.getCheckBox().setSelected(checkBox.isSelected());
            }
            setSelectedCount();
        });
    }

    void setSearchFilter() {
        FilteredList<CategoryTM> filteredData = new FilteredList<>(list, b -> true);
        txtSearch.setOnKeyPressed(keyEvent -> {
            txtSearch.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate(categoryTM -> {
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                    String searchKeyWord = newValue.toLowerCase();
                    if (categoryTM.getId().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (categoryTM.getName().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }));

            SortedList<CategoryTM> sortedList = new SortedList<>(filteredData);
            sortedList.comparatorProperty().bind(tableCategory.comparatorProperty());
            tableCategory.setItems(sortedList);
        });
    }

    private void setSelectedCount() {
        int count = 0;
        for (CategoryTM categoryTM : list) {
            if (categoryTM.getCheckBox().isSelected()) {
                count++;
            }
        }
        lblDelete.setText(count + "  selected");
        lblDelete.setStyle("-fx-font-weight: bold;-fx-text-fill: "+ Colors.RED +";-fx-font-size: 15;");
    }
}
