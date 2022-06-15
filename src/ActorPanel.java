import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 * Абстрактный класс, устанавливающий контент в панель актеров. Наследует класс Window.
 */
public abstract class ActorPanel extends Window {
    /**
     * Устанавливает контент в панели актеров.
     *
     * @param actors          the actors
     * @param spectacleVector the spectacle vector
     * @param spectTitles     the spectacle titles
     * @param actorVector     the actor vector
     * @param actNames        the actor names
     */
    public static void setActorPanel(JPanel actors,
                                     Vector<Spectacle> spectacleVector, Vector<String> spectTitles,
                                     Vector<Actor> actorVector, Vector<String> actNames) {
        actors.setLayout(new BorderLayout());

        // Список актеров слева
        JList<String> actorList = new JList<>(actNames);
        actorList.setVisibleRowCount(20);
        actorList.setFixedCellHeight(15);
        actorList.setFixedCellWidth(100);

        // Полоса прокрутки для списка слева
        JScrollPane listScroll = new JScrollPane(actorList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Панель справа с контентом для каждого актера
        JPanel actorContent = new JPanel();
        // Полоса прокрутки для панели справа
        JScrollPane scrollPane = new JScrollPane(actorContent,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        actors.add(scrollPane, BorderLayout.CENTER);

        // Действие при выборе элемента в списке слева
        actorList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // Установить контент в панели справа
                setActorContent(actorContent, actorVector.elementAt(actorList.getSelectedIndex()),
                        spectacleVector,
                        actorVector, actNames);
            }
        });
        // Добавление списка в панель
        actors.add(listScroll, BorderLayout.WEST);

        // Нижняя панель с кнопками добавления и удаления актеров
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        // Кнопки добавления и удаления актеров
        JButton plusButton = new JButton("+"), minusButton = new JButton("-");
        plusButton.setMargin(new Insets(5, 5, 5, 5));
        minusButton.setMargin(new Insets(5, 5, 5, 5));

