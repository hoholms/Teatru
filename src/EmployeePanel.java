import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 * Абстрактный класс, устанавливающий контент в панель работников. Наследует класс Window.
 */
public abstract class EmployeePanel extends Window {
    /**
     * Устанавливает контент в панели работников.
     *
     * @param employees       the employees
     * @param spectacleVector the spectacle vector
     * @param employeeVector  the employee vector
     * @param empNames        the emp names
     */
    public static void setEmployeePanel(JPanel employees,
                                        Vector<Spectacle> spectacleVector,
                                        Vector<Employee> employeeVector, Vector<String> empNames) {
        employees.setLayout(new BorderLayout());

        // Список работников слева
        JList<String> empList = new JList<>(empNames);
        empList.setVisibleRowCount(20);
        empList.setFixedCellHeight(15);
        empList.setFixedCellWidth(100);

        // Полоса прокрутки для списка слева
        JScrollPane listScroll = new JScrollPane(empList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Панель справа с контентом для каждого актера
        JPanel empContent = new JPanel();
        // Полоса прокрутки для панели справа
        JScrollPane scrollPane = new JScrollPane(empContent,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        employees.add(scrollPane, BorderLayout.CENTER);

        // Действие при выборе элемента из списка
        empList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // Установить контент в панели справа
                setEmployeeContent(empContent, employeeVector.elementAt(empList.getSelectedIndex()),
                        employeeVector, empNames);
            }
        });
        // Добавление списка слева
        employees.add(listScroll, BorderLayout.WEST);

        // Нижняя панель с кнопками добавления и удаления работников
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        // Кнопки добавления и удаления работников
        JButton plusButton = new JButton("+"), minusButton = new JButton("-");
        plusButton.setMargin(new Insets(5, 5, 5, 5));
        minusButton.setMargin(new Insets(5, 5, 5, 5));

