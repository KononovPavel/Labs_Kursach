package view;

import lombok.SneakyThrows;
import view.viewConverters.impl.ImageConverterImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * <p>Класс представления</p>
 * <p>Класс, который создает стартовое окно</p>
 *
 * @author Kononov Pavel
 * @version 1.0
 */
public class StartWindow extends JFrame {

    private static final String TITLE_OF_WINDOW = "Курсовой проект";
    private JLabel jlblStudentName;
    private JLabel jlblStudent;
    private JLabel jlblUniversity;
    private JLabel jlblFaculty;
    private JLabel jlblDepartment;
    private JLabel jlblTopic;
    private JLabel jlblTitle;
    private JLabel jlblDiscipline;
    private JLabel jlblTeacher;
    private JLabel jlblTeacherName;
    private JLabel jlblCity;
    private JButton jbtStartOfWork;
    private JButton jbtExitProgram;
    private ImageIcon jImgImage;
    private JLabel jlblImage;
    private ImageIcon jImgTimer;
    private JLabel jlblTimerImage;
    private JLabel jlblTimer;
    private int count = 60;
    private final ImageConverterImpl imageConverter = new ImageConverterImpl();


    private Font getCourierFont(int fontSize, int fontType) {
        return new Font("Courier", fontType, fontSize);
    }

    /**
     * Конструктор, который создает стартовое окно и заполняет его данными
     */
    public StartWindow() {
        super(TITLE_OF_WINDOW);
        jImgImage = imageConverter.scaleImage("images/javalab.png", 400, 300);
        jImgTimer = imageConverter.scaleImage("images/timer.png", 40, 40);

        JPanel jPanel = new JPanel(null);
        JPanel jpnlTimer = new JPanel(null);
        /*----------Работа с таймером-----------*/

        jPanel.setBackground(Color.getHSBColor(2, 250, 7));
        jpnlTimer.setBackground(Color.getHSBColor(2, 250, 7));

        jlblTimerImage = new JLabel(jImgTimer);
        jlblTimerImage.setBounds(15, 15, 40, 40);

        jlblTimer = new JLabel(String.valueOf(count));
        jlblTimer.setFont(getCourierFont(36, Font.PLAIN));
        jlblTimer.setBounds(80, 15, 40, 40);

        jpnlTimer.setBounds(650, 530, 150, 70);
        jpnlTimer.setBackground(Color.PINK);
        jpnlTimer.add(jlblTimerImage);
        jpnlTimer.add(jlblTimer);

        Timer timer = new Timer(1000, e -> {
            if (count == 0) {
                System.exit(1);
            }
            jlblTimer.setText(String.valueOf(--count));
        });
        timer.start();



        /*------------------------------------*/
        jlblUniversity = new JLabel("БЕЛОРУССКИЙ НАЦИОНАЛЬНЫЙ ТЕХНИЧЕСКИЙ УНИВЕРСИТЕТ");
        jlblUniversity.setFont(getCourierFont(16, Font.BOLD));
        jlblUniversity.setBounds(220, 10, 600, 20);


        jlblFaculty = new JLabel("Факультет информационных технологий и робототехники");
        jlblFaculty.setFont(getCourierFont(14, Font.PLAIN));
        jlblFaculty.setBounds(300, 40, 600, 20);

        jlblDepartment = new JLabel("Кафедра программного обеспечения инфоррмационых систем и технологий");
        jlblDepartment.setFont(getCourierFont(14, Font.PLAIN));
        jlblDepartment.setBounds(250, 70, 600, 20);


        jlblTitle = new JLabel("Курсовая работа");
        jlblTitle.setFont(getCourierFont(24, Font.BOLD));
        jlblTitle.setBounds(400, 150, 400, 40);


        jlblDiscipline = new JLabel("По дисциплине \"Программирование на языке Java\"");
        jlblDiscipline.setFont(getCourierFont(16, Font.BOLD));
        jlblDiscipline.setBounds(290, 190, 500, 20);

        jlblTopic = new JLabel("Успеваемость на лабораторных занятиях");
        jlblTopic.setFont(getCourierFont(34, Font.BOLD));
        jlblTopic.setBounds(150, 240, 800, 40);

        jlblStudent = new JLabel("Выполнил: Студент группы 10702419");
        jlblStudent.setFont(getCourierFont(20, Font.BOLD));
        jlblStudent.setBounds(540, 350, 512, 30);

        jlblStudentName = new JLabel("Кононов Павел Валерьевич");
        jlblStudentName.setFont(getCourierFont(20, Font.ITALIC));
        jlblStudentName.setBounds(540, 380, 512, 30);

        jlblTeacher = new JLabel("Преподаватель: к.ф.-м.н, доц.");
        jlblTeacher.setFont(getCourierFont(20, Font.BOLD));
        jlblTeacher.setBounds(540, 440, 512, 30);

        jlblTeacherName = new JLabel("Сидорик Валерий Владимирович");
        jlblTeacherName.setFont(getCourierFont(20, Font.ITALIC));
        jlblTeacherName.setBounds(540, 470, 512, 30);


        jlblImage = new JLabel(jImgImage);
        jlblImage.setBounds(60, 330, 400, 300);

        jlblCity = new JLabel("Минск 2022");
        jlblCity.setFont(getCourierFont(20, Font.BOLD));
        jlblCity.setBounds(460, 580, 200, 30);


        jbtStartOfWork = new JButton("Далее");
        jbtStartOfWork.setFocusPainted(false);
        jbtStartOfWork.setFont(getCourierFont(24, Font.PLAIN));
        jbtStartOfWork.setSize(400, 60);
        jbtStartOfWork.setBounds(60, 650, 400, 60);
        jbtStartOfWork.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                setVisible(false);
                new MainWindow();
            }
        });

        jbtExitProgram = new JButton("Выйти");
        jbtExitProgram.setFocusPainted(false);
        jbtExitProgram.setFont(getCourierFont(24, Font.PLAIN));
        jbtExitProgram.setBackground(Color.getHSBColor(250, 2, 101));
        jbtExitProgram.setSize(400, 60);
        jbtExitProgram.setBounds(560, 650, 400, 60);
        jbtExitProgram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });

        jPanel.add(jlblUniversity);
        jPanel.add(jlblFaculty);
        jPanel.add(jlblDepartment);
        jPanel.add(jlblTitle);
        jPanel.add(jlblDiscipline);
        jPanel.add(jlblTopic);
        jPanel.add(jlblStudent);
        jPanel.add(jlblStudentName);
        jPanel.add(jlblTeacher);
        jPanel.add(jlblTeacherName);
        jPanel.add(jlblCity);
        jPanel.add(jbtStartOfWork);
        jPanel.add(jbtExitProgram);
        jPanel.add(jlblImage);
        jPanel.add(jpnlTimer);

        add(jPanel);
        setSize(1024, 768);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    public static void main(String[] args) {
        new StartWindow();
    }
}
