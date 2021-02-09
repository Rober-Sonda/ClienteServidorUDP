package com.rober.udp_com;

import android.util.Log;

import java.security.PublicKey;

public class Habitacion {

    int ID;
    int Lights;
    int Shaders;
    int Items;
    int Ir_devs;
    int Temp;
    int Bright;
    int Humidity;
    int Watts;
    int Precence;
    int Offline;
    int Chairs;

    public Habitacion(int[] varargs) {
        ID = varargs[0];
        Lights = varargs[1];
        Shaders = varargs[2];
        Items = varargs[3];
        Ir_devs = varargs[4];
        Temp = varargs[5];
        Bright = varargs[6];
        Humidity = varargs[7];
        Watts = varargs[8];
        Precence = varargs[9];
        Offline = varargs[10];
        Chairs = varargs[11];
    }

    public void mostrarDatos(){
        Log.i("mostrarDatos",
                "\n"+" ID-> " + ID
        +" Lights-> " + Lights
        +" Shaders-> " + Shaders
        +" Items-> " + Items
        +" Ir_devs-> " + Ir_devs
        +" Temp->"  + Temp
        +" Bright-> " + Bright
        +" Humidity-> " + Humidity
        +" Watts-> " + Watts
        +" Precence-> " + Precence
        +" Offline-> " + Offline
        +" Chairs-> " + Chairs+"\n"
        +"--------------------------------------------------------------------------------------");
    }
}

//    int ID;
//    int Lights;
//    int Shaders;
//    int Items;
//    int Ir_devs;
//    int Temp;
//    int Bright;
//    int Humidity;
//    int Watts;
//    int Precence;
//    int Offline;
//    int Chairs;


//    private static habitacion Objhabitacion;
//    private static habitacion getInstance() {
//        if (Objhabitacion == null) {
//            Objhabitacion = new habitacion();
//        }
//        return Objhabitacion;
//    }

//    private int ID;
//    private int Lights;
//    private int Shaders;
//    private int Items;
//    private int Ir_devs;
//    private int Temp;
//    private int Bright;
//    private int Humidity;
//    private int Watts;
//    private int Precence;
//    private int Offline;
//    private int Chairs;

//public habitacion(int[] args){
//    ID = args[0];
//    Lights = args[1];
//    Shaders = args[2];
//    Items = args[3];
//    Ir_devs = args[4];
//    Temp = args[5];
//    Bright = args[6];
//    Humidity = args[7];
//    Watts = args[8];
//    Precence = args[9];
//    Offline = args[10];
//    Chairs = args[11];
// }
