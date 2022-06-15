import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Vector;

/**
 * Абстрактный класс, устанавливающий контент в панель спектаклей. Наследует класс Window.
 */
public abstract class SpectaclePanel extends Window {
    /**
     * Создает основной контент панели для спекталей.
     *
     * @param spectacles      the spectacles
     * @param spectacleVector the spectacle vector
     * @param spectTitles     the spectacle titles
     * @param actorVector     the actor vector
     * @param actNames        the act names
     * @param employeeVector  the employee vector
     * @param empNames        the emp names
     */
    public static void setSpectaclePanel(JPanel spectacles,
                                         Vector<Spectacle> spectacleVector, Vector<String> spectTitles,
                                         Vector<Actor> actorVector, Vector<String> actNames,
                                         Vector<Employee> employeeVector, Vector<String> empNames) {
        spectacles.setLayout(new BorderLayout());

        // Панель, содержащая контент для каждого отдельного спектакля
        JPanel spectacleContent = new JPanel();
        // Полоса прокрутки при переполнении панели
        JScrollPane scrollPane = new JScrollPane(spectacleContent,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        // Добавление панели справа
        spectacles.add(scrollPane, BorderLayout.CENTER);

        // Создание списка всех спектаклей слева
        JList<String> spectacleList = new JList<>(spectTitles);
        spectacleList.setVisibleRowCount(20);
        spectacleList.setFixedCellHeight(15);
        spectacleList.setFixedCellWidth(100);
        // Создание полосы прокрутки при переполнении списка
        JScrollPane listScroll = new JScrollPane(spectacleList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Действия при выборе элемента из списка
        spectacleList.addListSelectionListener(e -> {
            // При выборе элемента
            if (!e.getValueIsAdjusting()) {
                // Заполнение правой панели
                setSpectacleContent(spectacleContent,
                        spectacleVector.elementAt(spectacleList.getSelectedIndex()),
                        spectacleVector,
                        spectTitles,
                        actorVector,
                        actNames,
                        employeeVector,
                        empNames);
            }
        });
        // Добавление списка слева
        spectacles.add(listScroll, BorderLayout.WEST);

        // Нижняя панель с кнопками для добавления и удаления спектаклей
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        // Создание кнопок
        JButton plusButton = new JButton("+"), minusButton = new JButton("-");
        plusButton.setMargin(new Insets(5, 5, 5, 5));
        minusButton.setMargin(new Insets(5, 5, 5, 5));

        // Действия при нажатии кнопки "+"
        plusButton.addActionListener(e -> {
            // Панель для диалогового окна
            JPanel plusPanel = new JPanel(new GridLayout(4, 2));
            // Поле ввода названия
            plusPanel.add(new JLabel("Title:"));
            JTextField tField = new JTextField(10);
            plusPanel.add(tField);
            // Поле ввода места проведения
            plusPanel.add(new JLabel("Location:"));
            JTextField lField = new JTextField(10);
            plusPanel.add(lField);
            // Поле ввода гонорара
            plusPanel.add(new JLabel("Fee:"));
            JTextField fField = new JTextField(10);
            TextFieldsMethods.makeTfFloat(fField);
            plusPanel.add(fField);

            // Создание диалогового окна
            JOptionPane.showConfirmDialog(
                    null,
                    plusPanel,
                    "Create new spectacle",
                    JOptionPane.OK_CANCEL_OPTION
            );
            // Если пользователь ввел все верно
            if (tField.getText() != null && tField.getText().length() > 0 &&
                    lField.getText() != null && lField.getText().length() > 0 &&
                    fField.getText() != null && fField.getText().length() > 0) {
                // Добавление нового спектакля
                spectacleVector.add(new Spectacle(tField.getText(), lField.getText(),
                        Float.parseFloat(fField.getText())));
                // Добаление спектакля в список слева
                spectTitles.add(tField.getText());
                // Действия для корректного отображения панели справа
                spectacleList.updateUI();
                if (spectTitles.size() > 1)
                    spectacleList.setSelectedIndex(spectTitles.indexOf(spectTitles.lastElement()));
                else {
                    try {
                        spectacleList.clearSelection();
                    } catch (Exception ex) {
                        System.out.println("Selection cleared!");
                    }
                }
            }
        });

        // Действия при нажатии кнопки "-"
        minusButton.addActionListener(e -> {
            // Елси в списке не выделен спектакль, то нечего удалять
            if (!spectacleList.isSelectionEmpty()) {
                // Диалоговое окно для подтверждения удаления
                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Are you sure to delete \"" + spectacleList.getSelectedValue() + "\"?",
                        "Delete Confirmation Message Box",
                        JOptionPane.YES_NO_OPTION);

                // Пользователь подтвердил удаление
                if (confirmed == JOptionPane.YES_OPTION) {
                    // Удаление спектакля из списков
                    spectacleVector.remove(spectacleList.getSelectedIndex());
                    spectTitles.remove(spectacleList.getSelectedIndex());

                    // Очистка панели справа и выделения в списке
                    try {
                        spectacleList.clearSelection();
                    } catch (Exception ex) {
                        System.out.println("Selection cleared!");
                    }
                    spectacleList.updateUI();
                    spectacleContent.removeAll();
                    spectacleContent.updateUI();
                }
            }
        });

        // Добавление кнопок
        addPanel.add(plusButton);
        addPanel.add(minusButton);
        // Добавление нижней панели
        spectacles.add(addPanel, BorderLayout.SOUTH);
    }

    /**
     * Установка контента в правой панели для выбранного спектакля.
     *
     * @param spectacleVector the spectacle vector
     * @param spectTitles     the spect titles
     * @param actorVector     the actor vector
     * @param actNames        the act names
     * @param employeeVector  the employee vector
     * @param empNames        the emp names
     */
    private static void setSpectacleContent(JPanel spectacleContent,
                                            Spectacle spectacle,
                                            Vector<Spectacle> spectacleVector, Vector<String> spectTitles,
                                            Vector<Actor> actorVector, Vector<String> actNames,
                                            Vector<Employee> employeeVector, Vector<String> empNames) {
        // Предварительная очистка панели
        spectacleContent.removeAll();

        spectacleContent.setLayout(new BorderLayout());

        // Заголовок - название спектакля
        JLabel tLabel = new JLabel(spectacle.getTitle());
        tLabel.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD | Font.ITALIC, 32));

