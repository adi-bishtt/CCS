import javax.sound.sampled.*;
import java.net.URL;

public class AudioPlayer {
    private final String fileName;

    public AudioPlayer(String fileName) {
        this.fileName = fileName;
    }

    public void playSync() {
        try {
            URL audioUrl = AudioPlayer.class.getClassLoader().getResource(fileName);
            if (audioUrl == null) {
                System.err.println("Audio file not found in resources: " + fileName);
                return;
            }

            try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioUrl)) {
                AudioFormat format = audioStream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);

                if (!AudioSystem.isLineSupported(info)) {
                    System.err.println("Audio format not supported by your system mixer.");
                    return;
                }

                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.open(audioStream);
                clip.start();

                Thread.sleep(clip.getMicrosecondLength() / 1000);
                clip.close();
            }
        } catch (Exception e) {
            System.err.println("Error playing audio: " + e.getMessage());
        }
    }
}