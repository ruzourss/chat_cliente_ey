package ui;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JuanJoseMoya
 */
public class ConexionMensajes extends Thread {

    //atributos de la clase

    private Socket skMensajes;
    private ObjectOutputStream OjectOSMensajeria;
    private ObjectInputStream OjectISMensajeria;
    private final String host = "localhost";
    private String nick;
    private String checknick;
    private final int puertoMesajeria = 8889;
    private boolean correcto = true;
    private ArrayList<String> historial;

    //constructor de la clase
    public ConexionMensajes() {
        //e inicializo el ArrayList de historial
        historial = new ArrayList<String>();
       
    }
    /**
     *Metodo para la comprobacion de que la conexion se hizo correctamente
     * @return
     */
    public boolean isCorrecto() {

        return correcto;

    }

    

    //creacion del metodo de conexion con el servidor
    public void serverContect() {

        try {
            // Creo el socket del cliente
            //skControl = new Socket(host, puertoControl);
            skMensajes = new Socket(host, puertoMesajeria);

            //mensaje de ok
            System.out.println("conexion establecida");

            // Creo los flujos de entrada y salida mensajeria
            OjectISMensajeria = new ObjectInputStream(skMensajes.getInputStream());
            OjectOSMensajeria = new ObjectOutputStream(skMensajes.getOutputStream());



        } catch (IOException ex) {
            correcto = false;
            System.out.println("Error exception");
            Logger.getLogger(ConexionMensajes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
        public boolean comprobarNick(String nick) throws IOException{
            //boolean nick
             boolean nick_ok=false;
            //envio de la señal para comprobar el nick
            OjectOSMensajeria.writeUTF("0x0001");

            //envio del nick de TextArea del Principal
            OjectOSMensajeria.writeUTF(nick);

            //retorno de comprobacion de nick 
            checknick = OjectISMensajeria.readUTF();

            //chequeo del nick
            do {

                if (checknick.equals("0x0006")) {
                    //nick correcto
                    System.out.println("Nick Correcto");
                    //variable mensaje
                    nick_ok = true;
                    //pedimos el historial al server
                    OjectOSMensajeria.writeUTF("0x0007");
                    
                    //recibiremos un Object con el historial de mensajes que contiene un ArrayList<String>
                    //MIRAR...........
                    //historial=OjectISMensajeria.readUTF();

                } else {
                    //nick incorrecto
                    nick_ok = false;
                    //mensaje
                    System.out.println("Nick Incorrecto");

                }

            } while (checknick.equals("0x0003"));
            
            return nick_ok;
        }


    @Override
    public void run() {

        serverContect();

    }

    //metodo que esta siempre para pedir informacion
    //Metodo que ejecute el enviar 
}

    //crear la  clase conexion en un hilo
    //atributos socket , object outupustream e imputStream, 
//imputStream en un hilo

