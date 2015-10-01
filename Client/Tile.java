

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
*Tile class represents the pieces on the board
*@author Matthew Shampine
*/
public class Tile extends JPanel implements Serializable
{
   
   boolean occupied;
   boolean isKing;
   int team;
   int locX;
   int locY;
   
   /**
   *Constructor for the tile class. Uses a BorderLayout to fill space
   *@param x An int for the X cord
   *@param y An int for the Y cord
   */
   public Tile(int x, int y)
   {
      locX = x;
      locY = y;
      this.setLayout(new BorderLayout());
      this.setSize(100,100);
   }
   
  /**
   *A method to set the tile to occupied or not
   *@param _occupied A boolean
   */ 
   public void setOccupied(boolean _occupied)
   {
      occupied = _occupied;
   }
   
   /**
   *A method to check if the tile is occupied or not
   *@return boolean
   */
   public boolean isOccupied()
   {
      return occupied;
   }
   
   /**
   *Method to set the team number
   *@param i The team number as an int
   */
   public void setTeam(int i)
   {
      team = i;
   }
   
   /**
   *Method to get the team associated with this tile
   *@return int
   */
   public int getTeam()
   {
      return team;
   }
   
   /**
   *Method to get the X value of the tile
   *@return int
   */
   public int getLocX()
   {
      return locX;
   }
   
   /**
   *Method to get the Y value of the tile
   *@return int
   */
   public int getLocY()
   {
      return locY;
   }
   
   /**
   *A method to check if the piece is a king
   *@return boolean
   */
   public boolean isKing()
   {
      return isKing;
   }
   
   /**
   *Method to set the piece to king
   *@param _king A boolean
   */
   public void setKing(boolean _king)
   {
      isKing = _king;
   }
   
   /**
   *Gets all data affiliated with the tile
   *@return String All data from tile
   */
   public String getData()
   {
      return ""+locX+" "+locY;
   }
   
   /**
   *A method to quickly get the information from the tile
   *@return String
   */
   @Override
   public String toString()
   {
      return "Occupied: "+occupied+"\n X: "+locX+"\n Y:"+locY;
   }
   
}