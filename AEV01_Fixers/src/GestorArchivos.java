import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class GestorArchivos {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Introduce la ruta del directorio de trabajo:");
        String ruta = sc.nextLine();

        File directorio = new File(ruta);

        if (!directorio.exists() || !directorio.isDirectory()) {
            System.out.println("La ruta no es válida o no es un directorio.");
            return;
        }

        int opcion;
        do {
            System.out.println("--- MENÚ ---");
            System.out.println("1. Mostrar información");
            System.out.println("2. Crear carpeta");
            System.out.println("3. Crear fichero");
            System.out.println("4. Eliminar archivo/carpeta");
            System.out.println("5. Renombrar archivo/carpeta");
            System.out.println("0. Salir");
            System.out.print("Elige opción: ");
            opcion = Integer.parseInt(sc.nextLine());

            if (opcion == 1) {
                getInformacion(directorio);
            } else if (opcion == 2) {
                creaCarpeta(directorio);
            } else if (opcion == 3) {
                creaFichero(directorio);
            } else if (opcion == 4) {
                elimina(directorio);
            } else if (opcion == 5) {
                renombra(directorio);
            } else if (opcion == 0) {
                System.out.println("Saliendo...");
            } else {
                System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    // --- MÉTODOS ---

    public static void getInformacion(File f) {
        System.out.println("Información del directorio/archivo: " + f.getAbsolutePath());
        if (f.exists()) {
            System.out.println("Nombre: " + f.getName());
            System.out.println("Tipo: " + (f.isFile() ? "Archivo" : "Directorio"));
            System.out.println("Ruta completa: " + f.getAbsolutePath());
            System.out.println("Última modificación: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(f.lastModified())));
            System.out.println("Oculto: " + f.isHidden());

            if (f.isFile()) {
                System.out.println("Tamaño: " + f.length() + " bytes");
            } else {
                File[] elementos = f.listFiles();
                System.out.println("Elementos que contiene: " + (elementos != null ? elementos.length : 0));
                System.out.println("Espacio libre: " + f.getFreeSpace() + " bytes");
                System.out.println("Espacio usable: " + f.getUsableSpace() + " bytes");
                System.out.println("Espacio total: " + f.getTotalSpace() + " bytes");
            }
        } else {
            System.out.println("El archivo/directorio no existe.");
        }
    }

    public static void creaCarpeta(File dir) {
        System.out.print("Introduce nombre de la carpeta: ");
        String nombre = sc.nextLine();
        File carpeta = new File(dir, nombre);
        if (carpeta.mkdir()) {
            System.out.println("Carpeta creada: " + carpeta.getAbsolutePath());
        } else {
            System.out.println("No se pudo crear la carpeta.");
        }
    }

    public static void creaFichero(File dir) {
        System.out.print("Introduce nombre del archivo: ");
        String nombre = sc.nextLine();
        File archivo = new File(dir, nombre);
        try {
            if (archivo.createNewFile()) {
                System.out.println("Archivo creado: " + archivo.getAbsolutePath());
            } else {
                System.out.println("No se pudo crear el archivo (ya existe).");
            }
        } catch (IOException e) {
            System.out.println("Error al crear archivo: " + e.getMessage());
        }
    }

    public static void elimina(File dir) {
        System.out.print("Introduce el nombre del archivo/carpeta a eliminar: ");
        String nombre = sc.nextLine();
        File elemento = new File(dir, nombre);
        if (elemento.exists()) {
            if (elemento.delete()) {
                System.out.println("Eliminado correctamente.");
            } else {
                System.out.println("No se pudo eliminar (¿carpeta no vacía?).");
            }
        } else {
            System.out.println("El archivo/carpeta no existe.");
        }
    }

    public static void renombra(File dir) {
        System.out.print("Introduce el nombre del archivo/carpeta a renombrar: ");
        String viejoNombre = sc.nextLine();
        File viejo = new File(dir, viejoNombre);

        if (!viejo.exists()) {
            System.out.println("No existe.");
            return;
        }

        System.out.print("Introduce el nuevo nombre: ");
        String nuevoNombre = sc.nextLine();
        File nuevo = new File(dir, nuevoNombre);

        if (viejo.renameTo(nuevo)) {
            System.out.println("Renombrado correctamente.");
        } else {
            System.out.println("No se pudo renombrar.");
        }
    }
}
