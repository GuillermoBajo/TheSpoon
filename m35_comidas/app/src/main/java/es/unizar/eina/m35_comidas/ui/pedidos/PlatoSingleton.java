package es.unizar.eina.m35_comidas.ui.pedidos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import es.unizar.eina.m35_comidas.database.pedidos.UnionPlatoPedido;
import es.unizar.eina.m35_comidas.database.platos.Plato;

/**
 * Clase Singleton que gestiona la información de los platos agregados y no agregados a un pedido.
 * Utilizada para mantener la información entre actividades.
 */
public class PlatoSingleton {
    private static PlatoSingleton instance;

    private UnionPlatoPedido PlatosAgnadidos; // Platos que tiene el pedido al iniciar la actividad
    private LiveData<UnionPlatoPedido> PlatosAgnadidosAfter; // Platos que tiene el pedido después de la actividad
    private List<Plato> PlatosNoAgnadidos; // Platos que no tiene el pedido al iniciar la actividad
    private LiveData<List<Plato>> PlatosNoAgnadidosAfter; // Platos que no tiene el pedido después de la actividad

    /**
     * Constructor privado del Singleton.
     */
    private PlatoSingleton(){
        PlatosAgnadidos = new UnionPlatoPedido();
        PlatosAgnadidosAfter = new MutableLiveData<>();
        PlatosNoAgnadidos = new ArrayList<>();
        PlatosNoAgnadidosAfter = new MutableLiveData<>();
    }

    /**
     * Método para obtener la instancia única del Singleton.
     *
     * @return La instancia única del Singleton PlatoSingleton.
     */
    public static PlatoSingleton getInstance(){
        if(instance == null){
            instance = new PlatoSingleton();
        }
        return instance;
    }

    /**
     * Método para eliminar la instancia del Singleton, utilizado al salir de la aplicación.
     */
    public static void deleteInstance(){
        instance = null;
    }

    /**
     * Obtiene los platos agregados al pedido al iniciar la actividad.
     *
     * @return Los platos agregados al pedido al iniciar la actividad.
     */
    public UnionPlatoPedido getPlatosAgnadidos() {
        return PlatosAgnadidos;
    }

    /**
     * Obtiene los platos agregados al pedido después de la actividad.
     *
     * @return LiveData que contiene los platos agregados al pedido después de la actividad.
     */
    public LiveData<UnionPlatoPedido> getPlatosAgnadidosAfter() {
        return PlatosAgnadidosAfter;
    }

    /**
     * Obtiene los platos no agregados al pedido al iniciar la actividad.
     *
     * @return Los platos no agregados al pedido al iniciar la actividad.
     */
    public List<Plato> getPlatosNoAgnadidos() {
        return PlatosNoAgnadidos;
    }

    /**
     * Obtiene los platos no agregados al pedido después de la actividad.
     *
     * @return LiveData que contiene los platos no agregados al pedido después de la actividad.
     */
    public LiveData<List<Plato>> getPlatosNoAgnadidosAfter() {
        return PlatosNoAgnadidosAfter;
    }

    /**
     * Establece la lista de platos agregados al pedido al iniciar la actividad.
     *
     * @param PlatosAgnadidos La lista de platos agregados al pedido al iniciar la actividad.
     */
    public void setListaPlatosAgnadidos(UnionPlatoPedido PlatosAgnadidos) {
        this.PlatosAgnadidos = PlatosAgnadidos;
    }

    /**
     * Establece la lista de platos agregados al pedido después de la actividad.
     *
     * @param PlatosAgnadidosAfter LiveData que contiene la lista de platos agregados al pedido después de la actividad.
     */
    public void setListaPlatosAgnadidosAfter(LiveData<UnionPlatoPedido> PlatosAgnadidosAfter) {
        this.PlatosAgnadidosAfter = PlatosAgnadidosAfter;
    }

    /**
     * Establece la lista de platos no agregados al pedido al iniciar la actividad.
     *
     * @param PlatosNoAgnadidos La lista de platos no agregados al pedido al iniciar la actividad.
     */
    public void setListaPlatosNoAgnadidos(List<Plato> PlatosNoAgnadidos) {
        this.PlatosNoAgnadidos = PlatosNoAgnadidos;
    }

    /**
     * Establece la lista de platos no agregados al pedido después de la actividad.
     *
     * @param PlatosNoAgnadidosAfter LiveData que contiene la lista de platos no agregados al pedido después de la actividad.
     */
    public void setListaPlatosNoAgnadidosAfter(LiveData<List<Plato>> PlatosNoAgnadidosAfter) {
        this.PlatosNoAgnadidosAfter = PlatosNoAgnadidosAfter;
    }
}
