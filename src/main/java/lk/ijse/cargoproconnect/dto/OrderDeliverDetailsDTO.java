package lk.ijse.cargoproconnect.dto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderDeliverDetailsDTO {
    private String orderId;
    private String deliverId;
}
