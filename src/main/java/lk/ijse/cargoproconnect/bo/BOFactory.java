package lk.ijse.cargoproconnect.bo;

public class BOFactory {

    private static BOFactory boFactory;

    private BOFactory(){
    }

    public static BOFactory getBoFactory(){return (boFactory == null) ? new BOFactory() : boFactory;}

    public enum BOTypes {BATCH}

    //need to implement getBO() method
}
