import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Aplicación Generador.
 * Pide al usuario cuántas instancias de "Cadenas" lanzar (mínimo 10) y cuántas
 * cadenas debe generar cada una. Lanza cada instancia como un PROCESO
 * independiente y muestra por pantalla lo que cada proceso escribe en su salida.
 *
 * IMPORTANTE: Cadenas.java debe estar en el mismo proyecto que este archivo.
 */
public class Generador {

    private static final int MINIMO_INSTANCIAS = 10;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. ENTRADA DE DATOS (con validación)
        int instancias = leerEntero(sc,
                "Número de instancias a lanzar (mínimo " + MINIMO_INSTANCIAS + "): ", MINIMO_INSTANCIAS);
        int cadenasPorInstancia = leerEntero(sc, "Número de cadenas que generará cada instancia: ", 1);

        // 2. PREPARAR EL COMANDO para ejecutar Cadenas
        // Ruta del ejecutable "java" de la misma JDK con la que se está ejecutando este programa
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        // Classpath actual: así el nuevo proceso sabe dónde encontrar la clase Cadenas
        String classpath = System.getProperty("java.class.path");

        List<Process> procesos = new ArrayList<>();

        // 3. LANZAR TODOS LOS PROCESOS
        // Primero los lanzamos todos seguidos para que se ejecuten a la vez (en paralelo)
        // y después leemos sus resultados.
        for (int i = 0; i < instancias; i++) {
            try {
                // Equivale a escribir en consola: java -cp <classpath> Cadenas <numero>
                ProcessBuilder pb = new ProcessBuilder(
                        java, "-cp", classpath, "Cadenas", String.valueOf(cadenasPorInstancia));

                // Mezcla la salida de errores con la salida normal, así leemos
                // los mensajes de error del hijo por el mismo canal
                pb.redirectErrorStream(true);

                procesos.add(pb.start()); // start() crea el proceso hijo
            } catch (IOException e) {
                System.err.println("Error al lanzar la instancia " + (i + 1) + ": " + e.getMessage());
            }
        }

        // 4. LEER LA SALIDA DE CADA PROCESO (comunicación padre <- hijo)
        for (int i = 0; i < procesos.size(); i++) {
            Process proceso = procesos.get(i);
            System.out.println("=== Instancia " + (i + 1) + " ===");

            // getInputStream() es la salida estándar del HIJO vista desde el PADRE
            try (BufferedReader lector = new BufferedReader(
                    new InputStreamReader(proceso.getInputStream()))) {

                String linea;
                while ((linea = lector.readLine()) != null) {
                    System.out.println(linea);
                }

                // Esperamos a que el proceso termine y comprobamos cómo acabó
                int codigoSalida = proceso.waitFor();
                if (codigoSalida != 0) {
                    System.err.println("La instancia " + (i + 1)
                            + " terminó con error (código " + codigoSalida + ")");
                }
            } catch (IOException e) {
                System.err.println("Error leyendo la instancia " + (i + 1) + ": " + e.getMessage());
            } catch (InterruptedException e) {
                // Buena práctica: restaurar el estado de interrupción del hilo
                Thread.currentThread().interrupt();
                System.err.println("Se interrumpió la espera de la instancia " + (i + 1));
            }
        }

        sc.close();
    }

    /**
     * Pide un número entero por teclado y repite la pregunta hasta que el usuario
     * escriba un entero válido que sea mayor o igual que "minimo".
     */
    private static int leerEntero(Scanner teclado, String mensaje, int minimo) {
        while (true) {
            System.out.print(mensaje);
            String texto = teclado.nextLine().trim();

            try {
                int numero = Integer.parseInt(texto);
                if (numero >= minimo) {
                    return numero; // Valor correcto: salimos del bucle
                }
                System.out.println("Error: el valor debe ser como mínimo " + minimo + ".");
            } catch (NumberFormatException e) {
                System.out.println("Error: '" + texto + "' no es un número entero válido.");
            }
        }
    }
}