package es.unizar.eina.m35_comidas.ui.pedidos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import es.unizar.eina.m35_comidas.R;
import es.unizar.eina.m35_comidas.database.pedidos.NumRaciones;
import es.unizar.eina.m35_comidas.database.pedidos.Pedido;
import es.unizar.eina.m35_comidas.database.pedidos.UnionPlatoPedido;
import es.unizar.eina.m35_comidas.database.platos.Plato;
import es.unizar.eina.m35_comidas.ui.platos.PlatoEdit;
import es.unizar.eina.send.SendAbstractionImpl;

/** Fragmento de pedido */
public class  PedidoFragmento extends Fragment {
    private PedidoViewModel mPedidoViewModel;

    public static final int ACTIVITY_CREATEPED = 3;

    public static final int ACTIVITY_EDITPED = 4;

    public static final int ACTIVITY_AGNADIRPLATOS = 5;

    static final int INSERT_ID = Menu.FIRST + 3;
    static final int DELETE_ID = Menu.FIRST + 4;
    static final int EDIT_ID = Menu.FIRST + 5;

    RecyclerView mRecyclerView;

    PedidoListAdapter mAdapter;

    String filtro = "";

    // Establecemos un valor predeterminado
    String orden = "";

    /**
     * Método llamado para crear y devolver la jerarquía de vistas asociada con el fragmento.
     *
     * @param inflater           El objeto LayoutInflater que se utilizará para inflar la vista.
     * @param container          Si no es nulo, este es el grupo de vistas al que se
     *                           adjuntará la vista resultante.
     * @param savedInstanceState Si no es nulo, este fragmento se está reconstruyendo a partir de un
     *                           estado guardado previamente como se indica aquí.
     * @return La vista inflada del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_pedido, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview2);
        mAdapter = new PedidoListAdapter(new PedidoListAdapter.PedidoDiff(), requireActivity());

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mPedidoViewModel = new ViewModelProvider(this).get(PedidoViewModel.class);

        mPedidoViewModel.getAllPedidos().observe(getViewLifecycleOwner(), pedidos -> {
            mAdapter.submitList(pedidos);
        });

        Button ordButton = view.findViewById(R.id.ord2);
        ordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), ordButton);
                popupMenu.getMenuInflater().inflate(R.menu.context_menuordped, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if( R.id.menuOrderByNomCli == item.getItemId()){
                            orden = "nombreCliente ASC";
                            mPedidoViewModel.ordenar(orden, filtro);
                            mPedidoViewModel.getAllPedidos().removeObservers(getViewLifecycleOwner());
                            mPedidoViewModel.getAllPedidos().observe(getViewLifecycleOwner(), pedidos -> {
                                mAdapter.submitList(pedidos);});

                            return true;
                        }
                        if( R.id.menuOrderByNumCli == item.getItemId()){
                            orden = "numeroCliente ASC";
                            mPedidoViewModel.ordenar(orden, filtro);
                            mPedidoViewModel.getAllPedidos().removeObservers(getViewLifecycleOwner());
                            mPedidoViewModel.getAllPedidos().observe(getViewLifecycleOwner(), pedidos -> {
                                mAdapter.submitList(pedidos);});
                            return true;
                        }
                        if( R.id.menuOrderByFecha == item.getItemId()){
                            orden = "fecha ASC";
                            mPedidoViewModel.ordenar(orden, filtro);
                            mPedidoViewModel.getAllPedidos().removeObservers(getViewLifecycleOwner());
                            mPedidoViewModel.getAllPedidos().observe(getViewLifecycleOwner(), pedidos -> {
                                mAdapter.submitList(pedidos);});
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        Button filtButton = view.findViewById(R.id.filt);
        filtButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              PopupMenu popupMenu = new PopupMenu(getActivity(), filtButton);

              popupMenu.getMenuInflater().inflate(R.menu.context_menu_filtrarped, popupMenu.getMenu());
              popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                  @Override
                  public boolean onMenuItemClick(MenuItem item) {
                      if (R.id.menuFiltBySolicitado == item.getItemId()) {
                          filtro = "Solicitado";
                          mPedidoViewModel.ordenar(orden, filtro);
                          mPedidoViewModel.getAllPedidos().removeObservers(getViewLifecycleOwner());
                          mPedidoViewModel.getAllPedidos().observe(getViewLifecycleOwner(), pedidos -> {
                              mAdapter.submitList(pedidos);
                          });
                          return true;
                      }
                      if (R.id.menuFiltByPreparado == item.getItemId()) {
                          filtro = "Preparado";
                          mPedidoViewModel.ordenar(orden, filtro);
                          mPedidoViewModel.getAllPedidos().removeObservers(getViewLifecycleOwner());
                          mPedidoViewModel.getAllPedidos().observe(getViewLifecycleOwner(), pedidos -> {
                              mAdapter.submitList(pedidos);
                          });
                          return true;
                      }
                      if (R.id.menuFiltByRecogido == item.getItemId()) {
                          filtro = "Recogido";
                          mPedidoViewModel.ordenar(orden, filtro);
                          mPedidoViewModel.getAllPedidos().removeObservers(getViewLifecycleOwner());
                          mPedidoViewModel.getAllPedidos().observe(getViewLifecycleOwner(), pedidos -> {
                              mAdapter.submitList(pedidos);
                          });
                          return true;
                      }
                      if (R.id.menuFiltByNone == item.getItemId()) {
                          filtro = "";
                          mPedidoViewModel.ordenar(orden, filtro);
                          mPedidoViewModel.getAllPedidos().removeObservers(getViewLifecycleOwner());
                          mPedidoViewModel.getAllPedidos().observe(getViewLifecycleOwner(), pedidos -> {
                              mAdapter.submitList(pedidos);
                          });
                          return true;
                      }
                      return false;
                  }
              });
              popupMenu.show();
          }
      });


        Button createButton = view.findViewById(R.id.crePed);
        createButton.setOnClickListener(v -> {
            createPedido();
        });

        return view;
    }


    /**
     * Infla el menú de opciones en la barra de acciones (action bar) de la actividad.
     *
     * @param menu     El menú en el que se inflarán las opciones.
     * @param inflater El objeto MenuInflater que se utilizará para inflar el menú.
     */
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(2, INSERT_ID, Menu.NONE, "Crear pedido");
    }

    /**
     * Llamado cuando se crea el menú contextual asociado con la vista.
     *
     * @param menu     El menú contextual que se está creando.
     * @param v        La vista que originó el menú contextual.
     * @param menuInfo Información adicional sobre la vista (puede ser nulo).
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    /**
     * Método llamado cuando se selecciona un elemento del menú contextual.
     *
     * @param item El elemento del menú contextual seleccionado.
     * @return true si el evento ha sido consumido, false en caso contrario.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() != 2) { return super.onContextItemSelected(item); }

        if(mAdapter.getCurrentList().size() == 0) { return super.onContextItemSelected(item); }

        Pedido current = mAdapter.getCurrent();

        switch (item.getItemId()) {
            case DELETE_ID:
                Toast.makeText(getActivity().getApplicationContext(), "Borrando pedido de " + current.getNombreCliente(), Toast.LENGTH_LONG).show();
                mPedidoViewModel.delete(current);
                mPedidoViewModel.ordenar(orden, filtro);
                mPedidoViewModel.getAllPedidos().removeObservers(getViewLifecycleOwner());
                mPedidoViewModel.getAllPedidos().observe(getViewLifecycleOwner(), pedidos -> {
                    mAdapter.submitList(pedidos);});
                return true;
            case EDIT_ID:
                editPedido(current);
                mPedidoViewModel.ordenar(orden, filtro);
                mPedidoViewModel.getAllPedidos().removeObservers(getViewLifecycleOwner());
                mPedidoViewModel.getAllPedidos().observe(getViewLifecycleOwner(), pedidos -> {
                    mAdapter.submitList(pedidos);});
                return true;
            default:
                break;
        }

        return super.onContextItemSelected(item);
    }

    /**
     * Método llamado cuando se selecciona un elemento del menú de opciones.
     *
     * @param item El elemento del menú de opciones seleccionado.
     * @return true si el evento ha sido consumido, false en caso contrario.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                createPedido();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**Manejo de actividades secundariasj*/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**Extrae los datos adicionales (extras) pasados de la actividad secundaria.
         *  En este caso, estos extras podrían contener información sobre el plato creado o editado.*/
        Bundle extras = null;

        if(data != null) {
            extras = data.getExtras();
        } else {
            Log.d("onActivityResult", " data = null");
            return;
        }


        if (resultCode != -1) {

            Toast.makeText(
                    getActivity().getApplicationContext(),
                    "Pedido no guardado",
                    Toast.LENGTH_LONG).show();
        } else {

            switch (requestCode) {
                case ACTIVITY_CREATEPED:
                    /** Si la actividad secundaria fue la de creación (ACTIVITY_CREATE), crea una
                     * nueva instancia de la clase Pedido con los datos proporcionados por la actividad
                     * secundaria y la inserta en el ViewModel.*/
                    es.unizar.eina.m35_comidas.database.pedidos.Pedido newPedido = new es.unizar.eina.m35_comidas.database.pedidos.Pedido(extras.getString(PedidoEdit.PEDIDO_NOMBRECLIENTE)
                            , extras.getInt(PedidoEdit.PEDIDO_NUMEROCLIENTE)
                            , extras.getString(PedidoEdit.PEDIDO_ESTADO)
                            , extras.getString(PedidoEdit.PEDIDO_FECHA)
                            , extras.getString(PedidoEdit.PEDIDO_HORA)
                            , Double.parseDouble(convDouble(extras.getString(PedidoEdit.PEDIDO_PRECIO))));


                    int IDpedido = (int) mPedidoViewModel.insert(newPedido);
                    newPedido.setId(IDpedido);

                    // Llamada al método para el primer conjunto de datos
                    List<PlatosPedido> nuevosBorrar1 = new ArrayList<>();
                    List<PlatosPedido> nuevosAgnadir1 = new ArrayList<>();
                    getLists(nuevosAgnadir1, nuevosBorrar1);
                    actualizarPedidos(nuevosBorrar1, nuevosAgnadir1, newPedido, orden, filtro);

                    break;


                case ACTIVITY_EDITPED:
                    /** Si la actividad secundaria fue la de edición (ACTIVITY_EDIT), obtiene el ID
                     * del plato editado y crea una instancia de Pedido actualizada con los datos
                     * proporcionados. Luego, actualiza la nota en el ViewModel*/
                    int id = extras.getInt(PedidoEdit.PEDIDO_ID);
                    es.unizar.eina.m35_comidas.database.pedidos.Pedido updatedPedido = new es.unizar.eina.m35_comidas.database.pedidos.Pedido(extras.getString(PedidoEdit.PEDIDO_NOMBRECLIENTE)
                            , extras.getInt(PedidoEdit.PEDIDO_NUMEROCLIENTE)
                            , extras.getString(PedidoEdit.PEDIDO_ESTADO)
                            , extras.getString(PedidoEdit.PEDIDO_FECHA)
                            , extras.getString(PedidoEdit.PEDIDO_HORA)
                            , Double.parseDouble(convDouble(extras.getString(PedidoEdit.PEDIDO_PRECIO))));
                    updatedPedido.setId(id);
                    mPedidoViewModel.update(updatedPedido);

                    List<PlatosPedido> nuevosBorrar2 = new ArrayList<>();
                    List<PlatosPedido> nuevosAgnadir2 = new ArrayList<>();
                    getLists(nuevosAgnadir2, nuevosBorrar2);
                    mPedidoViewModel.ordenar(orden, filtro);
                    mPedidoViewModel.getAllPedidos().removeObservers(getViewLifecycleOwner());
                    mPedidoViewModel.getAllPedidos().observe(getViewLifecycleOwner(), pedidos -> {
                        mAdapter.submitList(pedidos);
                    });
                    actualizarPedidos(nuevosBorrar2, nuevosAgnadir2, updatedPedido, orden, filtro);
                    mPedidoViewModel.ordenar(orden, filtro);
                    mPedidoViewModel.getAllPedidos().removeObservers(getViewLifecycleOwner());
                    mPedidoViewModel.getAllPedidos().observe(getViewLifecycleOwner(), pedidos -> {
                        mAdapter.submitList(pedidos);
                    });
            }

        }
    }

    /**
     * Crea un nuevo pedido iniciando la actividad de edición de pedidos.
     * Utiliza un Intent para iniciar la actividad PedidoEdit con un código de solicitud ACTIVITY_CREATEPED.
     */
    private void createPedido() {
        /**Se utiliza para iniciar la actividad de edición de platos.*/
        Intent intent = new Intent(getActivity(), PedidoEdit.class);

        PlatoSingleton.deleteInstance();
        PlatoSingleton ped = PlatoSingleton.getInstance();

        mPedidoViewModel.setPlatosNotFromPedido(-1);
        ped.setListaPlatosNoAgnadidosAfter(mPedidoViewModel.getPlatosNotFromPedido());
        /** Inicia la actividad PlatoEdit utilizando el Intent y especifica un código de solicitud ACTIVITY_CREATE.*/
        startActivityForResult(intent, ACTIVITY_CREATEPED);
    }

    /**
     * Edita un pedido existente iniciando la actividad de edición de pedidos.
     * Utiliza un Intent para pasar los detalles del pedido actual a la actividad PedidoEdit
     * con un código de solicitud ACTIVITY_EDITPED.
     *
     * @param current El pedido actual que se va a editar.
     */
    private void editPedido(Pedido current) {
        /**Se utiliza para iniciar la actividad de edición de platos.*/
        Intent intent = new Intent(getActivity(), PedidoEdit.class);
        /**Añade extras al Intent con la clave PlatoEdit.PLATO_TITLE y los valores de la nota actual */
        intent.putExtra(PedidoEdit.PEDIDO_NOMBRECLIENTE, current.getNombreCliente());
        intent.putExtra(PedidoEdit.PEDIDO_NUMEROCLIENTE, current.getNumeroCliente().toString());
        intent.putExtra(PedidoEdit.PEDIDO_ESTADO, current.getEstado());
        intent.putExtra(PedidoEdit.PEDIDO_FECHA, current.getFecha());
        intent.putExtra(PedidoEdit.PEDIDO_HORA, current.getHora());
        intent.putExtra(PedidoEdit.PEDIDO_PRECIO, current.getPrecioPedido());
        intent.putExtra(PedidoEdit.PEDIDO_ID, current.getId());

        /** Inicia la actividad PlatoEdit utilizando el Intent y especifica un código de solicitud ACTIVITY_EDIT.*/
        PlatoSingleton.deleteInstance();
        PlatoSingleton ped = PlatoSingleton.getInstance();
        mPedidoViewModel.setPlatosFromPedido(-1);
        mPedidoViewModel.setPlatosNotFromPedido(-1);
        mPedidoViewModel.setPlatosFromPedido(current.getId());
        mPedidoViewModel.setPlatosNotFromPedido(current.getId());
        ped.setListaPlatosAgnadidosAfter(mPedidoViewModel.getPlatosFromPedido());
        ped.setListaPlatosNoAgnadidosAfter(mPedidoViewModel.getPlatosNotFromPedido());

        startActivityForResult(intent, ACTIVITY_EDITPED);
    }

    /**
     * Convierte una cadena que representa un precio a un formato numérico.
     *
     * @param precio La cadena que representa el precio.
     * @return La cadena con solo caracteres numéricos y el punto decimal.
     */
    private String convDouble(String precio){
        String toNumbers = precio.replaceAll("[^0-9.]", "");
        return toNumbers;
    }

    /**
     * Obtiene dos listas de platos antes y después de la modificación.
     *
     * @param LPP1 Lista de platos antes.
     * @param LPP2 Lista de platos después.
     */
    private void getLists(List<PlatosPedido> LPP1, List<PlatosPedido> LPP2) {
        PlatoSingleton ped = PlatoSingleton.getInstance();

        UnionPlatoPedido UPP = ped.getPlatosAgnadidos();
        List<PlatosPedido> listaAntes = new ArrayList<>();
        for (Plato plato : UPP.platos) {
            for (NumRaciones nr : UPP.cantidad) {
                if(plato.getId() == nr.getPlatoId()){
                    listaAntes.add(new PlatosPedido(plato, nr));
                }
            }
        }

        UnionPlatoPedido listaDesp1 = ped.getPlatosAgnadidosAfter().getValue();
        List<PlatosPedido> listaDesp2 = new ArrayList<>();
        if (listaDesp1 != null){
            for (Plato plato: listaDesp1.platos) {
                for (NumRaciones nr : listaDesp1.cantidad) {
                    if(plato.getId() == nr.getPlatoId()){
                        listaDesp2.add(new PlatosPedido(plato, nr));
                    }
                }
            }
        }


        // si contiene antes y no contiene ahora, lo añade a LPP2
        for (PlatosPedido antes : listaAntes) {
            if (!listaDesp2.contains(antes)) {
                LPP2.add(antes);
            }
        }

        // si contiene ahora y no contiene antes, lo añade a LPP1
        for (PlatosPedido despues : listaDesp2) {
            if (!listaAntes.contains(despues)) {
                LPP1.add(despues);
            }
        }
    }


    /**
     * Realiza operaciones comunes de actualización de pedidos utilizando listas de platos a agregar y borrar,
     * el pedido actual y los parámetros de orden y filtro.
     *
     * @param nuevosBorrar Lista de platos a borrar.
     * @param nuevosAgnadir Lista de platos a agregar.
     * @param pedido El pedido actual que se está actualizando.
     * @param orden El criterio de ordenamiento.
     * @param filtro El filtro aplicado.
     */
    private void actualizarPedidos(List<PlatosPedido> nuevosBorrar, List<PlatosPedido> nuevosAgnadir, Pedido pedido, String orden, String filtro) {
        List<NumRaciones> NRAgnadir = new ArrayList<>();
        List<NumRaciones> NRBorrar = new ArrayList<>();

        for (PlatosPedido item : nuevosBorrar) {
            NumRaciones aux = new NumRaciones(pedido.getId(), item.getPlato().getId(), item.getCantidad());
            NRBorrar.add(aux);
        }

        for (NumRaciones item : NRBorrar) {
            mPedidoViewModel.delete(item);
        }

        for (PlatosPedido item : nuevosAgnadir) {
            NumRaciones au = new NumRaciones(pedido.getId(), item.getPlato().getId(), item.getCantidad());
            NRAgnadir.add(au);
        }

        for (NumRaciones item : NRAgnadir) {
            mPedidoViewModel.insert(item);
        }

        mPedidoViewModel.ordenar(orden, filtro);
        mPedidoViewModel.getAllPedidos().removeObservers(getViewLifecycleOwner());
        mPedidoViewModel.getAllPedidos().observe(getViewLifecycleOwner(), pedidos -> {
            mAdapter.submitList(pedidos);
        });
    }
}
