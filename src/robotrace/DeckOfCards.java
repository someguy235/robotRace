/*
 * DeckOfCards.java
 *
 * Created on October 18, 2006, 1:25 PM
 *
 * by Ethan Shepherd
 */

/*
 * DeckOfCards builds the deck of useable MovementCards (creates 84 unique Movement
 * Card objects), and keeps track of which ones are currently in the deck (can be drawn). 
 * It includes a Random function so that random cards can be drawn from the deck.
 */

package robotrace;

import java.util.Random;

public class DeckOfCards {
    MovementCard[] cards = new MovementCard[84];  
    Random generator = new Random();
    int randomCard, numCards = 84;
    
    //creates each of the MovementCards. The second value passed is the unique speed
    //of the card, for resolving conflicting moves.
    public DeckOfCards() {
        for (int i=0; i<18; i++)
            cards[i] = new MovementCard(i, i*10+490, "Move1");
        for (int i=18; i<30; i++)
            cards[i] = new MovementCard(i, i*10+490, "Move2");
        for (int i=30; i<36; i++)
            cards[i] = new MovementCard(i, i*10+490, "Move3");
        for (int i=36; i<42; i++)
            cards[i] = new MovementCard(i, i*10+70, "BackUp");
        for (int i=42; i<60; i++)
            cards[i] = new MovementCard(i, i*20-770, "RotateLeft");
        for (int i=60; i<78; i++)
            cards[i] = new MovementCard(i, i*20-1120, "RotateRight");
        for (int i=78; i<84; i++)
            cards[i] = new MovementCard(i, i*10-770, "UTurn");
    }
    
    public MovementCard getRandomCard(){
        int randomCard = generator.nextInt(84);

        while(!(cards[randomCard].getIsInDeck())){
            randomCard = generator.nextInt(84);
        }
        cards[randomCard].setIsInDeck(false);
        numCards--;
        return cards[randomCard];
    }
    
    public void returnCardToDeck(MovementCard returnCard){
        returnCard.setIsInDeck(true);
        numCards++;
    }
    
    public MovementCard getCard(int card){
        return cards[card];
    }
    
    public int getNumCards(){
        return numCards;
    }
}
