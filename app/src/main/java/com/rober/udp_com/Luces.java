package com.rober.udp_com;

import android.util.Log;

public class Luces {
    int ID;
    int Valor;
    int Color;

    public Luces(int[] varargs) {
        ID = varargs[0];
        Valor = varargs[1];
        Color = varargs[2];
    }

    public void mostrarLuces(){
        Log.i("mostrarDatos",
        "\n"+" ID-> " + ID
        +" Lights-> " + Valor
        +" Shaders-> " + Color +"\n"
        +"------------------------------------------------");
    }
}
