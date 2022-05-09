package view;

import lombok.SneakyThrows;
import view.viewConverters.impl.ImageConverterImpl;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * <p>Класс представления</p>
 * <p>Класс, создающий окно "О программе", которое показывает пользователю,
 * что умеет программа</p>
 *
 * @author Kononov Pavel
 * @version 1.0
 */
public class AboutProgram extends JFrame {

    private JLabel jlblTitle;
    private JLabel jlblPossibilityOfProgram;

    private JButton jbtBackToMainWindow;
    private JPanel jpnlAboutProgram;
    private JPanel jpnlInfo;
    private JLabel jlblStudentsImage;
    private JLabel jlblExcelImage;
    private JLabel jlblTableImage;
    private JLabel jlblCheckedUnCheckedImage;
    private JLabel jlblSortImage;

    private JLabel jlblStudents;
    private JLabel jlblExcel;
    private JLabel jlblTable;
    private JLabel jlblCheckedUnChecked;
    private JLabel jlblSort;

    private static final int sizeX = 1024;
    private static final int sizeY = 768;

    private Font getCourierFont(int fontSize, int fontType) {
        return new Font("Arial", fontType, fontSize);
    }

    /**
     * Главный конструктор, который создает окно и наполняет его информацией
     */
    public AboutProgram(){
        super("О программе");
        ImageConverterImpl imageConverter = new ImageConverterImpl();
        jpnlAboutProgram = new JPanel(null);


        jpnlInfo = new JPanel(null);
        jpnlInfo.setBounds(0,120,820,360);


        jpnlAboutProgram.setBackground(Color.getHSBColor(2, 250, 7));

        jlblTitle = new JLabel("Успеваемость на лабораторных занятиях");
        jlblTitle.setFont(getCourierFont(24, Font.CENTER_BASELINE));
        jlblTitle.setBounds(160,28, 820,32);

        jlblPossibilityOfProgram = new JLabel("Программа позволяет");
        jlblPossibilityOfProgram.setFont(getCourierFont(20, Font.BOLD));
        jlblPossibilityOfProgram.setBounds(280, 88, 820, 24);

        //------------------Информация о программе -------------------//

        jlblStudentsImage = new JLabel(imageConverter.scaleImage("images/students.png", 64,64));
        jlblStudentsImage.setBounds(30,16, 64,64);

        jlblExcelImage = new JLabel(imageConverter.scaleImage("images/excel.png",64,64));
        jlblExcelImage.setBounds(30,88, 64,64);

        jlblTableImage = new JLabel(imageConverter.scaleImage("images/table.png",64,64));
        jlblTableImage.setBounds(30,160, 64,64);

        jlblCheckedUnCheckedImage = new JLabel(imageConverter.scaleImage("images/checkedUnChecked.png",64,64));
        jlblCheckedUnCheckedImage.setBounds(30,232, 64,64);

        jlblSortImage = new JLabel(imageConverter.scaleImage("images/sort.png",64,64));
        jlblSortImage.setBounds(30,296, 64,64);

        jlblStudents = new JLabel("Получать списки студентов из файла и работать с ними.");
        jlblStudents.setFont(getCourierFont(20,Font.PLAIN));
        jlblStudents.setBounds(120,40, 820, 24);

        jlblExcel = new JLabel("Сохранять результат лабораторных работ в excel файлы.");
        jlblExcel.setFont(getCourierFont(20,Font.PLAIN));
        jlblExcel.setBounds(120,110, 820, 24);

        jlblTable = new JLabel("Работать в привычной форме - в виде таблицы данных.");
        jlblTable.setFont(getCourierFont(20,Font.PLAIN));
        jlblTable.setBounds(120,184, 820, 24);

        jlblCheckedUnChecked = new JLabel("Отмечать сдачу лабораторных работ и присутствие студентов на них.");
        jlblCheckedUnChecked.setFont(getCourierFont(20,Font.PLAIN));
        jlblCheckedUnChecked.setBounds(120,256, 820, 24);

        jlblSort = new JLabel("Сортировать студентов по посещениям, кол-ву сданных лабораторных");
        jlblSort.setFont(getCourierFont(20,Font.PLAIN));
        jlblSort.setBounds(120,312, 820, 24);





        jpnlInfo.add(jlblStudentsImage);
        jpnlInfo.add(jlblExcelImage);
        jpnlInfo.add(jlblTableImage);
        jpnlInfo.add(jlblCheckedUnCheckedImage);
        jpnlInfo.add(jlblSortImage);
        jpnlInfo.add(jlblStudents);
        jpnlInfo.add(jlblExcel);
        jpnlInfo.add(jlblCheckedUnChecked);
        jpnlInfo.add(jlblSort);
        jpnlInfo.add(jlblTable);

        jbtBackToMainWindow = new JButton("Назад");
        jbtBackToMainWindow.setSize(300,48);
        jbtBackToMainWindow.setFocusPainted(false);
        jbtBackToMainWindow.setFont(getCourierFont(24,Font.PLAIN));
        jbtBackToMainWindow.setBounds(448, 520, 300, 48);
        jbtBackToMainWindow.setBackground(Color.getHSBColor(250, 2, 101));
        jbtBackToMainWindow.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new MainWindow();
            }
        });


        jpnlAboutProgram.add(jlblTitle);
        jpnlAboutProgram.add(jlblPossibilityOfProgram);
        jpnlAboutProgram.add(jbtBackToMainWindow);
        jpnlAboutProgram.add(jpnlInfo);


        add(jpnlAboutProgram);
        setSize(820, 615);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
    }
}
