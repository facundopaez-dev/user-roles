# Instructivo

### Instalación y configuración del stack tecnológico

Estos ejemplos asumen que está instalado `Java SE` 7 o superior y `Apache Ant`. Verificar corriendo los comandos `java -version` y `ant -version`.

`Java SE` puede descargarse de http://java.sun.com/javase/downloads/index.jsp.

`Apache Ant` está disponible para la descarga en http://ant.apache.org.

1. Sin necesidad de hacer fork, clonar el repositorio ejemplos. Utilizaremos `<EXAMPLES_HOME>` para referirnos al directorio donde fue clonado.

1. Descargar y descomprimir `Glassfish V4` desde http://glassfish.dev.java.net. La versión **Java EE 7 Full Platform**.

1. Editar `<EXAMPLES_HOME>/config/common.properties` y establecer:
  ```
  EXAMPLES_HOME: El directorio donde fue clonado el repositorio.
  SERVER_HOME: El directorio donde fue descomprimido Glassfish.
  ```
  > NOTA: Asegurarse de usar el separador *barra* ('/') y no *barra invertida* ('\').

1. Iniciar el servidor y la base de datos ejecutando el comando `ant start`.
  > Para detener el servidor y la base de datos `ant stop`. Los comandos disponibles se encuentran en `<EXAMPLES_HOME>/build.xml` y su detalle de implementación en `<EXAMPLES_HOME>/config/common.xml`.

1. Una vez iniciado el servidor se puede verificar su correcta configuración ingresando desde el navegador a http://localhost:8080/.


### A partir de aquí, ejecutar los ejemplos desde cada uno de sus directorios.

> `<EXAMPLES_HOME>/ejemplos/<EXAMPLE>`

1. Crear la base de datos `ant createdb`.

1. Para compilar el ejemplo `ant compile`.

1. Para correr los tests del ejemplo y verificar el funcionamiento `ant test`.

1. Para ejecutar el ejemplo `ant deploy`. Luego abrir el navegador y entrar en la dirección `http://localhost:8080/<EXAMPLE>`
