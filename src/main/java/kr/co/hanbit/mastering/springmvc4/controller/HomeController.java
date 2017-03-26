package kr.co.hanbit.mastering.springmvc4.controller;

import kr.co.hanbit.mastering.springmvc4.profile.UserProfileSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by Jeon on 2017-03-26.
 */
@Controller
public class HomeController {
    private UserProfileSession userProfileSession;

    @Autowired
    public HomeController(UserProfileSession userProfileSession) {
        this.userProfileSession = userProfileSession;
    }

    @RequestMapping("/")
    public String home() {
        List<String> tastes = userProfileSession.getTastes();

        if(tastes.isEmpty()) {
            return "redirect:/profile";
        }

        return "redirect:/search/mixed;keywords=" + String.join(",", tastes);
    }
}
