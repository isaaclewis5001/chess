package model.response;


import model.data.GameDesc;

import java.util.List;

public record ListGamesResponse(List<GameDesc> games) {}
