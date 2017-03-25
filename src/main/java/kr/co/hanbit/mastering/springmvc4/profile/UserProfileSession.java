package kr.co.hanbit.mastering.springmvc4.profile;


import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeon on 2017-03-25.
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
/* proxyMode 파라미터
 * TARGET_CLASS: CGI 프락시를 사용한다.
 * INTERFACES: JDK 프락시를 생성한다.
 * NO 프락시를 생성하지 않는다. */
public class UserProfileSession implements Serializable{
    /*
     *  SessionAttributes 어노테이션으로 세션에 객체를 밀어넣으면
     *  서로 다른 컨트롤러에서는 데이터를 공유하기 어렵다.
     *  단위테스트 작성도 어렵다. */

    private String twitterHandle;

    private String email;
    private LocalDate birthDate;
    private List<String> tastes = new ArrayList<>();

    public void saveForm(ProfileForm profileForm) {

        this.twitterHandle = profileForm.getTwitterHandle();
        this.email = profileForm.getEmail();
        this.birthDate = profileForm.getBirthDate();
        this.tastes = profileForm.getTastes();
    }

    public ProfileForm toForm() {

        ProfileForm profileForm = new ProfileForm();
        profileForm.setTwitterHandle(twitterHandle);
        profileForm.setEmail(email);
        profileForm.setBirthDate(birthDate);
        profileForm.setTastes(tastes);
        return profileForm;
    }
}
