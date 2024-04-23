package es.unizar.eina.m35_comidas.ui.pedidos;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

import es.unizar.eina.m35_comidas.R;

/**
 * Clase que representa un ViewHolder para un pedido en un RecyclerView.
 * Implementa la interfaz View.OnCreateContextMenuListener para manejar el menú contextual.
 */
class PedidoViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    // Declaraciones de vistas
    private final TextView mNomCliTextView;
    private final TextView mNumCliTextView;
    private final TextView mEstadoTextView;
    private final TextView mFechaTextView;
    private final TextView mPrecioPedTextView;

    /**
     * Constructor de la clase PedidoViewHolder.
     * @param itemView La vista del elemento del RecyclerView.
     */
    private PedidoViewHolder(View itemView) {
        super(itemView);
        // Inicialización de vistas mediante sus identificadores
        mNomCliTextView = itemView.findViewById(R.id.nomCliTextView);
        mNumCliTextView = itemView.findViewById(R.id.numCliTextView);
        mEstadoTextView = itemView.findViewById(R.id.estadoTextView);
        mFechaTextView = itemView.findViewById(R.id.fechaTextView);
        mPrecioPedTextView = itemView.findViewById(R.id.precioPedTextView);

        // Configuración del listener para el menú contextual
        itemView.setOnCreateContextMenuListener(this);
    }

    /**
     * Método para enlazar los datos del pedido con las vistas del ViewHolder.
     * @param text1 El texto para el nombre del cliente.
     * @param text2 El texto para el número de cliente.
     * @param text3 El texto para el estado del pedido.
     * @param text4 El texto para la fecha del pedido.
     * @param text5 El texto para el día del pedido.
     * @param text6 El texto para el costo del pedido.
     */
    public void bind(String text1, String text2, String text3, String text4, String text5, String text6) {
        // Establecer los textos en las vistas
        mNomCliTextView.setText(text1);
        mNumCliTextView.setText("+34 " + text2);
        mEstadoTextView.setText("ESTADO: " +  text3);
        mFechaTextView.setText(text5 + " " + text4);

        // Formatear el precio y establecerlo en la vista
        double precio = Double.parseDouble(text6);
        String precioFormateado = new DecimalFormat("#,##0.00").format(precio);
        mPrecioPedTextView.setText("Coste: " + precioFormateado + "€");
    }

    /**
     * Método estático para crear una instancia de PedidoViewHolder.
     * @param parent El ViewGroup padre en el que se inflará la vista.
     * @return Una nueva instancia de PedidoViewHolder.
     */
    static PedidoViewHolder create(ViewGroup parent) {
        // Inflar la vista del elemento del RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_itempedido, parent, false);
        return new PedidoViewHolder(view);
    }

    /**
     * Este método se llama cuando se crea el menú contextual para un elemento de la lista.
     * @param menu El menú contextual.
     * @param v La vista asociada al menú contextual.
     * @param menuInfo Información adicional sobre el menú contextual.
     */
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        // Añadir opciones de editar y eliminar al menú contextual
        menu.add(2, PedidoFragmento.EDIT_ID, Menu.NONE, R.string.menuPed_edit);
        menu.add(2, PedidoFragmento.DELETE_ID, Menu.NONE, R.string.menuPed_delete);
    }
}

