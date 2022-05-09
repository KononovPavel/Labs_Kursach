package controllers.impl;

import controllers.GroupController;
import models.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * <p>Класс контроллер</p>
 * <p>Класс, реализующий методы, для работы с определенной группой</p>
 * Здесь представленны методы, для сортировки студентов по кол-ву посещений,
 * по фамилии и присутствии на определнной дате занятия
 * @author Kononov Pavel
 * @version 1.0
 */
public class GroupControllerImpl implements GroupController {


    /**
     * Метод, который сортирует список студентов по кол-ву посещений
     * @param students  - список студентов
     * @param isFlag - направление сортировки
     * @return - отсортированный список
     */
    @Override
    public List<Student> sortStudentsByAttendanceCount(List<Student> students, boolean isFlag) {
        if (students.isEmpty()) {
            return null;
        }
        students.sort(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return isFlag
                        ? o2.getCountOfAttendance() - o1.getCountOfAttendance()
                        : o1.getCountOfAttendance() - o2.getCountOfAttendance();
            }
        });
        return students;
    }

    /**
     * Метод, который сортирует список студентов по фамилии
     * @param students  - список студентов
     * @param isFlag - направление сортировки
     * @return - отсортированный список
     */
    @Override
    public List<Student> sortStudentsByLastName(List<Student> students, boolean isFlag) {
        if (students.isEmpty()) {
            return null;
        }
        students.sort(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return isFlag
                        ? o1.getLastName().compareTo(o2.getLastName())
                        : o2.getLastName().compareTo(o1.getLastName());
            }
        });
        return students;
    }

    /**
     * Метод, который реализует поиск студентов посещавших занятия для определенной даты
     * @param dateOfLab - дата занятия
     * @param students - список студентов
     * @return - отсортированный список студентов
     */
    @Override
    public List<Student> sortStudentByDateOfLab(Date dateOfLab, List<Student> students) {
        List<Student> sortedStudents = new ArrayList<>();
        if (students.isEmpty()) {
            return null;
        }
        for (Student student : students) {
            boolean isValidDate = student.getAttendanceOfStudent().containsKey(dateOfLab);
            if (isValidDate) {
                boolean isAttendanceOnLab = student.getAttendanceOfStudent().get(dateOfLab);
                if (isAttendanceOnLab) {
                    sortedStudents.add(student);
                }
            }
        }
        if(sortedStudents.isEmpty()) {
            return null;
        }
        return sortedStudents;
    }
}
