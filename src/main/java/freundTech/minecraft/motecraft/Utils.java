package freundTech.minecraft.motecraft;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemSign;
import motej.Mote;
import motej.MoteFinder;
import motej.MoteFinderListener;

public class Utils {
	public static void drawTexturedQuadFit(double x, double y, double width,
			double height, double zLevel) {
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getWorldRenderer().startDrawingQuads();
		tessellator.getWorldRenderer().addVertexWithUV(x + 0, y + height,
				zLevel, 0, 1);
		tessellator.getWorldRenderer().addVertexWithUV(x + width, y + height,
				zLevel, 1, 1);
		tessellator.getWorldRenderer().addVertexWithUV(x + width, y + 0,
				zLevel, 1, 0);
		tessellator.getWorldRenderer().addVertexWithUV(x + 0, y + 0, zLevel, 0,
				0);
		tessellator.draw();
	}
}