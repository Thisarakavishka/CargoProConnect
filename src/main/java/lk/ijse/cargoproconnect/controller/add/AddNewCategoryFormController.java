package lk.ijse.cargoproconnect.controller.add;

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
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lk.ijse.cargoproconnect.bo.BOFactory;
import lk.ijse.cargoproconnect.bo.bos.ItemCategoryBO;
import lk.ijse.cargoproconnect.dto.TaxDTO;
import lk.ijse.cargoproconnect.dto.tm.CategoryTaxTM;
import lk.ijse.cargoproconnect.model.*;
import lk.ijse.cargoproconnect.util.Colors;
import lk.ijse.cargoproconnect.util.NotificationUtil;
import lk.ijse.cargoproconnect.util.TextFieldValidator;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddNewCategoryFormController implements Initializable {

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
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnDiscard;

    private ObservableList<CategoryTaxTM> observableList = FXCollections.observableArrayList();

    //Dependency Injection (Property Injection)
    ItemCategoryBO itemCategoryBO = (ItemCategoryBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM_CATEGORY);

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if(txtCategoryName.getFocusColor().equals(Color.web(Colors.GREEN))){
            try {
//                boolean isAdded = CategoryTaxModel.addNewCategory(lblCategoryId.getText(), txtCategoryName.getText(), observableList);
                boolean isAdded = itemCategoryBO.addNewCategory(lblCategoryId.getText(), txtCategoryName.getText(), observableList);
                if (isAdded) {
                    NotificationUtil.showNotification("Success", "Successfully " + txtCategoryName.getText() + " Category added" + LoginModel.getEmployeeUserName(), NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                    rootChange.getChildren().clear();
                    rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/CategoryForm.fxml")));
                }
            } catch (IOException | SQLException e) {
                NotificationUtil.showNotification("Error", "Something happen" + LoginModel.getEmployeeUserName(), NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
                e.printStackTrace();
            }
        }else {
            NotificationUtil.showNotification("Error", "OOPS! Enter correct value for category name" + LoginModel.getEmployeeUserName(), NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
        }
    }

    @FXML
    void btnAddTaxOnAction(ActionEvent event) {
        if (txtCategoryName.getFocusColor().equals(Color.web(Colors.GREEN)) && cmbTaxName.getValue() != null) {
            try {
                List<TaxDTO> taxes = TaxModel.getTaxes();
                for (TaxDTO tax : taxes) {
                    if (tax.getName().equalsIgnoreCase(cmbTaxName.getValue())) {

                        if (!observableList.isEmpty()) {
                            for (int i = 0; i < observableList.size(); i++) {
                                if (tax.getName().equalsIgnoreCase(observableList.get(i).getName())) {
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

                        observableList.add(taxTM);
                        taxTable.setItems(observableList);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            NotificationUtil.showNotification("Error", "OOPS! enter values", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
        }
    }

    private void setDeleteBtnOnAction(JFXButton btnDelete, TaxDTO tax) {
        btnDelete.setOnAction(event -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to delete?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {
                int index = 0;
                for (int i = 0; i < observableList.size(); i++) {
                    if (observableList.get(i).getName().equalsIgnoreCase(tax.getName())) {
                        index = i;
                        break;
                    }
                }
                observableList.remove(index);
                taxTable.setItems(observableList);
                taxTable.refresh();
            }
        });
    }

    @FXML
    void btnDiscardOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/CategoryForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDocumentTypes();
        generateNextCustomerId();
        setCellValueFactory();
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

    void loadDocumentTypes() {
        try {
            ObservableList<String> observableList = FXCollections.observableArrayList();
            List<TaxDTO> taxes = TaxModel.getTaxes();

            for (TaxDTO tax : taxes) {
                observableList.add(tax.getName());
            }

            cmbTaxName.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateNextCustomerId() {
        try {
//            lblCategoryId.setText(CategoryModel.getNextCategoryId());
            lblCategoryId.setText(itemCategoryBO.generateNewCategoryId());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnBackOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/CategoryForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
