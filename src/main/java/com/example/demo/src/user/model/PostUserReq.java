package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostUserReq {

    // @ControllerAdvice , ExceptionHandler를 사용하기엔 구조 상 애매해서 사용 안함.
 //   @NotBlank
    private String email;

   // @NotBlank
    private String password;

   // @NotBlank
    private String name;

    //@NotBlank
    private String phone;
}
