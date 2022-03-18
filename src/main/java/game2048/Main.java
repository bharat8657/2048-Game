package game2048;

import game2048.gamegraphik.ideaconsole.Game2048Console;
import game2048.gamegraphik.swingconsole.AuthorizationFrame;
import org.hibernate.SessionFactory;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SessionFactory sessionFactory = new Factory().getSessionFactory();


        JFrame authorizationFrame = new AuthorizationFrame();






//        Game2048Console game2048Console = new Game2048Console(sessionFactory);
//        game2048Console.start();
    }
}