package es.unizar.eina.m35_comidas.ui.pedidos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import es.unizar.eina.m35_comidas.R;
import es.unizar.eina.m35_comidas.database.pedidos.NumRaciones;
import es.unizar.eina.m35_comidas.database.pedidos.UnionPlatoPedido;
import es.unizar.eina.m35_comidas.database.platos.Plato;
import es.unizar.eina.m35_comidas.ui.platos.PlatoListAdapter;
import es.unizar.eina.m35_comidas.ui.pedidos.PlatoSingleton;

/**
 * Actividad para agregar platos a un pedido.
 */
public class AgnadirPlatoAPedido extends AppCompatActivity {

    RecyclerView RVagnadidos;
    RecyclerView RVNoagnadidos;
    PlatoPedidoListAdapter mAdapter;
    PlatoAAgnadirListAdapter mAdapter2;
    Button mConfirmar;

    /**
     * Método llamado cuando se crea la actividad.
     *
     * @param savedInstanceState Datos guardados del estado anterior.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agnadir_platos_a_pedido);

        // Inicialización de vistas y adaptadores
        RVagnadidos = findViewById(R.id.recyclerviewAgnadidosPlatos);
        RVNoagnadidos = findViewById(R.id.recyclerviewNoAgnadidosPlatos);
        mAdapter = new PlatoPedidoListAdapter(new PlatoPedidoListAdapter.PlatosPedidoDiff(), this);
        mAdapter2 = new PlatoAAgnadirListAdapter(new PlatoListAdapter.PlatoDiff(), this);
        RVagnadidos.setAdapter(mAdapter);
        RVNoagnadidos.setAdapter(mAdapter2);
        RVagnadidos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RVNoagnadidos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        PlatoSingleton ped = PlatoSingleton.getInstance();

        // Observadores para actualizar las listas de platos agregados y no agregados
        ped.getPlatosNoAgnadidosAfter().observe(this, p -> mAdapter2.submitList(p));
        ped.getPlatosAgnadidosAfter().observe(this, ps -> {
            List<PlatosPedido> adapter = new ArrayList<>();
            for (Plato plato : ps.platos) {
                for (NumRaciones racion : ps.cantidad) {
                    if (plato.getId() == racion.getPlatoId()) {
                        PlatosPedido nuevoP = new PlatosPedido(plato, racion);
                        adapter.add(nuevoP);
                    }
                }
            }
            mAdapter.submitList(adapter);
        });

        // Confirmar la selección de platos y finalizar la actividad
        mConfirmar = findViewById(R.id.botConfirmPlatos);
        LifecycleOwner owner = this;
        mConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                setResult(5, replyIntent);
                ped.getPlatosAgnadidosAfter().removeObservers(owner);
                ped.getPlatosNoAgnadidosAfter().removeObservers(owner);
                finish();
            }
        });
    }

    /**
     * Elimina un plato de la lista de platos agregados al pedido.
     *
     * @param current Plato a eliminar.
     */
    public void eliminarDeLista(PlatosPedido current) {
        Plato nuevoPlato = current.getPlato();
        PlatoSingleton ped = PlatoSingleton.getInstance();
        List<Plato> nuevaListaP = ped.getPlatosNoAgnadidosAfter().getValue();
        nuevaListaP.add(nuevoPlato);
        MutableLiveData<List<Plato>> nuevaLista = new MutableLiveData<>();
        nuevaLista.setValue(nuevaListaP);
        ped.setListaPlatosNoAgnadidosAfter(nuevaLista);
        LiveData<UnionPlatoPedido> act = ped.getPlatosAgnadidosAfter();
        UnionPlatoPedido camb = act.getValue();

        NumRaciones nuevoNumRaciones = current.getNumraciones();
        nuevoNumRaciones.setPedidoId(0);

        for (NumRaciones nr : camb.cantidad) {
            nr.setPedidoId(0);
        }

        camb.cantidad.remove(nuevoNumRaciones);
        camb.platos.remove(nuevoPlato);

        MutableLiveData<UnionPlatoPedido> MLDPedidoPlato = new MutableLiveData<>();
        MLDPedidoPlato.setValue(camb);
        ped.setListaPlatosAgnadidosAfter(MLDPedidoPlato);
        ped.getPlatosNoAgnadidosAfter().observe(this, PP -> {
            mAdapter2.submitList(PP);
        });

        mAdapter2.notifyDataSetChanged();
        actualizarUI(ped);
    }

