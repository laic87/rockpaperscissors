package laic.rockpaperscissors.repository;

import laic.rockpaperscissors.model.Game;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class GameRepository {

    private final Map<UUID, Game> gameMap = new HashMap<>();

    public UUID save(Game game) {
        gameMap.put(game.getId(), game);
        return game.getId();
    }

    public Optional<Game> findGame(UUID id) {
        return Optional.ofNullable(gameMap.get(id));
    }
}