/*
 * MovementCard.java
 *
 * Created on October 18, 2006, 1:28 PM
 *
 * by Ethan Shepherd
 */

/* 
 * The MovementCard class parses the card type strings into image indexes, and holds
 * information about whether the card is still in the deck, or is in the card pool. 
 * DeckOfCards creates an array of 84 MovementCards.
 */

package robotrace;

public class MovementCard {
private int cardNumber, cardSpeed;
private String cardType;
private int imageLoaderIndex;
private boolean isInDeck;
private boolean isInPool;

    public MovementCard(int cardNumber, int cardSpeed, String cardType) {
        this.cardNumber = cardNumber;
        this.cardSpeed = cardSpeed;
        this.cardType = cardType;
        isInDeck = true;
        
        if (cardType.matches("Hidden"))
            this.imageLoaderIndex = 0;
        else if (cardType.matches("BackUp"))
            this.imageLoaderIndex = 1;
        else if (cardType. matches("Move1"))
            this.imageLoaderIndex = 2;
        else if (cardType. matches("Move2"))
            this.imageLoaderIndex = 3;
        else if (cardType. matches("Move3"))
            this.imageLoaderIndex = 4;
        else if (cardType. matches("RotateLeft"))
            this.imageLoaderIndex = 5;
        else if (cardType. matches("RotateRight"))
            this.imageLoaderIndex = 6;
        else if (cardType. matches("UTurn"))
            this.imageLoaderIndex = 7;
    }
    public int getCardNumber(){
        return cardNumber;
    }
    public String getCardType(){
        return cardType;
    }
    public int getCardSpeed(){
        return cardSpeed;
    }
    public void setIsInDeck(boolean x){
        isInDeck = x;
    }
    public boolean getIsInDeck(){
        return isInDeck;
    }
    public void setIsInPool(boolean x){
        isInPool = x;
    }
    public boolean getIsInPool(){
        return isInPool;
    }
    public int getIndex(){
        return imageLoaderIndex;
    }
    
}
