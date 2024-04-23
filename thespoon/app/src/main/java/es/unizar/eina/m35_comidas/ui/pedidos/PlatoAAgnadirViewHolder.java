package es.unizar.eina.m35_comidas.ui.pedidos;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;

import es.unizar.eina.m35_comidas.R;

/**
 * Clase que representa un ViewHolder para un plato que se puede agregar a un pedido en un RecyclerView.
 * Implementa la interfaz View.OnCreateContextMenuListener para manejar el menú contextual.
 */
public class PlatoAAgnadirViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    private final TextView mTitulo;
    private final TextView mDescripcion;
    private final TextView mPrecio;
    private final TextView mCategoria;
    ImageButton mBotonAgnadir;

    /**
     * Constructor de la clase PlatoAAgnadirViewHolder.
     *
     * @param itemView La vista del elemento del RecyclerView.
     */
    private PlatoAAgnadirViewHolder(View itemView) {
        super(itemView);
        mTitulo = itemView.findViewById(R.id.tituloTextViewPP);
        mDescripcion = itemView.findViewById(R.id.descripcionTextViewPP);
        mPrecio = itemView.findViewById(R.id.precioTextViewPP);
        mCategoria = itemView.findViewById(R.id.categoriaTextViewPP);
        mBotonAgnadir = itemView.findViewById(R.id.botonAgnadirPP);

        itemView.setOnCreateContextMenuListener(this);
    }

    /**
     * Vincula los datos del plato actual al PlatoAAgnadirViewHolder.
     *
     * @param text1 El texto para el título del plato.
     * @param text2 El texto para la descripción del plato.
     * @param text3 El texto para la categoría del plato.
     * @param text4 El texto para el precio del plato.
     */
    public void bind(String text1, String text2, String text3, String text4) {
        mTitulo.setText(text1);

        // Limitar la longitud de la descripción
        String descripcionFinal = text2.length() > 90 ? text2.substring(0, 90) + "..." : text2;
        mDescripcion.setText(descripcionFinal);

        mCategoria.setText(text3);

        double precio = Double.parseDouble(text4);
        String precioFormateado = new DecimalFormat("#,##0.00").format(precio);
        mPrecio.setText("PVP: " + precioFormateado + "€");
    }

    /**
     * Crea y devuelve una nueva instancia de PlatoAAgnadirViewHolder.
     *
     * @param parent El ViewGroup padre en el que se inflará la vista.
     * @return Una nueva instancia de PlatoAAgnadirViewHolder.
     */
    static PlatoAAgnadirViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_itemplatosnoagnadidos, parent, false);
        return new PlatoAAgnadirViewHolder(view);
    }

    /**
     * Este método se llama cuando se crea el menú contextual para un elemento de la lista.
     *
     * @param menu     El menú contextual.
     * @param v        La vista asociada al menú contextual.
     * @param menuInfo Información adicional sobre el menú contextual.
     */
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        // El diseño no ofrece opciones al seleccionar un plato
    }
}
