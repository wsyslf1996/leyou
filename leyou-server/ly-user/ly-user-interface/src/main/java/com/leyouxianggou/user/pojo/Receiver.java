package com.leyouxianggou.user.pojo;

import lombok.Data;

import javax.persistence.*;

@Table(name = "tb_receiver")
@Data
public class Receiver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "receiver_name")
    private String name;

    @Column(name = "receiver_phone")
    private String phone;

    @Column(name = "receiver_province")
    private String province;

    @Column(name = "receiver_city")
    private String city;

    @Column(name = "receiver_district")
    private String district;

    @Column(name = "receiver_post_code")
    private String postCode;

    @Column(name = "receiver_address")
    private String address;

    @Column(name = "label")
    private String label;

    @Column(name = "default_address")
    private Boolean defaultAddress;
}
