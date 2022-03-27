package game2048.gamegraphik.swingconsole;

import game2048.board.BoardGenerator;
import game2048.board.BoardProcessor;
import game2048.user.User;
import org.hibernate.SessionFactory;

import javax.swing.*;
import java.awt.*;

public class MenuFrame extends JFrame {

    private JButton newGame;
    private JButton loadGame;
    private final JLabel gameLogo;
    private final User activeUser;
    private final SessionFactory sessionFactory;
    private final ImageIcon ICON = new ImageIcon("game2048_logo.png");
    private JLabel menuLabel;
    private final BoardProcessor boardProcessor;

    public MenuFrame(User user, SessionFactory sessionFactory, JLabel game_logo, BoardProcessor boardProcessor) {
        this.boardProcessor = boardProcessor;
        this.activeUser = user;
        this.sessionFactory = sessionFactory;
        this.gameLogo = game_logo;
        setNewGameButton();
        setLoadGameButton();
        setMenuLabel();
        setMenuFrame();
    }

    private void setNewGameButton() {
        this.newGame = new JButton();
        this.newGame.setBounds(15,35,170,30);
        this.newGame.setText("New game");
        this.newGame.setFont(new Font("Book Antiqua", Font.ITALIC, 15));
        this.newGame.setForeground(new Color(0x000000));
        this.newGame.setBorder(BorderFactory.createEtchedBorder());
        this.newGame.setFocusable(false);
        this.newGame.setBackground(new Color(0xd1c6b9));
        this.newGame.addActionListener(pressing -> {
            this.dispose();
            boardProcessor.getUserBoardList(activeUser.getId(), sessionFactory)
                    .forEach(board -> boardProcessor.deleteBoard(board.getId(), sessionFactory));
            boardProcessor.addNewBoard(activeUser, BoardGenerator.generateNewBoard(), sessionFactory);
            new GamingFrame(activeUser, sessionFactory);
        });
    }

    private void setLoadGameButton() {
        this.loadGame = new JButton();
        this.loadGame.setBounds(50,70,170,30);
        this.loadGame.setText("Load game");
        this.loadGame.setFont(new Font("Book Antiqua", Font.ITALIC,15));
        this.loadGame.setForeground(new Color(0x000000));
        this.loadGame.setBorder(BorderFactory.createEtchedBorder());
        this.loadGame.setFocusable(false);
        this.loadGame.setBackground(new Color(0xd1c6b9));
        this.loadGame.addActionListener(pressing -> {
            this.dispose();
            new GamingFrame(activeUser, sessionFactory);
        });
    }

    private void setMenuLabel() {
        this.menuLabel = new JLabel();
        this.menuLabel.setText("Choose the way below:");
        this.menuLabel.setFont(new Font("Book Antiqua", Font.ITALIC, 25));
        this.menuLabel.setVerticalAlignment(JLabel.TOP);
        this.menuLabel.setHorizontalAlignment(JLabel.CENTER);
        this.menuLabel.setBackground(new Color(0xf2b179));
        this.menuLabel.setOpaque(true);
        this.menuLabel.setBorder(BorderFactory.createEtchedBorder());
        this.menuLabel.setBounds(26,10,233,110);
        this.menuLabel.add(this.newGame);
        this.menuLabel.add(this.loadGame);
    }

    private void setMenuFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(ICON.getImage());
        this.setTitle("Menu");
        this.getContentPane().setBackground(new Color(0xd1c6b9));
        this.setSize(new Dimension(300, 440));
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setVisible(true);
        this.add(menuLabel);
        this.add(gameLogo);
    }
}
