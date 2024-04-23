package es.unizar.eina.m35_comidas.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;

import es.unizar.eina.m35_comidas.R;
import es.unizar.eina.m35_comidas.ui.test.TestsImplementation;


/**
 * Clase que representa la actividad principal de la aplicación.
 */
public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    PagerAdapter PagerAdapter;

    /**
     * Crea el menú de opciones en la barra de aplicaciones.
     *
     * @param menu El menú en el que se inflarán los elementos del menú.
     * @return Devuelve true para mostrar el menú.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.context_tests, menu);
        return true;
    }

    /**
     * Maneja los eventos de clic en los elementos del menú.
     *
     * @param item El elemento del menú seleccionado.
     * @return Devuelve true si el evento ha sido manejado.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TestsImplementation test = new TestsImplementation(getApplication());
        switch (item.getTitle().toString()) {
            case "Test equivalencia platos":
                test.testEquivalenciaPlato();
                return true;
            case "Test equivalencia pedidos":
                test.testEquivalenciaPedido();
                return true;
            case "Test volumen":
                test.testVolumen();
                return true;
            case "Test sobrecarga":
                test.testSobrecarga();
                return true;
        }
        return true;
    }

    /**
     * Método llamado cuando se crea la actividad.
     * @param savedInstanceState El estado guardado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m35_comidas);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        PagerAdapter = new PagerAdapter(this);
        viewPager2.setAdapter(PagerAdapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.setScrollPosition(position, 0f, true);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
