package converters;

import models.Group;
import models.Groups;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface FileConverter {

    public Groups processFilesFromFolderRecursive(File folder) throws IOException, ParseException, InvalidFormatException;

    public void createFileFromGroup(Group group, String path, boolean isNeedToCreatePath) throws IOException, ParseException;

    public Group getGroupByFile(String path, Groups groups) throws IOException, ParseException, InvalidFormatException;

    public boolean convertAttendance(String attendance);

    public String[] convert(String fullName);

    public void setFirstRow(XSSFRow row, Group group) throws ParseException;

    public List<Date> convertSetToList(Set<Date> dates);

    public List<Date> sortListOfDates(List<Date> dates);

    public String getAttendanceForTableByStudentAttendance (Boolean attendanceOfStudent);

    public String getPassedOrFailedLabForTableByStudents(Boolean passedOrFailedLab);

    public boolean convertPassedOrFailedLab(String passedOrFailedLab);


}
