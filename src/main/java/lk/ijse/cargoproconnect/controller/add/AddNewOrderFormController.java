package lk.ijse.cargoproconnect.controller.add;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.cargoproconnect.bo.BOFactory;
import lk.ijse.cargoproconnect.bo.bos.*;
import lk.ijse.cargoproconnect.controller.popup.PaymentFormController;
import lk.ijse.cargoproconnect.controller.popup.ViewBatchFormController;
import lk.ijse.cargoproconnect.dto.BatchDTO;
import lk.ijse.cargoproconnect.dto.CategoryDTO;
import lk.ijse.cargoproconnect.dto.CustomerDTO;
import lk.ijse.cargoproconnect.dto.DeliveryDTO;
import lk.ijse.cargoproconnect.dto.tm.OrderItemTM;
import lk.ijse.cargoproconnect.model.*;
import lk.ijse.cargoproconnect.util.Colors;
import lk.ijse.cargoproconnect.util.NotificationUtil;
import lk.ijse.cargoproconnect.util.TextFieldValidator;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class AddNewOrderFormController implements Initializable {

    @FXML
    public Label lblDeliverId;

    @FXML
    public JFXTextField txtCustomerContact1;

    @FXML
    public JFXTextField txtCustomerContact2;

    @FXML
    public ScrollPane scrollable;

    @FXML
    public DatePicker datePicker;

    @FXML
    public DatePicker datePicker2;

    @FXML
    public Label lblEmail;

    @FXML
    public JFXButton btnBack;

    @FXML
    private AnchorPane rootChange;

    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblBatchId;

    @FXML
    private JFXButton btnAddBatch;

    @FXML
    private JFXTextField txtSearchCustomer;

    @FXML
    private Label lblCustomerId;

    @FXML
    private Label lblCustomerFName;

    @FXML
    private Label lblCustomerLName;

    @FXML
    private JFXTextField txtItemName;

    @FXML
    private JFXTextField txtNote;

    @FXML
    private JFXTextField txtSearchCategory;

    @FXML
    private JFXTextField txtWeight;

    @FXML
    private JFXButton btnAddToCart;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private Label lblCategoryId;

    @FXML
    private JFXTextField txtPrice;

    @FXML
    private TableView<OrderItemTM> tableOrder;

    @FXML
    private TableColumn<?, ?> colItem;

    @FXML
    private TableColumn<?, ?> colCategory;

    @FXML
    private TableColumn<?, ?> colNote;

    @FXML
    private TableColumn<?, ?> colTaxes;

    @FXML
    private TableColumn<?, ?> colUnitWeight;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colTaxValue;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private JFXButton btnPlaceOrder;

    @FXML
    private JFXButton btnDiscard;

    @FXML
    private JFXButton btnAddNewCustomer;

    @FXML
    private JFXTextField txtSearchAddress;

    @FXML
    private JFXButton btnNewDeliverAddress;

    @FXML
    private JFXTextField txtCustomerAddress;

    @FXML
    private Label lblTotalAmount;

    @FXML
    private Label lblTotalWeight;

    private static final DecimalFormat decimalFormatter = new DecimalFormat("0.00");
    private ObservableList<OrderItemTM> observableList = FXCollections.observableArrayList();
    private int totalWeight;
    private double netTax;

    //Dependency Injection (Property Injection)
    BatchBO batchBO = (BatchBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.BATCH);
    ItemCategoryBO itemCategoryBO = (ItemCategoryBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM_CATEGORY);
    CustomerBO customerBO = (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);
    DeliverDetailBO detailBO = (DeliverDetailBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.DELIVER_DETAIL);
    TaxBO taxBO = (TaxBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.TAX);
    OrderBO orderBO = (OrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDER);

    @FXML
    void btnAddBatchOnAction(ActionEvent event) { //check batch
        if (datePicker.getValue() != null && datePicker2.getValue() != null) {
            if (datePicker.getValue().isBefore(datePicker2.getValue()) && datePicker.getValue().isEqual(LocalDate.now()) || datePicker.getValue().isAfter(LocalDate.now())) {
                try {
                    batchBO.setBatchStatus();
                    ViewBatchFormController.setSelectedDates(datePicker.getValue(), datePicker2.getValue());
                    Stage stage = new Stage();
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/popups/ViewBatchForm.fxml"))));
                    stage.setX(550);
                    stage.setY(250);
                    stage.setResizable(false);
                    stage.show();
                    stage.setOnHiding(evt -> {
                        lblBatchId.setText(ViewBatchFormController.getBatchId());
                    });
                    stage.focusedProperty().addListener((observableValue, aBoolean, newValue) -> {
                        if (!newValue) {
                            stage.close();
                        }
                    });

                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            } else {
                NotificationUtil.showNotification("Error", "OOPS! Enter correct dates!", NotificationUtil.NotificationType.INFORMATION, Duration.seconds(5));
            }
        } else {
            NotificationUtil.showNotification("Error", "OOPS! there is no value in date picker", NotificationUtil.NotificationType.INFORMATION, Duration.seconds(5));
        }
    }

    @FXML
    void btnAddNewCustomerOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/popups/NewCustomerForm.fxml"))));
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.setY(150);
            stage.setX(600);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        if (txtItemName.getFocusColor().equals(Color.web(Colors.GREEN)) && txtWeight.getFocusColor().equals(Color.web(Colors.GREEN)) && txtQty.getFocusColor().equals(Color.web(Colors.GREEN)) && txtPrice.getFocusColor().equals(Color.web(Colors.GREEN))) {

            JFXButton btnDelete = new JFXButton();
            ImageView deleteIcon = new ImageView(new Image("/lk/ijse/cargoproconnect/img/delete.png"));
            deleteIcon.setFitHeight(20);
            deleteIcon.setFitWidth(20);
            btnDelete.setGraphic(deleteIcon);
            btnDelete.setCursor(Cursor.HAND);

            int taxes = 0;
            String total = (Integer.parseInt(txtWeight.getText()) * Integer.parseInt(txtQty.getText()) + " Kg");

            double totalTax = 0;

            try {
//                taxes = CategoryTaxModel.taxCount(txtSearchCategory.getText());
                taxes = itemCategoryBO.taxCount(txtSearchCategory.getText());
//                totalTax = OrderItemTaxModel.getTotalTax(lblCategoryId.getText(), txtPrice.getText(), txtQty.getText());
                totalTax = taxBO.getTotalTax(lblCategoryId.getText(), txtPrice.getText(), txtQty.getText());
                if (!txtPrice.getText().isEmpty() || txtPrice != null) {
//                    totalTax = OrderItemTaxModel.getTotalTax(lblCategoryId.getText(), txtPrice.getText(), txtQty.getText());
                    totalTax = taxBO.getTotalTax(lblCategoryId.getText(), txtPrice.getText(), txtQty.getText());
                } else {
//                    totalTax = OrderItemTaxModel.getTotalTax(lblCategoryId.getText(), "0", txtQty.getText());
                    totalTax = taxBO.getTotalTax(lblCategoryId.getText(), "0", txtQty.getText());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //check duplicates
            if (!observableList.isEmpty()) {
                for (int i = 0; i < observableList.size(); i++) {
                    if (observableList.get(i).getCategory().equalsIgnoreCase(txtSearchCategory.getText()) &&
                            observableList.get(i).getItem().equalsIgnoreCase(txtItemName.getText())) {
                        return;
                    }
                }
            }

            OrderItemTM orderItemTM = new OrderItemTM(
                    txtItemName.getText(),
                    txtSearchCategory.getText(),
                    txtNote.getText(),
                    taxes,
                    Double.parseDouble(txtWeight.getText()),
                    Integer.parseInt(txtQty.getText()),
                    total,
                    Double.parseDouble(decimalFormatter.format(totalTax)),
                    btnDelete
            );
            setDeleteBtnOnAction(btnDelete, orderItemTM.getItem());
            observableList.add(orderItemTM);
            tableOrder.setItems(observableList);

            totalWeight += Integer.parseInt(orderItemTM.getTotal().replaceAll("\\D+", ""));
            lblTotalWeight.setText(totalWeight + " Kg");

            netTax += totalTax;
            lblTotalAmount.setText(decimalFormatter.format(netTax) + " $");

            txtQty.setText(null);
            txtItemName.setText(null);
            txtWeight.setText(null);
            txtSearchCategory.setText(null);
            txtNote.setText(null);
            txtPrice.setText(null);
            txtItemName.setFocusColor(Paint.valueOf(Colors.BLUE));
            txtNote.setFocusColor(Paint.valueOf(Colors.BLUE));
            txtWeight.setFocusColor(Paint.valueOf(Colors.BLUE));
            txtQty.setFocusColor(Paint.valueOf(Colors.BLUE));
            txtPrice.setFocusColor(Paint.valueOf(Colors.BLUE));
        } else {
            NotificationUtil.showNotification("Error", "OOPS! Enter correct values", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
        }
    }

    private void setDeleteBtnOnAction(JFXButton btnDelete, String item) {
        btnDelete.setOnAction(event -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to delete?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {
                int index = 0;
                for (int i = 0; i < observableList.size(); i++) {
                    if (observableList.get(i).getItem().equalsIgnoreCase(item)) {
                        index = i;
                        break;
                    }
                }
                totalWeight -= Integer.parseInt(observableList.get(index).getTotal().replaceAll("\\D+", ""));   //remove delete item weight from total weight
                lblTotalWeight.setText(totalWeight + " Kg");

                double itemTotalTax = observableList.get(index).getTotalTax();
                //.replaceAll("\\s*\\$\\s*", ""));//remove delete item tax from total tax
                netTax -= itemTotalTax;
                lblTotalAmount.setText(decimalFormatter.format(netTax) + " $");

                observableList.remove(index);
                tableOrder.setItems(observableList);
                tableOrder.refresh();
            }
        });
    }

    @FXML
    void btnDiscardOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/OrderForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
        if (!tableOrder.getItems().isEmpty() && lblCustomerId != null && txtCustomerAddress.getFocusColor().equals(Color.web(Colors.GREEN)) && txtCustomerContact1.getFocusColor().equals(Color.web(Colors.GREEN)) && txtCustomerContact2.getFocusColor().equals(Color.web(Colors.GREEN))) {
            try {
                DeliveryDTO delivery = new DeliveryDTO(lblDeliverId.getText(),txtCustomerAddress.getText(),txtCustomerContact1.getText(),txtCustomerContact2.getText());

                int weight = Integer.parseInt(lblTotalWeight.getText().replaceAll("\\D+", ""));
                BatchDTO batch = batchBO.searchBatch(lblBatchId.getText());
                int currentWeight = batch.getCurrentWeight();
                int totalWeight = batch.getTotalWeight();

                if (weight + currentWeight == totalWeight || weight + currentWeight < totalWeight) {
                    try {
                        PaymentFormController.setParentRoot(rootChange);
                        PaymentFormController.setPlaceOrderDetails(weight, delivery, observableList, lblOrderId.getText(), lblCustomerId.getText(), lblBatchId.getText(), new SimpleDateFormat("yyyy-MM-dd ").format(Calendar.getInstance().getTime()), netTax);
                        Stage stage = new Stage();
                        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/popups/PaymentForm.fxml"))));
                        stage.setResizable(false);
                        stage.setAlwaysOnTop(true);
                        stage.setY(150);
                        stage.setX(600);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    NotificationUtil.showNotification("OOPS!", lblBatchId.getText() + "Batch is completed try another batch", NotificationUtil.NotificationType.NOTIFICATION, Duration.seconds(5));
                }
            } catch (SQLException | ClassNotFoundException e) {
                NotificationUtil.showNotification("Error", "OOPS! Something happen", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
            }
        } else {
            NotificationUtil.showNotification("Error", "OOPS! Input correct values", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateNextOrderId();
        generateNextDeliverId();
        setSearchCustomer();
        setSearchCategory();
        setSearchAddress();
        setCellValueFactory();
        scrollable.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        validateTextFields();
    }

    private void validateTextFields() {
        TextFieldValidator.setFocus(txtItemName, TextFieldValidator.getItemPattern());
        TextFieldValidator.setFocus(txtWeight, TextFieldValidator.getWeightPattern());
        TextFieldValidator.setFocus(txtQty, TextFieldValidator.getIntPattern());
        TextFieldValidator.setFocus(txtPrice, TextFieldValidator.getWeightPattern());
        TextFieldValidator.setFocus(txtCustomerAddress, TextFieldValidator.getAddressPattern());
        TextFieldValidator.setFocus(txtCustomerContact1, TextFieldValidator.getContactNumberPattern());
        TextFieldValidator.setFocus(txtCustomerContact2, TextFieldValidator.getContactNumberPattern());
    }

    private void setCellValueFactory() {
        colItem.setCellValueFactory(new PropertyValueFactory<>("item"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        colTaxes.setCellValueFactory(new PropertyValueFactory<>("taxes"));
        colUnitWeight.setCellValueFactory(new PropertyValueFactory<>("unitWeight"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colTaxValue.setCellValueFactory(new PropertyValueFactory<>("totalTax"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("button"));
    }

    private void setSearchCategory() {
        List<String> ids = new ArrayList<>();
        List<String> name = new ArrayList<>();
        try {
//            List<CategoryDTO> categories = CategoryModel.getCategories();
            List<CategoryDTO> categories =itemCategoryBO.getAllCategories();
            for (CategoryDTO category : categories) {
                ids.add(category.getId());
                name.add(category.getName());
            }
            txtSearchCategory.setOnKeyReleased(keyEvent -> {
                TextFields.bindAutoCompletion(txtSearchCategory, ids);
                TextFields.bindAutoCompletion(txtSearchCategory, name);

                //find customer from id,fName or lName
                for (CategoryDTO category : categories) {
                    if (category.getId().equalsIgnoreCase(txtSearchCategory.getText()) ||
                            category.getName().equalsIgnoreCase(txtSearchCategory.getText())) {
                        txtSearchCategory.setText(category.getName());
                        lblCategoryId.setText(category.getId());
                    } else if (txtSearchCategory.getText() == null || txtSearchCategory.getText().isEmpty() || txtSearchCategory.getText().isBlank()) {
                        txtSearchCategory.setText(null);
                        lblCategoryId.setText(null);
                    }
                }
            });
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        txtSearchCategory.setOnKeyTyped(keyEvent -> {
            int taxes = 0;
            try {
//                taxes = CategoryTaxModel.taxCount(txtSearchCategory.getText());
                taxes = itemCategoryBO.taxCount(txtSearchCategory.getText());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (taxes == 0) {
                txtPrice.setText("0");
                txtPrice.setFocusColor(Paint.valueOf(Colors.GREEN));
            } else {
                txtPrice.setText(null);
                txtPrice.setFocusColor(Paint.valueOf(Colors.BLUE));
            }
        });
    }

    private void setSearchCustomer() {
        List<String> ids = new ArrayList<>();
        List<String> fName = new ArrayList<>();
        List<String> lName = new ArrayList<>();
        try {
//            List<CustomerDTO> customers = CustomerModel.getCustomers();
            List<CustomerDTO> customers =customerBO.getAllCustomers();
            for (CustomerDTO customer : customers) {
                ids.add(customer.getId());
                fName.add(customer.getFName());
                lName.add(customer.getLName());
            }
            txtSearchCustomer.setOnKeyReleased(keyEvent -> {
                TextFields.bindAutoCompletion(txtSearchCustomer, ids);
                TextFields.bindAutoCompletion(txtSearchCustomer, fName);
                TextFields.bindAutoCompletion(txtSearchCustomer, lName);

                //find customer from id,fName or lName
                for (CustomerDTO customer : customers) {
                    if (customer.getId().equalsIgnoreCase(txtSearchCustomer.getText()) ||
                            customer.getFName().equalsIgnoreCase(txtSearchCustomer.getText()) ||
                            customer.getLName().equalsIgnoreCase(txtSearchCustomer.getText())) {
                        lblCustomerId.setText(customer.getId());
                        lblCustomerFName.setText(customer.getFName());
                        lblCustomerLName.setText(customer.getLName());
                        lblEmail.setText(customer.getEmail());
                        PaymentFormController.setCustomerEmail(customer.getEmail());
                    } else if (txtSearchCustomer.getText() == null || txtSearchCustomer.getText().isEmpty() || txtSearchCustomer.getText().isBlank()) {
                        lblCustomerId.setText(null);
                        lblCustomerFName.setText(null);
                        lblCustomerLName.setText(null);
                        lblEmail.setText(null);
                    }
                }
            });
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setSearchAddress() {
        List<String> address = new ArrayList<>();
        List<String> contact1 = new ArrayList<>();
        List<String> contact2 = new ArrayList<>();
        try {
//            List<DeliveryDTO> deliveries = DeliveryModel.getDeliveries();
            List<DeliveryDTO> deliveries = detailBO.getAllDeliverDetails();
            for (DeliveryDTO delivery : deliveries) {
                address.add(delivery.getAddress());
                contact2.add(delivery.getContact2());
            }
            txtSearchAddress.setOnKeyReleased(keyEvent -> {
                TextFields.bindAutoCompletion(txtSearchAddress, address);
                TextFields.bindAutoCompletion(txtSearchAddress, contact1);
                TextFields.bindAutoCompletion(txtSearchAddress, contact2);

                //find customer delivery details from address,contact1 or contact2
                for (DeliveryDTO delivery : deliveries) {
                    if (delivery.getAddress().equalsIgnoreCase(txtSearchAddress.getText()) ||
                            delivery.getContact1().equalsIgnoreCase(txtSearchAddress.getText()) ||
                            delivery.getContact2().equalsIgnoreCase(txtSearchAddress.getText())) {
                        txtCustomerAddress.setText(delivery.getAddress());
                        txtCustomerContact1.setText(delivery.getContact1());
                        txtCustomerContact2.setText(delivery.getContact2());
                        TextFieldValidator.setValid(txtCustomerAddress, TextFieldValidator.getAddressPattern());
                        TextFieldValidator.setValid(txtCustomerContact1, TextFieldValidator.getContactNumberPattern());
                        TextFieldValidator.setValid(txtCustomerContact2, TextFieldValidator.getContactNumberPattern());
                    } else if (txtSearchAddress.getText() == null || txtSearchAddress.getText().isEmpty() || txtSearchAddress.getText().isBlank()) {
                        txtCustomerAddress.setText(null);
                        txtCustomerContact1.setText(null);
                        txtCustomerContact2.setText(null);
                    }
                }
            });
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void generateNextOrderId() {
        try {
//            lblOrderId.setText(OrderModel.getNextOrderId());
            lblOrderId.setText(orderBO.generateNewOrderId());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void generateNextDeliverId() {
        try {
//            lblDeliverId.setText(DeliveryModel.getNextDeliverId());
            lblDeliverId.setText(detailBO.generateNewDeliverDetailId());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnBackOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/OrderForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
