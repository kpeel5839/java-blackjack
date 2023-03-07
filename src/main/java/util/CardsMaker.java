package util;

import domain.Card;
import domain.Cards;
import type.Letter;
import type.Shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardsMaker {

    private static final List<Card> cards;

    static {
        cards = new ArrayList<>();

        for (Shape shape : Shape.values()) {
            Arrays.stream(Letter.values())
                    .forEach(value -> cards.add(Card.of(shape, value)));
        }
    }

    public static Cards generate() {
        return new Cards(new ArrayList<>(cards));
    }

}
