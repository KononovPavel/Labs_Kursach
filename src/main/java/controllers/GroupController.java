package controllers;

import models.Student;

import java.util.Date;
import java.util.List;

public interface GroupController {

    //получить отсортированный список студентов группы по количеству посещений
    public List<Student> sortStudentsByAttendanceCount(List<Student> students, boolean isFlag);

    //получить отсортированный список студентов группы по фамилии
    public List<Student> sortStudentsByLastName(List<Student> students, boolean isFlag);

    //получить отсортированный список студентов группы по заданной дате
    public List<Student> sortStudentByDateOfLab(Date dateOfLab, List<Student> students);
}
