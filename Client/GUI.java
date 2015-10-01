
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
*Class that handles all graphics stuff, such as drawing pieces and updating them
*@author Matthew Shampine
*/
public class GUI extends JFrame
{
   
   Tile[][] location;
   MoveListener listener;
   JLabel player1ScoreLabel;
   JLabel player2ScoreLabel;
   JTextArea chatWindow;
   JTextArea messageBox;
   GameLogic logic;
   //Networking addition
   Networking net;
   //Jar addition
   ImageIcon player1 = new ImageIcon(getClass().getResource("player1.png"));
   ImageIcon player1King = new ImageIcon(getClass().getResource("player1King.png"));
   ImageIcon player2 = new ImageIcon(getClass().getResource("player2.png"));
   ImageIcon player2King = new ImageIcon(getClass().getResource("player2King.png"));

   /**
   *Method to construct the frame and set everything up at the start.
   */
   public void constructWindow()
   {
      net = new Networking(this);
      logic = new GameLogic();
      
      MenuBar menuBar = new MenuBar(this, logic);
      
      location = new Tile[8][8];
      
      this.setTitle("Checkers");
      this.setMinimumSize(new Dimension(640,480));
      this.setLocationRelativeTo(null);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setJMenuBar(menuBar);
      this.addComponentListener(new ResizeHandler());
      
      JPanel centerWrapper = new JPanel(new GridLayout(8,8));
      setTiles(centerWrapper);
      centerWrapper.setPreferredSize(new Dimension(400, 400));
      JPanel eastWrapper = new JPanel(new GridLayout(0,1));
      JPanel northWrapper = new JPanel(new GridBagLayout());
      northWrapper.setPreferredSize(new Dimension(640, 25));
      JPanel southWrapper = new JPanel(new GridBagLayout());
      southWrapper.setPreferredSize(new Dimension(640, 45));
      
      JPanel player1ScorePanel = new JPanel();
      JPanel player2ScorePanel = new JPanel();
      
      Color nwRed = new Color(153,0,0);
      Color nwBlue = new Color(0,0,255);
      
      JLabel player1Label = new JLabel("Player 1: ");
      player1Label.setFont(new Font("Arial Black", 1, 11));
      player1Label.setForeground(nwBlue);
      JLabel player2Label = new JLabel("Player 2: ");
      player2Label.setFont(new Font("Arial Black", 1, 11));
      player2Label.setForeground(nwRed);
      player1ScoreLabel = new JLabel("0");
      player2ScoreLabel = new JLabel("0");
      
      
      chatWindow = new JTextArea(15,15);
      chatWindow.setPreferredSize(new Dimension(220,440));
      chatWindow.setEditable(false);
      chatWindow.setWrapStyleWord(true);
      chatWindow.setLineWrap(true);
      JScrollPane scroll = new JScrollPane(chatWindow);
      
      JLabel msgWindowLabel = new JLabel("Chat: ");
      msgWindowLabel.setFont(new Font("Arial Black", 1, 11));
      messageBox = new JTextArea();
      messageBox.setPreferredSize(new Dimension(500, 25));
      messageBox.setWrapStyleWord(true);
      messageBox.setLineWrap(true);
      messageBox.setEditable(false);
      JButton sendButton = new JButton("Send");
      
      
      sendButton.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            net.sendMessage("$chat "+messageBox.getText());
            messageBox.setText("");
         }
      });
      
      player1ScorePanel.add(player1ScoreLabel);
      player2ScorePanel.add(player2ScoreLabel);
      
      northWrapper.add(player1Label);
      northWrapper.add(player1ScoreLabel);
      northWrapper.add(player1ScorePanel);
      northWrapper.add(player2Label);
      northWrapper.add(player2ScoreLabel);
      northWrapper.add(player2ScorePanel);
      
     
      eastWrapper.add(scroll);
     
      southWrapper.add(msgWindowLabel);
      southWrapper.add(messageBox);
      southWrapper.add(sendButton);
      
      this.add(centerWrapper, BorderLayout.CENTER);
      this.add(eastWrapper, BorderLayout.EAST);
      this.add(northWrapper, BorderLayout.NORTH);
      this.add(southWrapper, BorderLayout.SOUTH);
           
      newGame();
      colorBoard();
      
      this.setVisible(true);
      resizeImages();
      OnlinePlayersList playerList = new OnlinePlayersList(this, net);
      menuBar.addPlayerList(playerList);
   }
   
   /**
   *Method to set up the tiles in the array and on the board
   *@param wrapper The JPanel that all the tiles will be placed on. Should be a grid layout.
   */
   private void setTiles(JPanel wrapper)
   {
      listener = new MoveListener(location, this, net);
      for(int i = 0; i < 8; i++)
      {
         for(int j = 0; j < 8; j++)
         {
            location[i][j] = new Tile(j,i);
            wrapper.add(location[i][j]);
            JButton b = new JButton();
            location[i][j].add(b);
            b.addActionListener(listener);
         }
      }
   } 
   
   /**
   *Method that starts a new game. Clears the scores and resets the board
   */
   public void newGame()
   {  
      clearBoard();
      listener.resetTurn();
      
      for(int i = 0; i < 8; i++)
      {
         JButton btn = (JButton)location[0][i].getComponent(0);
         location[0][i].setOccupied(true);
         location[0][i].setTeam(2);
         btn.setIcon(player2);
         i++;
      }
      for(int i = 1; i < 8; i++)
      {
         JButton btn = (JButton)location[1][i].getComponent(0);
         location[1][i].setOccupied(true);
         location[1][i].setTeam(2);
         btn.setIcon(player2);
         i++;
      }
      for(int i = 0; i < 8; i++)
      {
         JButton btn = (JButton)location[2][i].getComponent(0);
         location[2][i].setOccupied(true);
         location[2][i].setTeam(2);
         btn.setIcon(player2);
         i++;
      }
      for(int i = 1; i < 8; i++)
      {
         JButton btn = (JButton)location[5][i].getComponent(0);
         location[5][i].setOccupied(true);
         location[5][i].setTeam(1);
         btn.setIcon(player1);
         i++;
      }
      for(int i = 0; i < 8; i++)
      {
         JButton btn = (JButton)location[6][i].getComponent(0);
         location[6][i].setOccupied(true);
         location[6][i].setTeam(1);
         btn.setIcon(player1);
         i++;
      }
      for(int i = 1; i < 8; i++)
      {
         JButton btn = (JButton)location[7][i].getComponent(0);
         location[7][i].setOccupied(true);
         location[7][i].setTeam(1);
         btn.setIcon(player1);
         i++;
      }
      
      player1ScoreLabel.setText("0");
      player2ScoreLabel.setText("0");
      
      logic.setKings(0,1);
      logic.setKings(0,2);
      logic.setCapCount(0,1);
      logic.setCapCount(0,2);
   }
   
   /**
   *Helper method to clear the board when new game is called
   */
   public void clearBoard()
   {
      for(int i = 0; i < 8; i++)
      {
         for(int j = 0; j < 8; j++)
         {
            Tile currentTile = location[i][j];
            currentTile.setOccupied(false);
            currentTile.setTeam(0);
            currentTile.setKing(false);
            JButton b = (JButton)currentTile.getComponent(0);
            b.setIcon(null);
         }
      }
   }
   
   /**
   *Method that handles resizing of all pieces
   */
   public void resizeImages()
   {
      for(int i = 0; i < 8; i++)
      {
         for(int j = 0; j < 8; j++)
         {
            Tile currentTile = location[i][j];
            JButton btn = (JButton) currentTile.getComponent(0);
            ImageIcon icon = null;
            if(currentTile.getTeam() == 1)
            {
               if(currentTile.isKing())
               {
                  icon = player1King;
               }
               else
               {
                  icon = player1;
               }
            }
            else if(currentTile.getTeam() == 2)
            { 
               if(currentTile.isKing())
               {
                  icon = player2King;
               }
               else
               {
                  icon = player2;
               }
            }
            if(icon != null)
            {
               Image original = (Image) icon.getImage();
               Image scaled = original.getScaledInstance(btn.getWidth(),btn.getHeight(), 0);
               btn.setIcon(new ImageIcon(scaled));
            }
         }
      }
   }
   
   /**
   *Method that colors the background of the board
   */
   public void colorBoard()
   {
      for(int i = 0; i < 8; i++)
      {
         for(int j = 0; j < 8; j++)
         {
            Tile currentTile = (Tile)location[i][j];
            JButton btn = (JButton)currentTile.getComponent(0);
            if((i+j)%2 == 0)
            {
               btn.setBackground(Color.BLACK);
            }
            else
            {
               btn.setBackground(Color.WHITE);
            }
         }
      }
   }
   
   /**
   *Method that resizes only one tile rather then the entire board
   *@param dest The tile to be resized
   */
   public void resizeTile(Tile dest)
   {
      JButton btn = (JButton) dest.getComponent(0);
      ImageIcon icon = (ImageIcon) btn.getIcon();
      Image original = (Image) icon.getImage();
      Image scaled = original.getScaledInstance(btn.getWidth(),btn.getHeight(), 0);
      btn.setIcon(new ImageIcon(scaled));
   }
   
   /**
   *Method that adds a point to a players score and updates the display
   *@param team The team to be reciving the point
   */
   public void addPoint(int team)
   {
      if(team == 1)
      {
         Integer currentScore = Integer.parseInt(player1ScoreLabel.getText());
         currentScore++;
         player1ScoreLabel.setText(currentScore.toString());
      }
      else if(team == 2)
      {
         Integer currentScore = Integer.parseInt(player2ScoreLabel.getText());
         currentScore++;
         player2ScoreLabel.setText(currentScore.toString());
      }
   }
   
   /**
   *Method that handles moving the other players peices
   *@param tile1X The X value of the source tile
   *@param tile1Y The Y value of the source tile
   *@param tile2X The X value of the destination tile
   *@param tile2Y The Y value of the destination tile
   */
   public void updateScreen(int tile1X, int tile1Y, int tile2X, int tile2Y)
   {
      boolean filler = listener.validateMove(location[tile1Y][tile1X], location[tile2Y][tile2X]);
   }

   /**
   *Method to update the chat box
   *@param message The message to be shown
   */
   public void updateChat(String message)
   {
      chatWindow.setText(chatWindow.getText()+"\n"+message);
   }
   
   /**
   *Method to enable the chat box. Prevents chat before players connected
   */
   public void enableChat()
   {
      messageBox.setEditable(true);
   }
   
   /**
   *Inner class that handles the resizing event of the window
   */
   protected class ResizeHandler extends ComponentAdapter
   {
      
      @Override
      public void componentResized(ComponentEvent e)
      {
         resizeImages();
      }
      
   }
}