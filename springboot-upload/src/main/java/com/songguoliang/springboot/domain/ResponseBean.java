package com.songguoliang.springboot.domain;

public class ResponseBean {
    private String errorCode;
    private String msg;
    private Object data;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    private ResponseBean(){

    }
    private static ResponseBean responseBean = new ResponseBean();

    public static ResponseBean error(String msg,Object data){
        responseBean.setMsg(msg);
        responseBean.setErrorCode("-1");
        responseBean.setData(data);
        return responseBean;
    }

    public static ResponseBean success(String msg,Object data){
        responseBean.setMsg(msg);
        responseBean.setErrorCode("0");
        responseBean.setData(data);
        return responseBean;
    }

}
