package pl.lodz.it.sitodruk.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pl.lodz.it.sitodruk.exceptions.BaseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class Interceptor extends HandlerInterceptorAdapter {

    private String transactionId;

    @Override
    public boolean preHandle(HttpServletRequest requestServlet, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HandlerMethod method = (HandlerMethod) o;
        String login = requestServlet.getRemoteUser();
        transactionId = Long.toString(System.currentTimeMillis()) + ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        log.info("Transaction ID: " + transactionId + " has been started, account: " + login + ", method " + method.getShortLogMessage());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        HandlerMethod method = (HandlerMethod) o;
        String login = request.getRemoteUser();
        log.info("Transaction with ID: " + transactionId + " post handle , account:" + login + ", method " + method.getShortLogMessage());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, Exception exception) throws Exception {
        HandlerMethod method = (HandlerMethod) o;
        String login = request.getRemoteUser();
        log.info("Transaction with ID: " + transactionId + " after completion, account:" + login + ", method " + method.getShortLogMessage());
    }
}