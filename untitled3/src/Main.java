import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    private static final Map<String, String> profesores = new HashMap<>();

    static {
        profesores.put("SSP", "Yolanda");
        profesores.put("DSP", "Aida");
        profesores.put("AD", "Lluis");
        profesores.put("SGE", "Guillem");
        profesores.put("DI", "Guillem");
        profesores.put("ANG", "Ernesto");
        profesores.put("IP", "Lola");
        profesores.put("IPE", "Lola");
        profesores.put("PM", "Gines");
        profesores.put("PI", "Lola, Lluis, Salva");
        profesores.put("PS", "Salva");
        profesores.put("patio", "Guardia");
    }

    public static void main(String[] args) {

        String rutaCsv = "C:\\Users\\DAM2\\gits\\ej 1 lluis";

        List<String[]> horario = leerCsv(rutaCsv);

        if (horario.isEmpty()) {
            System.out.println("El archivo CSV está vacío o no se pudo leer.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduce el nombre del profesor: ");
        String profesorBuscado = scanner.nextLine().trim();

        System.out.println("Introduce la columna (1 a " + (horario.get(0).length ) + "): ");
        int columna = scanner.nextInt() - 1;

        System.out.println("Introduce la fila (1 a " + (horario.size() ) + "): ");
        int fila = scanner.nextInt() - 1;

        if (fila < 0 || fila >= horario.size() || columna < 0 || columna >= horario.get(0).length) {
            System.out.println("Coordenadas fuera de rango.");
            return;
        }

        String asignatura = horario.get(fila)[columna].trim();

        if (asignatura.isEmpty()) {
            System.out.println(profesorBuscado + " no tiene clase en esta hora.");
        } else if (asignatura.equalsIgnoreCase("patio")) {
            System.out.println(profesorBuscado + " está de GUARDIA.");
        } else {
            String profesorAsignatura = profesores.getOrDefault(asignatura, "Desconocido");

            List<String> listaProfes = Arrays.asList(profesorAsignatura.split(","));

            boolean imparte = listaProfes.stream()
                    .anyMatch(p -> p.trim().equalsIgnoreCase(profesorBuscado));

            if (imparte) {
                System.out.println(profesorBuscado + " está DANDO CLASE de " + asignatura + ".");
            } else {
                System.out.println(profesorBuscado + " no tiene clase en esta hora.");
            }
        }
    }

    private static List<String[]> leerCsv(String rutaCsv) {
        List<String[]> filas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaCsv))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] columnas = linea.split(";");
                filas.add(columnas);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV: " + e.getMessage());
        }
        return filas;
    }
}