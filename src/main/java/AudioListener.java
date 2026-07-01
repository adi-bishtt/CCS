import javax.sound.sampled.*;

public class AudioListener {
    private final double flipThreshold;
    private final double resignThreshold;
    private final KeyClicker clicker;
    private boolean isRunning;

    public AudioListener(double flipThreshold, double resignThreshold, KeyClicker clicker) {
        this.flipThreshold = flipThreshold;
        this.resignThreshold = resignThreshold;
        this.clicker = clicker;
        this.isRunning = false;
    }

    public void startListening() {
        AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            System.err.println("Microphone line not supported.");
            return;
        }

        try (TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info)) {
            line.open(format);
            line.start();
            isRunning = true;
            System.out.println("Audio detector started. Listening for slams...");

            byte[] buffer = new byte[1024];
            while (isRunning) {
                int bytesRead = line.read(buffer, 0, buffer.length);
                if (bytesRead > 0) {
                    double scaledAmplitude = calculateScaledAmplitude(buffer, bytesRead);

                    if (scaledAmplitude > 0.1) {
                        System.out.printf("Current volume level: %.2f\n", scaledAmplitude);
                    }

                    if (scaledAmplitude > resignThreshold) {
                        System.out.printf("MASSIVE SLAM! (Level: %.2f) -> Resigning...\n", scaledAmplitude);
                        clicker.executeFullSlam();
                        Thread.sleep(3000);
                    } else if (scaledAmplitude > flipThreshold) {
                        System.out.printf("Minor slam! (Level: %.2f) -> Flipping Board\n", scaledAmplitude);
                        clicker.pressKey(java.awt.event.KeyEvent.VK_F);
                        Thread.sleep(1500);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopListening() {
        this.isRunning = false;
    }

    private double calculateScaledAmplitude(byte[] buffer, int bytesRead) {
        double max = 0;
        for (int i = 0; i < bytesRead - 1; i += 2) {
            int sample = (buffer[i] << 8) | (buffer[i + 1] & 0xFF);
            double absValue = Math.abs(sample);
            if (absValue > max) {
                max = absValue;
            }
        }

        double scaled = (max / 32768.0) * 5.0;
        return Math.min(scaled, 5.0);
    }
}