/**
 * BreakingBricks
 *
 * Personaje para juego creado en Examen
 *
 * @author José Elí Santiago Rodríguez A07025007, Alex Martinez Quintanilla
 * A00810480
 * @version 1.01 2014/09/17
 */
//import java.awt.Color;
//import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Toolkit;
import java.awt.Font;
import javax.swing.JFrame;
//import javax.swing.JOptionPane;
import java.net.URL;
//import java.util.HashSet;
import java.util.LinkedList;
//import java.util.Set;
//import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
//import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.swing.ImageIcon;

public class BreakingBricks extends JFrame implements Runnable, KeyListener {

    /* objetos para manejar el buffer del JFrame y este no parpadee */
    // Imagen a proyectar en el JFrame
    private Image imaImagenJFrame;   	
    // Objeto grafico de la Imagen
    private Graphics graGraficaJFrame;  
    //Imagen de fondo
    private URL urlImagenBackG = this.getClass().getResource("fondo.png");
    //Contador de vidas
    private int iVidas;
    //Objeto de la clase personaje. El Bate (slider de brick breaker)
    private Personaje perCrowbar;
    //vida y score como personajes por si se implementa colision
    private Personaje perScoreVidas;
    //Objeto personaje mosca aliada
    private Personaje perMosca;
    //Dirección en la que se mueve el bate
    private int iDireccionBate;
    //Dirección de la mosca
    private int iDireccionMosca;
    //Linked List para las charolas
    private LinkedList lnkCharolas;
    //Linked list para la pelota (temporal?)
    private LinkedList lnkProyectiles;
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
    //URL para cargar imagen de la mosca
    private URL urlImagenMosca = this.getClass().getResource("mosco.gif");
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
    private URL urlImagenScoreVidas = this.getClass().getResource("scorevidas.png");
    //imagen para el score y las vidas
    Image imaImagenPausa = Toolkit.getDefaultToolkit().getImage(urlImagenPausa);
    private URL urlImagenInicio = this.getClass().getResource("pantallaInicio.png");
    //Imagen al pausar el juego.
    Image imaImagenInicio = Toolkit.getDefaultToolkit().getImage(urlImagenInicio);
    //X del proyectil
    private int iMovX;
    //Y del proyectil
    private int iMovY;
    //Booleana para saber si el juego comenzó o no
    private boolean bGameStarted;

    //Constructor de BreakingBricks
    public BreakingBricks() {
        init();
        start();
    }

    /**
     * init
     *
     * Metodo sobrescrito de la clase <code>JFrame</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos a
     * usarse en el <code>JFrame</code> y se definen funcionalidades.
     */
    public void init() {
        // hago el JFRAME de un tamaño 800,700
        setSize(800, 700);

        // introducir instrucciones para iniciar juego
        bPausado = false; //Booleana para pausar

        //Inicializamos las vidas al azar entre 3 y 5
        iVidas = (int) (Math.random() * (6 - 3) + 3);

        //Inicializamos el score en 0
        iScore = 0;

        //inicializamos las colisiones de los corredores con nena
        iColisionesBate = 0;
        
        //inicializamos el movimiento del proyectil en X en -1
        iMovX = 4;
        
        //inicializamos el movimiento del proyectil en Y en -1
        iMovY = 4;
        
        //inicializamos la variable que checa si ya empezó el juego en falso.
        bGameStarted = false;

        // se crea imagen del crowbar
        URL urlImagenCrowbar = this.getClass().getResource("crowbar.png");
        
        //se crea la imagen de la msoca
        URL urlImagenMosca = this.getClass().getResource("mosco.gif");
        
        //se crea el personaje mosca
        perMosca = new Personaje((int) (Math.random() * (this.getWidth() - 1) + 1), (int) (Math.random() * (this.getHeight() - 1) + 1),
                Toolkit.getDefaultToolkit().getImage(urlImagenMosca));
        perMosca.setVelocidad(7);

        // se crea a Nena 
        perCrowbar = new Personaje(getWidth() / 2, getHeight() / 2,
                Toolkit.getDefaultToolkit().getImage(urlImagenCrowbar));
        //Se inicializa con velocidad 3
        perCrowbar.setVelocidad(7);
        
        perScoreVidas = new Personaje (40, 550 ,
                Toolkit.getDefaultToolkit().getImage(urlImagenScoreVidas));

        // se posiciona al bate en el centro de la pantalla y en la parte inferior
        perCrowbar.setX((getWidth() / 2) - (perCrowbar.getAncho() / 2));
//        perCrowbar.setY((getHeight() / 2) - (perCrowbar.getAlto() / 2));
        perCrowbar.setY(getHeight() - perCrowbar.getAlto() -20);

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
            Personaje perCharola;
            perCharola = new Personaje(posX, posY,
                    Toolkit.getDefaultToolkit().getImage(urlImagenCharola));
            perCharola.setX(0 - perCharola.getAncho());
            perCharola.setY((int) (Math.random() * (getHeight() 
                    - perCharola.getAlto())));
            perCharola.setVelocidad((int) (Math.random() * (5 - 3) + 3));
            lnkCharolas.add(perCharola);
        }
        
