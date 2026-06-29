import java.awt.Robot;

public class KeyClicker {
    private Robot robot;

    public KeyClicker() {
        try {
            this.robot = new Robot();
        } catch (Exception e) {
            System.err.println("Failed to initialize Keyboard Robot: " + e.getMessage());
        }
    }
    public void triggerKey(int keyCode) {
        if (robot != null) {
            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
        }
    }
}
