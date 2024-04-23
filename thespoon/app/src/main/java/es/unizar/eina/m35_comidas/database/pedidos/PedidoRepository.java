package es.unizar.eina.m35_comidas.database.pedidos;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import es.unizar.eina.m35_comidas.database.ComidasRoomDatabase;
import es.unizar.eina.m35_comidas.database.platos.Plato;


/**
 * Clase que actúa como intermediario entre la capa de datos y la interfaz de usuario de la aplicación.
 * Se encarga de interactuar con la base de datos y proporcionar métodos para realizar operaciones de
 * inserción, actualización y eliminación de pedidos.
 */
public class PedidoRepository {

    private PedidoDao mPedidoDao;
    private LiveData<List<Pedido>> mAllPedidos;
    private final long TIMEOUT = 15000;

    /**
     * Constructor de la clase PedidoRepository.
     * Recibe una instancia de la clase Application y utiliza PlatoRoomDatabase para
     * obtener una instancia de PedidoDao y la lista de todos los pedidos ordenados.
     *
     * @param application La instancia de la clase Application.
     */
    public PedidoRepository(Application application) {
        ComidasRoomDatabase db = ComidasRoomDatabase.getDatabase(application);
        mPedidoDao = db.PedidoDao();
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM pedido");
        mAllPedidos = mPedidoDao.getOrderedPedidos(query);
    }

    /**
     * Obtiene todos los pedidos almacenados en la base de datos.
     *
     * @return LiveData que contiene la lista de todos los pedidos.
     */
    public LiveData<List<Pedido>> getAllPedidos() {
        return mAllPedidos;
    }

    /**
     * Obtiene una lista de platos que no están asociados a un pedido específico.
     *
     * @param pedidoId El ID del pedido.
     * @return Una lista observable de platos que no están en el pedido especificado.
     */
    public LiveData<List<Plato>> getPlatosNoEnPedido(int pedidoId) {
        return mPedidoDao.getPlatosNoEnPedido(pedidoId);
    }

    /**
     * Obtiene un pedido con información detallada sobre los platos asociados.
     *
     * @param pedidoId El ID del pedido.
     * @return Un objeto LiveData que contiene un pedido con la lista de platos asociados.
     */
    public LiveData<UnionPlatoPedido> getAllPlatosFromPedido(int pedidoId) {
        if (pedidoId < 0) {
            LiveData<UnionPlatoPedido> mAllPedidosConPlatos = new MutableLiveData<>();
            return mAllPedidosConPlatos;
        }
        LiveData<UnionPlatoPedido> aux = mPedidoDao.getPedidoConPlatos(pedidoId);
        return aux;
    }

    /**
     * Ordena los pedidos según el criterio especificado.
     *
     * @param criterio El criterio de ordenación de los pedidos.
     *                 Los valores posibles son:
     *                 - "nombreCliente ASC": Ordena los pedidos por nombre de cliente en orden ascendente.
     *                 - "numeroCliente ASC": Ordena los pedidos por número de cliente en orden ascendente.
     *                 - "fecha ASC": Ordena los pedidos por fecha y hora en orden ascendente.
     */
    public void orderPed(String criterio, String filtro) {
        SimpleSQLiteQuery query;
        switch(criterio){
            case "":
                if (filtro == ""){
                    query = new SimpleSQLiteQuery("SELECT * FROM pedido");
                    mAllPedidos = mPedidoDao.getOrderedPedidos(query);
                } else {
                    query = new SimpleSQLiteQuery("SELECT * FROM pedido WHERE estado = ? ", new Object[]{filtro});
                    mAllPedidos = mPedidoDao.getOrderedPedidos(query);
                }
                break;
            case "nombreCliente ASC":
                if (filtro == ""){
                    query = new SimpleSQLiteQuery("SELECT * FROM pedido ORDER BY nombreCliente ASC");
                    mAllPedidos = mPedidoDao.getOrderedPedidos(query);
                } else {
                    query = new SimpleSQLiteQuery("SELECT * FROM pedido WHERE estado = ? ORDER BY nombreCliente ASC", new Object[]{filtro});
                    mAllPedidos = mPedidoDao.getOrderedPedidos(query);
                }
                break;

            case "numeroCliente ASC":
                if (filtro == "") {
                    query = new SimpleSQLiteQuery("SELECT * FROM pedido ORDER BY numeroCliente ASC");
                    mAllPedidos = mPedidoDao.getOrderedPedidos(query);
                }
                else {
                    query = new SimpleSQLiteQuery("SELECT * FROM pedido WHERE estado = ? ORDER BY numeroCliente ASC", new Object[]{filtro});
                    mAllPedidos = mPedidoDao.getOrderedPedidos(query);
                }
                break;
            case "fecha ASC":
                if (filtro == ""){
                    query = new SimpleSQLiteQuery("SELECT * FROM pedido ORDER BY fecha ASC, hora ASC");
                    mAllPedidos = mPedidoDao.getOrderedPedidos(query);
                }
                else {
                    query = new SimpleSQLiteQuery("SELECT * FROM pedido WHERE estado = ? ORDER BY fecha ASC, hora ASC", new Object[]{filtro});
                    mAllPedidos = mPedidoDao.getOrderedPedidos(query);
                }
                break;
        }
    }

