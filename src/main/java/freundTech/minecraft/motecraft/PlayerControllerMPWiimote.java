package freundTech.minecraft.motecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class PlayerControllerMPWiimote extends PlayerControllerMP {

	private GameListener listener;

	public PlayerControllerMPWiimote(Minecraft mcIn,
			NetHandlerPlayClient p_i45062_2_, GameListener gameListener) {
		super(mcIn, p_i45062_2_);
		listener = gameListener;
	}

	public void onStoppedUsingItem(EntityPlayer playerIn) {
		if (!listener.rightclick) {
			super.onStoppedUsingItem(playerIn);
			System.out.println("Stoppedrightclick");
		}
	}

}
