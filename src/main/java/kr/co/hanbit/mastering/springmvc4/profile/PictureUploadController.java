package kr.co.hanbit.mastering.springmvc4.profile;

import kr.co.hanbit.mastering.springmvc4.config.PicturesUploadProperties;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.util.Locale;


/**
 * Created by Jeon on 2017-03-19.
 */
@Controller
@SessionAttributes("picturePath")
public class PictureUploadController {
    /*
     * 업로드된 파일을 디스크에 저장하고 업로드 오류를 제어한다.
     * */

    private final Resource pictureDir;
    private final Resource anonymousPicture;
    private final MessageSource messageSource;
    private final UserProfileSession userProfileSession;

    @Autowired
    public PictureUploadController(PicturesUploadProperties picturesUploadProperties,
                                   MessageSource messageSource,
                                   UserProfileSession userProfileSession) {
        this.pictureDir = picturesUploadProperties.getUploadPath();
        this.anonymousPicture = picturesUploadProperties.getAnonymousPicture();
        this.messageSource = messageSource;
        this.userProfileSession = userProfileSession;
    }

   // public static final Resource PICTURES_DIR = new FileSystemResource("./pictures");

    @ExceptionHandler(IOException.class)
    public ModelAndView handleIoException (/*IOException exception*/ Locale locale) {

        ModelAndView modelAndView = new ModelAndView("profile/uploadPage");
        modelAndView.addObject("error", messageSource.getMessage("upload.io.exception",null, locale));// exception.getMessage());
        return modelAndView;
    }
    /*
    @ModelAttribute("picturePath")
    public Resource picturePath() {
        // ModelAttribute 어노테이션이 선언된 메소드를 통해
        // 모델어트리뷰트를 쉽게 생성할 수 있다.
        // 아래 리턴값을 주입한다고 보면 된다.
        return anonymousPicture;
    }
    */
    /*
    @RequestMapping("/upload")
    public String uploadPage() {

        return "profile/uploadPage";
    }
    */
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String onUpload(MultipartFile file, RedirectAttributes redirectAttrs, Model model) throws IOException {
        /*
        *  MultipartFile 인터페이스에 사용자가 업로드한 파일이 주입됨
        *  이 인터페이스는 파일의 이름, 크기, 콘텐츠 등을 얻을 수 있는 다양한 메소드 제공
        *  */
        if(file.isEmpty() || !isImage(file)) {

            redirectAttrs.addFlashAttribute("error", "Incorrect file. Please upload a picture");
            return "redirect:/profile";
        }

        Resource picturePath = copyFileToPictures(file);
        userProfileSession.setPicturePath(picturePath);
        //model.addAttribute("picturePath", picturePath);


        return "redirect:profile";
    }

    @RequestMapping(value="/uploadedPicture")
    public void getUploadedPicture(HttpServletResponse response
                                   /* ,@ModelAttribute("picturePath") Resource picturePath*/) throws IOException {
        Resource picturePath = userProfileSession.getPicturePath();
        if(picturePath == null) {
            picturePath = anonymousPicture;
        }

        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(picturePath.toString())/*(anonymousPicture.getFilename())*/);
        IOUtils.copy(picturePath.getInputStream(), response.getOutputStream());

        /*ClassPathResource classPathResource = new ClassPathResource("/images/anonymous.png");
        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(classPathResource.getFilename()));
        IOUtils.copy(classPathResource.getInputStream(), response.getOutputStream());*/

    }

    @RequestMapping("upload-error")
    public ModelAndView onUploadError(/*HttpServletRequest request*/ Locale locale) {

        ModelAndView modelAndView = new ModelAndView("profile/uploadPage");
        modelAndView.addObject("error", messageSource.getMessage("upload.file.too.big", null, locale));
        //modelAndView.addObject("error", request.getAttribute(WebUtils.ERROR_MESSAGE_ATTRIBUTE));
        return modelAndView;
    }

    private Resource copyFileToPictures(MultipartFile file) throws IOException {

        String fileExtension = getFileExtension(file.getOriginalFilename());
        //String filename = file.getOriginalFilename();
        File tempFile = File.createTempFile("pic", fileExtension, pictureDir.getFile());
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