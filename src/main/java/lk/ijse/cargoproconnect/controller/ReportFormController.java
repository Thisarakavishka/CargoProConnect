package lk.ijse.cargoproconnect.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import lk.ijse.cargoproconnect.db.DBConnection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.sql.SQLException;

public class ReportFormController {

    @FXML
    public JFXTextField txtOrderId;

    @FXML
    public JFXButton btnOrder;

    @FXML
    public JFXTextField txtEmployeeId;

    @FXML
    public JFXButton btnEmployee;

    @FXML
    public JFXTextField txtCustomerId;

    @FXML
    public JFXButton btnCustomer;

    @FXML
    private AnchorPane rootChange;

    @FXML
    private JFXButton btnViewBatch;

    @FXML
    private JFXButton btnViewOrder;

    @FXML
    private JFXButton btnViewCustomer;

    @FXML
    private JFXButton btnViewEmployee;

    @FXML
    private JFXButton btnViewTax;

    @FXML
    private JFXButton btnViewCategory;

    @FXML
    void btnViewBatchOnAction(ActionEvent event) {
        new Thread() {
            @Override
            public void run() {
                try {
                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\thisa\\IdeaProjects\\CargoProConnect\\src\\main\\resources\\lk\\ijse\\cargoproconnect\\report\\Batches.jrxml"));
                    JasperReport js = JasperCompileManager.compileReport(load);
                    JasperPrint jp = JasperFillManager.fillReport(js, null, DBConnection.getInstance().getConnection());
                    JasperViewer viewer = new JasperViewer(jp, false);
                    viewer.show();
                    viewer.setFitWidthZoomRatio();
                } catch (SQLException | JRException e) {
                    e.printStackTrace();
                }
                this.stop();
            }
        }.start();
    }

    @FXML
    void btnViewCategoryOnAction(ActionEvent event) {
        new Thread() {
            @Override
            public void run() {
                try {
                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\thisa\\IdeaProjects\\CargoProConnect\\src\\main\\resources\\lk\\ijse\\cargoproconnect\\report\\Categories.jrxml"));
                    JasperReport js = JasperCompileManager.compileReport(load);
                    JasperPrint jp = JasperFillManager.fillReport(js, null, DBConnection.getInstance().getConnection());
                    JasperViewer viewer = new JasperViewer(jp, false);
                    viewer.show();
                    viewer.setFitWidthZoomRatio();
                } catch (SQLException | JRException e) {
                    e.printStackTrace();
                }
                this.stop();
            }
        }.start();
    }

    @FXML
    void btnViewCustomerOnAction(ActionEvent event) {
        new Thread() {
            @Override
            public void run() {
                try {
                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\thisa\\IdeaProjects\\CargoProConnect\\src\\main\\resources\\lk\\ijse\\cargoproconnect\\report\\Customers.jrxml"));
                    JasperReport js = JasperCompileManager.compileReport(load);
                    JasperPrint jp = JasperFillManager.fillReport(js, null, DBConnection.getInstance().getConnection());
                    JasperViewer viewer = new JasperViewer(jp, false);
                    viewer.show();
                    viewer.setFitWidthZoomRatio();
                } catch (SQLException | JRException e) {
                    e.printStackTrace();
                }
                this.stop();
            }
        }.start();
    }

    @FXML
    void btnViewEmployeeOnAction(ActionEvent event) {
        new Thread() {
            @Override
            public void run() {
                try {
                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\thisa\\IdeaProjects\\CargoProConnect\\src\\main\\resources\\lk\\ijse\\cargoproconnect\\report\\Employees.jrxml"));
                    JasperReport js = JasperCompileManager.compileReport(load);
                    JasperPrint jp = JasperFillManager.fillReport(js, null, DBConnection.getInstance().getConnection());
                    JasperViewer viewer = new JasperViewer(jp, false);
                    viewer.show();
                    viewer.setFitWidthZoomRatio();
                } catch (SQLException | JRException e) {
                    e.printStackTrace();
                }
                this.stop();
            }
        }.start();
    }

