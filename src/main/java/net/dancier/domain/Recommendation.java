package net.dancier.domain;

import net.dancier.domain.dance.Dancer;
import net.dancier.domain.dance.School;

import javax.validation.constraints.NotNull;

/**
 * Wraps something that could be recommended.
 * Instances are created with {@link #of(Recommendable)}
 * @param <T> the Class that implements {@link Recommendable}
 */
public class Recommendation<T extends Recommendable> {

    public enum Type {
        DANCER,
        SCHOOL,
        WORKSHOP,
        EVENING
    }

    public Type type;
    public T payload;

    /**
     * Factory method that creates new instances of
     * a {@link Recommendation}
     * @param recommendable used as the payload of the Recommendation
     * @return in case a proper Recommendable provided by the client, a Recommendation is being returned,
     *         with the recommendable as the payload and a type that is inferred by the concrete class
     *         of the recommendable.
     * @throws IllegalArgumentException in case a {@link Type} could not be inferred for the given recommendable,
     *         an {@link IllegalArgumentException} is being thrown
     */
    public static Recommendation of(@NotNull  Recommendable recommendable) {
        if (recommendable instanceof Dancer) {
            return new Recommendation(Type.DANCER, recommendable);
        } else if (recommendable instanceof School) {
            return new Recommendation(Type.SCHOOL, recommendable);
        } else {
            throw new IllegalArgumentException("Unprocessable recommendable: " + recommendable);
        }
    }
    private Recommendation(Type type, T recommendable) {
        this.type = type;
        this.payload = recommendable;
    }
}
