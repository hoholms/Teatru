import java.io.Serializable;

/**
 * Класс, содержащий все данные об актере. Наследует класс работника.
 */
public class Actor extends Employee implements Serializable {

    /**Опыт работы**/
    private int workExp;
    /**Звание или награды актера**/
    private String rank;

    /**
     * Констуктор по умолчанию.
     */
    public Actor() {
        this.setJob("Actor");
        this.setSalary(0);
        this.workExp = 0;
        this.rank = "";
        this.setName("");
        this.setSurname("");
        this.setIdnp(0);
        this.setAddress("");
        this.setPhone(0);
    }

    /**
     * Неполный конструктор. Используется при добавлении нового актера.
     *
     * @param name    the name
     * @param surname the surname
     * @param idnp    the idnp
     */
    public Actor(String name, String surname, long idnp) {
        super(name, surname, idnp);
        this.setJob("Actor");
        this.setSalary(0);
        this.workExp = 0;
        this.rank = "";
    }

    /**
     * Полный конструктор.
     *
     * @param salary  the salary
     * @param workExp the work exp
     * @param rank    the rank
     * @param name    the name
     * @param surname the surname
     * @param idnp    the idnp
     * @param address the address
     * @param phone   the phone
     */
    public Actor(float salary, int workExp, String rank, String name,
                 String surname, long idnp, String address, long phone) {
        super(salary, name, surname, idnp, address, phone);
        this.setJob("Actor");
        this.workExp = workExp;
        this.rank = rank;
    }

    /**
     * Gets work exp.
     *
     * @return the work exp
     */
    public int getWorkExp() {
        return workExp;
    }

    /**
     * Sets work exp.
     *
     * @param workExp the work exp
     */
    public void setWorkExp(int workExp) {
        this.workExp = workExp;
    }

    /**
     * Gets rank.
     *
     * @return the rank
     */
    public String getRank() {
        return rank;
    }

    /**
     * Sets rank.
     *
     * @param rank the rank
     */
    public void setRank(String rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "workExp=" + workExp +
                ", rank='" + rank + '\'' +
                "} " + super.toString();
    }
}
