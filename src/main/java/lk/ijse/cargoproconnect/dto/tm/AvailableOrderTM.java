package lk.ijse.cargoproconnect.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AvailableOrderTM {
    private String orderId;
    private String OrderDate;
    private String DeliverDate;
    private String BatchId;
    private String CustomerId;
    private JFXButton action;
}
