package cn.fanzy.plus.swagger;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendSetting;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jAutoConfiguration;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.github.xiaoymin.knife4j.spring.filter.ProductionSecurityFilter;
import com.github.xiaoymin.knife4j.spring.filter.SecurityBasicAuthFilter;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


/**
 * swagger????????????????????????
 *
 * @author fanzaiyang
 * @since 2021/09/07
 */
@Slf4j
@Configuration
@EnableSwagger2WebMvc
@EnableConfigurationProperties({SwaggerProperties.class})
@ComponentScan(
        basePackages = {
                "com.github.xiaoymin.knife4j.spring.plugin",
        }
)
@AutoConfigureAfter(value = Knife4jAutoConfiguration.class)
@ConditionalOnProperty(prefix = "plus.swagger", name = {"enable"}, havingValue = "true", matchIfMissing = true)
public class Swagger2AutoConfiguration implements WebMvcConfigurer {
    @Autowired
    private Environment environment;
    @Autowired
    private SwaggerProperties swaggerProperties;

    /**
     * ????????????????????????,??????????????????swagger-ui???????????????404
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/META-INF/resources/");
        // ??????knife4j ????????????
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/META-INF/resources/");
        // ??????swagger-ui????????????
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/META-INF/resources/");
        // ??????????????????
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/doc").setViewName("forward:/doc.html");
    }

    // @formatter:off

    /**
     * swagger-ui??????
     *
     * @return Docket??????
     */
    @Bean
    @ConditionalOnMissingBean
    public Docket createRestApi() {
        //??????????????????
        List<Parameter> pars = this.buildParameter();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(swaggerProperties.getGroupName())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars)
                .extensions(markdownResolver().buildSettingExtensions());
    }

    /**
     * ???????????????????????????
     *
     * @return api??????
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .contact(new Contact(
                        swaggerProperties.getContactUser(),
                        swaggerProperties.getContactUrl(),
                        swaggerProperties.getContactEmail())
                )
                .version(swaggerProperties.getVersion())
                .build();
    }


    /**
     * ????????????????????????
     *
     * @return ??????????????????
     */
    private List<Parameter> buildParameter() {
        if (BooleanUtil.isTrue(this.swaggerProperties.getShowDeatil())) {
            log.info("???Plus????????? swagger-ui ??????????????? {}", this.swaggerProperties.getAuths());
        }

        List<Parameter> pars = new ArrayList<>();
        if (CollUtil.isNotEmpty(this.swaggerProperties.getAuths())) {
            this.swaggerProperties.getAuths().forEach(t -> pars.add(new ParameterBuilder()
                    .name(t.getName())
                    .description(t.getDescription())
                    .modelRef(new ModelRef(t.getModelRef()))
                    .parameterType(t.getParameterType())
                    .required(t.getRequired())
                    .build()));
        }
        return pars;
    }

    /**
     * ????????????
     */
    @PostConstruct
    public void checkConfig() {

        log.debug("???Plus?????????: ?????? <Swagger-ui????????????> ???????????????");
    }


    /**
     * ??????Cors
     *
     * @return {@link CorsFilter}
     */
    @Bean("knife4jCorsFilter")
    @ConditionalOnMissingBean(CorsFilter.class)
    @ConditionalOnProperty(name = "plus.swagger.knife4j.cors", havingValue = "true")
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setMaxAge(10000L);
        //????????????API
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }


    @Bean(initMethod = "start")
    @ConditionalOnMissingBean(OpenApiExtensionResolver.class)
    @ConditionalOnProperty(name = "plus.swagger.knife4j.enable", havingValue = "true", matchIfMissing = true)
    public OpenApiExtensionResolver markdownResolver() {

        OpenApiExtendSetting setting = swaggerProperties.getKnife4j().getSetting();
        if (setting == null) {
            setting = new OpenApiExtendSetting();
        }
        if (!setting.isEnableSwaggerModels() || StrUtil.isNotBlank(setting.getSwaggerModelName())) {
            setting.setEnableSwaggerModels(true);
            setting.setSwaggerModelName(StrUtil.blankToDefault(setting.getSwaggerModelName(), "???????????????"));
        }
        if (!setting.isEnableFooterCustom() || StrUtil.isBlank(setting.getFooterCustomContent())) {
            setting.setEnableFooter(false);
            setting.setEnableFooterCustom(true);
            setting.setFooterCustomContent("Apache License 2.0 | Copyright  2021-[????????????](https://gitee.com/it-xiaofan)");
        }
        return new OpenApiExtensionResolver(setting, swaggerProperties.getKnife4j().getDocuments());
    }

    @Bean
    @ConditionalOnMissingBean(SecurityBasicAuthFilter.class)
    @ConditionalOnProperty(name = "plus.swagger.knife4j.basic.enable", havingValue = "true")
    public SecurityBasicAuthFilter securityBasicAuthFilter() {
        boolean enableSwaggerBasicAuth = false;
        String dftUserName = "admin", dftPass = "123321";
        SecurityBasicAuthFilter securityBasicAuthFilter = null;
        if (swaggerProperties.getKnife4j() == null) {
            if (environment != null) {
                String enableAuth = environment.getProperty("plus.swagger.knife4j.basic.enable");
                enableSwaggerBasicAuth = Boolean.parseBoolean(enableAuth);
                if (enableSwaggerBasicAuth) {
                    //????????????basic??????,??????????????????????????????????????????
                    dftUserName = StrUtil.blankToDefault(
                            environment.getProperty("plus.swagger.knife4j.basic.username"),
                            dftUserName
                    );
                    dftPass = StrUtil.blankToDefault(
                            environment.getProperty("plus.swagger.knife4j.basic.password"),
                            dftPass
                    );
                }
                securityBasicAuthFilter = new SecurityBasicAuthFilter(enableSwaggerBasicAuth, dftUserName, dftPass);
            }
        } else {
            //????????????
            if (swaggerProperties.getKnife4j().getBasic() == null) {
                securityBasicAuthFilter = new SecurityBasicAuthFilter(enableSwaggerBasicAuth, dftUserName, dftPass);
            } else {
                securityBasicAuthFilter = new SecurityBasicAuthFilter(swaggerProperties.getKnife4j().getBasic().isEnable(),
                        swaggerProperties.getKnife4j().getBasic().getUsername(), swaggerProperties.getKnife4j().getBasic().getPassword());
            }
        }
        return securityBasicAuthFilter;
    }


    @Bean
    @ConditionalOnMissingBean(ProductionSecurityFilter.class)
    @ConditionalOnProperty(name = "plus.swagger.knife4j.production", havingValue = "true")
    public ProductionSecurityFilter productionSecurityFilter() {
        boolean prod = false;
        ProductionSecurityFilter p;
        if (swaggerProperties.getKnife4j() == null) {
            if (environment != null) {
                String prodStr = environment.getProperty("plus.swagger.knife4j.production");
                if (log.isDebugEnabled()) {
                    log.debug("swagger.production:{}", prodStr);
                }
                prod = Boolean.parseBoolean(prodStr);
            }
            p = new ProductionSecurityFilter(prod);
        } else {
            p = new ProductionSecurityFilter(swaggerProperties.getKnife4j().isProduction());
        }

        return p;
    }
}