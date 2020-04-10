package com.microservices.support;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResponseArrayModel<T> implements Serializable {
    protected boolean success = false;
    protected String errCode = "";
    protected List<T> data = new ArrayList<>();
    protected String message = "";
    private Long execTime = 0L, startTime;

    public ResponseArrayModel() {
        startTime = System.currentTimeMillis();
    }

    public ResponseArrayModel(List<T> iRows) {
        this.data = iRows;
    }


    public List<T> getData() {
        return this.data;
    }

    public void setData(List<T> rows) {
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
