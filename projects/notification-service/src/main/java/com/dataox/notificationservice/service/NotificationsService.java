package com.dataox.notificationservice.service;

public interface NotificationsService {

    void sendInternal(String message);

    void sendExternal(String message);

    void sendAll(String message);

}
