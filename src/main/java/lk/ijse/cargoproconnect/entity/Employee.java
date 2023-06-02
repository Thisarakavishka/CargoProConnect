package lk.ijse.cargoproconnect.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Employee {
    private String id;
    private String username;
    private String password;
    private String email;
    private String documentType;
    private String documentNumber;
    private Integer status;

    public Employee(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public Employee(String id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
