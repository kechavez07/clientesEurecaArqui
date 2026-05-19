package com.eureka.consola.servicio.soap;

import com.eureka.consola.config.ContextoBackend;
import com.eureka.consola.modelo.Movimiento;
import com.eureka.consola.modelo.Resultado;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.*;
import java.net.*;

public class ServicioSoap {
    
    private String construirEnvelope(String metodo, String... parametros) {
        StringBuilder params = new StringBuilder();
        for (int i = 0; i < parametros.length; i += 2) {
            params.append("<").append(parametros[i]).append(">").append(parametros[i + 1]).append("</").append(parametros[i]).append(">");
        }
        
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
               "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.monster.edu.ec/\">" +
               "<soapenv:Header/>" +
               "<soapenv:Body>" +
               "<ws:" + metodo + ">" + params.toString() + "</ws:" + metodo + ">" +
               "</soapenv:Body>" +
               "</soapenv:Envelope>";
    }

    private String realizarSolicitud(String metodo, String... parametros) throws Exception {
        String urlBase = ContextoBackend.obtenerBackendActual().getUrlBase();
        URL url = new URL(urlBase);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        conn.setRequestProperty("SOAPAction", urlBase + "/" + metodo);
        conn.setDoOutput(true);
        conn.setDoInput(true);

        String xml = construirEnvelope(metodo, parametros);
        
        try (OutputStream os = conn.getOutputStream()) {
            os.write(xml.getBytes("UTF-8"));
        }

        StringBuilder respuesta = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                respuesta.append(linea);
            }
        }
        
        return respuesta.toString();
    }

    public Resultado autenticar(String usuario, String contrasena) {
        try {
            String respuestaXml = realizarSolicitud("login", "usuario", usuario, "password", contrasena);
            
            if (respuestaXml.contains("<return>1</return>") || 
                respuestaXml.contains("<return>true</return>")) {
                return new Resultado(true, "Autenticacion exitosa");
            }
            return new Resultado(false, "Credenciales invalidas");
        } catch (Exception e) {
            return new Resultado(false, "Error de conexion con el servidor: " + e.getMessage());
        }
    }

    public Resultado registrarDeposito(String cuenta, String importe) {
        try {
            String respuestaXml = realizarSolicitud("registrarDeposito", "cuenta", cuenta, "importe", importe);
            
            if (respuestaXml.contains("<return>1</return>") || 
                respuestaXml.contains("<return>true</return>")) {
                return new Resultado(true, "Deposito exitoso: " + importe + " a cuenta " + cuenta);
            }
            return new Resultado(false, "Error al procesar el deposito");
        } catch (Exception e) {
            return new Resultado(false, "Error de conexion: " + e.getMessage());
        }
    }

    public Resultado registrarRetiro(String cuenta, String importe) {
        try {
            String respuestaXml = realizarSolicitud("registrarRetiro", "cuenta", cuenta, "importe", importe);
            
            if (respuestaXml.contains("<return>1</return>") || 
                respuestaXml.contains("<return>true</return>")) {
                return new Resultado(true, "Retiro exitoso: " + importe + " de cuenta " + cuenta);
            }
            return new Resultado(false, "Error al procesar el retiro");
        } catch (Exception e) {
            return new Resultado(false, "Error de conexion: " + e.getMessage());
        }
    }

    public Resultado registrarTransferencia(String cuentaOrigen, String cuentaDestino, String importe) {
        try {
            String respuestaXml = realizarSolicitud("registrarTransferencia", 
                "cuentaOrigen", cuentaOrigen, "cuentaDestino", cuentaDestino, "importe", importe);
            
            if (respuestaXml.contains("<return>1</return>") || 
                respuestaXml.contains("<return>true</return>")) {
                return new Resultado(true, "Transferencia exitosa: " + importe + " de " + cuentaOrigen + " a " + cuentaDestino);
            }
            return new Resultado(false, "Error al procesar la transferencia");
        } catch (Exception e) {
            return new Resultado(false, "Error de conexion: " + e.getMessage());
        }
    }

    public ResultadoMovimientos obtenerMovimientos(String cuenta) {
        try {
            String respuestaXml = realizarSolicitud("obtenerMovimientos", "cuenta", cuenta);
            return parsearMovimientos(respuestaXml);
        } catch (Exception e) {
            return new ResultadoMovimientos(false, "Error al consultar movimientos: " + e.getMessage());
        }
    }

    private ResultadoMovimientos parsearMovimientos(String xml) {
        try {
            java.util.List<Movimiento> movimientos = new java.util.ArrayList<>();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
            
            NodeList nodosMovimiento = doc.getElementsByTagName("movimiento");
            
            for (int i = 0; i < nodosMovimiento.getLength(); i++) {
                Node nodo = nodosMovimiento.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodo;
                    Movimiento mov = new Movimiento();
                    mov.setCuenta(obtenerTexto(elemento, "cuenta"));
                    mov.setNromov(obtenerTexto(elemento, "nromov"));
                    mov.setFecha(obtenerTexto(elemento, "fecha"));
                    mov.setTipo(obtenerTexto(elemento, "tipo"));
                    mov.setAccion(obtenerTexto(elemento, "accion"));
                    mov.setImporte(obtenerTexto(elemento, "importe"));
                    movimientos.add(mov);
                }
            }
            
            return new ResultadoMovimientos(true, "", movimientos);
        } catch (Exception e) {
            return new ResultadoMovimientos(false, "Error al parsear movimientos: " + e.getMessage());
        }
    }

    private String obtenerTexto(Element elemento, String tagHijo) {
        NodeList nodos = elemento.getElementsByTagName(tagHijo);
        if (nodos.getLength() > 0) {
            String contenido = nodos.item(0).getTextContent();
            return contenido != null ? contenido : "";
        }
        return "";
    }

    public static class ResultadoMovimientos {
        private final boolean exito;
        private final String mensaje;
        private final java.util.List<Movimiento> movimientos;

        public ResultadoMovimientos(boolean exito, String mensaje) {
            this(exito, mensaje, new java.util.ArrayList<>());
        }

        public ResultadoMovimientos(boolean exito, String mensaje, java.util.List<Movimiento> movimientos) {
            this.exito = exito;
            this.mensaje = mensaje;
            this.movimientos = movimientos;
        }

        public boolean isExito() { return exito; }
        public String getMensaje() { return mensaje; }
        public java.util.List<Movimiento> getMovimientos() { return movimientos; }
    }
}