package lk.ijse.cargoproconnect.dto.tm;

import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.layout.HBox;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomerTM {
    private String id;
    private String fName;
    private String lName;
    private String cNumber1;
    private String cNumber2;
    private String documentType;
    private String documentNumber;
    private HBox actions;
    private JFXCheckBox checkBox;
}
