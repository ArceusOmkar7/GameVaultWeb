package gamevaultbase.helpers;

import java.util.List;

public class GameJspHelper {
    public static boolean isOwned(List<Integer> ownedGameIds, Object gameId) {
        System.out.println("GameJspHelper.isOwned: Checking game ID = " + gameId);
        System.out.println("GameJspHelper.isOwned: ownedGameIds = " + ownedGameIds);

        if (ownedGameIds == null || gameId == null) {
            System.out.println("GameJspHelper.isOwned: ownedGameIds or gameId is null");
            return false;
        }

        try {
            int id = Integer.parseInt(gameId.toString());
            boolean result = ownedGameIds.contains(id);
            System.out.println("GameJspHelper.isOwned: Checking if " + id + " is in ownedGameIds. Result = " + result);
            return result;
        } catch (NumberFormatException e) {
            System.out.println("GameJspHelper.isOwned: NumberFormatException for gameId = " + gameId);
            return false;
        }
    }
}