import java.io.Serializable;

/**
 * Дополнительный класс для хранения списка работников в спектакле. Содержит только работника.
 */
public class Worker implements Serializable {
    private Employee employee;

    /**
     * Конструктор по умолчанию.
     */
    public Worker() {
        this.employee = null;
    }

    /**
     * Полный конструктор.
     *
     * @param employee the employee
     */
    public Worker(Employee employee) {
        this.employee = employee;
    }

    /**
     * Gets employee.
     *
     * @return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Sets employee.
     *
     * @param employee the employee
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "employee=" + employee +
                '}';
    }
}
