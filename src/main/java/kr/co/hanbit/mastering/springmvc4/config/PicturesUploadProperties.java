package kr.co.hanbit.mastering.springmvc4.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;


/**
 * Created by Jeon on 2017-03-19.
 */
@ConfigurationProperties(prefix = "upload.pictures")
public class PicturesUploadProperties {
    /*
    * 이 클래스는 스프링 부트 ConfigurationProperties를 사용해 만들었음.
    * 클래스패스 상에서 프로퍼티를 찾아,
    * 스프링 부트에 자동으로 속성을 type-safe하게 매핑하도록 지시함.
    * 게터의 경우 반환형을 자유롭게(Resource) 정의하였음.
    * */
    private Resource uploadPath;
    private Resource anonymousPicture;

    public Resource getAnonymousPicture() {
        return anonymousPicture;
    }

    public void setAnonymousPicture(String anonymousPicture) {
        /*
        * DefaultResourceLoader 사용을 함으로써, properties파일에 값을 추가할 때
        * 접두어 file: 또는 classpath: 로 자원이 어디에 있는지를 정의한다.
        * */
        this.anonymousPicture = new DefaultResourceLoader().getResource(anonymousPicture);
    }

    public Resource getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = new DefaultResourceLoader().getResource(uploadPath);
    }
}
