package kr.co.hanbit.mastering.springmvc4.profile;

import kr.co.hanbit.mastering.springmvc4.date.KRLocalDateFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;



/**
 * Created by Jeon on 2017-03-01.
 */

@Controller
public class ProfileController {

    private UserProfileSession userProfileSession; // 커스터마이징 세션

    @Autowired
    public ProfileController(UserProfileSession userProfileSession) {
        /* 생성자 주입
         * 빈이 인스턴스화 되기 전에 생성자의 인자로 주입된다.
         */
        this.userProfileSession = userProfileSession;
    }

    @ModelAttribute
    public ProfileForm getProfileForm() {

        return userProfileSession.toForm();
    }

    @ModelAttribute("dateFormat")
    public String localeFormat(Locale locale) {

        return KRLocalDateFormatter.getPattern(locale);
    }

    @RequestMapping("/profile")
    public String displayProfile(ProfileForm profileForm) {

        return "profile/profilePage";
    }

    @RequestMapping(value = "/profile", params={"save"}, method = RequestMethod.POST)
    public String saveProfile(@Valid ProfileForm profileForm, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "profile/profilePage";
        }
        //log.debug("ProfileForm: {}", profileForm);
        //System.out.println("save ok"+profileForm);
        userProfileSession.saveForm(profileForm);
        return "redirect:/profile";
    }

    @RequestMapping(value = "/profile", params={"addTaste"})
    public String addRow(ProfileForm profileForm) {
        profileForm.getTastes().add(null);
        return "profile/profilePage";
    }

    @RequestMapping(value = "/profile", params={"removeTaste"})
    public String removeRow(ProfileForm profileForm, HttpServletRequest req){

        Integer rowId = Integer.valueOf(req.getParameter("removeTaste"));
        profileForm.getTastes().remove(rowId.intValue());
        return "profile/profilePage";
    }
}
