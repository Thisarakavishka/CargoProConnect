package lk.ijse.cargoproconnect.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DeliverDetails {
    private String id;
    private String address;
    private String contact1;
    private String contact2;
    private Integer isDelivered;
    private String confirmedBy;

    public DeliverDetails(String id, String address, String contact1, String contact2) {
        this.id = id;
        this.address = address;
        this.contact1 = contact1;
        this.contact2 = contact2;
    }
}
