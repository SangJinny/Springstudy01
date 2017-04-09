package kr.co.hanbit.mastering.springmvc4.authentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Jeon on 2017-04-08.
 */
@Controller
public class LoginController {

    @RequestMapping("/login")
    public String authenticate() {
        return  "login";
    }
}
