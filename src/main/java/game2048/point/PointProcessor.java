package game2048.point;

import game2048.Factory;
import game2048.board.Board;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class PointProcessor {

    public void addPoints(Board board, SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            List<Point> points = board.getPointList();
            for (Point point : points) {
                point.setBoard(board);
                session.save(point);
            }
        }
    }

    public void deletePoints(Board board, SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            List<Point> pointsToRemoveFromDB = board.getPointList();
            for (Point nextPoint : pointsToRemoveFromDB) {
                session.remove(nextPoint);
            }
            transaction.commit();
        }
    }

    public List<Point> getBoardPointList(Long id, SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
             return session.createQuery("SELECT p FROM Point p left join p.board b WHERE b.id = :id", Point.class)
                     .setParameter("id", id)
                     .getResultList();
        }
    }
}
