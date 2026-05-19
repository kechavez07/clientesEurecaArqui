package com.eureka.consola.config;

import com.eureka.consola.modelo.Backend;
import com.eureka.consola.modelo.ConfiguracionBackends;

public class ContextoBackend {
    private static Backend backendActual = ConfiguracionBackends.BACKENDS.get(0);

    public static Backend obtenerBackendActual() {
        return backendActual;
    }

    public static void cambiarBackend(String id) {
        for (Backend backend : ConfiguracionBackends.BACKENDS) {
            if (backend.getId().equals(id)) {
                backendActual = backend;
                return;
            }
        }
    }
}