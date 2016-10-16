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
    @RequestMapping(value = "/tiles", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public TileList getTiles() {
		return game.getBoard().getTiles();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MonopolyController.class, args);
        
        game = new Monopoly();
    }
}