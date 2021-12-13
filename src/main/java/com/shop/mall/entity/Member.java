package com.shop.mall.entity;

import com.shop.mall.constant.Role;
import com.shop.mall.dto.MemberFormDto;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;


    public static Member createMember(MemberFormDto memberFromDto, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .name(memberFromDto.getName())
                .email(memberFromDto.getEmail())
                .address(memberFromDto.getAddress())
                .password(passwordEncoder.encode(memberFromDto.getPassword()))
                .role(Role.ADMIN)
                .build();
    }
}
