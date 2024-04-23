package es.unizar.eina.m35_comidas.database.pedidos;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

import es.unizar.eina.m35_comidas.database.platos.Plato;

/**
 * Clase que representa la unión entre un pedido y los platos asociados, incluyendo la cantidad de cada plato.
 * Utilizada para obtener información detallada sobre un pedido y sus platos asociados desde la base de datos.
 */
public class UnionPlatoPedido {
    @Embedded
    public Pedido pedido;

    @Relation(
            parentColumn = "pedidoId",
            entityColumn = "platoId",
            associateBy = @Junction(NumRaciones.class)
    )
    public List<Plato> platos;

    @Relation(
            parentColumn = "pedidoId",
            entityColumn = "pedidoId"
    )
    public List<NumRaciones> cantidad;

    /**
     * Constructor sin parámetros que inicializa las listas de platos y cantidades.
     */
    public UnionPlatoPedido() {
        this.platos = new ArrayList<>();
        this.cantidad = new ArrayList<>();
    }

    /**
     * Constructor que recibe un pedido, una lista de platos y una lista de cantidades.
     *
     * @param pedido El pedido asociado.
     * @param platos La lista de platos asociados al pedido.
     * @param cantidades La lista de cantidades correspondientes a los platos en el pedido.
     */
    public UnionPlatoPedido(Pedido pedido, List<Plato> platos, List<NumRaciones> cantidades) {
        this.pedido = pedido;
        this.cantidad = cantidades;
        this.platos = platos;
    }
}