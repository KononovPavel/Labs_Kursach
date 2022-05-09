package controllers;

import models.Student;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface StudentController {

    public void addDateToSubject(Date dateOfLab,  List<Student> studentList) throws ParseException;

    public void addAttendanceForStudent(Student student, boolean isAttendanceOfLab, Date dateOfLab) throws ParseException;

    public void addPassesOrFailedLabForStudent(Student student, boolean isPassedOrFailedOfLab, String lab);

    public void addLabForStudent(List<Student> studentList);

    public void deleteLabForStudent(List<Student> studentList, String lab);

    public void deleteDateToSubject(Date dateOfLab, List<Student> studentList) throws ParseException;
}
