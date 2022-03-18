package game2048.board;

import game2048.Factory;
import game2048.point.Point;
import game2048.point.PointProcessor;
import game2048.user.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class BoardProcessor {

    private static final PointProcessor POINT_PROCESSOR = new PointProcessor();

    public void addNewBoard(User user, Board board, SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            board.setUser(user);
            List<Board> boardList = getUserBoardList(user.getId(), sessionFactory);
            boardList.add(board);
            session.save(board);
            POINT_PROCESSOR.addPoints(board, sessionFactory);
        }
    }

    public void deleteBoard(Long id, SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Board board = session.load(Board.class, id);
            POINT_PROCESSOR.deletePoints(board, sessionFactory);
            session.remove(board);
            transaction.commit();
        }
    }

    public List<Board> getUserBoardList(Long id, SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT b FROM Board b LEFT JOIN b.user u WHERE u.id = :id ORDER BY b.id ASC", Board.class)
                    .setParameter("id", id)
                    .getResultList();
        }
    }
}