package kr.co.hanbit.mastering.springmvc4.search;

import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by Jeon on 2017-03-29.
 * Spring boot는 JSON직렬화 라이브러리로 잭슨을 사용한다.
 * 잭슨은 접근자메소드(게터 등)으로 접근할 수 있는 모든 것들을 직렬화한다.
 * 그래서 어노테이션등으로 무시하도록 설정해야한다.
 * @JsonIgnoreProperties (클래스 무시)
 * @JsonIgnore (접근자 무시)
 *
 * 그런데 Tweet클래스는 내가 만든 것이 아니라서 저런거를 쓸 수 없다
 * 그래서 아래의 클래스를 만들었다.
 */
public class LightTweet {

    private String profileImageUrl;
    private String user;
    private String text;
    private LocalDateTime date;
    private String lang;
    private Integer retweetCount;

    public LightTweet(String text) {
        super();
        this.text = text;
    }

    public static LightTweet ofTweet (Tweet tweet) {
        LightTweet lightTweet = new LightTweet(tweet.getText());
        Date createdAt = tweet.getCreatedAt();

        if (createdAt != null) {
            lightTweet.date = LocalDateTime.ofInstant(createdAt.toInstant(), ZoneId.systemDefault());
        }

        TwitterProfile tweetUser = tweet.getUser();
        if(tweetUser != null) {
            lightTweet.user = tweetUser.getName();
            lightTweet.profileImageUrl = tweetUser.getProfileImageUrl();
        }

        lightTweet.lang = tweet.getLanguageCode();
        lightTweet.retweetCount = tweet.getRetweetCount();
        return lightTweet;
    }

    /* 이 밑으로는 게터세터 */

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Integer getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(Integer retweetCount) {
        this.retweetCount = retweetCount;
    }
}
