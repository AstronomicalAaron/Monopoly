package cs414.a4.n;

/*************************************************************************************
 *                                      MONOPOLY									 *
 *************************************************************************************
 *                               CREATED ON: (C)10/28/2016							 *
 *                                   UPDATED ON: 10/28/2016						     *
 *                                   VERSION: 0.0.1									 *
 *                                     WRITTEN BY:									 *
 * 	    								 Joey Bzdek	                                 *
 * 								    Dylan Crescibene 								 *
 * 									 Chris Geohring 								 *
 * 									Aaron Barczewski 								 *
 * 																					 *
 *************************************************************************************/

/*************************************************************************************
 * 										CONTROLLER									 *
 *************************************************************************************/

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class MonopolyController {
    
	private static Monopoly game;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource get() {
		return getHttp("page.html");
    }
	
    @RequestMapping(value = "/{file_name:.+}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getHttp(@PathVariable("file_name") String fileName) {
		return new FileSystemResource("http/" + fileName);
    }
    
    // Operations
    @RequestMapping(value = "/state", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly getState() {
    	return game;
    }
    
    @RequestMapping(value = "/join", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly start(
    		@RequestParam String name, 
    		@RequestParam TokenType token) {
    	game.join(name, token);
    	return game;
    }
    
    @RequestMapping(value = "/start", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly start(@RequestParam int timelimit) {
    	game.start(timelimit);
    	return game;
    }
    
    @RequestMapping(value = "/endturn", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly rollDice() {
    	game.endTurn();
    	return game;
    }
    
    @RequestMapping(value = "/buyproperty", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly buyProperty() {
    	game.buyProperty();
    	return game;
    }
    
    @RequestMapping(value = "/selltobank", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly sellToBank(
    		@RequestParam int propertyIndex) {
    	game.sellToBank(propertyIndex);
    	return game;
    }
    
    @RequestMapping(value = "/selltoplayers", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly sellToPlayers(
    		@RequestParam int propertyIndex,
    		@RequestParam double startingBid) {
    	game.sellToPlayers(propertyIndex, startingBid);
    	return game;
    }
    
    @RequestMapping(value = "/passproperty", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly passProperty(
    		@RequestParam int tileIndex) {
    	Runnable r = new Runnable() {
    		public void run() {
    			game.auctionProperty(game.getBank(), tileIndex, 0);
    			game.startManagement();
    		}
    	};
    	new Thread(r).run();
    	return game;
    }
    
    @RequestMapping(value = "/setbid", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly setBid(
    		@RequestParam String username,
    		@RequestParam double bid) {
    	game.setBid(username, bid);
    	return game;
    }
    
    @RequestMapping(value = "/buymortgage", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly buyMortgage() {
    	game.buyMortgage();
    	return game;
    }
    
    @RequestMapping(value = "/liftmortgage", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly liftMortgage(
    		@RequestParam int propertyIndex) {
    	game.liftMortgage(propertyIndex);
    	return game;
    }
    
    @RequestMapping(value = "/upgradeprop", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly upgradeProperty(
    		@RequestParam int index) {
    	game.upgradeProperty(index);
    	return game;
    }
    
    @RequestMapping(value = "/degradeprop", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly degradeProperty(
    		@RequestParam int index) {
    	game.degradeProperty(index);
    	return game;
    }
    
    @RequestMapping(value = "/jailchoice", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly jailChoice(
    		@RequestParam boolean choice) {
    	game.jailChoice(choice);
    	return game;
    }
    
    @RequestMapping(value = "/usefreecard", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly useFreeCard() {
    	game.useFreeCard();
    	return game;
    }
    
    @RequestMapping(value = "/hackedroll", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly hackedRoll(
    		@RequestParam int val1,
    		@RequestParam int val2) {
    	game.hackedRoll(val1, val2);
    	return game;
    }
    
    @RequestMapping(value = "/setcheatmodeon", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly hackedRoll(
    		@RequestParam boolean val) {
    	game.setCheatModeOn(val);
    	return game;
    }
    
    @RequestMapping(value = "/resetgame", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly resetGame() {
    	game = new Monopoly();
    	return game;
    }
    
    @RequestMapping(value = "/endgame", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly endGame() {
    	game.endGame();
    	return game;
    }
    
    @RequestMapping(value = "/ackcard", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly ackCard() {
    	game.acknowledgeCard();
    	return game;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MonopolyController.class, args);
        
        game = new Monopoly();
    }
}