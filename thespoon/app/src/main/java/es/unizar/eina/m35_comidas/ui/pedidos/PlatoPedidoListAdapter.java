package es.unizar.eina.m35_comidas.ui.pedidos;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.google.android.material.button.MaterialButton;

import es.unizar.eina.m35_comidas.R;
import es.unizar.eina.m35_comidas.database.pedidos.Pedido;
import es.unizar.eina.send.SendAbstractionImpl;

/**
 * Adaptador para la lista de platos en un pedido.
 * Extiende ListAdapter y utiliza un ViewHolder personalizado (PlatoPedidoViewHolder).
 */
public class PlatoPedidoListAdapter extends ListAdapter<PlatosPedido, PlatoPedidoViewHolder> {
    private AgnadirPlatoAPedido ped; // Instancia de la actividad de añadir plato a pedido
    private int position; // Posición actual del adaptador
    private boolean boton; // Indica si se debe incluir un botón en el ViewHolder

    /**
     * Obtiene la posición actual del adaptador.
     *
     * @return La posición actual del adaptador.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Establece la posición actual del adaptador.
     *
     * @param position La posición a establecer.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Constructor de la clase PlatoPedidoListAdapter.
     *
     * @param diffCallback El callback para calcular las diferencias entre los elementos de la lista.
     * @param ped          La actividad asociada al adaptador.
     */
    public PlatoPedidoListAdapter(@NonNull DiffUtil.ItemCallback<PlatosPedido> diffCallback, AgnadirPlatoAPedido ped) {
        super(diffCallback);
        if (ped != null) {
            this.ped = ped;
            boton = true;
        } else {
            boton = false;
        }
    }

    /**
     * Crea y devuelve un nuevo PlatoPedidoViewHolder.
     *
     * @param parent   El ViewGroup padre en el que se añadirá la vista.
     * @param viewType El tipo de vista.
     * @return El PlatoPedidoViewHolder creado.
     */
    @Override
    public PlatoPedidoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return PlatoPedidoViewHolder.create(parent, boton);
    }

    /**
     * Vincula los datos del plato actual al PlatoPedidoViewHolder.
     *
     * @param holder   El PlatoPedidoViewHolder en el que se mostrarán los datos.
     * @param position La posición del plato en la lista.
     */
    @Override
    public void onBindViewHolder(PlatoPedidoViewHolder holder, int position) {
        PlatosPedido current = getItem(position);

        // Invoca el método bind del PlatoPedidoViewHolder para establecer los datos del plato en la vista.
        holder.bind(current.getNombre(), current.getCategoria(), String.valueOf(current.getCantidad()), String.valueOf(current.getPrecio()));

        // Configura el listener del botón para eliminar el plato si es necesario.
        if (boton) {
            holder.mBotQuitarPlato.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ped.eliminarDeLista(current);
                }
            });
        }
    }

    /**
     * Obtiene el plato actual en la posición especificada.
     *
     * @return El plato actual en la posición especificada.
     */
    public PlatosPedido getCurrent() {
        return getItem(getPosition());
    }

    /**
     * Clase interna para calcular las diferencias entre los elementos de la lista de platos.
     */
    public static class PlatosPedidoDiff extends DiffUtil.ItemCallback<PlatosPedido> {

        /**
         * Comprueba si los elementos son los mismos.
         *
         * @param oldItem El antiguo plato.
         * @param newItem El nuevo plato.
         * @return true si los elementos son los mismos, false en caso contrario.
         */
        @Override
        public boolean areItemsTheSame(@NonNull PlatosPedido oldItem, @NonNull PlatosPedido newItem) {
            return oldItem.getNombre().equals(newItem.getNombre());
        }

        /**
         * Comprueba si los contenidos de los elementos son los mismos.
         *
         * @param oldItem El antiguo plato.
         * @param newItem El nuevo plato.
         * @return true si los contenidos de los elementos son los mismos, false en caso contrario.
         */
        @Override
        public boolean areContentsTheSame(@NonNull PlatosPedido oldItem, @NonNull PlatosPedido newItem) {
            return oldItem.getCantidad() == newItem.getCantidad() && oldItem.getPrecio() == newItem.getPrecio();
        }
    }
}
