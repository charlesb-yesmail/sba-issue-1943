package name.charlesb.webapp1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/p")
public class DemoController {

	@GetMapping(value = "/")
	public ModelAndView index() {
		return new ModelAndView("index");
	}

	@GetMapping(value = "/hello")
	public ModelAndView sayHello(@RequestParam(required = false) String name) {
		ModelAndView result = new ModelAndView();
		if (StringUtils.hasText(name)) {
			result.addObject("name", name);
		} else {
			result.addObject("name", "World");
		}
		result.setViewName("hello");
		return result;
	}
}
