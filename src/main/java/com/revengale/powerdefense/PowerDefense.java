package com.revengale.powerdefense;

import com.revengale.powerdefense.proxies.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = PowerDefense.MODID, name = PowerDefense.MODNAME, version = PowerDefense.VERSION)
public class PowerDefense {

    public static final String MODID = "powerdefense";
    public static final String MODNAME = "PowerDefense Mod";
    public static final String VERSION = "1.0.0";
    
    @SidedProxy(clientSide="com.revengale.powerdefense.proxies.ClientProxy", serverSide="com.revengale.powerdefense.proxies.ServerProxy")
    public static CommonProxy proxy;
        
    @Instance
    public static PowerDefense instance = new PowerDefense();
        
     
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	proxy.preInit(e);
    }
        
    @EventHandler
    public void init(FMLInitializationEvent e) {
    	proxy.init(e);
    }
        
    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}