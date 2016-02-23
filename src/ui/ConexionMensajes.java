package ui;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author JuanJoseMoya
 */
public class ConexionMensajes extends Thread {

    //atributos de la clase

    private Socket conexion;
    private final String host = "localhost";
    private final int puertoMesajeria = 8889;
    private boolean correcto = true;
    private ArrayList<String> historial;
    private JFrame ventanaLogin;
    private JFrame ventanaPrincipal;
    private JTextArea area;
    private JTextField nick;

    /**
     * constructor por defecto
     */
    public ConexionMensajes(JTextArea area,JTextField nick,JFrame ventanaLogin,JFrame ventanaPrincipal) {
        //e inicializo el ArrayList de historial
        historial = new ArrayList<String>();
        this.area=area;
        this.nick=nick;
        this.ventanaLogin=ventanaLogin;
        this.ventanaPrincipal=ventanaPrincipal;
       
    }
    /**
     *Metodo para la comprobacion de que la conexion se hizo correctamente
     * @return
     */
    public boolean isCorrecto() {
        return correcto;
    }

    /**
     * Método que se conecta al servidor del chat
     */
    public void serverContect() {

        try {
            // Creo el socket del cliente
            //skControl = new Socket(host, puertoControl);
            conexion = new Socket(host, puertoMesajeria);
            new hiloIO(area, conexion,nick,ventanaLogin,ventanaPrincipal).start();
            
        } catch (IOException ex) {
            correcto = false;
            JOptionPane.showMessageDialog(null,"No se pudo establecer la conexión");
        }
    }
        
//        public boolean comprobarNick(String nick) throws IOException{
//            //boolean nick
//             boolean nick_ok=false;
//            //envio de la señal para comprobar el nick
//            OjectOSMensajeria.writeUTF("0x0001");
//
//            //envio del nick de TextArea del Principal
//            OjectOSMensajeria.writeUTF(nick);
//
//            //retorno de comprobacion de nick 
//            checknick = OjectISMensajeria.readUTF();
//
//            //chequeo del nick
//            do {
//
//                if (checknick.equals("0x0006")) {
//                    //nick correcto
//                    System.out.println("Nick Correcto");
//                    //variable mensaje
//                    nick_ok = true;
//                    //pedimos el historial al server
//                    OjectOSMensajeria.writeUTF("0x0007");
//                    
//                    //recibiremos un Object con el historial de mensajes que contiene un ArrayList<String>
//                    //MIRAR...........
//                    //historial=OjectISMensajeria.readUTF();
//
//                } else {
//                    //nick incorrecto
//                    nick_ok = false;
//                    //mensaje
//                    System.out.println("Nick Incorrecto");
//
//                }
//
//            } while (checknick.equals("0x0003"));
//            
//            return nick_ok;
//        }


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

