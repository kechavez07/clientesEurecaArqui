package com.eureka.consola.modelo;

import java.util.Arrays;
import java.util.List;

public class ConfiguracionBackends {
    public static final List<Backend> BACKENDS = Arrays.asList(
        new Backend("soap-java", "SOAP Java", "Java", "SOAP", "http://209.145.48.25:8091/ROOT/CoreBancarioWS"),
        new Backend("rest-java", "REST Java", "Java", "REST", "http://209.145.48.25:8090/resources/corebancario"),
        new Backend("soap-net", "SOAP .NET", ".NET", "SOAP", "http://209.145.48.25:8092/CoreBancarioWS"),
        new Backend("rest-net", "REST .NET", ".NET", "REST", "http://209.145.48.25:8093/resources/corebancario")
    );
}