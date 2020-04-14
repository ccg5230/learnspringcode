package com.ccg.mvcframework.ioc.servlet;


import com.alibaba.fastjson.JSON;
import com.ccg.bean.Data;
import com.ccg.bean.Handler;
import com.ccg.bean.Param;
import com.ccg.bean.View;
import com.ccg.mvcframework.helper.*;
import com.ccg.mvcframework.ioc.annotation.RequestParm;
import com.ccg.util.ReflectionUtil;
import com.ccg.util.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @className MyDispatchservlet
 * @Description
 * @Author chungaochen
 * Date 2020/4/4 14:52
 * Version 1.0
 **/
@SuppressWarnings("unchecked")//使用了未经检查或不安全的操作。 注: 有关详细信息, 请使用 -Xlint:unchecked 重新编译。
public class Dispatchservlet extends HttpServlet {

    private Properties contextConfig = new Properties();
    private List<String> classNames = new ArrayList<>();
    private Map<String, Object> ioc = new HashMap<>(); //实例化bean保存容器
    private Map<String, Method> handlerMapping = new HashMap<>();//mapping的url与方法映射

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
//        //1.加载配置文件
//        doLoadConfig(servletConfig.getInitParameter("contextConfigLocation"));
//        //2.扫描相关类
//        doScanner(contextConfig.getProperty("scanBasePackage"));
//        //3.实例化相关类
//        doInitStance();
//        //4.完成依赖注入
//        doAutowried();
//        //5.初始化HandMapping
//        doInitHandlerMapping();
//        System.out.println("My framework start and init success!");

        //初始化相关的helper类
        HelperLoader.init();

        //获取ServletContext对象, 用于注册Servlet
        ServletContext servletContext = servletConfig.getServletContext();

        //注册处理jsp和静态资源的servlet
        registerServlet(servletContext);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            //6调用
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("500 Exception Detail:" + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestMethod = request.getMethod().toUpperCase();
        String requestPath = request.getPathInfo();

        //根据请求获取处理器(这里类似于SpringMVC中的映射处理器)
        Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
        if (handler != null) {
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);

            //初始化参数
            Param param = RequestHelper.createParam(request);

            //调用与请求对应的方法(这里类似于SpringMVC中的处理器适配器)
            Object result;
            Method actionMethod = handler.getControllerMethod();
            if (param == null || param.isEmpty()) {
                result = ReflectionUtil.invokeMethod(controllerBean, actionMethod);
            } else {
                /**
                 invokeMethod方法调用，Object... args可变参数不能直接传数组，需要转换new Object[]{req, resp, name}
                 Controller如果自由定义参数，反射调用很困难，详细参考请读
                 SpringMVC源码之参数解析绑定原理：https://www.cnblogs.com/w-y-c-m/p/8443892.html
                 */
                result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
            }

            //跳转页面或返回json数据(这里类似于SpringMVC中的视图解析器)
            if (result instanceof View) {
                handleViewResult((View) result, request, response);
            } else if (result instanceof Data) {
                handleDataResult((Data) result, response);
            }
        }
    }

    /**
     * 跳转页面
     */
    private void handleViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = view.getPath();
        if (StringUtils.isNotBlank(path)) {
            if (path.startsWith("/")) { //重定向
                response.sendRedirect(request.getContextPath() + path);
            } else { //请求转发
                Map<String, Object> model = view.getModel();
                for (Map.Entry<String, Object> entry : model.entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
                request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
            }
        }
    }

    /**
     * 返回JSON数据
     */
    private void handleDataResult(Data data, HttpServletResponse response) throws IOException {
        Object model = data.getModel();
        if (model != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            String json = JSON.toJSON(model).toString();
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }
    /**
     * DefaultServlet和JspServlet都是由Web容器创建
     * org.apache.catalina.servlets.DefaultServlet
     * org.apache.jasper.servlet.JspServlet
     */
    private void registerServlet(ServletContext servletContext) {
        //动态注册处理JSP的Servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");

        //动态注册处理静态资源的默认Servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping("/favicon.ico"); //网站头像
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }




    class ParamIsNullException extends RuntimeException {
        private final String parameterName;
        private final String parameterType;

        public ParamIsNullException(String parameterName, String parameterType) {
            super("");
            this.parameterName = parameterName;
            this.parameterType = parameterType;
        }

        @Override
        public String getMessage() {
            return "Required " + this.parameterType + " parameter \'" + this.parameterName + "\' must be not null !";
        }

        public final String getParameterName() {
            return this.parameterName;
        }

        public final String getParameterType() {
            return this.parameterType;
        }
    }


    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String uri = req.getRequestURI();//getRequestURL是全路径：http://localhost:8080/WebServlet/servlet/api
        String contextPath = req.getContextPath();
        uri = uri.replaceAll(contextPath, "").replaceAll("/+", "/");
        if (!this.handlerMapping.containsKey(uri)) {
            resp.getWriter().write("404  Request url is Not Found! ");
            return;
        }

        Map<String, String[]> params = req.getParameterMap();
        Method method = this.handlerMapping.get(uri);
        String beanName = toLoverFirstCase(method.getDeclaringClass().getSimpleName());
        //获取方法参数注解，返回二维数组是因为某些参数可能存在多个注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        //获取方法参数类型
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                Annotation ann = parameterAnnotations[i][j];
                if (ann != null && ann instanceof RequestParm && ((RequestParm) ann).required()) {
                    //获取参数值
                    if (params.get(((RequestParm) ann).value()) == null || "".equals(params.get(((RequestParm) ann).value())[0])) {
                        paramIsNull(((RequestParm) ann).value(), null, parameterTypes[i] == null ? null : parameterTypes[i].getName());
                        break;
                    }
                }
            }
    }
        String name = params.get("name")[0];
        method.invoke(ioc.get(beanName), new Object[]{req, resp, name});

    }

    private String toLoverFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        /* har类型即字符类型，可以int来转换，转换规则见ascii码表 :增加32之后，如果原来是大写字母，会变成小写字母；小写字母转换大写字母要减去32
         */
        chars[0] += 32;
        return String.valueOf(chars);
    }

    /**
     * 参数非空校验，如果参数为空，则抛出ParamIsNullException异常
     *
     * @param paramName
     * @param value
     * @param parameterType
     */
    private void paramIsNull(String paramName, Object value, String parameterType) {
        if (value == null || "".equals(value.toString().trim())) {
            throw new ParamIsNullException(paramName, parameterType);
        }
    }
}