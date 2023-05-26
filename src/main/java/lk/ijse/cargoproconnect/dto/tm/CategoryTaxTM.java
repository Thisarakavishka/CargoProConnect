package lk.ijse.cargoproconnect.dto.tm;

import javafx.scene.layout.HBox;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CategoryTaxTM {
    private String id;
    private String name;
    private Double percentage;
    private String description;
    private HBox actions;
}
