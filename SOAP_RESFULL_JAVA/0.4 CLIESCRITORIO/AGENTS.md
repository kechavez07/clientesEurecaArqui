# Configuración del Agente - Cliente Escritorio Java Maven Eureka 2026

## Objetivo del Proyecto

Desarrollar un cliente bancario de escritorio en Java con una interfaz moderna, profesional y visualmente atractiva, orientada a estándares de diseño contemporáneos del año 2026.

La aplicación deberá consumir servicios SOAP XML y REST JSON existentes, permitiendo realizar autenticación, consultas de movimientos y depósitos bancarios.

El sistema debe priorizar:

- Experiencia visual moderna con JavaFX
- Arquitectura limpia en capas
- Separación de responsabilidades
- Escalabilidad y mantenibilidad
- Manejo profesional de erroresrevisa
- Compatibilidad con múltiples backends
- Código desacoplado y testeable

---

# Tecnologías

## Base del Proyecto

- **Java**: 21 LTS
- **Build Tool**: Maven 3.8.1+
- **Tipo**: Aplicación Desktop con GUI
- **IDE**: NetBeans / IntelliJ IDEA / VS Code

---

## Interfaz Gráfica

### Framework UI

- **JavaFX 21**: Framework moderno para interfaces de escritorio
- **FXML**: Lenguaje XML para definición de interfaces
- **CSS**: Estilos para componentes JavaFX

### Diseño Visual

- Diseño moderno tipo fintech 2026
- Interfaz minimalista y limpia
- Componentes reutilizables
- Transiciones suaves con efectos
- Bordes redondeados y sombras
- Paleta oscura profesional
- Responsive dentro de escritorio
- Tipografía moderna y legible

### Paleta de Colores

```
Fondo principal:      #0F172A (azul oscuro)
Tarjetas/Paneles:     #1E293B (azul grisáceo)
Botones primarios:    #2563EB (azul brillante)
Botones secundarios:  #64748B (gris)
Texto principal:      #FFFFFF (blanco)
Texto secundario:     #94A3B8 (gris claro)
Énfasis/Links:        #60A5FA (azul claro)
Errores:              #EF4444 (rojo)
Éxito:                #22C55E (verde)
Advertencias:         #F59E0B (naranja)
```

---

## Consumo de APIs

### SOAP XML

- `HttpURLConnection` para conexiones
- Parsing manual de XML
- Generación de requests SOAP envelopados
- Manejo de SOAP Faults

### REST JSON

- `HttpURLConnection` para conexiones
- Gson 2.10.1 para serialización/deserialización
- Manejo de códigos HTTP
- Validación de respuestas

---

# Arquitectura del Sistema

Arquitectura limpia en capas con separación clara de responsabilidades:

```
┌─────────────────────────────────────────┐
│    Presentación (JavaFX UI)             │
│  - Vistas FXML                          │
│  - Controladores                        │
│  - Componentes reutilizables            │
└─────────────────────────────────────────┘
         ↓ (Binding/Events)
┌─────────────────────────────────────────┐
│    Capa de Aplicación                   │
│  - Controladores de flujo                │
│  - Gestión de estado                     │
│  - Orquestación de servicios             │
└─────────────────────────────────────────┘
         ↓ (Llamadas)
┌─────────────────────────────────────────┐
│    Capa de Servicios                    │
│  - ServicioBancario (interfaz)          │
│  - Implementaciones SOAP/REST            │
│  - Lógica de negocio                    │
└─────────────────────────────────────────┘
         ↓ (Adaptación)
┌─────────────────────────────────────────┐
│    Capa de Adaptadores                  │
│  - Conversión request/response           │
│  - Transformación de datos              │
│  - Parseo XML/JSON                      │
└─────────────────────────────────────────┘
         ↓ (HTTP)
┌─────────────────────────────────────────┐
│    Backends (SOAP/REST)                 │
│  - Java SOAP                            │
│  - Java REST                            │
│  - .NET SOAP                            │
│  - .NET REST                            │
└─────────────────────────────────────────┘
```

---

# Estructura del Proyecto

