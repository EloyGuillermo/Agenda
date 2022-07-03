package com.example.agenda;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ContactosViewHolder>{

    ArrayList<Contacto> arrayContactos;
    public RecyclerAdapter(ArrayList<Contacto> arrayContactos){
        this.arrayContactos = arrayContactos;
    }

    @NonNull
    @Override
    public ContactosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listacontactos_row, null, false);
        return new ContactosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactosViewHolder holder, int position) {

        holder.nombreTV.setText("Nombre: "+arrayContactos.get(position).getNombre().toString());
        holder.telefonoTV.setText("Teléfono: "+arrayContactos.get(position).getTelefono().toString());
        holder.foto.setImageResource(R.drawable.imagenc);
    }

    @Override
    public int getItemCount() {
        return arrayContactos.size();
    }


    class ContactosViewHolder extends RecyclerView.ViewHolder{

        TextView nombreTV;
        TextView telefonoTV;
        ImageView foto;

        public ContactosViewHolder(View itemView) {
            super(itemView);
            nombreTV = itemView.findViewById(R.id.nombreTV);
            telefonoTV = itemView.findViewById(R.id.telefonoTV);
            foto = itemView.findViewById(R.id.fotoTV);
        }

        void bind(int listaIndex){
            nombreTV.setText(String.valueOf(listaIndex));
            telefonoTV.setText(String.valueOf(listaIndex));
        }
    }
}
