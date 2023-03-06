package controller;

import domain.CardDeck;
import domain.CardDistributor;
import domain.Dealer;
import domain.Name;
import domain.Participant;
import domain.Player;
import domain.Players;
import domain.Result;
import util.CardDeckMaker;
import util.CardStatusConverter;
import view.InputView;
import view.OutputView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BlackJackController {

    private static final String MORE_CARD = "y";
    private static final String CARD_STOP = "n";
    private static final String DELIMITER = ",";
    private static final int LIMIT_REMOVED = -1;
    private static final String INVALID_MORE_CARD_ERROR_MESSAGE = "y 나 n 만을 입력해주세요.";
    private final InputView inputView;
    private final OutputView outputView;

    public BlackJackController() {
        inputView = new InputView();
        outputView = new OutputView();
    }

    public void run() {
        List<String> playerNames = requestPlayerName();
        CardDistributor cardDistributor = new CardDistributor(CardDeckMaker.generate());
        Players players = Players.of(playerNames, cardDistributor);
        Dealer dealer = new Dealer(new CardDeck(cardDistributor.distributeInitialCard()));
        printInitialDistribution(players, dealer);
        progress(players, cardDistributor, dealer);
        end(players, dealer);
    }

    private void progress(Players players, CardDistributor cardDistributor, Dealer dealer) {
        for (Player player : players.getPlayers()) {
            requestPlayerMoreCard(cardDistributor, player);
        }
        int dealerMoreCardCount = 0;
        while (dealer.isMoreCardAble()) {
            dealer.pick(cardDistributor.distribute());
            dealerMoreCardCount++;
        }
        outputView.printDealerMoreCard(dealer.getNameValue(), dealerMoreCardCount);
    }

    private void end(Players players, Dealer dealer) {
        printFinalCard(dealer);
        players.getPlayers().forEach(this::printFinalCard);
        outputView.printFinalResult(dealer.getNameValue(),new Result(dealer, players).getResult());
    }

    private void printFinalCard(Participant participant) {
        outputView.printCardAndScore(participant.getNameValue(),
                CardStatusConverter.convertToCardStatus(participant.getCardDeck().getCards()), participant.getTotalScore());
    }

    private void printInitialDistribution(Players players, Dealer dealer) {
        outputView.printFirstCardDistribution(dealer.getNameValue(), getPlayerNames(players));

        outputView.printCardStatus(dealer.getNameValue(), CardStatusConverter.convertToCardStatus(List.of(dealer.showOneCard())));
        for (Participant player : players.getPlayers()) {
            outputView.printCardStatus(player.getNameValue(), CardStatusConverter.convertToCardStatus(player.getCardDeck().getCards()));
        }
    }

    private List<String> getPlayerNames(Players players) {
        List<String> playerNames;
        playerNames = players.getPlayers()
                .stream()
                .map(Participant::getName)
                .map(Name::getValue)
                .collect(Collectors.toList());
        return playerNames;
    }

    private void requestPlayerMoreCard(CardDistributor cardDistributor, Player player) {
        boolean isCardRequested = true;

        while (player.isMoreCardAble() && isCardRequested) {
            String answer = inputView.askMoreCard(player.getNameValue());
            validate(answer);
            isCardRequested = isNoStop(cardDistributor, player, answer);
        }
    }

    private void validate(String answer) { 
        if (isNotRequestMoreCardCommand(answer)) {
            throw new IllegalArgumentException(INVALID_MORE_CARD_ERROR_MESSAGE);
        }
    }

    private boolean isNotRequestMoreCardCommand(String answer) {
        return !(answer.equals(MORE_CARD) || answer.equals(CARD_STOP));
    }

    private boolean isNoStop(CardDistributor cardDistributor, Player player, String answer) {
        if (answer.equals(MORE_CARD)) {
            player.pick(cardDistributor.distribute());
        }

        outputView.printCardStatus(player.getNameValue(), CardStatusConverter.convertToCardStatus(player.getCardDeck().getCards()));
        return answer.equals(MORE_CARD);
    }

    private List<String> requestPlayerName() {
        return Arrays.asList(inputView.requestPlayerName().split(DELIMITER, LIMIT_REMOVED));
    }

}
