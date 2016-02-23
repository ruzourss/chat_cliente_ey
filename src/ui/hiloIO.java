package ui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Tautvydas
 */
public class hiloIO extends Thread{
    
    private JTextArea area;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket canal;
    private JTextField nick;
    private boolean sesion;
    private ArrayList<String> historial;
    private JFrame ventanaLogin;
    private JFrame ventanaPrincipal;

    public hiloIO() {
    }

    public hiloIO(JTextArea area, Socket canal,JTextField nick,JFrame ventanaLogin,JFrame ventanaPrincipal) {
        this.area = area;
        this.canal = canal;
        this.nick=nick;
        sesion=true;
        historial= new ArrayList<>();
    }
    
    
    @Override
    public void run() {
       obtenerCanales();
       
    }
    
    private void obtenerCanales(){
        try {
            in = new ObjectInputStream(canal.getInputStream());
            out = new ObjectOutputStream(canal.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(hiloIO.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    
    public void controlDeFlujos(){
        try {         
            while(sesion){
                out.writeUTF(estados.SEND_NICK);
                out.flush();
                switch(in.readUTF()){
                case estados.GET_NICK:
                    out.writeUTF(nick.getText());
                    out.flush();
                    break;
                case estados.NICK_OK:
                    out.writeUTF(estados.GET_RECORD);
                    out.flush();
                    //a la espera de recibir el historial
                    historial = (ArrayList<String>) in.readObject();
                    //aviso al servidor de que le voy a enviar mensajes
                    out.writeUTF(estados.SEND_MESSAGES);
                    out.flush();
                    break;
                case estados.GET_MESSAGES:
                    ventanaLogin.setVisible(false);
                    ventanaPrincipal.setVisible(true);
                    
                    
                    
                    break;
                case estados.NICK_ERROR:
                    
                    break;
                }
            }
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(hiloIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(hiloIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    
    private void leerMensajes(){
        try {
            while(sesion){
                String mensajeRecibido = (String) in.readObject();
                area.append(mensajeRecibido);
            }
        } catch (IOException ex) {
            Logger.getLogger(hiloIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(hiloIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
