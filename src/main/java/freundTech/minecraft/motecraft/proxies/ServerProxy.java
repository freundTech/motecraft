package freundTech.minecraft.motecraft.proxies;

import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy extends CommonProxy{
	public void postInit(FMLPostInitializationEvent event) {
		for (int i = 0; i < 20; i++) {
			System.out.println("Motecraft mod is not compatible with Servers!!!");
		}
	}
}
