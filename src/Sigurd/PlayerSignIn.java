package Sigurd;
import java.awt.*;
import java.util.Scanner;

import Sigurd.BoardObjects.PlayerObject;

import java.util.LinkedList;

/*  Class PlayerSignIn  */
class PlayerSignIn
{
    LinkedList<PlayerObject> list = new LinkedList<PlayerObject>();
    LinkedList<String> nameList=new LinkedList<String>();
    protected String name;
    protected PlayerSignIn link;
    public int numPlayers;
    boolean isnumPlayersSet;
    CommandPanel command;
    DisplayPanel display;
    
    public PlayerSignIn()
    {
        command=Game.GetCommand();
        display= Game.GetDisplay();
        link=null;
        name="";
        isnumPlayersSet=false;
    }
    public PlayerSignIn(String n,PlayerSignIn l)
    {
        name=n;
        link=l;
    }
    /*Function to set link to next PlayerSignIn node*/
    public void setLink(PlayerSignIn n)
    {
        link = n;
    }
    /*Function to set data to current PlayerSignIn node*/
    public void setData(String n)
    {
        name=n;
    }
    
    public void add(PlayerObject e)
    {
        list.addLast(e);
    }
    
    /*Function to get link to next node*/
    public PlayerSignIn getLink()
    {
        return link;
    }
    /*Function to get data from current PlayerSignIn node*/
    public String getName()
    {
        return name;
    }
    public void askPlayerNames()
    {
        Game.GetDisplay().sendMessage("Enter the number of players\n");
        if(isnumPlayersSet==false)
        {
            
        }
    }
    
    public void takeInput(String input)
    {
        if(isnumPlayersSet==false)
        {
            if(Character.isDigit(input.charAt(0)))
            {
                numPlayers=Integer.parseInt(input);
                isnumPlayersSet=true;
                Game.GetDisplay().sendMessage("Hello\n");
            }
        }
        else
            if(isnumPlayersSet==false && !Character.isDigit(input.charAt(0)))
            {
                Game.GetDisplay().sendMessage("Wrong Input\n Enter the number of players\n");
            }
            else
                if(isnumPlayersSet==true && !Character.isDigit(input.charAt(0)) && !input.equalsIgnoreCase("Done"))
                {
                    nameList.addLast(input);
                }
                else
                    if(input.equalsIgnoreCase("Done"))
                    {
                        if(nameList.size()==numPlayers)
                        {
                            Game.StartGame();
                        }
                        else
                        {
                            Game.GetDisplay().sendMessage("Wrong number of players inputted\n");
                        }
                    }
        for(int i=0;i<nameList.size();i++)
        {
            Game.GetDisplay().sendMessage(nameList.get(i));
        }
    }
    
    public static void main(String[] args)
    {
        
        
    				
    				PlayerSignIn obj=new PlayerSignIn();
    				Scanner scan = new Scanner(System.in);
        System.out.println("Circular Singly Linked List Test\n");
        char ch;
        do
        {
            System.out.println("\nCircular Singly Linked List Operations\n");
            System.out.println("1. insert");
            System.out.println("5. check empty");
            System.out.println("6. get size");
            int choice = scan.nextInt();
            switch (choice)
            {
                case 1 :
                    System.out.println("Enter string element to insert");
                    obj.name="Peter";
                    obj.list.add(new PlayerObject(Coordinates.UP, Color.BLACK, "Peter", "Adrain"));
                    break;
                case 5 :
                    System.out.println("Empty status = "+ obj.list.isEmpty());
                    break;
                case 6 :
                    System.out.println("Size = "+ obj.list.size() +" \n");
                    break;
                default :
                    System.out.println("Wrong Entry \n ");
                    break;
            }
            System.out.println(obj.getName());
            System.out.println("\nDo you want to continue (Type y or n) \n");
            ch = scan.next().charAt(0);
        } while (ch == 'Y'|| ch == 'y');
    } 
}
