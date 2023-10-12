/*     */ package com.viaversion.viaversion;
/*     */ 
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.ViaAPI;
/*     */ import com.viaversion.viaversion.api.command.ViaCommandSender;
/*     */ import com.viaversion.viaversion.api.configuration.ConfigurationProvider;
/*     */ import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
/*     */ import com.viaversion.viaversion.api.platform.PlatformTask;
/*     */ import com.viaversion.viaversion.api.platform.UnsupportedSoftware;
/*     */ import com.viaversion.viaversion.api.platform.ViaInjector;
/*     */ import com.viaversion.viaversion.api.platform.ViaPlatform;
/*     */ import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
/*     */ import com.viaversion.viaversion.bukkit.commands.BukkitCommandHandler;
/*     */ import com.viaversion.viaversion.bukkit.commands.BukkitCommandSender;
/*     */ import com.viaversion.viaversion.bukkit.listeners.JoinListener;
/*     */ import com.viaversion.viaversion.bukkit.platform.BukkitViaAPI;
/*     */ import com.viaversion.viaversion.bukkit.platform.BukkitViaConfig;
/*     */ import com.viaversion.viaversion.bukkit.platform.BukkitViaInjector;
/*     */ import com.viaversion.viaversion.bukkit.platform.BukkitViaLoader;
/*     */ import com.viaversion.viaversion.bukkit.platform.BukkitViaTask;
/*     */ import com.viaversion.viaversion.bukkit.platform.BukkitViaTaskTask;
/*     */ import com.viaversion.viaversion.bukkit.platform.PaperViaInjector;
/*     */ import com.viaversion.viaversion.commands.ViaCommandHandler;
/*     */ import com.viaversion.viaversion.dump.PluginInfo;
/*     */ import com.viaversion.viaversion.libs.gson.JsonObject;
/*     */ import com.viaversion.viaversion.unsupported.UnsupportedPlugin;
/*     */ import com.viaversion.viaversion.unsupported.UnsupportedServerSoftware;
/*     */ import com.viaversion.viaversion.util.GsonUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.command.CommandExecutor;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.command.TabCompleter;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Event;
/*     */ import org.bukkit.event.EventException;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ViaVersionPlugin
/*     */   extends JavaPlugin
/*     */   implements ViaPlatform<Player>
/*     */ {
/*  58 */   private static final boolean FOLIA = PaperViaInjector.hasClass("io.papermc.paper.threadedregions.RegionizedServer");
/*     */   private static ViaVersionPlugin instance;
/*     */   private final BukkitCommandHandler commandHandler;
/*     */   private final BukkitViaConfig conf;
/*  62 */   private final ViaAPI<Player> api = (ViaAPI<Player>)new BukkitViaAPI(this);
/*     */   private boolean protocolSupport;
/*     */   private boolean lateBind;
/*     */   
/*     */   public ViaVersionPlugin() {
/*  67 */     instance = this;
/*     */ 
/*     */     
/*  70 */     this.commandHandler = new BukkitCommandHandler();
/*     */ 
/*     */     
/*  73 */     BukkitViaInjector injector = new BukkitViaInjector();
/*  74 */     Via.init(ViaManagerImpl.builder()
/*  75 */         .platform(this)
/*  76 */         .commandHandler((ViaCommandHandler)this.commandHandler)
/*  77 */         .injector((ViaInjector)injector)
/*  78 */         .loader((ViaPlatformLoader)new BukkitViaLoader(this))
/*  79 */         .build());
/*     */ 
/*     */     
/*  82 */     this.conf = new BukkitViaConfig();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLoad() {
/*  87 */     this.protocolSupport = (Bukkit.getPluginManager().getPlugin("ProtocolSupport") != null);
/*  88 */     this.lateBind = !((BukkitViaInjector)Via.getManager().getInjector()).isBinded();
/*     */     
/*  90 */     if (!this.lateBind) {
/*  91 */       getLogger().info("ViaVersion " + getDescription().getVersion() + " is now loaded. Registering protocol transformers and injecting...");
/*  92 */       ((ViaManagerImpl)Via.getManager()).init();
/*     */     } else {
/*  94 */       getLogger().info("ViaVersion " + getDescription().getVersion() + " is now loaded. Waiting for boot (late-bind).");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/* 100 */     ViaManagerImpl manager = (ViaManagerImpl)Via.getManager();
/* 101 */     if (this.lateBind) {
/* 102 */       getLogger().info("Registering protocol transformers and injecting...");
/* 103 */       manager.init();
/*     */     } 
/*     */     
/* 106 */     if (Via.getConfig().shouldRegisterUserConnectionOnJoin())
/*     */     {
/*     */       
/* 109 */       getServer().getPluginManager().registerEvents((Listener)new JoinListener(), (Plugin)this);
/*     */     }
/*     */     
/* 112 */     if (FOLIA) {
/*     */       Class<? extends Event> serverInitEventClass;
/*     */ 
/*     */       
/*     */       try {
/* 117 */         serverInitEventClass = (Class)Class.forName("io.papermc.paper.threadedregions.RegionizedServerInitEvent");
/* 118 */       } catch (ReflectiveOperationException e) {
/* 119 */         throw new RuntimeException(e);
/*     */       } 
/*     */       
/* 122 */       getServer().getPluginManager().registerEvent(serverInitEventClass, new Listener() {  }, EventPriority.HIGHEST, (listener, event) -> manager.onServerLoaded(), (Plugin)this);
/*     */     }
/* 124 */     else if (Via.getManager().getInjector().lateProtocolVersionSetting()) {
/*     */       
/* 126 */       runSync(manager::onServerLoaded);
/*     */     } else {
/* 128 */       manager.onServerLoaded();
/*     */     } 
/*     */     
/* 131 */     getCommand("viaversion").setExecutor((CommandExecutor)this.commandHandler);
/* 132 */     getCommand("viaversion").setTabCompleter((TabCompleter)this.commandHandler);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 137 */     ((ViaManagerImpl)Via.getManager()).destroy();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPlatformName() {
/* 142 */     return Bukkit.getServer().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPlatformVersion() {
/* 147 */     return Bukkit.getServer().getVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPluginVersion() {
/* 152 */     return getDescription().getVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public PlatformTask runAsync(Runnable runnable) {
/* 157 */     if (FOLIA) {
/* 158 */       return (PlatformTask)new BukkitViaTaskTask(Via.getManager().getScheduler().execute(runnable));
/*     */     }
/* 160 */     return (PlatformTask)new BukkitViaTask(getServer().getScheduler().runTaskAsynchronously((Plugin)this, runnable));
/*     */   }
/*     */ 
/*     */   
/*     */   public PlatformTask runRepeatingAsync(Runnable runnable, long ticks) {
/* 165 */     if (FOLIA) {
/* 166 */       return (PlatformTask)new BukkitViaTaskTask(Via.getManager().getScheduler().schedule(runnable, ticks * 50L, TimeUnit.MILLISECONDS));
/*     */     }
/* 168 */     return (PlatformTask)new BukkitViaTask(getServer().getScheduler().runTaskTimerAsynchronously((Plugin)this, runnable, 0L, ticks));
/*     */   }
/*     */ 
/*     */   
/*     */   public PlatformTask runSync(Runnable runnable) {
/* 173 */     if (FOLIA)
/*     */     {
/* 175 */       return runAsync(runnable);
/*     */     }
/* 177 */     return (PlatformTask)new BukkitViaTask(getServer().getScheduler().runTask((Plugin)this, runnable));
/*     */   }
/*     */ 
/*     */   
/*     */   public PlatformTask runSync(Runnable runnable, long delay) {
/* 182 */     return (PlatformTask)new BukkitViaTask(getServer().getScheduler().runTaskLater((Plugin)this, runnable, delay));
/*     */   }
/*     */ 
/*     */   
/*     */   public PlatformTask runRepeatingSync(Runnable runnable, long period) {
/* 187 */     return (PlatformTask)new BukkitViaTask(getServer().getScheduler().runTaskTimer((Plugin)this, runnable, 0L, period));
/*     */   }
/*     */ 
/*     */   
/*     */   public ViaCommandSender[] getOnlinePlayers() {
/* 192 */     ViaCommandSender[] array = new ViaCommandSender[Bukkit.getOnlinePlayers().size()];
/* 193 */     int i = 0;
/* 194 */     for (Player player : Bukkit.getOnlinePlayers()) {
/* 195 */       array[i++] = (ViaCommandSender)new BukkitCommandSender((CommandSender)player);
/*     */     }
/* 197 */     return array;
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendMessage(UUID uuid, String message) {
/* 202 */     Player player = Bukkit.getPlayer(uuid);
/* 203 */     if (player != null) {
/* 204 */       player.sendMessage(message);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean kickPlayer(UUID uuid, String message) {
/* 210 */     Player player = Bukkit.getPlayer(uuid);
/* 211 */     if (player != null) {
/* 212 */       player.kickPlayer(message);
/* 213 */       return true;
/*     */     } 
/* 215 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPluginEnabled() {
/* 221 */     return Bukkit.getPluginManager().getPlugin("ViaVersion").isEnabled();
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationProvider getConfigurationProvider() {
/* 226 */     return (ConfigurationProvider)this.conf;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onReload() {
/* 231 */     if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
/* 232 */       getLogger().severe("ViaVersion is already loaded, we're going to kick all the players... because otherwise we'll crash because of ProtocolLib.");
/* 233 */       for (Player player : Bukkit.getOnlinePlayers()) {
/* 234 */         player.kickPlayer(ChatColor.translateAlternateColorCodes('&', this.conf.getReloadDisconnectMsg()));
/*     */       }
/*     */     } else {
/*     */       
/* 238 */       getLogger().severe("ViaVersion is already loaded, this should work fine. If you get any console errors, try rebooting.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonObject getDump() {
/* 244 */     JsonObject platformSpecific = new JsonObject();
/*     */     
/* 246 */     List<PluginInfo> plugins = new ArrayList<>();
/* 247 */     for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
/* 248 */       plugins.add(new PluginInfo(p.isEnabled(), p.getDescription().getName(), p.getDescription().getVersion(), p.getDescription().getMain(), p.getDescription().getAuthors()));
/*     */     }
/* 250 */     platformSpecific.add("plugins", GsonUtil.getGson().toJsonTree(plugins));
/*     */     
/* 252 */     return platformSpecific;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOldClientsAllowed() {
/* 257 */     return !this.protocolSupport;
/*     */   }
/*     */ 
/*     */   
/*     */   public BukkitViaConfig getConf() {
/* 262 */     return this.conf;
/*     */   }
/*     */ 
/*     */   
/*     */   public ViaAPI<Player> getApi() {
/* 267 */     return this.api;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Collection<UnsupportedSoftware> getUnsupportedSoftwareClasses() {
/* 272 */     List<UnsupportedSoftware> list = new ArrayList<>(super.getUnsupportedSoftwareClasses());
/* 273 */     list.add((new UnsupportedServerSoftware.Builder()).name("Yatopia").reason("You are using server software that - outside of possibly breaking ViaVersion - can also cause severe damage to your server's integrity as a whole.")
/* 274 */         .addClassName("org.yatopiamc.yatopia.server.YatopiaConfig")
/* 275 */         .addClassName("net.yatopia.api.event.PlayerAttackEntityEvent")
/* 276 */         .addClassName("yatopiamc.org.yatopia.server.YatopiaConfig")
/* 277 */         .addMethod("org.bukkit.Server", "getLastTickTime").build());
/* 278 */     list.add((new UnsupportedPlugin.Builder()).name("software to mess with message signing").reason("Instead of doing the obvious (or nothing at all), these kinds of plugins completely break chat message handling, usually then also breaking other plugins.")
/* 279 */         .addPlugin("NoEncryption").addPlugin("NoReport")
/* 280 */         .addPlugin("NoChatReports").addPlugin("NoChatReport").build());
/* 281 */     return Collections.unmodifiableList(list);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasPlugin(String name) {
/* 286 */     return (getServer().getPluginManager().getPlugin(name) != null);
/*     */   }
/*     */   
/*     */   public boolean isLateBind() {
/* 290 */     return this.lateBind;
/*     */   }
/*     */   
/*     */   public boolean isProtocolSupport() {
/* 294 */     return this.protocolSupport;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static ViaVersionPlugin getInstance() {
/* 299 */     return instance;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\ViaVersionPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */