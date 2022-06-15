import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Vector;

/**
 * Класс Window. Основная часть программы, создает окно программы, которое содержит весь необходимый контент.
 */
public class Window extends JFrame {

    /**Вектор, содержащий все спектакли**/
    private Vector<Spectacle> spectacleVector = new Vector<>(0);
    /**Вектор, содержащий названия спектаклей для списка Jlist**/
    private final Vector<String> spectTitles = new Vector<>(0);
    /**Вектор, содержащий всех актеров**/
    private Vector<Actor> actorVector = new Vector<>(0);
    /**Вектор, содержащий имена актеров для списка Jlist**/
    private final Vector<String> actNames = new Vector<>(0);
    /**Вектор, содержащий всех работников**/
    private Vector<Employee> employeeVector = new Vector<>(0);
    /**Вектор, содержащий имена работников для списка Jlist**/
    private final Vector<String> empNames = new Vector<>(0);

    /**
     * Создание нового окна.
     */
    Window() {
        // Выбор стиля всей программы из системы с обработкой исключений
        try {
            javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex) {
            System.out.println(ex);
        }

        // Чтение данных из файлов
        readData();
        // Заголовок окна
        setTitle("Teatru");
        // Рарешить изменять размер окна
        setResizable(true);
        // Минимальный размер окна
        setMinimumSize(new Dimension(640, 480));
        // Начальный размер окна
        setSize(900, 700);

        // Добавление всех компонентов в окно
        setComponents();

        // Создание диалогового окна при закрытии
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Save changes?", "Exit Program Message Box",
                        JOptionPane.YES_NO_CANCEL_OPTION);

                // Если пользователь хочет сохранить изменения
                if (confirmed == JOptionPane.YES_OPTION) {
                    // Запись данных в файл
                    writeData();
                    // Закрытие окна
                    dispose();
                }
                // Иначе
                else if (confirmed == JOptionPane.NO_OPTION)
                    // Закрытие окна
                    dispose();
            }
        });

        // Появление окна в центре экрана
        setLocationRelativeTo(null);
        // Сделать окно видимым
        setVisible(true);
    }

    /**
     * Метод setComponents создает весь основной контент внутри окна.
     */
    private void setComponents() {
        // Заполнение названий спектаклей для JList
        if (!spectacleVector.isEmpty())
            for (Spectacle el : spectacleVector)
                spectTitles.add(el.getTitle());

        // Заполнение имен актеров для JList
        if (!actorVector.isEmpty())
            for (Actor el : actorVector)
                actNames.add(el.getName() + " " + el.getSurname());

        // Заполнение имен работников для JList
        if (!employeeVector.isEmpty())
            for (Employee el : employeeVector)
                empNames.add(el.getName() + " " + el.getSurname());

        // Создание панели спектаклей
        JPanel spectacles = new JPanel();
        // Установка контента в панели
        SpectaclePanel.setSpectaclePanel(spectacles,
                spectacleVector, spectTitles,
                actorVector, actNames,
                employeeVector, empNames);

        // Создание панели актеров
        JPanel actors = new JPanel();
        // Установка контента в панели
        ActorPanel.setActorPanel(actors,
                spectacleVector, spectTitles,
                actorVector, actNames);

        // Создание панели работников
        JPanel employees = new JPanel();
        // Установка контента в панели
        EmployeePanel.setEmployeePanel(employees,
                spectacleVector,
                employeeVector, empNames);

        // Верхнее меню
        JTabbedPane tabs = new JTabbedPane();
        // Добавление панелей в меню
        tabs.addTab("Spectacles", spectacles);
        tabs.addTab("Actors", actors);
        tabs.addTab("Employees", employees);
        add(tabs);
    }

    /**
     * Метод readData читает данные программы из файлов посредством сериализации
     */
    private void readData() {
        // Обработка исключений
        try {
            // Чтение списка спектаклей
            FileInputStream fileInputStream = new FileInputStream("spectacles.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            spectacleVector = (Vector<Spectacle>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();

            // Чтение списка актеров
            fileInputStream = new FileInputStream("actors.ser");
            objectInputStream = new ObjectInputStream(fileInputStream);
            actorVector = (Vector<Actor>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();

            // Чтение списка работников
            fileInputStream = new FileInputStream("employee.ser");
            objectInputStream = new ObjectInputStream(fileInputStream);
            employeeVector = (Vector<Employee>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        }
        // Если брошено исключение
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Метод writeData записывает данные программы в файлы
     */
    private void writeData() {
        // Обработка исключений
        try {
            // Запись списка спектаклей
            FileOutputStream outputStream = new FileOutputStream("spectacles.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(spectacleVector);
            objectOutputStream.close();
            outputStream.close();

            // Запись списка актреов
            outputStream = new FileOutputStream("actors.ser");
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(actorVector);
            objectOutputStream.close();
            outputStream.close();

            // Запись списка работников
            outputStream = new FileOutputStream("employee.ser");
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(employeeVector);
            objectOutputStream.close();
            outputStream.close();
        }
        // Если брошено исключение
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