```plaintext
src/
├── main/
│
│   ├── java/
│   │
│   │   └── ec/
│   │       └── edu/
│   │           └── espe/
│   │               └── eurekadesktop/
│   │
│   │                   ├── app/
│   │                   │   └── Main.java
│   │                   │
│   │                   ├── config/
│   │                   │   ├── ConfiguracionBackend.java
│   │                   │   └── ConfiguracionUI.java
│   │                   │
│   │                   ├── context/
│   │                   │   └── ContextoBackend.java
│   │                   │
│   │                   ├── controllers/
│   │                   │   ├── LoginController.java
│   │                   │   ├── MainController.java
│   │                   │   ├── ConsultaController.java
│   │                   │   └── DepositoController.java
│   │                   │
│   │                   ├── views/
│   │                   │   ├── LoginView.fxml
│   │                   │   ├── MainView.fxml
│   │                   │   ├── ConsultaView.fxml
│   │                   │   └── DepositoView.fxml
│   │                   │
│   │                   ├── services/
│   │                   │
│   │                   │   ├── interfaces/
│   │                   │   │   └── ServicioBancario.java
│   │                   │   │
│   │                   │   ├── soap/
│   │                   │   │   ├── ServicioSoapJava.java
│   │                   │   │   └── ServicioSoapDotNet.java
│   │                   │   │
│   │                   │   └── rest/
│   │                   │       ├── ServicioRestJava.java
│   │                   │       └── ServicioRestDotNet.java
│   │                   │
│   │                   ├── adapters/
│   │                   │
│   │                   │   ├── request/
│   │                   │   │   └── AdaptadorRequestSoap.java
│   │                   │   │
│   │                   │   └── response/
│   │                   │       └── AdaptadorResponseSoap.java
│   │                   │
│   │                   ├── factory/
│   │                   │   └── ServicioFactory.java
│   │                   │
│   │                   ├── models/
│   │                   │   ├── Movimiento.java
│   │                   │   ├── Deposito.java
│   │                   │   ├── Usuario.java
│   │                   │   └── Backend.java
│   │                   │
│   │                   ├── utils/
│   │                   │   ├── XmlUtils.java
│   │                   │   ├── JsonUtils.java
│   │                   │   ├── Alertas.java
│   │                   │   ├── Validaciones.java
│   │                   │   └── ConsolaDebug.java
│   │                   │
│   │                   └── styles/
│   │                       └── app.css
│   │
│   └── resources/
│       ├── application.properties
│       ├── images/
│       └── styles/
│
└── test/
```

---

# Diseño Visual 2026

## Estilo General

La interfaz debe inspirarse en aplicaciones fintech modernas.

### Características visuales

- Fondo oscuro elegante
- Efecto glassmorphism
- Tarjetas flotantes
- Animaciones suaves
- Botones modernos
- Inputs estilizados
- Bordes redondeados
- Sombras suaves
- Colores profesionales

---

## Paleta Recomendada

```plaintext
Fondo principal: #0F172A
Tarjetas: #1E293B
Botones: #2563EB
Texto principal: #FFFFFF
Texto secundario: #94A3B8
Errores: #EF4444
Éxito: #22C55E
```

---

# Requisitos Funcionales

## Login

- Usuario
- Contraseña
- Selector de backend

### Backends disponibles

1. SOAP Java
2. REST Java
3. SOAP .NET
4. REST .NET

---

## MainView

Mostrar:

- DEPÓSITO
- CONSULTA
- SALIR

---

## ConsultaView

- Campo cuenta
- Tabla de movimientos
- Botón consultar
- Botón regresar

---

## DepositoView

- Campo cuenta
- Campo importe
- Botón procesar
- Mensajes visuales

---

# Manejo Profesional de Errores

## Objetivo

El usuario debe recibir mensajes simples y amigables.

La consola técnica debe mostrar información detallada para debugging.

---

# Reglas de Manejo de Errores

## 1. Backend no responde

### UI

Mostrar:

```plaintext
No se pudo conectar con el servidor.
Intente nuevamente más tarde.
```

### Consola técnica

Mostrar:

```plaintext
[ERROR CONEXION]

Backend: SOAP Java
Endpoint: http://209.145.48.25:8091/ROOT/CoreBancarioWS

Detalle:
Connection timed out

Posible causa:
- Backend apagado
- Problema de red
- Puerto inaccesible
```

---

## 2. SOAP devuelve Fault

### UI

Mostrar tabla vacía o pantalla vacía.

Mostrar mensaje:

```plaintext
No se pudieron obtener los datos.
```

