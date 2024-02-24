package model.response;

import model.GameDesc;

import java.util.Collection;

public record ListGamesResponse(Collection<GameDesc> games) {}