        // Действия на нажатие кнопи "+"
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
            // Длина IDNP должна быть не более 13 символов
            iField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if (iField.getText().length() >= 13 &&
                            e.getKeyChar() != KeyEvent.VK_BACK_SPACE &&
                            e.getKeyChar() != KeyEvent.VK_DELETE)
                        e.consume();
                    for (Actor el : actorVector) {
                        if (iField.getText().equals(String.valueOf(el.getIdnp()))) {
                            e.consume();
                        }
                    }
                }
            });
            TextFieldsMethods.makeTfLong(iField);
            plusPanel.add(iField);

            // Диалоговое окно
            JOptionPane.showConfirmDialog(
                    null,
                    plusPanel,
                    "Add new actor",
                    JOptionPane.OK_CANCEL_OPTION
            );
            // IDNP должен быть уникальным, а поля не пустыми
            if (nField.getText() != null && nField.getText().length() > 0 &&
                    sField.getText() != null && sField.getText().length() > 0 &&
                    iField.getText() != null && iField.getText().length() > 0 &&
                    isIdnpUnique(Long.parseLong(iField.getText()), actorVector)) {
                actorVector.add(new Actor(nField.getText(), sField.getText(),
                        Long.parseLong(iField.getText())));
                actNames.add(nField.getText() + " " + sField.getText());
                actorList.updateUI();
                // Для корректного отображения контента
                if (spectTitles.size() > 1)
                    actorList.setSelectedIndex(actNames.indexOf(actNames.lastElement()));
                else {
                    try {
                        actorList.clearSelection();
                    } catch (Exception ex) {
                        System.out.println("Selection cleared!");
                    }
                }
            }
        });

        // Дейтсвия при нажатии на кнопку "-"
        minusButton.addActionListener(e -> {
            // Диалоговое окно с подтверждением удаления
            if (!actorList.isSelectionEmpty()) {
                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Are you sure to delete \"" + actorList.getSelectedValue() + "\"?",
                        "Delete Confirmation Message Box",
                        JOptionPane.YES_NO_OPTION);

                // Если пользователь подтвердил удаление
                if (confirmed == JOptionPane.YES_OPTION) {
                    for (Spectacle el : spectacleVector) {
                        el.getActors().removeIf(val -> val.getActor().getIdnp() ==
                                actorVector.elementAt(actorList.getSelectedIndex()).getIdnp());
                    }
                    // Удаление элемента
                    actorVector.remove(actorList.getSelectedIndex());
                    actNames.remove(actorList.getSelectedIndex());
                    // Для корректного отображения контента
                    try {
                        actorList.clearSelection();
                    } catch (Exception ex) {
                        System.out.println("Selection cleared!");
                    }
                    actorList.updateUI();
                    actorContent.removeAll();
                    actorContent.updateUI();
                }
            }
        });

        // Добавление кнопок
        addPanel.add(plusButton);
        addPanel.add(minusButton);
        // Добавление нижней панели
        actors.add(addPanel, BorderLayout.SOUTH);
    }

    /**
     * Устанавливает контент панели для каждого актера
     *
     * @param actorContent the actor content
     * @param actor the actor
     * @param spectacleVector the spectacle
     * @param actorVector the actor vector
     * @param actNames the actors name
     */
    private static void setActorContent(JPanel actorContent, Actor actor,
                                        Vector<Spectacle> spectacleVector,
                                        Vector<Actor> actorVector, Vector<String> actNames) {
        // Предварительная очистка панели
        actorContent.removeAll();

        actorContent.setLayout(new BorderLayout());
        // Верхняя панель с заголовком и всеми полями ввода
        JPanel topPanel = new JPanel(new GridLayout(0, 1));
        topPanel.setBorder(new EtchedBorder());

        // Заголовок с именем актера
        JLabel nameLabel = new JLabel(actor.getName() + " " + actor.getSurname());
        nameLabel.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD | Font.ITALIC, 32));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(nameLabel);

        // Следующие панели содержат поля ввода и действия к ним

        // Панель имени
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        namePanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField(15);
        nameField.setText(actor.getName());
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
                    actor.setName(nameField.getText());
                    actNames.setElementAt(nameField.getText() + " " + actor.getSurname(),
                            actorVector.indexOf(actor));
                } else {
                    actor.setName("");
                    actNames.setElementAt("" + " " + actor.getSurname(),
                            actorVector.indexOf(actor));
                }
                nameLabel.setText(actor.getName() + " " + actor.getSurname());
            }
        });
        namePanel.add(nameField);

        // Панель фамилии
        JPanel surnamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        surnamePanel.add(new JLabel("Surname:"));
        JTextField surnameField = new JTextField(15);
        surnameField.setText(actor.getSurname());
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
                    actor.setSurname(surnameField.getText());
                    actNames.setElementAt(actor.getName() + " " + surnameField.getText(),
                            actorVector.indexOf(actor));
                } else {
                    actor.setSurname("");
                    actNames.setElementAt(actor.getName() + " " + "",
                            actorVector.indexOf(actor));
                }
                nameLabel.setText(actor.getName() + " " + actor.getSurname());
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
        idnpField.setText(String.valueOf(actor.getIdnp()));
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
                if (idnpField.getText().length() > 0 && idnpField.getText() != null) {
                    if (isIdnpUnique(Long.parseLong(idnpField.getText()), actorVector)) {
                        idnpField.setForeground(Color.black);
                        actor.setIdnp(Long.parseLong(idnpField.getText()));
                    } else {
                        idnpField.setForeground(Color.red);
                    }
                } else
                    actor.setIdnp(0);
            }
        });
        idnpPanel.add(idnpField);

        // Панель зарплаты
        JPanel salaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        salaryPanel.add(new JLabel("Salary:"));
        JTextField salaryField = new JTextField(15);
        salaryField.setText(String.valueOf(actor.getSalary()));
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
                    actor.setSalary(Float.parseFloat(salaryField.getText()));
                } else
                    actor.setSalary(0);
            }
        });
        salaryPanel.add(salaryField);

        // Панель опыта работы
        JPanel expPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        expPanel.add(new JLabel("Work Expirience:"));
        JTextField expField = new JTextField(15);
        expField.setText(String.valueOf(actor.getWorkExp()));
        TextFieldsMethods.makeTfInt(expField);
        expField.getDocument().addDocumentListener(new DocumentListener() {
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
                if (expField.getText().length() > 0 && expField.getText() != null) {
                    actor.setWorkExp(Integer.parseInt(expField.getText()));
                } else
                    actor.setWorkExp(0);
            }
        });
        expPanel.add(expField);

        // Панель звания и/или наград актера
        JPanel rankPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rankPanel.add(new JLabel("Rank:"));
        JTextField rankField = new JTextField(15);
        rankField.setText(actor.getRank());
        rankField.getDocument().addDocumentListener(new DocumentListener() {
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
                if (rankField.getText().length() > 0 && rankField.getText() != null) {
                    actor.setRank(rankField.getText());
                } else
                    actor.setRank("");
            }
        });
        rankPanel.add(rankField);

        // Панель номера телефона
        JPanel phonePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        phonePanel.add(new JLabel("Phone number:"));
        JTextField phoneField = new JTextField(15);
        phoneField.setText(String.valueOf(actor.getPhone()));
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
                    actor.setPhone(Long.parseLong(phoneField.getText()));
                } else
                    actor.setPhone(0);
            }
        });
        phonePanel.add(phoneField);

        // Панель адреса
        JPanel addressPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addressPanel.add(new JLabel("Address:"));
        JTextField addressField = new JTextField(15);
        addressField.setText(actor.getAddress());
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
                    actor.setAddress(addressField.getText());
                } else
                    actor.setAddress("");
            }
        });
        addressPanel.add(addressField);

        // Добавление элементов в верхнюю панель
        topPanel.add(namePanel);
        topPanel.add(surnamePanel);
        topPanel.add(idnpPanel);
        topPanel.add(salaryPanel);
        topPanel.add(expPanel);
        topPanel.add(rankPanel);
        topPanel.add(phonePanel);
        topPanel.add(addressPanel);

        // Нижняя панель со статистикой актера
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(new EtchedBorder());

        // Заголовок
        JLabel statLabel = new JLabel("Statistics");
        statLabel.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD | Font.ITALIC, 32));
        statLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        bottomPanel.add(statLabel);
        bottomPanel.add(new JLabel(" "));

        // Общаяя сумма заработанных денег актером
        float totalEarned = 0.0f;

        // Добавление статистики актера по всем спектаклям
        for (Spectacle el : spectacleVector) {
            for (Role val : el.getActors()) {
                if (val.getActor().getIdnp() == actor.getIdnp()) {
                    JLabel tmp = new JLabel(el.getTitle() + "\t\t\t — \t\t\tSalary: " + actor.getSalary() +
                            ",\t\t\t Fee: " + el.getFee() + ",\t\t\t Total: " + (actor.getSalary() + el.getFee()));
                    tmp.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                    bottomPanel.add(tmp);
                    totalEarned += actor.getSalary() + el.getFee();
                    break;
                }
            }
        }
        bottomPanel.add(new JLabel(" "));

        // Панель с общей заработанной суммой
        JLabel totalLabel = new JLabel("Total earned: " + totalEarned);
        totalLabel.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 16));
        totalLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        bottomPanel.add(totalLabel);

        // Добавление панелей с контентом
        actorContent.add(topPanel, BorderLayout.NORTH);
        actorContent.add(bottomPanel);

        // Обновление панели
        actorContent.updateUI();
    }

    /**
     * Проверка IDNP на уникальность
     *
     * @param idnp the IDNP
     * @param actorVector the actor vector
     * @return is IDNP unique
     */
    private static boolean isIdnpUnique(long idnp, Vector<Actor> actorVector) {
        for (Actor el : actorVector)
            if (idnp == el.getIdnp())
                return false;
        return true;
    }
}
