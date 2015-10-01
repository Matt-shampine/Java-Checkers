
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
*Class that handles the intro screen for player names and waiting list
*@author Matthew Shampine
*/
public class OnlinePlayersList extends JFrame implements ActionListener
{
   
   Networking net;
   
   JPanel serverPanel;
   JPanel playerPanel;
   
   JTextField serverText;
   JTextField username;
   JLabel message = new JLabel();
   JPanel wrapper  = new JPanel(new GridLayout(0,1));
   JScrollPane scrollPanel = new JScrollPane(wrapper);
   
   /**
   *Constructor for the player list class
   *@param parentFrame The JFrame where this will appear
   *@param _net The networking class to pass data to
   */
   public OnlinePlayersList(JFrame parentFrame, Networking _net)
   {
      net = _net;
   
      this.setLocationRelativeTo(parentFrame);
      this.setSize(450,300);
      this.setTitle("Online Players");
      
      JButton serverButton = new JButton("Server Settings");
      JButton playerList = new JButton("Players");
      JButton connectButton = new JButton("Connect");
      serverText = new JTextField(25);
      username = new JTextField(25);
      JLabel serverLabel = new JLabel("Server IP Address: ");
      JLabel nameLabel = new JLabel("Username: ");
      
      JPanel buttonWrapper = new JPanel(new GridLayout(0,1));
      JPanel serverWrapper = new JPanel();
      serverPanel = new JPanel(new GridLayout(0,1));
      playerPanel = new JPanel();
      
      serverButton.setBackground(new Color(235,235,235));
      serverButton.addActionListener(this);
      playerList.setBackground(new Color(235,235,235));
      playerList.addActionListener(this);
      connectButton.addActionListener(this);
      
      buttonWrapper.add(serverButton);
      buttonWrapper.add(playerList);
      serverWrapper.add(serverLabel);
      serverWrapper.add(serverText);
      serverWrapper.add(nameLabel);
      serverWrapper.add(username);
      serverWrapper.add(connectButton);
      
      serverPanel.add(serverWrapper);
      
      this.add(serverPanel, BorderLayout.CENTER);
      this.add(buttonWrapper, BorderLayout.WEST);
     
      this.setVisible(true);
   }
   
   /**
   *Populates the list of waiting players to display to the user
   */
   private void populateList()
   {
      
      wrapper.removeAll();
      scrollPanel.remove(wrapper);
      playerPanel.remove(scrollPanel);
      
      JLabel allPlayersLabel = new JLabel("All Players Online");
      wrapper.add(allPlayersLabel);
      
      ArrayList<String> waitingPlayers = net.getPlayersWaiting();
      for(int i = 0; i < 100; i++)
      {
         //Slow down the program
      }
      System.out.println("Waiting passed");
      ArrayList<String> allPlayers = net.getAllPlayerNames();
      System.out.println("Players list passed");
      for(String playerName : allPlayers)
      {
         Player player = new Player(playerName);
         for(String waitName : waitingPlayers)
         {
            if(playerName.equals(waitName) && !(playerName.equals(net.getLocalName())) && !(net.isPlaying()))
            {
               player.setWait(true);
               break;
            }
         }
         if(!(player.isWaiting()))
         {
            player.setEnabled(false);
            player.setBackground(new Color(235,235,235));
         }
         player.addActionListener(this);
         wrapper.add(player);
      }
      playerPanel.add(scrollPanel);
   }
   
   /**
   *Method to check that the selected name is not already in use on the server
   *@param name The name to validate with
   */
   private void validateName(String name)
   {
      boolean valid = true;
      ArrayList<String> names = net.getAllPlayerNames();
      for(String playerName : names)
      {
         if(name.equals(playerName))
         {
            valid = false;
         }
      }
      if(valid)
      {
         net.setPlayerName(name);
         net.setLocalName(name);
      }
      else
      {
         String newName = (String)JOptionPane.showInputDialog(this, "Name is already taken", "Name Select",JOptionPane.PLAIN_MESSAGE,null,null, null);
         validateName(newName);
      }
   }
   
   public void makeVisible()
   {
      this.setVisible(true);
   }
   
   /**
   *Mouse listener method
   *@param e The event performed
   */
   public void actionPerformed(ActionEvent e)
   {
      String cmd = e.getActionCommand();
      if(cmd.equals("Server Settings"))
      {
         this.remove(playerPanel);
         this.remove(serverPanel);
         this.add(serverPanel, BorderLayout.CENTER);
         serverPanel.setVisible(false);
         serverPanel.setVisible(true);
         //this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
      }
      else if(cmd.equals("Players"))
      {
         if(net.isConnected())
         {
            populateList();
            this.remove(serverPanel);
            this.remove(playerPanel);
            this.add(playerPanel, BorderLayout.CENTER);
            playerPanel.setVisible(false);
            playerPanel.setVisible(true);
         }
         else
         {
            JOptionPane.showMessageDialog(null, "Please connect to a server first.");
         }
         //this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
      }
      else if(cmd.equals("Connect"))
      {
         boolean check = net.connect(serverText.getText());
         if(check)
         {
            message.setText("Connected to server");
            message.setForeground(Color.GREEN);
            validateName(username.getText());
         }
         else
         {
            message.setText("Unable to connect to server");
            message.setForeground(Color.RED);
         }
         serverPanel.remove(message);
         serverPanel.add(message);
         serverPanel.setVisible(false);
         serverPanel.setVisible(true);
      }
      else
      {
         net.joinPlayer(cmd);
         this.setVisible(false);
      }
   }
   
   protected class Player extends JButton
   {
      String name;
      boolean waiting;
      
      public Player(String _name)
      {
         name = _name;
         waiting = false;
         this.setText(name);
      }
      
      public void setWait(boolean wait)
      {
         waiting = wait;
      }
      
      public boolean isWaiting()
      {
         return waiting;
      }
   }
}