
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.event.*;
import javax.swing.*;

/**
*Class that deals with all movment of pieces
*@author Matthew Shampine
*/
public class MoveListener implements ActionListener
{
   GameLogic logic;
   Networking net;
   GUI gui;
   Tile[][] location;
   Tile selected;
   int playerTeam;
   int turn = 0;
   String moveData = null;
   
   ImageIcon player1 = new ImageIcon(getClass().getResource("player1.png"));
   ImageIcon player1Selected = new ImageIcon(getClass().getResource("player1Selected.png"));
   ImageIcon player1King = new ImageIcon(getClass().getResource("player1King.png"));
   ImageIcon player1KingSelected = new ImageIcon(getClass().getResource("player1KingSelected.png"));
   
   ImageIcon player2 = new ImageIcon(getClass().getResource("player2.png"));
   ImageIcon player2Selected = new ImageIcon(getClass().getResource("player2Selected.png"));
   ImageIcon player2King = new ImageIcon(getClass().getResource("player2King.png"));
   ImageIcon player2KingSelected = new ImageIcon(getClass().getResource("player2KingSelected.png"));
   
   /**
   *Constructor for the MoveListener
   *@param _location Takes a 2D array of Tile objects
   */
   public MoveListener(Tile[][] _location, GUI _gui, Networking _net)
   {
      location = _location;
      gui = _gui;
      net = _net;
      logic = new GameLogic();
   }
   
   @Override
   public void actionPerformed(ActionEvent e)
   {
      playerTeam = net.getTeam();
      
      JButton btn = (JButton) e.getSource();
      Tile tile = (Tile) btn.getParent();
      int team = tile.getTeam();
      //Check to see if it is player 1's turn and they are picking their own peice
      if(playerTeam == 1 && turn == 0 && team == 1)
      {
         if(tile.isKing())
         {
            btn.setIcon(player1KingSelected);
         }
         else
         {
            btn.setIcon(player1Selected);
         }
         if(selected != null)
         {
            JButton prevBtn = (JButton) selected.getComponent(0);
            if(selected.isKing())
            {
               prevBtn.setIcon(player1King);
            }
            else
            {
               prevBtn.setIcon(player1);
            }
            gui.resizeTile(selected);
         }
         gui.resizeTile(tile);
         selected = tile;
      }
      //Check to see if it is player 2's turn and they are picking their own peice
      else if(playerTeam == 2 && turn == 1 && team == 2)
      {
         if(tile.isKing())
         {
            btn.setIcon(player2KingSelected);
         }
         else
         {
            btn.setIcon(player2Selected);
         }
         if(selected != null)
         {
            JButton prevBtn = (JButton) selected.getComponent(0);
            if(selected.isKing())
            {
               prevBtn.setIcon(player2King);
            }
            else
            {
               prevBtn.setIcon(player2);
            }
            gui.resizeTile(selected);
         }
         gui.resizeTile(tile);
         selected = tile;
      }
      //Check to see if they selected the peice they wish to move already
      else if(selected != null)
      {
         JButton srcButton = (JButton)e.getSource();
         //If true, validate the move with the selected tile and the destination tile
         boolean valid = validateMove(selected, (Tile)srcButton.getParent());
         if(valid)
         {
            net.endTurn(moveData);
            moveData = null;
         }
      }
      //If player clicks on their piece out of turn, alert them
      else if(team != 0)
      {
         JOptionPane.showMessageDialog(null, "Not your turn");
      }
   }
   
   /*
   *A helper method to reset the turn counter back to player 1
   */
   public void resetTurn()
   {
      turn = 0;
   }
   
