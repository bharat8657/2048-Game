package game2048;

import game2048.board.Board;
import game2048.board.BoardGenerator;
import game2048.board.BoardProcessor;
import game2048.movements.Movement;
import game2048.point.Point;
import game2048.user.User;
import game2048.user.UserProcessor;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game2048 {
    private static final BoardProcessor BOARD_PROCESSOR = new BoardProcessor();
    //    Prywatna DB:
    private static final Session SESSION = new Factory().getSessionFactory().openSession();
    //    DB projektu wspólnego:
//    private static final Session SESSION = new game2048.HibernateFactory().getSessionFactory().openSession();
    private static final UserProcessor USER_PROCESSOR = new UserProcessor();

    public void startGame() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Witaj w grze '2048'!\n" +
                "Podaj swój nickname:");

        String userNickname = scanner.nextLine();
        User user = new UserProcessor().getUser(userNickname, SESSION);
        if (user.getBoardList().size() > 1) {
            System.out.println("Wybierz co chcesz zrobić: \n" +
                    "1 - nowa gra \n" +
                    "2 - wczytaj grę");
            switch (scanner.nextInt()) {
                case 1:
                    System.out.println("Rozpoczęto nową grę!");
                    BOARD_PROCESSOR.addNewBoard(user, BoardGenerator.generateNewBoard());
                    do {
                        play(user);
                    } while (new Scanner(System.in).nextInt() != 1);
                    break;
                case 2:
                    System.out.println("Wczytano ostatnią grę!");
                    do {
                        play(user);
                    } while (new Scanner(System.in).nextInt() != 1);
                    break;
            }
        } else {
            play(user);
        }
    }

    private void play(User user) {
        String move;

        do {
            List<Board> boardList = user.getBoardList();
            int index = boardList.size() - 1;
            BoardGenerator.printBoard(boardList.get(index).getPointList());
            Board board = boardList.get(index);
            List<Point> points = new ArrayList<>(board.getPointList());
            System.out.println("Enter movement:\n" +
                    "w - Move up.\n" +
                    "s - Move down.\n" +
                    "d - Move right.\n" +
                    "a - Move left\n" +
                    "q - Quit.");
            move = new Scanner(System.in).nextLine();
            switch (move) {
                case "w":
                    board.setPointList(Movement.moveUp(points));
                    addBoard(user, BoardGenerator.updateBoard(board));
                    break;
                case "s":
                    board.setPointList(Movement.moveDown(points));
                    addBoard(user, BoardGenerator.updateBoard(board));
                    break;
                case "d":
                    board.setPointList(Movement.moveRight(points));
                    addBoard(user, BoardGenerator.updateBoard(board));
                    break;
                case "a":
                    board.setPointList(Movement.moveLeft(points));
                    addBoard(user, BoardGenerator.updateBoard(board));
                    break;
                case "q":
                    System.out.println("Koniec!");
                    break;
                default:
                    System.out.println("Zły ruch!");
                    break;
            }
        } while (!move.equals("q"));

    }

    private void addBoard(User user, Board updatedBoard) {
        BOARD_PROCESSOR.addNewBoard(user, updatedBoard);
    }

    public static void main(String[] args) {
        Game2048 game2048 = new Game2048();
        game2048.startGame();

    }
}