        // Действия на нажатие кнопки "+"
        plusButton.addActionListener(e -> {
            // Панель для диалогового окна
            JPanel plusPanel = new JPanel(new GridLayout(4, 2));
            plusPanel.add(new JLabel("Name:"));
            JTextField nField = new JTextField(10);
            plusPanel.add(nField);
            plusPanel.add(new JLabel("Surname:"));
            JTextField sField = new JTextField(10);
            plusPanel.add(sField);
            plusPanel.add(new JLabel("IDNP:"));
            JTextField iField = new JTextField(10);
            // IDNP должен быть не длиннее 13 символов
            iField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if (iField.getText().length() >= 13 &&
                            e.getKeyChar() != KeyEvent.VK_BACK_SPACE &&
                            e.getKeyChar() != KeyEvent.VK_DELETE)
                        e.consume();
                    for (Employee el : employeeVector) {
                        if (iField.getText().equals(String.valueOf(el.getIdnp()))) {
                            e.consume();
                        }
                    }
                }
            });
            TextFieldsMethods.makeTfLong(iField);
            plusPanel.add(iField);
            plusPanel.add(new JLabel("Job:"));
            JTextField jField = new JTextField(10);
            plusPanel.add(jField);

            // Диалоговое окно
            JOptionPane.showConfirmDialog(
                    null,
                    plusPanel,
                    "Add new employee",
                    JOptionPane.OK_CANCEL_OPTION
            );
            // IDNP должен быть уникальным, а поля не пустыми
            if (nField.getText() != null && nField.getText().length() > 0 &&
                    sField.getText() != null && sField.getText().length() > 0 &&
                    iField.getText() != null && iField.getText().length() > 0 &&
                    isIdnpUnique(Long.parseLong(iField.getText()), employeeVector)) {
                // Добавление работника
                employeeVector.add(new Employee(jField.getText(), nField.getText(), sField.getText(),
                        Long.parseLong(iField.getText())));
                empNames.add(nField.getText() + " " + sField.getText());
                empList.updateUI();
                // Для корректного отображения контента
                if (empNames.size() > 1)
                    empList.setSelectedIndex(empNames.indexOf(empNames.lastElement()));
                else {
                    try {
                        empList.clearSelection();
                    } catch (Exception ex) {
                        System.out.println("Selrction Cleared!");
                    }
                }
            }
        });

        // Действие при нажатии кнопки  "2"
        minusButton.addActionListener(e -> {
            if (!empList.isSelectionEmpty()) {
                // Диалоговое окно подтверждения удаления
                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Are you sure to delete \"" + empList.getSelectedValue() + "\"?",
                        "Delete Confirmation Message Box",
                        JOptionPane.YES_NO_OPTION);

                // Если пользователь подтвердил удаление
                if (confirmed == JOptionPane.YES_OPTION) {
                    for (Spectacle el : spectacleVector) {
                        el.getEmployees().removeIf(val -> val.getEmployee().getIdnp() ==
                                employeeVector.elementAt(empList.getSelectedIndex()).getIdnp());
                    }
                    // Удаление работника
                    employeeVector.remove(empList.getSelectedIndex());
                    empNames.remove(empList.getSelectedIndex());
                    // Для корректного отображения контента
                    try {
                        empList.clearSelection();
                    } catch (Exception ex) {
                        System.out.println("Selection cleared!");
                    }
                    empList.updateUI();
                    empContent.removeAll();
                    empContent.updateUI();
                }
            }
        });

        // Добавление кнопок
        addPanel.add(plusButton);
        addPanel.add(minusButton);
        // Добавление нижней панели
        employees.add(addPanel, BorderLayout.SOUTH);
    }

    /**
     * Установка контента для панели справа для каждого работника.
     *
     * @param employeeContent the employee content
     * @param employee        the employee
     * @param employeeVector  the employee vector
     * @param empNames        the employee names
     */
    private static void setEmployeeContent(JPanel employeeContent, Employee employee,
                                           Vector<Employee> employeeVector,
                                           Vector<String> empNames) {
        // Предварительная очистка панели
        employeeContent.removeAll();

        employeeContent.setLayout(new BorderLayout());

        // Верхняя панель с заголовком и всеми полями ввода
        JPanel topPanel = new JPanel(new GridLayout(0, 1));

        // Заголовок с именем работника
        JLabel nameLabel = new JLabel(employee.getName() + " " + employee.getSurname());
        nameLabel.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD | Font.ITALIC, 32));
        topPanel.add(nameLabel);

        // Следующие панели содержат все поля ввода для работника

        // Панель имени
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        namePanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField(15);
        nameField.setText(employee.getName());
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                if (nameField.getText().length() > 0 && nameField.getText() != null) {
                    employee.setName(nameField.getText());
                    empNames.setElementAt(nameField.getText() + " " + employee.getSurname(),
                            employeeVector.indexOf(employee));
                } else {
                    employee.setName("");
                    empNames.setElementAt("" + " " + employee.getSurname(),
                            employeeVector.indexOf(employee));
                }
                nameLabel.setText(employee.getName() + " " + employee.getSurname());
            }
        });
        namePanel.add(nameField);

        // Панель фамилии
        JPanel surnamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        surnamePanel.add(new JLabel("Surname:"));
        JTextField surnameField = new JTextField(15);
        surnameField.setText(employee.getSurname());
        surnameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                if (surnameField.getText().length() > 0 && surnameField.getText() != null) {
                    employee.setSurname(surnameField.getText());
                    empNames.setElementAt(employee.getName() + " " + surnameField.getText(),
                            employeeVector.indexOf(employee));
                } else {
                    employee.setSurname("");
                    empNames.setElementAt(employee.getName() + " " + "",
                            employeeVector.indexOf(employee));
                }
                nameLabel.setText(employee.getName() + " " + employee.getSurname());
            }
        });
        surnamePanel.add(surnameField);

        // Панель IDNP
        JPanel idnpPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        idnpPanel.add(new JLabel("IDNP:"));
        JTextField idnpField = new JTextField(15);
        idnpField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (idnpField.getText().length() >= 13 &&
                        e.getKeyChar() != KeyEvent.VK_BACK_SPACE &&
                        e.getKeyChar() != KeyEvent.VK_DELETE)
                    e.consume();
            }
        });
        TextFieldsMethods.makeTfLong(idnpField);
        idnpField.setText(String.valueOf(employee.getIdnp()));
        idnpField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                if (idnpField.getText().length() > 0 &&
                        idnpField.getText() != null) {
                    if (isIdnpUnique(Long.parseLong(idnpField.getText()), employeeVector)) {
                        idnpField.setForeground(Color.black);
                        employee.setIdnp(Long.parseLong(idnpField.getText()));
                    } else {
                        idnpField.setForeground(Color.red);
                    }
                } else
                    employee.setIdnp(0);
            }
        });
        idnpPanel.add(idnpField);

        // Панель зарплаты
        JPanel salaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        salaryPanel.add(new JLabel("Salary:"));
        JTextField salaryField = new JTextField(15);
        salaryField.setText(String.valueOf(employee.getSalary()));
        TextFieldsMethods.makeTfFloat(salaryField);
        salaryField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                if (salaryField.getText().length() > 0 && salaryField.getText() != null) {
                    employee.setSalary(Float.parseFloat(salaryField.getText()));
                } else
                    employee.setSalary(0);
            }
        });
        salaryPanel.add(salaryField);

        // Панель номера телефона
        JPanel phonePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        phonePanel.add(new JLabel("Phone number:"));
        JTextField phoneField = new JTextField(15);
        phoneField.setText(String.valueOf(employee.getPhone()));
        TextFieldsMethods.makeTfLong(phoneField);
        phoneField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                if (phoneField.getText().length() > 0 && phoneField.getText() != null) {
                    employee.setPhone(Long.parseLong(phoneField.getText()));
                } else
                    employee.setPhone(0);
            }
        });
        phonePanel.add(phoneField);

        // Панель адреса
        JPanel addressPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addressPanel.add(new JLabel("Address:"));
        JTextField addressField = new JTextField(15);
        addressField.setText(employee.getAddress());
        addressField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                if (addressField.getText().length() > 0 && addressField.getText() != null) {
                    employee.setAddress(addressField.getText());
                } else
                    employee.setAddress("");
            }
        });
        addressPanel.add(addressField);

        // Панель должности
        JPanel jobPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jobPanel.add(new JLabel("Job:"));
        JTextField jobField = new JTextField(15);
        jobField.setText(employee.getJob());
        jobField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                if (jobField.getText().length() > 0 && jobField.getText() != null) {
                    employee.setJob(jobField.getText());
                } else
                    employee.setJob("");
            }
        });
        jobPanel.add(jobField);

        // Добавление всех элементов
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(nameLabel);
        topPanel.add(namePanel);
        topPanel.add(surnamePanel);
        topPanel.add(idnpPanel);
        topPanel.add(salaryPanel);
        topPanel.add(phonePanel);
        topPanel.add(addressPanel);
        topPanel.add(jobPanel);
        employeeContent.add(topPanel, BorderLayout.NORTH);

        // Обновление панели
        employeeContent.updateUI();
    }

    /**
     * Проверка IDNP на уникальность
     *
     * @param idnp the IDNP
     * @param employeeVector the employee vector
     * @return is IDNP unique
     */
    private static boolean isIdnpUnique(long idnp, Vector<Employee> employeeVector) {
        for (Employee el : employeeVector)
            if (idnp == el.getIdnp())
                return false;
        return true;
    }
}
