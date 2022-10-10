import javazoom.jl.player.Player;

import javax.swing.*;

import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

 public class musicPlayer implements ActionListener {
     JButton select,play,pause, resume ,stop;
     JPanel player, control;
     JLabel songName;
     String filename, filePath;
     JFileChooser fileChooser;
     private File myfile ;
     private FileInputStream fileInputStream;
     private BufferedInputStream bufferedInputStream;
     private Player py;
     private long totalLength, pauseLength;

     Thread playThread ,resumeThread;



     public musicPlayer() {

         JFrame frame = new JFrame();


         frame.setSize(450, 450);
         frame.setTitle("Mp3 music player");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setResizable(false);
         songName = new JLabel("", SwingConstants.CENTER);

         select = new JButton("select Mp3 ");

         player = new JPanel();
         control = new JPanel();

         player.setLayout(new GridLayout(1, 2));
         player.add(select);
         player.add(songName);
         control.setLayout(new GridLayout(1, 4, 2, 2));


         ImageIcon iconPlay = new ImageIcon("play-button.png");
         ImageIcon iconPause = new ImageIcon("pause-button.png");
         ImageIcon iconResume = new ImageIcon("resume-button.png");
         ImageIcon iconStop = new ImageIcon("stop-button.png");

         play = new JButton(iconPlay);
         pause = new JButton(iconPause);
         resume = new JButton(iconResume);
         stop = new JButton(iconStop);

         play.setBackground(Color.WHITE);
         pause.setBackground(Color.WHITE);
         resume.setBackground(Color.white);
         stop.setBackground(Color.white);

         Image icon = new ImageIcon("music").getImage();
         frame.setIconImage(icon);

         frame.add(songName);
         frame.add(player, BorderLayout.NORTH);
         frame.add(control, BorderLayout.SOUTH);


         control.add(play);
         control.add(pause);
         control.add(resume);
         control.add(stop);

         frame.setVisible(true);

         playThread = new Thread(runnablePlay);
         resumeThread=new Thread(runnableResume);



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
             fileChooser.setFileFilter(new FileNameExtensionFilter("Mp3 ","mp3"));
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
             }catch (IllegalThreadStateException a){
                 a.getMessage();
                 //System.out.println("Exception in play thread");
             }
         }
         if (e.getSource().equals(pause)) {

             //code for pause button
             if (py != null && filename != null) {
                 try {

                     pauseLength = fileInputStream.available();
                     py.close();
                 } catch (IOException e1) {
                     e1.getMessage();
                 }
             }
         }
         if (e.getSource().equals(resume)) {
             if (filename != null) {
                 resumeThread.start();

             } else {
                 songName.setText("No File was selected!");
             }
         }
         if (e.getSource().equals(stop)) {
             //code for stop button
             if (py != null) {
                 py.close();
                 songName.setText("song stopped");
             }
         }
     }

     Runnable runnablePlay = new Runnable() {
         @Override
         public void run() {
             try {
                 //code for play button
                 fileInputStream = new FileInputStream(myfile);
                 bufferedInputStream = new BufferedInputStream(fileInputStream);
                 py = new Player(bufferedInputStream);
                 totalLength = fileInputStream.available();
                 py.play();//starting music
             } catch (Exception e) {
                 e.printStackTrace();

             }
         }
     };
     Runnable runnableResume = new Runnable() {
         @Override
         public void run() {
             try {
                 //code for resume button
                 fileInputStream = new FileInputStream(myfile);
                 bufferedInputStream = new BufferedInputStream(fileInputStream);
                 py = new Player(bufferedInputStream);
                 fileInputStream.skip(totalLength - pauseLength);
                 py.play();

             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
     };

     public static void main(String[] args) {

         musicPlayer m = new musicPlayer();


     }



     }
     // End of musicPlayer class

