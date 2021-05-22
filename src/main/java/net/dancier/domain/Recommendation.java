package net.dancier.domain;

import net.dancier.domain.dance.Dancer;

public class Recommendation<T extends Recommendable> {

    public enum Type {
        DANCER,
        SCHOOL,
        WORKSHOP,
        EVENING
    }

    public Type type;
    public T payload;

    public static Recommendation of(Recommendable recommendable) {
        if (recommendable instanceof Dancer) {
            return new Recommendation(Type.DANCER, recommendable);
        }
        return null;
    }
    private Recommendation(Type type, T recommendable) {
        this.type = type;
        this.payload = recommendable;
    }
}
