package ui;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import ui.control.control;
/**
 *
 * @author Tautvydas
 * Clase que extiende de hilo y crea un hilo en cada cliente
 * Esta clase  contiene los metodos necesarios para la conexion con el servidor
 * (Logueo y pass)
 */
public class hiloIO extends Thread{
    
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket canal;
    private boolean sesion;
    private ArrayList<String> historial;
    private final Principal principal;
    private Chat Chat;
    /**
     * Cosntructor de la clase que recibe el socket y el jframe Principal
     * @param canal Recibe el Socket de la comunicacion
     * @param principal  Panel Jframe
     */
    public hiloIO(Socket canal,Principal principal) {
        this.canal = canal;
        sesion=true;
        historial= new ArrayList<>();
        this.principal=principal;
    }
    
    
    @Override
    public void run() {
       obtenerCanales();
       controlDeFlujos();
    }
    /**
     * Metodo por el cual obtenemos los socket de comunicacion
     * para la comunicacion
     */
    private void obtenerCanales(){
        try {
            in = new ObjectInputStream(canal.getInputStream());
            out = new ObjectOutputStream(canal.getOutputStream());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(principal, "No se pudo conectar con el servidor");
            System.exit(-1);
        } 
    }
    
    /**
     * metodo que controla los flujos de informacion desde el cliente hacia el
     * servidor
     */
    private void controlDeFlujos(){
        //creacion de un objeto chat del que recogemos el nick
        Chat = new Chat(this,principal.getjTextFieldNick().getText());
        try {       
            //enviamos estadp de envio del nick
            out.writeUTF(estados.SEND_NICK);
            out.flush();
            //mientras la sesion este activa
            while(sesion){
                //recibimos los estados del servidor
                switch(in.readUTF()){
                //en el caso que recibamos obtener nick
                case estados.GET_NICK:
                    //recogemos el nick del textfield y lo enviamos
                    out.writeUTF(principal.getjTextFieldNick().getText());
                    out.flush();
                    System.out.println("Envio nick");
                    break;
                    //en el caso de que el nick este correcto
                case estados.NICK_OK:
                    //enviamos señal de que envie el historial
                    out.writeUTF(estados.GET_RECORD);
                    out.flush();
                    System.out.println("Dame el historial");
                    //a la espera de recibir el historial
                    historial = (ArrayList<String>) in.readObject();
                    System.out.println("Recibo historial");
                    cargaHistorial(historial);
                    //aviso al servidor de que le voy a enviar mensajes
                    out.writeUTF(estados.SEND_MESSAGES);
                    out.flush();
                    System.out.println("aviso de que voy enviar mensajes");
                    break;
                case estados.GET_MESSAGES:
                    System.out.println("preparado para enviar mensajes");
                    new control(Chat.getjListUsuarios()).start();
                    principal.setVisible(false);
                    Chat.setVisible(true);
                    //dejamos a este hilo que este siempre recibiendo los mensajes
                    leerMensajes();
                    break;
                case estados.NICK_ERROR:
                    principal.getjLabelError().setText("Nick repetido");
                    System.out.println("Error con el nick");
                    sesion=false;
                    break;
                default:
                    System.out.println("No se puedo leer el dato");
                    break;
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(principal, "No se pudo conectar con el servidor");
            System.exit(-1);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(principal, "No se pudo conectar con el servidor");
            System.exit(-1);
        }
    }
    
    
    private void leerMensajes(){
        try {
            while(sesion){
                String mensajeRecibido = (String) in.readObject();
                Chat.getjTextAreaPanel().append(mensajeRecibido+"\n");
                Chat.getjTextAreaPanel().setCaretPosition(Chat.getjTextAreaPanel().getDocument().getLength());
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(principal, "Se ha producido un error con la conxion con el servidor");
            System.exit(-1);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(principal, "Se ha producido un error con la conxion con el servidor");
            System.exit(-1);
        }
    }
    
    public void enviarMensaje(String mensaje){
        try {
            out.writeObject(mensaje);
            out.flush();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(principal, "Se ha producido un error con la conxion con el servidor");
            System.exit(-1);
        }
    }
    /**
     * Método que carga el historial de mensajes y lo muestra en la ventana principal
     * @param historico el array con los mensajes
     */
    private void cargaHistorial(ArrayList<String> historico){
        for(int i=0;i<historico.size();i++){
            Chat.getjTextAreaPanel().append(historico.get(i)+"\n");
        }
    }
    
}
