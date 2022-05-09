package converters;

import models.Group;
import models.Student;

import java.util.List;


public interface StudentConverter {

    public String convert(String groupId, List<Student> students);

}
