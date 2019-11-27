package com.groupten.online_music.entity;

import com.groupten.online_music.entity.entityEnum.ConfirmStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_email_confirm")
public class EmailConfirm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eid;
    @Column(length = 40)
    private String email;
    @Column(length = 10)
    private String checkCode;
    private Date confirmTime;
    @Enumerated(EnumType.STRING)
    private ConfirmStatus status;

    public EmailConfirm(String to, String checkCode, Date date, ConfirmStatus status) {
        this.email = to;
        this.checkCode = checkCode;
        this.confirmTime = date;
        this.status = status;
    }

    public EmailConfirm() {
    }

    public ConfirmStatus getStatus() {
        return status;
    }

    public void setStatus(ConfirmStatus status) {
        this.status = status;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }
}