        lnkProyectiles = new LinkedList();
        
        //imagen de las vidas y el score como personaje
           
        //se crean de 8 a 10 caminadores y se guardan en la lista de caminadores
        iAzar = (int) (Math.random() * (16 - 10) + 10);
        for (int iK = 1; iK <= iAzar; iK++) {
            posX = (int) (Math.random() * getHeight());
            posY = 0;
            // se crea el personaje caminador
            Personaje perProyectil;
            perProyectil = new Personaje(posX, posY,
                    Toolkit.getDefaultToolkit().getImage(urlImagenPelota));
            perProyectil.setX((int) (Math.random() * (getWidth() 
                    - perProyectil.getAncho())));
            perProyectil.setY(-perProyectil.getAlto() 
                    - ((int) (Math.random() * getWidth())));
            lnkProyectiles.add(perProyectil);
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
        iDireccionMosca = 0;
        addKeyListener(this);
    }

    /**
     * start
     *
     * Metodo sobrescrito de la clase <code>JFrame</code>.<P>
     * En este metodo se crea e inicializa el hilo para la animacion este metodo
     * es llamado despues del init o cuando el usuario visita otra pagina y
     * luego regresa a la pagina en donde esta este <code>JFrame</code>
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
            if (!bPausado && bGameStarted) {
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
              iDireccionMosca = ((int) (Math.random() * (5-1) + 1));
              switch(iDireccionMosca) {

                        case 1: {

                              perMosca.setY(perMosca.getY() - perMosca.getVelocidad());
                              break; //se mueve hacia arriba
                        }
                        case 2: {

                              perMosca.setY(perMosca.getY() + perMosca.getVelocidad());
                              break; //se mueve hacia abajo
                        }
                        case 3: {

                              perMosca.setX(perMosca.getX() - perMosca.getVelocidad());
                              break; //se mueve hacia la izquierda
                        }
                        case 4: {

                              perMosca.setX(perMosca.getX() + perMosca.getVelocidad());
                              break; //se mueve hacia la derecha
                        }
                }
        
        //Nena actualiza movimiento dependiendo de la tecla que se presionó
        switch (iDireccionBate) {
//            case 1:
//                perCrowbar.abajo();
//                break;
//            case 2:
//                perCrowbar.arriba();
//                break;
            case 3:
                perCrowbar.derecha();
                break;
            case 4:
                perCrowbar.izquierda();
                break;
            case 5:
                perCrowbar.setX(perCrowbar.getX());
                break;

        }
        //Actualiza el movimiento de los caminadores
        for (Object lnkCharola : lnkCharolas) {
            Personaje perCharola = (Personaje) lnkCharola;
            if (perCharola.getGolpes() > 1) {
            perCharola.setVelocidad(perCharola.getVelocidad() + 1);                
            perCharola.abajo();
            }
        }
        //Actualiza el movimiento de los corredores
        for (Object lnkProyectil : lnkProyectiles) {
            Personaje perProyectil = (Personaje) lnkProyectil;
            perProyectil.setX(perProyectil.getX() + iMovX);
            perProyectil.setY(perProyectil.getY() + iMovY);
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
     * del <code>JFrame</code>.
     *
     */
    public void checaColision() {
        // instrucciones para checar colision y reacomodar personajes si 
        // es necesario
        //Checa colisiones de Nena
//        if (perCrowbar.getY() + perCrowbar.getAlto() > getHeight()) {
//            perCrowbar.setY(getHeight() - perCrowbar.getAlto());
//        }
//        if (perCrowbar.getY() <= 0) {
//            perCrowbar.setY(0);
//        }
        if ((perCrowbar.getX() + perCrowbar.getAncho()) >= getWidth()) {
            perCrowbar.setX(getWidth() - perCrowbar.getAncho());
        }
        if (perCrowbar.getX() <= 0) {
            perCrowbar.setX(0);
        }
        //Checa colisiones de los caminadores
        for (Object lnkCharola : lnkCharolas) {
            Personaje perCharola = (Personaje) lnkCharola;
            if (perCharola.getY() >= getHeight()) {
                perCharola.setX(0 - perCharola.getAncho() * 2);
                perCharola.setY(0);
                perCharola.setVelocidad(0);
            }
//            if (perCharola.colisiona(perCrowbar)) {
//                perCharola.setX(0 - perCharola.getAncho());
//                perCharola.setY((int) (Math.random() * (getHeight() 
//                        - perCharola.getAlto())));
//                perCharola.setVelocidad((int) (Math.random() * (5 - 3) + 3));
//                iScore = iScore + 1;
//                scSonidoColisionCharolaGolpeada.play();
//            }
        }
        //Checa colisiones de los corredores
        for (Object lnkProyectil : lnkProyectiles) {
            Personaje perProyectil = (Personaje) lnkProyectil;
            if (perProyectil.getX() + perProyectil.getAncho() >= getWidth() || perProyectil.getX() <= 0) {
                iMovX = -iMovX;
                scSonidoColisionPelota.play();
            }
            if (perProyectil.colisiona(perCrowbar)) {
                iMovY = -iMovY;
                perProyectil.setY(perCrowbar.getY() - perProyectil.getAlto());
                if (perProyectil.getY() > perCrowbar.getY()) {
                    iMovX = -iMovX;
                }
                else {
                if (( perProyectil.getX() > (perCrowbar.getX() + (perCrowbar.getAncho() - perCrowbar.getAncho() / 4)) && iMovX < 0) || ( perProyectil.getX() + perProyectil.getAncho() > (perCrowbar.getX() + (perCrowbar.getAncho() - perCrowbar.getAncho() / 4)))) {
                    iMovX = -iMovX;
                    iMovX = 1;
                }
                else if ((perProyectil.getX() < (perCrowbar.getX() + perCrowbar.getAncho() / 4) && iMovX > 0) || (perProyectil.getX() + perProyectil.getAncho() < (perCrowbar.getX() + perCrowbar.getAncho() / 4) && iMovX > 0)) {
                    iMovX = -iMovX;
                    iMovX = 1;
                }
                else {
                    iMovX += 4;
                }
                }
                scSonidoColisionPelota.play();
                
            }
            if (perProyectil.getY() <= 0) {
                iMovY = -iMovY;
                scSonidoColisionPelota.play();
            }
            for (Object lnkCharola : lnkCharolas) {
            Personaje perCharola = (Personaje) lnkCharola;
                if (perProyectil.colisiona(perCharola) && !perCharola.getDead()) {
                    if (urlImagenCharolaRota != null && (perCharola.getGolpes() > 1)) {
                    perCharola.setDead(true);
                    perCharola.setImagen(Toolkit.getDefaultToolkit().getImage(urlImagenCharolaRota));
                    scSonidoColisionCharolaRota.play();
                    }
                    else {
                        perCharola.setImagen(Toolkit.getDefaultToolkit().getImage(urlImagenCharolaGolpeada));
                        perCharola.setGolpes(perCharola.getGolpes() + 1);
                        scSonidoColisionCharolaGolpeada.play();
                    }
//                    if ( iMovY > 0 ) {
//                        perProyectil.setY(perCharola.getY() - perProyectil.getAlto());
//                        iMovY = -iMovY;
//                        scSonidoColisionCharolaGolpeada.play();
//                    }
//                    if ( iMovY < 0 ) {
//                    perProyectil.setY(perCharola.getY() +  perCharola.getAlto());
//                    iMovY = -iMovY;
//                    scSonidoColisionCharolaGolpeada.play();
//                    }
                    iMovY = -iMovY;
                    scSonidoColisionCharolaGolpeada.play();
                }
            }
            //Colisiones de la mosca con las charolas
            for (Object lnkCharola : lnkCharolas) {
            Personaje perCharola = (Personaje) lnkCharola;
                if (perMosca.colisiona(perCharola) && !perCharola.getDead()) {
                    if (urlImagenCharolaRota != null && (perCharola.getGolpes() > 1)) {
                    perCharola.setDead(true);
                    perCharola.setImagen(Toolkit.getDefaultToolkit().getImage(urlImagenCharolaRota));
                    scSonidoColisionCharolaRota.play();
                    }
                    else {
                        perCharola.setImagen(Toolkit.getDefaultToolkit().getImage(urlImagenCharolaGolpeada));
                        perCharola.setGolpes(perCharola.getGolpes() + 1);
                        scSonidoColisionCharolaGolpeada.play();
                    }
                 
                    scSonidoColisionCharolaGolpeada.play();
                }
            }
            //Colisiones de la mosca con las orillas
            
            if (perMosca.getX() <0 )    {
                perMosca.setX(10);
            }
            if (perMosca.getX() > this.getWidth() -perMosca.getAncho() )    {
                perMosca.setX( this.getWidth()-perMosca.getAncho());
            }
            if (perMosca.getY() <0 )    {
                perMosca.setY(2);
            }
            if (perMosca.getY() > this.getHeight()-perMosca.getAlto() )    {
                perMosca.setY( this.getHeight() - perMosca.getAlto());
            }
            
            if (perProyectil.getY() + perProyectil.getAlto() >= getHeight()) {
                iVidas += -1;
                perProyectil.setX(perCrowbar.getX() + perCrowbar.getAncho() / 2);
                perProyectil.setY(perCrowbar.getY() - 30);
                scSonidoColisionPelota.play();
            }
        }
    }

