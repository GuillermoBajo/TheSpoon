package es.unizar.eina.m35_comidas.database.pedidos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import es.unizar.eina.m35_comidas.database.platos.Plato;

/**
 * Definición de un Data Access Object (DAO) para los pedidos en la base de datos.
 * La interfaz proporciona métodos para realizar operaciones de acceso a datos relacionadas con los pedidos.
 */
@Dao
public interface PedidoDao {

    /**
     * Método utilizado para insertar un nuevo pedido en la base de datos.
     * La anotación @Insert indica que este método realiza una operación de inserción.
     * onConflict = OnConflictStrategy.IGNORE especifica que si hay un conflicto al intentar
     * insertar un pedido con la misma clave primaria, se debe ignorar.
     *
     * @param pedido El pedido a insertar en la base de datos.
     * @return El ID del pedido insertado.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Pedido pedido);

    /**
     * Método utilizado para actualizar un pedido existente en la base de datos.
     *
     * @param pedido El pedido a actualizar.
     * @return El número de filas afectadas por la operación de actualización.
     */
    @Update
    int update(Pedido pedido);

    /**
     * Método utilizado para eliminar un pedido existente de la base de datos.
     *
     * @param pedido El pedido a eliminar.
     * @return El número de filas afectadas por la operación de eliminación.
     */
    @Delete
    int delete(Pedido pedido);

    /**
     * Método utilizado para eliminar todos los pedidos de la base de datos.
     */
    @Query("DELETE FROM pedido")
    void deleteAll();

    /**
     * Método utilizado para obtener una lista de pedidos ordenados según la consulta proporcionada.
     *
     * @param query La consulta utilizada para ordenar los pedidos.
     * @return Una lista observable de pedidos ordenados.
     */
    @RawQuery(observedEntities = Pedido.class)
    LiveData<List<Pedido>> getOrderedPedidos(SupportSQLiteQuery query);

    /**
     * Método utilizado para insertar una nueva relación NumRaciones en la base de datos.
     *
     * @param numraciones La relación NumRaciones a insertar en la base de datos.
     * @return El ID de la relación NumRaciones insertada.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(NumRaciones numraciones);

    /**
     * Método utilizado para eliminar una relación NumRaciones de la base de datos.
     *
     * @param numraciones La relación NumRaciones a eliminar.
     * @return El número de filas afectadas por la operación de eliminación.
     */
    @Delete
    int delete(NumRaciones numraciones);

    /**
     * Método utilizado para actualizar una relación NumRaciones en la base de datos.
     *
     * @param numraciones La relación NumRaciones a actualizar.
     * @return El número de filas afectadas por la operación de actualización.
     */
    @Update
    int update(NumRaciones numraciones);

    /**
     * Método utilizado para obtener un pedido con información detallada sobre los platos asociados.
     *
     * @param pedidoId El ID del pedido.
     * @return Un objeto LiveData que contiene un pedido con la lista de platos asociados.
     */
    @Transaction
    @Query("SELECT * FROM PEDIDO where pedidoId = :pedidoId")
    LiveData<UnionPlatoPedido> getPedidoConPlatos(int pedidoId);

    /**
     * Método utilizado para obtener una lista de platos que no están asociados a un pedido específico.
     *
     * @param pedidoId El ID del pedido.
     * @return Una lista observable de platos que no están en el pedido especificado.
     */
    @Transaction
    @Query("SELECT plato.* FROM plato LEFT JOIN NumRaciones ON plato.platoId = NumRaciones.platoId AND NumRaciones.pedidoId = :pedidoId " +
            "WHERE NumRaciones.platoId IS NULL;")
    LiveData<List<Plato>> getPlatosNoEnPedido(int pedidoId);
}
