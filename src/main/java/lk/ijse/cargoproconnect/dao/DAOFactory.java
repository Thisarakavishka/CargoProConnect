package lk.ijse.cargoproconnect.dao;

import lk.ijse.cargoproconnect.dao.daos.impl.*;

public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getDaoFactory() {
        return (daoFactory == null) ? daoFactory = new DAOFactory() : daoFactory;
    }

    public enum DAOTypes {ADMIN, BATCH, CUSTOMER, DELIVER_DETAIL, EMPLOYEE, ITEM_CATEGORY, ITEM_CATEGORY_TAX_DETAIL, ORDER, ORDER_DELIVER_DETAIL, ORDER_ITEM_CATEGORY, PAYMENT, TAX}

    public SuperDAO getDAO(DAOTypes daoTypes) {
        switch (daoTypes) {
            case ADMIN:
                return null; // not implement yet
            case BATCH:
                return new BatchDAOImpl();
            case CUSTOMER:
                return new CustomerDAOImpl();
            case DELIVER_DETAIL:
                return new DeliverDetailDAOImpl();
            case EMPLOYEE:
                return new EmployeeDAOImpl();
            case ITEM_CATEGORY:
                return new ItemCategoryDAOImpl();
            case ITEM_CATEGORY_TAX_DETAIL:
                return new ItemCategoryTaxDetailDAOImpl();
            case ORDER:
                return new OrderDAOImpl();
            case ORDER_DELIVER_DETAIL:
                return new OrderDeliverDetailDAOImpl();
            case ORDER_ITEM_CATEGORY:
                return new OrderItemCategoryDAOImpl();
            case PAYMENT:
                return new PaymentDAOImpl();
            case TAX:
                return new TaxDAOImpl();
            default:
                return null;
        }
    }
}