    @FXML
    void btnViewOrderOnAction(ActionEvent event) {
        new Thread() {
            @Override
            public void run() {
                try {
                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\thisa\\IdeaProjects\\CargoProConnect\\src\\main\\resources\\lk\\ijse\\cargoproconnect\\report\\Orders.jrxml"));
                    JasperReport js = JasperCompileManager.compileReport(load);
                    JasperPrint jp = JasperFillManager.fillReport(js, null, DBConnection.getInstance().getConnection());
                    JasperViewer viewer = new JasperViewer(jp, false);
                    viewer.show();
                    viewer.setFitWidthZoomRatio();
                } catch (SQLException | JRException e) {
                    e.printStackTrace();
                }
                this.stop();
            }
        }.start();
    }

    @FXML
    void btnViewTaxOnAction(ActionEvent event) {

        new Thread() {
            @Override
            public void run(){
                try {
                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\thisa\\IdeaProjects\\CargoProConnect\\src\\main\\resources\\lk\\ijse\\cargoproconnect\\report\\Taxes.jrxml"));
                    JasperReport js = JasperCompileManager.compileReport(load);
                    JasperPrint jp = JasperFillManager.fillReport(js, null, DBConnection.getInstance().getConnection());
                    JasperViewer viewer = new JasperViewer(jp, false);
                    viewer.show();
                    viewer.setFitWidthZoomRatio();
                } catch (SQLException | JRException e) {
                    e.printStackTrace();
                }
                this.stop();
            }
        }.start();
    }


}


//
//package lk.ijse.cargoproconnect.controller;
//
//import com.jfoenix.controls.JFXTextField;
//import javafx.event.ActionEvent;
//import lk.ijse.cargoproconnect.db.DBConnection;
//import net.sf.jasperreports.engine.*;
//import net.sf.jasperreports.engine.design.JasperDesign;
//import net.sf.jasperreports.engine.xml.JRXmlLoader;
//import net.sf.jasperreports.view.JasperViewer;
//
//import java.io.File;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class ReportFormController {
//    public JFXTextField txtTest;
//
//    public void btnClickOnAction(ActionEvent event) {
//        new Thread() {
//            @Override
//            public void run() {
//                try {
////                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\thisa\\IdeaProjects\\demoCargoProConnect\\src\\main\\resources\\lk\\ijse\\cargoproconnect\\report\\Customers.jrxml"));
////                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\thisa\\IdeaProjects\\demoCargoProConnect\\src\\main\\resources\\lk\\ijse\\cargoproconnect\\report\\Employees.jrxml"));
////                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\thisa\\IdeaProjects\\demoCargoProConnect\\src\\main\\resources\\lk\\ijse\\cargoproconnect\\report\\Batches.jrxml"));
////                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\thisa\\IdeaProjects\\demoCargoProConnect\\src\\main\\resources\\lk\\ijse\\cargoproconnect\\report\\Taxes.jrxml"));
////                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\thisa\\IdeaProjects\\demoCargoProConnect\\src\\main\\resources\\lk\\ijse\\cargoproconnect\\report\\Categories.jrxml"));
//                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\thisa\\IdeaProjects\\demoCargoProConnect\\src\\main\\resources\\lk\\ijse\\cargoproconnect\\report\\Orders.jrxml"));
//
//                    JasperReport js = JasperCompileManager.compileReport(load);
//
//                    Map<String, Object> data = new HashMap<>();
//                    data.put("document_type", txtTest.getText());
//
//                    JasperPrint jp = JasperFillManager.fillReport(js, data, DBConnection.getInstance().getConnection());
//
//                    JasperViewer viewer = new JasperViewer(jp, false);
//                    viewer.show();
//                } catch (JRException | SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//}
