package es.unizar.eina.m35_comidas.ui.test;

import android.app.Application;
import android.util.Log;

import javax.xml.transform.sax.TransformerHandler;

import es.unizar.eina.m35_comidas.database.pedidos.Pedido;
import es.unizar.eina.m35_comidas.database.platos.Plato;
import es.unizar.eina.m35_comidas.ui.pedidos.PedidoViewModel;
import es.unizar.eina.m35_comidas.ui.platos.PlatoViewModel;

/**
 */
public class TestsImplementation {
    private int NUM_PLATOS_TEST = 150;
    private int NUM_PEDIDOS_TEST = 2000;

    private PlatoViewModel modelPlatos;
    private PedidoViewModel modelPedidos;
    public TestsImplementation(Application app) {
        modelPlatos = new PlatoViewModel(app);
        modelPedidos = new PedidoViewModel(app);
    }


    // TEST VOLUMEN
    public void testVolumen() {
        testVolumenPlatos();
        testVolumenPedidos();
        Log.i("TestVolumen", "ok");
    }

    private void testVolumenPlatos() {
        Plato platoTest = new Plato("Volumen", "como joan praderas", "Segundo", 5.0);
        long res = 0;
        for (int i = 0; i <= NUM_PLATOS_TEST; i++) {
            if ((res = modelPlatos.insert(platoTest)) <= 0)
                Log.e("TestVolumenPlato", "Error. Id:" + String.valueOf(res));
        }
    }
    private void testVolumenPedidos() {
        long res = 0;
        Pedido pedidoAux = new Pedido("joan", 1, "Solicitado", "01/12/2023",
                "21:00", 10.0);
        for (int i = 0; i <= NUM_PEDIDOS_TEST; i++) {
            if ((res = modelPedidos.insert(pedidoAux)) <= 0)
                Log.e("TestVolumenPedido", "Error. Id:" + String.valueOf(res));
        }
    }

    // TEST DE SOBRECARGA
    public void testSobrecarga() {
        for (int length = 10; length < 2e20; length*=10) {
            String descripcion = new String(new char[length]).replace("\0", "t");
            Plato platoTest = new Plato("Sobrecarga", descripcion, "Segundo", 5.0);
            Log.i("TestSobrecarga longitud "+length, String.valueOf(descripcion));
            modelPlatos.insert(platoTest);
        }
    }

    // TEST DE EQUIVALENCIA
    public void testEquivalenciaPlato() {
        testEquivalenciaInsertarPlato();
        testEquivalenciaUpdatePlato();
        testEquivalenciaEliminarPlato();
    }

    public void testEquivalenciaPedido() {
        testEquivalenciaInsertarPedido();
        testEquivalenciaUpdatePedido();
        testEquivalenciaEliminarPedido();
    }

