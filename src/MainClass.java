import game.Game;

public class MainClass {
    public static void main(String [] args){
        Game gameInstance = new Game();
        gameInstance.initGame();

    }
}

/**
 * ДЗ
 * Неоптимальные места и их оптимизация:
 * 1. в Game.java  метод передачи хода void passTurn() if заменяем на тернарный оператор
 * 2. в GameBoard.java метод isFull(), добавляем принудительный выход из цикла сразу же, когда найден
 *     первый пустой символ (чтоб не расходовать ресурсы)
 *  Портирование псевдо-AI из прошлого задания - сделано
 *  - много добавлено в метод хода компьютера - updateByAiData, для этого созданы еще у метода -
 *   checkNearCells и scoreNearCells, что они делают -  в комментариях к ним
 *   В общем все работает :)
 */

