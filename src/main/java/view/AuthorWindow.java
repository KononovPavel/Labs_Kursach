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

        jbtBackImageTo = imageConverter.scaleImage("images/back.png", 24, 24);

        jlblAuthorPhoto = new JLabel(imageConverter.scaleImage("images/author.jpg", 280, 280));
        jlblAuthorPhoto.setBounds(60,40,280,280);

        jlblAuthorName = new JLabel("Кононов Павел Валерьевич");
        jlblAuthorName.setFont(getCourierFont(20,Font.PLAIN));
        jlblAuthorName.setBounds(60, 360,300,40);

        jlblAuthorGroup = new JLabel("Студент группы 10702419");
        jlblAuthorGroup.setFont(getCourierFont(20, Font.PLAIN));
        jlblAuthorGroup.setBounds(60, 384,400,40);


        jpnlContactInfo.setSize(480,160);
        jpnlContactInfo.setBackground(Color.getHSBColor(2, 250, 7));
        jpnlContactInfo.setBounds(50, 440, 400, 80);

        jlblEmailImage = new JLabel(imageConverter.scaleImage("images/email.png", 30, 30));
        jlblEmailImage.setBounds(0,0,24,24);


        jlblEmail = new JLabel("pavel.konanau@netcracker.com");
        jlblEmail.setFont(getCourierFont(20, Font.PLAIN));
        jlblEmail.setBounds(40,0,400,24);

        jlblPhoneImage = new JLabel(imageConverter.scaleImage("images/phone.png", 30, 30));
        jlblPhoneImage.setBounds(0, 30, 24,24);

        jlblPhone = new JLabel("+375(29)181-16-55");
        jlblPhone.setFont(getCourierFont(20,Font.PLAIN));
        jlblPhone.setBounds(40,30,300,24);



        jbtBack = new JButton("ВЕРНУТЬСЯ ОБРАТНО");
        jbtBack.setFocusPainted(false);
        jbtBack.setSize(300,48);
        jbtBack.setFont(getCourierFont(20, Font.PLAIN));
        jbtBack.setBackground(Color.getHSBColor(250, 2, 101));
        jbtBack.setBounds(40, 526, 300, 48);
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
        setSize(400, 630);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }
}
