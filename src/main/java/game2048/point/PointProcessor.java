package game2048.point;

import game2048.Factory;
import game2048.board.Board;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class PointProcessor {
    //        Prywatna DB:
    private static final SessionFactory SESSION_FACTORY = new Factory().getSessionFactory();

    //        DB projektu wspólnego:
//    private static final SessionFactory SESSION_FACTORY = new game2048.HibernateFactory().getSessionFactory();
    public void addPoints(Board board) {
        try (Session session = SESSION_FACTORY.openSession()) {
            List<Point> points = board.getPointList();
            for (Point point : points) {
                point.setBoard(board);
                session.save(point);
            }
        }
    }
}
