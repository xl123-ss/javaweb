package com.xxx.vto;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class R {

    private Boolean success;

    private Integer code;

    private String message;

    private Map<String, Object> data = new HashMap<String, Object>();
    private R() {}
    public static R ok() {
        R r = new R();
        r.setSuccess(true);
        r.setCode(200); //0
        r.setMessage("成功");
        return r;
    }

    public static R error() {
        R r = new R();
        r.setSuccess(true);
        r.setCode(500); //1
        r.setMessage("失败");
        return r;
    }
    public R success(boolean success) {
        this.setSuccess(success);
        return this;
    }

    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    public R data(String key, Object val) {
        this.data.put(key, val);
        return this;
    }

    public R data(Map<String, Object> data) {
        this.setData(data);
        return this;
    }

    public String toJsonStr(){
        return JSONArray.toJSONString(this);
    }


}