   /**
   *Method to validate each move made by a peice
   *@param source The source Tile to be used
   *@param dest Destination Tile to be used
   */
   public boolean validateMove(Tile source, Tile dest)
   {  
      //Get all data used thru out the method
      int srcX = source.getLocX();
      int srcY = source.getLocY();
      int destX = dest.getLocX();
      int destY = dest.getLocY();
      
      //Check to see if the peice is a king and attacking
      if(source.isKing() && !(dest.isOccupied()) && (srcY - 2 == destY || srcY + 2 == destY))
      {
         moveData = checkAttack(source, dest);
         return true;
      }
      //Check to see if a king is just moving
      else if(source.isKing() && !(dest.isOccupied()) && (srcY + 1 == destY || srcY - 1 == destY) && (srcX + 1 == destX || srcX - 1 == destX))
      {
         moveData = move(source, dest);
         return true;
      } 
      ///Check to see if player 1 is attacking
      else if(turn == 0 && !(dest.isOccupied()) && srcY - 2 == destY)
      {
         moveData = checkAttack(source, dest);
         return true;
      }
      //Check to see if player 1 is moving
      else if(turn == 0 && !(dest.isOccupied()) && srcY - 1 == destY && (srcX + 1 == destX || srcX - 1 == destX))
      {
         moveData = move(source, dest);
         return true;
      }
      //Check to see if player 2 is attacking
      else if(turn == 1 && !(dest.isOccupied()) && srcY + 2 == destY)
      {
         moveData = checkAttack(source, dest);
         return true;
      }
      //Check to see if player 2 is just moving
      else if(turn == 1 && !(dest.isOccupied()) && srcY + 1 == destY && (srcX + 1 == destX || srcX - 1 == destX))
      {
         moveData = move(source,dest);
         return true;
      }
      else if(dest.isOccupied() &&  (srcY + 1 == destY || srcY - 1 == destY) && (srcX + 1 == destX || srcX - 1 == destX))
      {
         JOptionPane.showMessageDialog(null, "Space is already occupied");
         return false;
      }
      else
      {
         JOptionPane.showMessageDialog(null, "Invalid move:\nMovement is only 1 space diagonally away from your starting side\nKings can only move 1 space diagonally in either direction\nAttacks are 2 spaces diagonally over an enemy piece");
         return false;
      }
   }
   
   /**
   *Moves the piece from the source tile to the dest tile
   *@param source The source tile
   *@param dest The destination tile
   *@return String The data from the source and destination
   */
   public String move(Tile source, Tile dest)
   {
      JButton srcButton = (JButton)source.getComponent(0);
      JButton destButton =(JButton)dest.getComponent(0);
      
      //File sourceImage = null;
      ImageIcon sourceImage = null;
      
      //Check to see what team to load the image for
      if(source.getTeam() == 1)
      {
         if(source.isKing())
         {
            sourceImage = player1King;
         }
         else
         {
            sourceImage = player1;
         }
      }
      else if(source.getTeam() == 2)
      {
         if(source.isKing())
         {
            sourceImage = player2King;
         }
         else
         {
            sourceImage = player2;
         }
      }
      // Image image = null;
//       //Load the image to be displayed
//       try
//       {
//          image = ImageIO.read(sourceImage);
//       } 
//       catch(Exception e) 
//       {
//          e.printStackTrace();
//       }
      
      if(turn == 0)
      {
         srcButton.setIcon(null);
         destButton.setIcon(sourceImage);
         source.setTeam(0);
         dest.setTeam(1);
         checkKing(dest);
         turn = 1;
      }
      else if(turn == 1)
      {
         srcButton.setIcon(null);
         destButton.setIcon(sourceImage);
         source.setTeam(0);
         dest.setTeam(2);
         checkKing(dest);
         turn = 0;
      }
      source.setOccupied(false);
      dest.setOccupied(true);
      selected = null;
      if(source.isKing())
      {
         source.setKing(false);
         dest.setKing(true);
      }
      gui.resizeTile(dest);
      //Check to see if the move won the game
      if(logic.victory())
      {
         if(!(logic.getAnti()))
         {
            JOptionPane.showMessageDialog(null,"Player "+dest.getTeam()+" wins!");
         }
         else
         {
            if(dest.getTeam() == 1)
            {
               JOptionPane.showMessageDialog(null,"Player "+(dest.getTeam()+1)+" wins!");
            }
            else
            {
               JOptionPane.showMessageDialog(null,"Player "+(dest.getTeam()-1)+" wins!");
            }
         }
      }
      return source.getData()+" "+dest.getData();
   }
   
   /**
   *Method to check that the attack is valid.
   *@param source The tile that the peice is attaking from.
   *@param dest The destination tile.
   */
   private String checkAttack(Tile source, Tile dest)
   {
      //Get all relevent data
      int srcX = source.getLocX();
      int srcY = source.getLocY();
      int destX = dest.getLocX();
      int difference = destX - srcX;
      int attackTeam = source.getTeam();
      
      String moveData = null;
      
      //Make sure it won't check for a negative location in the array
      if(srcX - 1 != -1)
      {
         if(srcY - 1 != -1)
         {
             if((srcX - 2 == destX || srcX + 2 == destX) && (attackTeam == 1 || source.isKing()))
             {
               //Check to see if the space Y-1 and X - 1 is occupied by an enemy
               if(location[srcY-1][srcX-1].isOccupied() && location[srcY-1][srcX-1].getTeam() != attackTeam && difference == -2)
               {
                  capture(location[srcY-1][srcX-1]);
                  moveData = move(source, dest);
               }
               else if(srcX+1 != 8)
               {
                  //Check to see if the space Y-1 and X + 1 is occupied by an enemy.
                  if(location[srcY-1][srcX+1].isOccupied() && location[srcY-1][srcX+1].getTeam() != attackTeam && difference == 2)
                  {
                     capture(location[srcY-1][srcX+1]);
                     moveData = move(source, dest);
                  }
               }
            }
         }
         if(srcY + 1 != 8)
         {
            //Check to see if player 2 is attacking
            if((srcX - 2 == destX || srcX + 2 == destX) && (attackTeam == 2 || source.isKing()))
            {
               //Check to see if Y+1, X-1 is occupied by an enemy
               if(location[srcY+1][srcX-1].isOccupied() && location[srcY+1][srcX-1].getTeam() != attackTeam && difference == -2)
               {
                  capture(location[srcY+1][srcX-1]);
                  moveData = move(source, dest);
               }
               //Check to make sure X won't go to 8
               else if(srcX + 1 != 8)
               {
                  //Check to see if Y+1, X+1 is occupied by an enemy
                  if(location[srcY+1][srcX+1].isOccupied() && location[srcY+1][srcX+1].getTeam() != attackTeam && difference == 2)
                  {
                     capture(location[srcY+1][srcX+1]);
                     moveData = move(source, dest);
                  }
               }
            }
         }
      }
      else if(attackTeam == 1 && location[srcY-1][srcX+1].isOccupied() && location[srcY-1][srcX+1].getTeam() != attackTeam && difference == 2)
      {
         capture(location[srcY-1][srcX+1]);
         moveData = move(source, dest);
      }
      else if(attackTeam == 2 && location[srcY+1][srcX+1].isOccupied() && location[srcY+1][srcX+1].getTeam() != attackTeam && difference == 2)
      {
         capture(location[srcY+1][srcX+1]);
         moveData = move(source, dest);
      }
      return moveData;
   }
   
   /**
   *Method that executes the capture of a piece and increments the other teams counter
   *@param capture The tile to be captured
   */
   public void capture(Tile capture)
   {
      capture.setOccupied(false);
      int team = capture.getTeam();
      //Add to the team who captured the opposing piece
      if(team == 1)
      {
         logic.capCount(team+1);
         gui.addPoint(team+1);
      }
      else
      {
         logic.capCount(team-1);
         gui.addPoint(team-1);
      } 
      capture.setTeam(0);
      JButton btn = (JButton) capture.getComponent(0);
      if(capture.isKing())
      {
         logic.subtractKing(team);
         capture.setKing(false);
      }
      btn.setIcon(null);
   }
   
   /**
   *Method to see if the piece has become a king
   *@param dest Destination tile
   */
   private void checkKing(Tile dest)
   {
      JButton btn = (JButton) dest.getComponent(0);
      //Check to see which players turn it is
      if(turn == 0)
      {
         if(dest.getLocY() == 0)
         {
            //If the piece is not a king and became one add to the king counter
            if(!(dest.isKing()))
            {
               logic.addKing(dest.getTeam());
            }
            dest.setKing(true);
            btn.setIcon(player1King);
         }
      }
      else if(turn == 1)
      {
         if(dest.getLocY() == 7)
         {
            //If the piece is not a king and became one add to the king counter
            if(!(dest.isKing()))
            {
               logic.addKing(dest.getTeam());
            }
            dest.setKing(true);
            btn.setIcon(player2King);
         }
      }
   }
}