    /**
     * Abre un cuadro de diálogo para seleccionar la cantidad de un plato.
     *
     * @param current Plato seleccionado.
     */
    public void selectCantidad(Plato current) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.seleccionar_cantidad, null);
        builder.setView(view);

        final TextView mCantidad = view.findViewById(R.id.num);
        final Button mMas = view.findViewById(R.id.botonMas);
        final Button mMenos = view.findViewById(R.id.botonMenos);
        final Button mConfirmar = view.findViewById(R.id.ConfBoton);
        final AlertDialog dialogo = builder.create();

        // Manejadores de eventos para incrementar o decrementar la cantidad
        mMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cantidad = Integer.parseInt(mCantidad.getText().toString());
                if (cantidad < 100) {
                    cantidad++;
                    mCantidad.setText(String.valueOf(cantidad));
                }
            }
        });

        mMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cantidad = Integer.parseInt(mCantidad.getText().toString());
                if (cantidad > 1) {
                    cantidad--;
                    mCantidad.setText(String.valueOf(cantidad));
                }
            }
        });

        // Confirmar la cantidad seleccionada
        mConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cantidadStr = mCantidad.getText().toString();
                int cantidad = Integer.parseInt(cantidadStr);
                dialogo.dismiss();
                agnadirPlatoALista(current, cantidad);
            }
        });
        dialogo.show();
    }

    /**
     * Agrega un plato a la lista de platos agregados al pedido.
     *
     * @param current  Plato seleccionado.
     * @param cantidad Cantidad seleccionada.
     */
    public void agnadirPlatoALista(Plato current, int cantidad) {
        PlatoSingleton ped = PlatoSingleton.getInstance();
        NumRaciones nr = new NumRaciones(0, current.getId(), cantidad);
        PlatosPedido nuevoPP = new PlatosPedido(current, nr);
        UnionPlatoPedido nuevoUPP = ped.getPlatosAgnadidosAfter().getValue();
        if (nuevoUPP == null) {
            nuevoUPP = new UnionPlatoPedido();
        }
        nuevoUPP.platos.add(current);
        nuevoUPP.cantidad.add(nr);

        MutableLiveData<UnionPlatoPedido> nuevaLista = new MutableLiveData<>();
        nuevaLista.setValue(nuevoUPP);
        ped.setListaPlatosAgnadidosAfter(nuevaLista);

        LiveData<List<Plato>> act = ped.getPlatosNoAgnadidosAfter();
        List<Plato> camb = act.getValue();
        camb.remove(current);
        MutableLiveData<List<Plato>> nMLD = new MutableLiveData<>();
        nMLD.setValue(camb);

        ped.setListaPlatosNoAgnadidosAfter(nMLD);
        actualizarUI(ped);
        ped.getPlatosAgnadidosAfter().removeObservers(this);
        ped.getPlatosNoAgnadidosAfter().observe(this, pedidosPlatos -> {
            mAdapter2.submitList(pedidosPlatos);
        });
        mAdapter2.notifyDataSetChanged();
        ped.getPlatosNoAgnadidosAfter().removeObservers(this);
    }

    /**
     * Actualiza la interfaz de usuario con las listas de platos.
     *
     * @param ped Singleton de platos.
     */
    private void actualizarUI(PlatoSingleton ped) {
        ped.getPlatosAgnadidosAfter().observe(this, PPL -> {
            List<PlatosPedido> adapter = new ArrayList<>();
            for (Plato plato : PPL.platos) {
                for (NumRaciones nr : PPL.cantidad) {
                    if (plato.getId() == nr.getPlatoId()) {
                        PlatosPedido nPP = new PlatosPedido(plato, nr);
                        adapter.add(nPP);
                    }
                }
            }
            mAdapter.submitList(adapter);
        });
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Método llamado cuando se presiona el botón de retroceso.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent replyIntent = new Intent();
        setResult(0, replyIntent);
        finish();
    }
}
