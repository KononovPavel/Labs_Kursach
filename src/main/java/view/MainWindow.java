package view;

import controllers.impl.GroupControllerImpl;
import controllers.impl.GroupsControllerImpl;
import controllers.impl.StudentControllerImpl;
import converters.impl.FileConverterImpl;
import lombok.SneakyThrows;
import models.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import view.viewConverters.impl.ImageConverterImpl;

import javax.swing.*;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * <p>Класс представления</p>
 * <p>Класс - Главное окно приложения</p>
 *
 * @author Kononov Pavel
 * @version 1.0
 */
public class MainWindow extends JFrame {

    private Groups groupsGeneral;
    private final JFileChooser fileChooser = new JFileChooser();

    private final String defaultPathToFiles = getClass().getClassLoader().getResource("resources/files") == null ? "src/main/resources/files" : getClass().getClassLoader().getResource("resources/files").getPath();

    private JPanel jpnlGroups;
    private JPanel jpnlStudentsTable;
    private JPanel jpnlAddDateToLab;
    private JPanel jpnlControllers;
    private JPanel jpnlCreateGroup;
    private JPanel jpnlDeleteGroup;

    // without group from first attendance of app
    private String currentlyGroupId = null;
    private Group currentlyGroup = null;
    private JButton jbtCurrentlyGroup = null;
    private boolean isAttendance = true;
    private boolean isAddedDate;
    private FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel", "xls");


    //controllers buttons
    private JButton jbtSortStudentsByAttendanceCount;
    private JButton jbtSortStudentsByLastNameButton;

    private JButton jbtStudentsAttendanceCount;
    private JButton jbtStudentsPassedOrFailedLab;

    private JPanel jpnlMain;

    private final ImageConverterImpl imageConverter = new ImageConverterImpl();
    private final GroupControllerImpl groupController = new GroupControllerImpl();
    private final FileConverterImpl fileConverter = new FileConverterImpl();
    private final GroupsControllerImpl groupsController = new GroupsControllerImpl();
    private final StudentControllerImpl studentController = new StudentControllerImpl();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private Font getCourierFont(int fontSize, int fontType) {
        return new Font("Courier", fontType, fontSize);
    }

