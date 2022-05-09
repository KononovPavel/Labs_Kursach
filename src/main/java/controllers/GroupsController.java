package controllers;

import models.Group;
import models.Groups;
import models.Student;

import java.io.FileNotFoundException;
import java.util.List;

public interface GroupsController {

    public Group createGroup(String groupId, Groups groups, List<Student> students);

    public Group deleteGroup(String groupId, Groups groups) throws FileNotFoundException;

    public boolean isGroupWasCreatedFirstly(String groupId, Groups groups);
}
