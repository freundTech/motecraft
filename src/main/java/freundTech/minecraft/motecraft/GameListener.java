package freundTech.minecraft.motecraft;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmorStand;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHangingEntity;
import net.minecraft.item.ItemRedstone;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemSign;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class GameListener {

	private MovementInputWiimote input;
	private PlayerControllerMPWiimote playerController;
	private WiiMoteListener listener;
	private Minecraft mc;

	private Motestate motestate = Motestate.Neutral;
	public int motecounter = -1;
	private boolean motedown = false;

	private Method clickMouse;
	private Method rightClickMouse;
	private Method sendClick;
	private Method keyTyped;

	private Field delayTimer;
	private Field netClientHandler;

	public boolean rightclick;

	private float[] prevYawList = new float[WiimoteConfig.extraViewSmoothing
			+ WiimoteConfig.viewSmoothing];
	private float[] prevPitchList = new float[WiimoteConfig.extraViewSmoothing
			+ WiimoteConfig.viewSmoothing];
	private float prevYaw = 0;
	private float prevPitch = 0;

	private boolean extraViewSmoothing = false;

	Robot bot;

	private static enum Motestate {
		Down, Up, Neutral;
	}

	public GameListener(WiiMoteListener listener) {
		try {
			bot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		mc = Minecraft.getMinecraft();
		this.listener = listener;
		
		try {
			try {
				keyTyped = GuiScreen.class.getDeclaredMethod("func_73869_a",
						Character.TYPE, Integer.TYPE);
			} catch (NoSuchMethodException e) {
				keyTyped = GuiScreen.class.getDeclaredMethod("keyTyped",
						Character.TYPE, Integer.TYPE);
			}
			keyTyped.setAccessible(true);

			try {
				clickMouse = Minecraft.class
						.getDeclaredMethod("func_147116_af");
			} catch (NoSuchMethodException e) {
				clickMouse = Minecraft.class.getDeclaredMethod("clickMouse");
			}
			clickMouse.setAccessible(true);

			try {
				rightClickMouse = Minecraft.class
						.getDeclaredMethod("func_147121_ag");
			} catch (NoSuchMethodException e) {
				rightClickMouse = Minecraft.class
						.getDeclaredMethod("rightClickMouse");
			}
			rightClickMouse.setAccessible(true);

			/*try {

				sendClick = Minecraft.class.getDeclaredMethod("func_147115_a",
						Boolean.TYPE);
			} catch (NoSuchMethodException e) {
				sendClick = Minecraft.class.getDeclaredMethod(
						"sendClickBlockToController", Boolean.TYPE);
			}
			sendClick.setAccessible(true);*/

			try {
				delayTimer = Minecraft.class.getDeclaredField("field_71467_ac");
			} catch (NoSuchFieldException e) {
				delayTimer = Minecraft.class
						.getDeclaredField("rightClickDelayTimer");
			}
			delayTimer.setAccessible(true);

			try {
				netClientHandler = PlayerControllerMP.class
						.getDeclaredField("field_78774_b");
			} catch (NoSuchFieldException e) {
				netClientHandler = PlayerControllerMP.class
						.getDeclaredField("netClientHandler");
			}
			netClientHandler.setAccessible(true);

		} catch (Exception e) {
			System.out
					.println("This Mod is not compatible with this version of Forge/Minecraft!");
			e.printStackTrace();
		}

	}

	@SubscribeEvent
	public void onRenderTickStart(TickEvent.RenderTickEvent event) {
		// Player Rotation

		if (event.phase == Phase.START && mc.currentScreen == null) {
			synchronized (listener.lock) {
				if (mc.thePlayer != null && !listener.zpressed) {
					float yaw = 0;
					if (!listener.yawList.isEmpty()) {
						for (Float v : listener.yawList) {
							yaw += v;
						}
						yaw /= listener.yawList.size();
						listener.yawList = new ArrayList<Float>();
					}

					float pitch = 0;
					if (!listener.pitchList.isEmpty()) {
						for (Float v : listener.pitchList) {
							pitch += v;
						}
						pitch /= listener.pitchList.size();
						listener.pitchList = new ArrayList<Float>();
					}

					// Shift left
					System.arraycopy(prevYawList, 1, prevYawList, 0,
							prevYawList.length - 1);
					System.arraycopy(prevPitchList, 1, prevPitchList, 0,
							prevPitchList.length - 1);
					prevYawList[prevYawList.length - 1] = yaw;
					prevPitchList[prevPitchList.length - 1] = pitch;

					float smoothYaw = 0;
					for (int i = (extraViewSmoothing) ? 0 : prevYawList.length
							- WiimoteConfig.viewSmoothing; i < prevYawList.length; i++) {
						smoothYaw += prevYawList[i];
					}
					smoothYaw /= (extraViewSmoothing)?prevYawList.length:prevYawList.length-WiimoteConfig.extraViewSmoothing;

					float smoothPitch = 0;
					for (int i = (extraViewSmoothing) ? 0 : prevYawList.length
							- WiimoteConfig.viewSmoothing; i < prevPitchList.length; i++) {
						smoothPitch += prevPitchList[i];
					}
					smoothPitch /= (extraViewSmoothing)?prevPitchList.length:prevPitchList.length-WiimoteConfig.extraViewSmoothing;;

					float angle = (float) Math.pow(
							1.0420264,
							Math.abs(listener.analogPoint.x
									- listener.analogCenter.x)) - 1;
					if (listener.analogPoint.x - listener.analogCenter.x < 0) {
						angle *= -1;
					}

					mc.thePlayer
							.setAngles(
									(float) (angle + (prevPitch - smoothPitch * WiimoteConfig.viewSensibilityX) / 0.15D),
									(float) ((prevYaw - smoothYaw * WiimoteConfig.viewSensibilityY) / 0.15D));

					prevYaw = smoothYaw * WiimoteConfig.viewSensibilityY;
					prevPitch = smoothPitch * WiimoteConfig.viewSensibilityX;

				}
			}
		}
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase == Phase.START) {
			// Clicking and Rightclicking
			rightclick = false;

			if (mc.thePlayer != null
					&& (mc.currentScreen == null || mc.currentScreen.allowUserInput)) {

				if (this.motecounter != -1) {
					++this.motecounter;
				}

				if (listener.accelZmoved < -45
						&& this.motestate == Motestate.Down) {
					this.motecounter = 0;
				}

				ItemStack item = mc.thePlayer.inventory.getCurrentItem();
				if (item == null) {
					item = new ItemStack(new Item(), 1, 0);
				}
				if (listener.accelZmoved < -70) {
					if (!this.motedown) {
						this.motedown = true;
						if (!mc.thePlayer.isUsingItem()) {
							if (!listener.bpressed && mc.objectMouseOver.typeOfHit != MovingObjectType.BLOCK) {
								/*
								 * Hack for accessing private method. Please
								 * forgive me .____.
								 */
								try {
									clickMouse.invoke(mc);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							if (listener.bpressed
									&& item.getItemUseAction() == EnumAction.NONE) {
								/*
								 * Hack for accessing private method. Please
								 * forgive me .____.
								 */
								try {
									rightClickMouse.invoke(mc);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}

					this.motecounter = 0;
					this.motestate = Motestate.Down;

				} else {
					this.motedown = false;
				}

				if (listener.accelZmoved > 20 && this.motestate == Motestate.Up) {
					this.motecounter = 0;
				}

				if (listener.accelZmoved > 40) {
					this.motecounter = 0;
					this.motestate = Motestate.Up;
				}

				if (this.motecounter > 5) {
					this.motecounter = -1;
					this.motestate = Motestate.Neutral;
				}
				
				if (this.motecounter != -1) {
					this.extraViewSmoothing = true;
				} else {
					this.extraViewSmoothing = false;
				}

				if (this.motecounter != -1
						&& (item.getItemUseAction() == EnumAction.EAT || item
								.getItemUseAction() == EnumAction.DRINK) && listener.bpressed) {
					rightclick = true;
					/*
					 * Hack for accessing private method. Please forgive me
					 * .____.
					 */
					try {
						rightClickMouse.invoke(mc);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				if (listener.bpressed
						&& (item.getItemUseAction() == EnumAction.BLOCK || item
								.getItemUseAction() == EnumAction.BOW)) {
					rightclick = true;
					try {
						if ((Integer) delayTimer.get(mc) == 0
								&& !mc.thePlayer.isUsingItem()) {
							/*
							 * Hack for accessing private method. Please forgive
							 * me .____.
							 */
							try {
								rightClickMouse.invoke(mc);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} catch (Exception e) {
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.player == mc.thePlayer) {
			EntityPlayerSP player = (EntityPlayerSP) event.player;

			// Wiimote Rumble
			if (event.player.hurtTime > 0) {
				if (MoteCraft.mote != null) {
					MoteCraft.mote.rumble(100);
				}
			}
			
			if (event.player.isDead)
			{
				if (MoteCraft.mote != null) {
					MoteCraft.mote.rumble(10000);
				}
			}

			// Movement
			if (mc.thePlayer.movementInput != input) {
				input = new MovementInputWiimote(mc.thePlayer.movementInput,
						listener);
				mc.thePlayer.movementInput = input;
			}

			if (mc.playerController != playerController) {
				try {
					playerController = new PlayerControllerMPWiimote(mc,
							(NetHandlerPlayClient) netClientHandler
									.get(mc.playerController), this);
					mc.playerController = playerController;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// Hotbar Slots
			int slot = player.inventory.currentItem + listener.slotoffset;
			listener.slotoffset = 0;

			slot = (slot > 8) ? 0 : slot;
			slot = (slot < 0) ? 8 : slot;

			player.inventory.currentItem = slot;

			// Inventory Close
			if (mc.currentScreen != null) {
				if (listener.homepressed) {
					listener.homepressed = false;
					/*
					 * Hack for accessing protected method. Please forgive me
					 * .____.
					 */
					try {
						keyTyped.invoke(mc.currentScreen, ' ', 1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			// Inventory Open
			if (mc.currentScreen == null || mc.currentScreen.allowUserInput) {
				if (listener.homepressed) {
					listener.homepressed = false;
					if (mc.playerController.isRidingHorse()) {
						player.sendHorseInventory();
					} else {
						mc.getNetHandler()
								.addToSendQueue(
										new C16PacketClientStatus(
												C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
						mc.displayGuiScreen(new GuiInventory(player));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onDraw(GuiScreenEvent.DrawScreenEvent.Post event) {
		// Inventory Mouse Mode
		if (listener.mousePosX != 0 && listener.mousePosY != 0) {
			float mousePosX = (float) ((listener.mousePosX + 1) / 1024.0 * 1.5);
			float mousePosY = (float) ((listener.mousePosY + 1) / 1024.0 * 1.5);
			mousePosX = (float) ((Math.min(Math.max(mousePosX, 0), 1) - 0.5)
					* -1 + 0.5);
			mousePosY = (float) ((Math.min(Math.max(mousePosY, 0), 1) - 0.5)
					* -1 + 0.5);
			mousePosX *= Display.getWidth();
			mousePosY *= Display.getHeight();
			Mouse.setCursorPosition((int) mousePosX, (int) mousePosY);
			
			if (listener.apressed) {
				bot.mousePress(InputEvent.BUTTON1_MASK);
			} else {
				bot.mouseRelease(InputEvent.BUTTON1_MASK);
			}
			if (listener.bpressed) {
				bot.mousePress(InputEvent.BUTTON3_MASK);
			} else {
				bot.mouseRelease(InputEvent.BUTTON3_MASK);
			}
		}
	}
}
