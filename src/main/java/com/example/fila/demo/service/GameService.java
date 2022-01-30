package com.example.fila.demo.service;

import java.util.List;

import com.example.fila.demo.constant.BoardTile;
import com.example.fila.demo.dto.BoardSizeRequest;
import com.example.fila.demo.entity.Game;
import com.example.fila.demo.entity.Game.*;
import com.example.fila.demo.entity.User;
import com.example.fila.demo.repository.GameRepository;
import com.example.fila.demo.util.BoardUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    // call when player go first
    @Transactional
    public Game create(User user, boolean playerGoFirst, BoardSizeRequest sizeRequest) {

        gameRepository.deleteUserGames(user); // delete existing game

        Game game = new Game();
        game.setUser(user);
        game.setState(GameState.IN_PROGRESS);
        game.setNextMove(PlayerNumber.PLAYER_1);
        game.setBoardSize(sizeRequest.getColumn());

        if (playerGoFirst) {
            game.setPlayer1Type(PlayerType.HUMAN);
            game.setPlayer2Type(PlayerType.COMPUTER);
        } else {
            game.setPlayer1Type(PlayerType.COMPUTER);
            game.setPlayer2Type(PlayerType.HUMAN);
        }

        game.setRows(BoardUtil.createEmpty(sizeRequest));

        gameRepository.save(game);

        return game;
    }

    // returning array of rows of last game
    // game status is ongoing or finish(which player is won or draw)
    // next player to take turn
    public Game getLastGame(User user) {
        List<Game> game = gameRepository.findAll();
        System.out.println("list all game: "+game.size());
        return gameRepository.findFirstByUserOrderByIdDesc(user);
    }

    public void takeTurnRandom(Game game) {
        String tileId = BoardUtil.getRandomAvailableTile(game.getRows());
        if (tileId != null) {
            takeTurn(game, tileId);
        }
    }

    // call after player taking turn
    // get current game data and set to new one
    public void takeTurn(Game game, String tileId) {
        if (game.getState() != GameState.IN_PROGRESS) {
            return; // if game is over then return
        }

        String[] index = tileId.split("-");
        if (index.length != 2) { // validate that tile always n-n
            return;
        }

        BoardTile tile;
        if (game.getNextMove() == PlayerNumber.PLAYER_1) {
            tile = BoardTile.X; // set tile to X letter
            game.setNextMove(PlayerNumber.PLAYER_2);
        } else {
            tile = BoardTile.O; // set tile to O letter
            game.setNextMove(PlayerNumber.PLAYER_1);
        }

        int rowIndex = Integer.parseInt(index[0]); // get row index from input
        int columnIndex = Integer.parseInt(index[1]); // get col index from input
        game.getRows().get(rowIndex).set(columnIndex, tile.toString()); // set selected tile

        GameState state = validateGameState(game.getRows(), game);
        game.setState(state);
        if (state != GameState.IN_PROGRESS) {
            game.setNextMove(null); // no player can take turn
        }

        gameRepository.save(game);

        log.info("game data: {}", gameRepository.findAll().size());
    }

    private GameState validateGameState(List<List<String>> rows, Game game) {
        System.out.println("isi board: "+game.getBoardSize());
        List<List<String>> data = BoardUtil.getAllLines(rows, game.getBoardSize());
        for (List<String> line : data) {
            System.out.println("line is: "+line);
            String firstTile = line.get(0);
            if (firstTile.isEmpty()) { // skip empty tile
                continue;
            }

            if (line.stream().allMatch(tile -> tile.equals(firstTile))) {
                return firstTile.equals(BoardTile.X.toString()) ? GameState.PLAYER_1_WIN : GameState.PLAYER_2_WIN;
            }
        }

        for (List<String> row : rows) {
            if (row.stream().anyMatch(String::isEmpty)) {
                return GameState.IN_PROGRESS;
            }
        }

        return GameState.DRAW;
    }
}