    /**
     * update
     *
     * Metodo sobrescrito de la clase <code>JFrame</code>, heredado de la clase
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
     * Metodo sobrescrito de la clase <code>JFrame</code>, heredado de la clase
     * Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada, ademas
     * que cuando la imagen es cargada te despliega una advertencia.
     *
     * @param g es el <code>objeto grafico</code> usado para dibujar.
     *
     */
    public void paint1(Graphics g) {
        if (perCrowbar != null & lnkCharolas != null & lnkProyectiles != null) {

            //Dibuja la imagen de Nena en la posicion actualizada
            g.drawImage(perCrowbar.getImagen(), perCrowbar.getX(),
                    perCrowbar.getY(), this);
            
            //Dibuja a la mosca en la posición actualizada
            //Dibuja la imagen de Nena en la posicion actualizada
            g.drawImage(perMosca.getImagen(), perMosca.getX(),
                    perMosca.getY(), this);

            //Dibuja la imagen de los caminadores en la posicion actualizada
            for (Object lnkCharola : lnkCharolas) {
                Personaje perCharola = (Personaje) lnkCharola;
                g.drawImage(perCharola.getImagen(), perCharola.getX(),
                        perCharola.getY(), this);
            }

            //Dibuja la imagen de los corredores en la posicion actualizada
            for (Object lnkProyectil : lnkProyectiles) {
                Personaje perProyectil = (Personaje) lnkProyectil;
                g.drawImage(perProyectil.getImagen(), perProyectil.getX(),
                        perProyectil.getY(), this);
            }
            
            //dibuja la imagen del score y las vidas
            g.drawImage(perScoreVidas.getImagen(), perScoreVidas.getX(),
                    perScoreVidas.getY(), this);
            //Despliega las vidas restantes y el score
            g.setColor(Color.GREEN);
            Font fFont = new Font("Verdana", Font.BOLD, 18);
            g.setFont(fFont);
            g.drawString( " : " +iScore, 160,595);
            g.drawString( " : " +iVidas, 200,635);
            
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
            g.drawImage(perScoreVidas.getImagen(), perScoreVidas.getX(),
                    perScoreVidas.getY(), this);
            g.drawString( " : " +iScore, 160,595);
            g.drawString( " : " +iVidas, 200,635);
        }
        if (bPausado) {
            g.drawImage(imaImagenPausa, (((getWidth() / 2)) 
                    - imaImagenPausa.getWidth(this) / 2), (((getHeight() / 2)) 
                            - imaImagenPausa.getHeight(this) / 2), this);
                    
        }
        if (!bGameStarted) {
            g.drawImage(imaImagenInicio, (((getWidth() / 2)) 
                    - imaImagenInicio.getWidth(this) / 2), (((getHeight() / 2)) 
                            - imaImagenInicio.getHeight(this) / 2), this);
                    
        }
    }

    public void grabaArchivo() throws IOException {
        PrintWriter fileOut = new PrintWriter(new FileWriter("datos.txt"));

        fileOut.println(lnkProyectiles.size());
        for (Object lnkCorre1 : lnkProyectiles) {
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

        fileOut.println(perCrowbar.getX());
        fileOut.println(perCrowbar.getY());
        fileOut.println(iDireccionBate);
        fileOut.println(iScore);
        fileOut.println(iVidas);

        fileOut.close();

    }

    public void leeArchivo() throws IOException {
        lnkProyectiles.clear();
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
                    lnkProyectiles.add(perCorre);
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

                perCrowbar.setX(Integer.parseInt(fileIn.readLine()));
                perCrowbar.setY(Integer.parseInt(fileIn.readLine()));
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
            
            bGameStarted = true;

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
