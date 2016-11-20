                                      _       
  /\/\   ___  _ __   ___  _ __   ___ | |_   _ 
 /    \ / _ \| '_ \ / _ \| '_ \ / _ \| | | | |
/ /\/\ \ (_) | | | | (_) | |_) | (_) | | |_| |
\/    \/\___/|_| |_|\___/| .__/ \___/|_|\__, |
                         |_|            |___/ 
			Version: 0.0.1
			Authors: Aaron Barczewski, Dylan Crescibene, Chris Geohring, Joseph Summers
			
	The JAR file contains a clone of the GitHub repository.
	
	First, extract the Monopoly-master folder out of the JAR file.
	
	In Eclipse, select "Import Maven Project" and choose the extracted folder.
	
	Once the project is opened, Refresh the Project in order to make sure that the Maven dependancies are set up.
	
	To run the Monopoly server, run MonopolyController.java as a Java Application
	
	The URL used to connect to the game in a browser, is "localhost:8080".
	To test the gameplay, you will need to open 2 or more windows simultaneously. You can do this on the same machine.
	If you are connecting from a different computer on the network, use the IP address instead of localhost.
	
	Strengths:
	1) Multi-player allows between 2-4 players to play together.
	2) UI is intuitive and reminiscent of the original board-game.
	3) The use of multithreading allows for smooth transitions between game-states.
	4) Buying/selling properties is easy, and the use of shadowing the gameboard makes it easy to manage properties.
	5) Easy to see whose turn it is. (easier than last assignment)
	6) Easier to see who the owner of a certain property is.
	7) A count-down timer for each player's turn ensures that gameplay continues if a player inadvertently drops out.
	
	Weaknesses:
	1) Not easy for someone who isn't familiar with networking to set up the game
	2) Hard to see the property cost when buying/mortgaging a property.
	
	What is missing:
	1) Would have like to see a message when you have to pay rent
	2) Seeing the net worths of each player, including the winner, in the end game window.
	3) Would have liked to have seen a list of servers that are available for players who log in.
	4) Would have liked to have seen an option for a player to drop out of the game.
	
	
	Our design pattern was a facade where the class Monopoly.java handles all the game logic while
	the MonopolyController.java binds the backend to the UI frontend.
	
	Refactoring Techniques:
	We composed some methods in Monopoly.java by making sure that
	any functions that were too long where broken down into shorter methods
	that are easier to work with.
	We added a new class that creates the Community Chest and Chance Cards
	and in doing so, we created methods that would simplify logical expressions
	in conditional statements.
	Removed methods that did nothing to the code for instance we had a removeCard and addCard method in the 
	Card.java class.
	Also removed excessive calls of certain functions such as startManagement
	Helper methods that were uses as helper methods in the Monopoly class were encapsulated in
	order to orgainize data better.