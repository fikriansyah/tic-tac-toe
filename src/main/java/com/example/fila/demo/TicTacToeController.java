package com.example.fila.demo;

import com.example.fila.demo.dto.BoardSizeRequest;
import com.example.fila.demo.service.GameService;
import com.example.fila.demo.entity.Game;
import com.example.fila.demo.entity.Game.*;
import com.example.fila.demo.entity.User;
import com.example.fila.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class TicTacToeController {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String takeTurns(
            Model model,
            @RequestParam("tile_id") String tileId,
            @RequestParam(value = "new_game", required = false, defaultValue = "false") boolean newGame,
            @RequestParam(value = "first_play", required = false, defaultValue = "false") boolean firstPlay,
            @RequestParam(value = "board_size", required = false, defaultValue = "3") int boardSize
    ) {
        System.out.println("tileId: "+tileId);
        System.out.println("newGame: "+newGame);
        User user = userRepository.findByUsername("asd");

        BoardSizeRequest sizeRequest = new BoardSizeRequest(); // set board size request
        sizeRequest.setColumn(boardSize);
        sizeRequest.setRows(boardSize);

        Game game;
        if (newGame) {
            game = gameService.create(user, firstPlay, sizeRequest);

            if (!firstPlay) {
                gameService.takeTurn(game, "1-1"); // 1-1 means computer always set to second row and second column
            }
        } else {
            game = gameService.getLastGame(user);
            gameService.takeTurn(game, tileId);
            gameService.takeTurnRandom(game);
        }

        setModelAttributes(model, game);

        return "index";
    }

    private void setModelAttributes(Model model, Game game) {
        boolean playerGoFirst = game.getPlayer1Type() == PlayerType.HUMAN;

        // set player status (win/lost by state)
        String playerStatus;
        switch (game.getState()) {
            case PLAYER_1_WIN:
                playerStatus = playerGoFirst ? "WON" : "LOST";
                break;
            case PLAYER_2_WIN:
                playerStatus = playerGoFirst ? "LOST" : "WON";
                break;
            case DRAW:
                playerStatus = "DRAW";
                break;
            case IN_PROGRESS:
            default:
                playerStatus = "IN_PROGRESS";
                break;
        }

        model.addAttribute("playerGoFirst", playerGoFirst);
        model.addAttribute("playStatus", playerStatus);
        model.addAttribute("board", game.getRows());
    }
}
