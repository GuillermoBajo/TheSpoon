package es.unizar.eina.m35_comidas.ui.platos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;

import es.unizar.eina.m35_comidas.R;

/** Pantalla utilizada para la creación o edición de un plato */
/**
 * Esta clase representa la actividad de edición de un plato.
 * Permite al usuario editar los campos de un plato, como el título, descripción, categoría y precio.
 * También permite guardar los cambios realizados y enviar los datos actualizados de vuelta a la actividad principal.
 */
public class PlatoEdit extends AppCompatActivity {

    /**
     * Constantes utilizadas para pasar datos entre actividades.
     */
    public static final String PLATO_NOMBRE = "titulo";
    public static final String PLATO_DESCRIPCION = "descripcion";
    public static final String PLATO_CATEGORIA = "categoria";
    public static final String PLATO_PRECIO = "precio";
    public static final String PLATO_ID = "id";

    /** Objetos EditText que representan los campos de entrada para el título, descripción, etc. del plato. */
    private EditText mTituloText;
    private EditText mDescripcionText;
    private RadioGroup mCategoriaRadioGroup;
    private RadioButton mOpcionARadioButton;
    private RadioButton mOpcionBRadioButton;
    private RadioButton mOpcionCRadioButton;
    private EditText mPrecioText;

    /** El identificador único del plato que se está editando. */
    private Integer mRowId;

    /** Objeto Button que representa el botón de guardar. */
    Button mSaveButton;

    /**
    * Este método se llama cuando la actividad está siendo creada.
    * Establece el contenido de la actividad a partir del archivo de diseño activity_platoedit.xml.
    */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platoedit);

        mTituloText = findViewById(R.id.titulo);
        mDescripcionText = findViewById(R.id.descripcion);

        mCategoriaRadioGroup = findViewById(R.id.categoria);
        mOpcionARadioButton = findViewById(R.id.opcionA);
        mOpcionBRadioButton = findViewById(R.id.opcionB);
        mOpcionCRadioButton = findViewById(R.id.opcionC);

        mPrecioText = findViewById(R.id.precio);

        mSaveButton = findViewById(R.id.button_save);
        mSaveButton.setOnClickListener(view -> {
            /**
             * Cuando se hace clic en el botón de guardar (mSaveButton), se crea un Intent llamado replyIntent.
             * Se verifica si alguno de los campos está vacío. Si es así, se establece el resultado como RESULT_CANCELED.
             * Si el título no está vacío, se añaden los datos al Intent y se establece el resultado como RESULT_OK.
             * El Intent se envía de vuelta a la actividad principal utilizando setResult(RESULT_OK, replyIntent) y luego se finaliza la actividad.
             */
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mTituloText.getText()) || TextUtils.isEmpty(mDescripcionText.getText()) || TextUtils.isEmpty(mTituloText.getText()) ||
                    TextUtils.isEmpty(mPrecioText.getText()) || Double.parseDouble(mPrecioText.getText().toString()) < 0.0) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                replyIntent.putExtra(PlatoEdit.PLATO_NOMBRE, mTituloText.getText().toString());
                replyIntent.putExtra(PlatoEdit.PLATO_DESCRIPCION, mDescripcionText.getText().toString());

                int selectedId = mCategoriaRadioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);
                String categoriaSeleccionada = radioButton.getText().toString();
                replyIntent.putExtra(PlatoEdit.PLATO_CATEGORIA, categoriaSeleccionada);

                double precio = 0.0;
                if (!TextUtils.isEmpty(mPrecioText.getText())) {
                    precio = Double.parseDouble(mPrecioText.getText().toString());
                }
                replyIntent.putExtra(PlatoEdit.PLATO_PRECIO, precio);

                if (mRowId != null) {
                    replyIntent.putExtra(PlatoEdit.PLATO_ID, mRowId.intValue());
                }
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });

        populateFields();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }

    /**
     * Este método se utiliza para llenar los campos de entrada (mTitleText, ...) con los datos
     * recibidos de la actividad principal (PlatoFragmento).
     * Si hay datos extras en el Intent, se extraen y se asignan a los campos de entrada.
     */
    private void populateFields() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mTituloText.setText(extras.getString(PlatoEdit.PLATO_NOMBRE));
            mDescripcionText.setText(extras.getString(PlatoEdit.PLATO_DESCRIPCION));
            mPrecioText.setText(extras.getString(PlatoEdit.PLATO_PRECIO));
            mRowId = extras.getInt(PlatoEdit.PLATO_ID);

            String categoriaSeleccionada = extras.getString(PlatoEdit.PLATO_CATEGORIA);

            if (categoriaSeleccionada != null) {
                switch (categoriaSeleccionada) {
                    case "Primero":
                        mOpcionARadioButton.setChecked(true);
                        break;
                    case "Segundo":
                        mOpcionBRadioButton.setChecked(true);
                        break;
                    case "Tercero":
                        mOpcionCRadioButton.setChecked(true);
                        break;
                }
            }
        }
        /**
         * Si extras es null en la llamada a populateFields(), significa que no hay datos adicionales
         * en el Intent que se utilizó para iniciar la actividad PlatoEdit.
         * Esto podría suceder si la actividad se inicia sin la intención de editar un plato existente,
         * es decir, cuando se está creando un nuevo plato.
         */
    }
}
