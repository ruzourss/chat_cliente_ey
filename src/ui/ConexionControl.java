package ui;

import java.io.IOException;
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
    private inObject in;

    public ConexionControl(JList lista) {
        this.lista=lista;
    }
    
    //creacion del metodo de conexion con el servidor
    public void serverContect() {
        try {
            skControl = new Socket(host, puertoControl);
            System.out.println("Se ha estabelido la conexión de control");
            while(true){
                in = new inObject(skControl.getInputStream());
                conectados = (ArrayList<String>) in.readObject();
                lista.setListData(conectados.toArray());
            }
        } catch (IOException ex) {
            Logger.getLogger(ConexionControl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void run() {
        serverContect();
    }
}
    

