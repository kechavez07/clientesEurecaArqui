# Configuracion del Agente - Cliente CLI Consola Eureka Bank (Java)

## Tecnologias

- **Lenguaje**: Java 11
- **Build**: Maven
- **Consumo API**: SOAP XML y REST JSON (HttpURLConnection)
- **Interfaz**: Consola/Terminal (BufferedReader)
- **Librerias**: Jakarta XML Web Services (JAX-WS), Gson

## Estructura del Proyecto

```
0.4 CLICONSOLA/
├── src/main/java/com/eureka/consola/
│   ├── AplicacionConsola.java  # Punto de entrada (menu principal)
│   ├── config/
│   │   └── ContextoBackend.java # Gestor de backend seleccionado
│   ├── modelo/
│   │   ├── Backend.java         # Modelo backend
│   │   ├── ConfiguracionBackends.java # Lista de backends
│   │   └── Movimiento.java      # Modelo movimiento
│   └── servicio/
│       ├── ServicioSoap.java    # Logica SOAP
│       └── ServicioRest.java    # Logica REST
├── pom.xml
└── README.md
```

## Flujo de Datos

**POST (Enviar datos)**:
```
Menu -> Entrada usuario -> Servicio SOAP/REST -> Backend
```

**GET (Consultar datos)**:
```
Backend -> Respuesta -> Parseo XML/JSON -> Mostrar en consola
```

## Backends Disponibles

### 1. SOAP con Java
- **Endpoint WSDL**: `http://209.145.48.25:8091/ROOT/CoreBancarioWS`
- **Puerto**: 8091
- **Protocolo**: SOAP XML
- **Metodos**:
  - `login(usuario, password)` -> retorna Entero (1=exito)
  - `registrarDeposito(cuenta, importe)` -> retorna Entero
  - `obtenerMovimientos(cuenta)` -> retorna lista de movimientos

### 2. REST con Java
- **Base URL**: `http://209.145.48.25:8090/resources/corebancario`
- **Puerto**: 8090
- **Protocolo**: REST JSON
- **Endpoints**:
  - `POST /login` body: `{"usuario": "MONSTER", "password": "MONSTER9"}`
  - `POST /deposito` body: `{"cuenta": "00100001", "importe": 500.0}`
  - `GET /movimientos/{cuenta}`

### 3. SOAP con .NET
- **Endpoint WSDL**: `http://209.145.48.25:8092/CoreBancarioWS`
- **Puerto**: 8092
- **Protocolo**: SOAP XML
- **Metodos**:
  - `Login(usuario, password)`
  - `ObtenerMovimientos(cuenta)`

### 4. REST con .NET
- **Base URL**: `http://209.145.48.25:8093/resources/corebancario`
- **Puerto**: 8093
- **Protocolo**: REST JSON
- **Endpoints**:
  - `POST /login` body: `{"usuario":"MONSTER","password":"MONSTER9"}`
  - `POST /deposito` body: `{"cuenta":"00100001","importe":500.0}`
  - `GET /movimientos/00100001`

## Comandos Maven

```bash
mvn compile              # Compila el proyecto
mvn exec:java           # Ejecuta la aplicacion
mvn jaxws:wsimport     # Genera clientes SOAP desde WSDL
```

## Convenciones de Codigo

- **IDIOMA**: TODO el proyecto en ESPANOL
- Paquetes en minusculas: `com.eureka.consola`
- Clases con CamelCase: `AplicacionConsola`, `ServicioSoap`
- Metodos con camelCase: `iniciarSesion`, `consultarMovimientos`
- Constantes en mayusculas: `CODIGO_EMPLEADO`
- Comentarios en ESPANOL
- **NO incluir XML directo en la logica principal**