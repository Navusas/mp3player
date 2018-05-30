package core;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class audioplayer extends soundPlayer{
    private JPanel panel1;
    private JButton volumeUpButton;
    private JButton previousSongButton;
    private JButton playButton;
    private JButton nextSongButton;
    private JButton volumeDownButton;
    private JPanel mainPanel;
    private JPanel screenPanel;
    private JPanel datePanel;
    private JPanel controlPanel;
    private JPanel songPanel;
    private JLabel songName;
    private JProgressBar volumeBar;
    private JLabel stateLabel;
    private JList<String> songList;
    private JLabel dateLabel;
    private String oldSongName="";
    private boolean isPlaying = false;
    private final static String PLAYING = "Playing . . .";
    private final static String PAUSED = "Paused . . .";
    private int songIndex;


    private audioplayer() {

        super();
        songIndex = 0;
        //** Read songs from directory and set default index
        setSongList();
        setDate();
        updateVolumeDisplay(volumeBar);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedValue = songList.getSelectedValue();
                runMusicPlayer(selectedValue);
                setSongIndex();
            }
        });
        volumeUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volumeUP();
                updateVolumeDisplay(volumeBar);
            }
        });
        volumeDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volumeDOWN();
                updateVolumeDisplay(volumeBar);
            }
        });
        nextSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                increaseIndex();
                keepFocusingList();
                runMusicPlayer(songList.getSelectedValue());
            }

        });
        previousSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                decreaseIndex();
                keepFocusingList();
                runMusicPlayer(songList.getSelectedValue());
            }
        });
    }


    private void changePlayerState(String selectedValue, String textToSet,boolean alreadyPlaying){
        this.songName.setText(selectedValue);
        this.stateLabel.setText(textToSet);
        this.isPlaying = alreadyPlaying;
        this.oldSongName = selectedValue;

    }
    private void pauseMusic(String selectedValue) {
        pauseSound();
        changePlayerState(selectedValue, PAUSED, false);
    }
    private void startMusic(String selectedValue) {
        setSound(selectedValue);
        playSound();
        changePlayerState(selectedValue, PLAYING, true);
    }
    private void startNewMusic(String selectedValue) {
        stopSound();
        setSound(selectedValue);
        playSound();
        changePlayerState(selectedValue, PLAYING, true);
    }
    private void runMusicPlayer(String selectedValue) {
        if(!oldSongName.equals(selectedValue)) {
            if(!isPlaying) {
                startMusic(selectedValue);
            }
            else {
                startNewMusic(selectedValue);
            }
        }
        else {
            if(isPlaying) {
                pauseMusic(selectedValue);
            }
            else startMusic(selectedValue);
        }
       // playNextSong();
    }
    private void playNextSong() {
        setSongIndex();
        increaseIndex();
        autoPlayNext(songList.getModel().getElementAt(songIndex));
        keepFocusingList();
        changePlayerState(songList.getSelectedValue(),PLAYING,true);
    }
    public String getSongName() {
        return songList.getSelectedValue();
    }
    private void setSongList() {
        DefaultListModel<String> songs = new DefaultListModel<>();
        File[] fileList = new File("src/sounds").listFiles();
        for (File file : fileList) {
            if (file.isFile()) {
                songs.addElement(file.getName());
            }
        }
        songList.setModel(songs);
        songList.setSelectedIndex(this.songIndex);
    }
    public void setDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        dateLabel.setText(sdf.format(cal.getTime()));
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
    private void keepFocusingList() {
        songList.setSelectedIndex(this.songIndex);
    }
    private void decreaseIndex(){
        if (songIndex <= 0) {
            songIndex = songList.getModel().getSize()-1;
        }
        else {
            songIndex--;
        }
    }
    private void increaseIndex() {
        if (songIndex >= songList.getModel().getSize()-1) {
            songIndex = 0;
        }
        else {
            songIndex++;
        }
    }
    private void setSongIndex() {
        songIndex = songList.getSelectedIndex();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MP3 Player");
        frame.setContentPane(new audioplayer().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
