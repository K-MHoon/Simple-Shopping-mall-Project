package com.shop.mall.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberFormDto {
    private String name;
    private String email;
    private String password;
    private String address;
}
