package lk.ijse.cargoproconnect.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Payment {
    private String id;
    private String paymentType;
    private double total;
    private double totalTax;
}
