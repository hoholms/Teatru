import java.io.Serializable;

/**
 * Класс, содержащий актера и его роль в спектакле.
 */
public class Role implements Serializable {
    /**Актер**/
    private Actor actor;
    /**Роль**/
    private String role;

    /**
     * Конструктор по умолчанию.
     */
    public Role() {
        this.actor = null;
        this.role = "";
    }

    /**
     * Неполный конструктор.
     *
     * @param actor the actor
     */
    public Role(Actor actor) {
        this.actor = actor;
        this.role = "";
    }

    /**
     * Полный конструктор.
     *
     * @param actor the actor
     * @param role  the role
     */
    public Role(Actor actor, String role) {
        this.actor = actor;
        this.role = role;
    }

    /**
     * Gets actor.
     *
     * @return the actor
     */
    public Actor getActor() {
        return actor;
    }

    /**
     * Sets actor.
     *
     * @param actor the actor
     */
    public void setActor(Actor actor) {
        this.actor = actor;
    }

    /**
     * Gets role.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets role.
     *
     * @param role the role
     */
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Role{" +
                "actor=" + actor +
                ", role='" + role + '\'' +
                '}';
    }
}
