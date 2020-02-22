package com.robertx22.mine_and_slash.new_content.building;

import com.robertx22.mine_and_slash.new_content.BuiltRoom;
import com.robertx22.mine_and_slash.new_content.RoomRotation;
import com.robertx22.mine_and_slash.new_content.UnbuiltRoom;
import com.robertx22.mine_and_slash.new_content.enums.RoomType;
import com.robertx22.mine_and_slash.new_content.registry.DungeonRoom;
import com.robertx22.mine_and_slash.new_content.registry.RoomsList;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.RandomUtils;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DungeonBuilder {

    public DungeonBuilder(long worldSeed, ChunkPos cpos) {

        int chunkX = cpos.x;
        int chunkZ = cpos.z;
        int distToEntranceX = 8 - (chunkX % 16);
        int distToEntranceZ = 11 - (chunkZ % 16);
        chunkX += distToEntranceX;
        chunkZ += distToEntranceZ;

        long newSeed = (worldSeed + (long) (chunkX * chunkX * 4987142) + (long) (chunkX * 5947611) + (long) (chunkZ * chunkZ) * 4392871L + (long) (chunkZ * 389711) ^ worldSeed);
        rand = new Random(newSeed);

    }

    Dungeon dungeon;
    Random rand;
    public int size = 25;
    public boolean isTesting = false;

    public void build() {
        dungeon = new Dungeon(size);

        setupEntrance();

        while (!dungeon.isFinished()) {

            dungeon.unbuiltRooms.forEach(x -> {

                UnbuiltRoom unbuilt = dungeon.getUnbuiltFor(x.left, x.right);
                RoomRotation rot = randomDungeonRoom(unbuilt);
                DungeonRoom dRoom = RoomsList.randomDungeonRoom(RoomsList.getAllOfType(rot.type), rand);
                BuiltRoom room = new BuiltRoom(rot, dRoom.loc);

                dungeon.addRoom(x.left, x.right, room);
            });

        }

    }

    public RoomRotation randomDungeonRoom(UnbuiltRoom unbuilt) {

        if (dungeon.shouldStartFinishing()) {
            return random(RoomType.END.getPossibleFor(unbuilt));
        } else {
            List<RoomType> types = new ArrayList<>();
            types.add(RoomType.CURVED_HALLWAY);
            types.add(RoomType.STRAIGHT_HALLWAY);
            types.add(RoomType.FOUR_WAY);
            types.add(RoomType.TRIPLE_HALLWAY);

            List<RoomRotation> possible = new ArrayList<>();

            types.forEach(x -> {
                x.getPossibleFor(unbuilt);
            });

            return random(possible);

        }
    }

    public RoomRotation random(List<RoomRotation> list) {
        return RandomUtils.weightedRandom(list, rand.nextDouble());
    }

    private void setupEntrance() {
        DungeonRoom entranceRoom = RoomsList.randomDungeonRoom(RoomsList.getAllOfType(RoomType.ENTRANCE), rand);
        RoomRotation rotation = new RoomRotation(RoomType.ENTRANCE, RoomType.ENTRANCE.sides, Rotation.NONE);
        BuiltRoom entrance = new BuiltRoom(rotation, entranceRoom.loc);

        int mid = dungeon.getMiddle();
        dungeon.addRoom(mid, mid, entrance);
    }

}
