package com.github.cato447.AbizeitungVotingSystem.entities;

import javax.persistence.*;

@Entity
@Table(name="auth_codes")
public class AuthCode {
    public AuthCode(){
        super();
    }

    public AuthCode(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private long time = System.currentTimeMillis();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }

    public boolean isExpired(){
            return System.currentTimeMillis() >= (time + 1800*1000);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Long getId() {
        return id;
    }
}