    private void testEquivalenciaInsertarPlato() {
        Plato[] tests = {
                new Plato("Sopa", "Con tomate", "Primero", 10.0),

                new Plato("", "Con tomate", "Primero", 10.0),
                new Plato(null, "Con tomate", "Primero", 10.0),
                new Plato("Sopa", null, "Primero", 10.0),
                new Plato("Sopa", "Con tomate", "NO_Valido", 10.0),
                new Plato("Sopa", "Con tomate", null, 10.0),
                new Plato("Sopa", "Con tomate", "Primero", null),
                new Plato("Sopa", "Con tomate", "Primero", -1.0)
        };
        try {
            long res = modelPlatos.insert(tests[0]);
            Log.d("Insertar0", "ok");
        } catch (Throwable e) {
            Log.d("Error", "Insertar0: "+  e.getMessage());
        }

        try {
            long res = modelPlatos.insert(tests[1]);    // titulo=""
            Log.d("Error", "Insertar1: Error expected, insertion went ok.");
        } catch (Throwable e) {
            if (e.getMessage() == "El título del plato no es válido. No hay titulo")
                Log.d("Insertar1", "ok");
            else
                Log.d("Error", "Insertar1: "+  e.getMessage());
        }

        try {
            long res = modelPlatos.insert(tests[2]);    // titulo=null
            Log.d("Error", "Insertar2: Error expected, insertion went ok.");
        } catch (Throwable e) {
            if (e.getMessage() == "El título del plato no es válido. Titulo es nulo")
                Log.d("Insertar2", "ok");
            else
                Log.d("Error", "Insertar2: "+  e.getMessage());
        }

        try {
            long res = modelPlatos.insert(tests[3]);    // descripcion=null
            Log.d("Error", "Insertar3: Error expected, insertion went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "La descripción del plato no es válida. Descripcion es nula")
                Log.d("Insertar3", "ok");
            else
                Log.d("Error", "Insertar3: "+  e.getMessage());
        }

        try {
            long res = modelPlatos.insert(tests[4]);    // categoria=no
            Log.d("Error", "Insertar4: Error expected, insertion went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "La categoría del plato no es válida. Categoría no es Primero, Segundo, Tercero")
                Log.d("Insertar4", "ok");
            else
                Log.d("Error", "Insertar4: "+  e.getMessage());
        }

        try {
            long res = modelPlatos.insert(tests[5]);    // categoria=null
            Log.d("Error", "Insertar5: Error expected, insertion went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "La categoría del plato no es válida. Categoría es nula")
                Log.d("Insertar5", "ok");
            else
                Log.d("Error", "Insertar5: "+  e.getMessage());
        }

        try {
            long res = modelPlatos.insert(tests[6]);    // precio=null
            Log.d("Error", "Insertar6: Error expected, insertion went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "El precio del plato no es válido. El precio es nulo")
                Log.d("Insertar6", "ok");
            else
                Log.d("Error", "Insertar6: "+  e.getMessage());
        }

        try {
            long res = modelPlatos.insert(tests[7]);    // precio=-1
            Log.d("Error", "Insertar7: Error expected, insertion went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "El precio del plato no es válido. El precio es <= 0")
                Log.d("Insertar7", "ok");
            else
                Log.d("Error", "Insertar7: "+  e.getMessage());
        }
    }

    private void testEquivalenciaUpdatePlato() {
        Plato[] tests = {
                new Plato("Sopa", "Con tomate", "Primero", 10.0),
                new Plato("Canelon", "Bechamel", "Segundo", 10.0),

                new Plato("Canelon", "Bechamel", "Segundo", 10.0),
                new Plato("", "Bechamel", "Segundo", 10.0),
                new Plato(null, "Bechamel", "Segundo", 10.0),
                new Plato("Canelon", null, "Segundo", 10.0),
                new Plato("Canelon", "Bechamel", "NO_Valido", 10.0),
                new Plato("Canelon", "Bechamel", null, 10.0),
                new Plato("Canelon", "Bechamel", "Segundo", null),
                new Plato("Canelon", "Bechamel", "Segundo", -1.0),
        };

        long id = modelPlatos.insert(tests[0]);
        for (Plato test: tests) {
            test.setId((int) id);
        }
        tests[2].setId((int) -id);

        try {
            long res = modelPlatos.update(tests[1]);
            Log.d("Modificar0", "ok");
        } catch (Throwable e) {
            Log.d("Error", "Modificar0: "+  e.getMessage());
        }

        try {
            long res = modelPlatos.update(tests[2]);    // titulo=null
            Log.d("Error", "Modificar2: Error expected, modification went ok.");
        } catch (Throwable e) {
            if (e.getMessage() == "El Id del plato no es válido. Id >= 0")
                Log.d("Modificar2", "ok");
            else
                Log.d("Error", "Modificar2: "+  e.getMessage());
        }

        try {
            long res = modelPlatos.update(tests[3]);    // descripcion=null
            Log.d("Error", "Modificar3: Error expected, modification went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "El título del plato no es válido. No hay titulo")
                Log.d("Modificar3", "ok");
            else
                Log.d("Error", "Modificar3: "+  e.getMessage());
        }

        try {
            long res = modelPlatos.update(tests[4]);    // categoria=no
            Log.d("Error", "Modificar4: Error expected, modification went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "El título del plato no es válido. Titulo es nulo")
                Log.d("Modificar4", "ok");
            else
                Log.d("Error", "Modificar4: "+  e.getMessage());
        }

        try {
            long res = modelPlatos.update(tests[5]);    // categoria=null
            Log.d("Error", "Modificar5: Error expected, modification went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "La descripción del plato no es válida. Descripcion es nula")
                Log.d("Modificar5", "ok");
            else
                Log.d("Error", "Modificar5: "+  e.getMessage());
        }

        try {
            long res = modelPlatos.update(tests[6]);    // precio=null
            Log.d("Error", "Modificar6: Error expected, modification went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "La categoría del plato no es válida. Categoría no es Primero, Segundo, Tercero")
                Log.d("Modificar6", "ok");
            else
                Log.d("Error", "Modificar6: "+  e.getMessage());
        }

        try {
            long res = modelPlatos.update(tests[7]);    // precio=-1
            Log.d("Error", "Modificar7: Error expected, modification went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "La categoría del plato no es válida. Categoría es nula")
                Log.d("Modificar7", "ok");
            else
                Log.d("Error", "Modificar7: "+  e.getMessage());
        }

        try {
            long res = modelPlatos.update(tests[8]);    // precio=-1
            Log.d("Error", "Modificar8: Error expected, modification went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "El precio del plato no es válido. El precio es nulo")
                Log.d("Modificar8", "ok");
            else
                Log.d("Error", "Modificar8: "+  e.getMessage());
        }

        try {
            long res = modelPlatos.update(tests[9]);    // precio=-1
            Log.d("Error", "Modificar9: Error expected, modification went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "El precio del plato no es válido. El precio es <= 0")
                Log.d("Modificar9", "ok");
            else
                Log.d("Error", "Modificar9: "+  e.getMessage());
        }
    }

    private void testEquivalenciaEliminarPlato() {
        Plato test = new Plato("Sopa", "Con tomate", "Primero", 10.0);
        Plato test1 = new Plato("Sopa", "Con tomate", "Primero", 10.0);
        try {
            long idTest = modelPlatos.insert(test);
            test.setId((int) idTest);
            test1.setId((int) -idTest);

            try {
                long res = modelPlatos.delete(test1);    // precio=-1
                Log.d("Error", "Eliminar0: Error expected, delete went ok.");

            } catch (Throwable e) {
                if (e.getMessage() == "El Id del plato no es válido")
                    Log.d("Eliminar0", "ok");
                else
                    Log.d("Error", "Eliminar0: "+  e.getMessage());
            }

            try {
                long res = modelPlatos.delete(test);
                Log.d("Eliminar1", "ok");
            } catch (Throwable e) {
                Log.d("Error", "Eliminar1: "+  e.getMessage());
            }

            try {
                long res = modelPlatos.delete(test);    // precio=-1
                if (res == 0)
                    Log.d("Eliminar2", "ok");
                else
                    Log.d("Error", "Eliminar2: se ha eliminado un plato que ya estaba" +
                            "eliminado");
            } catch (Throwable e) {
                Log.d("Error", "Eliminar2: "+  e.getMessage());
            }

        } catch (Throwable e) {
            Log.d("Error", e.getMessage());
        }
    }


    private void testEquivalenciaInsertarPedido() {
        Pedido[] tests = {
                new Pedido("Dean", 1, "Solicitado", "01/12/2023",
                        "21:00", 10.0),

                new Pedido("", 1, "Solicitado", "01/12/2023",
                        "21:00", 10.0),
                new Pedido(null, 1, "Solicitado", "01/12/2023",
                        "21:00", 10.0),
                new Pedido("Dean", null, "Solicitado", "01/12/2023",
                        "21:00", 10.0),
                new Pedido("Dean", -1, "Solicitado", "01/12/2023",
                        "21:00", 10.0),
                new Pedido("Dean", 1, null, "01/12/2023",
                        "21:00", 10.0),
                new Pedido("Dean", 1, "NO_Valido", "01/12/2023",
                        "21:00", 10.0),
                new Pedido("Dean", 1, "Solicitado", null,
                        "21:00", 10.0),
                new Pedido("Dean", 1, "Solicitado", "NO_Valido",
                        "21:00", 10.0),
                new Pedido("Dean", 1, "Solicitado", "01/12/2023",
                        "NO_Valido", 10.0),
                new Pedido("Dean", 1, "Solicitado", "01/12/2023",
                        null, 10.0),
                new Pedido("Dean", 1, "Solicitado", "01/12/2023",
                        "21:00", null),
                new Pedido("Dean", 1, "Solicitado", "01/12/2023",
                        "21:00", -10.0),
        };
        try {
            long res = modelPedidos.insert(tests[0]);
            Log.d("Insertar0", "ok");
        } catch (Throwable e) {
            Log.d("Error", "Insertar0: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.insert(tests[1]);
            Log.d("Error", "Insertar1: Error expected, insertion went ok.");
        } catch (Throwable e) {
            if (e.getMessage() == "El nombre del cliente no es válido. No hay nombre")
                Log.d("Insertar1", "ok");
            else
                Log.d("Error", "Insertar1: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.insert(tests[2]);
            Log.d("Error", "Insertar2: Error expected, insertion went ok.");
        } catch (Throwable e) {
            if (e.getMessage() == "El nombre del cliente no es válido. Nombre nulo")
                Log.d("Insertar2", "ok");
            else
                Log.d("Error", "Insertar2: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.insert(tests[3]);
            Log.d("Error", "Insertar3: Error expected, insertion went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "El número del cliente no es válida. Numero nulo")
                Log.d("Insertar3", "ok");
            else
                Log.d("Error", "Insertar3: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.insert(tests[4]);    // categoria=null
            Log.d("Error", "Insertar4: Error expected, insertion went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "El número del cliente no es válida. Numero <= 0")
                Log.d("Insertar4", "ok");
            else
                Log.d("Error", "Insertar4: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.insert(tests[5]);    // precio=null
            Log.d("Error", "Insertar5: Error expected, insertion went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "El estado del pedido no es válida. Estado nulo")
                Log.d("Insertar5", "ok");
            else
                Log.d("Error", "Insertar5: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.insert(tests[6]);    // precio=-1
            Log.d("Error", "Insertar6: Error expected, insertion went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "El estado del pedido no es válida. Estado no es Solicitado, Preparado, Recogido")
                Log.d("Insertar6", "ok");
            else
                Log.d("Error", "Insertar6: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.insert(tests[7]);    // precio=null
            Log.d("Error", "Insertar7: Error expected, insertion went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "La fecha del pedido no es válida. Fecha nula")
                Log.d("Insertar7", "ok");
            else
                Log.d("Error", "Insertar7: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.insert(tests[8]);    // precio=null
            Log.d("Error", "Insertar8: Error expected, insertion went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "La fecha del pedido no es válida. Fecha no cumple el formato DD/MM/AAAA")
                Log.d("Insertar8", "ok");
            else
                Log.d("Error", "Insertar8: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.insert(tests[9]);    // precio=null
            Log.d("Error", "Insertar9: Error expected, insertion went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "La hora del pedido no es válida. Hora no cumple el formato HH:MM")
                Log.d("Insertar9", "ok");
            else
                Log.d("Error", "Insertar9: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.insert(tests[10]);    // precio=null
            Log.d("Error", "Insertar10: Error expected, insertion went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "La hora del pedido no es válida. Hora nula")
                Log.d("Insertar10", "ok");
            else
                Log.d("Error", "Insertar10: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.insert(tests[11]);    // precio=null
            Log.d("Error", "Insertar11: Error expected, update went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "El precio del pedido no es valido. Precio nulo")
                Log.d("Insertar11", "ok");
            else
                Log.d("Error", "Insertar11: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.insert(tests[12]);    // precio=null
            Log.d("Error", "Insertar12: Error expected, update went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "El precio del pedido no es valido. Precio < 0")
                Log.d("Insertar12", "ok");
            else
                Log.d("Error", "Insertar12: "+  e.getMessage());
        }
    }

    private void testEquivalenciaUpdatePedido() {
        Pedido[] tests = {
                new Pedido("Dean", 1, "Solicitado", "01/12/2023",
                        "21:00", 10.0),
                new Pedido("Joank", 2, "Recogido", "01/12/2023",
                        "20:30", 5.0),

                new Pedido("Joank", 2, "Recogido", "01/12/2023",
                "20:30", 5.0),
                new Pedido("", 2, "Recogido", "01/12/2023",
                        "20:30", 5.0),
                new Pedido(null, 2, "Recogido", "01/12/2023",
                        "20:30", 5.0),
                new Pedido("Joank", null, "Recogido", "01/12/2023",
                        "20:30", 5.0),
                new Pedido("Joank", -2, "Recogido", "01/12/2023",
                        "20:30", 5.0),
                new Pedido("Joank", 2, null, "01/12/2023",
                        "20:30", 5.0),
                new Pedido("Joank", 2, "NO_Valido", "01/12/2023",
                        "20:30", 5.0),
                new Pedido("Joank", 2, "Recogido", null,
                        "20:30", 5.0),
                new Pedido("Joank", 2, "Recogido", "NO_Valido",
                        "20:30", 5.0),
                new Pedido("Joank", 2, "Recogido", "01/12/2023",
                        null, 5.0),
                new Pedido("Joank", 2, "Recogido", "01/12/2023",
                        "v", 5.0),
                new Pedido("Joank", 2, "Recogido", "01/12/2023",
                        "20:30", null),
                new Pedido("Joank", 2, "Recogido", "01/12/2023",
                        "20:30", -3.0),
        };

        long id = modelPedidos.insert(tests[0]);
        for (Pedido test: tests) {
            test.setId((int) id);
        }
        tests[2].setId((int) -id);

        try {
            long res = modelPedidos.update(tests[1]);
            Log.d("Modificar0", "ok");
        } catch (Throwable e) {
            Log.d("Error", "Modificar0: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.update(tests[2]);    // titulo=null
            Log.d("Error", "Modificar2: Error expected, modification went ok.");
        } catch (Throwable e) {
            if (e.getMessage() == "El Id del pedido no es válido. Id <= 0")
                Log.d("Modificar2", "ok");
            else
                Log.d("Error", "Modificar2: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.update(tests[3]);
            Log.d("Error", "Modificar3: Error expected, update went ok.");
        } catch (Throwable e) {
            if (e.getMessage() == "El nombre del cliente no es válido. No hay nombre")
                Log.d("Modificar3", "ok");
            else
                Log.d("Error", "Modificar3: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.update(tests[4]);
            Log.d("Error", "Modificar4: Error expected, update went ok.");
        } catch (Throwable e) {
            if (e.getMessage() == "El nombre del cliente no es válido. Nombre nulo")
                Log.d("Modificar4", "ok");
            else
                Log.d("Error", "Modificar4: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.update(tests[5]);
            Log.d("Error", "Modificar5: Error expected, update went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "El número del cliente no es válida. Numero nulo")
                Log.d("Modificar5", "ok");
            else
                Log.d("Error", "Modificar5: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.update(tests[6]);    // categoria=null
            Log.d("Error", "Modificar6: Error expected, update went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "El número del cliente no es válida. Numero <= 0")
                Log.d("Modificar6", "ok");
            else
                Log.d("Error", "Modificar6: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.update(tests[7]);    // precio=null
            Log.d("Error", "Modificar7: Error expected, update went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "El estado del pedido no es válida. Estado nulo")
                Log.d("Modificar7", "ok");
            else
                Log.d("Error", "Modificar7: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.update(tests[8]);    // precio=-1
            Log.d("Error", "Modificar8: Error expected, update went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "El estado del pedido no es válida. Estado no es Solicitado, Preparado, Recogido")
                Log.d("Modificar8", "ok");
            else
                Log.d("Error", "Modificar8: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.update(tests[9]);    // precio=null
            Log.d("Error", "Modificar9: Error expected, update went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "La fecha del pedido no es válida. Fecha nula")
                Log.d("Modificar9", "ok");
            else
                Log.d("Error", "Modificar9: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.update(tests[10]);    // precio=null
            Log.d("Error", "Modificar10: Error expected, update went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "La fecha del pedido no es válida. Fecha no cumple el formato DD/MM/AAAA")
                Log.d("Modificar10", "ok");
            else
                Log.d("Error", "Modificar10: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.update(tests[11]);    // precio=null
            Log.d("Error", "Modificar11: Error expected, update went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "La hora del pedido no es válida. Hora nula")
                Log.d("Modificar11", "ok");
            else
                Log.d("Error", "Modificar11: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.update(tests[12]);    // precio=null
            Log.d("Error", "Modificar12: Error expected, update went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "La hora del pedido no es válida. Hora no cumple el formato HH:MM")
                Log.d("Modificar12", "ok");
            else
                Log.d("Error", "Modificar12: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.update(tests[13]);    // precio=null
            Log.d("Error", "Modificar13: Error expected, update went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "El precio del pedido no es valido. Precio nulo")
                Log.d("Modificar13", "ok");
            else
                Log.d("Error", "Modificar13: "+  e.getMessage());
        }

        try {
            long res = modelPedidos.update(tests[14]);    // precio=null
            Log.d("Error", "Modificar14: Error expected, update went ok.");

        } catch (Throwable e) {
            if (e.getMessage() == "El precio del pedido no es valido. Precio < 0")
                Log.d("Modificar14", "ok");
            else
                Log.d("Error", "Modificar14: "+  e.getMessage());
        }
    }

    private void testEquivalenciaEliminarPedido() {
        Pedido test = new Pedido("Dean", 1, "Solicitado", "01/12/2023",
                "21:00", 10.0);
        Pedido test1 = new Pedido("Dean", 1, "Solicitado", "01/12/2023",
                "21:00", 10.0);
        try {
            long idTest = modelPedidos.insert(test);
            test.setId((int) idTest);
            test1.setId((int) -idTest);

            try {
                long res = modelPedidos.delete(test1);    // precio=-1
                Log.d("Error", "Eliminar0: Error expected, delete went ok.");

            } catch (Throwable e) {
                if (e.getMessage() == "El Id del pedido no es válido. Id <= 0")
                    Log.d("Eliminar0", "ok");
                else
                    Log.d("Error", "Eliminar0: "+  e.getMessage());
            }

            try {
                long res = modelPedidos.delete(test);
                Log.d("Eliminar1", "ok");
            } catch (Throwable e) {
                Log.d("Error", "Eliminar1: "+  e.getMessage());
            }

            try {
                long res = modelPedidos.delete(test);    // precio=-1
                if (res == 0)
                    Log.d("Eliminar2", "ok");
                else
                    Log.d("Error", "Eliminar2: se ha eliminado un plato que ya estaba " +
                            "eliminado");
            } catch (Throwable e) {
                Log.d("Error", "Eliminar2: "+  e.getMessage());
            }

        } catch (Throwable e) {
            Log.d("Error", e.getMessage());
        }
    }



}