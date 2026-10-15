import java.util.Random;

/**
 * Aplicación Cadenas.
 * Genera N cadenas de letras con longitud aleatoria (entre 1 y 20 caracteres)
 * y las escribe en la salida estándar (una por línea).
 *
 * Uso: java Cadenas <numeroDeCadenas>
 */
public class Cadenas {

    // Longitud máxima que puede tener cada cadena
    // Pongo 20 porque es lo que pide la práctica
    private static final int LONGITUD_MAXIMA = 20;

    // Caracteres que se pueden usar: todas las letras del alfabeto (mayúsculas y minúsculas)
    private static final String ALFABETO = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) {

        // 1. VALIDACIÓN: tiene que llegar exactamente un argumento
        if (args.length != 1) {
            System.err.println("Error: número de argumentos incorrecto.");
            System.err.println("Uso: java Cadenas <numeroDeCadenas>");
            System.exit(1); // Código distinto de 0 = el proceso terminó con error
        }

        // 2. VALIDACIÓN: el argumento debe ser un número entero positivo
        int numeroCadenas = 0;
        try {
            numeroCadenas = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            // Se lanza si el texto no es un número (por ejemplo "hola")
            System.err.println("Error: '" + args[0] + "' no es un número entero válido.");
            System.exit(1);
        }

        if (numeroCadenas <= 0) {
            System.err.println("Error: el número de cadenas debe ser mayor que 0.");
            System.exit(1);
        }

        // 3. GENERACIÓN: creamos tantas cadenas como se haya pedido
        Random random = new Random();

        for (int i = 0; i < numeroCadenas; i++) {
            // nextInt(20) devuelve un valor entre 0 y 19, así que sumamos 1
            // para que la longitud quede entre 1 y 20
            int longitud = random.nextInt(LONGITUD_MAXIMA) + 1;

            // StringBuilder es más eficiente que concatenar Strings con "+" en un bucle
            StringBuilder cadena = new StringBuilder();

            for (int j = 0; j < longitud; j++) {
                // Elegimos una posición aleatoria del alfabeto y añadimos esa letra
                int posicion = random.nextInt(ALFABETO.length());
                cadena.append(ALFABETO.charAt(posicion));
            }

            // Escribimos la cadena en la salida estándar.
            // El proceso padre (Generador) leerá esta salida.
            System.out.println(cadena);
        }
    }
}