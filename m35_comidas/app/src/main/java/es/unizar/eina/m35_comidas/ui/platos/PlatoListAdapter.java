package es.unizar.eina.m35_comidas.ui.platos;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import es.unizar.eina.m35_comidas.database.platos.Plato;

/**PlatoListAdapter, utilizado en una RecyclerView para mostrar una lista de platos (Plato)*/
/**
 * Adaptador personalizado para la lista de platos.
 */
public class PlatoListAdapter extends ListAdapter<Plato, PlatoViewHolder> {
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
     * Establece la posición del adaptador.
     *
     * @param position La posición a establecer.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Constructor de la clase PlatoListAdapter.
     *
     * @param diffCallback El objeto DiffUtil.ItemCallback utilizado para comparar los elementos de la lista.
     */
    public PlatoListAdapter(@NonNull DiffUtil.ItemCallback<Plato> diffCallback) {
        super(diffCallback);
    }

    /**
     * Crea y devuelve un nuevo objeto PlatoViewHolder.
     *
     * @param parent   El ViewGroup al que se añadirá la vista.
     * @param viewType El tipo de vista.
     * @return Un nuevo objeto PlatoViewHolder.
     */
    @Override
    public PlatoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return PlatoViewHolder.create(parent);
    }

    /**
     * Obtiene el plato actual en la posición especificada.
     *
     * @return El plato actual en la posición especificada.
     */
    public Plato getCurrent() {
        return getItem(getPosition());
    }

    /**
     * Vincula los datos del plato actual al PlatoViewHolder y establece el título del plato en la vista.
     *
     * @param holder   El PlatoViewHolder en el que se vincularán los datos.
     * @param position La posición del plato en la lista.
     */
    @Override
    public void onBindViewHolder(PlatoViewHolder holder, int position) {

        Plato current = getItem(position);

        /** invoca el método bind del PlatoViewHolder para establecer el título del plato en la vista.*/
        holder.bind(current.getTitulo(),  current.getDescripcion(), current.getCategoria(),  current.getPrecio().toString());


        /** Cuando se realiza una pulsación larga, se establece la posición del elemento (position)
         *  utilizando setPosition.*/
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });
    }


    public static class PlatoDiff extends DiffUtil.ItemCallback<Plato> {

        /**
         * Compara si los elementos antiguos y nuevos son los mismos.
         *
         * @param oldItem El elemento antiguo.
         * @param newItem El elemento nuevo.
         * @return true si los elementos son los mismos, false en caso contrario.
         */
        @Override
        public boolean areItemsTheSame(@NonNull Plato oldItem, @NonNull Plato newItem) {
            //android.util.Log.d ( "PlatoDiff" , "areItemsTheSame " + oldItem.getId() + " vs " + newItem.getId() + " " +  (oldItem.getId() == newItem.getId()));
            return oldItem.getId() == newItem.getId();
        }

        /**
         * Compara si los contenidos de los elementos antiguos y nuevos son los mismos.
         *
         * @param oldItem El elemento antiguo.
         * @param newItem El elemento nuevo.
         * @return true si los contenidos son los mismos, false en caso contrario.
         */
        @Override
        public boolean areContentsTheSame(@NonNull Plato oldItem, @NonNull Plato newItem) {
            return oldItem.getTitulo().equals(newItem.getTitulo()) && oldItem.getCategoria().equals(newItem.getCategoria()) &&
                    oldItem.getDescripcion().equals(newItem.getDescripcion()) && oldItem.getPrecio().equals(newItem.getPrecio());
        }
    }
}
