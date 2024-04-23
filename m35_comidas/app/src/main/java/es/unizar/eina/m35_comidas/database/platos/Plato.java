package es.unizar.eina.m35_comidas.database.platos;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** Clase anotada como entidad que representa un plato y que consta de título, etc... */
/** La clase Plato está anotada con @Entity, lo que indica que es una entidad de la base de datos.
 *  La anotación @Entity especifica el nombre de la tabla en la base de datos */
@Entity(tableName = "plato")
public class Plato {

    /**
     * La clase Plato representa un plato en la base de datos.
     * Contiene información como el título, descripción, categoría y precio del plato.
     */

    /**id es la clave primaria de la entidad Plato. La anotación @PrimaryKey indica que esta propiedad
     * es la clave primaria. autoGenerate = true significa que la clave primaria se generará automáticamente.*/
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "platoId")
    private int id;

    /**title es el título del plato. La anotación @ColumnInfo especifica el nombre de la columna
     * en la base de datos (name = "title").*/
    @ColumnInfo(name = "titulo")
    private String titulo;

    @ColumnInfo(name = "descripcion")
    private String descripcion;

    @ColumnInfo(name = "categoria")
    private String categoria;

    @ColumnInfo(name = "precio")
    private Double precio;

    /**Constructor*/
    public Plato(String titulo, String descripcion, String categoria, Double precio) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio = precio;
    }

    /**
     * Devuelve el identificador del plato.
     *
     * @return El identificador del plato.
     */
    public int getId(){
        return this.id;
    }

    /**
     * Permite actualizar el identificador del plato.
     *
     * @param id El nuevo identificador del plato.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devuelve el título del plato.
     *
     * @return El título del plato.
     */
    public String getTitulo(){
        return this.titulo;
    }

    /**
     * Devuelve la descripción del plato.
     *
     * @return La descripción del plato.
     */
    public String getDescripcion(){
        return this.descripcion;
    }

    /**
     * Devuelve la categoría del plato.
     *
     * @return La categoría del plato.
     */
    public String getCategoria(){
        return this.categoria;
    }

    /**
     * Devuelve el precio del plato.
     *
     * @return El precio del plato.
     */
    public Double getPrecio(){
        return this.precio;
    }
}
