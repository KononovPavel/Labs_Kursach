package controllers.impl;

import controllers.StudentController;
import models.Student;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>Класс контролер</p>
 * <p>Класс, реализующий работу с самим студентом</p>
 * Реализованные методы позволяют добавить/удалить посещение студенту, обозначить сдачу лабораторной,
 * Добавить лабораторную работу всем студентам группы
 *
 * @author Kononov Pavel
 * @version 1.0
 */
public class StudentControllerImpl implements StudentController {

    private String datePattern = "dd/MM/yyyy";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

    /**
     * Метод, который добавляет всем студентам группы новое занятие, и автоматически
     * проставляет им отсутсвие
     *
     * @param dateOfLab   - дата занятия
     * @param studentList - список студентов
     * @throws ParseException - error
     */
    @Override
    public void addDateToSubject(Date dateOfLab, List<Student> studentList) throws ParseException {
        for (Student student : studentList) {
            student.getAttendanceOfStudent().put(dateFormat.parse(dateFormat.format(dateOfLab)), false);
        }
    }

    /**
     * Метод, который удаляет у всех студентов группы дату занятия, при этом
     * валидируя по полям студента, чтобы правильно отобразить отсутствие
     *
     * @param dateOfLab   - дата занятия
     * @param studentList - список студентов
     * @throws ParseException - error
     */
    @Override
    public void deleteDateToSubject(Date dateOfLab, List<Student> studentList) throws ParseException {
        for (Student student : studentList) {
            if (student.getAttendanceOfStudent().get(dateFormat.parse(dateFormat.format(dateOfLab)))) {
                student.setCountOfAttendance(student.getCountOfAttendance() - 1);
            }
            student.getAttendanceOfStudent().remove(dateFormat.parse(dateFormat.format(dateOfLab)));
            if (student.getAttendanceOfStudent().isEmpty()) {
                student.setCountOfAttendance(0);
            }
        }
    }


    /**
     * Метод добавления / удаления посещения студента по определенной дате
     *
     * @param student           - студент
     * @param isAttendanceOfLab - был ли на занятии
     * @param dateOfLab         - дата занятия
     * @throws ParseException - error
     */
    @Override
    public void addAttendanceForStudent(Student student, boolean isAttendanceOfLab, Date dateOfLab) throws ParseException {
        if (isAttendanceOfLab) {
            student.setCountOfAttendance(student.getCountOfAttendance() + 1);
            student.getAttendanceOfStudent().put(dateFormat.parse(dateFormat.format(dateOfLab)), true);
        } else {
            student.setCountOfAttendance(student.getCountOfAttendance() - 1);
            student.getAttendanceOfStudent().put(dateFormat.parse(dateFormat.format(dateOfLab)), false);
        }
    }

    /**
     * Метод добавления / удаления у студента сдачи лабораторной работы
     *
     * @param student               - студент
     * @param isPassedOrFailedOfLab - сдана ли лабораторная
     * @param lab                   - номер лабораторной работы(ключ)
     */
    @Override
    public void addPassesOrFailedLabForStudent(Student student, boolean isPassedOrFailedOfLab, String lab) {
        student.getPassedOrFailedLab().put(lab, isPassedOrFailedOfLab);
    }

    /**
     * Метод, добавляющий всей группе студентов лабораторную работу
     * автоматически проставляя ее не сдачу
     *
     * @param studentList - список студентов
     */
    @Override
    public void addLabForStudent(List<Student> studentList) {
        String labName = "Лабораторная работа №";
        Boolean isLabWasAdded = studentList.get(0).getPassedOrFailedLab().get(labName + String.valueOf(studentList.get(0).getPassedOrFailedLab().size() + 1));
        if (isLabWasAdded != null) {
            for (Student student : studentList) {
                student.getPassedOrFailedLab().put(labName + String.valueOf(student.getPassedOrFailedLab().size() + 2), false);
            }
        } else {
            for (Student student : studentList) {
                student.getPassedOrFailedLab().put(labName + String.valueOf(student.getPassedOrFailedLab().size() + 1), false);
            }
        }

    }

    /**
     * Метод, удаляющий лабораторную работу у всех студентов
     *
     * @param studentList - список студентов
     * @param lab         - ключ лабораторной работы
     */
    @Override
    public void deleteLabForStudent(List<Student> studentList, String lab) {
        for (Student student : studentList) {
            student.getPassedOrFailedLab().remove(lab);
        }
    }
}
