package game2048.gamegraphik.swingconsole;

import game2048.board.Board;
import game2048.board.BoardGenerator;
import game2048.board.BoardProcessor;
import game2048.movements.Movement;
import game2048.point.Point;
import game2048.point.PointProcessor;
import game2048.user.User;
import org.hibernate.SessionFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GamingFrame extends JFrame {

    private final BoardProcessor BOARD_PROCESSOR = new BoardProcessor();
    private final PointProcessor POINT_PROCESSOR = new PointProcessor();
    private final ImageIcon ICON = new ImageIcon("game2048_logo.png");
    private final List<JTextField> BOX_LIST = new ArrayList<>();
    private final SessionFactory sessionFactory;
    private final JFrame GAME_FRAME = new JFrame();
    private final User user;
    private List<Point> boardPointList;
    private Map<Integer, Color> colorsMap;
    private JPanel gamePanel;
    private JPanel newGamePanel;
    private JButton newGameButton;
    private JButton moveBackButton;
    private Board activeBoard;

    public GamingFrame (User user, SessionFactory sessionFactory) {
        this.user = user;
        this.sessionFactory = sessionFactory;

        setNewGameButton();
        setBackMoveButton();
        setNewGamePanel();
        setColorMap();
        setGamePanel();
        setBoxes();
        updateBoardPoints();
        movement(gamePanel);
        setGameFrame();
    }

    private void setNewGamePanel() {
        newGamePanel = new JPanel();
        newGamePanel.setLayout(new GridLayout(1, 2));
        newGamePanel.setBounds(0, 0, 280, 25);
        newGamePanel.add(newGameButton);
        newGamePanel.add(moveBackButton);
    }

    private void setGamePanel() {
        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(4, 4));
        gamePanel.setBounds(0, 26, 280, 280);
    }

    private void setGameFrame() {
        GAME_FRAME.setIconImage(ICON.getImage());
        GAME_FRAME.add(gamePanel);
        GAME_FRAME.add(newGamePanel);
        GAME_FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GAME_FRAME.setSize(295, 345);
        GAME_FRAME.setTitle("Game 2048");
        GAME_FRAME.setLayout(null);
        GAME_FRAME.setResizable(false);
        GAME_FRAME.setLocationRelativeTo(null);
        GAME_FRAME.setVisible(true);
    }

    private void setColorMap() {
        colorsMap = new HashMap<>();
        colorsMap.put(2, new Color(0xFFF064));
        colorsMap.put(4, new Color(0xFFE164));
        colorsMap.put(8, new Color(0xFFD264));
        colorsMap.put(16, new Color(0xFFC364));
        colorsMap.put(32, new Color(0xFFB464));
        colorsMap.put(64, new Color(0xFFA564));
        colorsMap.put(128, new Color(0xFF9664));
        colorsMap.put(256, new Color(0xFF8764));
        colorsMap.put(512, new Color(0xFF7864));
        colorsMap.put(1024, new Color(0xFF6964));
        colorsMap.put(2048, new Color(0xFF5A64));
    }


    private void setBackMoveButton() {
        moveBackButton = new JButton("Back Move");
        moveBackButton.setFont(new Font("Consolas", Font.ITALIC, 15));
        moveBackButton.setBackground(new Color(0xCFEFD794));
        moveBackButton.setFocusable(false);
        moveBackButton.setBorder(BorderFactory.createEtchedBorder());
        moveBackButton.addActionListener(e -> {
            if (BOARD_PROCESSOR.getUserBoardList(user.getId(), sessionFactory).size() > 1) {
                BOARD_PROCESSOR.deleteBoard(activeBoard.getId(), sessionFactory);
                updateBoardPoints();
            } else {
                JOptionPane.showMessageDialog(null
                        , "There is no more move to back!"
                        , "No more back move"
                        , JOptionPane.PLAIN_MESSAGE);
            }
        });

    }

    private void    setNewGameButton() {
        newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Consolas", Font.ITALIC, 15));
        newGameButton.setBackground(new Color(0xCFEFD794));
        newGameButton.setFocusable(false);
        newGameButton.setBorder(BorderFactory.createEtchedBorder());
        newGameButton.addActionListener(e -> {
            BOARD_PROCESSOR.getUserBoardList(user.getId(), sessionFactory)
                    .forEach(board -> BOARD_PROCESSOR.deleteBoard(board.getId(), sessionFactory));
            BOARD_PROCESSOR.addNewBoard(user, BoardGenerator.generateNewBoard(), sessionFactory);
            updateBoardPoints();
        });
    }

    private void setBoxes() {
        for (int i = 0; i < 16; i++) {
            BOX_LIST.add(new JTextField());
        }
        for (JTextField box : BOX_LIST) {
            box.setEditable(false);
            box.setFont(new Font("Book Antiqua", Font.ITALIC, 30));
            box.setHorizontalAlignment(JTextField.CENTER);
            box.setForeground(Color.black);
            box.setBackground(new Color(0xbbada0));
            box.setBorder(BorderFactory.createEtchedBorder());
            gamePanel.add(box);
        }
    }

    private void addBoard(User user) {
        Board updatedBoard = BoardGenerator.updateBoard(activeBoard);
        if (updatedBoard == null) {
            JOptionPane.showMessageDialog(null
                    , "This move is not allowed now!"
                    , "End of game move"
                    , JOptionPane.PLAIN_MESSAGE);
        } else {
            BOARD_PROCESSOR.addNewBoard(user, updatedBoard, sessionFactory);
            updateBoardPoints();
        }
    }

    private void drawBoard(List<Point> pointList) {
        boolean isFilled;
        int boxIndex = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                isFilled = false;
                for (Point point : pointList) {
                    if (point.getX() == j && point.getY() == i) {
                        BOX_LIST.get(boxIndex).setText(String.valueOf(point.getValue()));
                        BOX_LIST.get(boxIndex).setBackground(colorsMap.get(point.getValue()));
                        isFilled = true;
                    }
                }
                if (!isFilled) {
                    BOX_LIST.get(boxIndex).setText("");
                    BOX_LIST.get(boxIndex).setBackground(new Color(0xbbada0));
                }
                boxIndex++;
            }
        }
    }

    private void updateBoardPoints() {
        List<Board> boardList = BOARD_PROCESSOR.getUserBoardList(user.getId(), sessionFactory);
        if (boardList.size() > 3) {
            BOARD_PROCESSOR.deleteBoard(boardList.get(0).getId(), sessionFactory);
            boardList.remove(0);
        }
        int indexOfLastBoard = boardList.size() - 1;
        activeBoard = boardList.get(indexOfLastBoard);
        boardPointList = new ArrayList<>(POINT_PROCESSOR.getBoardPointList(activeBoard.getId(), sessionFactory));
        drawBoard(boardPointList);
    }

    private void movement(JPanel boxesPanel) {
        Action downAction = new DownAction();
        Action rightAction = new RightAction();
        Action leftAction = new LeftAction();
        Action upAction = new UpAction();

        boxesPanel.getInputMap().put(KeyStroke.getKeyStroke("UP"), "upAction");
        boxesPanel.getActionMap().put("upAction", upAction);
        boxesPanel.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "rightAction");
        boxesPanel.getActionMap().put("rightAction", rightAction);
        boxesPanel.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "leftAction");
        boxesPanel.getActionMap().put("leftAction", leftAction);
        boxesPanel.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "downAction");
        boxesPanel.getActionMap().put("downAction", downAction);
    }

    public class UpAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            activeBoard.setPointList(Movement.moveUp(boardPointList));
            addBoard(user);
        }
    }

    public class LeftAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            activeBoard.setPointList(Movement.moveLeft(boardPointList));
            addBoard(user);
        }
    }

    public class DownAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            activeBoard.setPointList(Movement.moveDown(boardPointList));
            addBoard(user);
        }
    }

    public class RightAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            activeBoard.setPointList(Movement.moveRight(boardPointList));
            addBoard(user);
        }
    }
}