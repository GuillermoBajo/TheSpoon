package es.unizar.eina.m35_comidas.ui.pedidos;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Arrays;
import java.util.List;

import es.unizar.eina.m35_comidas.database.pedidos.*;
import es.unizar.eina.m35_comidas.database.platos.Plato;

/**
 * Esta clase representa el ViewModel para la pantalla de pedidos.
 * Extiende la clase AndroidViewModel y se utiliza para manejar la lógica de la interfaz de usuario
 * relacionada con los pedidos.
 */
public class PedidoViewModel extends AndroidViewModel {

    private PedidoRepository mRepository;
    private LiveData<List<Pedido>> mAllPedidos;
    private LiveData<List<Plato>> mAllPlatosNoEnPedido;
    private LiveData<UnionPlatoPedido> mAllpedidosEnPedido;

    /**
     * Constructor de la clase PedidoViewModel.
     * Recibe una instancia de Application y se utiliza para inicializar el repositorio y la lista de pedidos.
     *
     * @param application La instancia de Application.
     */
    public PedidoViewModel(Application application) {
        super(application);
        mRepository = new PedidoRepository(application);
        mAllPedidos = mRepository.getAllPedidos();
    }

    /**
     * Este método ordena los pedidos según un criterio específico.
     * Actualiza la lista de pedidos después de ordenarlos.
     *
     * @param criterio El criterio de ordenación de los pedidos.
     */
    public void ordenar(String criterio, String filtro){
        mRepository.orderPed(criterio, filtro);
        mAllPedidos = mRepository.getAllPedidos();
    }

    /**
     * Obtiene un LiveData que contiene la unión de pedidos y pedidos asociados a un pedido específico.
     * @return LiveData de tipo UnionpedidoPedido.
     */
    public LiveData<UnionPlatoPedido> getPlatosFromPedido(){
        return mAllpedidosEnPedido;
    }

    /**
     * Obtiene un LiveData que contiene la lista de pedidos que no están asociados a un pedido específico.
     * @return LiveData de tipo List<pedido>.
     */
    public LiveData<List<Plato>> getPlatosNotFromPedido(){ return mAllPlatosNoEnPedido; }

    /**
     * Establece el LiveData mAllpedidosEnPedido con la unión de pedidos y pedidos asociados a un pedido específico.
     * @param pedidoId Identificador del pedido.
     */
    public void setPlatosFromPedido(int pedidoId) {
        mAllpedidosEnPedido = mRepository.getAllPlatosFromPedido(pedidoId);
    }

    /**
     * Establece el LiveData mAllpedidosNoEnPedido con la lista de pedidos que no están asociados a un pedido específico.
     * @param pedidoId Identificador del pedido.
     */
    public void setPlatosNotFromPedido(int pedidoId) {
        mAllPlatosNoEnPedido = mRepository.getPlatosNoEnPedido(pedidoId);
    }

    /**
     * Este método proporciona acceso a la lista de todos los pedidos como un objeto LiveData.
     * Los observadores pueden suscribirse a este LiveData para recibir actualizaciones cuando cambian los datos.
     *
     * @return La lista de todos los pedidos como un objeto LiveData.
     */
    LiveData<List<Pedido>> getAllPedidos() { return mAllPedidos; }

    /**
     * Este método se utiliza para insertar un nuevo pedido en la base de datos.
     *
     * @param pedido El pedido a insertar.
     * @return Identificador del pedido.
     */
    public long insert(Pedido pedido) {
        if (pedido != null) {
            if (pedido.getNombreCliente() == null) {
                throw new IllegalArgumentException("El nombre del cliente no es válido. Nombre nulo");
            }
            if (pedido.getNombreCliente().length() == 0) {
                throw new IllegalArgumentException("El nombre del cliente no es válido. No hay nombre");
            }

            if (pedido.getNumeroCliente() == null) {
                throw new IllegalArgumentException("El número del cliente no es válida. Numero nulo");
            }
            if (pedido.getNumeroCliente() <= 0) {
                throw new IllegalArgumentException("El número del cliente no es válida. Numero <= 0");
            }

            if (pedido.getEstado() == null) {
                throw new IllegalArgumentException("El estado del pedido no es válida. Estado nulo");
            }
            String[] estadosValidas = {"Solicitado", "Preparado", "Recogido"};
            if (pedido.getEstado() == null || !Arrays.asList(estadosValidas).contains(pedido.getEstado())) {
                throw new IllegalArgumentException("El estado del pedido no es válida. Estado no es Solicitado, Preparado, Recogido");
            }

            if (pedido.getFecha() == null) {
                throw new IllegalArgumentException("La fecha del pedido no es válida. Fecha nula");
            }
            if (!pedido.getFecha().matches("^\\d{2}/\\d{2}/\\d{4}$")) {
                throw new IllegalArgumentException("La fecha del pedido no es válida. Fecha no cumple el formato DD/MM/AAAA");
            }

            if (pedido.getHora() == null) {
                throw new IllegalArgumentException("La hora del pedido no es válida. Hora nula");
            }
            if (!pedido.getHora().matches("^\\d{2}:\\d{2}$")) {
                throw new IllegalArgumentException("La hora del pedido no es válida. Hora no cumple el formato HH:MM");
            }

            if (pedido.getPrecioPedido() == null) {
                throw new IllegalArgumentException("El precio del pedido no es valido. Precio nulo");
            }
            if (pedido.getPrecioPedido() < 0) {
                throw new IllegalArgumentException("El precio del pedido no es valido. Precio < 0");
            }

        } else {
            // Lanzar una excepción o manejar el error apropiadamente si el pedido es nulo
            throw new IllegalArgumentException("El pedido a insertar es nulo.");
        }

        return mRepository.insert(pedido);
    }

