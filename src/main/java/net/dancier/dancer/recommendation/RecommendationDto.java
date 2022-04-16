package net.dancier.dancer.recommendation;

public class RecommendationDto<T extends Object> {
    public enum Type {
        DANCER,
        EVENT,
        LINK
    }

    public Type type;
    public T payload;

    public RecommendationDto(T recommendable) {
        this.type = Type.DANCER;
        this.payload = recommendable;
    }
}
