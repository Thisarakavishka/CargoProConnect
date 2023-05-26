package lk.ijse.cargoproconnect.dto.tm;

import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.layout.HBox;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CategoryTM {
    private String id;
    private String name;
    private Integer availableTax;
    private HBox actions;
    private JFXCheckBox checkBox;
}
