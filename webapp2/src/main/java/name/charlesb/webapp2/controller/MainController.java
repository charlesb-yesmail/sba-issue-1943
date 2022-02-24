package name.charlesb.webapp2.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

@RestController
//@RequestMapping("/index")
public class MainController {

	@GetMapping(value = "/index", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<BufferedImage> index() throws IOException {
		String imagePath = "/static/barcode.png";
		File imgFile = new ClassPathResource(imagePath).getFile();
		BufferedImage img = ImageIO.read(imgFile);
		return ResponseEntity.ok(img);
	}
}
