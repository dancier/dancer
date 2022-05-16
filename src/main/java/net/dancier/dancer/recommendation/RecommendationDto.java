package net.dancier.dancer.recommendation;

import net.dancier.dancer.core.model.Dancer;
import net.dancier.dancer.school.School;

public class RecommendationDto {
    public enum Type {
        DANCER,
        SCHOOL,
        EVENT,
        LINK
    }

    public Type type;
    public Object payload;

    public RecommendationDto(Object recommendable) {
        if (recommendable instanceof Dancer){
            this.type = Type.DANCER;
            this.payload = recommendable;
        } else if (recommendable instanceof School) {
            this.type = Type.SCHOOL;
            this.payload = ModelMapper.schoolToSchoolDto((School)recommendable);
        }
    }
}
