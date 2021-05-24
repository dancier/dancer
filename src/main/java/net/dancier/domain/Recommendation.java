package net.dancier.domain;

import net.dancier.domain.dance.Dancer;
import net.dancier.domain.dance.School;

public class Recommendation<T extends Recommendable> {

    public enum Type {
        DANCER,
        SCHOOL,
        WORKSHOP,
        EVENING,
        ETC
    }

    public Type type;
    public T payload;

    public static Recommendation of(Recommendable recommendable) {
        if (recommendable instanceof Dancer) {
            return new Recommendation(Type.DANCER, recommendable);
        } else if (recommendable instanceof School) {
            return new Recommendation(Type.SCHOOL, recommendable);
        } else {
            return new Recommendation(Type.ETC, recommendable);
        }
    }
    private Recommendation(Type type, T recommendable) {
        this.type = type;
        this.payload = recommendable;
    }
}
