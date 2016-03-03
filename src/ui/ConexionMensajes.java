package ui;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * clase que realiza la conexión por el puerto 
 * de mensajería
 * @author JuanJoseMoya
 */
public class ConexionMensajes extends Thread {

    //atributos de la clase

    private Socket conexion;
    private final String host = "localhost";
    private final int puertoMesajeria = 9000;
    private boolean correcto = true;
    private final ArrayList<String> historial;
    private final Principal principal;

    /**
     * constructor por defecto
     */
    public ConexionMensajes(Principal ventanaLogin) {
        //e inicializo el ArrayList de historial
        historial = new ArrayList<String>();
        this.principal=ventanaLogin;  
    }
    /**
     *Metodo para la comprobacion de que la conexion se hizo correctamente
     * @return
     */
    public boolean isCorrecto() {
        return correcto;
    }

    /**
     * Método que se conecta al servidor del chat, si se ha podido
     * establecer la comnucación se lanza un hilo nuevo
     */
    public void serverContect() {

        try {
            // Creo el socket del cliente
            conexion = new Socket(host, puertoMesajeria);
            new hiloIO(conexion,principal).start();
            
        } catch (IOException ex) {
            correcto = false;
            JOptionPane.showMessageDialog(null,"No se pudo establecer la conexión");
        }
    }
        


    @Override
    public void run() {

        serverContect();

    }

    //metodo que esta siempre para pedir informacion
    //Metodo que ejecute el enviar 

    /**
     * @return the conexion
     */
    public Socket getConexion() {
        return conexion;
    }
}

    //crear la  clase conexion en un hilo
    //atributos socket , object outupustream e imputStream, 
//imputStream en un hilo

