package kr.co.hanbit.mastering.springmvc4.user;

import kr.co.hanbit.mastering.springmvc4.error.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Jeon on 2017-04-01.
 */
@Repository
public class UserRepository {

    private final Map<String, User> userMap = new ConcurrentHashMap<>();

    public User update(String email, User user) throws EntityNotFoundException {

        if(!exists(email)) {
            throw new EntityNotFoundException("User "+ email +" cannot be found");
        }
        user.setEmail(email);
        return userMap.put(email, user);
    }
    public User save(String email, User user) {
        user.setEmail(email);
        return userMap.put(email, user);
    }

    public User save(User user) {
        return save(user.getEmail(), user);
    }

    public User findOne(String email) throws EntityNotFoundException {

        if(!exists(email)) {
            throw new EntityNotFoundException("user "+email+" cannot be found");
        }
        return userMap.get(email);
    }

    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    public void delete(String email) throws EntityNotFoundException {
        if(!exists(email)) {
            throw new EntityNotFoundException("user "+email+" cannot be found");
        }

        userMap.remove(email);
    }

    public boolean exists(String email) {
        return userMap.containsKey(email);
    }

    public void reset(User... users) {
        userMap.clear();
        for(User user : users) {
            save(user);
        }
    }
}
