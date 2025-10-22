import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String SEPARADOR = ";";

        try {
            String originalFile = "./src/ass.csv";

            System.out.println("Ingrese el nombre para la copia del CSV");
            String copiaFile = "./src/" + sc.nextLine() + ".csv";

            Files.copy(Paths.get(originalFile), Paths.get(copiaFile), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Archivo copiado correctamente como " + copiaFile);

            List<String[]> data = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(copiaFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    data.add(line.split(SEPARADOR, -1));
                }
            }

            if (data.isEmpty()) {
                System.err.println("El archivo CSV está vacío.");
                return;
            }

            String[] header = data.get(0);
            List<String> headerList = new ArrayList<>(Arrays.asList(header));

            // asegurar columnas necesarias
            String[] columnas = {"CC", "excelente", "bien", "normal", "malo", "nulo", "explicacion"};
            for (String col : columnas) {
                if (!headerList.contains(col)) {
                    headerList.add(col);
                }
            }

            header = headerList.toArray(new String[0]);
            data.set(0, header);


            boolean continuar = true;
            while (continuar) {
                String[] nuevaFila = new String[header.length];
                Arrays.fill(nuevaFila, "");

                System.out.println("Ingrese el nombre del ejercicio: ");
                nuevaFila[headerList.indexOf("CC")] = sc.nextLine();

                System.out.println("Seleccione como de bien hizo el ejercicio el alumno:");
                System.out.println("1) excelente");
                System.out.println("2) bien");
                System.out.println("3) normal");
                System.out.println("4) mal");
                System.out.println("5) nulo");
                String nivel = sc.nextLine().toLowerCase();

                switch (nivel) {
                    case "1" -> nuevaFila[headerList.indexOf("excelente")] = "X";
                    case "2" -> nuevaFila[headerList.indexOf("bien")] = "X";
                    case "3" -> nuevaFila[headerList.indexOf("normal")] = "X";
                    case "4" -> nuevaFila[headerList.indexOf("malo")] = "X";
                    case "5" -> nuevaFila[headerList.indexOf("nulo")] = "X";
                    default -> System.out.println("Nivel no válido. Se deja vacío.");
                }

                System.out.print("Ingrese una explicación: ");
                nuevaFila[headerList.indexOf("explicacion")] = sc.nextLine();

                data.add(nuevaFila);

                System.out.print("¿Desea agregar otro ejercicio? (s/n): ");
                continuar = sc.nextLine().equalsIgnoreCase("s");
            }

            double totalPuntos = 0;
            int totalEjercicios = 0;

            for (int i = 1; i < data.size(); i++) {
                String[] fila = data.get(i);
                int puntos = 0;

                if (getValue(fila, headerList, "excelente").equalsIgnoreCase("X")) puntos = 100;
                else if (getValue(fila, headerList, "bien").equalsIgnoreCase("X")) puntos = 75;
                else if (getValue(fila, headerList, "normal").equalsIgnoreCase("X")) puntos = 50;
                else if (getValue(fila, headerList, "malo").equalsIgnoreCase("X")) puntos = 25;
                else if (getValue(fila, headerList, "nulo").equalsIgnoreCase("X")) puntos = 0;

                totalPuntos += puntos;
                totalEjercicios++;
            }

            double media = totalEjercicios > 0 ? totalPuntos / totalEjercicios : 0;

            try (PrintWriter pw = new PrintWriter(new FileWriter(copiaFile))) {
                pw.println(String.join(SEPARADOR, header));

                for (int i = 1; i < data.size(); i++) {
                    String[] fila = Arrays.copyOf(data.get(i), header.length);
                    pw.println(String.join(SEPARADOR, fila));
                }

                pw.println();
                pw.println("MEDIA GENERAL" + SEPARADOR + SEPARADOR + String.format("%.2f", media));
            }

            System.out.println("Evaluaciones guardadas correctamente en " + copiaFile);
            System.out.printf("Media total: %.2f%n", media);

        } catch (IOException e) {
            System.err.println("Error al procesar el archivo: " + e.getMessage());
        }
    }

    private static String getValue(String[] fila, List<String> header, String col) {
        int idx = header.indexOf(col);
        return (idx >= 0 && idx < fila.length) ? fila[idx] : "";
    }
}