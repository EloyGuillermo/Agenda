package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class BuscarAvanzado extends AppCompatActivity {

    private EditText codigoET2, nombreET2, apellidosET2, telefonoET2, emailET2;
    private Spinner grupoSpinner3;
    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private Spinner spinner4;
    private Spinner spinner5;
    private ArrayList andOr = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_avanzado);

        andOr.add("AND");
        andOr.add("OR");

        codigoET2 = (EditText) findViewById((R.id.codigoET2));
        nombreET2 = (EditText) findViewById((R.id.nombreET2));
        apellidosET2 = (EditText) findViewById((R.id.apellidosET2));
        telefonoET2 = (EditText) findViewById((R.id.telefonoET2));
        emailET2 =(EditText)  findViewById((R.id.emailET2));
        grupoSpinner3 = (Spinner) findViewById((R.id.grupoSpinner3));

        spinner1 = (Spinner) findViewById((R.id.spinner1));
        spinner2 = (Spinner) findViewById((R.id.spinner2));
        spinner3 = (Spinner) findViewById((R.id.spinner3));
        spinner4 = (Spinner) findViewById((R.id.spinner4));
        spinner5 = (Spinner) findViewById((R.id.spinner5));
        rellenarSpinner3();
        rellenarSpinnners();
    }

    // Rellenar Spinner
    public void rellenarSpinner3() {
        grupoSpinner3.setAdapter(null);
        MainActivity.arrayGrupos.clear();
        MainActivity.arrayGrupos.add("");
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        Cursor fila = BaseDeDatos.rawQuery("select nombreGrupo from grupo", null);

        while (fila.moveToNext()) {
            MainActivity.arrayGrupos.add(fila.getString(0));
            BaseDeDatos.close();
        }
        grupoSpinner3.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_geekipedia, MainActivity.arrayGrupos));
    }

    // Rellenar Spinner
    public void rellenarSpinnners() {
        spinner1.setAdapter(null);
        spinner2.setAdapter(null);
        spinner3.setAdapter(null);
        spinner4.setAdapter(null);
        spinner5.setAdapter(null);

        spinner1.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_geekipedia, andOr));
        spinner2.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_geekipedia, andOr));
        spinner3.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_geekipedia, andOr));
        spinner4.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_geekipedia, andOr));
        spinner5.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_geekipedia, andOr));
    }

    public void busquedaAvanzada(View view) {
        String codigo = codigoET2.getText().toString();
        String nombre = nombreET2.getText().toString();
        String apellidos = apellidosET2.getText().toString();
        String telefono = telefonoET2.getText().toString();
        String email = emailET2.getText().toString();
        String grupo = "";

        if (grupoSpinner3.getCount() > 0) {
            grupo = grupoSpinner3.getSelectedItem().toString();
        }

        try {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
            SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

            String whereTocho = "select * from agenda where ";
            if (!codigo.isEmpty()) {
                whereTocho += "codigo LIKE \"%" + codigo+"%\"";
                if (!nombre.isEmpty() || !apellidos.isEmpty() || !telefono.isEmpty() || !email.isEmpty() || !grupo.isEmpty()) {
                    if (spinner1.getSelectedItem().toString().equals("AND")){
                        whereTocho += " AND ";
                    }
                    else{
                        whereTocho += " OR ";
                    }
                }
            }
            if (!nombre.isEmpty()) {
                whereTocho += "nombre LIKE \"%" + nombre + "%\"";
                if (!apellidos.isEmpty() || !telefono.isEmpty() || !email.isEmpty() || !grupo.isEmpty()) {
                    if (spinner2.getSelectedItem().toString().equals("AND")){
                        whereTocho += " AND ";
                    }
                    else{
                        whereTocho += " OR ";
                    }
                }
            }
            if (!apellidos.isEmpty()) {
                whereTocho += "apellidos LIKE \"%" + apellidos + "%\"";
                if (!telefono.isEmpty() || !email.isEmpty() || !grupo.isEmpty()) {
                    if (spinner3.getSelectedItem().toString().equals("AND")){
                        whereTocho += " AND ";
                    }
                    else{
                        whereTocho += " OR ";
                    }
                }
            }
            if (!telefono.isEmpty()) {
                whereTocho += "telefono LIKE \"%" + telefono + "%\"";
                if (!email.isEmpty() || !grupo.isEmpty()) {
                    if (spinner4.getSelectedItem().toString().equals("AND")){
                        whereTocho += " AND ";
                    }
                    else{
                        whereTocho += " OR ";
                    }
                }
            }
            if (!email.isEmpty()) {
                whereTocho += "email LIKE \"%" + email + "%\"";
                if (!grupo.isEmpty()) {
                    if (spinner5.getSelectedItem().toString().equals("AND")){
                        whereTocho += " AND ";
                    }
                    else{
                        whereTocho += " OR ";
                    }
                }
            }
            if (!grupo.isEmpty()) {
                whereTocho += "grupo LIKE \"" + grupo + "\"";
            }

            if (codigo.isEmpty() && nombre.isEmpty() && apellidos.isEmpty() && telefono.isEmpty() && email.isEmpty() && grupo.isEmpty()) {
               whereTocho = "select * from agenda";
                Toast.makeText(this, "Si no seleccionas nada, mostramos la lista completa", Toast.LENGTH_LONG).show();
            }

            Cursor fila = BaseDeDatos.rawQuery(whereTocho, null);

            MainActivity.arrayContactos.clear();
            while (fila.moveToNext()) {
                Contacto contacto = new Contacto(fila.getInt(0), fila.getString(1), fila.getString(2), fila.getString(3), fila.getString(4), fila.getString(5));
                MainActivity.arrayContactos.add(contacto);
            }
            double nPag = 0;
            double size = MainActivity.arrayContactos.size();
            nPag = size/5;
            int nPagina = (int) Math.ceil(nPag);

            if (!fila.moveToFirst()) {
                Toast.makeText(this, "No existe", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Mostramos la lista", Toast.LENGTH_LONG).show();
                Intent busquedaAvanzada = new Intent(this, BusquedaAvanzada.class);
                busquedaAvanzada.putExtra("nPag", nPagina);
                startActivity(busquedaAvanzada);
            }
            BaseDeDatos.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ha habido un error, comprueba los datos", Toast.LENGTH_SHORT).show();
        }

    }
}