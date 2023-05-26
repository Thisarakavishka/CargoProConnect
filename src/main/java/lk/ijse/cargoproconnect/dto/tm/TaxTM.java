package lk.ijse.cargoproconnect.dto.tm;

import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.layout.HBox;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TaxTM {
    private String id;
    private String name;
    private Double percentage;
    private String description;
    private HBox actions;
    private JFXCheckBox checkBox;
}
