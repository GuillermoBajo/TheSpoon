package es.unizar.eina.m35_comidas.database.pedidos;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * Clase anotada como entidad que representa un pedido en la base de datos.
 * Contiene información como el nombre del cliente, número del cliente, estado, fecha, hora y precio del pedido.
 */
@Entity(tableName = "pedido")
public class Pedido {

    /**
     * Clase que representa un pedido en la base de datos.
     */

    /** 
     * Identificador único del pedido. La anotación @PrimaryKey indica que esta propiedad
     * es la clave primaria. autoGenerate = true significa que la clave primaria se generará automáticamente.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pedidoId")
    private int id;

    /** 
     * Nombre del cliente que realizó el pedido.
     */
    @NonNull
    @ColumnInfo(name = "nombreCliente")
    private String nombreCliente;

    /** 
     * Número del cliente que realizó el pedido.
     */
    @NonNull
    @ColumnInfo(name = "numeroCliente")
    private Integer numeroCliente;

    /** 
     * Estado actual del pedido.
     */
    @NonNull
    @ColumnInfo(name = "estado")
    private String estado;

    /** 
     * Fecha en la que se realizó el pedido.
     */
    @NonNull
    @ColumnInfo(name = "fecha")
    private String fecha;

    /** 
     * Hora en la que se realizó el pedido.
     */
    @NonNull
    @ColumnInfo(name = "hora")
    private String hora;

    /** 
     * Precio total del pedido.
     */
    @NonNull
    @ColumnInfo(name = "precioPedido")
    private Double precioPedido;

    /** 
     * Constructor de la clase Pedido.
     * @param nombreCliente Nombre del cliente que realizó el pedido.
     * @param numeroCliente Número del cliente que realizó el pedido.
     * @param estado Estado actual del pedido.
     * @param fecha Fecha en la que se realizó el pedido.
     * @param hora Hora en la que se realizó el pedido.
     * @param precioPedido Precio total del pedido.
     */
    public Pedido(@NonNull String nombreCliente, @NonNull Integer numeroCliente, @NonNull String estado,
                  @NonNull String fecha, @NonNull String hora, @NonNull Double precioPedido) {
        this.nombreCliente = nombreCliente;
        this.numeroCliente = numeroCliente;
        this.estado = estado;
        this.fecha = fecha;
        this.hora = hora;
        this.precioPedido = precioPedido;
    }

    /**
     * Devuelve el identificador del pedido.
     *
     * @return El identificador del pedido.
     */
    public int getId(){
        return this.id;
    }

    /**
     * Permite actualizar el identificador del pedido.
     *
     * @param id El nuevo identificador del pedido.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre del cliente del pedido.
     *
     * @return El nombre del cliente del pedido.
     */
    public String getNombreCliente(){
        return this.nombreCliente;
    }

    /**
     * Devuelve el número del cliente del pedido.
     *
     * @return El número del cliente del pedido.
     */
    public Integer getNumeroCliente(){
        return this.numeroCliente;
    }

    /**
     * Devuelve el estado del pedido.
     *
     * @return El estado del pedido.
     */
    public String getEstado(){
        return this.estado;
    }

    /**
     * Devuelve la fecha del pedido.
     *
     * @return La fecha del pedido.
     */
    public String getFecha(){
        return this.fecha;
    }

    /**
     * Devuelve la hora del pedido.
     *
     * @return La hora del pedido.
     */
    public String getHora(){
        return this.hora;
    }

    /**
     * Devuelve el precio total del pedido.
     *
     * @return El precio total del pedido.
     */
    public Double getPrecioPedido() {
        return this.precioPedido;
    }
}
