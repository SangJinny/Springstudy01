package kr.co.hanbit.mastering.springmvc4.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import kr.co.hanbit.mastering.springmvc4.date.KRLocalDateFormatter;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.UrlPathHelper;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.activation.DataSource;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.logging.Filter;

/**
 * Created by Jeon on 2017-03-01.
 */
@Configuration
@EnableSwagger2 //swagger-springmvc 의존성 추가 후 사용
@EnableConfigurationProperties({PicturesUploadProperties.class})
public class WebConfiguration extends WebMvcConfigurerAdapter{

    @Override
    public void addFormatters(FormatterRegistry registry) {

        registry.addFormatterForFieldType(LocalDate.class, new KRLocalDateFormatter());
    }

    @Bean
    public LocaleResolver localeResolver() {

        return new SessionLocaleResolver();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {

        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }
    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }

    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return (Filter) characterEncodingFilter;
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
    /*multipartException을 커스터마이징하여 처리
    * 람다를 활용하면 아래와 같이 작성 가능하다.*/
        EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer
                = new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {

                container.addErrorPages(new ErrorPage(MultipartException.class, "/upload-error"));
            }
        } ;
        return embeddedServletContainerCustomizer;
    }
    /*
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        // 람다를 활용한 방식
        return container -> container.addErrorPages(new ErrorPage(MultipartException.class, "/upload-error"));
    }
    */

    /* 스프림의 기본 객체 매퍼를 사용자정의할 때 사용.
     * Spring MVC에서는 ObjectMapper를 제공한다 */
    @Bean
    @Primary
    public ObjectMapper objectsMapper(Jackson2ObjectMapperBuilder builder) {

        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return  objectMapper;
    }
/*
    @Bean
    @Primary
    public UsersConnectionRepository getUsersConnectionRepository(javax.sql.DataSource dataSource,
                                                                  ConnectionFactoryLocator connectionFactoryLocator) {
        return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
    }
  */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        /*
         * Spring MVC는 기본으로 URL에 세미콜론으로 이어진 모든 문자를 제거한다.
         * 이를 비활성화 하여 행렬변수를 URL에 활용하기 위해 이 메소드를 재정의한다. */
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
        configurer.setUseRegisteredSuffixPatternMatch(true); // 마침표(.) 처리?
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(localeChangeInterceptor());
    }
}
