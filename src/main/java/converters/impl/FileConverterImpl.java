package converters.impl;

import controllers.impl.GroupsControllerImpl;
import converters.FileConverter;
import models.Group;
import models.Groups;
import models.Student;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>Класс конвертер</p>
 * <p>Класс, реализующий функционал по конвертации данных из файла в оперативную память и обратно - в файл</p>
 *
 * @author Kononov Pavel
 * @version 1.0
 */
public class FileConverterImpl implements FileConverter {

    private static final String isPassedLab = "сдал";
    private static final String isFailedLab = "не сдал";
    private static final String isNotAttendanceOnLab = "не был";
    private static final String isAttendanceOnLab = "был";


    /**
     * Метод, рекурсивно пробегающийся по папкам и файлам, создающий группы и возвращающий список всех групп
     *
     * @param folder -  папка с файлами
     * @return - созданные группы
     * @throws IOException
     * @throws ParseException
     * @throws InvalidFormatException
     */
    @Override
    public Groups processFilesFromFolderRecursive(File folder) throws IOException, ParseException, InvalidFormatException {
        Groups groups = new Groups();
        File[] folderEntries = folder.listFiles();
        if (folderEntries != null && folderEntries.length != 0) {
            for (File entry : folderEntries) {
                if (entry.isDirectory()) {
                    processFilesFromFolderRecursive(entry);
                    continue;
                }
                Group group = getGroupByFile(entry.getPath(), groups);
                if (group != null) {
                    groups.getGroups().put(group.getGroupId(), group);
                }
            }
            if (groups.getGroups().isEmpty()) {
                return null;
            }
            return groups;
        } else {
            return null;
        }
    }

    /**
     * Метод, создающий excel файл одной группы
     * Наполняет файл данными и сохраняет его по выбранному пути
     *
     * @param group              - группа
     * @param path               - путь для сохранения файла
     * @param isNeedToCreatePath - флаг, для правильного установления пути
     * @throws IOException    - error
     * @throws ParseException - error
     */
    @Override
    public void createFileFromGroup(Group group, String path, boolean isNeedToCreatePath) throws IOException, ParseException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet(group.getGroupId());
        int rowId = 0;
        int cellCount = 0;
        XSSFRow row = spreadsheet.createRow(rowId);
        XSSFCell cell;

