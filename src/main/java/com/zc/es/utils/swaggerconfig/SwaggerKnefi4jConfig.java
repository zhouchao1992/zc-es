package com.zc.es.utils.swaggerconfig;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableSwagger2
@EnableKnife4j
public class SwaggerKnefi4jConfig {
    @Bean
    public Docket appApi() {
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        ticketPar.name("appId").description("家居给享系列分配一个应用ID")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build(); //header中的ticket参数非必填，传空也可以
        pars.add(ticketPar.build());    //根据每个方法名也知道当前方法在设置什么参数

        return new Docket(DocumentationType.SWAGGER_2).groupName("SecurityLearning").genericModelSubstitutes(ResponseEntity.class)
                .apiInfo(appApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zc"))//扫描的包
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //.paths(PathSelectors.any())
//                .paths(or(regex("/user/.*")))//过滤
                .build()
                .securitySchemes(securitySchemes())
                //.securityContexts(securityContexts())
                .globalOperationParameters(pars);
    }

    @Bean
    public Docket prjAppApi() {
        // 每个接口都共有的参数
        ParameterBuilder param1 = new ParameterBuilder();
        param1.name("sign").description("签名").modelRef(new ModelRef("string")).parameterType("query").required(false).build();
        ParameterBuilder param2 = new ParameterBuilder();
        param2.name("sessionId").description("sessionId").modelRef(new ModelRef("string")).parameterType("query").required(false).build();
        ParameterBuilder param3 = new ParameterBuilder();
        param3.name("userId").description("userId").modelRef(new ModelRef("integer")).parameterType("query").required(false).build();
        ParameterBuilder param4 = new ParameterBuilder();//参数非必填，传空也可以
        param4.name("appVersion").description("app版本").modelRef(new ModelRef("string")).parameterType("header").required(false).build();

        List<Parameter> aParameters = new ArrayList<>();
        aParameters.add(param1.build());
        aParameters.add(param2.build());
        aParameters.add(param3.build());
        aParameters.add(param4.build());

        return new Docket(DocumentationType.SWAGGER_2).groupName("prjApp（工程版app）").genericModelSubstitutes(ResponseEntity.class)
                .globalOperationParameters(aParameters)
                .apiInfo(prjAppApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zc"))//扫描的包
//                .apis(RequestHandlerSelectors.any()) // 对所有api进行监控
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
//                .paths(or(regex("/user/.*")))//过滤
                .build();
    }

    private ApiInfo appApiInfo() {
        return new ApiInfoBuilder()
                .title("Security学习接口文档-APP")
                .description("该文档主要提供APP 端的接口 \r\n\n"
                        + "请求服务:http//10.1.2.43:8888  （测试服务器不支持https）\r\n\n"
                        + "返回：  \r\n\n"
                        + "ReturnBody<T> {\"code\":\"标识码\",\"msg\":\"描述\",data{ json字符串（对象）  } \r\n\n"
                        + "")
                .contact(new springfox.documentation.service.Contact("xxx业务后台开发组", null, null))
                .version("1.0.0")
                .build();
    }


    private ApiInfo prjAppApiInfo() {
        return new ApiInfoBuilder()
                .title("xxx接口文档-工程版APP")
                .description("该文档主要提供工程版APP端的接口 \r\n\n"
                        + "请求服务:http//10.34.4.47:18777/  \r\n\n"
                        + "返回：  \r\n\n"
                        + "ReturnBody<T> {\"code\":\"标识码\",\"msg\":\"描述\",data{ json字符串（对象）  } \r\n\n"
                        + "")
                .contact(new springfox.documentation.service.Contact("xxxx业务后台开发组", null, null))
                .version("1.0.0")
                .build();
    }

    private List<ApiKey> securitySchemes() {
        //设置请求头信息
        List<ApiKey> result = new ArrayList<ApiKey>();
        ApiKey apiKey = new ApiKey("Authorization", "Authorization", "header");
        result.add(apiKey);
        return result;
    }
}
