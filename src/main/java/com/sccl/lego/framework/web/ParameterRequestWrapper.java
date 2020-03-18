package com.sccl.lego.framework.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

/**
 * 基于servletRequest，对请求进行包装
 * 对请求参数的修改设置和输出流的读取
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper {
    /**
     * 处理后的请求参数map
     */
    private Map newParams;

    /**
     * 构造
     * @param request
     * @param newParams
     */
    public ParameterRequestWrapper(HttpServletRequest request, Map newParams) {
        super(request);
        this.newParams = newParams;
    }

    /**
     * 获取处理后的参数map
     * @return
     */
    @Override
    public Map getParameterMap() {
        return newParams;
    }

    /**
     * 获取处理后的参数名称
     * @return
     */
    @Override
    public Enumeration getParameterNames() {
        Vector l = new Vector(newParams.keySet());
        return l.elements();
    }

    /**
     * 根据参数名称获取参数值
     * @param name 参数名称
     * @return
     */
    @Override
    public String[] getParameterValues(String name) {
        Object v = newParams.get(name);
        if (v == null) {
            return null;
        } else if (v instanceof String[]) {
            return (String[]) v;
        } else if (v instanceof String) {
            return new String[]{(String) v};
        } else {
            return new String[]{v.toString()};
        }
    }

    @Override
    public String getParameter(String name) {
        Object v = newParams.get(name);
        if (v == null) {
            return null;
        } else if (v instanceof String[]) {
            String[] strArr = (String[]) v;
            if (strArr.length > 0) {
                return strArr[0];
            } else {
                return null;
            }
        } else if (v instanceof String) {
            return (String) v;
        } else {
            return v.toString();
        }
    }
}
