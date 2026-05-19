package com.eureka.consola.servicio.rest;

import com.eureka.consola.config.ContextoBackend;
import com.eureka.consola.modelo.Movimiento;
import com.eureka.consola.modelo.Resultado;
import com.eureka.consola.servicio.soap.ServicioSoap;

import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ServicioRest {
    
    private String realizarSolicitud(String endpoint, String jsonBody, String metodoHttp) throws Exception {
        String urlBase = ContextoBackend.obtenerBackendActual().getUrlBase();
        URL url = new URL(urlBase + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setRequestMethod(metodoHttp);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.setDoInput(true);

        if (jsonBody != null) {
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes("UTF-8"));
            }
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
            String json = "{\"usuario\":\"" + usuario + "\",\"password\":\"" + contrasena + "\"}";
            String respuesta = realizarSolicitud("/login", json, "POST");
            
            JsonObject jsonObj = JsonParser.parseString(respuesta).getAsJsonObject();
            
            if (jsonObj.has("token") || jsonObj.get("mensaje") != null) {
                return new Resultado(true, "Autenticacion exitosa");
            }
            return new Resultado(false, "Credenciales invalidas");
        } catch (Exception e) {
            return new Resultado(false, "Error de conexion: " + e.getMessage());
        }
    }

    public Resultado registrarDeposito(String cuenta, String importe) {
        try {
            String json = "{\"cuenta\":\"" + cuenta + "\",\"importe\":" + importe + "}";
            String respuesta = realizarSolicitud("/deposito", json, "POST");
            
            JsonObject jsonObj = JsonParser.parseString(respuesta).getAsJsonObject();
            
            if (jsonObj.has("resultado") && jsonObj.get("resultado").getAsInt() == 1) {
                return new Resultado(true, "Deposito exitoso: " + importe + " a cuenta " + cuenta);
            }
            return new Resultado(false, "Error al procesar deposito");
        } catch (Exception e) {
            return new Resultado(false, "Error de conexion: " + e.getMessage());
        }
    }

    public Resultado registrarRetiro(String cuenta, String importe) {
        try {
            String json = "{\"cuenta\":\"" + cuenta + "\",\"importe\":" + importe + "}";
            String respuesta = realizarSolicitud("/retiro", json, "POST");
            
            JsonObject jsonObj = JsonParser.parseString(respuesta).getAsJsonObject();
            
            if (jsonObj.has("resultado") && jsonObj.get("resultado").getAsInt() == 1) {
                return new Resultado(true, "Retiro exitoso: " + importe + " de cuenta " + cuenta);
            }
            return new Resultado(false, "Error al procesar retiro");
        } catch (Exception e) {
            return new Resultado(false, "Error de conexion: " + e.getMessage());
        }
    }

    public Resultado registrarTransferencia(String cuentaOrigen, String cuentaDestino, String importe) {
        try {
            String json = "{\"cuentaOrigen\":\"" + cuentaOrigen + "\",\"cuentaDestino\":\"" + cuentaDestino + "\",\"importe\":" + importe + "}";
            String respuesta = realizarSolicitud("/transferencia", json, "POST");
            
            JsonObject jsonObj = JsonParser.parseString(respuesta).getAsJsonObject();
            
            if (jsonObj.has("resultado") && jsonObj.get("resultado").getAsInt() == 1) {
                return new Resultado(true, "Transferencia exitosa: " + importe + " de " + cuentaOrigen + " a " + cuentaDestino);
            }
            return new Resultado(false, "Error al procesar transferencia");
        } catch (Exception e) {
            return new Resultado(false, "Error de conexion: " + e.getMessage());
        }
    }

    public ServicioSoap.ResultadoMovimientos obtenerMovimientos(String cuenta) {
        try {
            String respuesta = realizarSolicitud("/movimientos/" + cuenta, null, "GET");
            List<Movimiento> movimientos = new ArrayList<>();
            
            JsonObject jsonObj = JsonParser.parseString(respuesta).getAsJsonObject();
            
            if (jsonObj.has("movimientos")) {
                JsonArray arrayMovimientos = jsonObj.getAsJsonArray("movimientos");
                for (JsonElement elemento : arrayMovimientos) {
                    JsonObject movJson = elemento.getAsJsonObject();
                    Movimiento mov = new Movimiento();
                    mov.setCuenta(getValorString(movJson, "cuenta"));
                    mov.setNromov(getValorString(movJson, "nromov"));
                    mov.setFecha(getValorString(movJson, "fecha"));
                    mov.setTipo(getValorString(movJson, "tipo"));
                    mov.setAccion(getValorString(movJson, "accion"));
                    mov.setImporte(getValorString(movJson, "importe"));
                    movimientos.add(mov);
                }
            }
            
            return new ServicioSoap.ResultadoMovimientos(true, "", movimientos);
        } catch (Exception e) {
            return new ServicioSoap.ResultadoMovimientos(false, "Error al consultar movimientos: " + e.getMessage());
        }
    }

    private String getValorString(JsonObject obj, String clave) {
        if (obj.has(clave) && !obj.get(clave).isJsonNull()) {
            return obj.get(clave).getAsString();
        }
        return "";
    }
}