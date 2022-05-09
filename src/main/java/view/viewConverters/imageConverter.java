package view.viewConverters;

import javax.swing.*;
import java.io.IOException;

public interface imageConverter {

   public ImageIcon scaleImage(String path, int sizeX, int sizeY) throws IOException;
}
