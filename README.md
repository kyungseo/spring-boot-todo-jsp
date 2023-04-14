# Spring Boot와 JSP로 구현한 [Todo App] demo project

## 1 개요

**Java Server Page(이하 JSP)** 기술의 경우 자체적 제한이 존재하며, 특히 Spring Boot와 결합할 때 훨씬 더 제한된다는 약점이 있습니다. 따라서 View 단의 Template Engine을 적용함에 있어 JSP 보다는 Thymeleaf를 채택하는 것을 더 나은 대안으로 고려해야 합니다. Spring Boot 진영에서도 기본적으로는 View 영역에 JSP를 사용하는 것을 더 이상 권장하지 않고 있으며, 이에따라 최근 Web Application들은 이전의 JSP 기술 보다는 Thymeleaf, Freemarker, Groovy, Mustache 등의 Template Engine들이 더 많이 사용되고 있는 추세입니다.

하지만 실제 프로젝트에서 JSP 기술을 메인 템플릿으로 적용하는 경우가 흔치 않은 추세라지만 아직까지는 JSP 기술을 완전히 배제하기에는 조금은 이른 시점인 것도 사실입니다. 실세계에는 여러가지 상황과 이유들로 인해 최신 기술을 도입하기 어려운 사업들이 생각보다 많이 존재하고 이들 프로젝트에서는 여전히 JSP와 같은 오래된 기술을 요구하고 있습니다. 이를테면 기존의 JSP 기반 레거시(web application)을 업그레이드 하는 프로젝트가 있을 수 있겠죠. 이 경우에는 HTML 페이지 구현을 위한 템플릿 매커니즘으로써 JSP는 여전히 유효하고 최선의 옵션이 될 수 있습니다.

이러한 이유들로 **Spring Boot에 기반하여 JSP를 적용**해야 하는 상황을 상정하고 기초적이면서 간단한 **Todo App(Demo)**을 구현해보았습니다.

![image](/docs/todo-dashboard-pending.png)

### 1.1 프로젝트의 구성

```
src/main
│
├── java
│    └── kyungseo.poc.todo                    → Backend module
│         ├── common                          → Common module
│         └── jsp                             → Todo App module
│              ├── TodoApplication.java         : @SpringBootApplication
│              └── WebMvcConfig.java            : @Configuration, @EnableWebMvc
│
├── resources
│    ├── i18n                                 → messages
│    ├── static                               → css, js, vendor
│    └── application.properties               → Application Configuration
│
├── webapp
│    └── WEB-INF
│         ├── view                            → Frontend
│         │    ├── common                       : Tiles Layout & Error page
│         │    └── todo                         : Todo App Pages
│         └── web.xml                         → Deployment Descriptor
│
└── pom.xml                                   → Maven POM file

```

### 1.2 기술 Spec.

<table style="border: 2px;">
  <tr>
    <td align="center"> <b>Tech</b> </td>
    <td align="center"> <b>Dependency</b> </td>
  </tr>
  <tr>
    <td> <b>Backend</b> </td>
    <td>
      <ul>
        <li><b>Java</b>: java-11-openjdk-11.0.2</li>
        <li><b>Database</b>: H2 Database 2.1.214</li>
        <li><b>Spring Boot</b>: Spring Boot 2.7.8</li>
        <li><b>Spring Framework</b>: Spring Web 5.3.25, Spring Security 5.7.6, Spring Data JPA 2.7.7, etc</li>
        <li><b>Persistency Framework</b>: Hibernate 5.6.14</li>
        <li><b>UI Layout</b>: Tiles 3.0.8</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td> <b>Frontend</b> </td>
    <td>
      <ul>
        <li><b>JavaScript Libraries</b>: jQuery 3.5.1</li>
        <li><b>CSS Library</b>: Bootstrap 5.2.3</li>
      </ul>
    </td>
  </tr>
</table>

## 2 Project Setup

### 2.1 Clone 'spring-boot-todo-jsp'

`git` 명령을 사용하여 **spring-boot-todo-jsp** 프로젝트를 `clone` 합니다.

```bash
$ git clone https://github.com/kyungseo/spring-boot-todo-jsp.git
$ cd spring-boot-todo-jsp
```

### 2.2 Build

`spring-boot-todo-jsp` 프로젝트 디렉토리로 이동한 후 다음 명령을 사용하여 프로젝트 전체 module을 `build` 합니다.

```bash
mvn clean install

````

### 2.3 Run & Test

먼저 `spring-boot-todo-jsp` 디렉토리로 이동한 후 `mvn spring-boot:run` 명령을 실행하도록 합니다.

```bash
mvn spring-boot:run

