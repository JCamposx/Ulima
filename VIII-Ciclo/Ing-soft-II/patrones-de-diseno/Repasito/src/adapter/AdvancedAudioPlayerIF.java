package adapter;

public interface AdvancedAudioPlayerIF {
    // Operaciones originales
    void play(String s);
    void pause();
    void stop();

    // Operaciones del Adapter
    void readFile(String file);
    void verifyTypeFile(String file);
    void playAudio(String file, String type);
}
