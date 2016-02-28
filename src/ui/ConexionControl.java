package ui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;


/**
 *
 * @author JuanJoseMoya
 */
public class ConexionControl extends Thread {
    //atributos de la clase
    private Socket skControl;
    private final String host="localhost";
    private final int  puertoControl=9900;
    private ArrayList<String> conectados;
    private JList lista;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String nick;

    public ConexionControl(JList lista,String nick) {
        this.lista=lista;
        this.nick=nick;
    }
    
    //creacion del metodo de conexion con el servidor
    public void serverContect() {
        try {
            skControl = new Socket(host, puertoControl);
            System.out.println("Se ha estabelido la conexión de control");
            negocioDeEnvio();
        } catch (IOException ex) {
            Logger.getLogger(ConexionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void run() {
        serverContect();
    }
    
    private void negocioDeEnvio(){
        try {
            in = new ObjectInputStream(skControl.getInputStream());
            out = new ObjectOutputStream(skControl.getOutputStream());
            while(true){
                switch(in.readUTF()){
                case estados.GET_NICK:
                    out.writeUTF(estados.SEND_NICK);
                    out.flush();
                    System.out.println("recibo peticion nick");
                    out.writeUTF(nick);
                    out.flush();
                    System.out.println("le envio el nick");
                    break;
                case estados.LISTEN:
                    System.out.println("recibo peticion de escucha");
                    out.writeUTF(estados.GET_USER);
                    out.flush();
                    System.out.println("le envio la peticion de usuarios");
                    while(true){
                        in = new ObjectInputStream(skControl.getInputStream());
                        System.out.println("A la espera de la nueva lista");
                        conectados = (ArrayList<String>) in.readObject();
                        System.out.println("Recibo la lista de clientes");
                        lista.setListData(conectados.toArray());
                    }      
            }
            }
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ConexionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
    

