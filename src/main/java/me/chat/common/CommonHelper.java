package me.chat.common;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;

public final class CommonHelper {

    private static final String[] SCRIPT_ENGINE_ARGS = {"-strict", "--language=es6", "--optimistic-types=false"};

    public static ScriptEngine newScriptEngine() {
        return new NashornScriptEngineFactory().getScriptEngine(SCRIPT_ENGINE_ARGS);
    }

    public static Thread newThread(String name, boolean daemon, Runnable runnable)
    {
        Thread thread = new Thread(runnable);
        thread.setDaemon(daemon);
        if (name != null) {
            thread.setName(name);
        }
        return thread;
    }

    public static String replace(String source, String... params)
    {
        for (int i = 0; i < params.length; i += 2)
        {
            source = source.replace('%' + params[i] + '%', params[i + 1]);
        }
        return source;
    }
}
