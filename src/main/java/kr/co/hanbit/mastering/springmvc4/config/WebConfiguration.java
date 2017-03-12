package kr.co.hanbit.mastering.springmvc4.config;

import kr.co.hanbit.mastering.springmvc4.date.KRLocalDateFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.time.LocalDate;

/**
 * Created by Jeon on 2017-03-01.
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter{

    @Override
    public void addFormatters(FormatterRegistry registry) {

        registry.addFormatterForFieldType(LocalDate.class, new KRLocalDateFormatter());
    }
}
