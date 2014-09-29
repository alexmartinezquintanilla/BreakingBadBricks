/**
 * BreakingBricks
 *
 * Personaje para juego creado en Examen
 *
 * @author José Elí Santiago Rodríguez A07025007, Alex Martinez Quintanilla
 * A00810480
 * @version 1.01 2014/09/17
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Toolkit;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

public class BreakingBricks extends JFrame implements Runnable, KeyListener {

    /* objetos para manejar el buffer del Applet y este no parpadee */
    // Imagen a proyectar en el JFrame
    private Image imaImagenJFrame;   	
    // Objeto grafico de la Imagen
    private Graphics graGraficaJFrame;  
    //Imagen de fondo
    private URL urlImagenBackG = this.getClass().getResource("fondo.png");
    //Contador de vidas
    private int iVidas;
    //Objeto de la clase personaje. El Bate (slider de brick breaker)
    private Personaje perBate;
    //Dirección en la que se mueve el bate.
    private int iDireccionBate;
    //Linked List para las charolas
    private LinkedList lnkCharolas;
    //Linked list para la pelota (temporal?)
    private LinkedList lnkPelotas;
    //Contador de puntos
    private int iScore;
    //Contador para las veces que el bate ha sido colisionado (a cambiar
    //por número de veces que la charola ha sido colisionada?
    private int iColisionesBate;
    // Objeto SoundClip cuando la pelota colisiona con el bate o la pared
    private SoundClip scSonidoColisionPelota;  
    //Objeto SoundClip para cuando la charola es golpeada la primera vez
    private SoundClip scSonidoColisionCharolaGolpeada; 
    //Objeto SoundClip para cuando la charola es destruida 
    private SoundClip scSonidoColisionCharolaRota; 
    //Objeto SoundClip para la música de fondo
    private SoundClip scSonidoBGM; 
    //Boleano para pausar el juego.
    private boolean bPausado;    
    //URL para cargar imagen de la charola
    private URL urlImagenCharola = 
            this.getClass().getResource("Charola/charola.png");
    //URL para cargar imagen de la charola golpeada
    private URL urlImagenCharolaGolpeada = 
            this.getClass().getResource("Charola/charolagolpeada.png");
    //URL para cargar imagen de la charola rota
    private URL urlImagenCharolaRota = 
            this.getClass().getResource("Charola/charolarota.gif");
    //URL para cargar la imagen de la pelota
    private URL urlImagenPelota = 
            this.getClass().getResource("pelota.gif");
    //URL para cargar la imagen de pausa
    private URL urlImagenPausa = this.getClass().getResource("pause.png");
    //Imagen al pausar el juego.
    Image imaImagenPausa = Toolkit.getDefaultToolkit().getImage(urlImagenPausa);
    private int iMovX;
    private int iMovY;

    //Constructor de BreakingBricks
    public BreakingBricks() {
        init();
        start();
    }

    /**
     * init
     *
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos a
     * usarse en el <code>Applet</code> y se definen funcionalidades.
     */
    public void init() {
        // hago el applet de un tamaño 800,600
        setSize(800, 700);

        // introducir instrucciones para iniciar juego
        bPausado = false; //Booleana para pausar

        //Inicializamos las vidas al azar entre 3 y 5
        iVidas = (int) (Math.random() * (6 - 3) + 3);

        //Inicializamos el score en 0
        iScore = 0;

        //inicializamos las colisiones de los corredores con nena
        iColisionesBate = 0;
        
        //inicializamos el movimiento en X en -1
        iMovX = 4;
        
        //inicializamos el movimiento en Y en -1
        iMovY = 4;

        // se crea imagen de Nena
        URL urlImagenNena = this.getClass().getResource("nena.png");

        // se crea a Nena 
        perBate = new Personaje(getWidth() / 2, getHeight() / 2,
                Toolkit.getDefaultToolkit().getImage(urlImagenNena));
        //Se inicializa con velocidad 3
        perBate.setVelocidad(5);

        // se posiciona a Nena en el centro de la pantalla y en la parte inferior
        perBate.setX((getWidth() / 2) - (perBate.getAncho() / 2));
//        perBate.setY((getHeight() / 2) - (perBate.getAlto() / 2));
        perBate.setY(getHeight() - perBate.getAlto());

        // se posiciona a Susy en alguna parte al azar del cuadrante 
        // superior izquierdo
        int posX;
        int posY;

        lnkCharolas = new LinkedList();

        //en este for se crean de 8 a 10 caminadores y se guardan en la lista de
        //caminadores
        int iAzar = (int) (Math.random() * (11 - 8) + 8);
        for (int iK = 1; iK <= iAzar; iK++) {
            posX = 0;
            posY = (int) (Math.random() * getHeight());
            // se crea el personaje caminador
            Personaje perCaminador;
            perCaminador = new Personaje(posX, posY,
                    Toolkit.getDefaultToolkit().getImage(urlImagenCharola));
            perCaminador.setX(0 - perCaminador.getAncho());
            perCaminador.setY((int) (Math.random() * (getHeight() 
                    - perCaminador.getAlto())));
            perCaminador.setVelocidad((int) (Math.random() * (5 - 3) + 3));
            lnkCharolas.add(perCaminador);
        }
        
        lnkPelotas = new LinkedList();

        //se crean de 8 a 10 caminadores y se guardan en la lista de caminadores
        iAzar = (int) (Math.random() * (16 - 10) + 10);
        for (int iK = 1; iK <= iAzar; iK++) {
            posX = (int) (Math.random() * getHeight());
            posY = 0;
            // se crea el personaje caminador
            Personaje perPelota;
            perPelota = new Personaje(posX, posY,
                    Toolkit.getDefaultToolkit().getImage(urlImagenPelota));
            perPelota.setX((int) (Math.random() * (getWidth() 
                    - perPelota.getAncho())));
            perPelota.setY(-perPelota.getAlto() 
                    - ((int) (Math.random() * getWidth())));
            lnkPelotas.add(perPelota);
        }
        
        //creo el sonido del bate golpeando la pelota
        scSonidoColisionPelota = new SoundClip("bate.wav");
        //creo el sonido  de la charola golpeada la primera vez
        scSonidoColisionCharolaGolpeada = new SoundClip("Charola/charolagolpeada.wav");
        //creo el sonido de la charola rompiéndose
        scSonidoColisionCharolaRota = new SoundClip("Charola/charolarota.wav");
        scSonidoBGM = new SoundClip("BGM.wav");
        scSonidoBGM.setLooping(true);
        scSonidoBGM.play();
        addKeyListener(this);
    }

    /**
     * start
     *
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo para la animacion este metodo
     * es llamado despues del init o cuando el usuario visita otra pagina y
     * luego regresa a la pagina en donde esta este <code>Applet</code>
     *
     */
    public void start() {
        // Declaras un hilo
        Thread th = new Thread(this);
        // Empieza el hilo
        th.start();
    }

    /**
     * run
     *
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones de
     * nuestro juego.
     *
     */
    public void run() {
        // se realiza el ciclo del juego en este caso nunca termina
        while (iVidas > 0) {
            /* mientras dure el juego, se actualizan posiciones de jugadores
             se checa si hubo colisiones para desaparecer jugadores o corregir
             movimientos y se vuelve a pintar todo
             */
            if (!bPausado) {
                actualiza();
                checaColision();
                repaint();
            }
            try {
                // El thread se duerme.
                Thread.sleep(20);
            } catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego "
                        + iexError.toString());
            }
        }
    }

    /**
     * actualiza
     *
     * Metodo que actualiza la posicion del objeto elefante
     *
     */
    public void actualiza() {
        // instrucciones para actualizar personajes

        //Nena actualiza movimiento dependiendo de la tecla que se presionó
        switch (iDireccionBate) {
//            case 1:
//                perBate.abajo();
//                break;
//            case 2:
//                perBate.arriba();
//                break;
            case 3:
                perBate.derecha();
                break;
            case 4:
                perBate.izquierda();
                break;
            case 5:
                perBate.setX(perBate.getX());
                break;

        }
        //Actualiza el movimiento de los caminadores
        for (Object lnkCaminadore : lnkCharolas) {
            Personaje perCaminador = (Personaje) lnkCaminadore;
            perCaminador.abajo();
        }
        //Actualiza el movimiento de los corredores
        for (Object lnkCorredore : lnkPelotas) {
            Personaje perPelota = (Personaje) lnkCorredore;
            perPelota.setX(perPelota.getX() + iMovX);
            perPelota.setY(perPelota.getY() + iMovY);
        }
        if (iColisionesBate >= 5) {
            iColisionesBate = 0;
            iVidas = iVidas - 1;
        }
    }

    /**
     * checaColision
     *
     * Metodo usado para checar la colision del objeto elefante con las orillas
     * del <code>Applet</code>.
     *
     */
    public void checaColision() {
        // instrucciones para checar colision y reacomodar personajes si 
        // es necesario
        //Checa colisiones de Nena
//        if (perBate.getY() + perBate.getAlto() > getHeight()) {
//            perBate.setY(getHeight() - perBate.getAlto());
//        }
//        if (perBate.getY() <= 0) {
//            perBate.setY(0);
//        }
        if ((perBate.getX() + perBate.getAncho()) >= getWidth()) {
            perBate.setX(getWidth() - perBate.getAncho());
        }
        if (perBate.getX() <= 0) {
            perBate.setX(0);
        }
        //Checa colisiones de los caminadores
        for (Object lnkCaminadore : lnkCharolas) {
            Personaje perCaminador = (Personaje) lnkCaminadore;
            if (perCaminador.getX() + perCaminador.getAncho() >= getWidth()) {
                perCaminador.setX(0 - perCaminador.getAncho());
                perCaminador.setY((int) (Math.random() * (getHeight() 
                        - perCaminador.getAlto())));
                perCaminador.setVelocidad((int) (Math.random() * (5 - 3) + 3));
            }
//            if (perCaminador.colisiona(perBate)) {
//                perCaminador.setX(0 - perCaminador.getAncho());
//                perCaminador.setY((int) (Math.random() * (getHeight() 
//                        - perCaminador.getAlto())));
//                perCaminador.setVelocidad((int) (Math.random() * (5 - 3) + 3));
//                iScore = iScore + 1;
//                scSonidoColisionCharolaGolpeada.play();
//            }
        }
        //Checa colisiones de los corredores
        for (Object lnkCorredore : lnkPelotas) {
            Personaje perPelota = (Personaje) lnkCorredore;
            if (perPelota.getX() + perPelota.getAncho() >= getWidth() || perPelota.getX() <= 0) {
                iMovX = -iMovX;
                scSonidoColisionPelota.play();
            }
            if (perPelota.colisiona(perBate)) {
                if (perPelota.getY() > perBate.getY()) {
                    iMovX = -iMovX;
                }
                else {
                iMovY = -iMovY;
                if (perPelota.getX() > (perBate.getX() + (perBate.getAncho() - perBate.getAncho() / 4)) && iMovX < 0) {
                    iMovX = -iMovX;
                }
                if (perPelota.getX() < (perBate.getX() + perBate.getAncho() / 4) && iMovX > 0) {
                    iMovX = -iMovX;
                }
                }
                scSonidoColisionPelota.play();
                
            }
            if (perPelota.getY() <= 0) {
                iMovY = -iMovY;
                scSonidoColisionPelota.play();
            }
            for (Object lnkCaminadore : lnkCharolas) {
            Personaje perCaminador = (Personaje) lnkCaminadore;
                if (perPelota.colisiona(perCaminador) && !perCaminador.getDead()) {
                    if (urlImagenCharolaRota != null && (perCaminador.getGolpes() > 1)) {
                    perCaminador.setDead(true);
                    perCaminador.setImagen(Toolkit.getDefaultToolkit().getImage(urlImagenCharolaRota));
                    perCaminador.setVelocidad(7);
                    scSonidoColisionCharolaRota.play();
                    }
                    else {
                        perCaminador.setImagen(Toolkit.getDefaultToolkit().getImage(urlImagenCharolaGolpeada));
                        perCaminador.setGolpes(perCaminador.getGolpes() + 1);
                        scSonidoColisionCharolaGolpeada.play();
                    }
//                    if ( iMovY > 0 ) {
//                        perPelota.setY(perCaminador.getY() - perPelota.getAlto());
//                        iMovY = -iMovY;
//                        scSonidoColisionCharolaGolpeada.play();
//                    }
//                    if ( iMovY < 0 ) {
//                    perPelota.setY(perCaminador.getY() +  perCaminador.getAlto());
//                    iMovY = -iMovY;
//                    scSonidoColisionCharolaGolpeada.play();
//                    }
                    iMovY = -iMovY;
                    scSonidoColisionCharolaGolpeada.play();
                }
            }
            if (perPelota.getY() + perPelota.getAlto() >= getHeight()) {
                iVidas += -1;
                perPelota.setX(perBate.getX() + perBate.getAncho() / 2);
                perPelota.setY(perBate.getY() - 30);
                scSonidoColisionPelota.play();
            }
        }
    }

    /**
     * update
     *
     * Metodo sobrescrito de la clase <code>Applet</code>, heredado de la clase
     * Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y define cuando
     * usar ahora el paint
     *
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     *
     */
    public void paint(Graphics graGrafico) {
        // Inicializan el DoubleBuffer
        if (imaImagenJFrame == null) {
            imaImagenJFrame = createImage(this.getSize().width,
                    this.getSize().height);
            graGraficaJFrame = imaImagenJFrame.getGraphics();
        }
        // creo imagen para el background
        Image imaImagenBackG = 
                Toolkit.getDefaultToolkit().getImage(urlImagenBackG);
        // Despliego la imagen
        graGraficaJFrame.drawImage(imaImagenBackG, 0, 0,
                getWidth(), getHeight(), this);

        // Actualiza el Foreground.
        graGraficaJFrame.setColor(getForeground());
        paint1(graGraficaJFrame);

        // Dibuja la imagen actualizada
        graGrafico.drawImage(imaImagenJFrame, 0, 0, this);
    }

    /**
     * paint1
     *
     * Metodo sobrescrito de la clase <code>Applet</code>, heredado de la clase
     * Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada, ademas
     * que cuando la imagen es cargada te despliega una advertencia.
     *
     * @param g es el <code>objeto grafico</code> usado para dibujar.
     *
     */
    public void paint1(Graphics g) {
        if (perBate != null & lnkCharolas != null & lnkPelotas != null) {

            //Dibuja la imagen de Nena en la posicion actualizada
            g.drawImage(perBate.getImagen(), perBate.getX(),
                    perBate.getY(), this);

            //Dibuja la imagen de los caminadores en la posicion actualizada
            for (Object lnkCaminadore : lnkCharolas) {
                Personaje perCaminador = (Personaje) lnkCaminadore;
                g.drawImage(perCaminador.getImagen(), perCaminador.getX(),
                        perCaminador.getY(), this);
            }

            //Dibuja la imagen de los corredores en la posicion actualizada
            for (Object lnkCorredore : lnkPelotas) {
                Personaje perPelota = (Personaje) lnkCorredore;
                g.drawImage(perPelota.getImagen(), perPelota.getX(),
                        perPelota.getY(), this);
            }

            //Despliega las vidas restantes
            g.setColor(Color.red);
            Font fFont = new Font("Verdana", Font.BOLD, 18);
            g.setFont(fFont);
            g.drawString("Quedan " + iVidas + " vidas!", 50, 80);
            g.drawString("Score : " + iScore, 50, 50);
        }
        if (iVidas <= 0) {

            //subo la imagen del bg
            URL urlImagenGOver = this.getClass().getResource("game_over.jpg");
            // creo imagen para el background
            Image imaImagenGOver = 
                    Toolkit.getDefaultToolkit().getImage(urlImagenGOver);
            // Despliego la imagen
            g.drawImage(imaImagenGOver, 0, 0,
                    getWidth(), getHeight(), this);
            g.drawString("Quedan " + iVidas + " vidas!", 50, 80);
            g.drawString("Score : " + iScore, 50, 50);
        }
        if (bPausado) {
            g.drawImage(imaImagenPausa, (((getWidth() / 2)) 
                    - imaImagenPausa.getWidth(this) / 2), (((getHeight() / 2)) 
                            - imaImagenPausa.getHeight(this) / 2), this);
                    
        }
    }

    public void grabaArchivo() throws IOException {
        PrintWriter fileOut = new PrintWriter(new FileWriter("datos.txt"));

        fileOut.println(lnkPelotas.size());
        for (Object lnkCorre1 : lnkPelotas) {
            Personaje perCorre = (Personaje) lnkCorre1;
            fileOut.println(perCorre.getX());
            fileOut.println(perCorre.getY());
            fileOut.println(perCorre.getVelocidad());
        }
        fileOut.println(lnkCharolas.size());
        for (Object lnkCamina1 : lnkCharolas) {
            Personaje perCamina = (Personaje) lnkCamina1;
            fileOut.println(perCamina.getX());
            fileOut.println(perCamina.getY());
            fileOut.println(perCamina.getVelocidad());
        }

        fileOut.println(perBate.getX());
        fileOut.println(perBate.getY());
        fileOut.println(iDireccionBate);
        fileOut.println(iScore);
        fileOut.println(iVidas);

        fileOut.close();

    }

    public void leeArchivo() throws IOException {
        lnkPelotas.clear();
        lnkCharolas.clear();
        BufferedReader fileIn;
        boolean bNoFileFound;
        try {
            fileIn = new BufferedReader(new FileReader("lvl1.txt"));
            bNoFileFound = false;
        } catch (FileNotFoundException e) {
            //Este pedazo de código es si quisieramos escribir un archivo en
            //Caso de que no existiera uno, pero no lo ocupamos porque usé algo
            //mejor.
//            File puntos = new File("datos.txt");
//            PrintWriter fileOut = new PrintWriter(puntos);
//            fileOut.println("1,1");
//            fileOut.close();
//            fileIn = new BufferedReader(new FileReader("datos.txt"));
            bNoFileFound = true;
            init();
        }
        if (!bNoFileFound) {
            fileIn = new BufferedReader(new FileReader("lvl1.txt"));
            String dato = fileIn.readLine();
            while (dato != null) {
                int iCorredores = Integer.parseInt(dato);
                for (int iK = 1; iK <= iCorredores; iK++) {
                    // se crea el personaje caminador
                    Personaje perCorre;
                    perCorre = new Personaje(0, 0,
                            Toolkit.getDefaultToolkit().
                                    getImage(urlImagenPelota));
                    perCorre.setX(Integer.parseInt(fileIn.readLine()));
                    perCorre.setY(Integer.parseInt(fileIn.readLine()));
                    perCorre.setVelocidad(Integer.parseInt(fileIn.readLine()));
                    lnkPelotas.add(perCorre);
                }

                int iCaminadores = Integer.parseInt(fileIn.readLine());
                for (int iK = 1; iK <= iCaminadores; iK++) {
                    // se crea el personaje caminador
                    Personaje perCamina;
                    perCamina = new Personaje(0, 0,
                            Toolkit.getDefaultToolkit().
                                    getImage(urlImagenCharola));
                    perCamina.setX(Integer.parseInt(fileIn.readLine()));
                    perCamina.setY(Integer.parseInt(fileIn.readLine()));
                    perCamina.setVelocidad(Integer.parseInt(fileIn.readLine()));
                    lnkCharolas.add(perCamina);
                }

                perBate.setX(Integer.parseInt(fileIn.readLine()));
                perBate.setY(Integer.parseInt(fileIn.readLine()));
                iDireccionBate = (Integer.parseInt(fileIn.readLine()));
                iScore = (Integer.parseInt(fileIn.readLine()));
                iVidas = (Integer.parseInt(fileIn.readLine()));
            }
            fileIn.close();
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        //To change body of generated methods, choose Tools | Templates.
        // si presiono flecha para Derecha
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            iDireccionBate = 3;  // cambio la dirección hacia derecha
        }
        // si presiono flecha para Izquierda
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            iDireccionBate = 4;   // cambio la dirección hacia izq
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
//        // si presiono flecha para abajo
//        if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
//            iDireccionBate = 1;  // cambio la dirección hacia abajo
//        }
//        // si presiono flecha para arriba
//        if (keyEvent.getKeyCode() == KeyEvent.VK_W) {
//            iDireccionBate = 2;   // cambio la dirección hacia arriba
//        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            iDireccionBate = 5;  // cambio la dirección hacia derecha
        }
        // si presiono flecha para Izquierda
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            iDireccionBate = 5;   // cambio la dirección hacia izq
        }
        //Si presionan P booleano Pausa cambia de estado.
        if (keyEvent.getKeyCode() == KeyEvent.VK_P) {

            bPausado = !bPausado; //Click a P pausa o despausa el juego
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_C) {

            try {
                leeArchivo(); //Carga datos
            } catch (IOException ex) {
                Logger.getLogger(BreakingBricks.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_G) {
            //Guarda datos
            try {
                grabaArchivo();
            } catch (IOException ex) {
                Logger.getLogger(BreakingBricks.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }
    }
}
