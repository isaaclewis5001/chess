package model.response;


import model.data.GameDesc;

import java.util.Collection;

public record ListGamesResponse(Collection<GameDesc> games) {}
