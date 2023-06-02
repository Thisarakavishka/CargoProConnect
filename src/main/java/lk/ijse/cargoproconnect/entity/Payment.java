package lk.ijse.cargoproconnect.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Payment {
    private String paymentId;
    private String paymentType;
    private double total;
    private double totalTax;
}
