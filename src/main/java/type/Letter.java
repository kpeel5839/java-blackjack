package type;

public enum Letter {

    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    ACE("A", 11),
    KING("K", 10),
    QUEEN("Q", 10),
    JACK("J", 10);

    private final String expression;
    private final int score;

    Letter(String expression, int score) {
        this.expression = expression;
        this.score = score;
    }

    public String getExpression() {
        return expression;
    }

    public int getScore() {
        return score;
    }

}
