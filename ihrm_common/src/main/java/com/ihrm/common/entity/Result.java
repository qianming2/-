package com.ihrm.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据相应对象
 *    success :是否成功
 *    code    :返回码
 *    message :返回信息
 *    data    :返回数据
 * */

@Data
@NoArgsConstructor
public class Result {
    private boolean success;//是否成功
    private Integer code;//返回码
    private String message;//返回信息
    private Object data;//返回数据

    public Result( ResultCode code){
        this.success=code.success;
        this.code=code.code;
        this.message=code.message;
    }
    public Result( ResultCode code,Object data){
        this.success=code.success;
        this.code=code.code;
        this.message=code.message;
        this.data=data;
    }

    public Result(boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public static Result SUCCESS(){
        return new Result(ResultCode.SUCCESS);
    }
    public static Result ERROR(){
        return new Result(ResultCode.SERVER_ERROR);
    };
    public static Result FAIL(){
        return new Result(ResultCode.FAIL);
    };
}
