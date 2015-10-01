
import java.io.*;
import java.net.*;
import java.util.*;

/**
*Class that handles the server. Server has its own main method
*@author Matthew Shampine
*/
public class Server extends Thread
{
   
   Vector<ConnectionThread> connections;
   Vector<ConnectionThread> waitingThreads;
   
   /**
   *Method to run the server itself
   */
   public void run()
   {
      try
      {
         ServerSocket serverSocket = new ServerSocket(6066);
         connections = new Vector<ConnectionThread>();
         waitingThreads = new Vector<ConnectionThread>();
         
         System.out.println("Listening on port 6066...");
         while(true)
         {
            Socket newConnection = serverSocket.accept();
            (new ConnectionThread(newConnection)).start();
         }
      }
      catch(IOException e)
      {
         e.printStackTrace();
      }
   }
   
   /**
   *Main method for the server
   *@param args String array for command line arguments
   */
   public static void main(String[] args)
   {
      (new Server()).start();
   }
   
   /**
   *Inner class for each connection made to the server
   */
   protected class ConnectionThread extends Thread
   {
      BufferedReader input;
      PrintWriter output;
      
      PrintWriter p2Output;
      
      Socket socket;
      String name;
      String p2Name;
      int team;
      
      /**
      *Constructor for the client thread on the server
      *@param _socket The clients socket
      */
      public ConnectionThread(Socket _socket)
      {
         try
         {
            output = new PrintWriter(new OutputStreamWriter(_socket.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
         }
         catch(IOException e)
         {  
            e.printStackTrace();
         }
      }
      
      /**
      *Handles all data coming from the client to the server
      */
      public void run()
      {
         
         connections.add(this);
         
         while(true)
         {
            String command = "";
            try
            {
               command = input.readLine();
            }
            catch(IOException e)
            {
               System.out.println("Player: "+getPlayerName()+" has disconnected");
               notifyOtherPlayer();
               connections.remove(this);
               waitingThreads.remove(this);
               break;
            }
            String[] data = command.split(" ");
            if(data[0].equals("$chat"))
            {
               //If the command is for chat
               String message = "$chat "+name+":";
               for(String word : data)
               {
                  if(!(word.equals("$chat")))
                  {
                     message = message + " " + word;
                  }
               }
               p2Output.println(message);
               p2Output.flush();
            }
            else if(data[0].equals("$name"))
            {
               //If the command is to set the name
               name = data[1];
            }
            else if(data[0].equals("$join"))
            {
               //If the command is for joining
               if(p2Output == null)
               {
                  double randomNumber = Math.random();
                  for(ConnectionThread player : connections)
                  {
                     if(player.getPlayerName().equals(data[1]))
                     {
                        p2Output = player.getOutput();
                        p2Name = player.getPlayerName();
                        p2Output.println("$join "+this.getPlayerName());
                        p2Output.flush();
                        int p2Team = player.getTeam();
                        if(p2Team == 0)
                        {
                           if(randomNumber > 0.0)
                           {
                              team = 1;
                              output.println("$team 1");
                              output.flush();
                           }
                           else
                           {
                              team = 2;
                              output.println("$team 2");
                              output.flush();
                           }
                        }
                        else if(p2Team == 1)
                        {
                           team = 2;
                           output.println("$team 2");
                           output.flush();
                        }
                        else
                        {
                           team = 1;
                           output.println("$team 1");
                           output.flush();
                        }
                     }
                  }
                  if(waitingThreads.contains(this))
                  {
                     waitingThreads.remove(this);
                  }
               }
            }
            else if(data[0].equals("$newgame"))
            {
               //If the command is for starting a new game
               waitingThreads.add(this);
            }
            else if(data[0].equals("$getWaitingList"))
            {
               //If the command is for the waiting list
               String namesList = "$waitingPlayers";
               for(ConnectionThread player : waitingThreads)
               {
                  namesList = namesList+" "+player.getPlayerName();
               }
               output.println(namesList);
               output.flush();
            }
            else if(data[0].equals("$getAllNames"))
            {
               String namesList = "$allNames";
               for(ConnectionThread player : connections)
               {
                  namesList = namesList+" "+player.getPlayerName();
               }
               output.println(namesList);
               output.flush();
            }
            else if(data[0].equals("$data"))
            {
               //If the command is for movment data
               p2Output.println(command);
               p2Output.flush();
            }
         } 
      }
      
      /**
      *Getter method for this threads name
      *@return String
      */
      public String getPlayerName()
      {
         return name;
      }
      
      /**
      *Getter method for the output stream of the opponent player
      *@return PrintWriter
      */
      public PrintWriter getOutput()
      {
         return output;
      }
      
      /**
      *Getter method for the team number
      *@return int
      */
      public int getTeam()
      {
         return team;
      }
      
      /**
      *Alert the other player that a player has left the game and is no longer playing.
      */
      public void notifyOtherPlayer()
      {
         //Tell other player that this player has quit
         if(p2Output != null)
         {
            p2Output.println("$chat Server: The other player has left the game");
            p2Output.flush();
         }
      }
      
   }
   
}