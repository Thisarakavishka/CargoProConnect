package lk.ijse.cargoproconnect.bo;

import lk.ijse.cargoproconnect.bo.bos.impl.*;

public class BOFactory {

    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getBoFactory() {
        return (boFactory == null) ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOTypes {ADMIN, BATCH, CUSTOMER, DELIVER_DETAIL, EMPLOYEE, ITEM_CATEGORY, ITEM_CATEGORY_TAX_DETAIL, ORDER, ORDER_DELIVER_DETAIL, ORDER_ITEM_CATEGORY, PAYMENT, TAX}

    public SuperBO getBO(BOTypes boTypes) {
        switch (boTypes) {
            case ADMIN:
                return new AdminBOImpl();
            case BATCH:
                return new BatchBOImpl();
            case CUSTOMER:
                return new CustomerBOImpl();
            case DELIVER_DETAIL:
                return new DeliverDetailBOImpl();
            case EMPLOYEE:
                return new EmployeeBOImpl();
            case ITEM_CATEGORY:
                return new ItemCategoryBOImpl();
            case ITEM_CATEGORY_TAX_DETAIL:
                return new ItemCategoryTaxDetailBOImpl();
            case ORDER:
                return new OrderBOImpl();
            case ORDER_DELIVER_DETAIL:
                return new OrderDeliverDetailBOImpl();
            case ORDER_ITEM_CATEGORY:
                return new OrderItemCategoryBOImpl();
            case PAYMENT:
                return new PaymentBOImpl();
            case TAX:
                return new TaxBOImpl();
            default:
                return null;
        }
    }
}
