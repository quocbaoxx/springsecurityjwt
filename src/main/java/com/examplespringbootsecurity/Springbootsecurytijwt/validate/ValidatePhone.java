package com.examplespringbootsecurity.Springbootsecurytijwt.validate;

public class ValidatePhone {
    public void checkPhone(String str) throws PhoneException {

        String reg = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$";

        boolean kt = str.matches(reg);

        if (kt == false) {
            throw new PhoneException("Lỗi: Không đúng định dạng SDT!");
        } else {
            System.out.println("Đúng định dạng số điện thoại !");
        }
    }
}
