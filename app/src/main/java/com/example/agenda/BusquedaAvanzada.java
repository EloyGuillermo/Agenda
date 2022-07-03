package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class BusquedaAvanzada extends AppCompatActivity {

    private RecyclerView listaContactosRV;
    public static ArrayList<Contacto> arrayContactos;
    public static ArrayList<Contacto> arrayAuxiliar = new ArrayList<>();
    private ImageButton flechaIzq, flechaDer;
    private Button botonPag, botonPag1, botonPag2, botonPag3, botonPag4;
    Bundle nPag;
    public static int nPagMeEncuentro = 1;
    int nPagsTotales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_avanzada);

        arrayContactos = MainActivity.arrayContactos;

        botonPag = findViewById((R.id.botonPag));
        botonPag1 = findViewById((R.id.botonPag1));
        botonPag2 = findViewById((R.id.botonPag2));
        botonPag3 = findViewById((R.id.botonPag3));
        botonPag4 = findViewById((R.id.botonPag4));

        flechaIzq = findViewById((R.id.flechaIzq));
        flechaDer = findViewById((R.id.flechaDer));

        nPag = getIntent().getExtras();
        nPagsTotales = nPag.getInt("nPag");

        if (nPagsTotales == 1) {
            botonPag.setVisibility(View.INVISIBLE);
            botonPag1.setVisibility(View.INVISIBLE);
            botonPag2.setVisibility(View.INVISIBLE);
            botonPag3.setVisibility(View.INVISIBLE);
            botonPag4.setVisibility(View.INVISIBLE);
        } else if (nPagsTotales == 2) {
            botonPag.setX(200);
            botonPag1.setX(220);
            botonPag2.setVisibility(View.INVISIBLE);
            botonPag3.setVisibility(View.INVISIBLE);
            botonPag4.setVisibility(View.INVISIBLE);
        } else if (nPagsTotales == 3) {
            botonPag.setX(135);
            botonPag1.setX(155);
            botonPag2.setX(175);
            botonPag3.setVisibility(View.INVISIBLE);
            botonPag4.setVisibility(View.INVISIBLE);
        } else if (nPagsTotales == 4) {
            botonPag.setX(50);
            botonPag1.setX(70);
            botonPag2.setX(90);
            botonPag3.setX(110);
            botonPag4.setVisibility(View.INVISIBLE);
        }
        View view = findViewById(R.id.botonPag1);
        pagina1(view);
    }

    public void flechaDer(View view) {
        if (nPagMeEncuentro != nPagsTotales){
            nPagMeEncuentro ++;
            switch (nPagMeEncuentro){
                case 1:
                    pagina1(view);
                    break;
                case 2:
                    pagina2(view);
                    break;
                case 3:
                    pagina3(view);
                    break;
                case 4:
                    pagina4(view);
                    break;
                case 5:
                    pagina5(view);
                    break;
            }
        }
    }

    public void flechaIzq(View view) {
        if (nPagMeEncuentro != 1){
            nPagMeEncuentro --;
            switch (nPagMeEncuentro){
                case 1:
                    pagina1(view);
                    break;
                case 2:
                    pagina2(view);
                    break;
                case 3:
                    pagina3(view);
                    break;
                case 4:
                    pagina4(view);
                    break;
                case 5:
                    pagina5(view);
                    break;
            }
        }
    }

    public void pagina1(View view){
        arrayAuxiliar.clear();
        nPagMeEncuentro = 1;
        for (int i = 0 ; i < 5; i ++) {
            arrayAuxiliar.add(arrayContactos.get(i));
            if (i+1 == arrayContactos.size()){
                i = 4;
            }
        }

        botonPag.setBackgroundColor(Color.parseColor("#FF0000"));
        botonPag1.setBackgroundColor(Color.parseColor("#2196F3"));
        botonPag2.setBackgroundColor(Color.parseColor("#2196F3"));
        botonPag3.setBackgroundColor(Color.parseColor("#2196F3"));
        botonPag4.setBackgroundColor(Color.parseColor("#2196F3"));

        listaContactosRV = findViewById(R.id.listaContactosRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listaContactosRV.setLayoutManager(linearLayoutManager);
        RecyclerAdapter adapter = new RecyclerAdapter(arrayAuxiliar);
        listaContactosRV.setAdapter(adapter);
    }

    public void pagina2(View view) {
        arrayAuxiliar.clear();
        nPagMeEncuentro = 2;
        for (int i = 5 ; i < 10; i ++) {
            arrayAuxiliar.add(arrayContactos.get(i));
            if (i+1 == arrayContactos.size()){
                i = 9;
            }
        }

        botonPag.setBackgroundColor(Color.parseColor("#2196F3"));
        botonPag1.setBackgroundColor(Color.parseColor("#FF0000"));
        botonPag2.setBackgroundColor(Color.parseColor("#2196F3"));
        botonPag3.setBackgroundColor(Color.parseColor("#2196F3"));
        botonPag4.setBackgroundColor(Color.parseColor("#2196F3"));

        listaContactosRV = findViewById(R.id.listaContactosRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listaContactosRV.setLayoutManager(linearLayoutManager);
        RecyclerAdapter adapter = new RecyclerAdapter(arrayAuxiliar);
        listaContactosRV.setAdapter(adapter);
    }

    public void pagina3(View view) {
        arrayAuxiliar.clear();
        nPagMeEncuentro = 3;
        for (int i = 10 ; i < 15 ; i ++){
            arrayAuxiliar.add(arrayContactos.get(i));
            if (i+1 == arrayContactos.size()){
                i = 14;
            }
        }

        botonPag.setBackgroundColor(Color.parseColor("#2196F3"));
        botonPag1.setBackgroundColor(Color.parseColor("#2196F3"));
        botonPag2.setBackgroundColor(Color.parseColor("#FF0000"));
        botonPag3.setBackgroundColor(Color.parseColor("#2196F3"));
        botonPag4.setBackgroundColor(Color.parseColor("#2196F3"));

        listaContactosRV = findViewById(R.id.listaContactosRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listaContactosRV.setLayoutManager(linearLayoutManager);
        RecyclerAdapter adapter = new RecyclerAdapter(arrayAuxiliar);
        listaContactosRV.setAdapter(adapter);
    }

    public void pagina4(View view) {
        arrayAuxiliar.clear();
        nPagMeEncuentro = 4;
        for (int i = 15 ; i < 20; i ++){
            arrayAuxiliar.add(arrayContactos.get(i));
            if (i+1 == arrayContactos.size()){
                i = 19;
            }
        }

        botonPag.setBackgroundColor(Color.parseColor("#2196F3"));
        botonPag1.setBackgroundColor(Color.parseColor("#2196F3"));
        botonPag2.setBackgroundColor(Color.parseColor("#2196F3"));
        botonPag3.setBackgroundColor(Color.parseColor("#FF0000"));
        botonPag4.setBackgroundColor(Color.parseColor("#2196F3"));

        listaContactosRV = findViewById(R.id.listaContactosRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listaContactosRV.setLayoutManager(linearLayoutManager);
        RecyclerAdapter adapter = new RecyclerAdapter(arrayAuxiliar);
        listaContactosRV.setAdapter(adapter);
    }

    public void pagina5(View view) {
        arrayAuxiliar.clear();
        nPagMeEncuentro = 5;
        for (int i = 20 ; i < 25; i ++){
            arrayAuxiliar.add(arrayContactos.get(i));
            if (i+1 == arrayContactos.size()){
                i = 24;
            }
        }

        botonPag.setBackgroundColor(Color.parseColor("#2196F3"));
        botonPag1.setBackgroundColor(Color.parseColor("#2196F3"));
        botonPag2.setBackgroundColor(Color.parseColor("#2196F3"));
        botonPag3.setBackgroundColor(Color.parseColor("#2196F3"));
        botonPag4.setBackgroundColor(Color.parseColor("#FF0000"));

        listaContactosRV = findViewById(R.id.listaContactosRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listaContactosRV.setLayoutManager(linearLayoutManager);
        RecyclerAdapter adapter = new RecyclerAdapter(arrayAuxiliar);
        listaContactosRV.setAdapter(adapter);
    }
}