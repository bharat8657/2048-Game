package game2048;

import game2048.gamegraphik.ideaconsole.Game2048Console;
import game2048.gamegraphik.swingconsole.Game2048Graphic;
import org.hibernate.SessionFactory;

public class Main {

    private static final SessionFactory SESSION_FACTORY = new Factory().getSessionFactory();

    public static void main(String[] args) {

// uncomment below to play through the graphic window (swing)

        Game2048Graphic game2048Graphic = new Game2048Graphic(SESSION_FACTORY);
        game2048Graphic.start();


//uncomment below to play though the IDEA console

//        Game2048Console game2048Console = new Game2048Console(SESSION_FACTORY);
//        game2048Console.start();
    }
}