package me.chat.ssbus;

public interface RegisteredListener {

    /**
     * Causing the handler to unregister Running this method multiple times will have no effect
     */
    void unregister();
}
