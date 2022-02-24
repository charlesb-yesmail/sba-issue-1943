package name.charlesb.standalone.resource;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1/services")
public class ServicesResource {

	@GetMapping(path = "/hello/{name}", produces = { MediaType.TEXT_PLAIN_VALUE })
	public String sayHello(@PathVariable String name) {
		if (StringUtils.hasText(name)) {
			return String.format("Hello, %s", name);
		}
		return "Hello world";
	}
}
