package kr.co.hanbit.mastering.springmvc4.profile;

import kr.co.hanbit.mastering.springmvc4.config.PicturesUploadProperties;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;

/**
 * Created by Jeon on 2017-03-19.
 */
// onUpload와 onUploadedPicture가 다른 요청임에도 각각의 메소드에서 모델이 초기화되므로 세션으로 관리.
@Controller
@SessionAttributes("picturePath")
public class PictureUploadController {


    private final Resource pictureDir;
    private final Resource anonymousPicture;
    @Autowired

    public PictureUploadController(PicturesUploadProperties picturesUploadProperties) {
        this.pictureDir = picturesUploadProperties.getUploadPath();
        this.anonymousPicture = picturesUploadProperties.getAnonymousPicture();
    }

   // public static final Resource PICTURES_DIR = new FileSystemResource("./pictures");

    @ModelAttribute("picturePath")
    public Resource picturePath() {
        /* ModelAttribute 어노테이션이 선언된 메소드를 통해
        *  모델어트리뷰트를 쉽게 생성할 수 있다.
        *  아래 리턴값을 주입한다고 보면 된다.*/
        return anonymousPicture;
    }

    @RequestMapping("/upload")
    public String uploadPage() {

        return "profile/uploadPage";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String onUpload(MultipartFile file, RedirectAttributes redirectAttrs, Model model) throws IOException {
        /*
        *  MultipartFile 인터페이스에 사용자가 업로드한 파일이 주입됨
        *  이 인터페이스는 파일의 이름, 크기, 콘텐츠 등을 얻을 수 있는 다양한 메소드 제공
        *  */
        if(file.isEmpty() || !isImage(file)) {

            redirectAttrs.addFlashAttribute("error", "Incorrect file. Please upload a picture");
            return "redirect:/upload";
        }

        Resource picturePath = copyFileToPictures(file);
        model.addAttribute("picturePath", picturePath);


        return "profile/uploadPage";
    }

    @RequestMapping(value="/uploadedPicture")
    public void getUploadedPicture(HttpServletResponse response, @ModelAttribute("picturePath") Resource picturePath) throws IOException {

        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(picturePath.toString())/*(anonymousPicture.getFilename())*/);
        IOUtils.copy(anonymousPicture.getInputStream(), response.getOutputStream());

        /*ClassPathResource classPathResource = new ClassPathResource("/images/anonymous.png");
        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(classPathResource.getFilename()));
        IOUtils.copy(classPathResource.getInputStream(), response.getOutputStream());*/

    }

    private Resource copyFileToPictures(MultipartFile file) throws IOException {

        String fileExtension = getFileExtension(file.getOriginalFilename());
        //String filename = file.getOriginalFilename();
        File tempFile = File.createTempFile("pic", getFileExtension(fileExtension), pictureDir.getFile());
        /* try... with 블록*/
        try(InputStream in = file.getInputStream();
            OutputStream out = new FileOutputStream(tempFile)) {
            /* 인풋스트림을 파일아웃풋스트림으로 copy메소드를 통해 복사한다.*/
            IOUtils.copy(in, out);
        }
        return new FileSystemResource(tempFile);
    }

    private boolean isImage (MultipartFile file) {

        return file.getContentType().startsWith("image");
    }

    private static String getFileExtension(String name) {

        return name.substring(name.lastIndexOf("."));
    }
}