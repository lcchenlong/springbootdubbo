package com.example.dubbo.demo.service.aop;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

/**
 * 记录日志切面
 * @author chenlong12
 *
 */
@Component
@Aspect
public class LogAspect {
	
	private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

	//定义切点
	@Pointcut("execution(public * com.example.dubbo.demo.service.impl..*.*(..))")
	public void serviceLog(){}
	
	@Around("serviceLog()")
	public Object logBefore(ProceedingJoinPoint pj) throws Throwable{
		//接口请求的开始时间
		Long startTimeMillis = System.currentTimeMillis();
		JSONObject paramJson = this.printMethodParams(pj,String.valueOf(startTimeMillis));
		logger.info("请求前：{}",paramJson.toString());
		
		Object retVal = pj.proceed();
		
		JSONObject returnJson = new JSONObject();
        returnJson.put("class_name",paramJson.get("class_name"));
        returnJson.put("method_name",paramJson.get("method_name"));
        returnJson.put("class_name_method",paramJson.get("class_name_method"));
        returnJson.put("return_name",retVal);
        Long endTimeMillis = System.currentTimeMillis();
        returnJson.put("endTimeMillis",endTimeMillis);
        returnJson.put("times",endTimeMillis - startTimeMillis);
        logger.info("请求后："+returnJson.toString());
        
        return retVal;
        
	}
	
	/**
	 * 抛除异常时的记录
	 * @param joinPoint
	 * @param e
	 * @throws IOException
	 */
	 @AfterThrowing(pointcut = "serviceLog()", throwing = "e")
	 public void handleThrowing(JoinPoint joinPoint, Exception e) throws IOException {
		 Long startTimeMillis = System.currentTimeMillis();
		 JSONObject paramJson = this.printMethodParams(joinPoint,String.valueOf(startTimeMillis));
		 //获取错误详细信息
		 StringWriter sw = new StringWriter();
	     PrintWriter pw = new PrintWriter(sw);
	     e.printStackTrace(pw);
		 paramJson.put("errorMsg", e.getMessage());
		 paramJson.put("StackTrace", sw.toString());
		 logger.info("请求错误："+paramJson.toString());
		 pw.flush();   
		 sw.flush();   
	}

	/**
     * 打印类method的名称以及参数
     * @param point 切面
     */
    public JSONObject printMethodParams(JoinPoint point,String startTimeMillis){
        if(point == null){
            return new JSONObject();
        }
        /**
         * Signature 包含了方法名、申明类型以及地址等信息
         */
        String class_name = point.getTarget().getClass().getName();
        String method_name = point.getSignature().getName();
    
        logger.info("class_name = {},startTimeMillis:"+startTimeMillis,class_name);
        logger.info("method_name = {},startTimeMillis:"+startTimeMillis,method_name);
 
        JSONObject paramJson = new JSONObject();
        paramJson.put("class_name",class_name);
        paramJson.put("method_name",method_name);
        paramJson.put("startTimeMillis",startTimeMillis);
        paramJson.put("class_name_method", String.format("%s.%s", class_name,method_name));
        /**
         * 获取方法的参数值数组。
         */
        Object[] method_args = point.getArgs();
 
        try {
             //获取方法参数名称
            String[] paramNames = getFieldsName(class_name, method_name);
 
            //打印方法的参数名和参数值
            String param_name = logParam(paramNames,method_args);
            paramJson.put("param_name",JSONObject.parse(param_name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paramJson;
    }
 
    /**
     * 使用javassist来获取方法参数名称
     * @param class_name    类名
     * @param method_name   方法名
     * @return
     * @throws Exception
     */
    private String[] getFieldsName(String class_name, String method_name) throws Exception {
        Class<?> clazz = Class.forName(class_name);
        String clazz_name = clazz.getName();
        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(clazz);
        pool.insertClassPath(classPath);
 
        CtClass ctClass = pool.get(clazz_name);
        CtMethod ctMethod = ctClass.getDeclaredMethod(method_name);
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if(attr == null){
            return null;
        }
        String[] paramsArgsName = new String[ctMethod.getParameterTypes().length];
        // 如果是静态方法，则第一就是参数
        // 如果不是静态方法，则第一个是"this"，然后才是方法的参数
        // 我接口中没有写public修饰词，导致我的数组少一位参数，所以再往后一位，原本应该是   XX ? 0 : 1
        int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
        for (int i=0;i<paramsArgsName.length;i++){
            paramsArgsName[i] = attr.variableName(i+pos);
        }
        return paramsArgsName;
    }
 
 
    /**
     * 判断是否为基本类型：包括String
     * @param clazz clazz
     * @return  true：是;     false：不是
     */
    private boolean isPrimite(Class<?> clazz){
        if (clazz.isPrimitive() || clazz == String.class){
            return true;
        }else {
            return false;
        }
    }
 
 
    /**
     * 打印方法参数值  基本类型直接打印，非基本类型需要重写toString方法
     * @param paramsArgsName    方法参数名数组
     * @param paramsArgsValue   方法参数值数组
     */
    private String logParam(String[] paramsArgsName,Object[] paramsArgsValue){
        StringBuffer buffer = new StringBuffer();
        if(ArrayUtils.isEmpty(paramsArgsName) || ArrayUtils.isEmpty(paramsArgsValue)){
            buffer.append("{\"noargs\":\"该方法没有参数\"}");
            return buffer.toString();
        }
        for (int i=0;i<paramsArgsName.length;i++){
            //参数名
            String name = paramsArgsName[i];
            //参数值
            Object value = paramsArgsValue[i];
            buffer.append("\""+name+"\":");
            if(isPrimite(value.getClass())){
                buffer.append("\""+value+"\",");
            }else {
                buffer.append(JSON.toJSONString(value)+",");
            }
        }
        return "{"+buffer.toString().substring(0,buffer.toString().length()-1)+"}";
    }

}
