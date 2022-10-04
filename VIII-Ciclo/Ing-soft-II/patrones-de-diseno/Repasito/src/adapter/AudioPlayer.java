package adapter;

public class AudioPlayer implements AudioPlayerIF {
    @Override
    public void play(String s) {
        System.out.println("Playing " + s + " ...");
    }

    @Override
    public void pause() {
        System.out.println("Pause");
    }

    @Override
    public void stop() {
        System.out.println("Stopped playing audio");
    }
}
