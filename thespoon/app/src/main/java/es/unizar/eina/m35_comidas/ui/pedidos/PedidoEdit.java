package es.unizar.eina.m35_comidas.ui.pedidos;

import static es.unizar.eina.m35_comidas.ui.pedidos.PedidoFragmento.ACTIVITY_AGNADIRPLATOS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.unizar.eina.m35_comidas.R;
import es.unizar.eina.m35_comidas.database.pedidos.NumRaciones;
import es.unizar.eina.m35_comidas.database.pedidos.Pedido;
import es.unizar.eina.m35_comidas.database.pedidos.UnionPlatoPedido;
import es.unizar.eina.m35_comidas.database.platos.Plato;
import es.unizar.eina.m35_comidas.ui.platos.PlatoEdit;

/**
 * Esta clase representa la actividad de edición de pedidos.
 * Permite al usuario editar los detalles de un pedido, como el nombre del cliente, el número de cliente,
 * el estado del pedido, la fecha de recogida y la hora de recogida.
 */
public class PedidoEdit extends AppCompatActivity{

    /**
     * Constantes utilizadas para pasar datos entre actividades.
     */
    public static final String PEDIDO_NOMBRECLIENTE = "nombrecliente";
    public static final String PEDIDO_NUMEROCLIENTE = "numerocliente";
    public static final String PEDIDO_ESTADO = "estado";
    public static final String PEDIDO_FECHA = "fecha";
    public static final String PEDIDO_HORA = "hora";
    public static final String PEDIDO_PRECIO = "pedidoprecio";
    public static final String PEDIDO_ID = "id";

    /** Objetos EditText que representan los campos de entrada para el nombre del cliente, número de cliente, fecha de recogida y hora de recogida. */
    private EditText mNombreCliente;
    private EditText mNumeroCliente;
    private EditText mFechaRecogida;
    private TimePicker mHoraRecogida;
    private RadioGroup mCategoriaRadioGroup;
    private RadioButton mOpcionSolicRadioButton;
    private RadioButton mOpcionPrepRadioButton;
    private RadioButton mOpcionRecRadioButton;
    TextView mPrecioPedido;

    PlatoPedidoListAdapter mAdapter;

    RecyclerView mRecyclerView;

    /** Identificador único del pedido que se está editando. */
    private Integer mRowId;

    /** Objeto Button que representa el botón de guardar. */
    Button mSaveButton;
    Button mAddPlatos;

