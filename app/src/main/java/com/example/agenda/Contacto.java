package com.example.agenda;

public class Contacto {
    int codigo;
    String nombre, apellidos, telefono, email, grupo;

    public Contacto() {
    }

    public Contacto(int codigo, String nombre, String apellidos, String telefono, String email, String grupo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.email = email;
        this.grupo = grupo;
    }
    public int getCodigo(){
        return this.codigo;
    }
    public void setCodigo(int codigo){
        this.codigo = codigo;
    }
    public String getNombre(){
        return this.nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public String getApellidos(){
        return this.apellidos;
    }
    public void setApellidos(String apellidos){
        this.apellidos = apellidos;
    }
    public String getTelefono(){
        return this.telefono;
    }
    public void setTelefono(String telefono){
        this.telefono = telefono;
    }
    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getGrupo(){
        return this.grupo;
    }
    public void setGrupo(String grupo){
        this.grupo = grupo;
    }
}
