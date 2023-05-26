package lk.ijse.cargoproconnect.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeRequestTM {
    private String id;
    private String userName;
    private String password;
    private String email;
    private String documentType;
    private String documentNumber;
    private JFXButton approve;
}
