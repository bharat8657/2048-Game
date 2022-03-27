package game2048.gamegraphik.swingconsole;

import game2048.gamegraphik.StartTheGame;
import game2048.gamegraphik.ideaconsole.Game2048Console;
import org.hibernate.SessionFactory;

public class Game2048Graphic implements StartTheGame {

    private final SessionFactory sessionFactory;

    public Game2048Graphic(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void start() {
        new AuthorizationFrame(sessionFactory);
    }
}
