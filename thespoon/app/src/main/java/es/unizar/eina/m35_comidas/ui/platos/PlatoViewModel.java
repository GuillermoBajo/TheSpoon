package es.unizar.eina.m35_comidas.ui.platos;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Arrays;
import java.util.List;

import es.unizar.eina.m35_comidas.database.platos.Plato;
import es.unizar.eina.m35_comidas.database.platos.PlatoRepository;

public class PlatoViewModel extends AndroidViewModel {

    private PlatoRepository mRepository;

    private LiveData<List<Plato>> mAllPlatos;

    /**
     * Constructor de la clase PlatoViewModel.
     * Recibe una instancia de Application y se encarga de inicializar el repositorio y la lista de platos.
     *
     * @param application La instancia de Application.
     */
    public PlatoViewModel(Application application) {
        super(application);
        mRepository = new PlatoRepository(application);
        mAllPlatos = mRepository.getAllPlatos();
    }

    /**
     * Este método ordena la lista de platos según el criterio especificado.
     *
     * @param criterio El criterio de ordenación.
     */
    public void ordenar(String criterio){
        mRepository.order(criterio);
        mAllPlatos = mRepository.getAllPlatos();
    }

    /**
     * Este método proporciona acceso a la lista de todos los platos como un objeto LiveData.
     * Los observadores pueden suscribirse a este LiveData para recibir actualizaciones cuando cambian los datos.
     *
     * @return La lista de todos los platos como un objeto LiveData.
     */
    LiveData<List<Plato>> getAllPlatos() { return mAllPlatos; }

    /**
     * Este método se encarga de insertar un plato en el repositorio.
     *
     * @param plato El plato a insertar.
     * @return Identificador del plato creado.
     */
    public long insert(Plato plato) {
        if (plato != null) {
            // Comprobación del campo Título
            if (plato.getTitulo() == null) {
                throw new IllegalArgumentException("El título del plato no es válido. Titulo es nulo");
            }
            if (plato.getTitulo().length() == 0) {
                throw new IllegalArgumentException("El título del plato no es válido. No hay titulo");
            }
            if (!plato.getTitulo().matches("[a-zA-Z0-9\\s]+")) {
                throw new IllegalArgumentException("El título del plato no es válido. Regex no se corresponde" +
                        "con [a-zA-Z0-9]+");
            }

            // Comprobación del campo Descripción
            if (plato.getDescripcion() == null) {
                throw new IllegalArgumentException("La descripción del plato no es válida. Descripcion es nula");
            }
            if (!plato.getDescripcion().matches("[a-zA-Z0-9\\s\\S]+")) {
                throw new IllegalArgumentException("La descripción del plato no es válida. Regex no se corresponde" +
                        "con [a-zA-Z0-9]+");
            }

            // Comprobación del campo Categoría
            if (plato.getCategoria() == null) {
                throw new IllegalArgumentException("La categoría del plato no es válida. Categoría es nula");
            }

            String[] categoriasValidas = {"Primero", "Segundo", "Tercero"};
            if (!Arrays.asList(categoriasValidas).contains(plato.getCategoria())) {
                throw new IllegalArgumentException("La categoría del plato no es válida. Categoría no es Primero, Segundo, Tercero");
            }

            // Comprobación del campo Precio
            if (plato.getPrecio() == null) {
                throw new IllegalArgumentException("El precio del plato no es válido. El precio es nulo");
            }
            if (plato.getPrecio() <= 0) {
                throw new IllegalArgumentException("El precio del plato no es válido. El precio es <= 0");
            }

        } else {
            // Lanzar una excepción o manejar el error apropiadamente si el plato es nulo
            throw new IllegalArgumentException("El plato a insertar es nulo.");
        }

        // Si todas las validaciones son exitosas, se procede a insertar el plato en el repositorio
        return mRepository.insert(plato);
    }

    
    /**
     * Este método se encarga de actualizar un plato en el repositorio.
     *
     * @param plato El plato a actualizar.
     * @return El identificador del plato actualizado
     */
    public long update(Plato plato) {
        if (plato != null) {
            // Comprobación del campo Id
            if (plato.getId() <= 0) {
                throw new IllegalArgumentException("El Id del plato no es válido. Id >= 0");
            }

            // Comprobación del campo Título
            if (plato.getTitulo() == null) {
                throw new IllegalArgumentException("El título del plato no es válido. Titulo es nulo");
            }
            if (plato.getTitulo().length() == 0) {
                throw new IllegalArgumentException("El título del plato no es válido. No hay titulo");
            }
            if (!plato.getTitulo().matches("[a-zA-Z0-9\\s]+")) {
                throw new IllegalArgumentException("El título del plato no es válido. Regex no se corresponde" +
                        "con [a-zA-Z0-9]+");
            }

            // Comprobación del campo Descripción
            if (plato.getDescripcion() == null) {
                throw new IllegalArgumentException("La descripción del plato no es válida. Descripcion es nula");
            }
            if (!plato.getDescripcion().matches("[a-zA-Z0-9\\s\\S]+")) {
                throw new IllegalArgumentException("La descripción del plato no es válida. Regex no se corresponde" +
                        "con [a-zA-Z0-9]+");
            }

            // Comprobación del campo Categoría
            if (plato.getCategoria() == null) {
                throw new IllegalArgumentException("La categoría del plato no es válida. Categoría es nula");
            }

            String[] categoriasValidas = {"Primero", "Segundo", "Tercero"};
            if (!Arrays.asList(categoriasValidas).contains(plato.getCategoria())) {
                throw new IllegalArgumentException("La categoría del plato no es válida. Categoría no es Primero, Segundo, Tercero");
            }

            // Comprobación del campo Precio
            if (plato.getPrecio() == null) {
                throw new IllegalArgumentException("El precio del plato no es válido. El precio es nulo");
            }
            if (plato.getPrecio() <= 0) {
                throw new IllegalArgumentException("El precio del plato no es válido. El precio es <= 0");
            }
        } else {
            // Lanzar una excepción o manejar el error apropiadamente si el plato es nulo
            throw new IllegalArgumentException("El plato a insertar es nulo.");
        }

        return mRepository.update(plato);
    }

    /**
     * Este método se encarga de eliminar un plato del repositorio.
     *
     * @param plato El plato a eliminar.
     * @return Número de filas afectadas por la eliminación.
     */
    public long delete(Plato plato) {
        if (plato != null) {
            // Comprobación del campo Id para garantizar su validez
            if (plato.getId() <= 0) {
                throw new IllegalArgumentException("El Id del plato no es válido");
            }
        } else {
            throw new IllegalArgumentException("El plato a eliminar es nulo");
        }

        return mRepository.delete(plato);
    }
}
