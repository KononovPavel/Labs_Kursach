package view.viewConverters.impl;

import view.viewConverters.imageConverter;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * <p>Класс Конвертер</p>
 * <p>Класс представляет собой конвертер, создан чтобы работать с обьектами ImageIcon</p>
 * @author Kononov Pavel
 * @version 1.0
 */
public class ImageConverterImpl implements imageConverter {

    /**
     * Метод, который масштабирует картинку и возвращает новую
     * @param path - путь к картинке
     * @param sizeX - размер по ширине
     * @param sizeY - размер по высоте
     * @return - новая, отмасштабированная картинка
     */
    @Override
    public ImageIcon scaleImage(String path, int sizeX, int sizeY) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ImageIcon icon  = new ImageIcon(Objects.requireNonNull(classLoader.getResource("resources/" + path)));
        Image newImageIcon = icon.getImage().getScaledInstance(sizeX,sizeY,Image.SCALE_DEFAULT);
        icon.setImage(newImageIcon);
        return icon;
    }
}