    /**
     * Конструктор, создающий главное окно приложения и заполняет его данными
     *
     * @throws IOException
     * @throws ParseException
     * @throws InvalidFormatException
     */
    public MainWindow() throws IOException, ParseException, InvalidFormatException {
        super("Главная");
        jpnlMain = new JPanel(null);
        JMenuBar jMenuBar = new JMenuBar();
        groupsGeneral = fileConverter.processFilesFromFolderRecursive(new File(defaultPathToFiles));

        if (groupsGeneral == null) {
            showPanelWithoutGroups();
        } else {
            createMainWindow(groupsGeneral);
        }

        JMenu FAQMenu = new JMenu("FAQ");
        JMenuItem aboutProgramMenuItem = new JMenuItem("О программе", imageConverter.scaleImage("images/aboutProgram.png", 30, 30));
        aboutProgramMenuItem.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new AboutProgram();
            }
        });
        FAQMenu.add(aboutProgramMenuItem);
        FAQMenu.addSeparator();

        JMenuItem aboutAuthorMenuItem = new JMenuItem("Об авторе", imageConverter.scaleImage("images/aboutAuthor.png", 30, 30));
        aboutAuthorMenuItem.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new AuthorWindow();
            }
        });
        FAQMenu.add(aboutAuthorMenuItem);

        JMenu printMenu = new JMenu("Печать");
        JMenuItem printMenuItem = new JMenuItem("Вывести на печать в папку");

        printMenuItem.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                if (groupsGeneral == null || groupsGeneral.getGroups().isEmpty()) {
                    JOptionPane.showMessageDialog(MainWindow.this, "Нет групп для печати, пожалуйста, создайте / выберите группу", "Предупреждение", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    fileChooser.setDialogTitle("Сохранение файла групп");
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    fileChooser.setFileFilter(filter);
                    int result = fileChooser.showSaveDialog(MainWindow.this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        for (Map.Entry<String, Group> entry : groupsGeneral.getGroups().entrySet()) {
                            groupController.sortStudentsByLastName(entry.getValue().getStudents(), true);
                            fileConverter.createFileFromGroup(entry.getValue(), fileChooser.getSelectedFile().getAbsolutePath(), true);
                        }
                        JOptionPane.showMessageDialog(MainWindow.this, "Группы успешно сохранены");
                    }
                }
            }
        });
        printMenu.add(printMenuItem);
        jMenuBar.add(FAQMenu);
        jMenuBar.add(printMenu);
        add(jpnlMain);
        addWindowListener(new WindowListener() {
            /**
             * Invoked the first time a window is made visible.
             */
            @Override
            public void windowOpened(WindowEvent e) {
            }

            /**
             * Invoked when the user attempts to close the window
             * from the window's system menu.
             */
            @SneakyThrows
            @Override
            public void windowClosing(WindowEvent e) {
                if (groupsGeneral != null) {
                    for (Map.Entry<String, Group> entry : groupsGeneral.getGroups().entrySet()) {
                        groupController.sortStudentsByLastName(entry.getValue().getStudents(), true);
                        fileConverter.createFileFromGroup(entry.getValue(), defaultPathToFiles, true);
                    }
                    JOptionPane.showMessageDialog(MainWindow.this, "Сохранение прошло успешно", "Подтверждение", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            /**
             * Invoked when a window has been closed as the result
             * of calling dispose on the window.
             */
            @Override
            public void windowClosed(WindowEvent e) {
            }

            /**
             * Invoked when a window is changed from a normal to a
             * minimized state. For many platforms, a minimized window
             * is displayed as the icon specified in the window's
             * iconImage property.
             * @see java.awt.Frame#setIconImage
             */
            @Override
            public void windowIconified(WindowEvent e) {
            }

            /**
             * Invoked when a window is changed from a minimized
             * to a normal state.
             */
            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            /**
             * Invoked when the Window is set to be the active Window. Only a Frame or
             * a Dialog can be the active Window. The native windowing system may
             * denote the active Window or its children with special decorations, such
             * as a highlighted title bar. The active Window is always either the
             * focused Window, or the first Frame or Dialog that is an owner of the
             * focused Window.
             */
            @Override
            public void windowActivated(WindowEvent e) {
            }

            /**
             * Invoked when a Window is no longer the active Window. Only a Frame or a
             * Dialog can be the active Window. The native windowing system may denote
             * the active Window or its children with special decorations, such as a
             * highlighted title bar. The active Window is always either the focused
             * Window, or the first Frame or Dialog that is an owner of the focused
             * Window.
             */
            @SneakyThrows
            @Override
            public void windowDeactivated(WindowEvent e) {
                if (groupsGeneral != null) {
                    for (Map.Entry<String, Group> entry : groupsGeneral.getGroups().entrySet()) {
                        groupController.sortStudentsByLastName(entry.getValue().getStudents(), true);
                        fileConverter.createFileFromGroup(entry.getValue(), defaultPathToFiles, true);
                    }
                }
            }
        });

        setJMenuBar(jMenuBar);
        setSize(1920, 1020);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void createMainWindow(Groups groups) {
        jpnlMain.removeAll();
        jpnlGroups = new JPanel(null);
        jpnlGroups.setSize(200, 1020);
        jpnlGroups.setBounds((1920 / 2) - 100, 0, 200, 1020);
        setGroupsButtons(groups.getGroups());

        jpnlControllers = new JPanel(null);
        jpnlControllers.setSize(860, 150);
        jpnlControllers.setBounds(1060, 820, 860, 150);
        jpnlControllers.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        setSortButtons();


        jpnlStudentsTable = new JPanel(null);
        jpnlStudentsTable.setSize(860, 820);
        jpnlStudentsTable.setBounds(1060, 0, 860, 820);
        jpnlStudentsTable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        if (currentlyGroupId == null && currentlyGroup == null) {
            setNotActiveGroupViewForStudentsTable();
        }

        jpnlAddDateToLab = new JPanel(null);
        jpnlAddDateToLab.setSize(860, 520);
        jpnlAddDateToLab.setBounds(0, 520, 860, 520);
        jpnlAddDateToLab.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        if (currentlyGroupId == null && currentlyGroup == null) {
            setNotActiveGroupViewForAddDateToLabPanel();
        }

        jpnlDeleteGroup = new JPanel(null);
        jpnlDeleteGroup.setSize(430, 520);
        jpnlDeleteGroup.setBounds(430, 0, 430, 520);
        jpnlDeleteGroup.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        if (currentlyGroup == null && currentlyGroupId == null) {
            setNotActiveGroupViewForDeleteGroupPanel();
        }

        jpnlMain.add(jpnlGroups);
        jpnlMain.add(jpnlControllers);
        jpnlMain.add(jpnlStudentsTable);
        jpnlMain.add(jpnlAddDateToLab);
        jpnlMain.add(getCreateGroupPanel());
        jpnlMain.add(jpnlDeleteGroup);
        jpnlMain.repaint();
        jpnlMain.revalidate();
    }


    private void setDeleteGroupPanel(Map<String, Group> groups) {
        jpnlDeleteGroup.removeAll();
        JLabel jlblDeleteGroupTitle = new JLabel("Удаление группы");
        jlblDeleteGroupTitle.setFont(getCourierFont(20, Font.PLAIN));
        jlblDeleteGroupTitle.setBounds(430 / 2 - 80, 30, 250, 30);

        String[] groupsArray = getStringGroups(groups);
        JComboBox<String> jComBoxTypeOfGroups = new JComboBox<>(groupsArray);

        JLabel jlblPleaseSelect = new JLabel("Выберите группу");
        jlblPleaseSelect.setFont(getCourierFont(24, Font.PLAIN));
        jlblPleaseSelect.setBounds(120, 120, 200, 30);

        jComBoxTypeOfGroups.setBounds(0, 180, 430, 100);
        jComBoxTypeOfGroups.setSize(430, 100);
        jComBoxTypeOfGroups.setFont(getCourierFont(24, Font.PLAIN));

        JLabel jlblSelectedGroup = new JLabel("");
        jlblSelectedGroup.setBounds(100, 300, 300, 30);
        jlblSelectedGroup.setFont(getCourierFont(20, Font.BOLD));

        JLabel jlblDeletedImage = new JLabel();
        jlblDeletedImage.setBounds(50, 300, 30, 30);

        JButton jbtDeleteSelectedGroup = new JButton();
        jbtDeleteSelectedGroup.setBounds(50, 370, 330, 60);
        jbtDeleteSelectedGroup.setText("Удалить группу");
        jbtDeleteSelectedGroup.setFocusPainted(false);
        jbtDeleteSelectedGroup.setFont(getCourierFont(20, Font.PLAIN));
        jbtDeleteSelectedGroup.setEnabled(false);

        jbtDeleteSelectedGroup.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                Group deletedGroup = groupsController.deleteGroup(String.valueOf(jComBoxTypeOfGroups.getSelectedItem()), groupsGeneral);
                if (deletedGroup == null) {
                    JOptionPane.showMessageDialog(MainWindow.this, "Группа не была удалена", "Ошибка", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (deletedGroup == currentlyGroup) {
                        currentlyGroup = null;
                        currentlyGroupId = null;
                        jbtCurrentlyGroup = null;
                        setNotActiveGroupViewForAddDateToLabPanel();
                        setNotActiveGroupViewForDeleteGroupPanel();
                        setNotActiveGroupViewForStudentsTable();
                        setGroupsButtons(groups);
                        setSortButtons();
                        JOptionPane.showMessageDialog(MainWindow.this, "Группа, с которой вы работали был удалена", "Предупреждение", JOptionPane.WARNING_MESSAGE);
                    } else {
                        jlblSelectedGroup.setText("");
                        jlblDeletedImage.setIcon(null);
                        jbtDeleteSelectedGroup.setEnabled(false);
                        setDeleteGroupPanel(groups);
                        setGroupsButtons(groups);
                        JOptionPane.showMessageDialog(MainWindow.this, "Группа " + String.valueOf(jComBoxTypeOfGroups.getSelectedItem()) + " была удалена, продолжайте работать спокойно!", "Ошибка", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        jComBoxTypeOfGroups.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                jlblSelectedGroup.setText("Удалить группу: " + String.valueOf(jComBoxTypeOfGroups.getSelectedItem()) + " ?");
                jlblDeletedImage.setIcon(imageConverter.scaleImage("images/deleteGroupt.png", 30, 30));
                jbtDeleteSelectedGroup.setEnabled(true);
            }
        });

        jpnlDeleteGroup.add(jlblDeleteGroupTitle);
        jpnlDeleteGroup.add(jComBoxTypeOfGroups);
        jpnlDeleteGroup.add(jlblPleaseSelect);
        jpnlDeleteGroup.add(jlblSelectedGroup);
        jpnlDeleteGroup.add(jlblDeletedImage);
        jpnlDeleteGroup.add(jbtDeleteSelectedGroup);
        jpnlDeleteGroup.repaint();
        jpnlDeleteGroup.revalidate();
    }

    private JPanel getCreateGroupPanel() {
        jpnlCreateGroup = new JPanel(null);
        jpnlCreateGroup.setSize(430, 520);
        jpnlCreateGroup.setBounds(0, 0, 430, 520);
        jpnlCreateGroup.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JLabel jlblCreateGroupTitle = new JLabel("Создание группы");
        jlblCreateGroupTitle.setFont(getCourierFont(20, Font.PLAIN));
        jlblCreateGroupTitle.setBounds(430 / 2 - 80, 30, 250, 30);

        JButton jbtOpenDialog = new JButton("Загрузить файл");
        jbtOpenDialog.setSize(300, 60);
        jbtOpenDialog.setBounds(65, 100, 300, 60);
        jbtOpenDialog.setFocusPainted(false);
        jbtOpenDialog.setFont(getCourierFont(24, Font.PLAIN));
        jbtOpenDialog.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Выбор файла");
                JButton jbtCreateGroup = new JButton();
                JLabel jlblFilePath = new JLabel("");
                jlblFilePath.setFont(getCourierFont(14, Font.PLAIN));
                jlblFilePath.setBounds((430) / 2 - 70, 185, 230, 30);

                JLabel jlblImageExcel = new JLabel("");
                jlblImageExcel.setBounds((430) / 2 - 120, 180, 40, 40);
                jpnlCreateGroup.add(jlblFilePath);
                jpnlCreateGroup.add(jbtCreateGroup);
                jpnlCreateGroup.add(jlblImageExcel);

                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(MainWindow.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String[] filePath = fileChooser.getSelectedFile().getPath().replace("\\", "/").split("/");
                    String fileName = filePath[filePath.length - 1];
                    jlblFilePath.setText(fileName);
                    jlblFilePath.setFont(getCourierFont(20, Font.BOLD));
                    jlblImageExcel.setIcon(imageConverter.scaleImage("images/excel.png", 40, 40));
                    jbtCreateGroup.setFocusPainted(false);
                    jbtCreateGroup.setText("Добавить группу");
                    jbtCreateGroup.setFont(getCourierFont(20, Font.PLAIN));
                    jbtCreateGroup.setSize(300, 60);
                    jbtCreateGroup.setBounds(65, 250, 300, 60);
                    jbtCreateGroup.addActionListener(new ActionListener() {
                        /**
                         * Invoked when an action occurs.
                         */
                        @SneakyThrows
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Group newGroup = fileConverter.getGroupByFile(fileChooser.getSelectedFile().getPath(), groupsGeneral);
                            if (newGroup == null) {
                                JOptionPane.showMessageDialog(MainWindow.this, "Группа была уже создана " + fileName, "Предупреждение", JOptionPane.WARNING_MESSAGE);
                            }
                            if (groupsGeneral == null && newGroup != null) {
                                groupsGeneral = new Groups();
                                groupsGeneral.getGroups().put(newGroup.getGroupId(), newGroup);
                                JOptionPane.showMessageDialog(MainWindow.this, "Группа была создана", "Успешно", JOptionPane.INFORMATION_MESSAGE);
                            }
                            if (groupsGeneral == null) {
                                JOptionPane.showMessageDialog(MainWindow.this, "В выбранном файле групп нет", "Предупреждение", JOptionPane.WARNING_MESSAGE);
                            } else if (newGroup != null) {
                                setGroupsButtons(groupsGeneral.getGroups());
                                setDeleteGroupPanel(groupsGeneral.getGroups());
                                JOptionPane.showMessageDialog(MainWindow.this, "Группа была создана", "Успешно", JOptionPane.INFORMATION_MESSAGE);
                            }
                            jpnlCreateGroup.remove(jbtCreateGroup);
                            jpnlCreateGroup.remove(jlblFilePath);
                            jpnlCreateGroup.remove(jlblImageExcel);
                            jpnlCreateGroup.repaint();
                            jpnlCreateGroup.revalidate();
                        }
                    });
                }
            }
        });
        jpnlCreateGroup.add(jlblCreateGroupTitle);
        jpnlCreateGroup.add(jbtOpenDialog);
        return jpnlCreateGroup;
    }

    private void setGroupsButtons(Map<String, Group> groups) {
        jpnlGroups.removeAll();
        int x = 10;
        int y = 50;
        JLabel jlblListGroupName = new JLabel("Список групп");
        jlblListGroupName.setFont(getCourierFont(20, Font.PLAIN));
        jlblListGroupName.setBounds(50, 10, 180, 40);
        for (Map.Entry<String, Group> groupEntry : groups.entrySet()) {
            JButton jbtGroup = new JButton(groupEntry.getValue().getGroupId());
            jbtGroup.setSize(180, 40);
            jbtGroup.setBounds(x, y, 180, 40);
            jbtGroup.setFont(getCourierFont(16, Font.PLAIN));
            jbtGroup.setFocusPainted(false);
            if (jbtCurrentlyGroup != null && jbtGroup.getText().equals(jbtCurrentlyGroup.getText())) {
                jbtCurrentlyGroup.setBackground(null);
                jbtCurrentlyGroup = jbtGroup;
                jbtGroup.setBackground(Color.PINK);
                jlblListGroupName.setText(currentlyGroupId);
                jpnlGroups.repaint();
                jpnlGroups.revalidate();
            }

            jbtGroup.addMouseListener(new MouseAdapter() {
                @SneakyThrows
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        showGroupToFileWithClickOnRightMouseButton(groupEntry.getValue());
                        setGroupsButtons(groups);
                    }
                }
            });
            jbtGroup.addActionListener(new ActionListener() {
                /**
                 * Invoked when an action occurs.
                 */
                @SneakyThrows
                @Override
                public void actionPerformed(ActionEvent e) {
                    currentlyGroupId = groupEntry.getValue().getGroupId();
                    currentlyGroup = groupEntry.getValue();
                    if (jbtCurrentlyGroup != null) {
                        jbtCurrentlyGroup.setBackground(null);
                    }
                    jbtCurrentlyGroup = jbtGroup;
                    jbtGroup.setBackground(Color.PINK);

                    jlblListGroupName.setText(currentlyGroupId);
                    jlblListGroupName.setBounds(50, 10, 180, 40);

                    jpnlStudentsTable.removeAll();
                    setStudentsTable(groupController.sortStudentsByLastName(currentlyGroup.getStudents(), true), isAttendance);
                    isAttendance = true;
                    setSortButtons();
                    setAddDateToLabView();
                    setDeleteGroupPanel(groups);
                    jpnlStudentsTable.repaint();
                    jpnlStudentsTable.revalidate();
                    jpnlGroups.repaint();
                    jpnlGroups.revalidate();
                }
            });
            jpnlGroups.add(jbtGroup);
            y += 50;
        }
        jpnlGroups.add(jlblListGroupName);
        jpnlGroups.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        jpnlGroups.repaint();
        jpnlGroups.revalidate();
    }

    private void setSortButtons() {
        jpnlControllers.removeAll();
        jbtStudentsAttendanceCount = new JButton("Посещение студентов");
        jbtStudentsAttendanceCount.setFont(getCourierFont(16, Font.PLAIN));

        jbtStudentsPassedOrFailedLab = new JButton("Лабораторные работы");
        jbtStudentsPassedOrFailedLab.setFont(getCourierFont(16, Font.PLAIN));

        jbtStudentsAttendanceCount.setBounds(0, 0, 430, 50);
        jbtStudentsPassedOrFailedLab.setBounds(430, 0, 430, 50);

        jbtStudentsAttendanceCount.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                isAttendance = true;
                createStudentsPane(groupController.sortStudentsByLastName(currentlyGroup.getStudents(), true), isAttendance);
                jbtStudentsAttendanceCount.setBackground(Color.PINK);
                jbtStudentsPassedOrFailedLab.setBackground(null);
            }
        });

        jbtStudentsPassedOrFailedLab.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                isAttendance = false;
                createStudentsPane(groupController.sortStudentsByLastName(currentlyGroup.getStudents(), true), isAttendance);
                jbtStudentsPassedOrFailedLab.setBackground(Color.PINK);
                jbtStudentsAttendanceCount.setBackground(null);
            }
        });


        jbtStudentsAttendanceCount.setFocusPainted(false);
        jbtStudentsPassedOrFailedLab.setFocusPainted(false);

        if (currentlyGroup != null && currentlyGroupId != null) {
            if (isAttendance) {
                jbtStudentsAttendanceCount.setBackground(Color.PINK);
                jbtStudentsPassedOrFailedLab.setBackground(null);
                jpnlControllers.repaint();
                jpnlControllers.revalidate();
            } else {
                jbtStudentsPassedOrFailedLab.setBackground(Color.PINK);
                jbtStudentsAttendanceCount.setBackground(null);
                jpnlControllers.repaint();
                jpnlControllers.revalidate();
            }
            if (currentlyGroup.getStudents().get(0).getPassedOrFailedLab().isEmpty()) {
                jbtStudentsPassedOrFailedLab.setEnabled(false);
            }
            jpnlControllers.add(jbtStudentsAttendanceCount);
            jpnlControllers.add(jbtStudentsPassedOrFailedLab);
            jpnlControllers.repaint();
            jpnlControllers.revalidate();
        } else {
            jpnlControllers.repaint();
            jpnlControllers.revalidate();
        }


        jbtSortStudentsByAttendanceCount = new JButton("Сортировка по посещениям");
        jbtSortStudentsByAttendanceCount.setFocusPainted(false);
        jbtSortStudentsByAttendanceCount.setSize(270, 80);
        jbtSortStudentsByAttendanceCount.setBounds(10, 55, 270, 80);
        jbtSortStudentsByAttendanceCount.setFont(getCourierFont(14, Font.PLAIN));
        ImageIcon jImgSortStudentsByAttendance = imageConverter.scaleImage("images/sort_1.png", 20, 20);
        jbtSortStudentsByAttendanceCount.setIcon(jImgSortStudentsByAttendance);
        final boolean[] isFlagForAttendance = {true};
        jbtSortStudentsByAttendanceCount.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentlyGroup != null && currentlyGroupId != null) {
                    isFlagForAttendance[0] = !isFlagForAttendance[0];
                    List<Student> sortedStudents = groupController.sortStudentsByAttendanceCount(currentlyGroup.getStudents(), isFlagForAttendance[0]);
                    isAttendance = true;
                    createStudentsPane(sortedStudents, isAttendance);
                    setSortButtons();
                } else {
                    JOptionPane.showMessageDialog(MainWindow.this, "Не выбрана группа", "Ошибка", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        jpnlControllers.add(jbtSortStudentsByAttendanceCount);


        jbtSortStudentsByLastNameButton = new JButton("Сортировка по фамилии");
        jbtSortStudentsByLastNameButton.setFocusPainted(false);
        jbtSortStudentsByLastNameButton.setSize(270, 80);
        jbtSortStudentsByLastNameButton.setBounds(270 + 20, 55, 270, 80);
        jbtSortStudentsByLastNameButton.setFont(getCourierFont(14, Font.PLAIN));
        ImageIcon jImgSortStudentsByLastName = imageConverter.scaleImage("images/sortA_Z.png", 20, 20);
        jbtSortStudentsByLastNameButton.setIcon(jImgSortStudentsByLastName);
        final boolean[] isFlagForLastName = {true};
        jbtSortStudentsByLastNameButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentlyGroup != null && currentlyGroupId != null) {
                    isFlagForLastName[0] = !isFlagForLastName[0];
                    List<Student> sortedStudents = groupController.sortStudentsByLastName(currentlyGroup.getStudents(), isFlagForLastName[0]);
                    isAttendance = true;
                    setStudentsTable(sortedStudents, isAttendance);

                } else {
                    JOptionPane.showMessageDialog(MainWindow.this, "Не выбрана группа", "Ошибка", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        jpnlControllers.add(jbtSortStudentsByLastNameButton);
        String[] listOfDate = getStringDateList();
        if (listOfDate != null) {
            JComboBox<String> typeOfDates = new JComboBox<>(listOfDate);
            typeOfDates.addActionListener(new ActionListener() {
                /**
                 * Invoked when an action occurs.
                 */
                @SneakyThrows
                @Override
                public void actionPerformed(ActionEvent e) {
                    List<Student> sortedStudents = groupController.sortStudentByDateOfLab(dateFormat.parse((String) typeOfDates.getSelectedItem()), currentlyGroup.getStudents());
                    if (sortedStudents != null) {
                        isAttendance = true;
                        setStudentsTable(sortedStudents, isAttendance);
                        jbtStudentsAttendanceCount.setBackground(Color.PINK);
                        jbtStudentsPassedOrFailedLab.setBackground(null);
                        jpnlStudentsTable.repaint();
                        jpnlStudentsTable.revalidate();
                    } else {
                        JOptionPane.showMessageDialog(MainWindow.this, "Нет посещений у студентов", "Предупреждение", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            typeOfDates.setBounds(270 + 270 + 20 + 10, 55, 270, 80);
            typeOfDates.setSize(270, 80);
            typeOfDates.setFont(getCourierFont(24, Font.PLAIN));
            jpnlControllers.add(typeOfDates);
            jpnlControllers.repaint();
            jpnlControllers.revalidate();
        }
    }

    private void showGroupToFileWithClickOnRightMouseButton(Group group) throws IOException, ParseException {
        fileChooser.setDialogTitle("Сохранение файла группы " + group.getGroupId());
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setSelectedFile(new File(group.getGroupId() + ".xls"));
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showSaveDialog(MainWindow.this);
        if (result == JFileChooser.APPROVE_OPTION) {
            fileConverter.createFileFromGroup(group, fileChooser.getSelectedFile().getAbsolutePath(), false);

            JOptionPane.showMessageDialog(MainWindow.this, "Группа была сохранена, путь: " + fileChooser.getSelectedFile().getAbsolutePath());
        }
        fileChooser.setFileFilter(null);
    }

    //Заглушки для неактивной группы
    private void setNotActiveGroupViewForStudentsTable() {
        jpnlStudentsTable.removeAll();
        JLabel jlblNotActiveGroupImg = new JLabel(imageConverter.scaleImage("images/not_active_group.png", 200, 200));
        jlblNotActiveGroupImg.setBounds(860 / 2 - 100, 820 / 2 - 100, 200, 200);
        JLabel jlblNotActiveGroup = new JLabel("Пожалуйста, выберите группу студентов");
        jlblNotActiveGroup.setFont(getCourierFont(20, Font.PLAIN));
        jlblNotActiveGroup.setBounds(250, 550, 500, 50);
        jpnlStudentsTable.add(jlblNotActiveGroup);
        jpnlStudentsTable.add(jlblNotActiveGroupImg);
        jpnlStudentsTable.repaint();
        jpnlStudentsTable.revalidate();
    }

    private void setNotActiveGroupViewForAddDateToLabPanel() {
        jpnlAddDateToLab.removeAll();
        JLabel jlblNotActiveGroupImg = new JLabel(imageConverter.scaleImage("images/not_active_group_for_date.png", 200, 200));
        jlblNotActiveGroupImg.setBounds(860 / 2 - 100, 520 / 2 - 170, 200, 200);
        JLabel jlblNotActiveGroup = new JLabel("Добавление даты будет доступно после выбора группы студентов");
        jlblNotActiveGroup.setFont(getCourierFont(24, Font.PLAIN));
        jlblNotActiveGroup.setBounds(60, 300, 800, 50);
        jpnlAddDateToLab.add(jlblNotActiveGroupImg);
        jpnlAddDateToLab.add(jlblNotActiveGroup);
        jpnlAddDateToLab.repaint();
        jpnlAddDateToLab.revalidate();
    }

    private void setNotActiveGroupViewForDeleteGroupPanel() {
        jpnlDeleteGroup.removeAll();
        JLabel jlblWithoutGroupsImg = new JLabel(imageConverter.scaleImage("images/deleteWithoutGroups.png", 200, 200));
        JLabel jlblWithoutGroups = new JLabel("Выберите или добавьте группу(ы)");
        jlblWithoutGroupsImg.setBounds(430 / 2 - 100, 520 / 2 - 100, 200, 200);
        jlblWithoutGroups.setFont(getCourierFont(20, Font.PLAIN));
        jlblWithoutGroups.setBounds(40, 400, 400, 30);
        jpnlDeleteGroup.add(jlblWithoutGroupsImg);
        jpnlDeleteGroup.add(jlblWithoutGroups);
        jpnlDeleteGroup.repaint();
        jpnlDeleteGroup.revalidate();
    }

    private void showPanelWithoutGroups() {
        JLabel jlblNoGroupsImage = new JLabel(imageConverter.scaleImage("images/noFiles.png", 350, 350));
        jlblNoGroupsImage.setBounds((1920 / 2) - 200, (1040 / 2) - 400, 350, 350);

        JLabel jlblNoGroups = new JLabel("Выполнен первый раз вход в приложение");
        jlblNoGroups.setFont(getCourierFont(30, Font.PLAIN));
        jlblNoGroups.setBounds(700, 470, 800, 60);

        JLabel jlblNoGroupsHelper = new JLabel("Или мы просто не смогли найти файлы во внутренней системе");
        jlblNoGroupsHelper.setFont(getCourierFont(24, Font.PLAIN));
        jlblNoGroupsHelper.setBounds(600, 530, 800, 60);

        JLabel jlblNoGroupsWarranty = new JLabel("Выберите один из трех вариантов");
        jlblNoGroupsWarranty.setFont(getCourierFont(24, Font.PLAIN));
        jlblNoGroupsWarranty.setBounds(800, 570, 800, 60);

        JButton jbtOpenFolder = new JButton("Открыть папку с файлами");
        jbtOpenFolder.setSize(400, 80);
        jbtOpenFolder.setFocusPainted(false);
        jbtOpenFolder.setBounds(250, 650, 400, 80);
        jbtOpenFolder.setFont(getCourierFont(30, Font.PLAIN));
        jbtOpenFolder.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Выбор папки с группами");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setFileFilter(filter);
                int result = fileChooser.showOpenDialog(MainWindow.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    groupsGeneral = fileConverter.processFilesFromFolderRecursive(fileChooser.getSelectedFile());
                    if (groupsGeneral == null) {
                        JOptionPane.showMessageDialog(MainWindow.this, "Групп в выбранной папке");
                    } else {
                        createMainWindow(groupsGeneral);
                    }
                }
            }
        });

        JButton jbtOpenFile = new JButton("Открыть файл");
        jbtOpenFile.setSize(300, 40);
        jbtOpenFile.setFocusPainted(false);
        jbtOpenFile.setBounds(750, 650, 400, 80);
        jbtOpenFile.setFont(getCourierFont(30, Font.PLAIN));
        jbtOpenFile.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Выбор файла");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setFileFilter(filter);
                int result = fileChooser.showOpenDialog(MainWindow.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    Group newGroup = fileConverter.getGroupByFile(fileChooser.getSelectedFile().getPath(), groupsGeneral);
                    if (newGroup == null) {
                        JOptionPane.showMessageDialog(MainWindow.this, "Группа была уже создана", "Предупреждение", JOptionPane.WARNING_MESSAGE);
                    }
                    if (groupsGeneral == null && newGroup != null) {
                        groupsGeneral = new Groups();
                        groupsGeneral.getGroups().put(newGroup.getGroupId(), newGroup);
                    }
                    if (groupsGeneral == null) {
                        JOptionPane.showMessageDialog(MainWindow.this, "В выбранном файле групп нет", "Предупреждение", JOptionPane.WARNING_MESSAGE);
                    } else if (newGroup != null) {
                        createMainWindow(groupsGeneral);
                    }
                }
            }
        });

        JButton jbtOpenHelpAboutFile = new JButton("Помощь");
        jbtOpenHelpAboutFile.setSize(400, 80);
        jbtOpenHelpAboutFile.setFocusPainted(false);
        jbtOpenHelpAboutFile.setBounds(1250, 650, 400, 80);
        jbtOpenHelpAboutFile.setFont(getCourierFont(30, Font.PLAIN));
        jbtOpenHelpAboutFile.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                new HelperWindow();
            }
        });


        jpnlMain.add(jlblNoGroupsImage);
        jpnlMain.add(jlblNoGroups);
        jpnlMain.add(jbtOpenFolder);
        jpnlMain.add(jbtOpenFile);
        jpnlMain.add(jlblNoGroupsWarranty);
        jpnlMain.add(jlblNoGroupsHelper);
        jpnlMain.add(jbtOpenHelpAboutFile);
        jpnlMain.repaint();
        jpnlMain.revalidate();
    }

    private void setStudentsTable(List<Student> studentList, boolean isAttendance) {
        if (studentList.size() == 0) {
            JOptionPane.showMessageDialog(MainWindow.this, "Упс, что-то пошло не так", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
        createStudentsPane(studentList, isAttendance);
    }

    private String[] getStringDateList() {
        if (currentlyGroup != null) {
            List<Date> listDate = getListOfDate();
            if (listDate.size() == 0) {
                return null;
            }
            sortListDate(listDate);
            String[] result = new String[listDate.size()];
            for (int i = 0; i < listDate.size(); i++) {
                result[i] = dateFormat.format(listDate.get(i));
            }
            return result;
        }
        return null;
    }

    private String[] getStringLabsList() {
        if (currentlyGroup != null) {
            List<String> listOfLabs = getListOfLabs();
            if (listOfLabs.size() == 0) {
                return null;
            }
            sortLabKeys(listOfLabs);
            String[] result = new String[listOfLabs.size()];
            for (int i = 0; i < listOfLabs.size(); i++) {
                result[i] = listOfLabs.get(i);
            }
            return result;
        }
        return null;
    }


    private List<String> getListOfLabs() {
        return new ArrayList<>(currentlyGroup.getStudents().get(0).getPassedOrFailedLab().keySet());
    }

    private List<Date> getListOfDate() {
        return fileConverter.convertSetToList(currentlyGroup.getStudents().get(0).getAttendanceOfStudent().keySet());
    }

    private String[] getStringGroups(Map<String, Group> groups) {
        Set<String> stringSet = groups.keySet();
        List<String> stringList = new ArrayList<>(stringSet);
        String[] result = new String[stringList.size()];
        for (int i = 0; i < stringList.size(); i++) {
            result[i] = stringList.get(i);
        }
        return result;
    }

    public void createStudentsPane(List<Student> students, boolean isAttendance) {
        JTable jtblStudents = null;
        if (isAttendance) {
            jtblStudents = new JTable(createTableDataOfAttendance(students), createHeaderTableOfAttendance()) {
                /**
                 * Returns the type of the column appearing in the view at
                 * column position <code>column</code>.
                 *
                 * @param   column   the column in the view being queried
                 * @return the type of the column at position <code>column</code>
                 *          in the view where the first column is column 0
                 */
                @Override
                public Class<?> getColumnClass(int column) {
                    if (column == 0 || column == 1 || column == 2 || column == 3) {
                        return String.class;
                    }
                    return Boolean.class;
                }

                /**
                 * Returns the cell value at <code>row</code> and <code>column</code>.
                 * <p>
                 * <b>Note</b>: The column is specified in the table view's display
                 *              order, and not in the <code>TableModel</code>'s column
                 *              order.  This is an important distinction because as the
                 *              user rearranges the columns in the table,
                 *              the column at a given index in the view will change.
                 *              Meanwhile the user's actions never affect the model's
                 *              column ordering.
                 *
                 * @param   row             the row whose value is to be queried
                 * @param   col          the column whose value is to be queried
                 * @return the Object at the specified cell
                 */
                @SneakyThrows
                @Override
                public void setValueAt(Object value, int row, int col) {
                    super.setValueAt(value, row, col);
                    if (col > 3) {
                        String date = createHeaderTableOfAttendance()[col];
                        studentController.addAttendanceForStudent(students.get(row), (Boolean) this.getValueAt(row, col), dateFormat.parse(date));
                        createStudentsPane(students, true);
                        setSortButtons();
                    }
                }
            };
        } else {
            jtblStudents = new JTable(createTableDataOfPassedOrFailedLabs(students), createHeaderTableOfPassedOrFailedLab()) {
                /**
                 * Returns the type of the column appearing in the view at
                 * column position <code>column</code>.
                 *
                 * @param   column   the column in the view being queried
                 * @return the type of the column at position <code>column</code>
                 *          in the view where the first column is column 0
                 */
                @Override
                public Class<?> getColumnClass(int column) {
                    if (column == 0 || column == 1 || column == 2) {
                        return String.class;
                    }
                    return Boolean.class;
                }

                /**
                 * Returns the cell value at <code>row</code> and <code>column</code>.
                 * <p>
                 * <b>Note</b>: The column is specified in the table view's display
                 *              order, and not in the <code>TableModel</code>'s column
                 *              order.  This is an important distinction because as the
                 *              user rearranges the columns in the table,
                 *              the column at a given index in the view will change.
                 *              Meanwhile the user's actions never affect the model's
                 *              column ordering.
                 *
                 * @param   row             the row whose value is to be queried
                 * @param   col          the column whose value is to be queried
                 * @return the Object at the specified cell
                 */
                @SneakyThrows
                @Override
                public void setValueAt(Object value, int row, int col) {
                    super.setValueAt(value, row, col);
                    if (col > 2) {
                        String lab = createHeaderTableOfPassedOrFailedLab()[col];
                        studentController.addPassesOrFailedLabForStudent(students.get(row), (Boolean) this.getValueAt(row, col), lab);
                        createStudentsPane(students, false);
                    }
                }
            };
        }
        jtblStudents.setAutoCreateRowSorter(true);
        jtblStudents.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jtblStudents.setPreferredScrollableViewportSize(new Dimension(855, 820));
        jtblStudents.setFont(getCourierFont(20, Font.PLAIN));
        jtblStudents.setRowHeight(50);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);

        jtblStudents.getColumnModel().getColumn(0).setPreferredWidth(30);
        jtblStudents.getColumnModel().getColumn(1).setPreferredWidth(350);
        jtblStudents.getColumnModel().getColumn(2).setPreferredWidth(130);
        if (isAttendance) {
            jtblStudents.getColumnModel().getColumn(3).setPreferredWidth(120);
            jtblStudents.getColumnModel().getColumn(3).setCellRenderer(center);
        }
        if (!isAttendance) {
            for (int i = 0; i < currentlyGroup.getStudents().get(0).getPassedOrFailedLab().size(); i++) {
                jtblStudents.getColumnModel().getColumn(3 + i).setPreferredWidth(180);
            }
        }

        jtblStudents.getColumnModel().getColumn(0).setCellRenderer(center);
        jtblStudents.getColumnModel().getColumn(2).setCellRenderer(center);

        JScrollPane jScrPaneStudents = new JScrollPane(jtblStudents);
        jScrPaneStudents.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrPaneStudents.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrPaneStudents.setMaximumSize(jScrPaneStudents.getSize());
        jScrPaneStudents.setMinimumSize(jScrPaneStudents.getSize());
        jScrPaneStudents.setBounds(0, 0, 855, 820);
        jpnlStudentsTable.removeAll();
        jpnlStudentsTable.add(jScrPaneStudents);
        jpnlStudentsTable.repaint();
        jpnlStudentsTable.revalidate();
    }

    private Object[][] createTableDataOfAttendance(List<Student> studentListFromMethod) {

        String[] students = createStudentsList(studentListFromMethod);
        List<Student> studentList = studentListFromMethod;

        String[] dates = getStringDateList();
        List<Date> dateList = getListOfDate();
        sortListDate(dateList);

        Object[][] tableData = new Object[students.length][dateList.size() + 4];
        for (int i = 0; i < studentList.size(); i++) {
            tableData[i][0] = i + 1;
            tableData[i][1] = students[i];
            tableData[i][2] = studentList.get(i).getStudentId();
            tableData[i][3] = String.valueOf(studentList.get(i).getCountOfAttendance());
            if (dates != null) {
                for (int j = 4; j < tableData[0].length; j++) {
                    tableData[i][j] = studentList.get(i).getAttendanceOfStudent().get(dateList.get(j - 4));
                }
            }
        }
        return tableData;
    }

    private Object[][] createTableDataOfPassedOrFailedLabs(List<Student> studentListFromMethod) {

        String[] students = createStudentsList(studentListFromMethod);
        List<Student> studentList = studentListFromMethod;

        String[] labs = getStringLabsList();
        List<String> labsList = getListOfLabs();
        sortLabKeys(labsList);

        Object[][] tableData = new Object[students.length][labsList.size() + 3];
        for (int i = 0; i < studentList.size(); i++) {
            tableData[i][0] = i + 1;
            tableData[i][1] = students[i];
            tableData[i][2] = studentList.get(i).getStudentId();
            if (labs != null) {
                for (int j = 3; j < tableData[0].length; j++) {
                    tableData[i][j] = studentList.get(i).getPassedOrFailedLab().get(labsList.get(j - 3));
                }
            }
        }
        return tableData;
    }

    private String[] createHeaderTableOfAttendance() {
        String[] dates = getStringDateList();
        String[] header;
        if (dates != null) {
            header = new String[dates.length + 4];
            header[0] = "№";
            header[1] = "ФИО";
            header[2] = "Зач.Книга №";
            header[3] = "Кол-во посещений";
            System.arraycopy(dates, 0, header, 4, dates.length);
            return header;
        }
        header = new String[]{"№", "ФИО", "Зач.Книга №", "Кол-во посещений"};
        return header;
    }


    private String[] createHeaderTableOfPassedOrFailedLab() {
        String[] labs = getStringLabsList();
        String[] header;
        if (labs != null) {
            header = new String[labs.length + 3];
            header[0] = "№";
            header[1] = "ФИО";
            header[2] = "Зач.Книга №";
            System.arraycopy(labs, 0, header, 3, labs.length);
            return header;
        }
        header = new String[]{"№", "ФИО", "Зач.Книга №"};
        return header;
    }


    private String[] createStudentsList(List<Student> studentList) {
        String[] students = new String[studentList.size()];
        int i = 0;
        for (Student student : studentList) {
            students[i] =
                    student.getLastName() + " " +
                            student.getFirstName() + " " +
                            student.getPatronymic();
            i++;
        }
        return students;
    }

    private void sortListDate(List<Date> list) {
        list.sort(new Comparator<Date>() {
            @Override
            public int compare(Date o1, Date o2) {
                return o1.compareTo(o2);
            }
        });
    }

    private void sortLabKeys(List<String> labs) {
        labs.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
    }

    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    private void setAddDateToLabView() {
        jpnlAddDateToLab.removeAll();

        JLabel jlblAddNewDateToSubject = new JLabel("Добавление даты занятия");
        jlblAddNewDateToSubject.setBounds(20, 40, 300, 30);
        jlblAddNewDateToSubject.setFont(getCourierFont(20, Font.PLAIN));

        JDatePickerImpl calendar = createDatePicker();
        calendar.setSize(300, 30);
        calendar.setBounds(20, 80, 300, 30);

        calendar.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        JButton jbtAddDateToLab = new JButton("Добавить дату занятия");
        jbtAddDateToLab.setBounds(20, 120, 300, 60);
        jbtAddDateToLab.setFont(getCourierFont(20, Font.PLAIN));
        jbtAddDateToLab.setFocusPainted(false);
        jbtAddDateToLab.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                Date newDate = (Date) calendar.getModel().getValue();
                if (newDate == null) {
                    JOptionPane.showMessageDialog(MainWindow.this, "Пожалуйста, выберите дату!", "Предупреждение", JOptionPane.ERROR_MESSAGE);
                    calendar.setBorder(BorderFactory.createLineBorder(Color.red, 1));
                } else {
                    newDate = dateFormat.parse(dateFormat.format(newDate));
                    addDateToSubject(newDate);
                    if (isAddedDate) {
                        JOptionPane.showMessageDialog(MainWindow.this, "Дата " + dateFormat.format(newDate) + " была добавлена", "Успех", JOptionPane.INFORMATION_MESSAGE);
                    }
                    calendar.setBorder(null);
                    setAddDateToLabView();
                    isAttendance = true;
                    setSortButtons();
                    setStudentsTable(currentlyGroup.getStudents(), isAttendance);

                }
            }
        });
        if (currentlyGroup != null && currentlyGroupId != null && !currentlyGroup.getStudents().get(0).getAttendanceOfStudent().isEmpty()) {
            JLabel deleteDateOfSubject = new JLabel("Удаление даты занятия");
            deleteDateOfSubject.setFont(getCourierFont(20, Font.PLAIN));
            deleteDateOfSubject.setBounds(20, 200, 300, 30);

            String[] listDates = getStringDateList();
            if (listDates != null) {
                JComboBox<String> jComboBoxListDates = new JComboBox<>(listDates);
                jComboBoxListDates.setBounds(20, 250, 300, 50);
                jComboBoxListDates.setFont(getCourierFont(20, Font.BOLD));
                jComboBoxListDates.setSelectedItem(null);

                JButton jbtDeleteDatToLab = new JButton("Удалить дату занятия");
                jbtDeleteDatToLab.setFont(getCourierFont(20, Font.PLAIN));
                jbtDeleteDatToLab.setBounds(20, 330, 300, 60);
                jbtDeleteDatToLab.setFocusPainted(false);
                jbtDeleteDatToLab.addActionListener(new ActionListener() {
                    /**
                     * Invoked when an action occurs.
                     */
                    @SneakyThrows
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (jComboBoxListDates.getSelectedItem() == null) {
                            JOptionPane.showMessageDialog(MainWindow.this, "Пожалуйста, выберите дату", "Предупреждение", JOptionPane.WARNING_MESSAGE);
                        } else {
                            studentController.deleteDateToSubject(dateFormat.parse((String) jComboBoxListDates.getSelectedItem()), currentlyGroup.getStudents());
                            JOptionPane.showMessageDialog(MainWindow.this, "Дата была удалена", "Успех", JOptionPane.INFORMATION_MESSAGE);
                            setSortButtons();
                            isAttendance = true;
                            setStudentsTable(currentlyGroup.getStudents(), isAttendance);
                            setAddDateToLabView();
                        }
                    }
                });
                jpnlAddDateToLab.add(deleteDateOfSubject);
                jpnlAddDateToLab.add(jbtDeleteDatToLab);
                jpnlAddDateToLab.add(jComboBoxListDates);
            }
        }

        JLabel jlblAddLabToGroup = new JLabel("Добавление лабораторной");
        jlblAddLabToGroup.setFont(getCourierFont(20, Font.PLAIN));
        jlblAddLabToGroup.setBounds(520, 40, 300, 30);

        JButton jbtAddLabToGroup = new JButton("Добавить лабораторную");
        jbtAddLabToGroup.setFocusPainted(false);
        jbtAddLabToGroup.setFont(getCourierFont(20, Font.PLAIN));
        jbtAddLabToGroup.setBounds(520, 120, 300, 60);
        jbtAddLabToGroup.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                studentController.addLabForStudent(currentlyGroup.getStudents());
                JOptionPane.showMessageDialog(MainWindow.this, "Лабораторная работа была добавлена", "Успех", JOptionPane.INFORMATION_MESSAGE);
                setAddDateToLabView();
                isAttendance = false;
                setStudentsTable(currentlyGroup.getStudents(), isAttendance);
                setSortButtons();
            }
        });

        if (currentlyGroup != null && currentlyGroupId != null && !currentlyGroup.getStudents().get(0).getPassedOrFailedLab().isEmpty()) {
            JLabel jlblDeleteLabToGroup = new JLabel("Удаление лабораторной работы");
            jlblDeleteLabToGroup.setFont(getCourierFont(20, Font.PLAIN));
            jlblDeleteLabToGroup.setBounds(520, 200, 300, 30);

            String[] listLabs = getStingKeysOfLabs();
            if (listLabs != null) {
                JComboBox<String> jComboBoxListLabs = new JComboBox<>(listLabs);
                jComboBoxListLabs.setFont(getCourierFont(20, Font.PLAIN));
                jComboBoxListLabs.setBounds(520, 250, 300, 50);
                jComboBoxListLabs.setSelectedItem(null);


                JButton jbtDeleteLabToGroup = new JButton("Удалить лабораторную");
                jbtDeleteLabToGroup.setFocusPainted(false);
                jbtDeleteLabToGroup.setFont(getCourierFont(20, Font.PLAIN));
                jbtDeleteLabToGroup.setBounds(520, 330, 300, 60);
                jbtDeleteLabToGroup.addActionListener(new ActionListener() {
                    /**
                     * Invoked when an action occurs.
                     */
                    @SneakyThrows
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (jComboBoxListLabs.getSelectedItem() == null) {
                            JOptionPane.showMessageDialog(MainWindow.this, "Пожалуйста, выберите лабораторную работу", "Предупреждение", JOptionPane.WARNING_MESSAGE);
                        } else {
                            studentController.deleteLabForStudent(currentlyGroup.getStudents(), (String) jComboBoxListLabs.getSelectedItem());
                            JOptionPane.showMessageDialog(MainWindow.this, "Лабораторная была удалена", "Успех", JOptionPane.INFORMATION_MESSAGE);
                            isAttendance = false;
                            if (currentlyGroup.getStudents().get(0).getPassedOrFailedLab().isEmpty()) {
                                isAttendance = true;
                            }
                            setAddDateToLabView();
                            setStudentsTable(currentlyGroup.getStudents(), isAttendance);
                            setSortButtons();
                        }
                    }
                });
                jpnlAddDateToLab.add(jbtDeleteLabToGroup);
                jpnlAddDateToLab.add(jComboBoxListLabs);
                jpnlAddDateToLab.add(jlblDeleteLabToGroup);
            }
        }


        jpnlAddDateToLab.add(jbtAddDateToLab);
        jpnlAddDateToLab.add(jlblAddLabToGroup);
        jpnlAddDateToLab.add(jbtAddLabToGroup);

        setListDates();
        jpnlAddDateToLab.add(calendar);
        jpnlAddDateToLab.add(jlblAddNewDateToSubject);
        jpnlAddDateToLab.repaint();
        jpnlAddDateToLab.revalidate();
    }

    private void setListDates() {
        String[] listDates = getStringDateList();
        if (listDates != null) {
            JList<String> listOfDates = new JList<>(listDates);
            listOfDates.setFont(getCourierFont(20, Font.PLAIN));
            JScrollPane jScrollPane = new JScrollPane(listOfDates, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane.setBounds(350, 40, 150, 400);
            jScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            jpnlAddDateToLab.add(jScrollPane);
        }
    }

    private boolean isDateWasAddedToSubject(Date dateOfLab) {
        List<Date> listOfDates = new ArrayList<>(currentlyGroup.getStudents().get(0).getAttendanceOfStudent().keySet());
        if (listOfDates.isEmpty()) {
            return false;
        }
        return listOfDates.contains(dateOfLab);
    }

    private void addDateToSubject(Date dateOfLab) throws ParseException {
        if (dateOfLab.before(dateFormat.parse(dateFormat.format(new Date())))) {
            JOptionPane.showMessageDialog(MainWindow.this, "Нельзя добавить дату, раньше сегодняшнего дня", "Предупреждение", JOptionPane.WARNING_MESSAGE);
            isAddedDate = false;
        } else if (!isDateWasAddedToSubject(dateOfLab)) {
            studentController.addDateToSubject(dateOfLab, currentlyGroup.getStudents());
            isAddedDate = true;
        } else {
            JOptionPane.showMessageDialog(MainWindow.this, "Дата " + dateFormat.format(dateOfLab) + " занятия была уже добавлена, пожалуйста, проверьте список дат", "Предупреждение", JOptionPane.WARNING_MESSAGE);
            isAddedDate = false;
        }
    }

    private String[] getStingKeysOfLabs() {

        List<String> listOfLabsKeys = new ArrayList<>(currentlyGroup.getStudents().get(0).getPassedOrFailedLab().keySet());
        sortLabKeys(listOfLabsKeys);
        String[] result = new String[listOfLabsKeys.size()];
        if (listOfLabsKeys.isEmpty()) {
            return null;
        } else {
            for (int i = 0; i < listOfLabsKeys.size(); i++) {
                result[i] = listOfLabsKeys.get(i);
            }
            return result;
        }
    }
}


