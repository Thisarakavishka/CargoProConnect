package lk.ijse.cargoproconnect.dto.tm;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Label;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ViewBatchTM {
    private String id;
    private String shipmentType;
    private Integer availableWeight;
    private String sDate;
    private String dDate;
    private String to;
    private Label status;
    private JFXButton select;
}
