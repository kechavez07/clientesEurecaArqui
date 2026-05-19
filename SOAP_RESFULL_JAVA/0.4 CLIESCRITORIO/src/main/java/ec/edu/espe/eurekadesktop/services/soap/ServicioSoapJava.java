package ec.edu.espe.eurekadesktop.services.soap;

import ec.edu.espe.eurekadesktop.models.Backend;
import ec.edu.espe.eurekadesktop.models.Deposito;
import ec.edu.espe.eurekadesktop.models.Movimiento;
import ec.edu.espe.eurekadesktop.models.Usuario;
import ec.edu.espe.eurekadesktop.services.interfaces.ServicioBancario;
import ec.edu.espe.eurekadesktop.utils.ConsolaDebug;
import ec.edu.espe.eurekadesktop.utils.XmlUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ServicioSoapJava implements ServicioBancario {
    private final String endpoint;
    private final Properties config;

    public ServicioSoapJava(Properties config) {
        this.config = config;
        this.endpoint = config.getProperty("soap.java.url", "http://209.145.48.25:8091/ROOT/CoreBancarioWS");
    }

    @Override
    public Usuario login(String username, String password) throws Exception {
        String xmlRequest = buildLoginRequest(username, password);
        ConsolaDebug.log("SOAP JAVA - LOGIN REQUEST", xmlRequest);

        String xmlResponse = sendSoapRequest(xmlRequest);
        ConsolaDebug.log("SOAP JAVA - LOGIN RESPONSE", xmlResponse);

        return parseLoginResponse(xmlResponse);
    }

    @Override
    public List<Movimiento> obtenerMovimientos(String token, String cuenta) throws Exception {
        String xmlRequest = buildMovimientosRequest(token, cuenta);
        ConsolaDebug.log("SOAP JAVA - MOVIMIENTOS REQUEST", xmlRequest);

        String xmlResponse = sendSoapRequest(xmlRequest);
        ConsolaDebug.log("SOAP JAVA - MOVIMIENTOS RESPONSE", xmlResponse);

        return parseMovimientosResponse(xmlResponse);
    }

    @Override
    public Deposito registrarDeposito(String token, String cuenta, double importe) throws Exception {
        String xmlRequest = buildDepositoRequest(token, cuenta, importe);
        ConsolaDebug.log("SOAP JAVA - DEPOSITO REQUEST", xmlRequest);

        String xmlResponse = sendSoapRequest(xmlRequest);
        ConsolaDebug.log("SOAP JAVA - DEPOSITO RESPONSE", xmlResponse);

        return parseDepositoResponse(xmlResponse);
    }

    @Override
    public String getEndpoint() {
        return endpoint;
    }

    @Override
    public Backend getBackend() {
        return Backend.SOAP_JAVA;
    }

    private String buildLoginRequest(String username, String password) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
               "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
               "xmlns:ws=\"http://ws.monster.edu.ec/\">" +
               "<soapenv:Header/>" +
               "<soapenv:Body>" +
               "<ws:login>" +
               "<ws:username>" + XmlUtils.escapeXml(username) + "</ws:username>" +
               "<ws:password>" + XmlUtils.escapeXml(password) + "</ws:password>" +
               "</ws:login>" +
               "</soapenv:Body>" +
               "</soapenv:Envelope>";
    }

    private String buildMovimientosRequest(String token, String cuenta) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
               "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
               "xmlns:ws=\"http://ws.monster.edu.ec/\">" +
               "<soapenv:Header/>" +
               "<soapenv:Body>" +
               "<ws:obtenerMovimientos>" +
               "<ws:token>" + XmlUtils.escapeXml(token) + "</ws:token>" +
               "<ws:cuenta>" + XmlUtils.escapeXml(cuenta) + "</ws:cuenta>" +
               "</ws:obtenerMovimientos>" +
               "</soapenv:Body>" +
               "</soapenv:Envelope>";
    }

    private String buildDepositoRequest(String token, String cuenta, double importe) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
               "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
               "xmlns:ws=\"http://ws.monster.edu.ec/\">" +
               "<soapenv:Header/>" +
               "<soapenv:Body>" +
               "<ws:registrarDeposito>" +
               "<ws:token>" + XmlUtils.escapeXml(token) + "</ws:token>" +
               "<ws:cuenta>" + XmlUtils.escapeXml(cuenta) + "</ws:cuenta>" +
               "<ws:importe>" + importe + "</ws:importe>" +
               "</ws:registrarDeposito>" +
               "</soapenv:Body>" +
               "</soapenv:Envelope>";
    }

    private String sendSoapRequest(String xml) throws Exception {
        java.net.URL url = new java.net.URL(endpoint);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        conn.setRequestProperty("SOAPAction", "\"\"");
        conn.setConnectTimeout(Integer.parseInt(config.getProperty("timeout.connection", "10000")));
        conn.setReadTimeout(Integer.parseInt(config.getProperty("timeout.read", "10000")));

        try (java.io.OutputStream os = conn.getOutputStream()) {
            os.write(xml.getBytes("UTF-8"));
        }

        int responseCode = conn.getResponseCode();
        ConsolaDebug.info("HTTP Response Code: " + responseCode);
        
        java.io.InputStream is = responseCode >= 400 ? conn.getErrorStream() : conn.getInputStream();
        
        if (is == null) {
            throw new Exception("HTTP " + responseCode + ": Sin cuerpo de respuesta");
        }
        
        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.InputStreamReader(is, "UTF-8"))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    private Usuario parseLoginResponse(String xml) throws Exception {
        if (xml.contains("soap:Fault") || xml.contains("Fault")) {
            String fault = XmlUtils.extractTag(xml, "faultstring");
            throw new Exception(fault != null ? fault : "Error en login");
        }

        String token = XmlUtils.extractTag(xml, "return");
        if (token == null || token.isEmpty()) {
            throw new Exception("Token no recibido");
        }

        return new Usuario("usuario", token, Backend.SOAP_JAVA);
    }

    private List<Movimiento> parseMovimientosResponse(String xml) {
        List<Movimiento> movimientos = new ArrayList<>();

        if (xml.contains("soap:Fault") || xml.contains("Fault")) {
            return movimientos;
        }

        String movimientosXml = XmlUtils.extractTagContent(xml, "obtenerMovimientosReturn");
        if (movimientosXml == null || movimientosXml.isEmpty()) {
            return movimientos;
        }

        String[] items = movimientosXml.split("</item>");
        for (String item : items) {
            if (item.contains("<item>")) {
                String data = item.replaceAll(".*<item>", "").trim();
                if (!data.isEmpty()) {
                    String[] campos = data.split(",");
                    if (campos.length >= 5) {
                        Movimiento m = new Movimiento();
                        m.setCuenta(campos[0].trim());
                        m.setTipo(campos[1].trim());
                        try { m.setMonto(Double.parseDouble(campos[2].trim())); } catch (Exception e) {}
                        m.setFecha(campos[3].trim());
                        m.setDescripcion(campos[4].trim());
                        movimientos.add(m);
                    }
                }
            }
        }

        return movimientos;
    }

    private Deposito parseDepositoResponse(String xml) throws Exception {
        Deposito deposito = new Deposito();

        if (xml.contains("soap:Fault") || xml.contains("Fault")) {
            String fault = XmlUtils.extractTag(xml, "faultstring");
            deposito.setResultado(fault != null ? fault : "Error en depósito");
            deposito.setExitoso(false);
            return deposito;
        }

        String resultado = XmlUtils.extractTag(xml, "return");
        deposito.setResultado(resultado != null ? resultado : "Depósito exitoso");
        deposito.setExitoso(true);
        return deposito;
    }
}