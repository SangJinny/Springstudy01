package kr.co.hanbit.mastering.springmvc4.search.api;

import kr.co.hanbit.mastering.springmvc4.search.LightTweet;
import kr.co.hanbit.mastering.springmvc4.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Jeon on 2017-03-29.
 * RESTful 구현 컨트롤러
 */
@RestController
@RequestMapping("/api/search")
public class SearchApiController {

    private SearchService searchService;

    @Autowired
    public SearchApiController(SearchService searchService) {

        this.searchService = searchService;
    }

    @RequestMapping(value ="/{searchType}", method= RequestMethod.GET)
    public List<LightTweet> search(@PathVariable String searchType, @MatrixVariable List<String> keywords) {

        return searchService.search(searchType, keywords);
    }
}
