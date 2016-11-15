package cs414.a4.n;

/*************************************************************************************
 *                                      MONOPOLY									 *
 *************************************************************************************
 *                               CREATED ON: (C)10/28/2016							 *
 *                                   UPDATED ON: 10/28/2016						     *
 *                                   VERSION: 0.0.1									 *
 *                                     WRITTEN BY:									 *
 * 	    								 Joey Summers	                                 *
 * 								    Dylan Crescibene 								 *
 * 									 Chris Geohring 								 *
 * 									Aaron Barczewski 								 *
 * 																					 *
 *************************************************************************************/

/*************************************************************************************
 * 										CHANCE CARD									 *
 *************************************************************************************/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Chance {

	private ArrayList<String> cards;
	private Random rand;
	private int cardIndex;

	public Chance(){
		
		cards = new ArrayList<String>();
		rand = new Random();
		cardIndex = rand.nextInt(15);

		readDescription();

	}

	private void readDescription(){

		try(BufferedReader br = new BufferedReader(new FileReader("chance_card_desc.txt")))
		{

			String currentLine;

			while((currentLine = br.readLine()) != null){

				cards.add(currentLine);

			}			


		}catch(IOException e){

			e.printStackTrace();

		}
	}
	
	public String getCardDesc(){
		
		return cards.get(cardIndex).substring(1, cards.get(cardIndex).lastIndexOf("\""));
		
	}
	
	public int getPayment(){
		
		String temp = cards.get(cardIndex);
		String [] sarry = temp.split(":");
		return Integer.parseInt(sarry[1]);
		
	}
	
	public int getDefPent(){
		
		String temp = cards.get(cardIndex);
		String [] sarry = temp.split(":");
		return Integer.parseInt(sarry[2]);
		
	}
	
	public int getSecdPent(){
		
		if(cardIndex != 5){
			
			return -1;
			
		}
		
		String temp = cards.get(cardIndex);
		String [] sarry = temp.split(":");
		if(sarry.length == 5){
			
			return Integer.parseInt(sarry[3]);
			
		}else{
			
			return -1;
			
		}
		
	}
	
	public int moveToIndex(){
		
		String temp = cards.get(cardIndex);
		String [] sarry = temp.split(":");
		if(cardIndex == 5){
			
			return Integer.parseInt(sarry[4]);
			
		}else{
			
			return Integer.parseInt(sarry[3]);
			
		}
		
	}

}
