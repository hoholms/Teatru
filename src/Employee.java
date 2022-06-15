import java.io.Serializable;

/**
 * Класс, содержащий все данные о работнике.
 */
public class Employee implements Serializable {
    /**Должность работника**/
    private String job;
    /**Зарплата**/
    private float salary;
    /**Имя**/
    private String name;
    /**Фамилия**/
    private String surname;
    /**Персональный код IDNP**/
    private long idnp;
    /**Домашний адрес**/
    private String address;
    /**Номер телефона**/
    private long phone;

    /**
     * Конструктор по умолчанию.
     */
    public Employee() {
        this.job = "";
        this.salary = 0;
        this.name = "";
        this.surname = "";
        this.idnp = 0;
        this.address = "";
        this.phone = 0;
    }

    /**
     * Неполный конструктор. Используется при добавлении работника.
     *
     * @param job     the job
     * @param name    the name
     * @param surname the surname
     * @param idnp    the idnp
     */
    public Employee(String job, String name, String surname, long idnp) {
        this.job = job;
        this.name = name;
        this.surname = surname;
        this.idnp = idnp;
        this.salary = 0;
        this.address = "";
        this.phone = 0;
    }

    /**
     * Полный конструктор.
     *
     * @param salary  the salary
     * @param name    the name
     * @param surname the surname
     * @param idnp    the idnp
     * @param address the address
     * @param phone   the phone
     */
    public Employee(float salary, String name, String surname, long idnp, String address, long phone) {
        this.salary = salary;
        this.name = name;
        this.surname = surname;
        this.idnp = idnp;
        this.address = address;
        this.phone = phone;
        this.job = "";
    }

    /**
     * Неполный констркутор. ИСпользуется для конструктора дочернего класса Actor.
     *
     * @param name    the name
     * @param surname the surname
     * @param idnp    the idnp
     */
    public Employee(String name, String surname, long idnp) {
        this.name = name;
        this.surname = surname;
        this.idnp = idnp;
        this.salary = 0;
        this.address = "";
        this.phone = 0;
        this.job = "";
    }

    /**
     * Gets job.
     *
     * @return the job
     */
    public String getJob() {
        return job;
    }

    /**
     * Sets job.
     *
     * @param job the job
     */
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * Gets salary.
     *
     * @return the salary
     */
    public float getSalary() {
        return salary;
    }

    /**
     * Sets salary.
     *
     * @param salary the salary
     */
    public void setSalary(float salary) {
        this.salary = salary;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets surname.
     *
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets surname.
     *
     * @param surname the surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Gets idnp.
     *
     * @return the idnp
     */
    public long getIdnp() {
        return idnp;
    }

    /**
     * Sets idnp.
     *
     * @param idnp the idnp
     */
    public void setIdnp(long idnp) {
        this.idnp = idnp;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets phone.
     *
     * @return the phone
     */
    public long getPhone() {
        return phone;
    }

    /**
     * Sets phone.
     *
     * @param phone the phone
     */
    public void setPhone(long phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "job='" + job + '\'' +
                ", salary=" + salary +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", idnp=" + idnp +
                ", adress='" + address + '\'' +
                ", phone=" + phone +
                '}';
    }
}
