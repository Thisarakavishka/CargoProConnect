package lk.ijse.cargoproconnect.dao;

public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getDaoFactory() {
        return (daoFactory == null) ? daoFactory = new DAOFactory() : daoFactory;
    }

    public enum DAOTypes {ADMIN, BATCH, CUSTOMER, DELIVER_DETAIL, EMPLOYEE, ITEM_CATEGORY, ITEM_CATEGORY_TAX_DETAIL, ORDER, ORDER_DELIVER_DETAIL, ORDER_ITEM_CATEGORY, PAYMENT, TAX}

    //need to implement getDAO() method
}
