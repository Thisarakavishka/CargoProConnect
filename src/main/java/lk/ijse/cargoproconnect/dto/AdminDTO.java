package lk.ijse.cargoproconnect.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AdminDTO {
    private String id;
    private String username;
    private String password;
    private String email;

    public AdminDTO(String id, String userName, String email) {
        this.id = id;
        this.username = userName;
        this.email = email;
    }
}
