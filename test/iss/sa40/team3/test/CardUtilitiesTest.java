package iss.sa40.team3.test;

import iss.sa40.team3.model.Card;
import iss.sa40.team3.utility.CardUtility;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class CardUtilitiesTest {
    
    private static List<Card> deck;
    private static Card[] table;
    private static int[] position;
    
    @BeforeClass
    public static void setup(){
        deck = new ArrayList<>(81);
        for(int number = 1; number< 4; number++){
            for(int shape = 1; shape < 4; shape++){
                for(int shading = 1; shading < 4; shading++){
                    for(int color = 1; color < 4; color++){
                        deck.add(new Card(number, shape, shading, color));
                    }
                }
            }
        }
        Collections.sort(deck);
        
        table = new Card[12];
        
        position = new int[]{1,2,3};
        
    }
    
//    @AfterClass
//    public static void cleanup(){
//        
//    }
    
    @Test
    public void testGetShuffledDeck(){
    
        List<Card> deck = CardUtility.getShuffledDeck();
        assertEquals(81, deck.size());
        
    }
    
    @Test
    public void testIssue12Cards(){
    
        List<Object> list = CardUtility.issue12Cards(deck, table);
        List<Card> deck69 = (List<Card>) list.get(0);
        Card[] table12 = (Card[]) list.get(1);
        assertEquals(69, deck69.size());
        assertEquals(12, table12.length);
        
    }
    
    @Test
    public void testIssue3Cards(){
        
        List<Object> list = CardUtility.issue3Cards(position, deck, table);
        List<Card> deck66 = (List<Card>) list.get(0);
        Card[] table12 = (Card[]) list.get(1);
        assertEquals(69, deck66.size());
        assertEquals(12, table12.length);
    }
    
    @Test
    public void testRemoveCards(){
        
        CardUtility.removeCards(position, table);
        
    }
    
    @Test
    public void testGetAllSets(){
        
        CardUtility.getAllSets(table, true);
        
    }
    
    @Test
    public void testValidateSet(){
        
        Card Card1 = new Card(3,3,2,1);
        Card Card2 = new Card(2,3,1,1);
        Card Card3 = new Card(1,3,3,1);
        CardUtility.validateSet(Card1, Card2, Card3);
        
    
    }
    
}