```

실행 로그의 마지막 라인에 `Started TodoApplication`이 보인다면 서버가 정상 기동된 것입니다.

```
[restartedMain] INFO  k.p.s.web.VueWebApplication - Started VueWebApplication in 10.243 seconds (JVM running for 10.807)
```

Chrome 이나 Edge 등의 Browser 열고 다음 주소로 접속하여 애플리케이션을 테스트할 수 있습니다.

* [http://localhost:8080](http://localhost:8080)
* [http://localhost:8080/h2](http://localhost:8080/h2) - 비번 없이 `sa`로 접속

## 3 주요 설정 및 관련 코드

### 3.1 Spring Boot에서 JSP를 사용하기 위한 주요 설정

Spring Boot에서 JSP를 지원하기 위한 기본적 dependency는 다음과 같습니다.

* 기본 내장된 서블릿 컨테이너(Tomcat)과 함께 Web Application 실행을 위해 `spring-boot-starter-web` 의존성 추가
* JSP 페이지를 컴파일하고 렌더링할 수 있도록 `tomcat-embed-jasper` 의존성을 추가
* JSP 페이지에 필요한 JSTL 태그 지원을 제공하는 `jstl` 라이브러리 추가

다음 `/spring-boot-todo-jsp/pom.xml`의 내용을 참고하세요.

```xml
    <dependencies>
        <!-- Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- To compile JSP files -->
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-jasper</artifactId>
            <scope>provided</scope>
         </dependency>

        <!-- JSTL -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>jstl</artifactId>
        </dependency>

        <!-- Web Continer -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
            <scope>provided</scope>
        </dependency>

         <!-- javax.el -->
        <dependency>
            <groupId>javax.el</groupId>
            <artifactId>javax.el-api</artifactId>
            <version>3.0.0</version>
        </dependency>
    </dependencies>
```

또한 Tomcat과 같은 웹 컨테이너에 Todo App을 배포할 계획이므로 `packaging`은 `war`로 지정하였습니다.

```xml
<packaging>war</packaging>
```

추가로 war 파일이 실행될 수 있도록 `spring-boot-maven-plugin`을 포함해야 합니다.

```xml
<build>
    <finalName>${project.artifactId}</finalName>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <!-- Spring Boot 기동을 위한 Main class 지정 -->
                <mainClass>kyungseo.poc.todo.jsp.TodoApplication</mainClass>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-war-plugin</artifactId>
            <configuration>
                <warSourceDirectory>src/main/webapp</warSourceDirectory>
                <!-- DD 파일(web.xml) 없이도 war 진행 -->
                <failOnMissingWebXml>false</failOnMissingWebXml>
            </configuration>
        </plugin>
    </plugins>
</build>
```

일반적인 경우라면 Spring Boot로 독립 실행하기 위해서 `@SpringBootApplication` annotation을 갖는 application class를 생성하면 되지만, **Todo App**을 웹 컨테이너에 배포하기 위해서 다음 코드와 같이 `SpringBootServletInitializer`를 확장하였습니다.

```java
@SpringBootApplication(scanBasePackages = "kyungseo.poc.todo")
public class TodoApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TodoApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(TodoApplication.class);
    }

}
```

### 3.2 Web Application 설정

`web.xml`은 다음과 같이 `Spring config`에 필요한 기본 설정만 추가하고, 나머지 대부분의 설정들은 `XML`이 아닌 애너테이션 기반의 Java Config(하단의 `kyungseo.poc.todo.jsp.WebMvcConfig`)로 대체하였습니다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         id="WebApp_ID" version="4.0">

    <display-name>[KYUNGSEO.PoC] Todo App - Spring Boot and JSP</display-name>

    <!-- Spring root config -->

    <context-param>
        <param-name>contextClass</param-name>
        <param-value>
            org.springframework.web.context.support.AnnotationConfigWebApplicationContext
        </param-value>
    </context-param>
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>kyungseo.poc.todo.jsp</param-value>
    </context-param>

    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <!-- Spring child config -->

    <servlet>
        <servlet-name>mvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>mvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>

</web-app>
```

`WebMvcConfig`의 주요 설정은 다음과 같습니다. 코드 내 주석을 참고하세요.

```java
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "kyungseo.poc.todo.jsp" })
public class WebMvcConfig implements WebMvcConfigurer {

    // Default Servlet을 사용하고, Error Page들을 등록

    @Bean
    WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> enableDefaultServlet() {
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
        configurer.enable();
    }

    // 우선순위 [0]: BeanNameViewResolver

    @Bean
    public BeanNameViewResolver beanNameViewResolver() {
        BeanNameViewResolver beanNameViewResolver = new BeanNameViewResolver();
        beanNameViewResolver.setOrder(0);
        return beanNameViewResolver;
    }

    // 우선순위 [1]: tilesViewResolver (UrlBasedViewResolver)

    @Bean
    public UrlBasedViewResolver tilesViewResolver() {
       UrlBasedViewResolver urlBasedViewResolver = new UrlBasedViewResolver();
       urlBasedViewResolver.setViewClass(TilesView.class);
       urlBasedViewResolver.setOrder(1);
       return urlBasedViewResolver;
    }

    @Bean
    public TilesConfigurer tilesConfigurer() {
       TilesConfigurer tilesConfigurer = new TilesConfigurer();
       tilesConfigurer.setDefinitions("/WEB-INF/view/common/templates/tiles-definitions.xml");
       return tilesConfigurer;
    }

    // 우선순위 [2]: InternalResourceViewResolver

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setViewClass(JstlView.class);
        bean.setPrefix("/WEB-INF/view/");
        bean.setSuffix(".jsp");
        bean.setOrder(2);
        return bean;
    }

}
```

