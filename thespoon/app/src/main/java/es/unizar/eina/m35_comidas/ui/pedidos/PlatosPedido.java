package es.unizar.eina.m35_comidas.ui.pedidos;

import es.unizar.eina.m35_comidas.database.pedidos.NumRaciones;
import es.unizar.eina.m35_comidas.database.platos.Plato;

/**
 * Clase que representa la relación entre un plato y la cantidad de raciones en un pedido.
 */
public class PlatosPedido {
    private NumRaciones nr; // Cantidad de raciones
    private Plato plato; // Plato asociado al pedido

    /**
     * Constructor de la clase PlatosPedido.
     * @param plato Plato asociado al pedido.
     * @param nr Cantidad de raciones.
     */
    public PlatosPedido(Plato plato, NumRaciones nr){
        this.plato = plato;
        this.nr = nr;
    }

    /**
     * Obtiene la cantidad de raciones.
     * @return La cantidad de raciones.
     */
    public int getCantidad(){
        return this.nr.getCantidad();
    }

    /**
     * Establece la cantidad de raciones.
     * @param cantidad La cantidad de raciones a establecer.
     */
    public void setCantidad(int cantidad){
        this.nr.setCantidad(cantidad);
    }

    /**
     * Obtiene el nombre del plato.
     * @return El nombre del plato.
     */
    public String getNombre(){
        return this.plato.getTitulo();
    }

    /**
     * Obtiene el precio del plato.
     * @return El precio del plato.
     */
    public double getPrecio(){
        return this.plato.getPrecio();
    }

    /**
     * Obtiene la categoría del plato.
     * @return La categoría del plato.
     */
    public String getCategoria(){
        return this.plato.getCategoria();
    }

    /**
     * Obtiene el objeto Plato asociado al pedido.
     * @return El objeto Plato asociado al pedido.
     */
    public Plato getPlato() {
        return this.plato;
    }

    /**
     * Obtiene el objeto NumRaciones asociado al pedido.
     * @return El objeto NumRaciones asociado al pedido.
     */
    public NumRaciones getNumraciones() {
        return this.nr;
    }

    /**
     * Sobrescribe el método equals para comparar la igualdad entre objetos PlatosPedido.
     * @param obj Objeto a comparar.
     * @return true si los objetos son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        PlatosPedido other = (PlatosPedido) obj;

        return this.plato.getTitulo().equals(other.plato.getTitulo()) &&
                this.nr.getPlatoId() == other.nr.getPlatoId() &&
                this.nr.getCantidad() == other.nr.getCantidad() &&
                this.nr.getPedidoId() == other.nr.getPedidoId();
    }
}

