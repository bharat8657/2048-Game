package game2048.user;

import game2048.Factory;
import game2048.board.BoardGenerator;
import game2048.board.BoardProcessor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.NoResultException;

public class UserProcessor {
//        Prywatna DB:
    private static final SessionFactory SESSION_FACTORY = new Factory().getSessionFactory();
    private static final BoardProcessor BOARD_PROCESSOR = new BoardProcessor();
//        DB projektu wspólnego:
//    private static final SessionFactory SESSION_FACTORY = new game2048.HibernateFactory().getSessionFactory();

    private User addNewUser(String nickname) {
        try (Session session = SESSION_FACTORY.openSession()){
            User user = new User();
            user.setNickname(nickname);
            session.save(user);
            BOARD_PROCESSOR.addNewBoard(user, BoardGenerator.generateNewBoard());
            return user;
        }
    }

    public User getUser(String nickname, Session session) {
        try {
            return session.createQuery("SELECT u FROM User u WHERE u.nickname = :nickname", User.class)
                    .setParameter("nickname", nickname)
                    .getSingleResult();
        } catch (NoResultException e) {
            return addNewUser(nickname);
        }
    }
}