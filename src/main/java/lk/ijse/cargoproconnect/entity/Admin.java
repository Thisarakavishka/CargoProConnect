package lk.ijse.cargoproconnect.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Admin {
    private String id;
    private String username;
    private String password;
    private String email;

    public Admin(String id, String userName, String email) {
        this.id = id;
        this.username = userName;
        this.email = email;
    }
}
