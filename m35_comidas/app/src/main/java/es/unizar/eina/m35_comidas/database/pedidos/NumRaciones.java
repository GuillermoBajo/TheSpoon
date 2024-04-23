package es.unizar.eina.m35_comidas.database.pedidos;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import es.unizar.eina.m35_comidas.database.platos.Plato;

/**
 * Clase para representar la cantidad de un plato específico en un pedido.
 * Cada entrada en esta tabla almacena los IDs: IDpedido, IDplato en el pedido, y la cantidad de veces que el plato está en ese pedido.
 */
@Entity(tableName = "numRaciones", primaryKeys = {"pedidoId", "platoId"}, foreignKeys = {
    @ForeignKey(
            entity = Plato.class,
            parentColumns = "platoId",
            childColumns = "platoId",
            onDelete = ForeignKey.CASCADE
    ),

    @ForeignKey(
            entity = Pedido.class,
            parentColumns = "pedidoId",
            childColumns = "pedidoId",
            onDelete = ForeignKey.CASCADE
    )
},
    indices = {
            @Index("pedidoId"),
            @Index("platoId")
        })
public class NumRaciones {
    @NonNull
    @ColumnInfo(name = "pedidoId")
    private Integer pedidoId;

    @NonNull
    @ColumnInfo(name = "platoId")
    private Integer platoId;

    @NonNull
    @ColumnInfo(name = "cantidad")
    private Integer cantidad;

    /**
     * Constructor de la clase NumRaciones.
     * @param pedidoId ID del pedido.
     * @param platoId ID del plato.
     * @param cantidad Cantidad de veces que el plato está en ese pedido.
     */
    public NumRaciones(Integer pedidoId, Integer platoId, Integer cantidad){
        this.pedidoId = pedidoId;
        this.platoId = platoId;
        this.cantidad = cantidad;
    }

    /**
     * Establece la cantidad de veces que el plato está en ese pedido.
     * @param cantidad Nueva cantidad.
     */
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene la cantidad de veces que el plato está en ese pedido.
     * @return Cantidad de veces que el plato está en ese pedido.
     */
    public Integer getCantidad() {
        return cantidad;
    }

    /**
     * Obtiene el ID del pedido.
     * @return ID del pedido.
     */
    public Integer getPedidoId() {
        return pedidoId;
    }

    /**
     * Obtiene el ID del plato.
     * @return ID del plato.
     */
    public Integer getPlatoId() {
        return platoId;
    }

    /**
     * Establece el ID del pedido.
     * @param pedidoId Nuevo ID del pedido.
     */
    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    /**
     * Establece el ID del plato.
     * @param platoId Nuevo ID del plato.
     */
    public void setPlatoId(int platoId) {
        this.platoId = platoId;
    }
}
