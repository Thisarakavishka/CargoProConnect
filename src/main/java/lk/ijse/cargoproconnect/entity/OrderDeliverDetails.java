package lk.ijse.cargoproconnect.entity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderDeliverDetails {
    private String orderId;
    private String deliverId;
}
