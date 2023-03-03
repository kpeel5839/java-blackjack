package domain;

import java.util.LinkedHashMap;
import java.util.Map;

public class Result {

    private final Map<String, GameResult> gameResult;

    public Result(Dealer dealer, Players players) {
        gameResult = new LinkedHashMap<>();
        calculate(dealer, players);
    }

    private void calculate(Dealer dealer, Players players) {
        for (Player player : players.getPlayers()) {
            calculateWinLose(dealer, player);
        }
    }

    private void calculateWinLose(Dealer dealer, Player player) {
        win(dealer, player);
        lose(dealer, player);
        draw(dealer, player);
    }

    private void win(Dealer dealer, Player player) {
        if (!player.isBust() && (dealer.isBust() || dealer.getTotalScore() < player.getTotalScore())) {
            gameResult.put(player.getName().getValue(), GameResult.WIN);
        }
    }

    private void lose(Dealer dealer, Player player) {
        if (!dealer.isBust() && (player.isBust() || dealer.getTotalScore() > player.getTotalScore())) {
            gameResult.put(player.getName().getValue(), GameResult.LOSE);
        }
    }

    private void draw(Dealer dealer, Player player) {
        if (dealer.isBust() && player.isBust() || dealer.getTotalScore() == player.getTotalScore()) {
            gameResult.put(player.getName().getValue(), GameResult.DRAW);
        }
    }

    public Map<String, GameResult> getResult() {
        return gameResult;
    }

}
