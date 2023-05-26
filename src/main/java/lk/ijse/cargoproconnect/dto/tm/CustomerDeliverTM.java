package lk.ijse.cargoproconnect.dto.tm;

import javafx.scene.control.Label;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomerDeliverTM {
    private String id;
    private String address;
    private String contactNumber1;
    private String contactNumber2;
    private Label isDelivered;
    private String confirmedBy;
}
