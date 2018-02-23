package Sigurd;
import java.awt.*;
import java.util.Scanner;
 
/*  Class PlayerSignIn  */
class PlayerSignIn
{
	linkedList list = new linkedList();
	protected String name;
    protected PlayerSignIn link;
    //static CommandPanel command;
	//public static DisplayPanel display;
 
    /*  Constructor  */
    public PlayerSignIn()
    {
        link=null;
        name="";
        //command = new CommandPanel();
		//display = new DisplayPanel();
    }    
    /*  Constructor  */
    public PlayerSignIn(String n,PlayerSignIn l)
    {
        name=n;
        link=l;
    }    
    /*  Function to set link to next PlayerSignIn node */
    public void setLink(PlayerSignIn n)
    {
        link = n;
    }    
    /*  Function to set data to current PlayerSignIn  node*/
    public void setData(String n)
    {
        name=n;
    }    
    /*Function to get link to next node */
    public PlayerSignIn getLink()
    {
        return link;
    }    
    /*  Function to get data from current PlayerSignIn node */
    public String getName()
    {
        return name;
    }
    
    
    
}

 
/* Class linkedList */
class linkedList
{
    protected PlayerSignIn start;
    protected PlayerSignIn end;
    public int size;
 
    /* Constructor */
    public void display()
    {
        System.out.print("\nCircular Singly Linked List = ");
        PlayerSignIn ptr = start;
        if (size == 0) 
        {
            System.out.print("empty\n");
            return;
        }
        if (start.getLink() == start) 
        {
            System.out.print(start.getName()+ "->"+ptr.getName()+ "\n");
            return;
        }
        System.out.print(start.getName()+ "->");
        ptr = start.getLink();
        while (ptr.getLink() != start) 
        {
            System.out.print(ptr.getName()+ "->");
            ptr = ptr.getLink();
        }
        System.out.print(ptr.getName()+ "->");
        ptr = ptr.getLink();
        System.out.print(ptr.getName()+ "\n");
    }
    public linkedList()
    {
        start=null;
        end=null;
        size = 0;
    }
    /* Function to check if list is empty */
    public boolean isEmpty()
    {
        return start == null;
    }
    /* Function to get size of the list */
    public int getSize()
    {
        return size;
    }
    
    
    /* Function to insert element*/
    public void insert(String player)
    {
        PlayerSignIn nptr=new PlayerSignIn(player,null);    
        nptr.setLink(start);
        if(start==null)
        {            
            start=nptr;
            nptr.setLink(start);
            end=start;            
        }
        else
        {
            end.setLink(nptr);
            end = nptr;            
        }
        size++ ;
    }
    
    }
