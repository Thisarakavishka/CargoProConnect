package lk.ijse.cargoproconnect.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Batch {
    private String id;
    private String sDate;
    private String dDate;
    private Integer availableStatus;
    private Integer totalWeight;
    private Integer currentWeight;
    private String deliveryAddress;
    private Integer noOfOrders;
    private String shipmentType;

    public Batch(String id, LocalDate shipmentDate, LocalDate deliverDate, String totalWeight, String deliverAddress, String shipmentType) {
        this.id = id;
        this.sDate = shipmentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.dDate = deliverDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.totalWeight = Integer.valueOf(totalWeight);
        this.deliveryAddress = deliverAddress;
        this.shipmentType = shipmentType;
    }
}