    /**
     * Inserta un pedido en la base de datos.
     *
     * @param pedido El pedido que se desea insertar.
     * @return El identificador del pedido que se ha creado.
     */
    public long insert(Pedido pedido) {
        AtomicLong result = new AtomicLong();
        Semaphore resource = new Semaphore(0);
        ComidasRoomDatabase.databaseWriteExecutor.execute(() -> {
            long value = mPedidoDao.insert(pedido);
            result.set(value);
            resource.release();
        });
        try {
            resource.tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.d("PedidoRepository", "InterruptedException: " + e.getMessage());
        }
        return result.get();
    }

    /**
     * Actualiza un pedido en la base de datos.
     *
     * @param pedido El pedido que se desea actualizar.
     * @return El número de filas modificadas: 1 si el pedido se ha actualizado correctamente,
     *         0 si no existe un pedido con el mismo identificador o hay algún problema con los atributos.
     */
    public int update(Pedido pedido) {
        AtomicInteger result = new AtomicInteger();
        Semaphore resource = new Semaphore(0);
        ComidasRoomDatabase.databaseWriteExecutor.execute(() -> {
            result.set(mPedidoDao.update(pedido));
            resource.release();
        });
        try {
            resource.tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.d("PedidoRepository", "InterruptedException: " + e.getMessage());
        }
        return result.get();
    }

    /**
     * Elimina un pedido de la base de datos.
     *
     * @param pedido El pedido que se desea eliminar.
     * @return El número de filas eliminadas: 1 si el pedido se ha eliminado correctamente,
     *         0 si no existe un pedido con el mismo identificador o el identificador no es válido.
     */
    public int delete(Pedido pedido) {
        AtomicInteger result = new AtomicInteger();
        Semaphore resource = new Semaphore(0);
        ComidasRoomDatabase.databaseWriteExecutor.execute(() -> {
            result.set(mPedidoDao.delete(pedido));
            resource.release();
        });
        try {
            resource.tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.d("PedidoRepository", "InterruptedException: " + e.getMessage());
        }
        return result.get();
    }

    /**
     * Inserta una nueva relación NumRaciones en la base de datos.
     *
     * @param nr La relación NumRaciones a insertar en la base de datos.
     * @return El ID de la relación NumRaciones insertada.
     */
    public long insert(NumRaciones nr) {
        final long[] result = {0};
        ComidasRoomDatabase.databaseWriteExecutor.execute(() -> {
            result[0] = mPedidoDao.insert(nr);
        });
        return result[0];
    }

    /**
     * Actualiza una relación NumRaciones en la base de datos.
     *
     * @param nr La relación NumRaciones a actualizar.
     * @return El número de filas afectadas por la operación de actualización.
     */
    public int update(NumRaciones nr) {
        final int[] result = {0};
        ComidasRoomDatabase.databaseWriteExecutor.execute(() -> {
            result[0] = mPedidoDao.update(nr);
        });
        return result[0];
    }

    /**
     * Elimina una relación NumRaciones de la base de datos.
     *
     * @param nr La relación NumRaciones a eliminar.
     * @return El número de filas afectadas por la operación de eliminación.
     */
    public int delete(NumRaciones nr) {
        final int[] result = {0};
        ComidasRoomDatabase.databaseWriteExecutor.execute(() -> {
            result[0] = mPedidoDao.delete(nr);
        });
        return result[0];
    }
}
