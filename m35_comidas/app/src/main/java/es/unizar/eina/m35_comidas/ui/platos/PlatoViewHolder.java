package es.unizar.eina.m35_comidas.ui.platos;

        import android.view.ContextMenu;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.recyclerview.widget.RecyclerView;

        import es.unizar.eina.m35_comidas.R;
        import java.text.DecimalFormat;

/**se utiliza para representar visualmente un elemento de la lista en un RecyclerView*/
/**
 * Clase que representa un ViewHolder para mostrar un plato en un RecyclerView.
 * Implementa la interfaz View.OnCreateContextMenuListener para manejar el menú contextual.
 */
class PlatoViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    private final TextView mTituloTextView;
    private final TextView mDescripcionTextView;
    private final TextView mCategoriaTextView;
    private final TextView mPrecioTextView;

    /**
     * Constructor de la clase PlatoViewHolder.
     * @param itemView La vista del elemento del RecyclerView.
     */
    private PlatoViewHolder(View itemView) {
        super(itemView);
        mTituloTextView = itemView.findViewById(R.id.tituloTextView);
        mDescripcionTextView = itemView.findViewById(R.id.descripcionTextView);
        mCategoriaTextView = itemView.findViewById(R.id.categoriaTextView);
        mPrecioTextView = itemView.findViewById(R.id.precioTextView);

        itemView.setOnCreateContextMenuListener(this);
    }

    /**
     * Método para enlazar los datos del plato con las vistas del ViewHolder.
     * @param text1 El título del plato.
     * @param text2 La descripción del plato.
     * @param text3 La categoría del plato.
     * @param text4 El precio del plato.
     */
    public void bind(String text1, String text2, String text3, String text4) {
        mTituloTextView.setText(text1);

        // Limitar la descripción a 50 caracteres
        String descripcionFinal = text2.length() > 90 ? text2.substring(0, 90) + "..." : text2;
        mDescripcionTextView.setText(descripcionFinal);

        mCategoriaTextView.setText(text3);

        double precio = Double.parseDouble(text4);
        String precioFormateado = new DecimalFormat("#,##0.00").format(precio);
        mPrecioTextView.setText("PVP: " + precioFormateado + "€");
    }

    /**
     * Método estático para crear una instancia de PlatoViewHolder.
     * @param parent El ViewGroup padre.
     * @return Una instancia de PlatoViewHolder.
     */
    static PlatoViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new PlatoViewHolder(view);
    }

    /**
     * Este método se llama cuando se crea el menú contextual para un elemento de la lista.
     * @param menu El menú contextual.
     * @param v La vista asociada al menú contextual.
     * @param menuInfo Información adicional sobre el menú contextual.
     */
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        /**Añade opciones de editar y eliminar*/
        menu.add(1, PlatoFragmento.EDIT_ID, Menu.NONE, R.string.menu_edit);
        menu.add(1, PlatoFragmento.DELETE_ID, Menu.NONE, R.string.menu_delete);
    }
}
