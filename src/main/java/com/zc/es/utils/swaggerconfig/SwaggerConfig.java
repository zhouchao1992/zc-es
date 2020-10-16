/*
package com.zc.es.utils.swaggerconfig;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    @Bean(value = "createRestApi")
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        //标题
                        .title("es整合测试")
                        //版本信息
                        .version("9.0")
                        //描述消息
                        .description("es整合测试")
                        .contact(new Contact("zc-es", "http://127.0.0.1:8887/", "http://127.0.0.1:8887/"))
                        .license("es")
                        .licenseUrl("http://127.0.0.1:8887/")
                        .build())
                //最终调用接口后会和paths拼接在一起
                .pathMapping("/")
                .select()
                //包路径
                .apis(RequestHandlerSelectors.basePackage("com.zc.es.controller"))
                //过滤的接口
                .paths(PathSelectors.any())
                .build();
    }


    */
/***
     * Http安全配置，对每个到达系统的http请求链接进行校验
     * @param http
     * @throws Exception
     *//*

*/
/*    @Override
    public void configure(HttpSecurity http) throws Exception {
        //所有请求必须认证通过
        http.authorizeRequests()
                //下边的路径放行
                .antMatchers("/swagger-resources/**","/api-docs","/doc.html","/api-docs-ext","/webjars/**","/v2/**","/swagger-ui.html") //配置地址放行
                .permitAll()
                .anyRequest()
                .authenticated();    //其他地址需要认证授权
    }*//*


}
*/