        setFirstRow(row, group);
        spreadsheet.setColumnWidth(0, 8000);
        FileOutputStream out;
        if (isNeedToCreatePath) {
            out = new FileOutputStream(path + "/" + group.getGroupId() + ".xls");
        } else {
            out = new FileOutputStream(path);
        }
        for (Student student : group.getStudents()) {
            rowId++;
            row = spreadsheet.createRow(rowId);
            cell = row.createCell(cellCount);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(student.getLastName() + " " + student.getFirstName() + " " + student.getPatronymic());
            if (!student.getAttendanceOfStudent().isEmpty()) {
                for (Map.Entry<Date, Boolean> entry : student.getAttendanceOfStudent().entrySet()) {
                    cellCount++;
                    spreadsheet.setColumnWidth(cellCount, 3000);
                    cell = row.createCell(cellCount);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(getAttendanceForTableByStudentAttendance(entry.getValue()));
                }
            }
            cellCount++;
            if (!student.getPassedOrFailedLab().isEmpty()) {
                for (Map.Entry<String, Boolean> entry : student.getPassedOrFailedLab().entrySet()) {
                    cellCount++;
                    spreadsheet.setColumnWidth(cellCount, 7000);
                    cell = row.createCell(cellCount);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(getPassedOrFailedLabForTableByStudents(entry.getValue()));
                }
            }
            cellCount = 0;
        }
        workbook.write(out);
        out.close();
    }

    /**
     * Метод, созданный для заполнения первой строчки excel файла
     *
     * @param row   - колонка
     * @param group - группа
     */
    @Override
    public void setFirstRow(XSSFRow row, Group group) {
        XSSFCell cell = row.createCell(0);
        int currentlyCell = 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        cell.setCellType(CellType.STRING);
        cell.setCellValue("ФИО");
        if (group != null && !group.getStudents().isEmpty() && !group.getStudents().get(0).getAttendanceOfStudent().isEmpty()) {
            int count = group.getStudents().get(0).getAttendanceOfStudent().size();
            List<Date> listDate = convertSetToList(group.getStudents().get(0).getAttendanceOfStudent().keySet());
            List<Date> sorted = sortListOfDates(listDate);
            for (int i = 1; i <= count; i++) {
                cell = row.createCell(i);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(dateFormat.format(sorted.get(i - 1)));
                currentlyCell = count;
            }
        }
        if (group != null && !group.getStudents().isEmpty() && !group.getStudents().get(0).getPassedOrFailedLab().isEmpty()) {
            int count = group.getStudents().get(0).getPassedOrFailedLab().size();
            List<String> listOfLabs = new ArrayList<>(group.getStudents().get(0).getPassedOrFailedLab().keySet());
            currentlyCell++;
            for (int i = 1; i <= count; i++) {
                cell = row.createCell(currentlyCell + i);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(listOfLabs.get(i - 1));
            }
        }
    }

    /**
     * Конвертер набора дат в коллекцию дат
     *
     * @param dates - Набор дат
     * @return коллекцию дат
     */
    @Override
    public List<Date> convertSetToList(Set<Date> dates) {
        return new ArrayList<>(dates);
    }

    /**
     * Сортировка списка дат
     *
     * @param dates - список дат
     * @return отсортированный список дат
     */
    @Override
    public List<Date> sortListOfDates(List<Date> dates) {
        return dates.stream()
                .sorted(Comparator.comparingLong(Date::getTime))
                .collect(Collectors.toList());
    }

    /**
     * Метод, который достает данные из файла и создает группу студентов
     *
     * @param path   - путь к файлу
     * @param groups - группы
     * @return группу
     * @throws IOException            - error
     * @throws ParseException         - error
     * @throws InvalidFormatException - error
     */
    @Override
    public Group getGroupByFile(String path, Groups groups) throws IOException, ParseException, InvalidFormatException {
        try {
            GroupsControllerImpl groupsController = new GroupsControllerImpl();
            StudentConverterImpl studentConverter = new StudentConverterImpl();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            FileInputStream inputStream = new FileInputStream(path);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return null;
            }
            int row = 0;
            int cell = 0;
            int dateRow = 0;
            int labsRow = 0;
            String[] filePath = path.replace("\\", "/").split("/");
            String groupId = filePath[filePath.length - 1].replace(".xls", "");
            List<Student> studentList = new ArrayList<>();
            Group group = groupsController.createGroup(groupId, groups, studentList);
            if (group == null) {
                return null;
            }
            if (!sheet.getRow(0).getCell(0).getStringCellValue().equals("ФИО")) {
                return null;
            }
            while (true) {
                row += 1;
                if (sheet.getRow(row) == null || sheet.getRow(row).getCell(cell) == null || sheet.getRow(row).getCell(cell).getStringCellValue() == null) {
                    break;
                }
                String[] fullName = convert(sheet.getRow(row).getCell(cell).getStringCellValue());
                if (fullName.length == 0) {
                    break;
                }
                Map<Date, Boolean> attendanceOfStudent = new HashMap<>();
                Map<String, Boolean> passedOrFailedLabs = new HashMap<>();
                cell += 1;
                int countOfAttendance = 0;
                while (true) {
                    XSSFCell xssfCell = sheet.getRow(dateRow).getCell(cell);
                    if (xssfCell == null || xssfCell.getStringCellValue() == null || xssfCell.getStringCellValue().length() == 0) {
                        break;
                    }
                    attendanceOfStudent.put(dateFormat.parse(sheet.getRow(dateRow).getCell(cell).getStringCellValue()), convertAttendance(sheet.getRow(row).getCell(cell).getStringCellValue()));
                    cell++;
                }
                cell++;
                while (true) {
                    XSSFCell xssfCell = sheet.getRow(labsRow).getCell(cell);
                    if (xssfCell == null || xssfCell.getStringCellValue() == null || xssfCell.getStringCellValue().length() == 0) {
                        break;
                    }
                    passedOrFailedLabs.put(sheet.getRow(labsRow).getCell(cell).getStringCellValue(), convertPassedOrFailedLab(sheet.getRow(row).getCell(cell).getStringCellValue()));
                    cell++;
                }
                for (Map.Entry<Date, Boolean> entry : attendanceOfStudent.entrySet()) {
                    if (entry.getValue()) {
                        countOfAttendance += 1;
                    }
                }
                Student student = Student
                        .builder()
                        .studentId(studentConverter.convert(groupId, studentList))
                        .groupId(groupId)
                        .attendanceOfStudent(attendanceOfStudent)
                        .passedOrFailedLab(passedOrFailedLabs)
                        .countOfAttendance(countOfAttendance)
                        .lastName(fullName[0])
                        .firstName(fullName[1])
                        .patronymic(fullName[2])
                        .build();
                studentList.add(student);
                cell = 0;
            }
            group.setStudents(studentList);
            inputStream.close();
            workbook.close();
            return group;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Метод, для конвертации строки в логику
     *
     * @param attendance - посещение
     * @return - посещение в логическом формате
     */
    @Override
    public boolean convertAttendance(String attendance) {
        return isAttendanceOnLab.equals(attendance);
    }

    /**
     * Конвертация в строку логического формата посещения
     *
     * @param attendanceOfStudent - посещение студента
     * @return - посещение в строковом формате
     */
    @Override
    public String getAttendanceForTableByStudentAttendance(Boolean attendanceOfStudent) {
        if (!attendanceOfStudent) {
            return isNotAttendanceOnLab;
        }
        return isAttendanceOnLab;
    }

    /**
     * Метод, конвертирующий boolean to string
     *
     * @param passedOrFailedLab - сдача лабораторной
     * @return отметка о защите лабораторной в строковом формате
     */
    @Override
    public String getPassedOrFailedLabForTableByStudents(Boolean passedOrFailedLab) {
        if (!passedOrFailedLab) {
            return isFailedLab;
        }
        return isPassedLab;
    }

    /**
     * Convert string to boolean
     *
     * @param passedOrFailedLab - сдача лабы
     * @return отметка о защите лабораторной в логическом формате
     */
    @Override
    public boolean convertPassedOrFailedLab(String passedOrFailedLab) {
        return isPassedLab.equals(passedOrFailedLab);
    }

    /**
     * Получение фио из строки
     *
     * @param fullName - ФИО
     * @return - ФИО[]
     */
    @Override
    public String[] convert(String fullName) {
        if (null == fullName || fullName.equals("")) return new String[]{};
        return fullName.split(" ");
    }
}
