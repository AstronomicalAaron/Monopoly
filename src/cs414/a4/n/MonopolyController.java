package cs414.a4.n;

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
    public Monopoly start() {
    	game.start();
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
    	game.auctionProperty(tileIndex, 0);
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
    public Monopoly liftMortgage() {
    	game.liftMortgage();
    	return game;
    }
    
    @RequestMapping(value = "/upgradeprop", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly upgradeProperty() {
    	game.upgradeProperty();
    	return game;
    }
    
    @RequestMapping(value = "/degradeprop", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly degradeProperty() {
    	game.degradeProperty();
    	return game;
    }
    
    @RequestMapping(value = "/jailchoice", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly degradeProperty(
    		@RequestParam boolean choice) {
    	game.jailChoice(choice);
    	return game;
    }
    
    @RequestMapping(value = "/endgame", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly endGame() {
    	game = new Monopoly();
    	return game;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MonopolyController.class, args);
        
        game = new Monopoly();
    }
}