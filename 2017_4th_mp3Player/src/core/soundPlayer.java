package core;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javax.swing.*;
import java.io.File;

abstract class soundPlayer {
    private double volume;
    private boolean allFine;
    private static MediaPlayer mediaPlayer;
    private boolean playing = false;
    private int currentSoundIndex;

    final JFXPanel fxPanel = new JFXPanel();

    soundPlayer() {
        this.volume = 1;
        this.currentSoundIndex = 0;
    }
    void volumeUP() {
        if(this.volume < 1) {
            this.volume+=0.1;
        }
        mediaPlayer.setVolume(volume);
    }
    void volumeDOWN() {
        if(this.volume > 0) {
            this.volume-=0.1;
        }
        mediaPlayer.setVolume(volume);
    }
    void updateVolumeDisplay(JProgressBar volumeBar) {
        volumeBar.setValue((int)(this.volume*100));
    }

    void playSound() {
         mediaPlayer.play();
    }
    void stopSound() {
        mediaPlayer.stop();
    }
    void autoPlayNext(String name) {
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            String soundName = "src/sounds/"+name;
            Media hit = new Media(new File(soundName).toURI().toString());
            mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
        });
    }
    void pauseSound() {
        mediaPlayer.pause();
    }
    void setSound(String name) {
        String soundName = "src/sounds/"+name;
        try {
            Media hit = new Media(new File(soundName).toURI().toString());
            mediaPlayer = new MediaPlayer(hit);
        }
        catch (MediaException e1) {
            mediaPlayer.dispose();
            JOptionPane.showMessageDialog(null, ".WAV is not supported","ERROR",
                    JOptionPane.WARNING_MESSAGE,null);
        }
        catch (NullPointerException e2) {
            mediaPlayer.dispose();
            JOptionPane.showMessageDialog(null, ".File not found","ERROR",
                    JOptionPane.WARNING_MESSAGE,null);
        }
    }
}
