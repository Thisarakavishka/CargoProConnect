package lk.ijse.cargoproconnect.util;

import lk.ijse.cargoproconnect.dto.Delivery;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiptGeneratorUtil {
    private static final DecimalFormat decimalFormatter = new DecimalFormat("0.00");

    public static String generate(String customerId, String batchId, int weight, double totalAmount, Delivery delivery, String paymentMethod) {
        StringBuilder sb = new StringBuilder();

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        String formattedDate = sdf.format(date);

        //  header
        sb.append("================================================\n");
        sb.append("                              \t\tRECEIPT\n");
        sb.append("================================================\n");
        sb.append("Customer Id: ").append(customerId).append("                                    \tbatch Id: ").append(batchId).append("\n\n\n");
        //  content
        sb.append("* To : ").append(delivery.getAddress()).append("\n");
        sb.append("* Contact number 1 : ").append(delivery.getContact1()).append("\n");
        sb.append("* Contact number 2 : ").append(delivery.getContact2()).append("\n");
        sb.append("* Total weight :").append(weight).append(" Kg\n\n");
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String formattedTotalAmount = nf.format(totalAmount);
        sb.append("Total Amount : ").append(formattedTotalAmount).append("\n");
        sb.append("Payment Method : ").append(paymentMethod).append("\n\n\n");
        // footer
        sb.append("Date : ").append(formattedDate).append("\n");
        sb.append("================================================\n");

        return sb.toString();
    }
}
