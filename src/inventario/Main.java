package inventario;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Clase principal del programa. Contiene el menú de consola (CLI) para
 * interactuar con la ListaProductos y la rutina main() que lo invoca.
 */
public class Main {

    /** Directorio (dentro del proyecto) donde deben estar las imágenes. */
    private static final String DIRECTORIO_IMAGENES = "imagenes";

    private static final Scanner scanner = new Scanner(System.in);
    private static final ListaProductos inventario = new ListaProductos();

    // ================= MAIN =================

    public static void main(String[] args) {
        menu();
        scanner.close();
    }

    // ================= MENÚ =================

    /** Muestra el menú en bucle hasta que el usuario elija salir. */
    private static void menu() {
        int opcion;
        do {
            imprimirMenu();
            opcion = leerEntero("Seleccione una opción: ", 0, 10);
            System.out.println();

            switch (opcion) {
                case 1 -> insertar(true);
                case 2 -> insertar(false);
                case 3 -> buscar();
                case 4 -> modificar();
                case 5 -> eliminar();
                case 6 -> agregarImagen();
                case 7 -> verImagenes();
                case 8 -> mostrarInventario();
                case 9 -> reporteCostos();
                case 10 -> cargarDatosDePrueba();
                case 0 -> System.out.println("Saliendo del sistema. ¡Hasta pronto!");
                default -> System.out.println("Opción no válida.");
            }
            if (opcion != 0) pausa();
        } while (opcion != 0);
    }

    private static void imprimirMenu() {
        System.out.println("""

                ==========================================
                        GESTIÓN DE INVENTARIO
                ==========================================
                  1. Insertar producto al inicio
                  2. Insertar producto al final
                  3. Buscar producto
                  4. Modificar producto
                  5. Eliminar producto
                  6. Agregar imagen a un producto
                  7. Ver imágenes de un producto
                  8. Mostrar inventario
                  9. Reporte de costos
                 10. Cargar datos de ejemplo
                  0. Salir
                ------------------------------------------""");
    }

    // ================= OPCIONES =================

    private static void insertar(boolean alInicio) {
        System.out.println("--- Insertar producto " + (alInicio ? "al inicio" : "al final") + " ---");
        String nombre = leerTexto("Nombre: ");
        if (inventario.buscarProducto(nombre) != null) {
            System.out.println("Ya existe un producto con ese nombre. Use la opción 4 para modificarlo.");
            return;
        }
        double precio = leerDecimal("Precio: ");
        String categoria = leerTexto("Categoría: ");
        String vencimiento = leerFecha("Fecha de vencimiento (AAAA-MM-DD, Enter si no aplica): ");
        int cantidad = leerEntero("Cantidad: ", 0, Integer.MAX_VALUE);

        if (alInicio) {
            inventario.insertarProductoInicio(nombre, precio, categoria, vencimiento, cantidad);
        } else {
            inventario.insertarProductoFinal(nombre, precio, categoria, vencimiento, cantidad);
        }
        System.out.println("Producto insertado correctamente.");
    }

    private static void buscar() {
        System.out.println("--- Buscar producto ---");
        Producto p = inventario.buscarProducto(leerTexto("Nombre del producto: "));
        System.out.println(p != null ? "Encontrado: " + p : "Producto no encontrado.");
    }

    private static void modificar() {
        System.out.println("--- Modificar producto ---");
        if (inventario.estaVacia()) {
            System.out.println("El inventario está vacío.");
            return;
        }
        String actual = leerTexto("Nombre del producto a modificar: ");
        Producto p = inventario.buscarProducto(actual);
        if (p == null) {
            System.out.println("Producto no encontrado.");
            return;
        }
        System.out.println("Datos actuales: " + p);

        String nombre = leerTexto("Nuevo nombre: ");
        Producto otro = inventario.buscarProducto(nombre);
        if (otro != null && otro != p) {
            System.out.println("Ya existe otro producto con ese nombre. No se modificó nada.");
            return;
        }
        double precio = leerDecimal("Nuevo precio: ");
        String categoria = leerTexto("Nueva categoría: ");
        String vencimiento = leerFecha("Nueva fecha de vencimiento (AAAA-MM-DD, Enter si no aplica): ");
        int cantidad = leerEntero("Nueva cantidad: ", 0, Integer.MAX_VALUE);

        inventario.modificarProducto(actual, nombre, precio, categoria, vencimiento, cantidad);
        System.out.println("Producto modificado correctamente.");
    }

