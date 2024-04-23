package es.unizar.eina.m35_comidas.ui.pedidos;

import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;

import es.unizar.eina.m35_comidas.R;
import es.unizar.eina.m35_comidas.database.pedidos.Pedido;
import es.unizar.eina.m35_comidas.database.platos.Plato;
import es.unizar.eina.send.SendAbstractionImpl;

/**
 * Adaptador para la lista de pedidos.
 */
public class PedidoListAdapter extends ListAdapter<Pedido, PedidoViewHolder> {
    // Declaraciones de variables
    private SendAbstractionImpl SAImpl; // Implementación para enviar notificaciones
    private int position; // Posición actual del adaptador
    private Activity mActivity; // Referencia a la actividad asociada

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
     * @param activity     La actividad asociada al adaptador.
     */
    public PedidoListAdapter(@NonNull DiffUtil.ItemCallback<Pedido> diffCallback, Activity activity) {
        super(diffCallback);
        mActivity = activity; // Inicializa la referencia de la actividad
    }

    /**
     * Crea y devuelve un nuevo PedidoViewHolder.
     *
     * @param parent   El ViewGroup padre en el que se añadirá la vista.
     * @param viewType El tipo de vista.
     * @return El PedidoViewHolder creado.
     */
    @Override
    public PedidoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return PedidoViewHolder.create(parent);
    }

    /**
     * Vincula los datos del pedido actual al PedidoViewHolder.
     *
     * @param holder   El PedidoViewHolder en el que se mostrarán los datos.
     * @param position La posición del pedido en la lista.
     */
    @Override
    public void onBindViewHolder(PedidoViewHolder holder, int position) {
        Pedido current = getItem(position);
        // Invoca el método bind del PedidoViewHolder para establecer los datos del pedido en la vista
        holder.bind(current.getNombreCliente(), current.getNumeroCliente().toString(), current.getEstado(),
         current.getFecha(), current.getHora(), current.getPrecioPedido().toString());

        MaterialButton botNotificarButton = holder.itemView.findViewById(R.id.botNotificar);
        // Configura un listener para el botón de notificación
        botNotificarButton.setOnClickListener(v -> showPopupMenu(v, current, mActivity));

        // Configura un listener para la pulsación larga
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });
    }

    /**
     * Obtiene el pedido actual en la posición especificada.
     *
     * @return El pedido actual en la posición especificada.
     */
    public Pedido getCurrent() {
        return getItem(getPosition());
    }

    /**
     * Muestra el menú contextual para notificar al cliente del pedido.
     *
     * @param view     La vista que activó el menú contextual.
     * @param pedido   El pedido asociado al elemento de la lista.
     * @param activity La actividad asociada al adaptador.
     */
    private void showPopupMenu(View view, Pedido pedido, Activity activity) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.context_menu_notificar);

        popupMenu.setOnMenuItemClickListener(item -> {
            double precio = pedido.getPrecioPedido();
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            String precioFormateado = decimalFormat.format(precio);

            String msj = "Estimado " + pedido.getNombreCliente() + ", su pedido realizado el " + pedido.getFecha() +
                    " a las " + pedido.getHora() + " se encuentra en estado " + pedido.getEstado().toUpperCase() +
                     ".\nEl precio total del mismo es de " + precioFormateado + "€";

            // Manejar la selección de opciones del menú contextual
            if (item.getItemId() == R.id.botSMS) {
                SAImpl = new SendAbstractionImpl(activity, "SMS");
                SAImpl.send(pedido.getNumeroCliente().toString(), msj);
                return true;
            } else if (item.getItemId() == R.id.botWhatsApp) {
                SAImpl = new SendAbstractionImpl(activity, "WhatsApp");
                SAImpl.send(pedido.getNumeroCliente().toString(), msj);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    /**
     * Clase interna para calcular las diferencias entre los elementos de la lista de pedidos.
     */
    public static class PedidoDiff extends DiffUtil.ItemCallback<Pedido> {

        /**
         * Comprueba si los elementos son los mismos.
         *
         * @param oldItem El antiguo pedido.
         * @param newItem El nuevo pedido.
         * @return true si los elementos son los mismos, false en caso contrario.
         */
        @Override
        public boolean areItemsTheSame(@NonNull Pedido oldItem, @NonNull Pedido newItem) {
            return oldItem.getId() == newItem.getId();
        }

        /**
         * Comprueba si los contenidos de los elementos son los mismos.
         *
         * @param oldItem El antiguo pedido.
         * @param newItem El nuevo pedido.
         * @return true si los contenidos de los elementos son los mismos, false en caso contrario.
         */
        @Override
        public boolean areContentsTheSame(@NonNull Pedido oldItem, @NonNull Pedido newItem) {
            return oldItem.getNombreCliente().equals(newItem.getNombreCliente()) && oldItem.getNumeroCliente().equals(newItem.getNumeroCliente()) &&
                    oldItem.getEstado().equals(newItem.getEstado()) && oldItem.getPrecioPedido().equals(newItem.getPrecioPedido());
        }
    }
}