        // Подсчет бюджета
        spectacle.calcBudget();
        // Панель, показывающая бюджет спектакля
        JLabel budgetLabel = new JLabel("Budget: " + spectacle.getBudget(), SwingConstants.CENTER);
        budgetLabel.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 18));

        // Основные поля спектакля
        JPanel generalFields = new JPanel(new FlowLayout(FlowLayout.CENTER));
        generalFields.add(new JLabel("Title:"));
        JTextField titleField = new JTextField(15);
        titleField.setText(spectacle.getTitle());
        // Действия при изменении содержания текстового поля
        titleField.getDocument().addDocumentListener(new DocumentListener() {
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
                // Если поле не пустое
                if (titleField.getText().length() > 0 && titleField.getText() != null) {
                    // Установить новое название
                    spectacle.setTitle(titleField.getText());
                    spectTitles.setElementAt(titleField.getText(), spectacleVector.indexOf(spectacle));
                } else {
                    // Установить пустое название
                    spectacle.setTitle("");
                    spectTitles.setElementAt("", spectacleVector.indexOf(spectacle));
                }
                // Обновление заголовка
                tLabel.setText(spectacle.getTitle());
            }
        });
        generalFields.add(titleField);

        generalFields.add(new JLabel("Location:"));
        JTextField locationField = new JTextField(15);
        locationField.setText(spectacle.getLocation());
        // Действия при изменении содержания текстового поля
        locationField.getDocument().addDocumentListener(new DocumentListener() {
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
                // Если поле не пустое
                if (locationField.getText().length() > 0 && locationField.getText() != null) {
                    // Устанока нового места
                    spectacle.setLocation(locationField.getText());
                } else
                    // Пустое поле
                    spectacle.setLocation("");
            }
        });
        generalFields.add(locationField);

        generalFields.add(new JLabel("Fee:"));
        JTextField feeField = new JTextField(15);
        feeField.setText(String.valueOf(spectacle.getFee()));
        TextFieldsMethods.makeTfFloat(feeField);
        // Действия при изменении содержания текстового поля
        feeField.getDocument().addDocumentListener(new DocumentListener() {
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
                // Если поле не пустое
                if (feeField.getText().length() > 0 && feeField.getText() != null) {
                    // Установка нового гонорара
                    spectacle.setFee(Float.parseFloat(feeField.getText()));
                } else
                    // Установка нулевого гонорара
                    spectacle.setFee(0.0f);
                // Переподсчет бюджета
                spectacle.calcBudget();
                // Обновление текста с бюджетом
                budgetLabel.setText("Budget: " + spectacle.getBudget());
            }
        });
        generalFields.add(feeField);

        // Верхняя панель содержит заголовок, поля ввода и бюджет спектакля
        JPanel topPanel = new JPanel(new GridLayout(0, 1));
        // Добавление элементов в верхнюю панель
        tLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(tLabel);
        topPanel.add(generalFields);
        topPanel.add(budgetLabel);

        // Добавление верхней панели
        spectacleContent.add(topPanel, BorderLayout.NORTH);

        // Центральная панель содержит списки актеров и работников, а так же взаимодействие с ними
        JPanel centerPanel = new JPanel(new GridLayout(0, 1));

        // Панель актеров
        JPanel actorsPanel = new JPanel();
        actorsPanel.setLayout(new BoxLayout(actorsPanel, BoxLayout.Y_AXIS));

        // Рамка для панели
        actorsPanel.setBorder(BorderFactory.createEtchedBorder());

        // Заголовок панели
        JLabel aLabel = new JLabel("Actors:", SwingConstants.CENTER);
        aLabel.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 16));

        // Кнопка добавления актера в спектакль
        JButton addActorButton = new JButton("+");
        // Действие при нажатии на кнопку
        addActorButton.addActionListener(e -> {
            // Панель для диалогового окна
            JPanel plusPanel = new JPanel(new GridLayout(2, 2));
            // Панель выбора актера
            plusPanel.add(new JLabel("Actor:"));
            JComboBox<String> aComboBox = new JComboBox<>(actNames);
            plusPanel.add(aComboBox);
            // Панель ввода роли
            plusPanel.add(new JLabel("Role:"));
            JTextField rField = new JTextField("", 10);
            plusPanel.add(rField);

            // Если актеров нет, то некого добавлять
            if (!actorVector.isEmpty()) {
                // Диалоговое окно
                JOptionPane.showConfirmDialog(
                        null,
                        plusPanel,
                        "Add actor to spectacle",
                        JOptionPane.OK_CANCEL_OPTION
                );
                // В спектале не может быть дважды один и тот же актер
                if (isActorUnique(spectacle, actorVector.elementAt(aComboBox.getSelectedIndex()))) {
                    // Добавление актера в спектакль
                    spectacle.getActors().add(new Role(actorVector.elementAt(aComboBox.getSelectedIndex()),
                            rField.getText()));
                    // Создание панели с актером в панели спектакля
                    addActorSpectacle(actorsPanel, spectacle.getActors().lastElement(),
                            spectacle,
                            budgetLabel,
                            actorVector,
                            actNames);
                    // Обновление панели
                    actorsPanel.updateUI();
                }
                // Переподсчет бюджета
                spectacle.calcBudget();
                budgetLabel.setText("Budget: " + spectacle.getBudget());
            }
        });

        // Панель для заголовка списка актеров в спектакле
        JPanel actorTitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actorTitlePanel.setPreferredSize(new Dimension(600, 40));
        actorTitlePanel.setMaximumSize(actorTitlePanel.getPreferredSize());
        actorTitlePanel.add(addActorButton);
        actorTitlePanel.add(aLabel);

        // Добавление панели заголовка списка актеров в спектакле
        actorsPanel.add(actorTitlePanel);

        // Добавление уже играющих в спектакле актеров
        for (Role el : spectacle.getActors()) {
            addActorSpectacle(actorsPanel, el, spectacle, budgetLabel, actorVector, actNames);
        }

        // Добавление панели актеров в центральную панель
        centerPanel.add(actorsPanel);

        /// Все то же самое для панели со списком работников
        JPanel employeesPanel = new JPanel();
        employeesPanel.setLayout(new BoxLayout(employeesPanel, BoxLayout.Y_AXIS));

        employeesPanel.setBorder(BorderFactory.createEtchedBorder());

        JLabel eLabel = new JLabel("Employees:", SwingConstants.CENTER);
        eLabel.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 16));

        JButton addEmployeeButton = new JButton("+");
        addEmployeeButton.addActionListener(e -> {
            JPanel plusPanel = new JPanel(new GridLayout(2, 2));
            plusPanel.add(new JLabel("Employee:"));
            JComboBox<String> eComboBox = new JComboBox<>(empNames);
            plusPanel.add(eComboBox);

            if (!employeeVector.isEmpty()) {
                JOptionPane.showConfirmDialog(
                        null,
                        plusPanel,
                        "Add employee to spectacle",
                        JOptionPane.OK_CANCEL_OPTION
                );
                if (isEmployeeUnique(spectacle, employeeVector.elementAt(eComboBox.getSelectedIndex()))) {
                    spectacle.getEmployees().add(new Worker(employeeVector.elementAt(eComboBox.getSelectedIndex())));
                    addEmployeeSpectacle(employeesPanel,
                            spectacle.getEmployees().lastElement(),
                            spectacle,
                            budgetLabel,
                            employeeVector,
                            empNames);
                    employeesPanel.updateUI();
                    spectacle.calcBudget();
                    budgetLabel.setText("Budget: " + spectacle.getBudget());
                }
            }
        });

        JPanel employeeTitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        employeeTitlePanel.setPreferredSize(new Dimension(600, 40));
        employeeTitlePanel.setMaximumSize(actorTitlePanel.getPreferredSize());
        employeeTitlePanel.add(addEmployeeButton);
        employeeTitlePanel.add(eLabel);

        employeesPanel.add(employeeTitlePanel);

        for (Worker el : spectacle.getEmployees()) {
            addEmployeeSpectacle(employeesPanel, el, spectacle, budgetLabel, employeeVector, empNames);
        }

        centerPanel.add(employeesPanel);

        // Добавление центральной панели
        spectacleContent.add(centerPanel, BorderLayout.CENTER);

        // Обновление панели с информацией по спектаклю
        spectacleContent.updateUI();
    }

    /**
     * Создание панели актера, играющего в спектакле
     *
     * @param actorsPanel the actors panel
     * @param actor       the actor
     * @param spectacle   the spectacle
     * @param budgetLabel the budget label
     * @param actorVector the actor vector
     * @param actNames    the act names
     */
    private static void addActorSpectacle(JPanel actorsPanel,
                                          Role actor, Spectacle spectacle,
                                          JLabel budgetLabel,
                                          Vector<Actor> actorVector, Vector<String> actNames) {
        // Основная панель
        JPanel aPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // Установка размеров
        aPanel.setPreferredSize(new Dimension(600, 40));
        aPanel.setMaximumSize(aPanel.getPreferredSize());

        // Выпадающий список с актерами
        JComboBox<String> aComboBox = new JComboBox<>(actNames);
        int ind = 0;
        for (Actor el : actorVector)
            if (el.getIdnp() == actor.getActor().getIdnp()) ind = actorVector.indexOf(el);
        aComboBox.setSelectedItem(actNames.elementAt(ind));
        // Изменение актера
        aComboBox.addActionListener(e -> {
            // В спектакле не может быть два раза один и тот же актер
            if (isActorUnique(spectacle, actorVector.elementAt(aComboBox.getSelectedIndex()))) {
                actor.setActor(actorVector.elementAt(aComboBox.getSelectedIndex()));
            } else {
                int indx = 0;
                for (Actor el : actorVector)
                    if (el.getIdnp() == actor.getActor().getIdnp()) indx = actorVector.indexOf(el);
                aComboBox.setSelectedItem(actNames.elementAt(indx));
            }
        });

        // Панель ввода роли актера
        JTextField aTextField = new JTextField(15);
        aTextField.setText(actor.getRole());
        // Действия при изменении введенного значения
        aTextField.getDocument().addDocumentListener(new DocumentListener() {
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
                // Если поле не пустое
                if (aTextField.getText().length() > 0 && aTextField.getText() != null) {
                    // Установить новое значение
                    actor.setRole(aTextField.getText());
                } else
                    // Установить пустое значение
                    actor.setRole("");
            }
        });

        // Кнопка удаления актера из спектакля
        JButton aMinusButton = new JButton("-");
        // Действия при нажатии на кнопку
        aMinusButton.addActionListener(e -> {
            // Удаление панели
            actorsPanel.remove(aPanel);
            // Удаление актера из спектакля
            spectacle.getActors().remove(actor);
            // Переподсчет бюджета
            spectacle.calcBudget();
            budgetLabel.setText("Budget: " + spectacle.getBudget());
            // Обновление панели
            actorsPanel.updateUI();
        });

        // Добавление элементов
        aPanel.add(aMinusButton);
        aPanel.add(new JLabel("Actor:"));
        aPanel.add(aComboBox);
        aPanel.add(Box.createHorizontalStrut(25));
        aPanel.add(new JLabel("Role:"));
        aPanel.add(aTextField);

        // Добавление панели в панель списка актеров
        actorsPanel.add(aPanel);
        // Обновление панели
        actorsPanel.updateUI();
    }

    /**
     * Создание панели работника, участвующего в спектакле.
     * Комментарии смотреть в методе addActorSpectacle, принцип тот же.
     *
     * @param employeesPanel the employee panel
     * @param employee the employee
     * @param spectacle the spectacle
     * @param budgetLabel the budget label
     * @param employeeVector the employee vector
     * @param empNames the emp names
     */
    private static void addEmployeeSpectacle(JPanel employeesPanel,
                                             Worker employee, Spectacle spectacle,
                                             JLabel budgetLabel,
                                             Vector<Employee> employeeVector, Vector<String> empNames) {
        JPanel ePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ePanel.setPreferredSize(new Dimension(600, 40));
        ePanel.setMaximumSize(ePanel.getPreferredSize());

        JLabel eLabel = new JLabel("— " + employee.getEmployee().getJob());

        JComboBox<String> eComboBox = new JComboBox<>(empNames);
        int ind = 0;
        for (Employee el : employeeVector)
            if (el.getIdnp() == employee.getEmployee().getIdnp()) ind = employeeVector.indexOf(el);
        eComboBox.setSelectedItem(empNames.elementAt(ind));
        eComboBox.addActionListener(e -> {
            if (isEmployeeUnique(spectacle, employeeVector.elementAt(eComboBox.getSelectedIndex()))) {
                employee.setEmployee(employeeVector.elementAt(eComboBox.getSelectedIndex()));
                eLabel.setText("– " + employee.getEmployee().getJob());
            }
            else {
                int indx = 0;
                for (Employee el : employeeVector)
                    if (el.getIdnp() == employee.getEmployee().getIdnp()) indx = employeeVector.indexOf(el);
                eComboBox.setSelectedItem(empNames.elementAt(indx));
            }
        });

        JButton eMinusButton = new JButton("-");
        eMinusButton.addActionListener(e -> {
            employeesPanel.remove(ePanel);
            spectacle.getEmployees().remove(employee);
            spectacle.calcBudget();
            budgetLabel.setText("Budget: " + spectacle.getBudget());
            employeesPanel.updateUI();
        });

        ePanel.add(eMinusButton);
        ePanel.add(eComboBox);
        ePanel.add(Box.createHorizontalStrut(25));
        ePanel.add(eLabel);

        employeesPanel.add(ePanel);
        employeesPanel.updateUI();
    }

    /**
     * Проверка на уникальность актера в спектакле
     *
     * @param spectacle the spectacle
     * @param actor the actor
     * @return is actor unique
     */
    private static boolean isActorUnique(Spectacle spectacle, Actor actor) {
        for (Role val : spectacle.getActors())
            if (val.getActor().getIdnp() == actor.getIdnp())
                return false;
        return true;
    }

    /**
     * Проверка на уникальность работника в спектакле
     *
     * @param spectacle the spectacle
     * @param employee the employee
     * @return is employee unique
     */
    private static boolean isEmployeeUnique(Spectacle spectacle, Employee employee) {
        for (Worker val : spectacle.getEmployees())
            if (val.getEmployee().getIdnp() == employee.getIdnp())
                return false;
        return true;
    }
}
