package adapter;

public class AdvancedAudioPlayer implements AdvancedAudioPlayerIF {
    @Override
    public void play(String s) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {
        System.out.println("Stopped playing audio");
    }

    @Override
    public void readFile(String file) {
        System.out.println("Reading file " + file + " ...");
    }

    @Override
    public void verifyTypeFile(String file) {
        System.out.println("File type: " + file);
    }

    @Override
    public void playAudio(String file, String type) {
        String s;

        if (type.equals("MP3") || type.equals("MP4")) {
            s = type;
        } else {
            s = "unknown";
        }

        System.out.println("Reproducing format " + s + " ...");
    }
}
