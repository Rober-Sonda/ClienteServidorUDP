package com.rober.udp_com;

import android.os.Build;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static com.rober.udp_com.GlobalInfo.LISTAHABITACIONES;
import static com.rober.udp_com.GlobalInfo.LISTALUCES;

public class Comunicacion extends Thread{

    private MainActivity _main;
    DatagramSocket Sender;

    public Comunicacion(MainActivity main) throws SocketException {
        _main = main;
    }

    public void run() {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try{
                    Sender = new DatagramSocket(GlobalInfo.PORT);
                    Sender.setBroadcast(true);
                    int i = 0;
                    while(true){
                        byte[] Trama = new byte[1500];
                        //preparo un paquete para recibir la respuesta del servidor
                        DatagramPacket reciboDatos = new DatagramPacket(Trama, 0, Trama.length);;
                        Sender.receive(reciboDatos);
                        String a = GlobalInfo.getIP();
                        String b = reciboDatos.getAddress().getHostName();
                        if (!a.equals(b)){ //quiere decir que viene del servidor
                            String IPServer = String.valueOf(reciboDatos.getAddress());
                            mostrarInfoTxtenPantalla(reciboDatos.getData(), reciboDatos.getLength(), IPServer);
                            String msg = PrimerParteTrama(reciboDatos.getData(), ' ');
                            sleep(500);
                            comprobarTramas(msg, reciboDatos);
                        }
                    }
                    //preparo el paquete para luego recibir los datos de respuesta del servidor
                    //deposito los datos recibidos en el paquete declarado
                } catch (Exception e){
                    e.getMessage();
                }
            }
        }).start();

    }
    //Convierte un Array de tipo int a tipo String
    public int[] convertArrayIntStr(String[] array){
        int i = 0;
        int [] arrayint = new int[array.length];
        while(i < array.length) {
            try {
                arrayint[i]=Integer.parseInt(array[i]);
            }catch (Exception e){
                e.getMessage();
            }
            i++;
        }
        return arrayint;
    }
    //descubrir el servidor sophia
    public void Discover() {
        try{
            String strtrama = "DISCOVER ";
            SendTrama(strtrama);
        } catch (Exception e){
            e.getMessage();
        }
    }
    //Envia la trama Discover a toda la red
    public void SendTrama(String strtrama){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    DatagramPacket Paquete = null;
                    byte [] Datos = strtrama.getBytes();
                    Paquete = new DatagramPacket(Datos, Datos.length, InetAddress.getByName("255.255.255.255"), GlobalInfo.PORT);
                    Sender.send(Paquete);
                } catch (Exception e){
                    e.getMessage();
                }
            }
        }).start();

    }
    // muestra en pantalla el texto recibido
    public void mostrarInfoTxtenPantalla(byte[] buffer, int punteroPpal, String IPServer) throws IOException {
        String message = "";
        message += new String(buffer).substring(0, punteroPpal);

        TextView TxtMensaje = new TextView(_main);
        String finalMessage = message;
        _main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TxtMensaje.setText("Repuesta: " + " "+ finalMessage +"\r\n");
                TxtMensaje.setTextSize(15);
                TxtMensaje.setGravity(Gravity.LEFT);
                TextView chat = _main.findViewById(R.id.textViewChat);
                chat.append(TxtMensaje.getText());
            }
        });
    }
    //Envia la trama Login al servidor
    public void SendTramaLogin(){
        String user = "Roberto";
        String mipassword = "123456", miHashPass = GlobalInfo.sha1Hash(mipassword);
        String login2 = String.format("APP_LOGIN2 USER=%s PASS=%s PASS_SHA1=SHA1(%s)", user, mipassword, miHashPass);
        SendTrama(login2);
    }
    //Envia la trama Status al servidor
    public void SendTramaStatus(){
        String room_status = "ROOM_STATUS ID=345";
        SendTrama(room_status);
    }
    //Envia la trama Status al servidor
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void SendTramaLights(){
        if (LISTAHABITACIONES != null){
            LISTAHABITACIONES.forEach(habitacion -> {
                if (habitacion.ID==212){
                    if(habitacion.Lights>0){
                        habitacion.Lights = 0;
                        String lights_set = "LIGTHS_SET UID=385 ID=212 VALUE=0";
                        SendTrama(lights_set);
                    }else{
                        habitacion.Lights = 100;
                        String lights_set2 = "LIGTHS_SET UID=385 ID=212 VALUE=100";
                        SendTrama(lights_set2);
                    }
                }
            });
        }
    }
    public void SendPeticionRoomItems(){
        String room_items = "ROOMITEMS_STATUS ID=212";
        SendTrama(room_items);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void UpDownoneLight(){
        if (LISTALUCES != null){
            LISTALUCES.forEach(Luz -> {
                if(Luz.ID==140) {
                    if (Luz.Valor > 0) {
                        Luz.Valor = 0;
                        String LightsVal = "APP_CMD UID=385 ID=143 VALUE=0 RAMP=4 TIMER_OFF=0 COLOR=-168778";
                        SendTrama(LightsVal);
                    } else {
                        Luz.Valor = 100;
                        String LightsVal = "APP_CMD UID=385 ID=143 VALUE=100 RAMP=4 TIMER_OFF=0 COLOR=-168778";
                        SendTrama(LightsVal);
                    }
                }
            });
        }
    }
    //corta el encabezado de la trama basandose en el byte 0
    public String PrimerParteTrama(byte[] buffer2, byte hasta){
        String encabezado= "";
        int i = 0;
        while (buffer2[i] != hasta){
            encabezado += (char)(buffer2[i]);
            i++;
        }
        return encabezado;
    }
    //corta el encabezado de la trama basandose en el char ' '
    public String PrimerParteTrama(byte[] buffer2, char hasta){
        String encabezado= "";
        int i = 0;
        while (buffer2[i] != hasta){
            encabezado += (char)(buffer2[i]);
            i++;
        }
        return encabezado;
    }
    //Comprueba las tramas que envia el servido y actua en consecuencia
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void comprobarTramas(String msg, DatagramPacket reciboDatos){
        try{
            if (msg.startsWith("APP_BRIDGE")){
                System.out.println("APP_BRIDGE");
                SendTramaLogin();
            }
            if (msg.startsWith("LOGIN2_REPLY")) {
                System.out.println("LOGIN2_REPLY");
                SendTramaStatus();
            }
            if (msg.startsWith("ROOM_R_STATUS")) {
                String[] strArray = Datos(reciboDatos);
                HabitacionItemList(strArray);
//                LISTAHABITACIONES.forEach(habitacion1 -> {
//                    habitacion1.mostrarDatos();
//                });
            }
            if (msg.startsWith("LIGTHS_SET")) {
                System.out.println("LIGTHS_SET");
            }
            if (msg.startsWith("ROOMITEMS_R_STATUS")) {
                String[] strarray = Datos(reciboDatos);
                LightsItemList(strarray);
//                LISTALUCES.forEach(Luz -> {
//                    Luz.mostrarLuces();
//                });
            }

        }catch (Exception e){
            e.getMessage();
        }
    }
    //devolvemos un String[] de la trama recibida sin el encabezado
    public String[] Datos (DatagramPacket reciboDatos){
        String Info_Status = new String(reciboDatos.getData()).substring(0, reciboDatos.getLength());
        //seleccionamos el encabezado
        String Encabezado = PrimerParteTrama(Info_Status.getBytes(), (byte) 0);
        int posDatos = Encabezado.length() + 1;
        //obtenemos los datos sin encabezado
        Info_Status = new String(reciboDatos.getData()).substring(posDatos, reciboDatos.getLength());
        //pasamos el string a un array
        String[] strArray = Info_Status.split("\\r\\n");
        return strArray;
    }
    //creo una habitacion a partir de un String[] lo convierto a int[] y creo el nuevo objeto
    public void HabitacionItemList(String[] strArray) {
        int j = 0;
        String[] arrayFinal;
        Habitacion habitacion = null;
        try {
            while (j < strArray.length) {
                arrayFinal = strArray[j].split(";"); //paso un array
                int[] arrayFint = convertArrayIntStr(arrayFinal); //lo convierto a tipo integer
                habitacion = new Habitacion(arrayFint);
                LISTAHABITACIONES.add(habitacion);
                j++;
            }
        }catch (Exception e){
            e.getMessage();
        }
    }
    //creo una luz a partir de un String[] lo convierto a int[] y creo el nuevo objeto
    public void LightsItemList(String[] strArray) {
        int j = 0;
        String[] arrayfinal;
        Luces luz = null;
        try {
            while (j < strArray.length) {
                arrayfinal = strArray[j].split(";"); //paso un array
                int[] arrayfint = convertArrayIntStr(arrayfinal); //lo convierto a tipo integer
                luz = new Luces(arrayfint);
                LISTALUCES.add(luz);
                j++;
            }
        }catch (Exception e){
            e.getMessage();
        }
    }
}
