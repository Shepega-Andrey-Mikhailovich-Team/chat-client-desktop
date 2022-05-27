package me.chat.ssbus;

final class DispatchInfo {

    final DispatchEmitter emitter;
    final int priority;

    DispatchInfo(DispatchEmitter emitter, int priority) {
        this.emitter = emitter;
        this.priority = priority;
    }
}
