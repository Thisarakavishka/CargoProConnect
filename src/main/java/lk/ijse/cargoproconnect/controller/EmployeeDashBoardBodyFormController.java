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
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.cargoproconnect.bo.BOFactory;
import lk.ijse.cargoproconnect.bo.bos.BatchBO;
import lk.ijse.cargoproconnect.bo.bos.CustomerBO;
import lk.ijse.cargoproconnect.bo.bos.OrderDeliverDetailBO;
import lk.ijse.cargoproconnect.dto.*;
import lk.ijse.cargoproconnect.dto.tm.ActionDeliverTM;
import lk.ijse.cargoproconnect.dto.tm.AvailableOrderTM;
import lk.ijse.cargoproconnect.model.*;
import lk.ijse.cargoproconnect.util.Colors;
import lk.ijse.cargoproconnect.util.NotificationUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeDashBoardBodyFormController implements Initializable {

    @FXML
    public AnchorPane rootChange;

    @FXML
    private LineChart<?, ?> lineChart;

    @FXML
    private Label lblTotalCustomers;

    @FXML
    private JFXButton btnCustomer;

    @FXML
    private Label lblTotalOrders;

    @FXML
    private JFXButton btnOrders;

    @FXML
    private Label lblAvailableBatches;

    @FXML
    private JFXButton btnBatch;

    @FXML
    private Label lblAllTaxes;

    @FXML
    private JFXButton btnTax;

    @FXML
    private JFXButton btnReport;

    @FXML
    private TableView<AvailableOrderTM> tableOrders;

    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private TableColumn<?, ?> colOrderDate;

    @FXML
    private TableColumn<?, ?> colDeliverDate;

    @FXML
    private TableColumn<?, ?> colBatchId;

    @FXML
    private TableColumn<?, ?> colCustomerId;


    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private Pagination pagination;

    @FXML
    private TableView<ActionDeliverTM> tableDeliver;

    @FXML
    private TableColumn<?, ?> colDeliverId;

    @FXML
    private TableColumn<?, ?> colDeliverAddress;

    @FXML
    private TableColumn<?, ?> colContact1;

    @FXML
    private TableColumn<?, ?> colContact2;

    @FXML
    private TableColumn<?, ?> colDeliverAction;

    @FXML
    private JFXTextField txtSearchDeliver;

    private ObservableList<ActionDeliverTM> listActionDeliver = FXCollections.observableArrayList();
    private ObservableList<AvailableOrderTM> listAvailableOrder = FXCollections.observableArrayList();
    private static int ROWS_PER_PAGE = 10;

    //Dependency Injection (Property Injection)
    BatchBO batchBO = (BatchBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.BATCH);
    CustomerBO customerBO = (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);
    OrderDeliverDetailBO orderDeliverDetailBO = (OrderDeliverDetailBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDER_DELIVER_DETAIL);

    @FXML
    void btnBatchOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/BatchForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnCustomerOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/CustomerForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnOrdersOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/OrderForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnReportOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/ReportForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnTaxOnAction(ActionEvent event) {
        try {
            rootChange.getChildren().clear();
            rootChange.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/cargoproconnect/view/TaxForm.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDataTolineChart();
        setCellValueFactory();
        setOrdersForCheckTableData();
        setDeliverTableData();
        setSearchFilter();
        setDataToLabels();
    }

    private void setDataToLabels() {
        try {
//            List<CustomerDTO> customers = CustomerModel.getCustomers();
            List<CustomerDTO> customers = customerBO.getAllCustomers();
            List<OrderDTO> orders = OrderModel.getOrders(1);
            List<BatchDTO> batches = batchBO.getAvailableBatches();
            List<TaxDTO> taxes = TaxModel.getTaxes();
            lblTotalCustomers.setText(String.valueOf(customers.size()));
            lblTotalOrders.setText(String.valueOf(orders.size()));
            lblAvailableBatches.setText(String.valueOf(batches.size()));
            lblAllTaxes.setText(String.valueOf(taxes.size()));

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setSearchFilter() {
        FilteredList<ActionDeliverTM> filteredData = new FilteredList<>(listActionDeliver, b -> true);

        txtSearchDeliver.setOnKeyPressed(keyEvent -> {
            txtSearchDeliver.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate(actionDeliverTM -> {
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                    String searchKeyWord = newValue.toLowerCase();
                    if (actionDeliverTM.getDeliverId().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (actionDeliverTM.getAddress().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (actionDeliverTM.getContact1().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (actionDeliverTM.getContact2().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }));

            SortedList<ActionDeliverTM> sortedList = new SortedList<>(filteredData);
            sortedList.comparatorProperty().bind(tableDeliver.comparatorProperty());
            tableDeliver.setItems(sortedList);
        });
    }

    private void setDeliverTableData() {
        try {
//            List<DeliveryDTO> deliveries = OrderDeliveryModel.getDeliveries(0);
            List<DeliveryDTO> deliveries = orderDeliverDetailBO.getDeliveries(0);
            if (deliveries == null) {
                tableDeliver.getItems().clear();
            } else if (!deliveries.isEmpty() || deliveries != null) {
                for (DeliveryDTO delivery : deliveries) {

                    JFXButton confirm = new JFXButton("confirm");
                    confirm.setStyle("-fx-background-color: "+ Colors.GREEN +";-fx-border-radius: 10px;-fx-text-fill: "+ Colors.WHITE +";-fx-font-weight: bold;-fx-font-size: 15px");
                    setConfirmBtnOnAction(delivery, confirm);

                    listActionDeliver.add(new ActionDeliverTM(
                            delivery.getId(),
                            delivery.getAddress(),
                            delivery.getContact1(),
                            delivery.getContact2(),
                            confirm
                    ));
                    pagination.setPageCount((int) Math.ceil(listActionDeliver.size() / 10.0));
                    pagination.setPageFactory(this::createPage);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private TableView createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, listActionDeliver.size());
        tableDeliver.setItems(FXCollections.observableArrayList(listActionDeliver.subList(fromIndex, toIndex)));

        return tableDeliver;
    }

    private void setOrdersForCheckTableData() {
        try {
            List<OrderDTO> orders = OrderModel.getOrders(0);
            for (OrderDTO order : orders) {

                JFXButton check = new JFXButton("Check");
                check.setStyle("-fx-background-color: "+ Colors.GREEN +";-fx-border-radius: 10px;-fx-text-fill: "+ Colors.WHITE +";-fx-font-weight: bold;-fx-font-size: 15px");
                setCheckBtnOnAction(order, check);

                listAvailableOrder.add(new AvailableOrderTM(
                        order.getId(),
                        order.getOrderDate(),
                        order.getDeliverDate(),
                        order.getBatchId(),
                        order.getCustomerId(),
                        check
                ));
            }
            tableOrders.setItems(listAvailableOrder);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setCheckBtnOnAction(OrderDTO order, JFXButton check) {
        check.setOnAction(event -> {
            try {
                boolean checkOrder = OrderModel.checkOrder(order.getId(), LoginModel.getEmployeeId(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
                if (checkOrder) {
                    NotificationUtil.showNotification("Checked", "Order " + order.getId() + " Checked SuccessFully", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                    listAvailableOrder = FXCollections.observableArrayList();
                    listActionDeliver = FXCollections.observableArrayList();
                    setOrdersForCheckTableData();
                    setDeliverTableData();
                    setDataToLabels();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void setConfirmBtnOnAction(DeliveryDTO delivery, JFXButton confirm) {
        confirm.setOnAction(event -> {
            try {
                boolean isConfirm = orderDeliverDetailBO.confirmDeliver(delivery.getId(), LoginModel.getEmployeeId());
                if (isConfirm) {
                    NotificationUtil.showNotification("Confirmed", "Deliver " + delivery.getId() + " Confirmed SuccessFully", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
                    listActionDeliver = FXCollections.observableArrayList();
                    tableDeliver.refresh();
                    setDeliverTableData();
                    setDataToLabels();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void setCellValueFactory() {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("OrderDate"));
        colDeliverDate.setCellValueFactory(new PropertyValueFactory<>("DeliverDate"));
        colBatchId.setCellValueFactory(new PropertyValueFactory<>("BatchId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        colDeliverId.setCellValueFactory(new PropertyValueFactory<>("deliverId"));
        colDeliverAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact1.setCellValueFactory(new PropertyValueFactory<>("contact1"));
        colContact2.setCellValueFactory(new PropertyValueFactory<>("contact2"));
        colDeliverAction.setCellValueFactory(new PropertyValueFactory<>("action"));
    }

    private void setDataTolineChart() {
        XYChart.Series series = new XYChart.Series();
        series.setName("Orders");
        try {
            List<OrderDTO> orders = OrderModel.getOrders();
            for (OrderDTO order : orders) {
                int count = 0;
                for (OrderDTO order1 : orders) {
                    if (order.getOrderDate().equalsIgnoreCase(order1.getOrderDate())) {
                        count += 1;
                    }
                }
                series.getData().add(new XYChart.Data<>(order.getOrderDate(), count));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        lineChart.getData().addAll(series);
    }
}
