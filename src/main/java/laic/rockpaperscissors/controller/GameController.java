package laic.rockpaperscissors.controller;

import laic.rockpaperscissors.dto.GameDTO;
import laic.rockpaperscissors.exception.NoNameException;
import laic.rockpaperscissors.model.enums.Move;
import laic.rockpaperscissors.service.GameService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<UUID> createGame(@RequestBody NameRequest request) {
        validateName(request.getName());
        UUID id = gameService.createGame(request.getName());
        return ResponseEntity.ok(id);
    }

    @PostMapping(path = "/{id}/join")
    public ResponseEntity<Void> joinGame(@PathVariable UUID id, @RequestBody NameRequest request) {
        validateName(request.getName());
        gameService.joinGame(id, request.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/{id}/move")
    public ResponseEntity<Void> makeMove(@PathVariable UUID id, @RequestBody MoveRequest request) {
        validateName(request.getName());
        gameService.makeMove(id, request.getName(), Move.valueOf(request.getMove().toUpperCase()));
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<GameDTO> getGame(@PathVariable UUID id) {
        return ResponseEntity.ok(gameService.getGameView(id));
    }

    private void validateName(String name) {
        if (name.isBlank()) {
            throw new NoNameException("Please provide a name");
        }
    }

    @Getter
    @Setter
    public static class NameRequest {
        private String name;
    }

    @Getter
    @Setter
    public static class MoveRequest extends NameRequest {
        private String move;
    }
}