### Consola técnica

Mostrar:

```plaintext
[SOAP FAULT]

Fault Code:
soap:Server

Fault String:
Error interno del servicio SOAP

Detalle XML:
<soap:Fault>...</soap:Fault>
```

---

## 3. REST devuelve HTTP 500

### UI

Mostrar:

```plaintext
Ocurrió un error al procesar la solicitud.
```

### Consola técnica

Mostrar:

```plaintext
[ERROR HTTP]

HTTP CODE: 500

Endpoint:
http://209.145.48.25:8090/resources/corebancario

Detalle:
Internal Server Error

Posible causa:
- Error interno del backend
- Error en base de datos
- Endpoint incorrecto
```

---

## 4. XML vacío

### UI

Mostrar:

```plaintext
No existen datos disponibles.
```

### Consola técnica

Mostrar:

```plaintext
[XML VACIO]

La respuesta SOAP llegó vacía.

Posibles causas:
- Error del backend
- Respuesta mal formada
- Problema de serialización XML
```

---

## 5. Cuenta no existe

### UI

Mostrar:

```plaintext
La cuenta ingresada no existe.
```

Mostrar cuentas disponibles:

```plaintext
Cuentas disponibles:

00100001
00100002
00100003
```

### Consola técnica

Mostrar:

```plaintext
[CUENTA NO ENCONTRADA]

Cuenta enviada:
00999999

El backend no encontró coincidencias.
```

---

# Reglas Técnicas

## Código

- TODO en español
- Sin lógica de backend en vistas
- Sin XML dentro de controladores
- Sin código duplicado
- Validaciones centralizadas

---

## Servicios

Todos los servicios deben implementar:

```java
ServicioBancario
```

---

## Navegación

- Navegación mediante escenas JavaFX
- No abrir múltiples ventanas innecesarias
- Mantener sesión activa
- Regresar al menú principal después de operaciones

---

# Validaciones

## Cuenta

- Solo números
- Longitud válida

## Importe

- Decimal válido
- Mayor a cero

## Login

- Campos obligatorios

---

# Configuración Centralizada

## application.properties

```properties
soap.java.url=http://209.145.48.25:8091/ROOT/CoreBancarioWS
rest.java.url=http://209.145.48.25:8090/resources/corebancario

soap.dotnet.url=http://209.145.48.25:8092/CoreBancarioWS
rest.dotnet.url=http://209.145.48.25:8093/resources/corebancario

timeout.connection=5000
timeout.read=5000
```

---

# Factory Pattern

El backend será seleccionado mediante:

```plaintext
ServicioFactory
```

Esto permitirá cambiar entre SOAP y REST dinámicamente.

---

# Dependencias Maven

```xml
<project>
    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <javafx.version>21</javafx.version>
        <gson.version>2.10.1</gson.version>
        <slf4j.version>2.0.9</slf4j.version>
        <logback.version>1.4.11</logback.version>
        <junit.version>5.9.3</junit.version>
        <mockito.version>5.3.1</mockito.version>
    </properties>

    <dependencies>
        <!-- JavaFX -->
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>${javafx.version}</version>
        </dependency>

        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-fxml</artifactId>
            <version>${javafx.version}</version>
        </dependency>

        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-graphics</artifactId>
            <version>${javafx.version}</version>
        </dependency>

        <!-- Gson para JSON -->
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>${gson.version}</version>
        </dependency>

        <!-- Logging: SLF4J + Logback -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${slf4j.version}</version>
        </dependency>

        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>${logback.version}</version>
        </dependency>

        <!-- Testing: JUnit 5 + Mockito -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <version>${mockito.version}</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-junit-jupiter</artifactId>
            <version>${mockito.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- Compilador Java 21 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                </configuration>
            </plugin>

            <!-- JavaFX Maven Plugin -->
            <plugin>
                <groupId>org.openjfx</groupId>
                <artifactId>javafx-maven-plugin</artifactId>
                <version>0.0.8</version>
                <configuration>
                    <mainClass>ec.edu.espe.eurekadesktop.app.Main</mainClass>
                </configuration>
            </plugin>

            <!-- Maven Surefire Plugin para pruebas -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.0.0</version>
            </plugin>

            <!-- JAR Plugin para crear JAR ejecutable -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>3.6.0</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>ec.edu.espe.eurekadesktop.app.Main</mainClass>
                        </manifest>
                    </archive>
                    <descriptorRefs>
                        <descriptorRef>jar-with-dependencies</descriptorRef>
                    </descriptorRefs>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

---

# Ejecución del Proyecto

```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar la aplicación
mvn javafx:run

