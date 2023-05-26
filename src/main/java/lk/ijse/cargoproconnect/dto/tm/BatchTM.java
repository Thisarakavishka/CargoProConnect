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
public class BatchTM {
    private String id;
    private String shipmentType;
    private Integer totalWeight;
    private Integer currentWeight;
    private String sDate;
    private String dDate;
    private String to;
    private Label availableStatus;
    private HBox Actions;
    private JFXCheckBox checkBox;
}
