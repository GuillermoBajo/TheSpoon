package es.unizar.eina.m35_comidas.ui.platos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import es.unizar.eina.m35_comidas.R;
import es.unizar.eina.m35_comidas.database.platos.Plato;

/** Pantalla principal de la aplicación Plato */
/**
 * Fragmento que muestra una lista de platos.
 */
public class PlatoFragmento extends Fragment {
    private PlatoViewModel mPlatoViewModel;

    public static final int ACTIVITY_CREATE = 1;
    public static final int ACTIVITY_EDIT = 2;

    static final int INSERT_ID = Menu.FIRST;
    static final int DELETE_ID = Menu.FIRST + 1;
    static final int EDIT_ID = Menu.FIRST + 2;
    static final int ORD_NOMBRE = Menu.FIRST + 6;
    static final int ORD_CATEGORIA = Menu.FIRST + 7;
    static final int ORD_AMBAS = Menu.FIRST + 8;

    RecyclerView mRecyclerView;

    PlatoListAdapter mAdapter;


    /**
    * Método llamado para crear y devolver la vista del fragmento.
    *
    * @param inflater           El LayoutInflater utilizado para inflar la vista.
    * @param container          El contenedor padre en el que se debe inflar la vista.
    * @param savedInstanceState El estado anteriormente guardado del fragmento.
    * @return La vista inflada del fragmento.
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_plato, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mAdapter = new PlatoListAdapter(new PlatoListAdapter.PlatoDiff());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mPlatoViewModel = new ViewModelProvider(this).get(PlatoViewModel.class);

        mPlatoViewModel.getAllPlatos().observe(getViewLifecycleOwner(), platos -> {
            mAdapter.submitList(platos);
        });

        Button ord = view.findViewById(R.id.ord);
        registerForContextMenu(ord);

        Button createPlateButton = view.findViewById(R.id.crePlat);
        createPlateButton.setOnClickListener(vist -> {
            createPlato();
        });

        return view;
    }

    /**
     * Crea el menú de opciones en la barra de acciones.
     * @param menu El menú de opciones.
     * @param inflater El inflador de menús.
     */
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(1, INSERT_ID, Menu.NONE, "Crear plato");
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
        menu.add(1, PlatoFragmento.ORD_NOMBRE, Menu.NONE, "Ordenar por nombre");
        menu.add(1, PlatoFragmento.ORD_CATEGORIA, Menu.NONE, "Ordenar por categoría");
        menu.add(1, PlatoFragmento.ORD_AMBAS, Menu.NONE, "Ordenar por ambas");
    }

    /**
     * Maneja las opciones seleccionadas en el menú contextual.
     * @param item El elemento de menú seleccionado.
     * @return true si el elemento de menú se maneja correctamente, false de lo contrario.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() != 1) { return super.onContextItemSelected(item); }

        Plato current = mAdapter.getCurrent();

        switch (item.getItemId()) {
            case DELETE_ID:
                Toast.makeText(getActivity().getApplicationContext(), "Borrando " + current.getTitulo(), Toast.LENGTH_LONG).show();
                mPlatoViewModel.delete(current);
                return true;
            case EDIT_ID:
                editPlato(current);
                return true;
            case ORD_NOMBRE:
                mPlatoViewModel.ordenar("titulo ASC");
                mPlatoViewModel.getAllPlatos().observe(this, platos -> {
                    mAdapter.submitList(platos);});
                return true;
            case ORD_CATEGORIA:
                mPlatoViewModel.ordenar("categoria ASC");
                mPlatoViewModel.getAllPlatos().observe(this, platos -> {
                    mAdapter.submitList(platos);});
                return true;
            case ORD_AMBAS:
                mPlatoViewModel.ordenar("categoria ASC, titulo ASC");
                mPlatoViewModel.getAllPlatos().observe(this, platos -> {
                    mAdapter.submitList(platos);});
                return true;
            default:
                break;
        }

        return super.onContextItemSelected(item);
    }

    /**
     * Maneja las opciones seleccionadas en el menú de opciones.
     * @param item El elemento de menú seleccionado.
     * @return true si el elemento de menú se maneja correctamente, false de lo contrario.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                createPlato();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Maneja el resultado de las actividades secundarias.
     * @param requestCode El código de solicitud de la actividad secundaria.
     * @param resultCode El código de resultado de la actividad secundaria.
     * @param data Los datos devueltos por la actividad secundaria.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                    "Plato no guardado",
                    Toast.LENGTH_LONG).show();
        } else {
            switch (requestCode) {
                case ACTIVITY_CREATE:
                    es.unizar.eina.m35_comidas.database.platos.Plato newPlato = new es.unizar.eina.m35_comidas.database.platos.Plato(extras.getString(PlatoEdit.PLATO_NOMBRE)
                            , extras.getString(PlatoEdit.PLATO_DESCRIPCION)
                            , extras.getString(PlatoEdit.PLATO_CATEGORIA)
                            , extras.getDouble(PlatoEdit.PLATO_PRECIO));
                    mPlatoViewModel.insert(newPlato);
                    break;
                case ACTIVITY_EDIT:
                    int id = extras.getInt(PlatoEdit.PLATO_ID);
                    es.unizar.eina.m35_comidas.database.platos.Plato updatedPlato = new es.unizar.eina.m35_comidas.database.platos.Plato(extras.getString(PlatoEdit.PLATO_NOMBRE)
                            , extras.getString(PlatoEdit.PLATO_DESCRIPCION)
                            , extras.getString(PlatoEdit.PLATO_CATEGORIA)
                            , extras.getDouble(PlatoEdit.PLATO_PRECIO));
                    updatedPlato.setId(id);
                    mPlatoViewModel.update(updatedPlato);
                    break;
            }
        }
    }

    /**
     * Crea un nuevo plato.
     */
    private void createPlato() {
        Intent intent = new Intent(getActivity(), PlatoEdit.class);
        startActivityForResult(intent, ACTIVITY_CREATE);
    }

    /**
     * Edita un plato existente.
     * @param current El plato actual que se va a editar.
     */
    private void editPlato(es.unizar.eina.m35_comidas.database.platos.Plato current) {
        Intent intent = new Intent(getActivity(), PlatoEdit.class);
        intent.putExtra(PlatoEdit.PLATO_NOMBRE, current.getTitulo());
        intent.putExtra(PlatoEdit.PLATO_DESCRIPCION, current.getDescripcion());
        intent.putExtra(PlatoEdit.PLATO_CATEGORIA, current.getCategoria());
        intent.putExtra(PlatoEdit.PLATO_PRECIO, current.getPrecio().toString());
        intent.putExtra(PlatoEdit.PLATO_ID, current.getId());
        startActivityForResult(intent, ACTIVITY_EDIT);
    }
}