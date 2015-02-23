package freundTech.minecraft.motecraft;

import com.google.common.util.concurrent.Service.Listener;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;

public class MovementInputWiimote extends MovementInput {
	public MovementInput oldMovementInput;
	public WiiMoteListener listener;

	public boolean sprint = false;

	public MovementInputWiimote(MovementInput oldMovementInput,
			WiiMoteListener listener) {
		this.oldMovementInput = oldMovementInput;
		this.listener = listener;

	}

	public void updatePlayerMoveState() {
		oldMovementInput.updatePlayerMoveState();

		this.moveStrafe = oldMovementInput.moveStrafe;
		this.moveForward = oldMovementInput.moveForward;

		this.jump = oldMovementInput.jump;
		this.sneak = oldMovementInput.sneak;

		if (Minecraft.getMinecraft().currentScreen == null) {
			if(listener.analogAngle > -67.5 && listener.analogAngle < 67.5)
			{
				if(listener.analogDistance > 15)
				{
					++this.moveForward;
				}
				
			}
			else if(listener.analogAngle > 112.5 || listener.analogAngle < -112.5)
			{
				if(listener.analogDistance > 15)
				{
					--this.moveForward;
				}
			}
			if(listener.zpressed)
			{
				if(listener.analogAngle >= 22.5 && listener.analogAngle <= 157.5)
				{
					if (listener.analogDistance > 15) {
						--this.moveStrafe;
					}
				}
				if(listener.analogAngle <= -22.5 && listener.analogAngle >= -157.5)
				{
					if (listener.analogDistance > 15) {
						++this.moveStrafe;
					}
				}
			}
			if(listener.analogDistance < 50 && listener.analogDistance > 3)
			{
				this.sneak = true;
			}
			
			/*if (!listener.zpressed) {
				if (listener.analogY > 125) {
					++this.moveForward;
					if (listener.analogY <= 155) {
						this.sneak = true;
					}
					if (listener.analogY > 200) {
						this.sprint = true;
					} else {
						this.sprint = false;
					}
				} else if (listener.analogY < 115) {
					--this.moveForward;
					if (listener.analogY >= 85) {
						this.sneak = true;
					}
				}
			}

			if (listener.zpressed) {
				if (listener.analogX < 115) {
					++this.moveStrafe;
					if (listener.analogX >= 85) {
						this.sneak = true;
					}
				}
				if (listener.analogX > 125) {
					--this.moveStrafe;
					if (listener.analogX <= 155) {
						this.sneak = true;
					}
				}
			}*/

			this.jump = this.jump || listener.cpressed;
			
			if (this.sneak) {
				this.moveStrafe = (float) ((double) this.moveStrafe * 0.3D);
				this.moveForward = (float) ((double) this.moveForward * 0.3D);
			}
		}
		/*
		 * if (this.gameSettings.keyBindBack.isKeyDown()) { --this.moveForward;
		 * }
		 * 
		 * if (this.gameSettings.keyBindLeft.isKeyDown()) { ++this.moveStrafe; }
		 * 
		 * if (this.gameSettings.keyBindRight.isKeyDown()) { --this.moveStrafe;
		 * }
		 * 
		 * this.jump = this.gameSettings.keyBindJump.isKeyDown(); this.sneak =
		 * this.gameSettings.keyBindSneak.isKeyDown();
		 * 
		 * if (this.sneak) { this.moveStrafe = (float)((double)this.moveStrafe *
		 * 0.3D); this.moveForward = (float)((double)this.moveForward * 0.3D); }
		 */
	}
}
