package converters.impl;

import converters.StudentConverter;
import models.Student;

import java.util.List;


/**
 * <p>Класс конвертер</p>
 * <p>Класс, конвертирующий данные студента</p>
 * Класс, который реализует метод convert- конвертация идентификатора студента
 *
 * @author Kononov Pavel
 * @version 1.0
 */
public class StudentConverterImpl implements StudentConverter {

    /**
     * Метод, который создает уникальный личный номер студента группы
     *
     * @param groupId  - идентификатор группы
     * @param students - список студентов группы
     * @return - строку - личный номер студента
     */
    @Override
    public String convert(String groupId, List<Student> students) {
        if (students.isEmpty()) {
            return groupId + "01";
        }
        Integer groupSize = students.size();
        if (groupSize < 10) {
            return groupId + "0" + (++groupSize).toString();
        }
        return groupId + (++groupSize).toString();
    }
}
