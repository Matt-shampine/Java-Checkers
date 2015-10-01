
import javax.swing.*;
import java.awt.*;

/**
*Class that handles game logic such as score keeping
*@author Matthew Shampine
*/
public class GameLogic
{

   int player1Cap;
   int player2Cap;
   int player1Kings;
   int player2Kings;
   boolean antiCheckers;
   
   /**
   *Method to add to the capture count
   *@param team The team number to add the score to
   */
   public void capCount(int team)
   {
      if(team == 1)
      {
         player1Cap++;
      }
      else if(team == 2)
      {
         player2Cap++;
      }
   }
   
   /**
   *Method to add a king to the players score. Does not add a king to the GUI
   *@param team The team number to add the king to
   */
   public void addKing(int team)
   {
      if(team == 1)
      {
         player1Kings++;
      }
      else if(team == 2)
      {
         player2Kings++;
      }
   }
   
   /**
   *Method to subtract a king if the king gets captured
   *@param team The team number whos king was catpured
   */
   public void subtractKing(int team)
   {
      if(team == 1)
      {
         player1Kings--;
      }
      else if(team == 2)
      {
         player2Kings--;
      }
   }
   
   /**
   *Method to set the kings to a value. Used by the new game method
   *@param num The number to be set to
   *@param team The team number
   */
   public void setKings(int num, int team)
   {
      if(team == 1)
      {
         player1Kings = num;
      }
      else if(team == 2)
      {
         player2Kings = num;
      }
   }
   
   /**
   *Method to set the players counters to 0. Used by the new game method
   *@param num The number to be set to
   *@param team The team number
   */
   public void setCapCount(int capNum, int team)
   {
      if(team == 1)
      {
         player1Cap = capNum;
      }
      else if(team == 2)
      {
         player2Cap = capNum;
      }
   }
   
   /**
   *Method to get the count of pieces captured by player 1
   *@return int
   */
   public int getPlayer1Cap()
   {
      return player1Cap;
   }
  
  /**
  *Method to get the count of pieces captured by player 2
  *@return int
  */ 
   public int getPlayer2Cap()
   {
      return player2Cap;
   }
   
   /**
   *Method to set antiCheckers
   *@param val Boolean to play or not play with this format
   */
   public void setAnti(boolean val)
   {
      antiCheckers = val;
   }
   
   /**
   *Method to check if anticheckers is set or not
   *@return boolean
   */
   public boolean getAnti()
   {
      return antiCheckers;
   }
   
   /**
   *Method to check if the player has won
   *@return boolean
   */
   public boolean victory()
   {
      if(player1Cap == 12 || player2Cap == 12)
      {
         return true;
      }
      else if(player1Kings == 5 || player2Kings == 5)
      {
         return true;
      }
      return false;
   }
   
}