    /**
     * Este método se utiliza para actualizar un pedido existente en la base de datos.
     *
     * @param pedido El pedido a actualizar.
     * @return Identificador del pedido
     */
    public long update(Pedido pedido) {
        if (pedido != null) {
            if (pedido.getId() <= 0) {
                throw new IllegalArgumentException("El Id del pedido no es válido. Id <= 0");
            }

            if (pedido.getNombreCliente() == null) {
                throw new IllegalArgumentException("El nombre del cliente no es válido. Nombre nulo");
            }
            if (pedido.getNombreCliente().length() == 0) {
                throw new IllegalArgumentException("El nombre del cliente no es válido. No hay nombre");
            }

            if (pedido.getNumeroCliente() == null) {
                throw new IllegalArgumentException("El número del cliente no es válida. Numero nulo");
            }
            if (pedido.getNumeroCliente() <= 0) {
                throw new IllegalArgumentException("El número del cliente no es válida. Numero <= 0");
            }

            if (pedido.getEstado() == null) {
                throw new IllegalArgumentException("El estado del pedido no es válida. Estado nulo");
            }
            String[] estadosValidas = {"Solicitado", "Preparado", "Recogido"};
            if (pedido.getEstado() == null || !Arrays.asList(estadosValidas).contains(pedido.getEstado())) {
                throw new IllegalArgumentException("El estado del pedido no es válida. Estado no es Solicitado, Preparado, Recogido");
            }

            if (pedido.getFecha() == null) {
                throw new IllegalArgumentException("La fecha del pedido no es válida. Fecha nula");
            }
            if (!pedido.getFecha().matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
                throw new IllegalArgumentException("La fecha del pedido no es válida. Fecha no cumple el formato DD/MM/AAAA");
            }

            if (pedido.getHora() == null) {
                throw new IllegalArgumentException("La hora del pedido no es válida. Hora nula");
            }
            if (!pedido.getHora().matches("^\\d{2}:\\d{2}$")) {
                throw new IllegalArgumentException("La hora del pedido no es válida. Hora no cumple el formato HH:MM");
            }

            if (pedido.getPrecioPedido() == null) {
                throw new IllegalArgumentException("El precio del pedido no es valido. Precio nulo");
            }
            if (pedido.getPrecioPedido() < 0) {
                throw new IllegalArgumentException("El precio del pedido no es valido. Precio < 0");
            }

        } else {
            // Lanzar una excepción o manejar el error apropiadamente si el pedido es nulo
            throw new IllegalArgumentException("El pedido a insertar es nulo.");
        }
        
        return mRepository.update(pedido);
    }

    /**
     * Este método se utiliza para eliminar un pedido existente de la base de datos.
     *
     * @param pedido El pedido a eliminar.
     */
    public long delete(Pedido pedido) {
        if (pedido != null) {
            // Comprobación del campo Id para garantizar su validez
            if (pedido.getId() <= 0) {
                throw new IllegalArgumentException("El Id del pedido no es válido. Id <= 0");
            }
        } else {
            throw new IllegalArgumentException("El pedido a eliminar es nulo");
        }

        return mRepository.delete(pedido);
    }

    /**
     * Este método se utiliza para insertar un nuevo pedido en la base de datos.
     *
     * @param nr El pedido a insertar.
     */
    public void insert(NumRaciones nr) {
       mRepository.insert(nr);
    }

    /**
     * Este método se utiliza para actualizar un pedido existente en la base de datos.
     *
     * @param nr El pedido a actualizar.
     */
    public void update(NumRaciones nr) {
        mRepository.update(nr);
    }

    /**
     * Este método se utiliza para eliminar un pedido existente de la base de datos.
     *
     * @param nr El pedido a eliminar.
     */
    public void delete(NumRaciones nr) {
        mRepository.delete(nr);
    }
}

