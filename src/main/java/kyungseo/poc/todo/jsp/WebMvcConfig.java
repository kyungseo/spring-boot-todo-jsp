/* ============================================================================
 * KYUNGSEO.PoC > Development Templates for building Web Apps
 *
 * Copyright 2023 Kyungseo Park <Kyungseo.Park@gmail.com>
 * ----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================================= */

package kyungseo.poc.todo.jsp;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

import kyungseo.poc.todo.common.AppConstants;
import kyungseo.poc.todo.common.web.interceptor.ViolationInterceptor;

/**
 * @author 박경서 (Kyungseo.Park@gmail.com)
 * @version 1.0
 */
//@ImportResource(value= {"classpath:spring/context-common.xml",
//        "classpath:spring/context-dataaccess.xml",
//        "classpath:spring/context-security.xml"
//})
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "kyungseo.poc.todo.jsp" })
public class WebMvcConfig implements WebMvcConfigurer {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final String[] CLASSPATH_RESOURCE_LOCATIONS = {
           "classpath:/static/",
           "classpath:/public/",
           "classpath:/resources/",
           "classpath:/META-INF/resources/",
           "classpath:/META-INF/resources/webjars/"
    };

    public WebMvcConfig() {
        super();
        LOGGER.info("WebMvcConfig Start...");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName(AppConstants.HOME_VIEW_NAME);
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE); // 우선순위를 가장 높게...
    }

    // https://stackoverflow.com/questions/64822250/illegalstateexception-after-upgrading-web-app-to-spring-boot-2-4
    @Bean
    WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> enableDefaultServlet() {
        //return (factory) -> factory.setRegisterDefaultServlet(true);
        return (factory) -> {
            factory.setRegisterDefaultServlet(true);
            factory.addErrorPages(
                    new ErrorPage(HttpStatus.BAD_REQUEST,           AppConstants.ERROR_400_URL),
                    new ErrorPage(HttpStatus.UNAUTHORIZED,          AppConstants.ERROR_401_URL),
                    new ErrorPage(HttpStatus.FORBIDDEN,             AppConstants.ERROR_403_URL),
                    new ErrorPage(HttpStatus.NOT_FOUND,             AppConstants.ERROR_404_URL),
                    new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, AppConstants.ERROR_500_URL));
        };
    }

    @Override
    public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
        // DispatcherSerlvet이 처리하지 못한 요청을 DefaultSerlvet(정적 자원 처리)이 처리 가능하게 함
        // 위 enableDefaultServlet() Bean 등록 필요
        configurer.enable();
    }

    @Bean
    public BeanNameViewResolver beanNameViewResolver() {
        BeanNameViewResolver beanNameViewResolver = new BeanNameViewResolver();
        // 우선순위 [0]
        beanNameViewResolver.setOrder(0);
        return beanNameViewResolver;
    }

    @Bean
    public UrlBasedViewResolver tilesViewResolver() {
       UrlBasedViewResolver urlBasedViewResolver = new UrlBasedViewResolver();
       urlBasedViewResolver.setViewClass(TilesView.class);
       // 우선순위 [1]
       urlBasedViewResolver.setOrder(1);
       return urlBasedViewResolver;
    }

    @Bean
    public TilesConfigurer tilesConfigurer() {
       TilesConfigurer tilesConfigurer = new TilesConfigurer();
       tilesConfigurer.setDefinitions("/WEB-INF/view/common/templates/tiles-definitions.xml");
       return tilesConfigurer;
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setViewClass(JstlView.class);
        bean.setPrefix("/WEB-INF/view/");
        bean.setSuffix(".jsp");
        // 우선순위 [2]
        bean.setOrder(2);
        return bean;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler("/resources/**").addResourceLocations("/", "/resources/")
        registry.addResourceHandler("/**").addResourceLocations(this.CLASSPATH_RESOURCE_LOCATIONS)
          .setCacheControl(CacheControl
                  .maxAge(60, TimeUnit.SECONDS)
                  .noTransform()
                  .mustRevalidate())
          .resourceChain(true)
          .addResolver(new PathResourceResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 전송된 lang 매개변수 의 값을 기반으로 로케일을 변경
        final LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName(AppConstants.LOCALE_CHANGE_PARAM_NAME);
        registry.addInterceptor(localeChangeInterceptor);

        // Double Submission 방지
        registry.addInterceptor(new ViolationInterceptor());

        // 특정 리소스에 대한 cache control
        WebContentInterceptor interceptor = new WebContentInterceptor();
        interceptor.addCacheMapping(CacheControl
                .maxAge(60, TimeUnit.SECONDS)
                .noTransform()
                .mustRevalidate(), "/todos/*");
        registry.addInterceptor(interceptor);
    }

    // CookieLocaleResolver
    @Bean
    public LocaleResolver localeResolver() {
        final CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        // Locale.KOREA -> "ko_KR", Locale.KOREAN -> "ko"
        cookieLocaleResolver.setDefaultLocale(Locale.KOREAN);
        cookieLocaleResolver.setCookieName(AppConstants.LOCALE_CHANGE_PARAM_NAME);
        return cookieLocaleResolver;
    }

    @Bean
    @ConditionalOnMissingBean(RequestContextListener.class)
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
        //registry.addMapping("/api/*").allowedOrigins("http://localhost:8080");
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    /*
    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(5242880);
        return multipartResolver;
    }
    */

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(AccessLevel.PRIVATE);
        return modelMapper;
    }

}
