
import javax.swing.*;
import java.awt.event.*;

/**
*Class that handles the menu
*@author Matthew Shampine
*/
public class MenuBar extends JMenuBar
{
 
   GUI gui;
   GameLogic logic;
   OnlinePlayersList playerList;
   
   /**
   *Constructor for the MenuBar
   *@param _gui An instance of the GUI
   */
   public MenuBar(GUI _gui, GameLogic _logic)
   {
      gui = _gui;
      logic = _logic;
      createMenuItems();
   }
   
   /**
   *Method that creates and add the menu bar to the frame
   */
   private void createMenuItems()
   {
      JMenu fileMenu = new JMenu("File");
      JMenu optionsMenu = new JMenu("Options");
      
      JMenuItem newGameItem = new JMenuItem("New Game");
      JMenuItem playerWindow = new JMenuItem("Server/Players List");
      JMenuItem closeItem = new JMenuItem("Exit");
      JMenuItem aboutItem = new JMenuItem("About");
      JCheckBoxMenuItem antiCheckers = new JCheckBoxMenuItem("AntiCheckers");
      
      MenuListener menuListener = new MenuListener();
      
      newGameItem.addActionListener(menuListener);
      closeItem.addActionListener(menuListener);
      aboutItem.addActionListener(menuListener);
      antiCheckers.addActionListener(menuListener);
      playerWindow.addActionListener(menuListener);
      
      //fileMenu.add(newGameItem);
      fileMenu.add(playerWindow);
      fileMenu.addSeparator();
      fileMenu.add(closeItem);
      
      optionsMenu.add(aboutItem);
      //optionsMenu.add(antiCheckers);
      
      this.add(fileMenu);
      this.add(optionsMenu);
   }
   
   public void addPlayerList(OnlinePlayersList _playerList)
   {
      playerList = _playerList;
   }
   
   /**
   *A listener class to check for actions on the menu
   */
   protected class MenuListener implements ActionListener
   {
      
      @Override
      public void actionPerformed(ActionEvent e)
      {
         String source = e.getActionCommand();
         if(source.equals("New Game"))
         {
            gui.newGame();
            gui.resizeImages();
         }
         else if(source.equals("Exit"))
         {
            System.exit(0);
         }
         else if(source.equals("About"))
         {
            JOptionPane.showMessageDialog(null,"Checkers designed and coded by:\n Brian Coleman\n Matthew Shampine\n Timothy Ascencio","About",JOptionPane.INFORMATION_MESSAGE);
         }
         else if(source.equals("AntiCheckers"))
         {
            JCheckBoxMenuItem anti = (JCheckBoxMenuItem)e.getSource();
            if(anti.isSelected())
            {
               logic.setAnti(true);
               System.out.println("Anti true");
            }
            else
            {
               logic.setAnti(false);
               System.out.println("Anti false");
            }
         }
         else if(source.equals("Server/Players List"))
         {
            playerList.makeVisible();
         }
      }
      
   }
   
}