import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Абстрактный класс, содержащий методы для оперирования с текстовыми полями JTextField.
 */
public abstract class TextFieldsMethods {

    /**
     * Текстовое поле может принимать только значения типа long.
     *
     * @param tf текстовое поле
     */
    public static void makeTfLong(JTextField tf) {
        tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                try {
                    Long.parseLong(tf.getText() + e.getKeyChar());
                } catch (NumberFormatException ex) {
                    e.consume();
                }
            }
        });
    }

    /**
     * Текстовое поле может принимать только значения типа int.
     *
     * @param tf текстовое поле
     */
    public static void makeTfInt(JTextField tf) {
        tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                try {
                    Integer.parseInt(tf.getText() + e.getKeyChar());
                } catch (NumberFormatException ex) {
                    e.consume();
                }
            }
        });
    }

    /**
     * Текстовое поле может принимать только значения типа float.
     *
     * @param tf текстовое поле
     */
    public static void makeTfFloat(JTextField tf) {
        tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                try {
                    Float.parseFloat(tf.getText() + e.getKeyChar());
                } catch (NumberFormatException ex) {
                    e.consume();
                }
            }
        });
    }
}
