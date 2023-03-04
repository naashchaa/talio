package server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/tasklist") // -> localhost:XXXX/named/name/variable
public class TestGetControllerArray {

    String[] tasks = new String[]{"never","gonna","give","you","up","never","gonna","let","you","down"};

    @GetMapping("/id/{id}")
    @ResponseBody
    public String named(@PathVariable("id") int id) {
        return tasks[id];
    }

}
