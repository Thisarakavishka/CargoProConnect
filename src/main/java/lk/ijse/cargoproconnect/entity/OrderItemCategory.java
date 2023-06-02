package lk.ijse.cargoproconnect.entity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderItemCategory {
    private String orderId;
    private String itemCategoryId;
    private String itemName;
    private String note;
    private Double weight;
    private Integer qty;
    private Double totalTax;
}
