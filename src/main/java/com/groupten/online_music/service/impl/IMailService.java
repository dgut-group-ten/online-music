package com.groupten.online_music.service.impl;

public interface IMailService {
    public boolean sendSimpleMail(String to, String title, String content);
}
