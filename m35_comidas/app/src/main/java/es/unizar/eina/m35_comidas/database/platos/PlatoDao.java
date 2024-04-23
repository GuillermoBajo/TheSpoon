package es.unizar.eina.m35_comidas.database.platos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

/** Definición de un Data Access Object para los platos */

/**Interfaz DAO Data Access Object y proporciona métodos para realizar operaciones de acceso a datos en la base de datos.*/
@Dao
public interface PlatoDao {

    /**
     * El método insert se utiliza para insertar un nuevo plato en la base de datos.
     * La anotación @Insert indica que este método realiza una operación de inserción.
     * onConflict = OnConflictStrategy.IGNORE especifica que si hay un conflicto al intentar
     * insertar un plato con la misma clave primaria, se debe ignorar.
     *
     * @param plato el plato a insertar en la base de datos
     * @return el ID del plato insertado
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Plato plato);

    /**
     * El método update se utiliza para actualizar un plato existente en la base de datos.
     *
     * @param plato el plato a actualizar en la base de datos
     * @return el número de filas afectadas por la operación de actualización
     */
    @Update
    int update(Plato plato);

    /**
     * El método delete se utiliza para eliminar un plato existente de la base de datos.
     *
     * @param plato el plato a eliminar de la base de datos
     * @return el número de filas afectadas por la operación de eliminación
     */
    @Delete
    int delete(Plato plato);

    /**
     * El método deleteAll se utiliza para eliminar todos los platos de la base de datos.
     */
    @Query("DELETE FROM plato")
    void deleteAll();

    /**
     * El método getOrderedPlatos se utiliza para obtener una lista ordenada de platos
     * de la base de datos, según la consulta especificada.
     *
     * @param query la consulta SQL para obtener los platos ordenados
     * @return una lista observable de platos ordenados
     */
    @RawQuery(observedEntities = Plato.class)
    LiveData<List<Plato>> getOrderedPlatos(SupportSQLiteQuery query);
}


