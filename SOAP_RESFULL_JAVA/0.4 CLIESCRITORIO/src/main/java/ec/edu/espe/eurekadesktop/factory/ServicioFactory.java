package ec.edu.espe.eurekadesktop.factory;

import ec.edu.espe.eurekadesktop.models.Backend;
import ec.edu.espe.eurekadesktop.services.interfaces.ServicioBancario;
import ec.edu.espe.eurekadesktop.services.soap.ServicioSoapJava;
import ec.edu.espe.eurekadesktop.services.rest.ServicioRestJava;
import java.util.Properties;

public class ServicioFactory {
    private static Properties config;

    public static void setConfig(Properties config) {
        ServicioFactory.config = config;
    }

    public static ServicioBancario crearServicio(Backend backend) {
        if (config == null) {
            throw new IllegalStateException("Configuración no inicializada");
        }

        switch (backend) {
            case SOAP_JAVA:
                return new ServicioSoapJava(config);
            case REST_JAVA:
                return new ServicioRestJava(config);
            case SOAP_DOTNET:
            case REST_DOTNET:
            default:
                throw new UnsupportedOperationException("Backend no implementado: " + backend);
        }
    }
}