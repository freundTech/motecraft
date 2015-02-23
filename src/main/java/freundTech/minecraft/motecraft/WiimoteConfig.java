package freundTech.minecraft.motecraft;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class WiimoteConfig {
	public static int viewSmoothing = 7;
	public static int extraViewSmoothing = 5;
	public static float viewSensibilityX = 1f;
	public static float viewSensibilityY = 1.3f;
	public static boolean replaceSplashes = true;

	private static final String CATEGORY_VIEW = "View";
	private static final String CATEGORY_MISC = "Misc";

	public static void update(File file) {
		Configuration config = new Configuration(file);

		config.load();

		// View
		WiimoteConfig.viewSmoothing = config.getInt("viewSmoothing",
				CATEGORY_VIEW, viewSmoothing, 1, 50,
				"Smoothing when tilting the Nunchuck to look around");
		WiimoteConfig.extraViewSmoothing = config.getInt("extraViewSmoothing",
				CATEGORY_VIEW, extraViewSmoothing, 0, 50,
				"Extra View Smoothing when breaking Blocks");
		WiimoteConfig.viewSensibilityX = config.getFloat("viewSensibilityX",
				CATEGORY_VIEW, viewSensibilityX, 0.1f, 5f,
				"View Sensibility on the X (right-left) axis");
		WiimoteConfig.viewSensibilityY = config.getFloat("viewSensibilityY",
				CATEGORY_VIEW, viewSensibilityY, 1f, 5f,
				"View Sensibility on the Y (up-down) axis");

		// Misc
		WiimoteConfig.replaceSplashes = config.getBoolean("replaceSplashes",
				CATEGORY_MISC, replaceSplashes,
				"Replace Splash Texts in Main Menu");

		config.save();
	}

}
