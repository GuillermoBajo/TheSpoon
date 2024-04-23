package es.unizar.eina.m35_comidas.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import es.unizar.eina.m35_comidas.ui.pedidos.PedidoFragmento;
import es.unizar.eina.m35_comidas.ui.platos.PlatoFragmento;


/**
 * Adaptador para el ViewPager que muestra los fragmentos de la aplicación.
 */
public class PagerAdapter extends FragmentStateAdapter {

    /**
     * Crea un nuevo PagerAdapter.
     *
     * @param fragmentActivity la actividad que contiene el ViewPager.
     */
    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    /**
     * Crea un nuevo fragmento en la posición especificada.
     *
     * @param position la posición del fragmento.
     * @return el fragmento creado.
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PlatoFragmento();
            case 1:
                return new PedidoFragmento();
            default:
                return new PlatoFragmento();
        }
    }

    /**
     * Obtiene el número total de fragmentos en el ViewPager.
     *
     * @return el número total de fragmentos.
     */
    @Override
    public int getItemCount() {
        return 2;
    }
}