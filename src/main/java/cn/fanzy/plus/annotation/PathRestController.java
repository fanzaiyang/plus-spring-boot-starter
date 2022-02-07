package cn.fanzy.plus.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;


/**
 * <pre>
 * 整合注解，少写点代码。
 * 可替代{@link RestController}和{@link RequestMapping}
 * </pre>
 *
 * @author fanzaiyang
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@RequestMapping
public @interface PathRestController {
    @AliasFor("path")
    String[] value() default {};

    @AliasFor("value")
    String[] path() default {};
}