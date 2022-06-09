package me.chat.connection;

public interface DefaultConnection {

    void login();

    void join();

    void leave(boolean login);
}
