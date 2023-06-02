package lk.ijse.cargoproconnect.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Customer {
    private String id;
    private String fName;
    private String lName;
    private String contactN1;
    private String contactN2;
    private String documentType;
    private String documentNumber;
    private String email;
}
