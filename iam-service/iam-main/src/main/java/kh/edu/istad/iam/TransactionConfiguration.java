package kh.edu.istad.iam;

import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.lang.reflect.Method;

@Configuration(proxyBeanMethods = false)
class TransactionConfiguration {

    private static final String USE_CASE_PACKAGE = "kh.edu.istad.iam.domain.usecase.";

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    static Advisor useCaseTransactionAdvisor(PlatformTransactionManager transactionManager) {
        var pointcut = new StaticMethodMatcherPointcut() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                return "execute".equals(method.getName())
                        && targetClass.getName().startsWith(USE_CASE_PACKAGE)
                        && targetClass.getSimpleName().endsWith("UseCase");
            }
        };
        var interceptor = new TransactionInterceptor(transactionManager, new MatchAlwaysTransactionAttributeSource());
        return new DefaultPointcutAdvisor(pointcut, interceptor);
    }
}