# Ejecutar pruebas
mvn test

# Generar JAR ejecutable
mvn clean package assembly:single
```

---

# Patrones de Código JavaFX

## Controlador FXML

Los controladores deben:

- Inyectar vistas FXML con `@FXML`
- NO contener lógica de negocio
- Delegar al servicio/capa de aplicación
- Actualizar UI desde el hilo de JavaFX

### Ejemplo de Controlador:

```java
public class LoginController {
  
    @FXML
    private TextField usuarioField;
  
    @FXML
    private PasswordField contraseñaField;
  
    @FXML
    private ComboBox<String> backendCombo;
  
    private ServicioFactory servicioFactory;
  
    @FXML
    public void initialize() {
        // Configuración inicial
        backendCombo.setItems(FXCollections.observableArrayList(
            "SOAP Java", "REST Java", "SOAP .NET", "REST .NET"
        ));
    }
  
    @FXML
    private void manejarLogin() {
        String usuario = usuarioField.getText();
        String contraseña = contraseñaField.getText();
        String backend = backendCombo.getValue();
      
        // Delegar al servicio
        // NO hacer aquí la llamada HTTP
    }
}
```

## Carga de Vistas FXML

Las vistas FXML deben cargarse desde el controlador principal:

```java
FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
Parent root = loader.load();
Scene scene = new Scene(root);
stage.setScene(scene);
stage.show();
```

## Binding de Propiedades

Usar propiedades observables para sincronizar datos:

```java
private final StringProperty usuario = new SimpleStringProperty();
private final ObjectProperty<BigDecimal> saldo = new SimpleObjectProperty<>();

public StringProperty usuarioProperty() {
    return usuario;
}

// En FXML
<Label text="${controller.usuario}"/>
```

## Threading en JavaFX

Para operaciones largas (HTTP, etc), usar Task:

```java
Task<ResultadoLogin> task = new Task<ResultadoLogin>() {
    @Override
    protected ResultadoLogin call() throws Exception {
        return servicio.autenticar(usuario, contraseña);
    }
};

task.setOnSucceeded(e -> {
    ResultadoLogin resultado = task.getValue();
    // Actualizar UI
});

task.setOnFailed(e -> {
    Throwable excepcion = task.getException();
    // Mostrar error
});

new Thread(task).start();
```

## Animaciones en JavaFX

```java
FadeTransition fade = new FadeTransition(Duration.millis(500), nodo);
fade.setFromValue(0);
fade.setToValue(1);
fade.play();

ScaleTransition scale = new ScaleTransition(Duration.millis(300), nodo);
scale.setFromX(0.8);
scale.setToX(1);
scale.play();
```

---

# Directrices de Uso de JavaFX

## 1. Organización de Vistas

- Una vista FXML por pantalla
- Ubicadas en: `src/main/resources/ec/edu/espe/eurekadesktop/views/`
- Nombres claros: `LoginView.fxml`, `MainView.fxml`, etc.

## 2. Controladores

- Un controlador por cada vista FXML
- Ubicados en: `src/main/java/.../controllers/`
- Nombre coincide con FXML: `LoginController` para `LoginView.fxml`

## 3. Estilos CSS

- CSS centralizado en: `src/main/resources/styles/app.css`
- Aplicar estilos por clase CSS en FXML
- NO usar estilos inline

### Ejemplo CSS:

```css
.root {
    -fx-font-family: "Segoe UI", "Helvetica Neue", sans-serif;
    -fx-font-size: 12px;
}

.principal-button {
    -fx-padding: 10px 20px;
    -fx-background-color: #2563EB;
    -fx-text-fill: #FFFFFF;
    -fx-font-size: 14px;
    -fx-border-radius: 8px;
    -fx-background-radius: 8px;
}

.tarjeta {
    -fx-background-color: #1E293B;
    -fx-border-radius: 12px;
    -fx-padding: 16px;
    -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 4);
}
```

## 4. Cambio de Vistas

Usar un manejador centralizado:

```java
public class SceneManager {
    private static Scene escenaActual;
  
