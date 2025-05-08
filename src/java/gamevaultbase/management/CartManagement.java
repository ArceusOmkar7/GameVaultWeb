package gamevaultbase.management;

import gamevaultbase.entities.Cart;
import gamevaultbase.entities.Game;
import gamevaultbase.exceptions.CartEmptyException;
import gamevaultbase.exceptions.GameAlreadyOwnedException;
import gamevaultbase.exceptions.UserNotFoundException;
import gamevaultbase.exceptions.GameNotFoundException;
import gamevaultbase.storage.CartStorage;
import gamevaultbase.storage.GameStorage;

import java.util.List;

public class CartManagement {

    private final CartStorage cartStorage;
    private final GameStorage gameStorage;

    public CartManagement(CartStorage cartStorage) {
        this.cartStorage = cartStorage;
        this.gameStorage = null; // Will be initialized in the constructor that takes GameStorage
    }

    public CartManagement(CartStorage cartStorage, GameStorage gameStorage) {
        this.cartStorage = cartStorage;
        this.gameStorage = gameStorage;
    }

    public Cart getCart(int userId) {
        Cart cart = cartStorage.findById(userId);
        if (cart == null) {
            cart = new Cart(userId); // Create a new cart if it doesn't exist
            cartStorage.save(cart);
        }
        return cart;
    }

    public void addGameToCart(int userId, int gameId)
            throws GameAlreadyOwnedException, UserNotFoundException, GameNotFoundException {
        // Check if the user already owns the game
        if (gameStorage != null) {
            List<Game> ownedGames = gameStorage.findOwnedGamesByUser(userId);
            boolean alreadyOwns = ownedGames.stream().anyMatch(game -> game.getGameId() == gameId);
            if (alreadyOwns) {
                throw new GameAlreadyOwnedException("You already own this game");
            }
        }

        // Attempt to add to cart - this will validate if user and game exist
        boolean success = cartStorage.addGameToCart(userId, gameId);

        // Handle validation errors
        if (!success) {
            // Try to determine the specific reason for failure
            // We'll need to check the logs to see specific errors,
            // but we can make some educated guesses based on the IDs
            if (userId <= 0) {
                throw new UserNotFoundException("Invalid user ID: " + userId);
            } else if (gameId <= 0) {
                throw new GameNotFoundException("Invalid game ID: " + gameId);
            } else {
                throw new RuntimeException("Failed to add game to cart. See server logs for details.");
            }
        }
    }

    public void removeGameFromCart(int userId, int gameId) {
        Cart cart = getCart(userId);
        cartStorage.removeGameFromCart(userId, gameId);
    }

    public List<Game> getGamesInCart(int userId) throws CartEmptyException {
        List<Game> games = cartStorage.getGamesInCart(userId);
        if (games.isEmpty()) {
            throw new CartEmptyException("Cart is empty for user: " + userId);
        }
        return games;
    }
}