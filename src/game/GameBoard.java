package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameBoard extends JFrame {
    static int dimension = 3; //размерность
    static int cellSize = 150; //размер клетки
    private char[][] gameField; //матрица игры
    private GameButton[] gameButtons; //массив кнопок

    private Game game;      //ссылка на игру

    static char nullSybol = '\u0000';

    public GameBoard(Game currentGame) {
        this.game = currentGame;
        initField();
    }

    //метод инициализации и отрисовки игрового поля
    private void initField() {
        setBounds(cellSize * dimension, cellSize * dimension, 400, 300);
        setTitle("Крестики нолики");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();//панель управления игрой
        JButton newGameButton = new JButton("Новая игра");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyField();
            }
        });

        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(newGameButton);
        controlPanel.setSize(cellSize * dimension, 150);
        //панель игры
        JPanel gameFieldPanel = new JPanel();
        gameFieldPanel.setLayout(new GridLayout(dimension, dimension));
        gameFieldPanel.setSize((cellSize * dimension), (cellSize * dimension));

        gameField = new char[dimension][dimension];
        gameButtons = new GameButton[dimension * dimension];

        //инициализ. игровое поле
        for (int i = 0; i < (dimension * dimension); i++) {
            GameButton fieldButton = new GameButton(i, this);
            gameFieldPanel.add(fieldButton);
            gameButtons[i] = fieldButton;
        }

        getContentPane().add(controlPanel, BorderLayout.NORTH);
        getContentPane().add(gameFieldPanel, BorderLayout.CENTER);

        setVisible(true);

    }

    //метод очистки поля и матрицы игры

    void emptyField() {
        for (int i = 0; i < (dimension * dimension); i++) {
            gameButtons[i].setText("");

            int x = i / GameBoard.dimension;
            int y = i % GameBoard.dimension;

            gameField[x][y] = nullSybol;
        }
    }

    Game getGame() {
        return game;
    }

    //проверка доступности кледки для хода
    boolean isTurnable(int x, int y) {
        boolean result = false;

        if (gameField[y][x] == nullSybol)
            result = true;

        return result;
    }

    //обновление матрицы после игры
    void updateGameField(int x, int y) {
        gameField[y][x] = game.getCurrentPlayer().getPlayerSign();
    }

    //проверка победы
    boolean checkWin() {
        boolean result = false;

        char playerSymbol = getGame().getCurrentPlayer().getPlayerSign();

        if (checkWinDiagonals(playerSymbol) || checkWinLines(playerSymbol)) {
            result = true;
        }

        return result;
    }

    private boolean checkWinDiagonals(char playerSymbol) {
        boolean diag1, diag2, result;
        diag1 = true;
        diag2 = true;
        result = false;

        for (int col = 0; col < dimension; col++) {
            diag1 &= gameField[col][col] == playerSymbol;
        }
        for (int row = dimension - 1; row >= 0; row--) {
            diag2 &= gameField[row][dimension - 1 - row] == playerSymbol;
        }
        if (diag1 || diag2)
            result = true;
        return result;
    }

    private boolean checkWinLines(char playerSymbol) {
        boolean cols, rows, result;

        result = false;

        for (int col = 0; col < dimension; col++) {
            cols = true;
            rows = true;

            for (int row = 0; row < dimension; row++) {
                cols &= gameField[col][row] == playerSymbol;
                rows &= gameField[row][col] == playerSymbol;
            }
            if (cols || rows) {
                result = true;
                break;
            }
            if (result)
                break;
        }
        return result;
    }

    boolean isFull() {
        boolean result = true;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (gameField[i][j] == nullSybol) {
                    result = false;
                    break;
                }
            }
            if (!result) {
                break;
            }
        }
        return result;
    }

    public GameButton getButton(int buttonIndex) {
        return gameButtons[buttonIndex];
    }


    public char[][] getGameField() {
        return gameField;
    }
}