package me.chat.ssbus;

interface Dispatcher<E> {

    void dispatch(E event);
}
