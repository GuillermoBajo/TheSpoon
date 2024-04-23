package es.unizar.eina.m35_comidas.ui.pedidos;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import es.unizar.eina.m35_comidas.database.platos.Plato;

/**
 * Clase que representa un ViewHolder para un pedido en un RecyclerView.
 * Implementa la interfaz View.OnCreateContextMenuListener para manejar el menú contextual.
 */

public class PlatoAAgnadirListAdapter extends ListAdapter<Plato, PlatoAAgnadirViewHolder> {
    private AgnadirPlatoAPedido ped;
    private int position;

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
     * Constructor de la clase PedidoListAdapter.
     *
     * @param diffCallback El callback para calcular las diferencias entre los elementos de la lista.
     * @param ped     La actividad asociada al adaptador.
     */
    public PlatoAAgnadirListAdapter(@NonNull DiffUtil.ItemCallback<Plato> diffCallback, AgnadirPlatoAPedido ped) {
        super(diffCallback);
        this.ped = ped;
    }

    @Override
    @NonNull
    public PlatoAAgnadirViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PlatoAAgnadirViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(PlatoAAgnadirViewHolder holder, int position) {
        Plato current = getItem(position);

        /** invoca el método bind del PedidoViewHolder para establecer el título del pedido en la vista.*/
        holder.bind(current.getTitulo(), current.getDescripcion(), current.getCategoria(), String.valueOf(current.getPrecio()));

        if(holder.mBotonAgnadir != null) {
            holder.mBotonAgnadir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ped.selectCantidad(current);
                }
            });
        }
    }

    /**
     * Obtiene el pedido actual en la posición especificada.
     *
     * @return El pedido actual en la posición especificada.
     */
    public Plato getCurrent() {
        return getItem(getPosition());
    }


    /**
     * Clase interna para calcular las diferencias entre los elementos de la lista de pedidos.
     */
    public static class PlatoDiff extends DiffUtil.ItemCallback<Plato> {

        /**
         * Comprueba si los elementos son los mismos.
         *
         * @param oldItem El antiguo pedido.
         * @param newItem El nuevo pedido.
         * @return true si los elementos son los mismos, false en caso contrario.
         */
        @Override
        public boolean areItemsTheSame(@NonNull Plato oldItem, @NonNull Plato newItem) {
            return oldItem.getTitulo() == newItem.getTitulo();
        }

        /**
         * Comprueba si los contenidos de los elementos son los mismos.
         *
         * @param oldItem El antiguo pedido.
         * @param newItem El nuevo pedido.
         * @return true si los contenidos de los elementos son los mismos, false en caso contrario.
         */
        @Override
        public boolean areContentsTheSame(@NonNull Plato oldItem, @NonNull Plato newItem) {
            return oldItem.getTitulo().equals(newItem.getTitulo()) && oldItem.getCategoria().equals(newItem.getCategoria()) &&
                    oldItem.getDescripcion().equals(newItem.getDescripcion()) && oldItem.getPrecio().equals(newItem.getPrecio());
        }
    }
}