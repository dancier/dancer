package net.dancier.dancer.util;

import net.dancier.dancer.DanceProfileDto;
import net.dancier.dancer.DancerDto;
import net.dancier.dancer.controller.payload.PollResponse;
import net.dancier.dancer.controller.payload.UserSummary;
import net.dancier.dancer.controller.payload.polls.ChoiceResponse;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.model.DanceProfile;
import net.dancier.dancer.model.Dancer;
import net.dancier.dancer.model.Sex;
import net.dancier.dancer.model.polls.Poll;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ModelMapper {
    public static PollResponse mapPollToPollResponse(Poll poll, Map<UUID, Long> choiceVotesMap, User creator, UUID userVote) {
        PollResponse pollResponse = new PollResponse();
        pollResponse.setId(poll.getId());
        pollResponse.setQuestion(poll.getQuestion());
        pollResponse.setCreationDateTime(poll.getCreatedAt());
        pollResponse.setExpirationDateTime(poll.getExpirationDateTime());
        Instant now = Instant.now();
        pollResponse.setExpired(poll.getExpirationDateTime().isBefore(now));

        List<ChoiceResponse> choiceResponses = poll.getChoices().stream().map(choice -> {
            ChoiceResponse choiceResponse = new ChoiceResponse();
            choiceResponse.setId(choice.getId());
            choiceResponse.setText(choice.getText());

            if(choiceVotesMap.containsKey(choice.getId())) {
                choiceResponse.setVoteCount(choiceVotesMap.get(choice.getId()));
            } else {
                choiceResponse.setVoteCount(0);
            }
            return choiceResponse;
        }).collect(Collectors.toList());

        pollResponse.setChoices(choiceResponses);
        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
        pollResponse.setCreatedBy(creatorSummary);

        if(userVote != null) {
            pollResponse.setSelectedChoice(userVote);
        }

        long totalVotes = pollResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
        pollResponse.setTotalVotes(totalVotes);

        return pollResponse;
    }
    public static DancerDto dancerToDancerDto(Dancer dancer) {
        DancerDto dancerDto = new DancerDto();
        dancerDto.setSex(dancer.getSex());
        dancerDto.setId(dancer.getId());
        dancerDto.setSize(dancer.getSize());
        dancerDto.setAbleTo(dancer
                .getAbleTo()
                .stream()
                .map(ModelMapper::danceProfile2danceProfile).collect(Collectors.toSet()));
        dancerDto.setWantsTo(dancer
                .getWantsTo()
                .stream()
                .map(ModelMapper::danceProfile2danceProfile).collect(Collectors.toSet())
        );
        return dancerDto;
    }

    public static DanceProfileDto danceProfile2danceProfile(DanceProfile danceProfile) {
        DanceProfileDto danceProfileDto = new DanceProfileDto();
        danceProfileDto.setDance(danceProfile.getDance().getName());
        danceProfileDto.setLeading(danceProfile.getLeading());
        danceProfileDto.setLevel(danceProfile.getLevel());
        return danceProfileDto;
    }

}
