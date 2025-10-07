package com.Grupo.ProjetoAcessibilidade.utils;

public class RequestContext {
    private static final ThreadLocal<String> currentUri = new ThreadLocal<>();

    public static void setUri(String uri) {
        currentUri.set(uri);
    }

    public static String getUri() {
        return currentUri.get();
    }

    public static void clear() {
        currentUri.remove();
    }
}