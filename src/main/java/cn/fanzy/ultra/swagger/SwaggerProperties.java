/**
 *
 */
package cn.fanzy.ultra.swagger;

import cn.hutool.core.collection.CollUtil;
import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendSetting;
import com.github.xiaoymin.knife4j.core.model.MarkdownProperty;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jHttpBasic;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * swagger扩展支持属性配置
 *
 * @author fanzaiyang
 * @since  2021/09/07
 */
@ConfigurationProperties(prefix = "plus.swagger")
public class SwaggerProperties {
    public static final List<String> SWAGGER_LIST = CollUtil.toList("/doc.html", "/swagger-resources/**", "/webjars/**", "/favicon.ico", "/error");
    /**
     * 是否启用swagger标志
     */
    private Boolean enable=true;
    /**
     * swagger 文档的标题
     */
    private String title = "API接口文档";
    /**
     * swagger 文档的描述
     */
    private String description = " 接口说明文档";
    /**
     * swagger 文档的中组织的链接
     */
    private String termsOfServiceUrl = "https://gitee.com/it-xiaofan";
    /**
     * swagger 文档的分组名
     */
    private String groupName = "默认分组";
    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 项目联系人
     *
     */
    private String contactUser = "it-xiaofan";

    /**
     * 项目的url
     */
    private String contactUrl = "https://gitee.com/it-xiaofan";

    /**
     * 项目联系邮箱
     */
    private String contactEmail = "fanzaiyang@hotmail.com";


    /**
     * 附加信息
     */
    private List<AuthoriZationPar> auths = new ArrayList<>();

    /**
     * 是否显示加载日志，默认为false
     */
    private Boolean showDeatil = false;

    /**
     * 扩展
     */
    private Knife4jProperties knife4j=new Knife4jProperties();
    /**
     * 附加信息
     *
     * @author fanzaiyang
     * @version 1.0.0
     * @since 1.0.0
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthoriZationPar implements Serializable {
        /**
         *
         */
        private static final long serialVersionUID = 7046632466056115744L;
        /**
         * 附加参数的名字
         */
        private String name;
        /**
         * 附加参数的描述
         */
        private String description;
        /**
         * 参数值的类型，例如string
         */
        private String modelRef;
        /**
         * 参数的格式化类型，例如 email
         */
        private String format;
        /**
         * 参数的位置
         */
        private String parameterType;
        /**
         * 是否为必需参数,默认为false
         */
        private Boolean required = false;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Knife4jProperties {
        /**
         * 是否开启Knife4j增强模式
         */
        private boolean enable = true;
        /**
         * 是否开启默认跨域
         */
        private boolean cors = false;

        /**
         * 是否开启BasicHttp验证
         */
        private Knife4jHttpBasic basic;

        /**
         * 是否生产环境
         */
        private boolean production = false;

        /**
         * 个性化配置
         */
        private OpenApiExtendSetting setting;

        /**
         * 分组文档集合
         */
        private List<MarkdownProperty> documents;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContactUser() {
        return contactUser;
    }

    public void setContactUser(String contactUser) {
        this.contactUser = contactUser;
    }

    public String getContactUrl() {
        return contactUrl;
    }

    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public List<AuthoriZationPar> getAuths() {
        return auths;
    }

    public void setAuths(List<AuthoriZationPar> auths) {
        this.auths = auths;
    }

    public Boolean getShowDeatil() {
        return showDeatil;
    }

    public void setShowDeatil(Boolean showDeatil) {
        this.showDeatil = showDeatil;
    }

    public Knife4jProperties getKnife4j() {
        return knife4j;
    }

    public void setKnife4j(Knife4jProperties knife4j) {
        this.knife4j = knife4j;
    }
}
