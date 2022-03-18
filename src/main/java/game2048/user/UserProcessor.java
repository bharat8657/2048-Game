package game2048.user;

import game2048.Factory;
import game2048.board.BoardGenerator;
import game2048.board.BoardProcessor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.NoResultException;

public class UserProcessor {

    private static final BoardProcessor BOARD_PROCESSOR = new BoardProcessor();

    private User addNewUser(String nickname, SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            User user = new User();
            user.setNickname(nickname);
            session.save(user);
            BOARD_PROCESSOR.addNewBoard(user, BoardGenerator.generateNewBoard(), sessionFactory);
            return user;
        }
    }

    public User getUser(String nickname, SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            try {
                return session.createQuery("SELECT u FROM User u WHERE u.nickname = :nickname", User.class)
                        .setParameter("nickname", nickname)
                        .getSingleResult();
            } catch (NoResultException e) {
                return addNewUser(nickname, sessionFactory);
            }
        }
    }
}