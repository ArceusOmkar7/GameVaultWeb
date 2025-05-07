package gamevaultbase.helpers;

import gamevaultbase.entities.Game;
import gamevaultbase.storage.GameStorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class to initialize the database with game data on application
 * startup.
 * This class avoids relying on JSON files by directly creating Game objects.
 */
public class DbInitializer {

    private static final Logger LOGGER = Logger.getLogger(DbInitializer.class.getName());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Checks if games exist in the database, and if not, populates it with initial
     * game data.
     */
    public static void initializeGames() {
        GameStorage gameStorage = new GameStorage();

        // Check if database already has games
        List<Game> existingGames = gameStorage.findAll();
        if (!existingGames.isEmpty()) {
            LOGGER.info("Database already contains " + existingGames.size() + " games. Skipping initialization.");
            return;
        }

        LOGGER.info("No games found in database. Initializing with default game data...");

        List<Game> games = createGamesList();
        int count = 0;

        // Save all games to database
        for (Game game : games) {
            try {
                gameStorage.save(game);
                count++;
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to save game: " + game.getTitle(), e);
            }
        }

        LOGGER.info("Successfully initialized database with " + count + " games.");
    }

    /**
     * Creates a list of Game objects with predefined data.
     * 
     * @return List of Game objects
     */
    private static List<Game> createGamesList() {
        List<Game> games = new ArrayList<>();

        // Game 1
        games.add(createGame(
                "The Legend of Zelda: Tears of the Kingdom",
                "An open-world adventure where Link explores the vast lands and skies of Hyrule, utilizing new abilities to combat emerging threats.",
                "Nintendo EPD",
                "Nintendo Switch",
                14.99f,
                "2023-05-12",
                "https://media.rawg.io/media/games/556/55684bfd048706f4266d331d70050b37.jpg",
                "Adventure, Action",
                4.39f));

        // Game 2
        games.add(createGame(
                "Elden Ring",
                "An expansive action RPG set in a dark fantasy world, offering players freedom to explore and challenging combat encounters.",
                "FromSoftware",
                "PC, PlayStation 5, Xbox One, PlayStation 4, Xbox Series S/X",
                49.99f,
                "2022-02-25",
                "https://media.rawg.io/media/games/b29/b294fdd866dcdb643e7bab370a552855.jpg",
                "Action, RPG",
                4.4f));

        // Game 3
        games.add(createGame(
                "Baldur's Gate III",
                "A story-rich RPG that emphasizes player choice, set in the Dungeons & Dragons universe, allowing for diverse character interactions and outcomes.",
                "Larian Studios",
                "PC, PlayStation 5, Xbox Series S/X, macOS",
                59.99f,
                "2023-08-03",
                "https://media.rawg.io/media/games/699/69907ecf13f172e9e144069769c3be73.jpg",
                "Strategy, Adventure, RPG",
                4.45f));

        // Game 4
        games.add(createGame(
                "Red Dead Redemption 2",
                "An epic tale of life in America's unforgiving heartland, blending storytelling with deep exploration and immersive gameplay.",
                "Rockstar Games",
                "PC, Xbox One, PlayStation 4",
                44.99f,
                "2018-10-26",
                "https://media.rawg.io/media/games/511/5118aff5091cb3efec399c808f8c598f.jpg",
                "Action",
                4.59f));

        // Game 5
        games.add(createGame(
                "The Witcher 3: Wild Hunt",
                "A narrative-driven RPG where Geralt of Rivia hunts monsters and navigates complex political landscapes in a richly detailed world.",
                "CD Projekt Red",
                "PC, PlayStation 5, Xbox One, PlayStation 4, Xbox Series S/X, Nintendo Switch, macOS",
                29.99f,
                "2015-05-18",
                "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg",
                "Action, RPG",
                4.64f));

        // Game 6
        games.add(createGame(
                "God of War Ragnarök",
                "Kratos and his son Atreus embark on a mythic journey through Norse realms, facing gods and monsters in a quest to prevent Ragnarök.",
                "Santa Monica Studio",
                "PC",
                59.99f,
                null,
                "https://media.rawg.io/media/screenshots/3c4/3c4c51b66741363d83b56ce66b1240ec.jpg",
                "Adventure, Action, RPG",
                4.5f));

        // Game 7
        games.add(createGame(
                "Cyberpunk 2077",
                "An open-world action-adventure set in Night City, a megalopolis obsessed with power, glamour, and body modification.",
                "CD Projekt Red",
                "PC, PlayStation 5, Xbox One, PlayStation 4, Xbox Series S/X",
                24.99f,
                "2020-12-10",
                "https://media.rawg.io/media/games/26d/26d4437715bee60138dab4a7c8c59c92.jpg",
                "Shooter, Action, RPG",
                4.21f));

        // Game 8
        games.add(createGame(
                "Minecraft",
                "A sandbox game that allows players to build and explore infinite worlds, fostering creativity and survival skills.",
                "Mojang Studios",
                "PC, Xbox One, PlayStation 4, Nintendo Switch, iOS, Android, Nintendo 3DS, macOS, Linux, Xbox 360, PlayStation 3, PS Vita, Wii U",
                29.99f,
                "2009-05-10",
                "https://media.rawg.io/media/games/b4e/b4e4c73d5aa4ec66bbf75375c4847a2b.jpg",
                "Action, Arcade, Simulation, Indie, Massively Multiplayer",
                4.43f));

        // Game 9
        games.add(createGame(
                "Grand Theft Auto V",
                "An action-adventure game featuring three protagonists in a sprawling open world, combining storytelling with diverse missions.",
                "Rockstar North",
                "PC, PlayStation 5, Xbox One, PlayStation 4, Xbox Series S/X, Xbox 360, PlayStation 3",
                14.99f,
                "2013-09-17",
                "https://media.rawg.io/media/games/20a/20aa03a10cda45239fe22d035c0ebe64.jpg",
                "Action",
                4.47f));

        // Game 10
        games.add(createGame(
                "Hades",
                "A rogue-like dungeon crawler where you defy the god of death as you hack and slash out of the Underworld of Greek myth.",
                "Supergiant Games",
                "PC, PlayStation 5, Xbox One, PlayStation 4, Xbox Series S/X, Nintendo Switch",
                19.99f,
                "2020-09-17",
                "https://media.rawg.io/media/games/1f4/1f47a270b8f241e4676b14d39ec620f7.jpg",
                "Indie, Adventure, Action, RPG",
                4.44f));

        // Game 11
        games.add(createGame(
                "Hollow Knight",
                "A challenging 2D action-adventure game set in a vast, interconnected world filled with secrets and powerful enemies.",
                "Team Cherry",
                "PC, Xbox One, PlayStation 4, Nintendo Switch, macOS, Linux",
                44.99f,
                "2017-02-23",
                "https://media.rawg.io/media/games/4cf/4cfc6b7f1850590a4634b08bfab308ab.jpg",
                "Platformer, Indie, Action",
                4.4f));

        // Game 12
        games.add(createGame(
                "Sekiro: Shadows Die Twice",
                "A third-person action-adventure game with intense swordplay, set in a reimagined late 1500s Sengoku period Japan.",
                "FromSoftware",
                "PC, Xbox One, PlayStation 4",
                14.99f,
                "2019-03-22",
                "https://media.rawg.io/media/games/67f/67f62d1f062a6164f57575e0604ee9f6.jpg",
                "Action, RPG",
                4.38f));

        // Game 13
        games.add(createGame(
                "Dark Souls III",
                "An action RPG known for its challenging combat and deep lore, set in a dark, decaying world.",
                "FromSoftware",
                "PC, Xbox One, PlayStation 4",
                9.99f,
                "2016-04-11",
                "https://media.rawg.io/media/games/da1/da1b267764d77221f07a4386b6548e5a.jpg",
                "Action, RPG",
                4.4f));

        // Game 14
        games.add(createGame(
                "Bloodborne",
                "A gothic action RPG with fast-paced combat and a mysterious, atmospheric world.",
                "FromSoftware",
                "PlayStation 4",
                44.99f,
                "2015-03-24",
                "https://media.rawg.io/media/games/214/214b29aeff13a0ae6a70fc4426e85991.jpg",
                "Action, RPG",
                4.41f));

        // Game 15
        games.add(createGame(
                "Resident Evil 4",
                "A modernized version of the classic survival horror game, featuring updated graphics and gameplay mechanics.",
                "Capcom",
                "PC, PlayStation 5, PlayStation 4, Xbox Series S/X, iOS",
                29.99f,
                "2023-03-24",
                "https://media.rawg.io/media/games/51a/51a404b9918a0b19fc704a3ca248c69f.jpg",
                "Adventure, Action",
                4.62f));

        // Game 16
        games.add(createGame(
                "Final Fantasy VII Rebirth",
                "The second installment in the Final Fantasy VII remake project, continuing the story with enhanced visuals and gameplay.",
                "Square Enix",
                "PC, PlayStation 5",
                29.99f,
                "2024-02-29",
                "https://media.rawg.io/media/games/511/511995d5dfcf18965dbb354d2ba9e176.jpg",
                "Adventure, Action, RPG",
                4.46f));

        // Game 17
        games.add(createGame(
                "Marvel's Spider-Man 2",
                "An action-adventure game where players swing through New York City as both Peter Parker and Miles Morales.",
                "Insomniac Games",
                "PC, PlayStation 5",
                44.99f,
                "2023-10-20",
                "https://media.rawg.io/media/games/7ae/7ae5a14cdb4ab222a134c15f4629e430.jpg",
                "Adventure, Action",
                4.36f));

        // Game 18
        games.add(createGame(
                "Persona 5 Royal",
                "An enhanced version of the original JRPG, adding new characters, storylines, and gameplay mechanics.",
                "Atlus",
                "PC, PlayStation 5, Xbox One, PlayStation 4, Xbox Series S/X, Nintendo Switch",
                44.99f,
                "2020-03-31",
                "https://media.rawg.io/media/games/a9c/a9c789951de65da545d51f664b4f2ce0.jpg",
                "Adventure, RPG",
                4.75f));

        // Game 19
        games.add(createGame(
                "Stardew Valley",
                "A farming simulation game where players can grow crops, raise animals, and build relationships in a charming rural town.",
                "ConcernedApe",
                "PC, Xbox One, PlayStation 4, Nintendo Switch, iOS, Android, macOS, Linux, PS Vita",
                14.99f,
                "2016-02-25",
                "https://media.rawg.io/media/games/713/713269608dc8f2f40f5a670a14b2de94.jpg",
                "Indie, RPG, Simulation",
                4.4f));

        // Game 20
        games.add(createGame(
                "Celeste",
                "A challenging platformer that tells a heartfelt story about overcoming personal struggles.",
                "Matt Makes Games",
                "PC, Xbox One, PlayStation 4, Nintendo Switch, macOS, Linux",
                49.99f,
                "2018-01-25",
                "https://media.rawg.io/media/games/594/59487800889ebac294c7c2c070d02356.jpg",
                "Platformer, Indie",
                4.38f));

        // Game 21
        games.add(createGame(
                "Disco Elysium: Final Cut",
                "A detective RPG featuring deep narrative choices and a unique skill system.",
                "ZA/UM",
                "PC, PlayStation 5, Xbox One, PlayStation 4, Xbox Series S/X, Nintendo Switch, iOS, macOS",
                14.99f,
                "2021-03-30",
                "https://media.rawg.io/media/games/0af/0afe9e8ace196123d8c7cf22172cec63.jpg",
                "Adventure, RPG",
                4.66f));

        // Game 22
        games.add(createGame(
                "Death Stranding",
                "An open-world action game with a focus on delivering supplies across a fractured America.",
                "Kojima Productions",
                "PC, PlayStation 4",
                49.99f,
                "2019-11-08",
                "https://media.rawg.io/media/games/2ad/2ad87a4a69b1104f02435c14c5196095.jpg",
                "Adventure, Action",
                4.32f));

        // Game 23
        games.add(createGame(
                "Ghost of Tsushima",
                "An open-world action-adventure game set in feudal Japan, focusing on samurai combat and stealth.",
                "Sucker Punch Productions",
                "PC, PlayStation 5, PlayStation 4",
                19.99f,
                "2020-07-17",
                "https://media.rawg.io/media/games/f24/f2493ea338fe7bd3c7d73750a85a0959.jpeg",
                "Adventure, Action, RPG",
                4.41f));

        // Game 24
        games.add(createGame(
                "The Last of Us Part II",
                "A narrative-driven action-adventure game exploring themes of revenge and redemption.",
                "Naughty Dog",
                "PlayStation 5, PlayStation 4",
                19.99f,
                "2020-06-19",
                "https://media.rawg.io/media/games/909/909974d1c7863c2027241e265fe7011f.jpg",
                "Shooter, Adventure, Action",
                4.42f));

        // Game 25
        games.add(createGame(
                "Monster Hunter: World",
                "An action RPG where players hunt massive creatures in a living, breathing ecosystem.",
                "Capcom",
                "PC, Xbox One, PlayStation 4",
                49.99f,
                "2018-01-26",
                "https://media.rawg.io/media/games/21c/21cc15d233117c6809ec86870559e105.jpg",
                "Adventure, Action, RPG",
                4.01f));

        // Game 26
        games.add(createGame(
                "DOOM Eternal",
                "A fast-paced first-person shooter that continues the story of the Doom Slayer battling demonic forces.",
                "id Software",
                "PC, PlayStation 5, Xbox One, PlayStation 4, Nintendo Switch",
                29.99f,
                "2020-03-20",
                "https://media.rawg.io/media/games/3ea/3ea3c9bbd940b6cb7f2139e42d3d443f.jpg",
                "Shooter, Action",
                4.38f));

        // Game 27
        games.add(createGame(
                "Overwatch 2",
                "A team-based multiplayer first-person shooter with a diverse cast of heroes.",
                "Blizzard Entertainment",
                "PC, PlayStation 5, Xbox One, PlayStation 4, Xbox Series S/X, Nintendo Switch",
                69.99f,
                "2022-10-04",
                "https://media.rawg.io/media/games/95a/95a10817d1fc648cff1153f3fa8ef6c5.jpg",
                "Shooter, Massively Multiplayer, Action",
                3.11f));

        // Game 28
        games.add(createGame(
                "Apex Legends",
                "A free-to-play battle royale game set in the Titanfall universe.",
                "Respawn Entertainment",
                "PC, Xbox One, PlayStation 4, Nintendo Switch, macOS",
                14.99f,
                "2019-02-04",
                "https://media.rawg.io/media/games/737/737ea5662211d2e0bbd6f5989189e4f1.jpg",
                "Shooter, Action",
                3.64f));

        // Game 29
        games.add(createGame(
                "Fortnite Battle Royale",
                "A battle royale game known for its building mechanics and frequent content updates.",
                "Epic Games",
                "PC, PlayStation 5, PlayStation 4, Xbox One, Xbox Series S/X, Nintendo Switch, iOS, Android",
                59.99f,
                "2017-09-26",
                "https://media.rawg.io/media/games/dcb/dcbb67f371a9a28ea38ffd73ee0f53f3.jpg",
                "Shooter, Action",
                3.27f));

        // Game 30
        games.add(createGame(
                "Call of Duty: Warzone",
                "A free-to-play battle royale game set in the Call of Duty universe.",
                "Infinity Ward",
                "PC, Xbox One, PlayStation 4",
                9.99f,
                "2020-03-10",
                "https://media.rawg.io/media/games/7e3/7e327a055bedb9b6d1be86593bef473d.jpg",
                "Shooter",
                3.62f));

        // Game 31
        games.add(createGame(
                "League of Legends",
                "A multiplayer online battle arena game with a large roster of champions.",
                "Riot Games",
                "PC, macOS",
                49.99f,
                "2009-10-27",
                "https://media.rawg.io/media/games/78b/78bc81e247fc7e77af700cbd632a9297.jpg",
                "Strategy, Action, RPG",
                3.65f));

        // Game 32
        games.add(createGame(
                "Valorant",
                "A tactical first-person shooter combining precise gunplay with unique agent abilities.",
                "Riot Games",
                "PC, PlayStation 5, Xbox Series S/X",
                14.99f,
                "2020-06-02",
                "https://media.rawg.io/media/games/b11/b11127b9ee3c3701bd15b9af3286d20e.jpg",
                "Shooter, Strategy, Action",
                3.52f));

        // Game 33
        games.add(createGame(
                "Counter-Strike 2",
                "The latest installment in the iconic tactical shooter series.",
                "Valve",
                "PC, Linux",
                49.99f,
                "2023-09-27",
                "https://media.rawg.io/media/games/ec4/ec4b02bdb3eb5c6212992c19bc05697e.jpg",
                "Shooter",
                3.59f));

        // Game 34
        games.add(createGame(
                "Dota 2",
                "A multiplayer online battle arena game known for its deep strategic gameplay.",
                "Valve",
                "PC, macOS, Linux",
                59.99f,
                "2013-07-09",
                "https://media.rawg.io/media/games/6fc/6fcf4cd3b17c288821388e6085bb0fc9.jpg",
                "Massively Multiplayer, Action",
                3.06f));

        // Game 35
        games.add(createGame(
                "Among Us",
                "A multiplayer party game where players work together to complete tasks while impostors attempt to sabotage them.",
                "Innersloth",
                "PC, PlayStation 5, PlayStation 4, Xbox One, Nintendo Switch, iOS, Android",
                69.99f,
                "2018-07-25",
                "https://media.rawg.io/media/games/e74/e74458058b35e01c1ae3feeb39a3f724.jpg",
                "Casual, Action, Simulation",
                3.84f));

        // Game 36
        games.add(createGame(
                "Genshin Impact",
                "An open-world action RPG with elemental combat and a gacha system.",
                "miHoYo",
                "PC, PlayStation 5, PlayStation 4, Nintendo Switch, iOS, Android",
                29.99f,
                "2020-09-28",
                "https://media.rawg.io/media/games/c38/c38bdb5da139005777176d33c463d70f.jpg",
                "Adventure, Action, RPG",
                3.56f));

        // Game 37
        games.add(createGame(
                "Animal Crossing: New Horizons",
                "A life simulation game where players build and manage their own island paradise.",
                "Nintendo EPD",
                "Nintendo Switch",
                24.99f,
                "2020-03-20",
                "https://media.rawg.io/media/games/42f/42fe1abd4d7c11ca92d93a0fb0f8662b.jpg",
                "Simulation",
                4.31f));

        // Game 38
        games.add(createGame(
                "Super Mario Odyssey",
                "A 3D platformer where Mario travels across various kingdoms to rescue Princess Peach.",
                "Nintendo EPD",
                "Nintendo Switch",
                44.99f,
                "2017-10-27",
                "https://media.rawg.io/media/games/267/267bd0dbc496f52692487d07d014c061.jpg",
                "Platformer, Arcade",
                4.42f));

        // Game 39
        games.add(createGame(
                "Metroid Dread",
                "A side-scrolling action-adventure game continuing the story of bounty hunter Samus Aran.",
                "MercurySteam",
                "Nintendo Switch",
                49.99f,
                "2021-10-08",
                "https://media.rawg.io/media/games/c26/c262f8b54b46edc72594c4a9bb8ee13e.jpg",
                "Platformer, Action, RPG",
                4.37f));

        // Game 40
        games.add(createGame(
                "Fire Emblem: Three Houses",
                "A tactical RPG where players lead students through battles and school life.",
                "Intelligent Systems",
                "Nintendo Switch",
                29.99f,
                "2019-07-26",
                "https://media.rawg.io/media/games/530/53081dbd5003f990fa5312404ac3d71a.jpg",
                "Strategy, RPG",
                4.36f));

        // Game 41
        games.add(createGame(
                "Bayonetta 3",
                "An action game featuring stylish combat and a charismatic witch protagonist.",
                "PlatinumGames",
                "Nintendo Switch",
                39.99f,
                "2022-10-28",
                "https://media.rawg.io/media/games/c30/c30ac50cb13096f5402250bf666a321c.jpg",
                "Action",
                4.25f));

        // Game 42
        games.add(createGame(
                "Cuphead",
                "A run-and-gun platformer known for its challenging gameplay and 1930s cartoon art style.",
                "Studio MDHR",
                "PC, Xbox One, PlayStation 4, Nintendo Switch, macOS",
                24.99f,
                "2017-09-29",
                "https://media.rawg.io/media/games/226/2262cea0b385db6cf399f4be831603b0.jpg",
                "Platformer, Indie, Action",
                4.37f));

        // Game 43
        games.add(createGame(
                "Dead Cells",
                "A rogue-lite, metroidvania-inspired action-platformer.",
                "Motion Twin",
                "PC, Xbox One, PlayStation 4, Nintendo Switch, iOS, macOS, Linux",
                69.99f,
                "2018-08-07",
                "https://media.rawg.io/media/games/f90/f90ee1a4239247a822771c40488e68c5.jpg",
                "Platformer, Indie, Action, RPG",
                4.24f));

        // Game 44
        games.add(createGame(
                "Slay the Spire",
                "A deck-building roguelike game combining card games and roguelike elements.",
                "MegaCrit",
                "PC, Xbox One, PlayStation 4, Nintendo Switch, iOS, Android, macOS, Linux",
                69.99f,
                "2019-01-22",
                "https://media.rawg.io/media/games/f52/f5206d55f918edf8ee07803101106fa6.jpg",
                "Card, Strategy, Indie, RPG",
                4.37f));

        // Game 45
        games.add(createGame(
                "It Takes Two",
                "A cooperative action-adventure game designed for split-screen multiplayer.",
                "Hazelight Studios",
                "PC, PlayStation 5, Xbox One, PlayStation 4, Xbox Series S/X, Nintendo Switch",
                29.99f,
                "2021-03-26",
                "https://media.rawg.io/media/games/d47/d479582ed0a46496ad34f65c7099d7e5.jpg",
                "Platformer, Adventure, Action",
                4.46f));

        // Game 46
        games.add(createGame(
                "Sea of Thieves",
                "A multiplayer pirate adventure game with open-world exploration.",
                "Rare",
                "PC, PlayStation 5, Xbox One, Xbox Series S/X",
                39.99f,
                "2018-03-20",
                "https://media.rawg.io/media/games/5f6/5f61441e6338e9221f96a8f4c64c7bb8.jpg",
                "Massively Multiplayer, Adventure, Action",
                3.68f));

        // Game 47
        games.add(createGame(
                "Hogwarts Legacy",
                "An open-world action RPG set in the Harry Potter universe.",
                "Avalanche Software",
                "PC, PlayStation 5, Xbox One, PlayStation 4, Xbox Series S/X, Nintendo Switch",
                29.99f,
                "2023-02-10",
                "https://media.rawg.io/media/games/044/044b2ee023930ca138deda151f40c18c.jpg",
                "Action, RPG",
                3.96f));

        // Game 48
        games.add(createGame(
                "Starfield",
                "A space-themed RPG from the creators of The Elder Scrolls and Fallout series.",
                "Bethesda Game Studios",
                "PC, Xbox Series S/X",
                29.99f,
                "2023-09-06",
                "https://media.rawg.io/media/games/ba8/ba82c971336adfd290e4c0eab6504fcf.jpg",
                "Adventure, RPG",
                3.27f));

        // Game 49
        games.add(createGame(
                "Palworld",
                "An open-world survival game featuring creatures that can be captured and used in combat.",
                "Pocketpair",
                "PC, Xbox Series S/X",
                59.99f,
                "2024-01-19",
                "https://media.rawg.io/media/games/4e9/4e9c951414c732923fa72d5b1da49402.jpg",
                "Indie, Adventure, Action, RPG",
                3.52f));

        // Game 50
        games.add(createGame(
                "Warframe",
                "A free-to-play cooperative third-person shooter set in a sci-fi universe.",
                "Digital Extremes",
                "PC, PlayStation 5, Xbox One, PlayStation 4, Xbox Series S/X, Nintendo Switch, iOS",
                69.99f,
                "2013-03-25",
                "https://media.rawg.io/media/games/f87/f87457e8347484033cb34cde6101d08d.jpg",
                "Shooter, Massively Multiplayer, Action, RPG",
                3.42f));

        return games;
    }

    /**
     * Helper method to create a Game object with the provided parameters.
     */
    private static Game createGame(String title, String description, String developer,
            String platform, float price, String releaseDateStr,
            String imagePath, String genre, float rating) {
        Game game = new Game();
        game.setTitle(title);
        game.setDescription(description);
        game.setDeveloper(developer);
        game.setPlatform(platform);
        game.setPrice(price);

        // Parse release date if not null
        if (releaseDateStr != null) {
            try {
                Date releaseDate = DATE_FORMAT.parse(releaseDateStr);
                game.setReleaseDate(releaseDate);
            } catch (ParseException e) {
                LOGGER.log(Level.WARNING, "Failed to parse release date for game: " + title, e);
            }
        }

        // Fix image URLs by replacing "media.rawg.io" with "https://media.rawg.io"
        if (imagePath != null && !imagePath.startsWith("http")) {
            imagePath = "https://" + imagePath;
        }

        game.setImagePath(imagePath);
        game.setGenre(genre);
        game.setRating(rating);

        return game;
    }
}