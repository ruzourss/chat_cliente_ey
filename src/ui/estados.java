package ui;

/**
 *clase que contiene los estados que puede tener nuestra comunicación
 */
public class estados {
    public static final String SEND_NICK = "0x0001";
    public static final String GET_NICK = "0x0002";
    public static final String NICK_ERROR = "0x0003";
    public static final String GET_MESSAGES = "0x0004";
    public static final String EXIT = "0x0005";
    public static final String NICK_OK = "0x0006";
    public static final String GET_RECORD = "0x0007";
    public static final String SEND_MESSAGES = "0x0008";
    public static final String SEND_USER = "0x0009";
    public static final String GET_USER = "0x0010";
    public static final String LISTEN = "0x0011";
}
