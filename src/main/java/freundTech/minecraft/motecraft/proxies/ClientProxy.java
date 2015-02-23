package freundTech.minecraft.motecraft.proxies;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import freundTech.minecraft.motecraft.AttackKeyBinding;
import freundTech.minecraft.motecraft.GameListener;
import freundTech.minecraft.motecraft.GuiWiimoteStatus;
import freundTech.minecraft.motecraft.MovementInputWiimote;
import freundTech.minecraft.motecraft.SprintKeyBinding;
import freundTech.minecraft.motecraft.MoteCraft;
import freundTech.minecraft.motecraft.WiiMoteListener;
import freundTech.minecraft.motecraft.WiimoteBatteryUpdater;
import freundTech.minecraft.motecraft.WiimoteConfig;
import freundTech.minecraft.motecraft.WiimoteFinder;

public class ClientProxy extends CommonProxy {
	private WiimoteFinder finder;
	private WiimoteBatteryUpdater bat;
	private GameListener game;
	private WiiMoteListener wiimotelistener;

	@Override
	public void init(FMLInitializationEvent event) {
		Minecraft mc = Minecraft.getMinecraft();

		wiimotelistener = new WiiMoteListener();

		finder = new WiimoteFinder(wiimotelistener);
		bat = new WiimoteBatteryUpdater();
		bat.start();

		game = new GameListener(wiimotelistener);

		FMLCommonHandler.instance().bus().register(game);
		MinecraftForge.EVENT_BUS.register(game);
		GuiWiimoteStatus status = new GuiWiimoteStatus(mc, finder);
		MinecraftForge.EVENT_BUS.register(status);
		FMLCommonHandler.instance().bus().register(status);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		WiimoteConfig.update(event.getSuggestedConfigurationFile());

		/*
		 * Replace Splashes. Warning!!! Really hacky. Better don't read it :P
		 */
		if (WiimoteConfig.replaceSplashes) {
			try {
				ResourceLocation newSplashes = new ResourceLocation(
						MoteCraft.MODID, "texts/splashes.txt");
				Field splashes;
				try {
					splashes = GuiMainMenu.class
							.getDeclaredField("field_110353_x");
				} catch (NoSuchFieldException e) {
					splashes = GuiMainMenu.class
							.getDeclaredField("splashTexts");
				}
				// "Unfinal" the Field
				Field modifiersField = Field.class
						.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				modifiersField.setInt(splashes, splashes.getModifiers()
						& ~Modifier.FINAL);

				splashes.setAccessible(true);
				splashes.set(null, newSplashes);
			} catch (Exception e) {
			}
		}

	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

		Minecraft mc = Minecraft.getMinecraft();

		mc.gameSettings.keyBindSprint = new SprintKeyBinding(
				mc.gameSettings.keyBindSprint.getKeyDescription(),
				mc.gameSettings.keyBindSprint.getKeyCode(),
				mc.gameSettings.keyBindSprint.getKeyCategory(), wiimotelistener);
		mc.gameSettings.keyBindAttack = new AttackKeyBinding(
				mc.gameSettings.keyBindAttack.getKeyDescription(),
				mc.gameSettings.keyBindAttack.getKeyCode(),
				mc.gameSettings.keyBindAttack.getKeyCategory(), game,
				wiimotelistener);
	}
}
