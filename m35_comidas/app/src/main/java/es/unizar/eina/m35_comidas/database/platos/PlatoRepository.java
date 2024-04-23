package es.unizar.eina.m35_comidas.database.platos;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import es.unizar.eina.m35_comidas.database.ComidasRoomDatabase;
import es.unizar.eina.m35_comidas.database.pedidos.Pedido;


/**
 * Esta clase representa el repositorio de platos, que se encarga de interactuar con la base de datos
 * y proporcionar métodos para realizar operaciones CRUD en la tabla de platos.
 */
public class PlatoRepository {

    private PlatoDao mPlatoDao;
    private LiveData<List<Plato>> mAllPlatos;
    private final long TIMEOUT = 15000;

    /**
     * Ordena los platos en función del criterio especificado.
     *
     * @param criterio El criterio de ordenación de los platos.
     */
    public void order(String criterio) {
        SimpleSQLiteQuery query;
        switch(criterio){
            case "titulo ASC":
                query = new SimpleSQLiteQuery("SELECT * FROM plato ORDER BY titulo ASC");
                mAllPlatos = mPlatoDao.getOrderedPlatos(query);
                break;
            case "categoria ASC":
                query = new SimpleSQLiteQuery("SELECT * FROM plato ORDER BY categoria ASC");
                mAllPlatos = mPlatoDao.getOrderedPlatos(query);
                break;
            case "categoria ASC, titulo ASC":
                query = new SimpleSQLiteQuery("SELECT * FROM plato ORDER BY categoria ASC, titulo ASC");
                mAllPlatos = mPlatoDao.getOrderedPlatos(query);
                break;
        }
    }

    /**
     * Constructor de la clase PlatoRepository.
     * Recibe una instancia de la clase Application y utiliza PlatoRoomDatabase para
     * obtener una instancia de PlatoDao y la lista de todos los platos ordenados.
     *
     * @param application La instancia de la clase Application.
     */
    public PlatoRepository(Application application) {
        ComidasRoomDatabase db = ComidasRoomDatabase.getDatabase(application);
        mPlatoDao = db.PlatoDao();
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM plato ORDER BY titulo ASC");
        mAllPlatos = mPlatoDao.getOrderedPlatos(query);
    }

    /**
     * Obtiene todos los platos de la base de datos.
     *
     * @return LiveData que contiene la lista de todos los platos.
     */
    public LiveData<List<Plato>> getAllPlatos() {
        return mAllPlatos;
    }

    /**
     * Inserta un plato en la base de datos.
     *
     * @param plato El plato que se desea insertar.
     * @return El identificador del plato que se ha creado.
     */
    public long insert(Plato plato) {
        AtomicLong result = new AtomicLong();
        Semaphore resource = new Semaphore(0);
        ComidasRoomDatabase.databaseWriteExecutor.execute(() -> {
            long value = mPlatoDao.insert(plato);
            result.set(value);
            resource.release();
        });
        try {
            resource.tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.d("PlatoRepository", "InterruptedException: " + e.getMessage());
        }
        return result.get();
    }

    /**
     * Actualiza un plato en la base de datos.
     *
     * @param plato El plato que se desea actualizar.
     * @return El número de filas modificadas: 1 si el plato se ha actualizado correctamente,
     *         0 si no existe un plato con el mismo identificador o hay algún problema con los atributos.
     */
    public int update(Plato plato) {
        AtomicInteger result = new AtomicInteger();
        Semaphore resource = new Semaphore(0);
        ComidasRoomDatabase.databaseWriteExecutor.execute(() -> {
            result.set(mPlatoDao.update(plato));
            resource.release();
        });
        try {
            resource.tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.d("PlatoRepository", "InterruptedException: " + e.getMessage());
        }
        return result.get();
    }

    /**
     * Elimina un plato de la base de datos.
     *
     * @param plato El plato que se desea eliminar.
     * @return El número de filas eliminadas: 1 si el plato se ha eliminado correctamente,
     *         0 si no existe un plato con el mismo identificador o el identificador no es válido.
     */
    public int delete(Plato plato) {
        AtomicInteger result = new AtomicInteger();
        Semaphore resource = new Semaphore(0);
        ComidasRoomDatabase.databaseWriteExecutor.execute(() -> {
            result.set(mPlatoDao.delete(plato));
            resource.release();
        });
        try {
            resource.tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.d("PlatoRepository", "InterruptedException: " + e.getMessage());
        }
        return result.get();
    }
}
