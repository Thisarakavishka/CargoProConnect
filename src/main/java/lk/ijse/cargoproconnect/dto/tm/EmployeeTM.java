package lk.ijse.cargoproconnect.dto.tm;

import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.layout.HBox;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeTM {
    private String id;
    private String username;
    private String password;
    private String email;
    private String dType;
    private String dNumber;
    private HBox actions;
    private JFXCheckBox checkBox;
}