    public static void cambiarEscena(String archivoFXML) throws IOException {
        FXMLLoader loader = new FXMLLoader(
            SceneManager.class.getResource("/views/" + archivoFXML)
        );
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) escenaActual.getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
```

## 5. Componentes Reutilizables

Crear componentes personalizados para patrones comunes:

```java
public class TarjetaMovimiento extends VBox {
    private final Label fechaLabel;
    private final Label conceptoLabel;
    private final Label montoLabel;
  
    public TarjetaMovimiento(Movimiento movimiento) {
        // Inicializar componentes
        getStyleClass().add("tarjeta");
    }
}
```

---

# Estructura de Vistas FXML

## LoginView.fxml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.geometry.Insets?>
<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>

<BorderPane xmlns="http://javafx.com/javafx/21" 
            xmlns:fx="http://javafx.com/fxml/1"
            fx:controller="ec.edu.espe.eurekadesktop.controllers.LoginController"
            styleClass="root">
  
    <center>
        <VBox alignment="CENTER" spacing="20" styleClass="tarjeta">
            <HBox.setHgrow value="ALWAYS" />
            <VBox.setVgrow value="ALWAYS" />
          
            <TextField fx:id="usuarioField" promptText="Usuario" />
            <PasswordField fx:id="contraseñaField" promptText="Contraseña" />
            <ComboBox fx:id="backendCombo" promptText="Seleccionar Backend" />
          
            <Button text="Ingresar" onAction="#manejarLogin" 
                    styleClass="principal-button" />
        </VBox>
    </center>
  
    <stylesheets>
        <URL value="@/styles/app.css" />
    </stylesheets>
</BorderPane>
```

---

# Objetivo Final del Sistema

El cliente debe sentirse como una aplicación bancaria real moderna:

- Profesional
- Fluida
- Elegante
- Escalable
- Fácil de mantener
- Compatible con SOAP y REST
- Visualmente alineada con aplicaciones fintech modernas del 2026

---

# Prácticas Recomendadas para Desarrollo

## 1. No Hacer en Controladores

- ❌ Llamadas HTTP directo
- ❌ Parsing de XML/JSON
- ❌ Lógica de negocio
- ❌ Acceso a base de datos
- ❌ Validaciones complejas

## 2. Responsabilidades del Controlador

- ✅ Obtener valores de componentes UI
- ✅ Validar entrada del usuario
- ✅ Delegar al servicio
- ✅ Actualizar UI con resultados
- ✅ Mostrar errores amigables

## 3. Estructura de Paquetes

```
ec.edu.espe.eurekadesktop
├── app                    # Entrada de la aplicación
├── context                # Contexto global de la app
├── controllers            # Controladores de vistas
├── models                 # Modelos de datos
├── services
│   ├── interfaces         # Contratos de servicios
│   ├── soap              # Implementaciones SOAP
│   └── rest              # Implementaciones REST
├── adapters
│   ├── request           # Adaptadores de request
│   └── response          # Adaptadores de response
├── factory               # Patrón Factory
├── utils                 # Utilidades
│   ├── XmlUtils
│   ├── JsonUtils
│   ├── Validaciones
│   ├── Alertas
│   └── ConsolaDebug
└── config                # Configuración centralizada
```

## 4. Ciclo de Vida JavaFX

- `initialize()` - Se ejecuta después de que FXML se carga
- `@FXML` - Se inyectan solo los componentes con este anotador
- `onAction` - Se ejecuta en el hilo de JavaFX

## 5. Manejo de Errores en JavaFX

```java
try {
    // Operación de UI
} catch (Exception e) {
    LOGGER.error("Error en UI", e);
    Alertas.mostrarError("Ocurrió un error", e.getMessage());
}
```

## 6. Performance en JavaFX

- Usar `Task` para operaciones largas
- NO bloquear el hilo de JavaFX
- Usar `Platform.runLater()` para actualizar UI desde otro hilo
- Limitar actualizaciones frecuentes de UI

---

# Logging y Debugging

## Configuración de Logback

Archivo `logback.xml` en `src/main/resources/`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
  
    <appender name="FILE" class="ch.qos.logback.core.FileAppender">
        <file>logs/app.log</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
  
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="FILE" />
    </root>
  
