import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class KeyClicker {
    private Robot robot;
    private final int resignX;
    private final int resignY;

    public KeyClicker(int resignX, int resignY) {
        this.resignX = resignX;
        this.resignY = resignY;
        try {
            this.robot = new Robot();
        } catch (Exception e) {
            System.err.println("Failed to initialize Keyboard Robot: " + e.getMessage());
        }
    }

    public void pressKey(int keyCode) {
        if (robot != null) {
            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
        }
    }

    public void executeFullSlam() {
        if (robot == null) return;

        try {
            pressKey(KeyEvent.VK_F);
            Thread.sleep(200);
            robot.mouseMove(resignX, resignY);
            Thread.sleep(50);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}