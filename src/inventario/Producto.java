package inventario;

import java.util.ArrayList;

/**
 * Entidad que representa un producto de la tienda.
 *
 * Guarda nombre, precio, categoría, fecha de vencimiento (si aplica) y cantidad.
 * La cantidad representa las unidades en inventario (y, en el próximo avance,
 * las unidades que se agregan al carrito del cliente).
 * Además mantiene una lista con las rutas de sus imágenes.
 */
public class Producto {

    /** Valor que se usa cuando el producto no tiene fecha de vencimiento. */
    public static final String SIN_VENCIMIENTO = "N/A";

    private String nombre;
    private double precio;
    private String categoria;
    private String fechaVencimiento;
    private int cantidad;
    private final ArrayList<String> listaImagenes;

    public Producto(String nombre, double precio, String categoria,
                    String fechaVencimiento, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.fechaVencimiento = fechaVencimiento;
        this.cantidad = cantidad;
        this.listaImagenes = new ArrayList<>();
    }

    /** Costo total del producto en función de su cantidad (precio x cantidad). */
    public double costoTotal() {
        return precio * cantidad;
    }

    /** Añade la ruta de una imagen a la lista de imágenes del producto. */
    public void agregarImagen(String ruta) {
        listaImagenes.add(ruta);
    }

    // ===== Getters =====
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public String getCategoria() { return categoria; }
    public String getFechaVencimiento() { return fechaVencimiento; }
    public int getCantidad() { return cantidad; }
    public ArrayList<String> getListaImagenes() { return listaImagenes; }

    // ===== Setters =====
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setFechaVencimiento(String fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    @Override
    public String toString() {
        return String.format("%s | Precio: %,.2f | Cantidad: %d | Categoría: %s | Vence: %s | Imágenes: %d",
                nombre, precio, cantidad, categoria, fechaVencimiento, listaImagenes.size());
    }
}
