package lk.ijse.cargoproconnect.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderDTO {
    private String id;
    private String customerId;
    private String paymentId;
    private String batchId;
    private String orderDate;
    private Integer isChecked;
    private String checkBy;
    private String checkTime;
    private Integer isDeliver;
    private String DeliverDate;
    private Integer weight;
    private Double totalPrice;

    public OrderDTO(String id, String customerId, String paymentId, String batchId, String orderDate ) {
        this.id = id;
        this.customerId = customerId;
        this.paymentId = paymentId;
        this.batchId = batchId;
        this.orderDate = orderDate;
    }

    public OrderDTO(String id, String customerId, String paymentId, String batchId, String orderDate, String deliverDate, int weight , int totalPrice) {
        this.id = id;
        this.customerId = customerId;
        this.paymentId = paymentId;
        this.batchId = batchId;
        this.orderDate = orderDate;
        this.DeliverDate = deliverDate;
        this.weight = weight;
        this.totalPrice = (double) totalPrice;
    }
}
