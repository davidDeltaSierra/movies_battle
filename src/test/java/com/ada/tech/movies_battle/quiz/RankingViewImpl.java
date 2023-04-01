package com.ada.tech.movies_battle.quiz;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class RankingViewImpl implements RankingView {
    double percent;
    String username;
    long total;
}
