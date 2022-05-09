package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 *<p>Класс сущность</p>
 *<p>Класс представляет модель Группы с полями : Идентификтор группы
 * Создана ли была уже группа, списком студентов</p>
 * @author Kononov Pavel
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Group {

    private String groupId;
    private boolean isCreatedFirstly;
    private List<Student> students = new ArrayList<>();
}
