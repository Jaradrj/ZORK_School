package game;

import lombok.Getter;

public class Exit {
    @Getter
    private String targetRoom;
    private String requiredFlag;

    public Exit(String targetRoom, String requiredFlag) {
        this.targetRoom = targetRoom;
        this.requiredFlag = requiredFlag;
    }

    public boolean isAccessible(Player player) {
        return requiredFlag == null || player.hasFlag(requiredFlag);
    }

}