    private int dia, mes, agno;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        /**
         * Este método se llama cuando la actividad está siendo creada.
         * Establece el contenido de la actividad a partir del archivo de diseño activity_pedidoedit.xml.
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidoedit);

        mRecyclerView = findViewById(R.id.recyclerviewListaPlatos);
        mAdapter = new PlatoPedidoListAdapter(new PlatoPedidoListAdapter.PlatosPedidoDiff(), null);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        PlatoSingleton ped = PlatoSingleton.getInstance();
        ped.setListaPlatosAgnadidos(new UnionPlatoPedido());
        ped.getPlatosAgnadidosAfter().observe(this, PPL -> {
            List<PlatosPedido> adapter = new ArrayList<>();
            for (Plato plato: PPL.platos) {
                for (NumRaciones nr : PPL.cantidad) {
                    if(plato.getId() == nr.getPlatoId()){
                        PlatosPedido nuevoPlato = new PlatosPedido(plato, nr);
                        adapter.add(nuevoPlato);
                    }
                }
            }

            mAdapter.submitList(adapter);
            List<Plato> copyPlato = List.copyOf(PPL.platos);
            List<NumRaciones> copyNR = List.copyOf(PPL.cantidad);

            UnionPlatoPedido aux = new UnionPlatoPedido(PPL.pedido, copyPlato, copyNR);
            ped.setListaPlatosAgnadidos(aux);
        });


        // Inicializar los campos de entrada
        mNombreCliente = findViewById(R.id.tituloPed);
        mNumeroCliente = findViewById(R.id.tlfn);
        mCategoriaRadioGroup = findViewById(R.id.estado);
        mOpcionSolicRadioButton = findViewById(R.id.OptSolicitado);
        mOpcionPrepRadioButton = findViewById(R.id.OptPreparado);
        mOpcionRecRadioButton = findViewById(R.id.OptRecogido);
        mFechaRecogida = findViewById(R.id.fecha);
        mHoraRecogida = findViewById(R.id.hora);
        mPrecioPedido = findViewById(R.id.precioPedido);
        mAddPlatos = findViewById(R.id.agnadirPlatosBot);
        mAddPlatos.setOnClickListener(vista -> {
            addPlatos();
        });

        // Configurar el formato de 24 horas para el TimePicker
        mHoraRecogida.setIs24HourView(true);

        // Configurar el listener para el campo de fecha de recogida
        mFechaRecogida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la fecha actual
                final Calendar c = Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                agno = c.get(Calendar.YEAR);

                // Crear un DatePickerDialog para seleccionar la fecha de recogida
                DatePickerDialog datePickerDialog = new DatePickerDialog(PedidoEdit.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar selectedDate = Calendar.getInstance();
                                selectedDate.set(year, monthOfYear, dayOfMonth);
                                if (selectedDate.before(Calendar.getInstance())) {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "La fecha no puede ser anterior a la actual",
                                            Toast.LENGTH_LONG).show();
                                } else if (selectedDate.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Los lunes no se aceptan pedidos",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    // Establecer el texto antes de cerrar el DatePickerDialog
                                    mFechaRecogida.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                }
                            }
                        }, agno, mes, dia);

                // Establecer el primer día de la semana como lunes
                datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);

                datePickerDialog.show();
            }
        });

        // Configurar el listener para el botón de guardar
        mSaveButton = findViewById(R.id.button_save);
        mSaveButton.setOnClickListener(view -> {
            // Crear un Intent para devolver los datos del pedido editado
            Intent replyIntent = new Intent();

            // Validar los campos de entrada
            if (TextUtils.isEmpty(mNombreCliente.getText()) ||
                    TextUtils.isEmpty(mNumeroCliente.getText()) ||
                    TextUtils.isEmpty(mFechaRecogida.getText())) {
                Toast.makeText(
                        this.getApplicationContext(),
                        "Error: Campos vacíos",
                        Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED, replyIntent);
            } else if (mHoraRecogida.getHour() < 0 || mHoraRecogida.getHour() > 23 || mHoraRecogida.getMinute() < 0 || mHoraRecogida.getMinute() > 59) {
                Toast.makeText(
                        this.getApplicationContext(),
                        "La hora introducida no sigue el formato horario",
                        Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED, replyIntent);
            } else if (mHoraRecogida.getHour() < 19 || mHoraRecogida.getHour() > 23 || (mHoraRecogida.getHour() == 19 && mHoraRecogida.getMinute() < 30) ||
                    (mHoraRecogida.getHour() == 23 && mHoraRecogida.getMinute() > 30)) {
                Toast.makeText(
                        this.getApplicationContext(),
                        "Solo se aceptan pedidos entre las 19:30 y las 23:00",
                        Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                // Obtener los valores de los campos de entrada y agregarlos al Intent
                replyIntent.putExtra(PedidoEdit.PEDIDO_NOMBRECLIENTE, mNombreCliente.getText().toString());
                replyIntent.putExtra(PedidoEdit.PEDIDO_NUMEROCLIENTE,Integer.parseInt(mNumeroCliente.getText().toString()));

                int selectedId = mCategoriaRadioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);
                String categoriaSeleccionada = radioButton.getText().toString();
                replyIntent.putExtra(PedidoEdit.PEDIDO_ESTADO, categoriaSeleccionada);

                replyIntent.putExtra(PedidoEdit.PEDIDO_FECHA, mFechaRecogida.getText().toString());

                int hora = mHoraRecogida.getHour();
                int minuto = mHoraRecogida.getMinute();
                String horaExacta = String.format("%02d:%02d", hora, minuto);
                replyIntent.putExtra(PedidoEdit.PEDIDO_HORA, horaExacta);
                replyIntent.putExtra(PedidoEdit.PEDIDO_PRECIO, mPrecioPedido.getText().toString());

                if (mRowId!=null) {
                    replyIntent.putExtra(PedidoEdit.PEDIDO_ID, mRowId.intValue());
                }
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });

        // Llenar los campos de entrada con los datos recibidos
        populateFields();
    }

    /**
     * Método llamado cuando se presiona el botón de retroceso.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }


    /**
     * Este método se utiliza para llenar los campos de entrada con los datos recibidos de la actividad principal.
     * Si hay datos extras en el Intent, se extraen y se asignan a los campos de entrada.
     */
    private void populateFields () {
        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            mNombreCliente.setText(extras.getString(PedidoEdit.PEDIDO_NOMBRECLIENTE));
            mNumeroCliente.setText(extras.getString(PedidoEdit.PEDIDO_NUMEROCLIENTE));
            mFechaRecogida.setText(extras.getString(PedidoEdit.PEDIDO_FECHA));

            String horaSelec = extras.getString(PedidoEdit.PEDIDO_HORA);
            String[] partesHora = horaSelec.split(":");
            int hora = Integer.parseInt(partesHora[0]);
            int minuto = Integer.parseInt(partesHora[1]);
            mHoraRecogida.setHour(hora);
            mHoraRecogida.setMinute(minuto);
            String precioFormateado = new DecimalFormat("#,##0.00").format(extras.getDouble(PedidoEdit.PEDIDO_PRECIO));
            mPrecioPedido.setText(precioFormateado + " €");



            mRowId = extras.getInt(PedidoEdit.PEDIDO_ID);

            String categoriaSeleccionada = extras.getString(PedidoEdit.PEDIDO_ESTADO);

            if (categoriaSeleccionada != null) {
                switch (categoriaSeleccionada) {
                    case "Solicitado":
                        mOpcionSolicRadioButton.setChecked(true);
                        break;
                    case "Preparado":
                        mOpcionPrepRadioButton.setChecked(true);
                        break;
                    case "Recogido":
                        mOpcionRecRadioButton.setChecked(true);
                        break;
                }
            }
        }
    }

    /**
     * Este método se llama cuando se desea agregar platos al pedido.
     * Inicia la actividad para agregar platos y espera el resultado.
     */
    private void addPlatos(){
        Intent intent = new Intent(this, AgnadirPlatoAPedido.class);
        startActivityForResult(intent, 5);
    }


    /**
     * Método llamado cuando se recibe un resultado de una actividad secundaria.
     * Se utiliza para actualizar la lista de platos en el pedido después de agregar platos.
     *
     * @param requestCode Código de solicitud de la actividad.
     * @param resultCode  Código de resultado de la actividad.
     * @param data        Datos recibidos de la actividad secundaria.
     */
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

        // Actualiza la lista de editar pedido
        if (resultCode == ACTIVITY_AGNADIRPLATOS) {
            PlatoSingleton camb = PlatoSingleton.getInstance();
            camb.getPlatosAgnadidosAfter().removeObservers(this);
            camb.getPlatosAgnadidosAfter().observe(this, PPL -> {
                List<PlatosPedido> adapter = new ArrayList<>();
                for (Plato plato: PPL.platos) {
                    for (NumRaciones racion : PPL.cantidad) {
                        if(plato.getId() == racion.getPlatoId()){
                            PlatosPedido nuevoP = new PlatosPedido(plato, racion);
                            adapter.add(nuevoP);
                        }
                    }
                }

                mAdapter.submitList(adapter);
                String precioFormateado = new DecimalFormat("#,##0.00").format(calcularPrecioPedido(adapter));
                mPrecioPedido.setText(precioFormateado + " €");
            });
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Calcula el precio total del pedido en función de los platos seleccionados.
     *
     * @param adapter Lista de platos en el pedido.
     * @return Precio total del pedido.
     */
    private double calcularPrecioPedido(List<PlatosPedido> adapter) {
        double precio = 0.0;
        for (PlatosPedido plato : adapter) {
            precio += plato.getPrecio() * plato.getCantidad();
        }
        return precio;
    }
}
