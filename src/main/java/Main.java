import java.awt.event.KeyEvent;

public class Main {
    private static final double SLAM_THRESHOLD = 0.30;
    private static final int TARGET_HOTKEY = KeyEvent.VK_F;

    public static void main(String[] args) {
        System.out.println("Initializing Chess Slam Module...");
        KeyClicker simulator = new KeyClicker();

        AudioListener listener = new AudioListener(SLAM_THRESHOLD, simulator, TARGET_HOTKEY);

        listener.startListening();
    }
}