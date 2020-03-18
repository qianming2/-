package com.ihrm.domain.system.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


@Data
@NoArgsConstructor
@ToString
public class FaceLoginResult implements Serializable {
    /**
     * 二维码使用状态
     * -1未使用
     * 0失败
     * 1登录成功 （返回用户id和token）
     */
    private String state;
    /**
     * 登录信息
     */
    private String token;
    /**
     * 用户ID
     */
    private String userId;

    public FaceLoginResult(String state, String token, String userId) {
        this.state = state;
        this.token = token;
        this.userId = userId;
    }

    public FaceLoginResult(String state) {
        this.state = state;
    }
}