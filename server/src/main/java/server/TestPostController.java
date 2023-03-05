package server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/response")
public class TestPostController {

    @PostMapping("/postbody")
    @ResponseBody
    public String postBody(@RequestBody String color) {
        return color + " is a great color";
    }

}
