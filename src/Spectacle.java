import java.io.Serializable;
import java.util.Vector;

/**
 * Класс, содержащий все данные о спектакле.
 */
public class Spectacle implements Serializable {
    /**Название спектакля**/
    private String title;
    /**Бюджет спектакля**/
    private float budget;
    /**Место проведения спектакля**/
    private String location;
    /**Гонорар спектакля**/
    private float fee;
    /**Список актеров**/
    private Vector<Role> actors;
    /**Список работников**/
    private Vector<Worker> employees;

    /**
     * Конструктор по умолчанию.
     */
    public Spectacle() {
        this.title = "";
        this.budget = 0;
        this.location = "";
        this.fee = 0;
        this.actors = new Vector<>(0);
        this.employees = new Vector<>(0);
    }

    /**
     * Неполный конструктор. Используется при добавлении нового спектакля.
     *
     * @param title    the title
     * @param location the location
     * @param fee      the fee
     */
    public Spectacle(String title, String location, float fee) {
        this.title = title;
        this.location = location;
        this.fee = fee;
        this.actors = new Vector<>(0);
        this.employees = new Vector<>(0);
    }

    /**
     * Полный конструктор.
     *
     * @param title     the title
     * @param budget    the budget
     * @param location  the location
     * @param fee       the fee
     * @param actors    the actors
     * @param employees the employees
     */
    public Spectacle(String title, float budget, String location,
                     float fee, Vector<Role> actors, Vector<Worker> employees) {
        this.title = title;
        this.budget = budget;
        this.location = location;
        this.fee = fee;
        this.actors = actors;
        this.employees = employees;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets budget.
     *
     * @return the budget
     */
    public float getBudget() {
        return budget;
    }

    /**
     * Sets budget.
     *
     * @param budget the budget
     */
    public void setBudget(float budget) {
        this.budget = budget;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets fee.
     *
     * @return the fee
     */
    public float getFee() {
        return fee;
    }

    /**
     * Sets fee.
     *
     * @param fee the fee
     */
    public void setFee(float fee) {
        this.fee = fee;
    }

    /**
     * Gets actors.
     *
     * @return the actors
     */
    public Vector<Role> getActors() {
        return actors;
    }

    /**
     * Sets actors.
     *
     * @param actors the actors
     */
    public void setActors(Vector<Role> actors) {
        this.actors = actors;
    }

    /**
     * Gets employees.
     *
     * @return the employees
     */
    public Vector<Worker> getEmployees() {
        return employees;
    }

    /**
     * Sets employees.
     *
     * @param employees the employees
     */
    public void setEmployees(Vector<Worker> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "Spectacle{" +
                "title='" + title + '\'' +
                ", budget=" + budget +
                ", location='" + location + '\'' +
                ", fee=" + fee +
                ", actors=" + actors +
                ", employees=" + employees +
                '}';
    }

    /**
     * Автоматический подсчет бюджета.
     */
    public void calcBudget() {
        float newBudget = 0.0f;

        for (Role el : actors)
            newBudget += el.getActor().getSalary() + fee;
        for (Worker el : employees)
            newBudget += el.getEmployee().getSalary();

        this.budget = newBudget;
    }
}
