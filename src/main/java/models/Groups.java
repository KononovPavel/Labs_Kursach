package models;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.Map;
/**
 *<p>Класс сущность</p>
 * <p>Класс представяет собой модель набора групп, со списком всех групп</p>
 * @author Kononov Pavel
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Groups {

    private Map<String, Group> groups = new HashMap<>();
}
