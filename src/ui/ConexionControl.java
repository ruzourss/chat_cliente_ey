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
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author JuanJoseMoya
 */
public class ConexionControl extends Thread {
    //atributos de la clase
    private Socket skControl;
    private ObjectOutputStream DataOSControl;
    private ObjectInputStream DataISControl;
    private final String host="localhost";
    private final int  puertoControl=9900;
    private boolean correcto=true;
    

    public  boolean isCorrecto(){
        
        return correcto;
   
    }
    //creacion del metodo de conexion con el servidor
    public void serverContect() {
        try {
            // Creo el socket del cliente
            //skControl = new Socket(host, puertoControl);
            skControl=new Socket(host, puertoControl);

            //mensaje de ok
            System.out.println("conexion establecida");
            
            // Creo los flujos de entrada y salida mensajeria
            DataISControl = new ObjectInputStream(skControl.getInputStream());
            DataOSControl= new ObjectOutputStream(skControl.getOutputStream());
            
            
            
        } catch (IOException ex) {
            correcto=false;
            System.out.println("Error exception");
            Logger.getLogger(ConexionControl.class.getName()).log(Level.SEVERE, null, ex);
        }

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

