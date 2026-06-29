import javax.sound.sampled.*;

public class AudioListener {

    private final double threshold;
    private final KeyClicker clicker;
    private final int targetKey;
    private boolean isRunning;

    public AudioListener(double threshold, KeyClicker clicker, int targetKey) {
        this.threshold = threshold;
        this.clicker = clicker;
        this.targetKey = targetKey;
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
                    double maxAmplitude = calculateMaxAmplitude(buffer, bytesRead);

                    if (maxAmplitude > threshold) {
                        System.out.printf("Slam detected! (Amplitude: %.2f) -> Triggering Key\n", maxAmplitude);
                        clicker.triggerKey(targetKey);
                        Thread.sleep(1000);
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

    private double calculateMaxAmplitude(byte[] buffer, int bytesRead) {
        double max = 0;
        for (int i = 0; i < bytesRead - 1; i += 2) {
            int sample = (buffer[i] << 8) | (buffer[i + 1] & 0xFF);
            double normalized = Math.abs(sample) / 32768.0;
            if (normalized > max) {
                max = normalized;
            }
        }
        return max;
    }
}