상기 설정에 따라 **Todo App** 내에서의 `Spring MVC`는 `${project.basedir}/main/webapp/WEB-INF/view` 하위 디렉토리에서 JSP 템플릿을 인식하게 됩니다.

### 3.3 Messaging 핸들링

`i18n`을 위한 **messages**는 다음 `application.properties`의 설정을 통해 기본 `messageSource`를 사용합니다.

```properties
spring.messages.basename=i18n/messages-common,i18n/messages-common_ko,i18n/messages-todo,i18n/messages-todo_ko
spring.messages.encoding=UTF-8
spring.messages.fallbackToSystemLocale=false
spring.messages.alwaysUseMessageFormat=true
```

`basename`에 정의된 항목 중 하나인 `messages-todo_ko.properties`에는 다음 message들이 정의되어 있습니다.

```properties
# pages
todo.page.list.title=할일 관리
todo.page.form.title=할일
todo.page.dashboard.title=대시보드

# labels
todo.username.label=사용자명
todo.description.label=할일
todo.targetdate.label=목표일
todo.isdone.label=완료
todo.action.label=액션
todo.action.button.change=변경
todo.action.button.delete=삭제
todo.action.button.add=할일 추가
todo.action.button.save=저장

# Validation
todo.username.valid.notmepty='사용자명'은 필수 입력 값입니다.
todo.description.valid.notnull='할일 설명'은 필수 입력 값입니다.
todo.description.valid.size='할일 설명'은 최소 {min} 글자 이상을 입력해야 합니다.
```

위 `messages`를 사용하는 실제 `list.jsp`의 일부 코드를 참고하시기 바랍니다.

```jsp
  <thead>
    <tr>
      <th width="45%" class="text-center"><spring:message code="todo.description.label" /></th>
      <th width="15%" class="text-center"><spring:message code="todo.targetdate.label" /></th>
      <th width="15%" class="text-center"><spring:message code="todo.isdone.label" /></th>
      <th width="25%" class="text-center"><spring:message code="todo.action.label" /></th>
    </tr>
  </thead>
```

### 3.4 Error 핸들링

상기 `WebMvcConfig`의 에러 페이지 설정과 다음 `@ControllerAdvice`(`GlobalControllerExceptionHandler`)에 따라 **Todo App**에서 발생하는 대부분의 에러는 `/src/main/webapp/WEB-INF/view/common/error.jsp`로 `redirect` 됩니다.

```java
package kyungseo.poc.todo.common.exception;

@Order(1)
@ControllerAdvice(annotations = Controller.class)
public class GlobalControllerExceptionHandler {
    // ...
}
```

에러 발생 시의 예시 화면은 다음과 같습니다.

![image](/docs/todo-error.png)

### 3.5 Tiles 레이아웃

**Todo App**은 레이아웃 구성을 위해 `Tiles`를 적용하고 있습니다. 다음은 `Tiles` 설정인 `/src/main/webapp/WEB-INF/view/common/templates/tiles-definitions.xml` 파일의 내용입니다.

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE tiles-definitions PUBLIC
        "-//Apache Software Foundation//DTD Tiles Configuration 3.0//EN"
        "http://tiles.apache.org/dtds/tiles-config_3_0.dtd">
<tiles-definitions>

    <definition name="defaultTemplate" template="/WEB-INF/view/common/templates/tiles/defaultTemplate.jsp">
        <put-attribute name="sidebar" value="/WEB-INF/view/common/templates/tiles/sidebar.jsp" />
        <put-attribute name="navbar" value="/WEB-INF/view/common/templates/tiles/navbar.jsp" />
    </definition>

</tiles-definitions>
```

## 4 Todo App 화면

**Todo App**에서 실제로 동작하는 화면의 몇 가지 예시를 첨부합니다.

참고로 **Todo App**은 실제 Production이 아닌 구현 예시를 위한 **demo project**이므로 화면의 구성과 프로세스가 효율적이지 않은 점을 양지하시기 바랍니다. 구현 예시를 위한 약식의 화면 배치일 뿐입니다.

### 4.1 메인 페이지

![image](/docs/todo-home.png)


### 4.2 Todo 관리 페이지

![image](/docs/todo-list.png)


### 4.3 Todo 등록 페이지

![image](/docs/todo-new.png)


### 4.4 모든 할일 페이지

![image](/docs/todo-dashboard-all.png)

## 5 맺음

**Todo App**(`spring-boot-todo-jsp`)은 **Apache 2.0** 라이선스 하에 배포됩니다.

버그 및 이슈에 대한 리포트나 개선에 대한 의견을 환영합니다!

