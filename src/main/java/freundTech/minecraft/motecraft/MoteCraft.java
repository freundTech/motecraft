package freundTech.minecraft.motecraft;

import freundTech.minecraft.motecraft.proxies.ClientProxy;
import freundTech.minecraft.motecraft.proxies.CommonProxy;
import motej.Mote;
import motejx.extensions.nunchuk.Nunchuk;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = MoteCraft.MODID, version = MoteCraft.VERSION, name=MoteCraft.NAME)
public class MoteCraft {
	public static final String MODID = "motecraft";
	public static final String NAME = "MoteCraft";
    public static final String VERSION = "0.4";
    
    @Instance(MoteCraft.MODID)
    public static MoteCraft instance;
    
    @SidedProxy(clientSide="freundTech.minecraft.motecraft.proxies.ClientProxy", serverSide="freundTech.minecraft.motecraft.proxies.ServerProxy")
    public static CommonProxy proxy;
    
    
    
    public static Mote mote = null;
    public static Nunchuk nunchuk = null;
    
    
    
    @EventHandler
    public void init(FMLInitializationEvent event) throws Exception
    {
    	this.proxy.init(event);
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	this.proxy.preInit(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	this.proxy.postInit(event);
    }
    
}
