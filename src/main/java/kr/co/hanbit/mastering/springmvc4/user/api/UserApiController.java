package kr.co.hanbit.mastering.springmvc4.user.api;

import kr.co.hanbit.mastering.springmvc4.error.EntityNotFoundException;
import kr.co.hanbit.mastering.springmvc4.user.User;
import kr.co.hanbit.mastering.springmvc4.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Jeon on 2017-04-01.
 */
@RestController
@RequestMapping("/api")
@Secured("ROLE_ADMIN")
public class UserApiController {

    private UserRepository userRepository;

    @Autowired
    public UserApiController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @RequestMapping(value="/users", method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        /* Spring MVC에서는 응답 엔티티에 HTTP상태를 넣을 수 있다.
         * (Response Entity를 활용)
         * */
        HttpStatus status = HttpStatus.OK;
        if(!userRepository.exists(user.getEmail())) {
            status = HttpStatus.CREATED;
        }
        User saved = userRepository.save(user);
        return new ResponseEntity<>(saved, status);
    }

    @RequestMapping(value = "/user/{email}", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@PathVariable String email, @RequestBody User user) throws EntityNotFoundException {
        /* 설정한 EntityNotFoundException을 컨트롤러에서 던진다
         * 404 Response를 처리하지 않아도 되어 편하다
         * UserRepositoty에서 발생한 익셉션을 처리하지 않으면, 500오류가 리턴된다.*/
        if(!userRepository.exists(user.getEmail())) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        User saved = userRepository.update(email, user);
        return new ResponseEntity<User>(saved, HttpStatus.CREATED);
    }


    @RequestMapping(value = "/user/{email}", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteUser(@PathVariable String email) throws EntityNotFoundException {
        if(!userRepository.exists(email)) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        userRepository.delete(email);
        return new ResponseEntity<User>(HttpStatus.OK);
    }
}