    <logger name="ec.edu.espe.eurekadesktop" level="DEBUG" />
</configuration>
```

## Uso de Logger en Código

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiClase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MiClase.class);
  
    public void miMetodo() {
        LOGGER.debug("Iniciando operación");
        LOGGER.info("Operación completada");
        LOGGER.warn("Advertencia");
        LOGGER.error("Error ocurrido", new Exception());
    }
}
```

---

# Testing en JavaFX

## Test de Controladores

```java
@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {
  
    @Mock
    private ServicioBancario servicio;
  
    private LoginController controller;
  
    @BeforeEach
    public void setUp() {
        controller = new LoginController();
        controller.setServicio(servicio);
    }
  
    @Test
    public void testLoginExitoso() {
        // Arrange
        when(servicio.autenticar("usuario", "password"))
            .thenReturn(new Usuario("usuario", "token"));
      
        // Act
        controller.manejarLogin();
      
        // Assert
        verify(servicio).autenticar("usuario", "password");
    }
}
```

---

# Configuración del Entorno

## Variables de Entorno

Para desarrollo local:

```bash
# Windows
set JAVA_HOME=C:\Program Files\Java\jdk-21
set JAVAFX_HOME=C:\javafx-sdk-21

# Linux/Mac
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
export JAVAFX_HOME=/path/to/javafx-sdk-21
```

## IDE: Configuración en NetBeans

1. Tools → Options → Java → Platform
2. Seleccionar JDK 21
3. Run → Run Project → Seleccionar main class

## IDE: Configuración en IntelliJ IDEA

1. File → Project Structure → Project
2. SDK: OpenJDK 21
3. Language Level: 21
4. Añadir JavaFX library

---

# Integración Continua (Opcional)

## GitHub Actions para Maven

`.github/workflows/build.yml`:

```yaml
name: Build

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
  
    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'
    - name: Build with Maven
      run: mvn -B package --file pom.xml
```

---

# Convenciones de Código

## Nomenclatura

- **Clases**: `LoginController`, `ServicioBancario`, `TarjetaMovimiento`
- **Métodos**: `manejarLogin()`, `obtenerMovimientos()`, `procesarDeposito()`
- **Variables**: `usuarioField`, `saldoActual`, `codigoRespuesta`
- **Constantes**: `URL_SOAP_JAVA`, `TIMEOUT_CONEXION`, `PALETA_COLOR_PRINCIPAL`

## Formato de Código

- Indentación: 4 espacios
- Máximo 120 caracteres por línea
- Comentarios en español
- TODO, FIXME en español

## Documentación (JavaDoc)

```java
/**
 * Autentica un usuario en el sistema bancario.
 * 
 * @param usuario Nombre de usuario
 * @param contraseña Contraseña del usuario
 * @param backend Backend a utilizar (SOAP_JAVA, REST_JAVA, etc.)
 * @return Usuario autenticado con token
 * @throws ErrorAutenticacionException Si la autenticación falla
 */
public Usuario autenticar(String usuario, String contraseña, String backend) 
    throws ErrorAutenticacionException {
    // Implementación
}
```

---

# Troubleshooting Común

## Problema: "Cannot find symbol javafx"

**Solución**: Asegurarse que JavaFX está en el pom.xml y que Maven descargó las dependencias:

```bash
mvn clean install
```

## Problema: "Module javafx.fxml not found"

**Solución**: Agregar módulos a la JVM:

En IntelliJ IDEA: Run → Edit Configurations → VM options:

```
--module-path /path/to/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml
```

## Problema: Controlador no se carga

**Solución**: Verificar que:

1. La clase controlador existe
2. Tiene el anotador `@FXML` correcto
3. El atributo `fx:controller` en FXML es correcto
4. El archivo FXML está en el classpath

---

# Checklist para Completar el Proyecto

- [ ] Proyecto Maven creado con estructura correcta
- [ ] Dependencias en pom.xml descargadas
- [ ] Vistas FXML creadas (Login, Main, Consulta, Deposito)
- [ ] Controladores implementados sin lógica de negocio
- [ ] CSS aplicado y validado visualmente
- [ ] Servicios SOAP/REST implementados
- [ ] Factory Pattern configurado
- [ ] Manejo de errores implementado
- [ ] Logging funcionando
- [ ] Pruebas unitarias creadas
- [ ] Aplicación ejecutable con `mvn javafx:run`
- [ ] JAR ejecutable generado

---
