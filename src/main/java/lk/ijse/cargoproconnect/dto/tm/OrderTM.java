package lk.ijse.cargoproconnect.dto.tm;

import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderTM {
    private String id;
    private String customerId;
    private String batchId;
    private String orderDate;
    private Label isChecked;
    private String checkBy;
    private String checkTime;
    private Label isDeliver;
    private String DeliverDate;
    private HBox actions;
    private JFXCheckBox checkBox;
}
