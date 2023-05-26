package lk.ijse.cargoproconnect.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderItemTM {
    private String item;
    private String category;
    private String note;
    private Integer taxes;
    private double unitWeight;
    private Integer qty;
    private String total;
    private Double totalTax;
    private JFXButton button;
}
