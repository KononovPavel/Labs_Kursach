package view;

import lombok.SneakyThrows;
import view.viewConverters.impl.ImageConverterImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * <p>Класс представления</p>
 * <p>Класс хелпер - помощь пользователю для работы с программой</p>
 *
 * @author Kononov Pavel
 * @version 1.0
 */
public class HelperWindow extends JFrame {

    private ImageConverterImpl imageConverter = new ImageConverterImpl();
    private JPanel jpnlHelper = new JPanel(null);
    private JLabel jlblLeftArrowImg = new JLabel(imageConverter.scaleImage("images/strelka_to_change_plane_left.png", 30, 30));
    private JLabel jlblRightArrowImg = new JLabel(imageConverter.scaleImage("images/strelka_to_change_plane.png", 30, 30));
    JLabel jlblAboutFirstPageImg = new JLabel();
    private final int leftArrowPositionX = 50;
    private final int leftArrowPositionY = 220;
    private final int rightArrowPositionX = 780;
    private final int rightArrowPositionY = 220;


    private Font getCourierFont(int fontSize, int fontType) {
        return new Font("Arial", fontType, fontSize);
    }

    /**
     * Конструктор, создающий окно хелпера и заполняющий его данными
     */
    public HelperWindow(){
        super("Помощь");
        jpnlHelper.setBackground(Color.getHSBColor(2, 250, 7));
        setFirstStepHelper();
        add(jpnlHelper);
        setSize(900, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setFirstStepHelper(){
        jpnlHelper.removeAll();
        jlblRightArrowImg.setBounds(rightArrowPositionX, rightArrowPositionY, 32, 32);
        jlblLeftArrowImg.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jlblRightArrowImg.addMouseListener(new MouseAdapter() {
            @SneakyThrows
            @Override
            public void mouseClicked(MouseEvent e) {
                setSecondStepHelper();
            }
        });

        jlblAboutFirstPageImg.setIcon(imageConverter.scaleImage("helperImages/aboutFirstPage.png", 600, 300));
        JLabel jlblFirstNote = new JLabel("1. Можете выбрать папку для файлов, чтобы загрузить их");
        JLabel jlblSecondNote = new JLabel("2. Можете выбрать 1 файл, чтобы начать работать с программой");
        JLabel jlblThirdNote = new JLabel("3. Нажмите сюда, чтобы получить помощь");

        jlblFirstNote.setFont(getCourierFont(16,Font.PLAIN));
        jlblFirstNote.setBounds(130, 370, 500, 16);

        jlblSecondNote.setFont(getCourierFont(16,Font.PLAIN));
        jlblSecondNote.setBounds(130, 400, 500, 16);


        jlblThirdNote.setFont(getCourierFont(16,Font.PLAIN));
        jlblThirdNote.setBounds(130, 430, 500, 16);


        jlblAboutFirstPageImg.setBounds(130, 50, 600, 300);
        jpnlHelper.add(jlblRightArrowImg);
        jpnlHelper.add(jlblAboutFirstPageImg);
        jpnlHelper.add(jlblFirstNote);
        jpnlHelper.add(jlblSecondNote);
        jpnlHelper.add(jlblThirdNote);
        jpnlHelper.repaint();
    }

    private void setSecondStepHelper(){
        jpnlHelper.removeAll();
        jlblRightArrowImg.setBounds(rightArrowPositionX, rightArrowPositionY, 32, 32);
        jlblRightArrowImg.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jlblRightArrowImg.addMouseListener(new MouseAdapter() {
            @SneakyThrows
            @Override
            public void mouseClicked(MouseEvent e) {
                setThirdStepHelper();
            }
        });

        jlblLeftArrowImg.setBounds(leftArrowPositionX, leftArrowPositionY, 32, 32);
        jlblLeftArrowImg.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jlblLeftArrowImg.addMouseListener(new MouseAdapter() {
            @SneakyThrows
            @Override
            public void mouseClicked(MouseEvent e) {
                setFirstStepHelper();
            }
        });

        jlblAboutFirstPageImg.setIcon(imageConverter.scaleImage("helperImages/excel_1.png", 650, 270));
        JLabel jlblFirstNote = new JLabel("Пожалуйста, убедитесь что у вас правильно заполненные поля");
        JLabel jlblSecondNote = new JLabel("Обратите внимание, что не должно быть ничего лишнего");
        JLabel jlblThirdNote = new JLabel("Не нужно придумывать что новое");


        jlblFirstNote.setFont(getCourierFont(16,Font.PLAIN));
        jlblFirstNote.setBounds(200, 340, 800, 16);

        jlblSecondNote.setFont(getCourierFont(16,Font.PLAIN));
        jlblSecondNote.setBounds(200, 370, 800, 16);


        jlblThirdNote.setFont(getCourierFont(16,Font.PLAIN));
        jlblThirdNote.setBounds(200, 400, 800, 16);


        jlblAboutFirstPageImg.setBounds(100, 40, 650, 270);

        jpnlHelper.add(jlblFirstNote);
        jpnlHelper.add(jlblSecondNote);
        jpnlHelper.add(jlblThirdNote);
        jpnlHelper.add(jlblRightArrowImg);
        jpnlHelper.add(jlblLeftArrowImg);
        jpnlHelper.add(jlblAboutFirstPageImg);
        jpnlHelper.repaint();
    }


    private void setThirdStepHelper(){
        jpnlHelper.removeAll();
        jlblLeftArrowImg.setBounds(leftArrowPositionX, leftArrowPositionY, 40, 40);
        jlblLeftArrowImg.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jlblLeftArrowImg.addMouseListener(new MouseAdapter() {
            @SneakyThrows
            @Override
            public void mouseClicked(MouseEvent e) {
                setSecondStepHelper();
            }
        });

        jlblAboutFirstPageImg.setIcon(imageConverter.scaleImage("helperImages/stringFormat_excel.png", 550, 300));
        JLabel jlblFirstNote = new JLabel("1. Убедитесь, что у вас тип ячеек выставлен правильно");
        JLabel jlblSecondNote = new JLabel("2. Тип всех ячеек должен быть 'TEXT'");
        JLabel jlblThirdNote = new JLabel("3. Не заполняйте даты, если не знаете как, загрузите лучше файл в программу, так будет корректнее");


        jlblAboutFirstPageImg.setBounds(200, 40, 550,300);
        jlblFirstNote.setFont(getCourierFont(16, Font.PLAIN));
        jlblFirstNote.setBounds(100, 370, 800, 16);

        jlblSecondNote.setFont(getCourierFont(16,Font.PLAIN));
        jlblSecondNote.setBounds(100, 400, 800, 16);


        jlblThirdNote.setFont(getCourierFont(16,Font.PLAIN));
        jlblThirdNote.setBounds(100, 430, 800, 16);

        jpnlHelper.add(jlblLeftArrowImg);
        jpnlHelper.add(jlblAboutFirstPageImg);
        jpnlHelper.add(jlblFirstNote);
        jpnlHelper.add(jlblSecondNote);
        jpnlHelper.add(jlblThirdNote);
        jpnlHelper.repaint();
    }
}