    private static void eliminar() {
        System.out.println("--- Eliminar producto ---");
        if (inventario.estaVacia()) {
            System.out.println("El inventario está vacío.");
            return;
        }
        String nombre = leerTexto("Nombre del producto a eliminar: ");
        Producto p = inventario.buscarProducto(nombre);
        if (p == null) {
            System.out.println("Producto no encontrado.");
            return;
        }
        if (confirmar("¿Seguro que desea eliminar \"" + p.getNombre() + "\"? (s/n): ")) {
            inventario.eliminarProducto(nombre);
            System.out.println("Producto eliminado.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private static void agregarImagen() {
        System.out.println("--- Agregar imagen ---");
        if (inventario.estaVacia()) {
            System.out.println("El inventario está vacío.");
            return;
        }
        String nombre = leerTexto("Nombre del producto: ");
        if (inventario.buscarProducto(nombre) == null) {
            System.out.println("Producto no encontrado.");
            return;
        }
        System.out.println("La imagen debe estar dentro de la carpeta \"" + DIRECTORIO_IMAGENES + "/\" del proyecto.");
        String archivo = leerTexto("Nombre del archivo (ej: leche.png): ");
        String ruta = DIRECTORIO_IMAGENES + "/" + archivo;

        if (!new File(ruta).isFile()) {
            System.out.println("No se encontró el archivo \"" + ruta + "\". Copie la imagen a esa carpeta e intente de nuevo.");
            return;
        }
        if (inventario.buscarProducto(nombre).getListaImagenes().contains(ruta)) {
            System.out.println("Esa imagen ya está registrada en el producto.");
            return;
        }
        inventario.agregarImagen(nombre, ruta);
        System.out.println("Imagen agregada al producto.");
    }

    private static void verImagenes() {
        System.out.println("--- Imágenes de un producto ---");
        Producto p = inventario.buscarProducto(leerTexto("Nombre del producto: "));
        if (p == null) {
            System.out.println("Producto no encontrado.");
        } else if (p.getListaImagenes().isEmpty()) {
            System.out.println("El producto no tiene imágenes.");
        } else {
            for (int i = 0; i < p.getListaImagenes().size(); i++) {
                System.out.println((i + 1) + ". " + p.getListaImagenes().get(i));
            }
        }
    }

    private static void mostrarInventario() {
        System.out.println("--- Inventario (" + inventario.getTamano() + " productos) ---");
        inventario.mostrarLista();
    }

    private static void reporteCostos() {
        inventario.reporteCostos();
    }

    /** Carga productos de ejemplo para probar el programa rápidamente. */
    private static void cargarDatosDePrueba() {
        if (inventario.buscarProducto("Leche") != null || inventario.buscarProducto("Manzana") != null
                || inventario.buscarProducto("Pan") != null) {
            System.out.println("Los datos de ejemplo ya fueron cargados (o ya existen esos nombres).");
            return;
        }
        inventario.insertarProductoInicio("Manzana", 500.0, "Frutas", "2026-10-10", 6);
        inventario.insertarProductoInicio("Leche", 900.0, "Lácteos", "2026-10-15", 2);
        inventario.insertarProductoFinal("Pan", 1200.0, "Panadería", "2026-10-08", 3);
        inventario.agregarImagen("Leche", DIRECTORIO_IMAGENES + "/leche_frente.png");
        inventario.agregarImagen("Leche", DIRECTORIO_IMAGENES + "/leche_tabla_nutricional.png");
        inventario.agregarImagen("Manzana", DIRECTORIO_IMAGENES + "/manzanas_rojas.png");
        System.out.println("Se cargaron 3 productos de ejemplo.");
    }

    // ================= UTILIDADES DE ENTRADA =================

    /** Lee un texto no vacío. */
    private static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) return texto;
            System.out.println("El valor no puede estar vacío.");
        }
    }

    /** Lee un entero dentro del rango [min, max]. */
    private static int leerEntero(String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor >= min && valor <= max) return valor;
                System.out.println("Ingrese un número entre " + min + " y " + (max == Integer.MAX_VALUE ? "el máximo permitido" : max) + ".");
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero.");
            }
        }
    }

    /** Lee un número decimal mayor que cero (acepta coma o punto). */
    private static double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                double valor = Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
                if (valor > 0) return valor;
                System.out.println("El valor debe ser mayor que cero.");
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
            }
        }
    }

    /** Lee una fecha AAAA-MM-DD. Si se deja vacío devuelve "N/A" (no aplica). */
    private static String leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();
            if (texto.isEmpty()) return Producto.SIN_VENCIMIENTO;
            try {
                return LocalDate.parse(texto).toString();
            } catch (DateTimeParseException e) {
                System.out.println("Fecha inválida. Use el formato AAAA-MM-DD (ej: 2026-12-31).");
            }
        }
    }

    private static boolean confirmar(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String r = scanner.nextLine().trim().toLowerCase();
            if (r.equals("s") || r.equals("si") || r.equals("sí")) return true;
            if (r.equals("n") || r.equals("no")) return false;
            System.out.println("Responda con s o n.");
        }
    }

    private static void pausa() {
        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
}
