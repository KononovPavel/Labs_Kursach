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
        return new Font("Arial", fontType, fontSize);
    }

    /**
     * Конструктор, который создает стартовое окно и заполняет его данными
     */
    public StartWindow() {
        super(TITLE_OF_WINDOW);
        jImgImage = imageConverter.scaleImage("images/javalab.png", 304, 224);
        jImgTimer = imageConverter.scaleImage("images/timer.png", 32, 32);

        JPanel jPanel = new JPanel(null);
        JPanel jpnlTimer = new JPanel(null);

        jPanel.setBackground(Color.getHSBColor(2, 250, 7));
        jpnlTimer.setBackground(Color.getHSBColor(2, 250, 7));

        jlblTimerImage = new JLabel(jImgTimer);
        jlblTimerImage.setBounds(12, 12, 32, 32);

        jlblTimer = new JLabel(String.valueOf(count));
        jlblTimer.setFont(getCourierFont(28, Font.PLAIN));
        jlblTimer.setBounds(60, 12, 32, 32);

        jpnlTimer.setBounds(520, 424, 120, 56);
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
        jlblUniversity.setFont(getCourierFont(14, Font.BOLD));
        jlblUniversity.setBounds(180, 8, 480, 16);


        jlblFaculty = new JLabel("Факультет информационных технологий и робототехники");
        jlblFaculty.setFont(getCourierFont(14, Font.PLAIN));
        jlblFaculty.setBounds(260, 32, 480, 16);

        jlblDepartment = new JLabel("Кафедра программного обеспечения инфоррмационых систем и технологий");
        jlblDepartment.setFont(getCourierFont(14, Font.PLAIN));
        jlblDepartment.setBounds(220, 56, 480, 16);


        jlblTitle = new JLabel("Курсовая работа");
        jlblTitle.setFont(getCourierFont(20, Font.BOLD));
        jlblTitle.setBounds(310, 112, 300, 30);


        jlblDiscipline = new JLabel("По дисциплине \"Программирование на языке Java\"");
        jlblDiscipline.setFont(getCourierFont(12, Font.BOLD));
        jlblDiscipline.setBounds(240, 152, 416, 16);

        jlblTopic = new JLabel("Успеваемость на лабораторных занятиях");
        jlblTopic.setFont(getCourierFont(20, Font.BOLD));
        jlblTopic.setBounds(200, 192, 600, 32);

        jlblStudent = new JLabel("Выполнил: Студент группы 10702419");
        jlblStudent.setFont(getCourierFont(16, Font.BOLD));
        jlblStudent.setBounds(430, 280, 410, 24);

        jlblStudentName = new JLabel("Кононов Павел Валерьевич");
        jlblStudentName.setFont(getCourierFont(16, Font.ITALIC));
        jlblStudentName.setBounds(430, 304, 410, 24);

        jlblTeacher = new JLabel("Преподаватель: к.ф.-м.н, доц.");
        jlblTeacher.setFont(getCourierFont(16, Font.BOLD));
        jlblTeacher.setBounds(430, 352, 410, 24);

        jlblTeacherName = new JLabel("Сидорик Валерий Владимирович");
        jlblTeacherName.setFont(getCourierFont(16, Font.ITALIC));
        jlblTeacherName.setBounds(430, 376, 410, 24);


        jlblImage = new JLabel(jImgImage);
        jlblImage.setBounds(48, 264,300, 240);

        jlblCity = new JLabel("Минск 2022");
        jlblCity.setFont(getCourierFont(16, Font.BOLD));
        jlblCity.setBounds(368, 464, 160, 24);


        jbtStartOfWork = new JButton("Далее");
        jbtStartOfWork.setFocusPainted(false);
        jbtStartOfWork.setFont(getCourierFont(20, Font.PLAIN));
        jbtStartOfWork.setSize(300, 48);
        jbtStartOfWork.setBounds(48, 520, 300, 48);
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
        jbtExitProgram.setFont(getCourierFont(20, Font.PLAIN));
        jbtExitProgram.setBackground(Color.getHSBColor(250, 2, 101));
        jbtExitProgram.setSize(300, 48);
        jbtExitProgram.setBounds(448, 520, 300, 48);
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
        setSize(820, 615);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    public static void main(String[] args) {
        new StartWindow();
    }
}
