package game;

import javax.swing.*;

public class Game {
    private GameBoard board;                            //ссылка на игр. поле
    private GamePlayer[] gamePlayers = new GamePlayer[2];//массив игроков
    private int playersTurn = 0;             //индекс тек. игрока

    public Game(){
        this.board = new GameBoard(this);
    }

    public  void initGame(){
        gamePlayers[0] = new GamePlayer(true, 'X');
        gamePlayers[1] = new GamePlayer(true, 'O');
    }

    //метод передачи хода
    void passTurn(){
    /*    if(playersTurn == 0)
            playersTurn = 1;
        else
            playersTurn = 0;
    */
        playersTurn = (playersTurn == 0) ? 1 : 0;
    }

    //возвращение объекта текущего игрока
    GamePlayer getCurrentPlayer() {
        return gamePlayers[playersTurn];
    }

    //показ popup для пользователя
    void showMessage (String messageText){
        JOptionPane.showMessageDialog(board, messageText);
    }
}
