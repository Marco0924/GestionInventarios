package inventario;

/**
 * Lista enlazada simple de productos.
 *
 * Permite insertar al inicio y al final, buscar, modificar, eliminar,
 * agregar imágenes, mostrar la lista y generar un reporte de costos.
 * La búsqueda por nombre no distingue mayúsculas de minúsculas.
 */
public class ListaProductos {

    private Nodo cabeza;
    private int tamano;

    public ListaProductos() {
        cabeza = null;
        tamano = 0;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int getTamano() {
        return tamano;
    }

    // ===== INSERTAR =====

    /** Inserta un producto nuevo al inicio de la lista. O(1). */
    public void insertarProductoInicio(String nombre, double precio, String categoria,
                                       String vencimiento, int cantidad) {
        Nodo nuevo = new Nodo(new Producto(nombre, precio, categoria, vencimiento, cantidad));
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
        tamano++;
    }

    /** Inserta un producto nuevo al final de la lista. O(n). */
    public void insertarProductoFinal(String nombre, double precio, String categoria,
                                      String vencimiento, int cantidad) {
        Nodo nuevo = new Nodo(new Producto(nombre, precio, categoria, vencimiento, cantidad));
        if (estaVacia()) {
            cabeza = nuevo;
        } else {
            Nodo aux = cabeza;
            while (aux.getSiguiente() != null) {
                aux = aux.getSiguiente();
            }
            aux.setSiguiente(nuevo);
        }
        tamano++;
    }

    // ===== BUSCAR =====

    /** Devuelve el producto con ese nombre, o null si no existe. */
    public Producto buscarProducto(String nombre) {
        Nodo aux = cabeza;
        while (aux != null) {
            if (aux.getProducto().getNombre().equalsIgnoreCase(nombre)) {
                return aux.getProducto();
            }
            aux = aux.getSiguiente();
        }
        return null;
    }

    // ===== ELIMINAR =====

    /** Elimina el producto con ese nombre y lo devuelve, o null si no existe. */
    public Producto eliminarProducto(String nombre) {
        if (estaVacia()) return null;

        // Caso 1: el producto a eliminar es el primero
        if (cabeza.getProducto().getNombre().equalsIgnoreCase(nombre)) {
            Producto eliminado = cabeza.getProducto();
            cabeza = cabeza.getSiguiente();
            tamano--;
            return eliminado;
        }

        // Caso 2: está en medio o al final
        Nodo anterior = cabeza;
        Nodo actual = cabeza.getSiguiente();
        while (actual != null) {
            if (actual.getProducto().getNombre().equalsIgnoreCase(nombre)) {
                anterior.setSiguiente(actual.getSiguiente());
                tamano--;
                return actual.getProducto();
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }
        return null;
    }

    // ===== MODIFICAR =====

    /** Reemplaza los datos del producto indicado. Devuelve false si no existe. */
    public boolean modificarProducto(String nombreActual, String nombre, double precio,
                                     String categoria, String vencimiento, int cantidad) {
        Producto p = buscarProducto(nombreActual);
        if (p == null) return false;

        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setCategoria(categoria);
        p.setFechaVencimiento(vencimiento);
        p.setCantidad(cantidad);
        return true;
    }

    // ===== IMÁGENES =====

    /** Agrega la ruta de una imagen al producto. Devuelve false si el producto no existe. */
    public boolean agregarImagen(String nombre, String ruta) {
        Producto p = buscarProducto(nombre);
        if (p == null) return false;
        p.agregarImagen(ruta);
        return true;
    }

    // ===== MOSTRAR =====

    public void mostrarLista() {
        System.out.println(obtenerLista());
    }

    public String obtenerLista() {
        if (estaVacia()) return "El inventario está vacío.";

        StringBuilder sb = new StringBuilder();
        Nodo aux = cabeza;
        int i = 1;
        while (aux != null) {
            sb.append(i++).append(". ").append(aux.getProducto()).append("\n");
            aux = aux.getSiguiente();
        }
        return sb.toString();
    }

    // ===== REPORTE DE COSTOS =====

    public void reporteCostos() {
        System.out.println(obtenerReporteCostos());
    }

    /** Recorre la lista y arma el costo de cada producto y el costo total acumulado. */
    public String obtenerReporteCostos() {
        if (estaVacia()) return "El inventario está vacío.";

        StringBuilder sb = new StringBuilder("REPORTE DE COSTOS\n");
        sb.append("--------------------------------------------------\n");
        double total = 0;
        Nodo aux = cabeza;
        while (aux != null) {
            Producto p = aux.getProducto();
            sb.append(String.format("%-20s %,12.2f x %-5d = %,14.2f%n",
                    p.getNombre(), p.getPrecio(), p.getCantidad(), p.costoTotal()));
            total += p.costoTotal();
            aux = aux.getSiguiente();
        }
        sb.append("--------------------------------------------------\n");
        sb.append(String.format("COSTO TOTAL ACUMULADO: %,.2f", total));
        return sb.toString();
    }
}
