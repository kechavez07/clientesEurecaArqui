# Configuración del Agente - Proyecto Cliente SOAP Eureka

## Tecnologías

- **Frontend**: React + Vite + TypeScript
- **Consumo API**: SOAP XML (servicio web existente en GlassFish)
- **Estilos**: CSS/Tailwind (a confirmar)

## Estructura del Proyecto

```
src/
├── presentation/        # Vista / UI
│   ├── pages/           # Páginas completas (Login, MainView, Consulta, Deposito)
│   ├── components/      # Componentes reutilizables
│   ├── layouts/         # Estructuras generales
│   └── routes/         # Configuración de navegación
│
├── services/            # Consumo SOAP/API
│   └── soap/           # Lógica SOAP (construir XML, enviar POST, recibir XML)
│
├── adapters/            # Conversión de modelos
│   ├── request/        # Vista → Request SOAP (JSON → XML)
│   └── response/       # SOAP XML → Modelo Frontend (XML → objeto)
│
├── models/              # Modelos internos del frontend (TypeScript interfaces)
│
└── utils/              # Utilidades (parsers, formateadores, validadores)
```

## Flujo de Datos

**POST (Enviar datos)**:
```
Vista → Formulario → Adapter Request → SOAP Service → Backend SOAP
```

**GET (Consultar datos)**:
```
SOAP Service → XML Response → Response Adapter → Modelo Frontend → Vista
```

## Requisitos del Sistema

### 1. Login (Pantalla de autenticación)
- Campo usuario y contraseña
- Validación contra servicio SOAP
- Redirige a MainView si es exitoso

### 2. MainView (Vista Principal)
- Título: "UNIVERSIDAD DE LAS FUERZAS ARMADAS"
- Subtítulo: "PÉREZ - SANDOVAL"
- 3 botones: DEPÓSITO, CONSULTA, SALIR
- Logo/imagen de fondo institucional
- Redirige a la vista correspondiente

### 3. ConsultaView (Consulta de Movimientos)
- Campo "CUENTA" (solo números)
- Botón "CONSULTAR" → llama a `traerMovimientos(cuenta)`
- Tabla con columnas: CUENTA, NROMOV, FECHA, TIPO, ACCIÓN, IMPORTE
- Botón "SALIR" (cierra solo esta ventana)

### 4. DepositoView (Depósito)
- Campo "CUENTA" (solo números)
- Campo "IMPORTE" (números decimales, ej: 100.50)
- Botón "PROCESAR" → llama a `registrarDeposito(cuenta, importe, codEmp)`
- Mensaje éxito: "Proceso completado, se hizo un depósito de [importe] a la cuenta [cuenta]"
- Botón "SALIR" (cierra solo esta ventana)
- Código empleado fijo: "0001"

## Validaciones de Campos

- **CUENTA**: solo caracteres numéricos
- **IMPORTE**: números decimales (un solo punto)
- Implementar en tiempo real (keypress/input)

## Servicios SOAP

- **WSDL**: `http://localhost:8080/WS_Eureka/WSEureka`
- **Métodos**:
  - `traerMovimientos(cuenta)` → devuelve lista de movimientos
  - `registrarDeposito(cuenta, importe, codEmp)` → devuelve entero (1=OK, -1=error)

## Convenciones de Código

- **IDIOMA**: TODO el proyecto debe estar en ESPAÑOL. Sin excepciones.
  - Variables en español (ej: `numeroCuenta`, `montoDeposito`)
  - Funciones en español (ej: `consultarMovimientos`, `procesarDeposito`)
  - Constantes en español (ej: `CODIGO_EMPLEADO`)
  - Interfaces/tipos en español (ej: `Movimiento`, `Deposito`)
  - Comentarios en español
  - Textos de UI en español
  - Nombres de archivos en español (o con prefijos en español)
- TypeScript interfaces para todos los modelos
- Prefijos: `soap*` para servicios, `*Adapter` para adaptadores
- NO incluir XML directo en componentes UI

## Backends Disponibles

El sistema soporta 4 backends diferentes que se pueden seleccionar desde el Login:

### 1. SOAP con Java
- **Endpoint WSDL**: `http://209.145.48.25:8091/ROOT/CoreBancarioWS`
- **Puerto**: 8091
- **Protocolo**: SOAP XML
- **Métodos**:
  - `login(usuario, password)` → retorna Entero (1=éxito)
  - `registrarDeposito(cuenta, importe)` → retorna Entero
  - `obtenerMovimientos(cuenta)` → retorna lista de movimientos

### 2. REST con Java
- **Base URL**: `http://209.145.48.25:8090/resources/corebancario`
- **Puerto**: 8090
- **Protocolo**: REST JSON
- **Endpoints**:
  - `POST /login` body: `{"usuario": "MONSTER", "password": "MONSTER9"}`
  - `POST /deposito` body: `{"cuenta": "00100001", "importe": 500.0}`
  - `GET /movimientos/{cuenta}`
  - `GET /ping`

### 3. SOAP con .NET
- **Endpoint WSDL**: `http://209.145.48.25:8092/CoreBancarioWS`
- **Puerto**: 8092
- **Protocolo**: SOAP XML
- **Métodos**:
  - `Ping()`
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
  - `GET /ping`

## Sistema de Selección de Backend

- El Login debe incluir un **selector/dropdown** para elegir el backend
- El título del Login debe cambiar dinámicamente según la selección:
  - SOAP Java → "EUREKA BANK - SOAP Java"
  - REST Java → "EUREKA BANK - REST Java"
  - SOAP .NET → "EUREKA BANK - SOAP .NET"
  - REST .NET → "EUREKA BANK - REST .NET"
- El contexto global debe mantener el backend seleccionado durante toda la sesión

## Estructura de Archivos

```
src/
├── config/
│   └── backendConfig.ts      # Configuración de los 4 backends
├── context/
│   └── BackendContext.tsx    # Contexto global para backend seleccionado
├── services/
│   ├── soap/
│   │   └── soapService.ts    # Lógica SOAP (reutilizable para Java y .NET)
│   └── rest/
│       └── restService.ts    # Lógica REST (reutilizable para Java y .NET)
└── ...
```

## Limitaciones

- Solo frontend (backends ya existen)
- El servidor backend debe estar desplegado antes de ejecutar el cliente
- La selección de backend se mantiene durante toda la sesión