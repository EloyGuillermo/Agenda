package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class CrearGrupo extends AppCompatActivity {

    private EditText nombreGET;
    private Button botonEliminar, botonModificar;
    private Spinner grupoSpinner2;
    String nombreGrupoViejo;
    String nombreGrupoNuevo;
    Bundle ponerBoton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_grupo);

        nombreGET = (EditText) findViewById((R.id.nombreGET));
        botonEliminar =(Button) findViewById((R.id.botonEliminar));
        botonModificar = (Button) findViewById((R.id.botonModifico));
        grupoSpinner2 = (Spinner) findViewById((R.id.grupoSpinner2));
        ponerBoton = getIntent().getExtras();
        String botones = ponerBoton.getString("ponerBoton");
        rellenarSpinner2();
        if (botones.equals("no")) {
            botonEliminar.setVisibility(View.INVISIBLE);
            botonModificar.setVisibility(View.INVISIBLE);
            grupoSpinner2.setVisibility(View.INVISIBLE);
        }
    }

    // Rellenar Spinner
    public void rellenarSpinner2() {
        grupoSpinner2.setAdapter(null);
        MainActivity.arrayGrupos.clear();
        MainActivity.arrayGrupos.add("");
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        Cursor fila = BaseDeDatos.rawQuery("select nombreGrupo from grupo", null);

        while (fila.moveToNext()) {
            MainActivity.arrayGrupos.add(fila.getString(0));
            BaseDeDatos.close();
        }
        grupoSpinner2.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_geekipedia, MainActivity.arrayGrupos));
    }

    // Introducir un grupo
    public void altaGrupo(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String nombreGrupo = nombreGET.getText().toString();
        if (!nombreGrupo.isEmpty()) {
            ContentValues registro = new ContentValues();
            registro.put("nombreGrupo", nombreGrupo);
            BaseDeDatos.insert("grupo", null, registro);
            BaseDeDatos.close();

            nombreGET.setText("");
            Toast.makeText(this, "Grupo Guardado", Toast.LENGTH_SHORT).show();
            MainActivity.arrayGrupos.add(nombreGrupo);
            irAMenu();

        } else {
            Toast.makeText(this, "Debes introducir el nombre del grupo", Toast.LENGTH_SHORT).show();
        }
    }

    // Modificar un grupo
    public void modificarGrupo(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();
        nombreGrupoNuevo = nombreGET.getText().toString();
        nombreGrupoViejo = grupoSpinner2.getSelectedItem().toString();
        if (!nombreGrupoNuevo.isEmpty()) {
            if (nombreGrupoViejo.isEmpty()) {
                Toast.makeText(this, "Debes seleccionar un grupo", Toast.LENGTH_SHORT).show();
            } else {
                ContentValues registro = new ContentValues();
                registro.put("nombreGrupo", nombreGrupoNuevo);

                int cantidad = BaseDeDatos.update("grupo", registro, "nombreGrupo Like \"" + nombreGrupoViejo + "\"", null);
                if (cantidad >= 1) {
                    Toast.makeText(this, "Grupo modificado con éxito", Toast.LENGTH_SHORT).show();
                    modificarGrupoContactos();
                    irAMenu();
                }
                BaseDeDatos.close();
            }
        } else {
            Toast.makeText(this, "Introduce el nuevo nombre", Toast.LENGTH_SHORT).show();
        }
    }

    public void modificarGrupoContactos() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        Cursor fila = BaseDeDatos.rawQuery("select nombre, apellidos, telefono, email from agenda where grupo Like \"" + nombreGrupoViejo + "\"", null);
        ContentValues registro = null;
        while (fila.moveToNext()) {
            registro = new ContentValues();
            registro.put("grupo", nombreGrupoNuevo);
        }

        int cantidad = BaseDeDatos.update("agenda", registro, "grupo Like \"" + nombreGrupoViejo + "\"", null);
        if (cantidad >= 1) {
            Toast.makeText(this, "Agenda Actualizada", Toast.LENGTH_SHORT).show();
            BaseDeDatos.close();
        }
    }

    // Eliminar un grupo
    public void eliminarGrupo(View view) {
        ArrayList<Integer> codigos = new ArrayList<>();
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String nombreGrupo = nombreGET.getText().toString();


        if (!nombreGrupo.isEmpty()) {
            int cantidad = BaseDeDatos.delete("grupo", "nombreGrupo LIKE \"" + nombreGrupo + "\"", null);


            nombreGET.setText("");

            if (cantidad >= 1) {
                Toast.makeText(this, "Grupo eliminado con éxito", Toast.LENGTH_SHORT).show();

                codigos = buscarCodigo(nombreGrupo);
                for (int i = 0; i < codigos.size(); i++) {
                    int cant = BaseDeDatos.delete("grupo", "codigo =" + codigos.get(i + 1), null);
                    MainActivity.arrayGrupos.remove((i + 1));
                    Toast.makeText(this, "manolo", Toast.LENGTH_SHORT).show();
                }
                MainActivity.nombreGrupo = nombreGrupo;
                MainActivity.grupoEliminado = true;
                irAMenu();
                nombreGET.setText("");
                BaseDeDatos.close();
            } else {
                Toast.makeText(this, "No existe el grupo", Toast.LENGTH_SHORT).show();
                BaseDeDatos.close();
            }

        } else {
            Toast.makeText(this, "Debes introducir el nombre del grupo", Toast.LENGTH_SHORT).show();
        }
    }

    public void irAMenu() {
        Intent menu = new Intent(this, MainActivity.class);
        startActivity(menu);
    }

    public ArrayList<Integer> buscarCodigo(String nombreGrupo) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();
        ArrayList<Integer> codigos = new ArrayList<>();
        Cursor fila = BaseDeDatos.rawQuery("select codigo from grupo where nombreGrupo Like \"" + nombreGrupo + "\"", null);
        while (fila.moveToNext()) {
            codigos.add(fila.getInt(0));
            BaseDeDatos.close();
        }
        return codigos;
    }
}