package view.viewConverters;

import javax.swing.*;
import java.io.IOException;

public interface imageConverter {

   public ImageIcon scaleImage(String path, double sizeX, double sizeY) throws IOException;
}
