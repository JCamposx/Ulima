package adapter;

public class Test {
    public static void main(String[] args) {
        AudioPlayer a = new AudioPlayer();
        a.play("DBZ");
        a.pause();
        a.stop();
    }
}
