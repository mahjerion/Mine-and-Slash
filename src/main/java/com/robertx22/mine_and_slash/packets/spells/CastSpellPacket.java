package com.robertx22.mine_and_slash.packets.spells;

import com.robertx22.mine_and_slash.saveclasses.spells.PlayerSpellsData;
import com.robertx22.mine_and_slash.uncommon.capability.PlayerSpellCap;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CastSpellPacket {

    public Integer hotbarNumber;
    public PlayerSpellsData.Hotbar hotbar;

    public CastSpellPacket() {

    }

    public CastSpellPacket(Integer hotbarNumber, PlayerSpellsData.Hotbar bar) {
        this.hotbarNumber = hotbarNumber;
        this.hotbar = bar;

    }

    public static CastSpellPacket decode(PacketBuffer buf) {

        CastSpellPacket newpkt = new CastSpellPacket();

        newpkt.hotbarNumber = buf.readInt();
        newpkt.hotbar = PlayerSpellsData.Hotbar.valueOf(buf.readString(30));

        return newpkt;

    }

    public static void encode(CastSpellPacket packet, PacketBuffer tag) {

        tag.writeInt(packet.hotbarNumber);
        tag.writeString(packet.hotbar.name());

    }

    public static void handle(final CastSpellPacket pkt, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            try {

                ServerPlayerEntity player = ctx.get().getSender();

                PlayerSpellCap.ISpellsCap spells = Load.spells(player);

                if (spells.getSpellData().canCast(pkt.hotbarNumber, pkt.hotbar, player)) {
                    spells.getSpellData().setToCast(pkt.hotbarNumber, pkt.hotbar, player, 0);

                    spells.syncToClient(player);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        ctx.get().setPacketHandled(true);
    }

}