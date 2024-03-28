package edu.pnu.myspring.web.handler;

import edu.pnu.myspring.annotations.MyRequestMapping;
import edu.pnu.myspring.annotations.PostMapping;
import edu.pnu.myspring.web.dispatcher.ControllerRegistry;
import edu.pnu.myspring.web.http.UserRequest;
import edu.pnu.myspring.web.http.UserResponse;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MyHandlerAdapter {
    private MyHandlerMapping handlerMapping;

    public MyHandlerAdapter(MyHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }
    public boolean supports(Object handler){
        return (handler instanceof Method) &&
                (((Method)handler).isAnnotationPresent(MyRequestMapping.class)||((Method)handler).isAnnotationPresent(PostMapping.class));
    }

    public UserResponse handle(UserRequest userRequest, Object handler, Object[] args) throws Exception {
        if(!(handler instanceof Method)){
            throw new IllegalArgumentException("Handler is not a method");
        }
        if(!supports(handler)){
            throw new IllegalArgumentException("Handler method is not annotated with MyRequestMapping or PostMapping");
        }
        Method method = (Method) handler;
        Object controller = handlerMapping.getControllerForMethod(method);
        Object result = method.invoke(controller, args);

        String responseBody = result.toString();

        return new UserResponse(200, responseBody);
    }

}
