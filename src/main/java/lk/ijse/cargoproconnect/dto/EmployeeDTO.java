package lk.ijse.cargoproconnect.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeDTO {
    private String id;
    private String username;
    private String password;
    private String email;
    private String documentType;
    private String documentNumber;
    private Integer status;

    public EmployeeDTO(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public EmployeeDTO(String id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
