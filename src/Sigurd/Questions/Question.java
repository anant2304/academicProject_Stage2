package Sigurd.Questions;

import java.util.Iterator;
import Sigurd.*;
import java.util.Stack;

import Cards.*;

public class Question extends AbstractQuestion {
    private Stack<Player> playersToAsk;
    
    private Card answerCard;
    
    public Question(Player player, Iterable<Player> allPlayers)
    {
        super(player);
        SetupPlayersToAsk(allPlayers);
        answerCard = null;
    }
    
    private void SetupPlayersToAsk(Iterable<Player> players) {
        if(players.iterator().hasNext() == false)
            throw new IllegalArgumentException("No other players to ask question to");
        
        playersToAsk = new Stack<Player>();
        
        // Put players after current player into stack
        Iterator<Player> it = players.iterator();
        while(it.hasNext() && it.next().equals(asker) == false);
        while(it.hasNext()) playersToAsk.push(it.next());
        
        // Put players before current player into stack
        it = players.iterator();
        Player p;
        while (it.hasNext() && (p = it.next()) != asker) playersToAsk.push(p);
    }
    
    public void StartAskingQuestion(Room r)
    {
        room = Game.GetCard(r.GetName(), RoomCard.class);
        Activate();
    }
    
    @Override
    public void Commands(String command) {
        if(HasBeenAnswered() || IsNoAnswer()){
            EndQuestion();
        } 
        else if(IsDoneAsking() == false) {
            super.Commands(command);
        } 
        else if(playersToAsk.isEmpty()) {
            throw new IllegalStateException("There are no more players to ask");
        } 
        else {
            if(command.equals("notes"))
                Game.GetDisplay().SendMessage(playersToAsk.peek().GetNotes());
            
            else if(command.equals("done"))
                AskNextPlayer();
            
            else if(command.equals("question"))
                Game.GetDisplay().SendMessage(this.toString());
            
            else if(HasQuestionCard()){
                
                Card shownCard = null;
                if(command.equals(character.getName()))
                    shownCard = character;
                else if(command.equals(weapon.getName()))
                    shownCard = weapon;
                else if(command.equals(room.getName()))
                    shownCard = room;
                
                if(shownCard != null && playersToAsk.peek().HasCard(shownCard) && shownCard.CanEveryOneSee() == false)
                    ValidAnswer(shownCard);
                else
                    Game.GetDisplay().SendMessage("That is not one of the cards that was asked about\n"
                            + "Please input the name of the card you want to display.");
            }
            else
                Game.GetDisplay().SendMessage("That is not one of the cards that was asked about\n"
                        + "If you do not have any of the cards, type \"done\" to go to the next player");
        }
    }

    private boolean HasQuestionCard() {
        return playersToAsk.peek().HasCard(character) || 
                playersToAsk.peek().HasCard(weapon)   ||
                playersToAsk.peek().HasCard(room);
    }

    public boolean HasBeenAnswered()
    {
        return answerCard != null;
    }
    
    private void AskNextPlayer() {
        if(HasQuestionCard()) {
            Game.GetDisplay().SendMessage("You own at least one card that was asked about\n"
                    + "Please input the name of the card you want to show.");
        }
        else {
            playersToAsk.pop();
            if(playersToAsk.isEmpty())
                NoPlayerHasCard();
            else
                PromptPlayer();
        }
    }

    

    private void ValidAnswer(Card shownCard) {
        answerCard = shownCard;
        Game.GetDisplay().clearScreen();
        Game.GetDisplay().SendMessage(playersToAsk.peek() + " answered the question\n"
                 + asker + " can enter any input to see the card that was answered");
        
    }
    
    private void NoPlayerHasCard() {
        if(IsNoAnswer() == false)
            throw new IllegalStateException("Players still might have one of the cards");
        
        Game.GetDisplay().clearScreen();
        Game.GetDisplay().SendMessage("No players had the card that was entered\n" 
                                 + asker + " can enter any input to continue their turn");
    }
    
    private boolean IsNoAnswer() {
        return HasBeenAnswered() == false && playersToAsk.isEmpty();
    }

    private void EndQuestion() {
        Game.GetDisplay().clearScreen();
        if(HasBeenAnswered()) {
            Game.GetDisplay().SendMessage(playersToAsk.peek() + " showed you the card " + answerCard.getName());
            asker.SeeCard(answerCard);
        }
        else if(IsNoAnswer())
            Game.GetDisplay().SendMessage("No one had any of the cards you asked for");
        Game.GetDisplay().log(this + "\n" + (HasBeenAnswered()?playersToAsk.peek():"nobody") + " showed a card");
        Deactivate();
    }

    
    @Override
    protected void DoneWithInput() {
        character.getPlayerObject().MoveToRoom(room.getRoom());
        weapon.getWeapon().MoveToRoom(room.getRoom());
        
        PromptPlayer();
    }
    
    public void ResetCanAsk()
    {
        canAsk = false;
    }
    
    private void PromptPlayer() {
        Game.GetDisplay().clearScreen();
        Game.GetDisplay().SendMessage(this.toString());
        Game.GetDisplay().SendMessage("It is " + playersToAsk.peek() + "'s turn to answer\n"
                + "Input \"help\" for help");
    }
    
    @Override
    public String toString()
    {
        return asker + " aksked if it was " + 
                character.getName() + " in the " + 
                room.getName() + " with the " + 
                weapon.getName();
    }
}
