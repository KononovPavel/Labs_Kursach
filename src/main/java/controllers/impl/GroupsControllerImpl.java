package controllers.impl;

import controllers.GroupsController;
import models.Group;
import models.Groups;
import models.Student;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Класс контроллер</p>
 * <p>Класс управления группами, созданный с целью добавить промежуточный слой между группой и группами</p>
 * Методы класса позволяют создавать группу, удалять ее, проверять на уже созданную
 *
 * @author Kononov Pavel
 * @version 1.0
 */
public class GroupsControllerImpl implements GroupsController {


    /**
     * Метод который создает группу, сначала проверяет, была ли она создана, заполняет список студентов
     * Устанавливает идентификатор, и передает группу в другой метод
     * @param groupId  - идентификатор группы
     * @param groups - список групп
     * @param students - список студентов
     * @return - созданную группу или null если группа была создана
     */
    @Override
    public Group createGroup(String groupId, Groups groups, List<Student> students) {
        if (isGroupWasCreatedFirstly(groupId, groups)) {
            Group newGroup = Group.builder().groupId(groupId).students(students.isEmpty() ? new ArrayList<>() : students).isCreatedFirstly(false).build();
            if (groups == null) {
                groups = new Groups();
            }
            groups.getGroups().put(groupId, newGroup);
            return newGroup;
        } else {
            return null;
        }
    }

    /**
     * Метод, для удаления группы из общего списка групп
     * @param groupId  - идентификатор группы
     * @param groups - список групп
     * @return - удаленную группу, с целью дальнейшей проверки
     */
    @Override
    public Group deleteGroup(String groupId, Groups groups, String path) {
        if (groups == null) {
            return null;
        }
        File selectedGroupForDeleting = new File(path + "/" + groupId + ".xls");
        if (selectedGroupForDeleting.delete()) {
            Group group = groups.getGroups().remove(groupId);
            if (group == null) {
                return null;
            }
            return group;
        } else {
            Group group = groups.getGroups().remove(groupId);
            if (group == null) {
                return null;
            }
            return group;
        }
    }

    /**
     * Метод, проверяющий на наличие группы в списке групп
     * @param groupId - идентификатор группы
     * @param groups - список групп
     * @return true - если группа не существовала, false - если группа была уже создана
     */
    @Override
    public boolean isGroupWasCreatedFirstly(String groupId, Groups groups) {
        if (groups == null) {
            return true;
        } else if (groups.getGroups().isEmpty()) {
            return true;
        } else if (groups.getGroups().get(groupId) == null) {
            return true;
        }
        return groups.getGroups().get(groupId).isCreatedFirstly();
    }
}
