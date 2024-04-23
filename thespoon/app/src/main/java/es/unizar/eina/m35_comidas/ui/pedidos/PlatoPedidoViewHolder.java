package es.unizar.eina.m35_comidas.ui.pedidos;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;

import es.unizar.eina.m35_comidas.R;

/**
 * Clase que representa un ViewHolder para un plato en un RecyclerView.
 * Implementa la interfaz View.OnCreateContextMenuListener para manejar el menú contextual.
 */
class PlatoPedidoViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    private final TextView mNombre; // TextView para el nombre del plato.
    private final TextView mPrecio; // TextView para el precio del plato.
    private final TextView mCantidad; // TextView para la cantidad del plato.
    private final TextView mCategoria; // TextView para la categoría del plato.
    ImageButton mBotQuitarPlato; // ImageButton para quitar el plato.

    /**
     * Constructor de la clase PlatoPedidoViewHolder.
     *
     * @param itemView La vista del elemento del RecyclerView.
     */
    private PlatoPedidoViewHolder(View itemView) {
        super(itemView);
        mNombre = itemView.findViewById(R.id.tituloTextViewPP);
        mPrecio = itemView.findViewById(R.id.precioTextViewPP);
        mCantidad = itemView.findViewById(R.id.cantidadTextViewPP);
        mCategoria = itemView.findViewById(R.id.categoriaTextViewPP);
        mBotQuitarPlato = itemView.findViewById(R.id.botonEliminarPP);

        // Establece el listener para el menú contextual.
        itemView.setOnCreateContextMenuListener(this);
    }

    /**
     * Método para enlazar los datos del plato con las vistas del ViewHolder.
     *
     * @param text1 El texto para el nombre del plato.
     * @param text2 El texto para la categoría del plato.
     * @param text3 El texto para la cantidad del plato.
     * @param text4 El texto para el precio del plato.
     */
    public void bind(String text1, String text2, String text3, String text4) {
        mNombre.setText(text1);
        mCategoria.setText(text2);
        mCantidad.setText("Cantidad: " + text3 + "uds");

        // Formatea el precio y lo muestra en el TextView.
        double precio = Double.parseDouble(text4);
        String precioFormateado = new DecimalFormat("#,##0.00").format(precio);
        mPrecio.setText("PVP: " + precioFormateado + "€");
    }

    /**
     * Método estático para crear una instancia de PlatoPedidoViewHolder.
     *
     * @param parent El ViewGroup padre en el que se inflará la vista.
     * @param boton  Indica si se debe incluir un botón en el ViewHolder.
     * @return Una nueva instancia de PlatoPedidoViewHolder.
     */
    static PlatoPedidoViewHolder create(ViewGroup parent, boolean boton) {
        // Infla la vista según la presencia del botón.
        View view = LayoutInflater.from(parent.getContext()).inflate(
                boton ? R.layout.recyclerview_itemplatosagnadidos : R.layout.recyclerview_itemplatosagnadidosnoboton,
                parent,
                false
        );
        return new PlatoPedidoViewHolder(view);
    }

    /**
     * Este método se llama cuando se crea el menú contextual para un elemento de la lista.
     *
     * @param menu     El menú contextual.
     * @param v        La vista asociada al menú contextual.
     * @param menuInfo Información adicional sobre el menú contextual.
     */
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // El diseño no ofrece opciones al seleccionar un plato
    }
}
