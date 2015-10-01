
import java.net.*;
import java.io.*;
import java.util.*;

/**
*Class that handles all networking for the client
*@author Matthew Shampine
*/
public class Networking extends Thread
{
   
   //String serverName = "localhost";
   int port = 6066;
   Socket socket;
   PrintWriter output;
   BufferedReader input;
   boolean connected;
   int teamNumber;
   int turnNumber;
   boolean turnOver;
   String capture = "";
   GUI gui;
   String allNamesString = "";
   String waitingNamesString;
   String playerName;
   boolean playing;
   
   /**
   *Constructor for the networking class
   *@param _gui An instance of the user interface for the client
   */
   public Networking(GUI _gui)
   {
      gui = _gui;
   }
   
   public boolean connect(String serverName)
   {
      try
      {
         socket = new Socket(serverName, port);
         
         OutputStream toServer = socket.getOutputStream();
         output = new PrintWriter(new OutputStreamWriter(toServer));
         
         InputStream fromServer = socket.getInputStream();
         input = new BufferedReader(new InputStreamReader(fromServer));
         
         output.println("$newgame");
         output.flush();
         this.start();
         
         connected = true;
         turnOver = false; 
         playing = false;
         return true;
         
      }
      catch(IOException e)
      {
         //e.printStackTrace();
         return false;
      }
   }
   
   /**
   *Handle listening for the data from the server
   */
   public void run()
   {
      while(true)
      {
         try
         {
            String updateData = input.readLine();
            String[] data = updateData.split(" ");
            if(data[0].equals("$data") && turnOver)
            {
               int tile1X = Integer.parseInt(data[1]);
               int tile1Y = Integer.parseInt(data[2]);
               int tile2X = Integer.parseInt(data[3]);
               int tile2Y = Integer.parseInt(data[4]);
               if(data.length == 5)
               {
                  gui.updateScreen(tile1X, tile1Y, tile2X, tile2Y);
               }
            }
            else if(data[0].equals("$join"))
            {
               gui.updateChat("Server: "+data[1]+" has joined");
               output.println("$join "+data[1]);
               output.flush();
               playing = true;
            }
            else if(data[0].equals("$team"))
            {
               teamNumber = Integer.parseInt(data[1]);
               gui.updateChat("Server: You're player "+teamNumber);
               gui.enableChat();
               if(teamNumber == 2)
               {
                  turnOver = true;
               }
            }
            else if(data[0].equals("$chat"))
            {
               String textString = "";
               for(String word : data)
               {
                  if(!(word.equals("$chat")))
                  {
                     textString = textString+" "+word;
                  }
               }
               gui.updateChat(textString);
            }
            else if(data[0].equals("$allNames"))
            {
               System.out.println("Data found for all names");
               for(String word : data)
               {
                  if(!(word.equals("$allNames")))
                  {
                     allNamesString = allNamesString+" "+word;
                  }
               }
               System.out.println("Data done, string is: "+allNamesString);
            }
            else if(data[0].equals("$waitingPlayers"))
            {
               System.out.println("Data found for waiting list");
               for(String word : data)
               {
                  if(!(word.equals("$waitingPlayers")))
                  {
                     waitingNamesString = waitingNamesString+" "+word;
                  }
               }
            }
         }
         catch(IOException e)
         {
            e.printStackTrace();
         }
      }
   }
   
   /**
   *Method that tells the server to join players
   *@param playerName The name of the player to join
   */
   public void joinPlayer(String playerName)
   {
      output.println("$join "+playerName);
      output.flush();
   }
   
   /**
   *Method to set the player name server side
   *@param playerName The name of this client
   */
   public void setPlayerName(String playerName)
   {
      output.println("$name "+playerName);
      output.flush();
   }
   
   /**
   *Getter method for the turn number
   */
   public int getTurn()
   {
      return turnNumber;
   }
   
   /**
   *Method to end the turn and send data to the server
   *@param data Data to be sent to server 
   */
   public void endTurn(String data)
   {
      output.println("$data "+data);
      output.flush();
      turnOver = true;
   }
   
   /**
   *Method to send a chat message to the server
   *@message The message the other user will see
   */
   public void sendMessage(String message)
   {
      output.println(message);
      output.flush();
      String[] splitMessage = message.split(" ");
      String selfMessage = "You: ";
      for(String word : splitMessage)
      {
         if(!(word.equals("$chat")))
         {
            selfMessage = selfMessage+word+" ";
         }
      }
      gui.updateChat(selfMessage);
   }
   
   /**
   *Getter method for the team number
   */
   public int getTeam()
   {
      return teamNumber;
   }
   
   /**
   *Method to get all player names from the server
   *@return ArrayList<String>
   */
   public ArrayList<String> getAllPlayerNames()
   {
      ArrayList<String> allNames = new ArrayList<String>();
      

      output.println("$getAllNames");
      output.flush();
      
      System.out.println("Sent to server");
        
      while(allNamesString.equals(""))
      {
         System.out.println("No data yet");
      }
      
      System.out.println("Passed while loop");
      
      String[] arrayData = allNamesString.split(" ");
      for(int i = 1; i < arrayData.length; i++)
      {
         allNames.add(arrayData[i]);
      }
      
      allNamesString = "";
      
      System.out.println(allNames);
      
      return allNames;
   }
   
   /**
   *Method to get all players waiting in the waiting queue
   *@return ArrayList<String>
   */
   public ArrayList<String> getPlayersWaiting()
   {
      ArrayList<String> playerNames = new ArrayList<String>();

      output.println("$getWaitingList");
      output.flush();
      
      while(waitingNamesString == null)
      {
      
      }
       
      String[] arrayData = waitingNamesString.split(" ");
      for(int i = 1; i < arrayData.length; i++)
      {
         playerNames.add(arrayData[i]);
      }
      return playerNames;
   }
   
   /**
   *Method to check if the client is connected or not
   *@return boolean
   */
   public boolean isConnected()
   {
      return connected;
   }
   
   public void setLocalName(String name)
   {
      playerName = name;
   }
   
   public String getLocalName()
   {
      return playerName;
   }
   
   public boolean isPlaying()
   {
      return playing;
   }
   
}