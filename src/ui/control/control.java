package ui.control;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import ui.inObject;

/**
 * clase que se encarga de recibir la lista de los usuarios
 * por el Socket
 * @author Tautvydas
 */
public class control extends Thread{
    
    private Socket canal;
    private JList lista;
    private ArrayList<String> listaUsuarios;
    private inObject in;
    /**
     * Constructor por defecto
     */
    public control() {
    }
    /**
     * Constructor que recibe
     * @param lista lista de la GUI
     */
    public control(JList lista) {
        this.lista = lista;
    }
    /**
     * método que establece la comunicación con el servidor
     */
    private void iniConexion(){
        try {
            canal = new Socket("localhost",9900);
            in=new inObject(canal.getInputStream());
            while(true){
                obtenerLista();
            }
        } catch (IOException ex) {
            Logger.getLogger(control.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Método que esta siempre a la espera de recibir la lista
     */
    private void obtenerLista(){
        try {
            listaUsuarios = (ArrayList<String>) in.readObject();
            System.out.println("Recibo la lista "+listaUsuarios.size());
            lista.setListData(listaUsuarios.toArray());
        } catch (IOException ex) {
            try {
                canal.close();
            } catch (IOException ex1) {
                Logger.getLogger(control.class.getName()).log(Level.SEVERE, null, ex1);
            }
           System.exit(-1);
        } catch (ClassNotFoundException ex) {
            try {
                canal.close();
            } catch (IOException ex1) {
                Logger.getLogger(control.class.getName()).log(Level.SEVERE, null, ex1);
            }
            System.exit(-1);
        }
    }

    @Override
    public void run() {
        iniConexion();
    }
    
    
}
