package lk.ijse.cargoproconnect.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomerDTO {
    private String id;
    private String fName;
    private String lName;
    private String contactN1;
    private String contactN2;
    private String documentType;
    private String documentNumber;
    private String email;
}
