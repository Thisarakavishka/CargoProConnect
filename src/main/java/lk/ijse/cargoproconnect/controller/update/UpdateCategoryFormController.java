package lk.ijse.cargoproconnect.controller.update;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.util.Duration;
import lk.ijse.cargoproconnect.dto.Category;
import lk.ijse.cargoproconnect.dto.Tax;
import lk.ijse.cargoproconnect.dto.tm.CategoryTaxTM;
import lk.ijse.cargoproconnect.model.CategoryTaxModel;
import lk.ijse.cargoproconnect.model.TaxModel;
import lk.ijse.cargoproconnect.util.NotificationUtil;
import lk.ijse.cargoproconnect.util.TextFieldValidator;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateCategoryFormController implements Initializable {

    @FXML
    public JFXButton btnBack;

    @FXML
    private AnchorPane rootChange;

    @FXML
    private JFXButton btnAddTax;

    @FXML
    private JFXComboBox<String> cmbTaxName;

    @FXML
    private TableView<CategoryTaxTM> taxTable;

    @FXML
    private TableColumn<?, ?> colTaxId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPercentage;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private JFXTextField txtCategoryName;

    @FXML
    private Label lblCategoryId;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXButton btnDiscard;

    static Category category;
    static ObservableList<CategoryTaxTM> list = FXCollections.observableArrayList();
    static List<Tax> originalTaxes = new ArrayList<>();

    public static void setCategoryDetails(Category category) {
        UpdateCategoryFormController.category = category;
    }

    @FXML
    void btnAddTaxOnAction(ActionEvent event) {
        try {
            List<Tax> taxes = TaxModel.getTaxes();
            for (Tax tax : taxes) {
                if (tax.getName().equalsIgnoreCase(cmbTaxName.getValue())) {
                    //check new add tax is already in the list
                    if (!list.isEmpty()) {
                        for (int i = 0; i < list.size(); i++) {
                            if (tax.getName().equalsIgnoreCase(list.get(i).getName())) {
                                return;
                            }
                        }
                    }

                    JFXButton btnDelete = new JFXButton();
                    ImageView deleteIcon = new ImageView(new Image("/lk/ijse/cargoproconnect/img/delete.png"));
                    deleteIcon.setFitHeight(20);
                    deleteIcon.setFitWidth(20);
                    btnDelete.setGraphic(deleteIcon);
                    btnDelete.setCursor(Cursor.HAND);
                    setDeleteBtnOnAction(btnDelete, tax);

                    HBox hBox = new HBox(btnDelete);
                    hBox.setAlignment(Pos.CENTER);
                    hBox.setSpacing(15);

                    CategoryTaxTM taxTM = new CategoryTaxTM(tax.getId(), tax.getName(), tax.getPercentage(), tax.getDescription(), hBox);

                    list.add(taxTM);
                    taxTable.setItems(list);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnDiscardOnAction(ActionEvent event) {
        try {
            list.clear();
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/CategoryForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        List<Tax> addedTaxes = new ArrayList<>();
        List<Tax> removedTaxes = new ArrayList<>();
        List<Tax> currentTaxes = new ArrayList<>();

        for (CategoryTaxTM categoryTaxTM : taxTable.getItems()) {
            currentTaxes.add(new Tax(
                    categoryTaxTM.getId(),
                    categoryTaxTM.getName(),
                    categoryTaxTM.getPercentage(),
                    categoryTaxTM.getDescription()
            ));
        }

        // find added and removed taxes
        for (Tax updatedTax : currentTaxes) {
            boolean found = false;
            for (Tax originalTax : originalTaxes) {
                if (updatedTax.getId() == originalTax.getId()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                addedTaxes.add(updatedTax);
            }
        }

        for (Tax originalTax : originalTaxes) {
            boolean found = false;
            for (Tax updatedTax : currentTaxes) {
                if (updatedTax.getId() == originalTax.getId()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                removedTaxes.add(originalTax);
            }
        }

        try {
            boolean isUpdated = CategoryTaxModel.updateCategory(category.getId(), category.getName(), addedTaxes, removedTaxes);
            if (isUpdated) {
                NotificationUtil.showNotification("Success", "Successfully " + category.getName() + " Category Updated ", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                rootChange.getChildren().clear();
                rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/CategoryForm.fxml")));

            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            NotificationUtil.showNotification("OOPS!", "Something happen ", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        setData();
        loadDocumentTypes();
        validateTextFields();
    }

    private void validateTextFields() {
        TextFieldValidator.setFocus(txtCategoryName, TextFieldValidator.getNamePattern());
    }

    private void setCellValueFactory() {
        colTaxId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPercentage.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("actions"));
    }

    private void setData() {
        lblCategoryId.setText(category.getId());
        txtCategoryName.setText(category.getName());
        setTableData();
    }

    private void setTableData() {
        try {
            list = FXCollections.observableArrayList();
            List<Tax> taxes = CategoryTaxModel.getIncludedTaxes(category.getId());
            if (taxes == null) {

            } else if (!taxes.isEmpty() || taxes != null) {
                for (Tax tax : taxes) {
                    JFXButton btnDelete = new JFXButton();
                    ImageView deleteIcon = new ImageView(new Image("/lk/ijse/cargoproconnect/img/delete.png"));
                    deleteIcon.setFitHeight(20);
                    deleteIcon.setFitWidth(20);
                    btnDelete.setGraphic(deleteIcon);
                    btnDelete.setCursor(Cursor.HAND);
                    setDeleteBtnOnAction(btnDelete, tax);

                    HBox hBox = new HBox(btnDelete);
                    hBox.setAlignment(Pos.CENTER);
                    hBox.setSpacing(15);

                    list.add(new CategoryTaxTM(
                            tax.getId(),
                            tax.getName(),
                            tax.getPercentage(),
                            tax.getDescription(),
                            hBox
                    ));
                }
                taxTable.setItems(list);

                //copy list for originalTaxes
                originalTaxes = new ArrayList<>();
                for (CategoryTaxTM categoryTaxTM : taxTable.getItems()) {
                    originalTaxes.add(new Tax(
                            categoryTaxTM.getId(),
                            categoryTaxTM.getName(),
                            categoryTaxTM.getPercentage(),
                            categoryTaxTM.getDescription()
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setDeleteBtnOnAction(JFXButton btnDelete, Tax tax) {
        btnDelete.setOnAction(event -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to delete?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {
                int index = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getName().equalsIgnoreCase(tax.getName())) {
                        index = i;
                        break;
                    }
                }
                list.remove(index);
                taxTable.setItems(list);
                taxTable.refresh();
            }
        });
    }

    void loadDocumentTypes() {
        try {
            ObservableList<String> observableList = FXCollections.observableArrayList();
            List<Tax> taxes = TaxModel.getTaxes();

            for (Tax tax : taxes) {
                observableList.add(tax.getName());
            }

            cmbTaxName.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnBackOnAction(ActionEvent event) {
        try {
            list.clear();
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/CategoryForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
