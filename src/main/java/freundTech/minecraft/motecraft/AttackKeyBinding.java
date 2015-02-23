package freundTech.minecraft.motecraft;

import net.minecraft.client.settings.KeyBinding;

public class AttackKeyBinding extends KeyBinding {

	GameListener glistener;
	WiiMoteListener wlistener;
	
	public AttackKeyBinding(String description, int keyCode, String category,
			GameListener glistener, WiiMoteListener wlistener) {
		super(description, keyCode, category);
		
		this.glistener = glistener;
		this.wlistener = wlistener;
	}

	public boolean isKeyDown()
    {
        return super.isKeyDown() || (glistener.motecounter != -1 && !wlistener.bpressed);
    }
}
