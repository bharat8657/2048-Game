package game2048.gamegraphik.swingconsole;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class AuthorizationFrame extends JFrame {

    private JLabel NICKNAME_INPUT;
    private JLabel GAME_LOGO;
    private static final ImageIcon IMAGE_ICON = new ImageIcon("game2048_logo.png");

    public AuthorizationFrame() {
        setNicknameInput();
        setGameLogo();
        setAuthorizationFrame();
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
        this.add(NICKNAME_INPUT);
        this.add(GAME_LOGO);
    }

    private void setNicknameInput() {
        this.NICKNAME_INPUT = new JLabel();
        this.NICKNAME_INPUT.setText("bla bla bla");
        this.NICKNAME_INPUT.setHorizontalAlignment(JLabel.CENTER);
        this.NICKNAME_INPUT.setVerticalTextPosition(JLabel.CENTER);
        this.NICKNAME_INPUT.setBackground(new Color(0xf2b179));
        this.NICKNAME_INPUT.setOpaque(true);
        this.NICKNAME_INPUT.setBorder(BorderFactory.createLineBorder(new Color(0x2D2102), 1));
        this.NICKNAME_INPUT.setBounds(26,10,233,110);
    }

    private void setGameLogo () {
        this.GAME_LOGO = new JLabel();
        this.GAME_LOGO.setBounds(10,130,265,265);
        this.GAME_LOGO.setIcon(new ImageIcon(IMAGE_ICON.getImage().getScaledInstance(GAME_LOGO.getWidth(), GAME_LOGO.getHeight(), Image.SCALE_DEFAULT)));
    }

//    private Image
}
