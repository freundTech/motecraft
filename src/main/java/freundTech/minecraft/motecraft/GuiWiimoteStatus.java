package freundTech.minecraft.motecraft;

import freundTech.minecraft.motecraft.Utils;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GuiWiimoteStatus extends Gui {
	private Minecraft mc;

	private byte animstate = 0;

	public GuiWiimoteStatus(Minecraft mc, WiimoteFinder finder) {
		super();

		this.mc = mc;
	}

	@SubscribeEvent
	public void onDraw(GuiScreenEvent.DrawScreenEvent.Post event) {
		if (event.gui instanceof GuiMainMenu
				|| event.gui instanceof GuiIngameMenu) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			if (MoteCraft.mote == null) {
				this.mc.renderEngine.bindTexture(new ResourceLocation(
						MoteCraft.MODID, String.format("wiimote_search%02d.png",
								this.animstate / 3)));
			} else {
				this.mc.renderEngine.bindTexture(new ResourceLocation(
						MoteCraft.MODID, "wiimote_found.png"));
			}
			/*
			 * this.drawTexturedModalRect( 0, 0, 0, 0, 230, 1000);
			 */
			Utils.drawTexturedQuadFit(10, 10, 32, 32, zLevel + 1);
		}
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		this.animstate = (byte) ((this.animstate + 1) % (16 * 3));
	}
}
