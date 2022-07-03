package com.example.agenda;


import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity<Arraylist> extends AppCompatActivity {

    private EditText codigoET, nombreET, apellidosET, telefonoET, emailET;
    private TextView acercaD;
    private Button botonAceptar;
    public static Spinner grupoSpinner;
    static ArrayList<String> arrayGrupos = new ArrayList<String>();
    static String nombreGrupo = "";
    static Boolean grupoEliminado = false;
    static ArrayList<Contacto> arrayContactos = new ArrayList<Contacto>();
    static boolean contrario = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        codigoET = (EditText) findViewById((R.id.codigoET));
        nombreET = (EditText) findViewById((R.id.nombreET));
        apellidosET = (EditText) findViewById((R.id.apellidosET));
        telefonoET = (EditText) findViewById((R.id.telefonoET));
        emailET = (EditText)findViewById((R.id.emailET));
        acercaD = (TextView)findViewById((R.id.acercaTV));
        botonAceptar = (Button)findViewById(R.id.botonAceptar);
        grupoSpinner = (Spinner) findViewById((R.id.grupoSpinner));
        rellenarSpinner();
        if (grupoEliminado)
            eliminarGrupoContactos();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.idAcercaD){
            mostrarCopyright();
        }
        else if (id == R.id.idSalir){
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        else if (id == R.id.idGrupo){
            try {
                irDeItemAActivity("CrearGrupo");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (id == R.id.idBuscar){
            try {
                irDeItemAActivity("BuscarAvanzado");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void mostrarCopyright(View view){
        if (!contrario) {
            botonAceptar.setVisibility(View.VISIBLE);
            acercaD.setVisibility(View.VISIBLE);
        }else{
            botonAceptar.setVisibility(View.INVISIBLE);
            acercaD.setVisibility(View.INVISIBLE);
        }
        contrario = !contrario;
    }

    public void mostrarCopyright(){
        if (!contrario) {
            botonAceptar.setVisibility(View.VISIBLE);
            acercaD.setVisibility(View.VISIBLE);
        }else{
            botonAceptar.setVisibility(View.INVISIBLE);
            acercaD.setVisibility(View.INVISIBLE);
        }
        contrario = !contrario;
    }

    public void irDeItemAActivity(String strNombreActivity) throws ClassNotFoundException {
        String ponerBoton = "si";
        Intent crearGrupo = new Intent(this, Class.forName(("com.example.agenda." + strNombreActivity)));
        crearGrupo.putExtra("ponerBoton", ponerBoton);
        startActivity(crearGrupo);
    }

    // Rellenar Spinner
    public void rellenarSpinner() {
        grupoSpinner.setAdapter(null);
        arrayGrupos.clear();
        arrayGrupos.add("");
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        Cursor fila = BaseDeDatos.rawQuery("select nombreGrupo from grupo", null);

        while (fila.moveToNext()) {
            arrayGrupos.add(fila.getString(0));
            BaseDeDatos.close();
        }
        grupoSpinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_geekipedia, arrayGrupos));
    }

    // Introducir un contacto
    public void altaContacto(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo = codigoET.getText().toString();
        String nombre = nombreET.getText().toString();
        String apellidos = apellidosET.getText().toString();
        String telefono = telefonoET.getText().toString();
        String email = emailET.getText().toString();
        String grupo = "";
        if (grupoSpinner.getCount() > 0) {
            grupo = grupoSpinner.getSelectedItem().toString();
        }
        if (!codigo.isEmpty()) {
            Toast.makeText(this, "El código no se puede introducir", Toast.LENGTH_SHORT).show();
            codigoET.setText("");
        }


        if ((!nombre.isEmpty() && !apellidos.isEmpty() && !telefono.isEmpty() && !email.isEmpty()) && codigo.isEmpty()) {
            if (comprobarEmail(email)) {
                if (telefono.length() == 9) {
                    ContentValues registro = new ContentValues();
                    registro.put("nombre", nombre);
                    registro.put("apellidos", apellidos);
                    registro.put("telefono", telefono);
                    registro.put("email", email);
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    String fecha = formatter.format(date);
                    registro.put("fecha", fecha);

                    if (grupoSpinner.getCount() > 0) {
                        registro.put("grupo", grupo);
                    }

                    BaseDeDatos.insert("agenda", null, registro);
                    BaseDeDatos.close();

                    nombreET.setText("");
                    apellidosET.setText("");
                    telefonoET.setText("");
                    emailET.setText("");
                    if (grupoSpinner.getCount() > 0) {
                        grupoSpinner.setSelection(0);
                    }
                    Toast.makeText(this, "Contacto Guardado", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Formato erróneo de teléfono", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Formato erróneo de email", Toast.LENGTH_SHORT).show();
            }
        }


        if (codigo.isEmpty() && (nombre.isEmpty() || apellidos.isEmpty() || telefono.isEmpty() || email.isEmpty())) {
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }


    // Consultar un contacto
    public void buscarContacto(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo = codigoET.getText().toString();

        if (!codigo.isEmpty()) {
            Cursor fila = BaseDeDatos.rawQuery("select nombre, apellidos, telefono, email, grupo from agenda where codigo=" + codigo, null);
            if (fila.moveToFirst()) {
                nombreET.setText(fila.getString(0));
                apellidosET.setText(fila.getString(1));
                telefonoET.setText(fila.getString(2));
                emailET.setText(fila.getString(3));
                int pos = 0;
                if (grupoSpinner.getCount() > 0) {
                    for (int i = 0; i < grupoSpinner.getCount(); i++) {
                        if (grupoSpinner.getItemAtPosition(i).equals(fila.getString(4))) {
                            pos = i;
                        }
                    }
                    grupoSpinner.setSelection(pos);
                }
                BaseDeDatos.close();
            } else {
                Toast.makeText(this, "No existe el contacto", Toast.LENGTH_SHORT).show();
                BaseDeDatos.close();
            }
        } else {
            Toast.makeText(this, "Debes introducir el código del contacto", Toast.LENGTH_SHORT).show();
        }
    }

    // Eliminar un contacto
    public void eliminarContacto(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo = codigoET.getText().toString();

        if (!codigo.isEmpty()) {
            int cantidad = BaseDeDatos.delete("agenda", "codigo=" + codigo, null);
            BaseDeDatos.close();

            nombreET.setText("");
            apellidosET.setText("");
            telefonoET.setText("");
            emailET.setText("");
            if (grupoSpinner.getCount() > 0) {
                grupoSpinner.setSelection(0);
            }
            if (cantidad == 1) {
                Toast.makeText(this, "Contacto eliminado con éxito", Toast.LENGTH_SHORT).show();
                codigoET.setText("");
                nombreET.setText("");
                apellidosET.setText("");
                telefonoET.setText("");
                emailET.setText("");
                if (grupoSpinner.getCount() > 0) {
                    grupoSpinner.setSelection(0);
                }

            } else {
                Toast.makeText(this, "No existe el contacto", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Debes introducir el código del contacto", Toast.LENGTH_SHORT).show();
        }
    }

    // Modificar un contacto
    public void modificarContacto(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo = codigoET.getText().toString();
        String nombre = nombreET.getText().toString();
        String apellidos = apellidosET.getText().toString();
        String telefono = telefonoET.getText().toString();
        String email = emailET.getText().toString();
        String grupo = "";
        if (grupoSpinner.getCount() > 0) {
            grupo = grupoSpinner.getSelectedItem().toString();
        }

        if (!codigo.isEmpty() && !nombre.isEmpty() && !apellidos.isEmpty() && !telefono.isEmpty() && !email.isEmpty()) {
            if (comprobarEmail(email)) {
                ContentValues registro = new ContentValues();
                registro.put("nombre", nombre);
                registro.put("apellidos", apellidos);
                registro.put("telefono", telefono);
                registro.put("email", email);
                if (grupoSpinner.getCount() > 0) {
                    registro.put("grupo", grupo);
                }

                int cantidad = BaseDeDatos.update("agenda", registro, "codigo=" + codigo, null);
                BaseDeDatos.close();

                if (cantidad == 1) {
                    Toast.makeText(this, "Contacto modificado con éxito", Toast.LENGTH_SHORT).show();
                    codigoET.setText("");
                    nombreET.setText("");
                    apellidosET.setText("");
                    telefonoET.setText("");
                    emailET.setText("");
                    if (grupoSpinner.getCount() > 0) {
                        grupoSpinner.setSelection(0);
                    }
                } else {
                    Toast.makeText(this, "No existe el contacto", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Formato erróneo de email", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void crearGrupo(View view) {
        String ponerBoton = "no";
        Intent crearGrupo = new Intent(this, CrearGrupo.class);
        crearGrupo.putExtra("ponerBoton", ponerBoton);
        startActivity(crearGrupo);

    }

    public void gestionGrupo(View view) {
        String ponerBoton = "si";
        Intent crearGrupo = new Intent(this, CrearGrupo.class);
        crearGrupo.putExtra("ponerBoton", ponerBoton);
        startActivity(crearGrupo);
    }

    public void busquedaAvanzada(View view) {
        Intent buscarAvanzado = new Intent(this, BuscarAvanzado.class);
        startActivity(buscarAvanzado);
    }

    public void eliminarGrupoContactos() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();
        Cursor fila = BaseDeDatos.rawQuery("select nombre, apellidos, telefono, email from agenda where grupo Like \"" + nombreGrupo + "\"", null);
        ContentValues registro = null;
        while (fila.moveToNext()) {
            String nombre = fila.getString(0);
            String apellidos = fila.getString(1);
            String telefono = fila.getString(2);
            String email = fila.getString(3);

            registro = new ContentValues();
            registro.put("nombre", nombre);
            registro.put("apellidos", apellidos);
            registro.put("telefono", telefono);
            registro.put("email", email);
            registro.put("grupo", "");
        }

        int cantidad = BaseDeDatos.update("agenda", registro, "grupo Like \"" + nombreGrupo + "\"", null);
        if (cantidad >= 1) {
            Toast.makeText(this, "Agenda Actualizada", Toast.LENGTH_SHORT).show();
            BaseDeDatos.close();
        }
        grupoEliminado = false;
    }

    public boolean comprobarEmail(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);
        return mather.find();
    }
}