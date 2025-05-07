import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import gamevaultbase.entities.Game;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Add Action Games
        List<Game> actionGames = new ArrayList<>();
        actionGames.add(new Game(6, "Call of Duty: Modern Warfare", "Intense first-person shooter with modern warfare setting.", "Infinity Ward", "PC", 59.99f, new Date(), "cod_mw.jpg", "Action"));
        actionGames.add(new Game(7, "Devil May Cry 5", "Stylish action game with intense combat and demon hunting.", "Capcom", "PS4", 49.99f, new Date(), "dmc5.jpg", "Action"));
        actionGames.add(new Game(8, "Doom Eternal", "Fast-paced first-person shooter with demon slaying.", "id Software", "PC", 39.99f, new Date(), "doom_eternal.jpg", "Action"));
        actionGames.add(new Game(9, "Bayonetta 3", "Stylish action game with supernatural combat.", "PlatinumGames", "Switch", 59.99f, new Date(), "bayonetta3.jpg", "Action"));
        actionGames.add(new Game(10, "Metal Gear Rising: Revengeance", "Cyberpunk action game with intense sword combat.", "PlatinumGames", "PC", 29.99f, new Date(), "mgr.jpg", "Action"));
        request.setAttribute("actionGames", actionGames);

        // Add Multiplayer Games
        List<Game> multiplayerGames = new ArrayList<>();
        multiplayerGames.add(new Game(11, "Fortnite", "Free-to-play battle royale game with building mechanics.", "Epic Games", "PC", 0.00f, new Date(), "fortnite.jpg", "Multiplayer"));
        multiplayerGames.add(new Game(12, "Apex Legends", "Free-to-play battle royale with unique character abilities.", "Respawn Entertainment", "PS4", 0.00f, new Date(), "apex.jpg", "Multiplayer"));
        multiplayerGames.add(new Game(13, "Overwatch 2", "Team-based hero shooter with unique characters.", "Blizzard", "PC", 39.99f, new Date(), "overwatch2.jpg", "Multiplayer"));
        multiplayerGames.add(new Game(14, "Valorant", "Tactical team-based shooter with unique agent abilities.", "Riot Games", "PC", 0.00f, new Date(), "valorant.jpg", "Multiplayer"));
        multiplayerGames.add(new Game(15, "Rocket League", "Soccer with rocket-powered cars.", "Psyonix", "PC", 0.00f, new Date(), "rocket_league.jpg", "Multiplayer"));
        request.setAttribute("multiplayerGames", multiplayerGames);

        request.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(request, response);
    }
} 