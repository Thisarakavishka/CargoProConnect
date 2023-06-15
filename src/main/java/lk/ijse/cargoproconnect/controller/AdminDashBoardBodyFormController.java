package lk.ijse.cargoproconnect.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.cargoproconnect.bo.BOFactory;
import lk.ijse.cargoproconnect.bo.bos.BatchBO;
import lk.ijse.cargoproconnect.bo.bos.CustomerBO;
import lk.ijse.cargoproconnect.dto.*;
import lk.ijse.cargoproconnect.dto.tm.CustomerDeliverTM;
import lk.ijse.cargoproconnect.dto.tm.EmployeeRequestTM;
import lk.ijse.cargoproconnect.model.*;
import lk.ijse.cargoproconnect.util.Colors;
import lk.ijse.cargoproconnect.util.NotificationUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AdminDashBoardBodyFormController implements Initializable {

    @FXML
    public Label lblAvailableBatches;

    @FXML
    public Label lblAllTaxes;

    @FXML
    public ScrollPane scrollPane;

    @FXML
    private AnchorPane changeRoot;

    @FXML
    private LineChart<?, ?> lineChart;

    @FXML
    private Label lblTotalCustomers;

    @FXML
    private JFXButton btnCustomer;

    @FXML
    private Label lblTotalEmployees;

    @FXML
    private JFXButton btnEmployee;

    @FXML
    private JFXButton btnBatch;

    @FXML
    private JFXButton btnTax;

    @FXML
    private JFXButton btnReport;

    @FXML
    private TableView<EmployeeRequestTM> tableEmployee;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colUserName;

    @FXML
    private TableColumn<?, ?> colPassword;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colDocumentType;

    @FXML
    private TableColumn<?, ?> colDocumentNumber;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private Pagination pagination;

    @FXML
    private TableView<CustomerDeliverTM> tableDeliver;

    @FXML
    private TableColumn<?, ?> colDeliverId;

    @FXML
    private TableColumn<?, ?> colDeliverAddress;

    @FXML
    private TableColumn<?, ?> colContact1;

    @FXML
    private TableColumn<?, ?> colContact2;

    @FXML
    private TableColumn<?, ?> colIsDeliver;

    @FXML
    private TableColumn<?, ?> colConfirmedBy;

    @FXML
    private JFXTextField txtSearchDeliver;

    static int ROWS_PER_PAGE = 8;
    private ObservableList<EmployeeRequestTM> listEmployeeRequest = FXCollections.observableArrayList();
    private ObservableList<CustomerDeliverTM> listCustomerDeliver = FXCollections.observableArrayList();

    //Dependency Injection (Property Injection)
    BatchBO batchBO = (BatchBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.BATCH);
    CustomerBO customerBO= (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);

    @FXML
    void btnBatchOnAction(ActionEvent event) {
        try {
            changeRoot.getChildren().clear();
            changeRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/BatchForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnCustomerOnAction(ActionEvent event) {
        try {
            changeRoot.getChildren().clear();
            changeRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/CustomerForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnEmployeeOnAction(ActionEvent event) {
        try {
            changeRoot.getChildren().clear();
            changeRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/EmployeeForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnReportOnAction(ActionEvent event) {
        try {
            changeRoot.getChildren().clear();
            changeRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/ReportForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnTaxOnAction(ActionEvent event) {
        try {
            changeRoot.getChildren().clear();
            changeRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/TaxForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDataTolineChart();
        setCellValueFactory();
        setEmployeeTableData();
        setDeliverTableData();
        setSearchFilter();
        setDataToLabels();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    private void setDataToLabels() {
        try {
//            List<CustomerDTO> customers = CustomerModel.getCustomers();
            List<CustomerDTO> customers = customerBO.getAllCustomers();
            List<EmployeeDTO> employees = EmployeeModel.getEmployees(1);
            List<BatchDTO> batches = batchBO.getAvailableBatches();
            List<TaxDTO> taxes = TaxModel.getTaxes();
            lblTotalCustomers.setText(String.valueOf(customers.size()));
            lblTotalEmployees.setText(String.valueOf(employees.size()));
            lblAvailableBatches.setText(String.valueOf(batches.size()));
            lblAllTaxes.setText(String.valueOf(taxes.size()));

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setSearchFilter() {
        FilteredList<CustomerDeliverTM> filteredData = new FilteredList<>(listCustomerDeliver, b -> true);

        txtSearchDeliver.setOnKeyPressed(keyEvent -> {
            txtSearchDeliver.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate(customerDeliverTM -> {
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                    String searchKeyWord = newValue.toLowerCase();
                    if (customerDeliverTM.getId().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (customerDeliverTM.getAddress().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (customerDeliverTM.getContactNumber1().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (customerDeliverTM.getContactNumber2().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (customerDeliverTM.getIsDelivered().getText().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (customerDeliverTM.getConfirmedBy() != null) {
                        if (customerDeliverTM.getConfirmedBy().toLowerCase().indexOf(searchKeyWord) > -1) {
                            return true;
                        }else{
                            return false;
                        }
                    } else {
                        return false;
                    }
                });
            }));

            SortedList<CustomerDeliverTM> sortedList = new SortedList<>(filteredData);
            sortedList.comparatorProperty().bind(tableDeliver.comparatorProperty());
            tableDeliver.setItems(sortedList);
        });
    }

    private void setDeliverTableData() {
        try {
            List<DeliveryDTO> deliveries = DeliveryModel.getDeliveries();
            for (DeliveryDTO delivery : deliveries) {

                Label label = new Label(delivery.getIsDelivered() == 1 ? "Delivered" : "Not Yet");
                label.setStyle(label.getText().equalsIgnoreCase("Delivered") ? "-fx-font-weight: bold;-fx-text-fill: "+ Colors.GREEN +";-fx-font-size: 15;" : "-fx-font-weight: bold;-fx-text-fill: "+ Colors.RED +";-fx-font-size: 15;");

                listCustomerDeliver.add(new CustomerDeliverTM(
                        delivery.getId(),
                        delivery.getAddress(),
                        delivery.getContact1(),
                        delivery.getContact2(),
                        label,
                        delivery.getConfirmedBy()
                ));
                pagination.setPageCount((int) Math.ceil(listCustomerDeliver.size() / 8.0));
                pagination.setPageFactory(this::createPage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private TableView createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, listCustomerDeliver.size());
        tableDeliver.setItems(FXCollections.observableArrayList(listCustomerDeliver.subList(fromIndex, toIndex)));

        return tableDeliver;
    }

    private void setEmployeeTableData() {
        try {
            List<EmployeeDTO> employees = EmployeeModel.getEmployees(0);
            for (EmployeeDTO employee : employees) {

                JFXButton approve = new JFXButton("Approve");
                approve.setStyle("-fx-background-color: "+ Colors.GREEN +";-fx-border-radius: 10px;-fx-text-fill: "+ Colors.WHITE +";-fx-font-weight: bold;-fx-font-size: 15px");
                setApproveBtnOnAction(employee, approve);

                listEmployeeRequest.add(new EmployeeRequestTM(
                        employee.getId(),
                        employee.getUsername(),
                        employee.getPassword(),
                        employee.getEmail(),
                        employee.getDocumentType(),
                        employee.getDocumentNumber(),
                        approve
                ));
            }
            tableEmployee.setItems(listEmployeeRequest);
//            tableEmployee.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setApproveBtnOnAction(EmployeeDTO employee, JFXButton approve) {
        approve.setOnAction(event -> {
            try {
                boolean isApprove = EmployeeModel.approveEmployee(employee.getId());
                if (isApprove) {
                    NotificationUtil.showNotification("Approved", "Employee " + employee.getUsername() + " Approve SuccessFully", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                    listEmployeeRequest = FXCollections.observableArrayList();
                    setEmployeeTableData();
                    setDataToLabels();
                } else {
                    System.out.println("No");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDocumentType.setCellValueFactory(new PropertyValueFactory<>("documentType"));
        colDocumentNumber.setCellValueFactory(new PropertyValueFactory<>("documentNumber"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("approve"));

        colDeliverId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDeliverAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact1.setCellValueFactory(new PropertyValueFactory<>("contactNumber1"));
        colContact2.setCellValueFactory(new PropertyValueFactory<>("contactNumber2"));
        colIsDeliver.setCellValueFactory(new PropertyValueFactory<>("isDelivered"));
        colConfirmedBy.setCellValueFactory(new PropertyValueFactory<>("confirmedBy"));
    }

    private void setDataTolineChart() {
        XYChart.Series orderDate = new XYChart.Series();
        orderDate.setName("Order Date");
        try {
            List<OrderDTO> orders = OrderModel.getOrders();
            for (OrderDTO order : orders) {
                int orderD = 0;
                for (OrderDTO order1 : orders) {
                    if (order.getOrderDate().equalsIgnoreCase(order1.getOrderDate())) {
                        orderD += 1;
                    }
                }
                orderDate.getData().add(new XYChart.Data<>(order.getOrderDate(), orderD));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        lineChart.getData().addAll(orderDate);
    }
}
