package lk.ijse.cargoproconnect.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Tax {
    private String id;
    private String name;
    private Double percentage;
    private String description;
}
