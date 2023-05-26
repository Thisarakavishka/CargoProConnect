package lk.ijse.cargoproconnect.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ActionDeliverTM {
    private String deliverId;
    private String address;
    private String contact1;
    private String contact2;
    private JFXButton action;
}
