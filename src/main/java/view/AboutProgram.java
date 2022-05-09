package view;

import lombok.SneakyThrows;
import view.viewConverters.impl.ImageConverterImpl;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 *<p>Класс представления</p>
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
        jpnlInfo.setBounds(0,150,sizeX,450);


        jpnlAboutProgram.setBackground(Color.getHSBColor(2, 250, 7));

        jlblTitle = new JLabel("Успеваемость на лабораторных занятиях");
        jlblTitle.setFont(getCourierFont(30, Font.CENTER_BASELINE));
        jlblTitle.setBounds(200,35, sizeX,40);

        jlblPossibilityOfProgram = new JLabel("Программа позволяет");
        jlblPossibilityOfProgram.setFont(getCourierFont(24, Font.BOLD));
        jlblPossibilityOfProgram.setBounds(350, 110, sizeX, 30);

        //------------------Информация о программе -------------------//

        jlblStudentsImage = new JLabel(imageConverter.scaleImage("images/students.png", 80,80));
        jlblStudentsImage.setBounds(40,20, 80,80);

        jlblExcelImage = new JLabel(imageConverter.scaleImage("images/excel.png",80,80));
        jlblExcelImage.setBounds(40,110, 80,80);

        jlblTableImage = new JLabel(imageConverter.scaleImage("images/table.png",80,80));
        jlblTableImage.setBounds(40,200, 80,80);

        jlblCheckedUnCheckedImage = new JLabel(imageConverter.scaleImage("images/checkedUnChecked.png",80,80));
        jlblCheckedUnCheckedImage.setBounds(40,290, 80,80);

        jlblSortImage = new JLabel(imageConverter.scaleImage("images/sort.png",80,80));
        jlblSortImage.setBounds(40,370, 80,80);

        jlblStudents = new JLabel("Получать списки студентов из файла и работать с ними.");
        jlblStudents.setFont(getCourierFont(24,Font.PLAIN));
        jlblStudents.setBounds(150,50, sizeX, 30);

        jlblExcel = new JLabel("Сохранять результат лабораторных работ в excel файлы.");
        jlblExcel.setFont(getCourierFont(24,Font.PLAIN));
        jlblExcel.setBounds(150,140, sizeX, 30);

        jlblTable = new JLabel("Работать в привычной форме - в виде таблицы данных.");
        jlblTable.setFont(getCourierFont(24,Font.PLAIN));
        jlblTable.setBounds(150,230, sizeX, 30);

        jlblCheckedUnChecked = new JLabel("Отмечать сдачу лабораторных работ и присутствие студентов на них.");
        jlblCheckedUnChecked.setFont(getCourierFont(24,Font.PLAIN));
        jlblCheckedUnChecked.setBounds(150,320, sizeX, 30);

        jlblSort = new JLabel("Сортировать студентов по посещениям, кол-ву сданных лабораторных");
        jlblSort.setFont(getCourierFont(24,Font.PLAIN));
        jlblSort.setBounds(150,390, sizeX, 30);





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
        jbtBackToMainWindow.setSize(400,60);
        jbtBackToMainWindow.setFocusPainted(false);
        jbtBackToMainWindow.setFont(getCourierFont(30,Font.PLAIN));
        jbtBackToMainWindow.setBounds(560, 650, 400, 60);
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
        setSize(sizeX, sizeY);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
    }
}
