package com.examplespringbootsecurity.Springbootsecurytijwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.PublicKey;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBase {

    private Object data;

    private long total;

    private String message;

    private String code;

    public ResponseBase(Object data, long total) {
        this.message = "Yêu cầu thực hiện thành công!";
        this.code = "200";
        this.total = total;
        this.data = data;
    }




    public ResponseBase(Object data) {
        this.message = "Yêu cầu thực hiện thành công!";
        this.code = "200";
        this.data = data;
    }


}



