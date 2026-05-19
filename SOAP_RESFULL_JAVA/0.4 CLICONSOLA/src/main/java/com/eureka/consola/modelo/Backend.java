package com.eureka.consola.modelo;

public class Backend {
    private final String id;
    private final String nombre;
    private final String tecnologia;
    private final String protocolo;
    private final String urlBase;

    public Backend(String id, String nombre, String tecnologia, String protocolo, String urlBase) {
        this.id = id;
        this.nombre = nombre;
        this.tecnologia = tecnologia;
        this.protocolo = protocolo;
        this.urlBase = urlBase;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTecnologia() { return tecnologia; }
    public String getProtocolo() { return protocolo; }
    public String getUrlBase() { return urlBase; }
}