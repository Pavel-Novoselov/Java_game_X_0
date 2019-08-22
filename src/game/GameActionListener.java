package game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GameActionListener implements ActionListener {
    private int row;
    private int cell;
    private GameButton button;
    private static final boolean SILLY_MODE = false;
    private static final boolean GOD_MODE = true; //улучшенный умный режим

    public GameActionListener(int row, int cell, GameButton gButton){
        this.row = row;
        this.cell = cell;
        this.button = gButton;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GameBoard board = button.getBoard();

        if (board.isTurnable(row, cell)){
            updateByPlayerData(board);

            if (board.isFull()){
                board.getGame().showMessage("Ничья");
                board.emptyField();
            }
            else{
                updateByAiData(board);
            }

        }
        else{
            board.getGame().showMessage("некорректный ход!");

        }
    }
// ДЗ - портированный псевдо-AI
    private void updateByAiData(GameBoard board) {
        //генерация входа компьютера
            int x=-1, y=-1;
            Random rnd = new Random();

            if (SILLY_MODE) {
                do{
                    x = rnd.nextInt(GameBoard.dimension);
                    y = rnd.nextInt(GameBoard.dimension);
                } while (!board.isTurnable(x,y));
            }
            else if (GOD_MODE){
                //псевдо-разумный режим - строим матрицу весов каждой ячейки с точки зрения предпочтительности следующего хода
                //-1 - ячейка занята, 0 и выше - количество ноликов в соседних ячейках
                int[][] scores = new int [GameBoard.dimension][GameBoard.dimension];
                for (int i=0; i<GameBoard.dimension; i++) {
                    for (int j = 0; j < GameBoard.dimension; j++) {
                        //проходим по игровому полю и формируем матрицу весов
                        if (!board.isTurnable(i,j))
                            scores[i][j] = -1;
                        else scores[i][j] = scoreNearCells(i, j);
                    }
                }
           /* отладочный вывод
            //для понимания процеса просмотрим массив весов
            System.out.println();
            for (int i=0; i<GameBoard.dimension; i++) {
                for (int j = 0; j < GameBoard.dimension; j++) {
                    System.out.print(scores[i][j] + " ");
                }
                System.out.println();
            }
            */
                //выбираем из матрицы весон наибольшее значение и делаем ход в эту ячейку
                int maxScore=scores[0][0];
                int maxI = 0;
                int maxJ = 0;
                for (int i=0; i<GameBoard.dimension; i++) {
                    for (int j = 0; j < GameBoard.dimension; j++) {
                        if (scores[i][j] > maxScore){
                            maxScore = scores[i][j];
                            maxI = i;
                            maxJ = j;
                        }
                    }
                }
                x=maxI;
                y=maxJ;
                //если нет ноликов, то случайный ход
                if (maxScore == 0) {
                    do {
                        x = rnd.nextInt(GameBoard.dimension);
                        y = rnd.nextInt(GameBoard.dimension);
                    } while (!board.isTurnable(x,y));
                }

            }
            else {
                //слегка разумный режим - проверка чтоб в ближайших клетках был хоть один нолик и ход туда
                for (int i=0; i<GameBoard.dimension; i++) {
                    for (int j = 0; j < GameBoard.dimension; j++) {
                        if (board.isTurnable(i, j) && checkNearCells(i, j )){
                            x=i;
                            y=j;
                            break;
                        }
                    }
                    if (x !=-1){
                        break;
                    }
                }
                if ( x == -1){
                    do {
                        x = rnd.nextInt(GameBoard.dimension);
                        y = rnd.nextInt(GameBoard.dimension);
                    } while (!board.isTurnable(x, y));
                }
            }
            //board.getGame().showMessage("Компьютер выбрал ячейку " + (x+1) + " " + (y+1));

            //делаем ход!
            board.updateGameField(x,y);
            //записываем в ячейку символ игрока
            int cellIndex = GameBoard.dimension * x + y;
            board.getButton(cellIndex).setText(Character.toString(board.getGame().getCurrentPlayer().getPlayerSign()));

            //проверяем победу
            if (board.checkWin()){
                button.getBoard().getGame().showMessage("Computer has won!");
                board.emptyField();
            }
            else{
                if (board.isFull()) {
                    board.getGame().showMessage("Ничья!");
                    board.emptyField();
                }
                else {
                    board.getGame().passTurn(); //передача хода
                }
            }
        }

    /*
     * проверка соседней ячейки на заполненность символами игрока для решения, куда ходить компьютеру
     * @param  i, j - координаты текущей клетки
     * @return boolean - признак наличия нуля в одной из соседних клеток
     */
    public boolean checkNearCells(int j, int i) {
        boolean result = false;
        if (
                (i-1>=0 && j-1>=0 && button.getBoard().getGameField()[i-1][j-1] == button.getBoard().getGame().getCurrentPlayer().getPlayerSign()) ||
                (i-1>=0 && button.getBoard().getGameField()[i-1][j] == button.getBoard().getGame().getCurrentPlayer().getPlayerSign()) ||
                (i-1>=0 && j+1<GameBoard.dimension && button.getBoard().getGameField()[i-1][j+1] == button.getBoard().getGame().getCurrentPlayer().getPlayerSign()) ||
                (j-1>=0 && button.getBoard().getGameField()[i][j-1] == button.getBoard().getGame().getCurrentPlayer().getPlayerSign()) ||
                (j+1<GameBoard.dimension && button.getBoard().getGameField()[i][j+1] == button.getBoard().getGame().getCurrentPlayer().getPlayerSign()) ||
                (i+1<GameBoard.dimension && j-1>=0 && button.getBoard().getGameField()[i+1][j-1] == button.getBoard().getGame().getCurrentPlayer().getPlayerSign()) ||
                (i+1<GameBoard.dimension && button.getBoard().getGameField()[i+1][j] == button.getBoard().getGame().getCurrentPlayer().getPlayerSign()) ||
                (i+1<GameBoard.dimension && j+1<GameBoard.dimension && button.getBoard().getGameField()[i+1][j+1] == button.getBoard().getGame().getCurrentPlayer().getPlayerSign()))
        {
            result = true;
        }
        //System.out.println("+"+i+j + result + " "); //отладочный вывод
        return result;

    }

    /**
     * проверка соседних ячеек на количество своих символов
     * @param  i, j - координаты текущей клетки
     * @return int - количество своих символов  в соседних клетках
     */
    private int scoreNearCells(int j, int i) {
        int score = 0;
        //System.out.println("в ячейуке - "+button.getBoard().getGameField()[i][j]);
        //System.out.println("знак - "+button.getBoard().getGame().getCurrentPlayer().getPlayerSign());
        if (i-1>=0 && j-1>=0 && (button.getBoard().getGameField()[i-1][j-1] == button.getBoard().getGame().getCurrentPlayer().getPlayerSign())) score++;
        if (i-1>=0 && (button.getBoard().getGameField()[i-1][j] == button.getBoard().getGame().getCurrentPlayer().getPlayerSign())) score++;
        if (i-1>=0 && j+1<GameBoard.dimension && (button.getBoard().getGameField()[i-1][j+1] == button.getBoard().getGame().getCurrentPlayer().getPlayerSign())) score++;
        if (j-1>=0 && (button.getBoard().getGameField()[i][j-1] == button.getBoard().getGame().getCurrentPlayer().getPlayerSign())) score++;
        if (j+1<GameBoard.dimension && button.getBoard().getGameField()[i][j+1] == button.getBoard().getGame().getCurrentPlayer().getPlayerSign()) score++;
        if  (i+1<GameBoard.dimension && j-1>=0 && button.getBoard().getGameField()[i+1][j-1] == button.getBoard().getGame().getCurrentPlayer().getPlayerSign()) score++;
        if (i+1<GameBoard.dimension && button.getBoard().getGameField()[i+1][j] == button.getBoard().getGame().getCurrentPlayer().getPlayerSign()) score++;
        if (i+1<GameBoard.dimension && j+1<GameBoard.dimension && button.getBoard().getGameField()[i+1][j+1] == button.getBoard().getGame().getCurrentPlayer().getPlayerSign()) score++;
        //System.out.println("score nearest "+i+"=i "+j+"=j - "+score + " "); //отладочный вывод
        return score;
    }

    //ход человека
    private void updateByPlayerData(GameBoard board) {
        //обновим матрицу игры
        board.updateGameField(row, cell);

        //обновить содержимое кнопки
        button.setText(Character.toString(board.getGame().getCurrentPlayer().getPlayerSign()));

        if (board.checkWin()){
            button.getBoard().getGame().showMessage("You have won!");
            board.emptyField();
        }
        else {
            board.getGame().passTurn();

        }
    }
}
