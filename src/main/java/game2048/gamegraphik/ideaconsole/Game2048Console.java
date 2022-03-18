package game2048.gamegraphik.ideaconsole;

import game2048.Factory;
import game2048.board.Board;
import game2048.board.BoardGenerator;
import game2048.board.BoardProcessor;
import game2048.gamegraphik.StartTheGame;
import game2048.movements.Movement;
import game2048.point.Point;
import game2048.point.PointProcessor;
import game2048.user.User;
import game2048.user.UserProcessor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game2048Console implements StartTheGame {

    private final BoardProcessor BOARD_PROCESSOR = new BoardProcessor();
    private final UserProcessor USER_PROCESSOR = new UserProcessor();
    private final PointProcessor POINT_PROCESSOR = new PointProcessor();
    private final SessionFactory SESSION_FACTORY;

    public Game2048Console(SessionFactory sessionFactory) {
        this.SESSION_FACTORY = sessionFactory;
    }

    private void play (User user) {
        String move;
        System.out.println("Enter a movement:\n" +
                "w - Move up.\t" +
                "s - Move down.\t" +
                "d - Move right.\t" +
                "a - Move left\t" +
                "r - Step back\t" +
                "q - Quit.");
        do {
            List<Board> boardList = BOARD_PROCESSOR.getUserBoardList(user.getId(), SESSION_FACTORY);
            if (boardList.size() > 3) {
                BOARD_PROCESSOR.deleteBoard(boardList.get(0).getId(), SESSION_FACTORY);
                boardList.remove(0);
            }
            int index = boardList.size() - 1;
            Board board = boardList.get(index);
            List<Point> currentPoints = new ArrayList<>(POINT_PROCESSOR.getBoardPointList(board.getId(), SESSION_FACTORY));
            BoardGenerator.printBoard(currentPoints);
            move = new Scanner(System.in).nextLine();
            switch (move) {
                case "w":
                    board.setPointList(Movement.moveUp(currentPoints));
                    addBoard(user, BoardGenerator.updateBoard(board));
                    break;
                case "s":
                    board.setPointList(Movement.moveDown(currentPoints));
                    addBoard(user, BoardGenerator.updateBoard(board));
                    break;
                case "d":
                    board.setPointList(Movement.moveRight(currentPoints));
                    addBoard(user, BoardGenerator.updateBoard(board));
                    break;
                case "a":
                    board.setPointList(Movement.moveLeft(currentPoints));
                    addBoard(user, BoardGenerator.updateBoard(board));
                    break;
                case "r":
                    if (boardList.size() > 1) {
                        BOARD_PROCESSOR.deleteBoard(board.getId(), SESSION_FACTORY);
                        System.out.println("Step back has been taken.");
                    } else {
                        System.out.println("There isn't possibilities to make step back.");
                    }
                    break;
                case "q":
                    System.out.println("The end of the game session.");
                    break;
                default:
                    System.out.println("Wrong entering. Try again.");
                    break;
            }
        } while (!move.equals("q"));

    }

    private void addBoard(User user, Board updatedBoard) {
        BOARD_PROCESSOR.addNewBoard(user, updatedBoard, SESSION_FACTORY);
    }

    @Override
    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the game '2048'!\nEnter your nickname:");
        String currentUserNickName = scanner.nextLine();
        User user = USER_PROCESSOR.getUser(currentUserNickName, SESSION_FACTORY);
        List<Board> currentUserBoardList = BOARD_PROCESSOR.getUserBoardList(user.getId(), SESSION_FACTORY);
        if (currentUserBoardList.size() > 1) {
            System.out.println("What we gonna do?\nPress 1 and then Enter button to start new game.\nPress 2 and then Enter button to load game.");
            switch (scanner.nextInt()) {
                case 1:
                    System.out.println("Starting the new game...");
                    BOARD_PROCESSOR.getUserBoardList(user.getId(), SESSION_FACTORY)
                            .forEach(board -> BOARD_PROCESSOR.deleteBoard(board.getId(), SESSION_FACTORY));
                    BOARD_PROCESSOR.addNewBoard(user, BoardGenerator.generateNewBoard(), SESSION_FACTORY);
                    play(user);
                    break;
                case 2:
                    System.out.println("The game session has loaded.");
                    play(user);
                    break;
            }
        } else {
            play(user);
        }
    }
}