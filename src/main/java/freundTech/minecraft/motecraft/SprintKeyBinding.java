package freundTech.minecraft.motecraft;

import net.minecraft.client.settings.KeyBinding;

public class SprintKeyBinding extends KeyBinding{
	
	WiiMoteListener listener;
	
	public SprintKeyBinding(String description, int keyCode, String category,
			WiiMoteListener listener) {
		super(description, keyCode, category);
		
		this.listener = listener;
	}

	public boolean isKeyDown()
    {
        return super.isKeyDown() || (listener.analogDistance > 90 && listener.analogAngle > -67.5 && listener.analogAngle < 67.5);
    }

}
