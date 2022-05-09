package models;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
/**
 * <p>Класс сущность</p>
 * <p>Класс представляет собой сущность даты для компоненты JDatePicker</p>
 * @author Kononov Pavel
 * @version 1.0 05.05.2022
 */
public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

    private String datePattern = "dd/MM/yyyy";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

    /**
     * метод, позволяющий конвертировать текст в Объект. В нашем случае - Дату
     * @param text  - параметр для конвертации
     * @return Object
     * @throws ParseException - error
     */
    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormat.parseObject(text);
    }

    /**
     * Метод, позволяющий конвертировать обьект в строку
     * @param value - Object
     * @return - String
     */
    @Override
    public String valueToString(Object value){
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormat.format(cal.getTime());
        }
        return "";
    }

}
