import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javazoom.jl.player.Player;

 public class musicPlayer implements ActionListener {
     JButton select, play, pause, resume, stop;
     JPanel playerpanel, controlpanel;
     JLabel songName;
     String filename, filePath;
     JFileChooser fileChooser;
     private File myfile;
     private FileInputStream fileInputStream;

     private BufferedInputStream bufferedInputStream;
     private Player player;
     private long totalLength, pauseLength;

     Thread playThread, resumeThread;


     public musicPlayer() {

         JFrame frame = new JFrame();

         frame.setSize(450, 400);
         frame.setTitle("Mp3 music player");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setResizable(false);
         frame.setLocationRelativeTo(null);

         songName = new JLabel("", SwingConstants.CENTER);

         select = new JButton("Select Mp3 ");

         playerpanel = new JPanel();
         controlpanel = new JPanel();

         playerpanel.setLayout(new GridLayout(1, 1));
         playerpanel.add(select);
         playerpanel.add(songName);
         controlpanel.setLayout(new GridLayout(1, 4, 2, 2));


         ImageIcon iconPlay = new ImageIcon("play-button.png");
         ImageIcon iconPause = new ImageIcon("pause-button.png");
         ImageIcon iconResume = new ImageIcon("resume-button.png");
         ImageIcon iconStop = new ImageIcon("stop-button.png");

         ImageIcon icon = new ImageIcon("music.png");
         frame.setIconImage(icon.getImage());

         play = new JButton(iconPlay);
         pause = new JButton(iconPause);
         resume = new JButton(iconResume);
         stop = new JButton(iconStop);

         play.setBackground(Color.WHITE);
         pause.setBackground(Color.WHITE);
         resume.setBackground(Color.white);
         stop.setBackground(Color.white);

         controlpanel.add(play);
         controlpanel.add(pause);
         controlpanel.add(resume);
         controlpanel.add(stop);


         frame.add(songName);

         frame.add(playerpanel, BorderLayout.NORTH);
         frame.add(controlpanel, BorderLayout.SOUTH);




         frame.setVisible(true);

         playThread = new Thread(runnablePlay);
         resumeThread = new Thread(runnableResume);


         select.addActionListener(this);
         play.addActionListener(this);
         pause.addActionListener(this);
         resume.addActionListener(this);
         stop.addActionListener(this);
     }// end of constructor


     @Override
     public void actionPerformed(ActionEvent e) {
         if (e.getSource().equals(select)) {
             fileChooser = new JFileChooser();
             fileChooser.setCurrentDirectory(new File("D:\\songs"));
             fileChooser.setDialogTitle("Select Mp3");
             fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
             fileChooser.setFileFilter(new FileNameExtensionFilter("Mp3 files", "mp3"));
             if (fileChooser.showOpenDialog(select) == JFileChooser.APPROVE_OPTION) {
                 myfile = fileChooser.getSelectedFile();
                 filename = fileChooser.getSelectedFile().getName();
                 filePath = fileChooser.getSelectedFile().getPath();
                 songName.setText("File Selected : " + filename);
             }
         }
         if (e.getSource().equals(play)) {
             try {
                 if (filename != null) {

                     playThread.start();

                     songName.setText("Now playing : " + filename);
                 } else {
                     songName.setText("No File was selected!");
                 }
             } catch (IllegalThreadStateException a) {
                 System.out.println(a);

             }
         }
         if (e.getSource().equals(pause)) {

             if ( filename != null) {
                 try {

                     pauseLength = fileInputStream.available();
                     player.close();
                 } catch (IOException e1) {
                     System.out.println(e1);
                 }
             }
         }
         if (e.getSource().equals(resume)) {
             try {


             if (filename != null) {
                 resumeThread.start();

             } else {
                 songName.setText("No File was selected!");
             }
         }catch (IllegalThreadStateException r){
                 System.out.println(r);
             }
         }
         if (e.getSource().equals(stop)) {
             if (filename!= null) {
                 player.close();
                 songName.setText("song stopped");
             }
         }
     }

     Runnable runnablePlay = new Runnable() {
         @Override
         public void run() {
             try {
                 fileInputStream = new FileInputStream(myfile);
                 bufferedInputStream = new BufferedInputStream(fileInputStream);
                 player = new Player(bufferedInputStream);
                 totalLength = fileInputStream.available();
                 player.play();
             } catch (Exception e) {
                 e.printStackTrace();

             }
         }
     };


     Runnable runnableResume = new Runnable() {
         @Override
         public void run() {
             try {
                 fileInputStream = new FileInputStream(myfile);
                 //bufferedInputStream = new BufferedInputStream(fileInputStream);
                 player = new Player(fileInputStream);
                 fileInputStream.skip(totalLength - pauseLength);
                 player.play();

             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
     };


 }// End of musicPlayer class

class Main {
    public static void main(String[] args) {

        musicPlayer m = new musicPlayer();


    }


}


