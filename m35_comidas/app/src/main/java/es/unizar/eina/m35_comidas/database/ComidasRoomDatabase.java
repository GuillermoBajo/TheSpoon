package es.unizar.eina.m35_comidas.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.unizar.eina.m35_comidas.database.pedidos.Pedido;
import es.unizar.eina.m35_comidas.database.pedidos.PedidoDao;
import es.unizar.eina.m35_comidas.database.pedidos.NumRaciones;
import es.unizar.eina.m35_comidas.database.platos.Plato;
import es.unizar.eina.m35_comidas.database.platos.PlatoDao;

/**
 * Esta clase representa la base de datos de la aplicación Comidas.
 * Extiende la clase RoomDatabase de la biblioteca de persistencia de Room.
 */
@Database(entities = {Plato.class, Pedido.class, NumRaciones.class}, version = 1, exportSchema = false)
public abstract class ComidasRoomDatabase extends RoomDatabase {

    /**
     * Devuelve una instancia del objeto PlatoDao.
     *
     * @return Una instancia del objeto PlatoDao.
     */
    public abstract PlatoDao PlatoDao();

    /**
     * Devuelve una instancia del objeto PedidoDao.
     *
     * @return Una instancia del objeto PedidoDao.
     */
    public abstract PedidoDao PedidoDao();

    private static volatile ComidasRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Obtiene una instancia de la base de datos ComidasRoomDatabase.
     * Si la instancia no existe, se crea una nueva instancia utilizando el patrón Singleton.
     *
     * @param context El contexto de la aplicación.
     * @return Una instancia de la base de datos ComidasRoomDatabase.
     */
    public static ComidasRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ComidasRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ComidasRoomDatabase.class, "comidas_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    /**
     * Clase Callback que se utiliza para manejar eventos de creación de la base de datos.
     */
    private static Callback sRoomDatabaseCallback = new Callback() {
        /**
         * Método que se llama cuando la base de datos es creada por primera vez.
         * @param db La instancia de la base de datos.
         */
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Si desea mantener los datos a través de reinicios de la aplicación,
            // comente el siguiente bloque
            databaseWriteExecutor.execute(() -> {
                // Poblar la base de datos en segundo plano.
                // Si desea comenzar con más Platos, simplemente añádalos.
                PlatoDao dao1 = INSTANCE.PlatoDao();
                PedidoDao dao2 = INSTANCE.PedidoDao();
                dao1.deleteAll();
                dao2.deleteAll();

                Plato plato = new Plato("Tortilla de patatas", "Ñam ñam...", "Primero", 2.3);
                dao1.insert(plato);
                plato = new Plato("Jamon serrano", "Muy rico", "Segundo", 5.0);
                dao1.insert(plato);
                plato = new Plato("Aceite de oliva", "Un poco malo", "Tercero", 2.3);
                dao1.insert(plato);
                plato = new Plato("Zanahorias", "Muy rico", "Primero", 10.0);
                dao1.insert(plato);


                Pedido pedido;
                pedido = new Pedido("Dave", 627328473, "Solicitado", "12/12/2023", "22:30", 0.0);
                dao2.insert(pedido);
                pedido = new Pedido("Yoank", 976358592, "Solicitado", "12/12/2023", "20:30", 0.0);
                dao2.insert(pedido);
                pedido = new Pedido("Yofersen", 123456, "Recogido", "10/12/2023", "21:30", 0.0);
                dao2.insert(pedido);
                pedido = new Pedido("Andres", 976358592, "Preparado", "11/12/2023", "20:30", 0.0);
                dao2.insert(pedido);
            });
        }
    };
}