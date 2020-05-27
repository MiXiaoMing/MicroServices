package com.microservices.common.response;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class ResponseModel<T> implements Serializable {
    protected boolean success = false;
    protected String errCode = "";
    protected String message = "";
    protected T data;
    private Long execTime = 0L, startTime;

    public ResponseModel() {
        startTime = System.currentTimeMillis();
    }

    public ResponseModel(T iRows) {
        this.data = iRows;
    }


    public T getData() {
        return this.data;
    }

    public void setData(T rows) {
        this.data = rows;
        execTime = System.currentTimeMillis() - startTime;
    }

    public Long getExecTime() {
        return this.execTime;
    }

    public void setExecTime(Long execTime) {
        this.execTime = execTime;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
