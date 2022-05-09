package view;

import lombok.SneakyThrows;
import view.viewConverters.impl.ImageConverterImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>Класс представления</p>
 * <p>Класс, создающий окно "Об авторе"</p>
 *
 * @author Kononov Pavel
 * @version 1.0
 */
public class AuthorWindow extends JFrame {

    private static final int sizeX = 500;
    private static final int sizeY = 768;
    private static final String WINDOW_TITLE = "Об авторе";
    private JLabel jlblAuthorPhoto;
    private JLabel jlblAuthorName;
    private JLabel jlblAuthorGroup;
    private JButton jbtBack;
    private JLabel jlblEmail;
    private JLabel jlblEmailImage;
    private JLabel jlblPhoneImage;
    private JLabel jlblPhone;
    private Icon jbtBackImageTo;


    private Font getCourierFont(int fontSize, int fontType) {
        return new Font("Arial", fontType, fontSize);
    }

    /**
     * Конструктор, в котором создается само окно и заполняется контентом
     */
    public AuthorWindow(){
        super(WINDOW_TITLE);
        ImageConverterImpl imageConverter = new ImageConverterImpl();
        JPanel jPanel = new JPanel(null);
        JPanel jpnlContactInfo = new JPanel(null);

        jbtBackImageTo = imageConverter.scaleImage("images/back.png", 30, 30);

        jlblAuthorPhoto = new JLabel(imageConverter.scaleImage("images/author.jpg", 350, 350));
        jlblAuthorPhoto.setBounds(75,50,350,350);

        jlblAuthorName = new JLabel("Кононов Павел Валерьевич");
        jlblAuthorName.setFont(getCourierFont(24,Font.PLAIN));
        jlblAuthorName.setBounds(75, 450,400,50);

        jlblAuthorGroup = new JLabel("Студент группы 10702419");
        jlblAuthorGroup.setFont(getCourierFont(24, Font.PLAIN));
        jlblAuthorGroup.setBounds(75, 480,400,50);


        jpnlContactInfo.setSize(600,200);
        jpnlContactInfo.setBackground(Color.getHSBColor(2, 250, 7));
        jpnlContactInfo.setBounds(75, 550, 400, 100);

        jlblEmailImage = new JLabel(imageConverter.scaleImage("images/email.png", 30, 30));
        jlblEmailImage.setBounds(0,0,30,30);


        jlblEmail = new JLabel("pavel.konanau@netcracker.com");
        jlblEmail.setFont(getCourierFont(24, Font.PLAIN));
        jlblEmail.setBounds(50,0,500,30);

        jlblPhoneImage = new JLabel(imageConverter.scaleImage("images/phone.png", 30, 30));
        jlblPhoneImage.setBounds(0, 40, 30,30);

        jlblPhone = new JLabel("+375(29)181-16-55");
        jlblPhone.setFont(getCourierFont(24,Font.PLAIN));
        jlblPhone.setBounds(50,40,400,30);



        jbtBack = new JButton("ВЕРНУТЬСЯ ОБРАТНО");
        jbtBack.setFocusPainted(false);
        jbtBack.setSize(400,60);
        jbtBack.setFont(getCourierFont(24, Font.PLAIN));
        jbtBack.setBackground(Color.getHSBColor(250, 2, 101));
        jbtBack.setBounds(50, sizeY - 60 - 50, 400, 60);
        jbtBack.setIcon(jbtBackImageTo);
        jbtBack.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new MainWindow();
            }
        });



        jpnlContactInfo.add(jlblEmail);
        jpnlContactInfo.add(jlblPhone);
        jpnlContactInfo.add(jlblPhoneImage);
        jpnlContactInfo.add(jlblEmailImage);


        jPanel.add(jlblAuthorPhoto);
        jPanel.add(jlblAuthorName);
        jPanel.add(jlblAuthorGroup);
        jPanel.add(jpnlContactInfo);
        jPanel.add(jbtBack);
        jPanel.setBackground(Color.getHSBColor(2, 250, 7));

        add(jPanel);
        setSize(sizeX, sizeY);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }
}
