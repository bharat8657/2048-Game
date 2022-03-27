package game2048.gamegraphik.swingconsole;

import game2048.board.BoardGenerator;
import game2048.board.BoardProcessor;
import game2048.user.User;
import game2048.user.UserProcessor;
import org.hibernate.SessionFactory;

import javax.swing.*;
import java.awt.*;

public class AuthorizationFrame extends JFrame {

    private final SessionFactory sessionFactory;
    private final UserProcessor USER_PROCESSOR = new UserProcessor();
    private final BoardProcessor BOARD_PROCESSOR = new BoardProcessor();
    private JLabel nicknameInput;
    private JLabel gameLogo;
    private JButton acceptButton;
    private JTextField nickInputField;
    private static final ImageIcon IMAGE_ICON = new ImageIcon("game2048_logo.png");

    public AuthorizationFrame(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        setAcceptButton();
        setGameLogo();
        setNickInputField();
        setNicknameInput();
        setAuthorizationFrame();
    }

    private void setNickInputField() {
        this.nickInputField = new JTextField("Your nickname");
        this.nickInputField.setBounds(0,0,233,55);
        this.nickInputField.setBackground(new Color(0xd1c6b9));
        this.nickInputField.setFont(new Font("Book Antiqua", Font.ITALIC, 30));
        this.nickInputField.setForeground(new Color(0x776e65));
        this.nickInputField.setBorder(BorderFactory.createEtchedBorder());
    }

    private void setAuthorizationFrame() {
        this.setTitle("Authorization panel");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(300,440);
        this.setIconImage(IMAGE_ICON.getImage());
        this.getContentPane().setBackground(new Color(0xbbada0));
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setVisible(true);
        this.add(nicknameInput);
        this.add(gameLogo);
    }

    private void setAcceptButton() {
        this.acceptButton = new JButton();
        this.acceptButton.setBounds(0,55,233,55);
        this.acceptButton.setText("Accept");
        this.acceptButton.setFont(new Font("Book Antiqua", Font.ITALIC, 30));
        this.acceptButton.setForeground(new Color(0x776e65));
        this.acceptButton.setBorder(BorderFactory.createEtchedBorder());
        this.acceptButton.setFocusable(false);
        this.acceptButton.setBackground(new Color(0xede0c8));
        this.acceptButton.addActionListener(e -> {
            this.acceptButton.setText("Loading...");
            this.acceptButton.setEnabled(false);
            this.dispose();
            User activeUser = USER_PROCESSOR.getUser(this.nickInputField.getText(), sessionFactory);
            if (BOARD_PROCESSOR.getUserBoardList(activeUser.getId(), sessionFactory).isEmpty()) {
                BOARD_PROCESSOR.addNewBoard(activeUser, BoardGenerator.generateNewBoard(), this.sessionFactory);
                new GamingFrame(activeUser, sessionFactory);
            } else {
                new MenuFrame(activeUser, sessionFactory, gameLogo, BOARD_PROCESSOR);
            }
        });
    }


    private void setNicknameInput() {
        this.nicknameInput = new JLabel();
        this.nicknameInput.setBackground(new Color(0xf2b179));
        this.nicknameInput.setOpaque(true);
        this.nicknameInput.setBorder(BorderFactory.createLineBorder(new Color(0x2D2102), 1));
        this.nicknameInput.setBounds(26,10,233,110);
        this.nicknameInput.add(acceptButton);
        this.nicknameInput.add(nickInputField);
    }

    private void setGameLogo () {
        this.gameLogo = new JLabel();
        this.gameLogo.setBounds(10,130,265,265);
        this.gameLogo.setIcon(new ImageIcon(IMAGE_ICON.getImage().getScaledInstance(gameLogo.getWidth(), gameLogo.getHeight(), Image.SCALE_DEFAULT)));
    }

}
