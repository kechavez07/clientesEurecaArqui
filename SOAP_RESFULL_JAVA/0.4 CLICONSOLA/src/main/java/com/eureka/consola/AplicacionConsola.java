package com.eureka.consola;

import com.eureka.consola.config.ContextoBackend;
import com.eureka.consola.modelo.Backend;
import com.eureka.consola.modelo.ConfiguracionBackends;
import com.eureka.consola.modelo.Movimiento;
import com.eureka.consola.modelo.Resultado;
import com.eureka.consola.servicio.rest.ServicioRest;
import com.eureka.consola.servicio.soap.ServicioSoap;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class AplicacionConsola {
    
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static final ServicioSoap servicioSoap = new ServicioSoap();
    private static final ServicioRest servicioRest = new ServicioRest();

    public static void main(String[] args) {
        try {
            iniciarAplicacion();
        } catch (Exception e) {
            System.out.println("Error fatal: " + e.getMessage());
        }
    }

    private static String leerLinea() throws Exception {
        String linea = reader.readLine();
        return linea != null ? linea : "";
    }

    private static void pausar() throws Exception {
        System.out.print("Presione ENTER para continuar...");
        reader.readLine();
    }

    private static void limpiarPantalla() {
        for (int i = 0; i < 50; i++) System.out.println();
    }

    private static void mostrarTitulo() {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║             UNIVERSIDAD DE LAS FUERZAS ARMADAS           ║");
        System.out.println("║                  PEREZ - SANDOVAL                         ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private static void mostrarMenuBackend() throws Exception {
        limpiarPantalla();
        mostrarTitulo();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         SELECCIONAR BACKEND           ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  1. SOAP Java                          ║");
        System.out.println("║  2. REST Java                          ║");
        System.out.println("║  3. SOAP .NET                          ║");
        System.out.println("║  4. REST .NET                          ║");
        System.out.println("║  0. Salir                              ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
    }

    private static void seleccionarBackend(int opcion) {
        List<Backend> backends = ConfiguracionBackends.BACKENDS;
        if (opcion >= 1 && opcion <= backends.size()) {
            ContextoBackend.cambiarBackend(backends.get(opcion - 1).getId());
            System.out.println("Backend seleccionado: " + backends.get(opcion - 1).getNombre());
        }
    }

    private static Resultado autenticar() throws Exception {
        System.out.println("--- INICIO DE SESION ---");
        System.out.println();

        System.out.print("Usuario: ");
        String usuario = reader.readLine();
        System.out.print("Contrasena: ");
        String contrasena = reader.readLine();

        Backend backend = ContextoBackend.obtenerBackendActual();

        if ("SOAP".equals(backend.getProtocolo())) {
            return servicioSoap.autenticar(usuario, contrasena);
        } else {
            return servicioRest.autenticar(usuario, contrasena);
        }
    }

    private static void consultarMovimientos() throws Exception {
        limpiarPantalla();
        mostrarTitulo();
        System.out.println("--- CONSULTA DE MOVIMIENTOS ---");
        System.out.println();

        System.out.print("Ingrese numero de cuenta: ");
        String cuenta = reader.readLine();

        Backend backend = ContextoBackend.obtenerBackendActual();
        ServicioSoap.ResultadoMovimientos resultado;

        if ("SOAP".equals(backend.getProtocolo())) {
            resultado = servicioSoap.obtenerMovimientos(cuenta);
        } else {
            resultado = servicioRest.obtenerMovimientos(cuenta);
        }

        System.out.println();

        if (resultado.isExito() && !resultado.getMovimientos().isEmpty()) {
            System.out.println("+----------+--------+------------------+--------+--------+---------+");
            System.out.println("|  CUENTA  | NROMOV |      FECHA       |  TIPO  | ACCION | IMPORTE |");
            System.out.println("+----------+--------+------------------+--------+--------+---------+");

            for (Movimiento mov : resultado.getMovimientos()) {
                System.out.printf("| %-8s | %-6s | %-16s | %-6s | %-6s | %-7s |%n",
                    mov.getCuenta(), mov.getNromov(), mov.getFecha(), 
                    mov.getTipo(), mov.getAccion(), mov.getImporte());
            }

            System.out.println("+----------+--------+------------------+--------+--------+---------+");
        } else {
            System.out.println("No se encontraron movimientos para esta cuenta.");
        }

        System.out.println();
        pausar();
    }

    private static void realizarDeposito() throws Exception {
        limpiarPantalla();
        mostrarTitulo();
        System.out.println("--- DEPOSITO ---");
        System.out.println();

        System.out.print("Ingrese numero de cuenta: ");
        String cuenta = reader.readLine();
        System.out.print("Ingrese importe: ");
        String importe = reader.readLine();

        Backend backend = ContextoBackend.obtenerBackendActual();
        Resultado resultado;

        if ("SOAP".equals(backend.getProtocolo())) {
            resultado = servicioSoap.registrarDeposito(cuenta, importe);
        } else {
            resultado = servicioRest.registrarDeposito(cuenta, importe);
        }

        System.out.println();
        System.out.println(resultado.getMensaje());
        System.out.println();
        pausar();
    }

    private static void realizarRetiro() throws Exception {
        limpiarPantalla();
        mostrarTitulo();
        System.out.println("--- RETIRO ---");
        System.out.println();

        System.out.print("Ingrese numero de cuenta: ");
        String cuenta = reader.readLine();
        System.out.print("Ingrese importe: ");
        String importe = reader.readLine();

        Backend backend = ContextoBackend.obtenerBackendActual();
        Resultado resultado;

        if ("SOAP".equals(backend.getProtocolo())) {
            resultado = servicioSoap.registrarRetiro(cuenta, importe);
        } else {
            resultado = servicioRest.registrarRetiro(cuenta, importe);
        }

        System.out.println();
        System.out.println(resultado.getMensaje());
        System.out.println();
        pausar();
    }

    private static void realizarTransferencia() throws Exception {
        limpiarPantalla();
        mostrarTitulo();
        System.out.println("--- TRANSFERENCIA ---");
        System.out.println();

        System.out.print("Cuenta origen: ");
        String cuentaOrigen = reader.readLine();
        System.out.print("Cuenta destino: ");
        String cuentaDestino = reader.readLine();
        System.out.print("Importe: ");
        String importe = reader.readLine();

        Backend backend = ContextoBackend.obtenerBackendActual();
        Resultado resultado;

        if ("SOAP".equals(backend.getProtocolo())) {
            resultado = servicioSoap.registrarTransferencia(cuentaOrigen, cuentaDestino, importe);
        } else {
            resultado = servicioRest.registrarTransferencia(cuentaOrigen, cuentaDestino, importe);
        }

        System.out.println();
        System.out.println(resultado.getMensaje());
        System.out.println();
        pausar();
    }

    private static void mostrarMenuPrincipal() throws Exception {
        Backend backend = ContextoBackend.obtenerBackendActual();
        System.out.println("Backend activo: " + backend.getNombre() + " (" + backend.getProtocolo() + " - " + backend.getTecnologia() + ")");
        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         MENU PRINCIPAL                 ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  1. Cambiar Backend                   ║");
        System.out.println("║  2. Deposito                          ║");
        System.out.println("║  3. Retiro                            ║");
        System.out.println("║  4. Transferencia                    ║");
        System.out.println("║  5. Consultar Movimientos            ║");
        System.out.println("║  0. Salir                             ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
    }

    private static void iniciarAplicacion() throws Exception {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║               EUREKA BANK - CLIENTE CONSOLA             ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();

        boolean autenticado = false;
        while (!autenticado) {
            mostrarMenuBackend();
            System.out.print("Seleccione backend (1-4): ");
            String opcionBackend = reader.readLine();
            
            int opcion = 0;
            try {
                opcion = Integer.parseInt(opcionBackend.trim());
            } catch (NumberFormatException e) {
                opcion = 0;
            }

            if (opcion == 0) {
                System.out.println("Saliendo...");
                return;
            }

            seleccionarBackend(opcion);
            System.out.println();
            
            Resultado resultado = autenticar();
            System.out.println();
            System.out.println(resultado.getMensaje());

            if (resultado.isExito()) {
                autenticado = true;
            } else {
                System.out.println("Intente de nuevo...");
                pausar();
            }
        }

        while (true) {
            limpiarPantalla();
            mostrarTitulo();
            mostrarMenuPrincipal();
            System.out.print("Seleccione una opcion: ");
            String opcion = reader.readLine();

            switch (opcion) {
                case "1":
                    mostrarMenuBackend();
                    System.out.print("Seleccione backend (1-4): ");
                    String opcionBackend = reader.readLine();
                    try {
                        seleccionarBackend(Integer.parseInt(opcionBackend.trim()));
                    } catch (Exception e) {
                        System.out.println("Opcion invalida");
                    }
                    pausar();
                    break;
                case "2":
                    realizarDeposito();
                    break;
                case "3":
                    realizarRetiro();
                    break;
                case "4":
                    realizarTransferencia();
                    break;
                case "5":
                    consultarMovimientos();
                    break;
                case "0":
                    System.out.println("Gracias por usar Eureka Bank!");
                    return;
                default:
                    System.out.println("Opcion invalida.");
                    pausar();
            }
        }
    }
}