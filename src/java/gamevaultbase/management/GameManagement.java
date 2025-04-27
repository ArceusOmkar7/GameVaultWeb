/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gamevaultbase.management;

import gamevaultbase.entities.Game;
import gamevaultbase.exceptions.GameNotFoundException;
import gamevaultbase.storage.GameStorage;

import java.util.List;

public class GameManagement {

    private final GameStorage gameStorage;

    public GameManagement(GameStorage gameStorage) {
        this.gameStorage = gameStorage;
    }

    public Game getGame(int gameId) throws GameNotFoundException {
        Game game = gameStorage.findById(gameId);
        if (game == null) {
            throw new GameNotFoundException("Game not found with ID: " + gameId);
        }
        return game;
    }

    public List<Game> getAllGames() {
        return gameStorage.findAll();
    }

    // Get games that a user owns (from completed orders)
    public List<Game> getOwnedGames(int userId) {
        return gameStorage.findOwnedGamesByUser(userId);
    }

    // Check if a user already owns a specific game
    public boolean isGameOwnedByUser(int userId, int gameId) {
        List<Game> ownedGames = getOwnedGames(userId);
        return ownedGames.stream().anyMatch(game -> game.getGameId() == gameId);
    }

    public void addGame(Game game) {
        gameStorage.save(game);
    }

    public void updateGame(Game game) {
        gameStorage.update(game);
    }

    public void deleteGame(int gameId) {
        gameStorage.delete(gameId);
    }
}
