package models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * <p>Класс сущность</p>
 * <p>Класс представляет сущность студента с полями: ФИО, Идентификатор студента
 * коллекцией Посещения, коллекцией Сдачи Лабораторной работы</p>
 * @author Kononov Pavel
 * @version 1.0 05.05.2022
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    private String firstName;
    private String lastName;
    private String patronymic;
    private String groupId;
    private String studentId;//10702419 + 12
    //кол-во посещений
    private Integer countOfAttendance;
    //лист дат, по которым можно будет получить студентов
    private Map<Date, Boolean> attendanceOfStudent = new HashMap<>();
    private Map<String, Boolean> passedOrFailedLab = new HashMap<>();  // l01
}
