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
    
    @RequestMapping(value = "/sellproperty", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Monopoly sellProperty() {
    	game.sellProperty();
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

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MonopolyController.class, args);
        
        game = new Monopoly();
    }
}