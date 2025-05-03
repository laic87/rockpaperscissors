package laic.rockpaperscissors.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import laic.rockpaperscissors.dto.GameDTO;
import laic.rockpaperscissors.model.Game;
import laic.rockpaperscissors.model.enums.GameStatus;
import laic.rockpaperscissors.model.enums.Move;
import laic.rockpaperscissors.model.Player;
import laic.rockpaperscissors.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createGame_returnsGameId() throws Exception {
        var gameId = UUID.randomUUID();
        var request = new GameController.NameRequest();
        request.setName("Alice");

        when(gameService.createGame("Alice")).thenReturn(gameId);

        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json("\"" + gameId.toString() + "\""));
    }

    @Test
    void joinGame_callsServiceAndReturnsOk() throws Exception {
        var id = UUID.randomUUID();
        var request = new GameController.NameRequest();
        request.setName("Bob");

        mockMvc.perform(post("/api/games/" + id + "/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(gameService).joinGame(id, "Bob");
    }

    @Test
    void makeMove_callsServiceAndReturnsOk() throws Exception {
        var id = UUID.randomUUID();
        var request = new GameController.MoveRequest();
        request.setName("Alice");
        request.setMove("rock");

        mockMvc.perform(post("/api/games/" + id + "/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(gameService).makeMove(id, "Alice", Move.ROCK);
    }

    @Test
    void getGame_returnsGameJson() throws Exception {
        var id = UUID.randomUUID();
        var game = Game.builder()
                .status(GameStatus.FINISHED)
                .player1(Player.builder().name("Alice").build())
                .player2(Player.builder().name("Bob").build())
                .result("Alice wins!")
                .build();

        var gameDTO = GameDTO.builder()
                .id(id)
                .player1(game.getPlayer1().getName())
                .player2(game.getPlayer2().getName())
                .status(GameStatus.FINISHED)
                .result(game.getResult())
                .build();

        when(gameService.getGameView(id)).thenReturn(gameDTO);

        mockMvc.perform(get("/api/games/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.status").value("FINISHED"))
                .andExpect(jsonPath("$.result").value("Alice wins!"));
    }
}