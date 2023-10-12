/*     */ package com.viaversion.viaversion.bukkit.platform;
/*     */ 
/*     */ import com.viaversion.viaversion.ViaVersionPlugin;
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.api.minecraft.item.Item;
/*     */ import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
/*     */ import com.viaversion.viaversion.api.platform.providers.Provider;
/*     */ import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
/*     */ import com.viaversion.viaversion.bukkit.compat.ProtocolSupportCompat;
/*     */ import com.viaversion.viaversion.bukkit.listeners.UpdateListener;
/*     */ import com.viaversion.viaversion.bukkit.listeners.multiversion.PlayerSneakListener;
/*     */ import com.viaversion.viaversion.bukkit.listeners.protocol1_15to1_14_4.EntityToggleGlideListener;
/*     */ import com.viaversion.viaversion.bukkit.listeners.protocol1_19_4To1_19_3.ArmorToggleListener;
/*     */ import com.viaversion.viaversion.bukkit.listeners.protocol1_19to1_18_2.BlockBreakListener;
/*     */ import com.viaversion.viaversion.bukkit.listeners.protocol1_9to1_8.ArmorListener;
/*     */ import com.viaversion.viaversion.bukkit.listeners.protocol1_9to1_8.BlockListener;
/*     */ import com.viaversion.viaversion.bukkit.listeners.protocol1_9to1_8.DeathListener;
/*     */ import com.viaversion.viaversion.bukkit.listeners.protocol1_9to1_8.HandItemCache;
/*     */ import com.viaversion.viaversion.bukkit.listeners.protocol1_9to1_8.PaperPatch;
/*     */ import com.viaversion.viaversion.bukkit.providers.BukkitAckSequenceProvider;
/*     */ import com.viaversion.viaversion.bukkit.providers.BukkitBlockConnectionProvider;
/*     */ import com.viaversion.viaversion.bukkit.providers.BukkitInventoryQuickMoveProvider;
/*     */ import com.viaversion.viaversion.bukkit.providers.BukkitViaMovementTransmitter;
/*     */ import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.providers.InventoryQuickMoveProvider;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.ConnectionData;
/*     */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.providers.BlockConnectionProvider;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.provider.AckSequenceProvider;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.HandItemProvider;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.scheduler.BukkitTask;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BukkitViaLoader
/*     */   implements ViaPlatformLoader
/*     */ {
/*  61 */   private final Set<BukkitTask> tasks = new HashSet<>();
/*     */   private final ViaVersionPlugin plugin;
/*     */   private HandItemCache handItemCache;
/*     */   
/*     */   public BukkitViaLoader(ViaVersionPlugin plugin) {
/*  66 */     this.plugin = plugin;
/*     */   }
/*     */   
/*     */   public void registerListener(Listener listener) {
/*  70 */     this.plugin.getServer().getPluginManager().registerEvents(listener, (Plugin)this.plugin);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public <T extends Listener> T storeListener(T listener) {
/*  75 */     return listener;
/*     */   }
/*     */ 
/*     */   
/*     */   public void load() {
/*  80 */     registerListener((Listener)new UpdateListener());
/*     */ 
/*     */     
/*  83 */     ViaVersionPlugin plugin = (ViaVersionPlugin)Bukkit.getPluginManager().getPlugin("ViaVersion");
/*     */ 
/*     */     
/*  86 */     if (plugin.isProtocolSupport() && ProtocolSupportCompat.isMultiplatformPS()) {
/*  87 */       ProtocolSupportCompat.registerPSConnectListener(plugin);
/*     */     }
/*     */     
/*  90 */     if (!Via.getAPI().getServerVersion().isKnown()) {
/*  91 */       Via.getPlatform().getLogger().severe("Server version has not been loaded yet, cannot register additional listeners");
/*     */       
/*     */       return;
/*     */     } 
/*  95 */     int serverProtocolVersion = Via.getAPI().getServerVersion().lowestSupportedVersion();
/*     */ 
/*     */     
/*  98 */     if (serverProtocolVersion < ProtocolVersion.v1_9.getVersion()) {
/*  99 */       (new ArmorListener((Plugin)plugin)).register();
/* 100 */       (new DeathListener((Plugin)plugin)).register();
/* 101 */       (new BlockListener((Plugin)plugin)).register();
/*     */       
/* 103 */       if (plugin.getConf().isItemCache()) {
/* 104 */         this.handItemCache = new HandItemCache();
/* 105 */         this.tasks.add(this.handItemCache.runTaskTimerAsynchronously((Plugin)plugin, 1L, 1L));
/*     */       } 
/*     */     } 
/*     */     
/* 109 */     if (serverProtocolVersion < ProtocolVersion.v1_14.getVersion()) {
/* 110 */       boolean use1_9Fix = (plugin.getConf().is1_9HitboxFix() && serverProtocolVersion < ProtocolVersion.v1_9.getVersion());
/* 111 */       if (use1_9Fix || plugin.getConf().is1_14HitboxFix()) {
/*     */         try {
/* 113 */           (new PlayerSneakListener(plugin, use1_9Fix, plugin.getConf().is1_14HitboxFix())).register();
/* 114 */         } catch (ReflectiveOperationException e) {
/* 115 */           Via.getPlatform().getLogger().warning("Could not load hitbox fix - please report this on our GitHub");
/* 116 */           e.printStackTrace();
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 121 */     if (serverProtocolVersion < ProtocolVersion.v1_15.getVersion()) {
/*     */       try {
/* 123 */         Class.forName("org.bukkit.event.entity.EntityToggleGlideEvent");
/* 124 */         (new EntityToggleGlideListener(plugin)).register();
/* 125 */       } catch (ClassNotFoundException classNotFoundException) {}
/*     */     }
/*     */ 
/*     */     
/* 129 */     if (serverProtocolVersion < ProtocolVersion.v1_12.getVersion() && !Boolean.getBoolean("com.viaversion.ignorePaperBlockPlacePatch")) {
/* 130 */       boolean paper = true;
/*     */       try {
/* 132 */         Class.forName("org.github.paperspigot.PaperSpigotConfig");
/* 133 */       } catch (ClassNotFoundException ignored) {
/*     */         try {
/* 135 */           Class.forName("com.destroystokyo.paper.PaperConfig");
/* 136 */         } catch (ClassNotFoundException alsoIgnored) {
/* 137 */           paper = false;
/*     */         } 
/*     */       } 
/* 140 */       if (paper) {
/* 141 */         (new PaperPatch((Plugin)plugin)).register();
/*     */       }
/*     */     } 
/*     */     
/* 145 */     if (serverProtocolVersion < ProtocolVersion.v1_19_4.getVersion() && plugin.getConf().isArmorToggleFix() && hasGetHandMethod()) {
/* 146 */       (new ArmorToggleListener(plugin)).register();
/*     */     }
/*     */ 
/*     */     
/* 150 */     if (serverProtocolVersion < ProtocolVersion.v1_9.getVersion()) {
/* 151 */       Via.getManager().getProviders().use(MovementTransmitterProvider.class, (Provider)new BukkitViaMovementTransmitter());
/*     */       
/* 153 */       Via.getManager().getProviders().use(HandItemProvider.class, (Provider)new HandItemProvider()
/*     */           {
/*     */             public Item getHandItem(UserConnection info) {
/* 156 */               if (BukkitViaLoader.this.handItemCache != null) {
/* 157 */                 return BukkitViaLoader.this.handItemCache.getHandItem(info.getProtocolInfo().getUuid());
/*     */               }
/*     */               try {
/* 160 */                 return Bukkit.getScheduler().callSyncMethod(Bukkit.getPluginManager().getPlugin("ViaVersion"), () -> {
/*     */                       UUID playerUUID = info.getProtocolInfo().getUuid();
/*     */ 
/*     */                       
/*     */                       Player player = Bukkit.getPlayer(playerUUID);
/*     */                       
/*     */                       return (player != null) ? HandItemCache.convert(player.getItemInHand()) : null;
/* 167 */                     }).get(10L, TimeUnit.SECONDS);
/* 168 */               } catch (Exception e) {
/* 169 */                 Via.getPlatform().getLogger().severe("Error fetching hand item: " + e.getClass().getName());
/* 170 */                 if (Via.getManager().isDebug())
/* 171 */                   e.printStackTrace(); 
/* 172 */                 return null;
/*     */               } 
/*     */             }
/*     */           });
/*     */     } 
/*     */     
/* 178 */     if (serverProtocolVersion < ProtocolVersion.v1_12.getVersion() && 
/* 179 */       plugin.getConf().is1_12QuickMoveActionFix()) {
/* 180 */       Via.getManager().getProviders().use(InventoryQuickMoveProvider.class, (Provider)new BukkitInventoryQuickMoveProvider());
/*     */     }
/*     */     
/* 183 */     if (serverProtocolVersion < ProtocolVersion.v1_13.getVersion() && 
/* 184 */       Via.getConfig().getBlockConnectionMethod().equalsIgnoreCase("world")) {
/* 185 */       BukkitBlockConnectionProvider blockConnectionProvider = new BukkitBlockConnectionProvider();
/* 186 */       Via.getManager().getProviders().use(BlockConnectionProvider.class, (Provider)blockConnectionProvider);
/* 187 */       ConnectionData.blockConnectionProvider = (BlockConnectionProvider)blockConnectionProvider;
/*     */     } 
/*     */     
/* 190 */     if (serverProtocolVersion < ProtocolVersion.v1_19.getVersion()) {
/* 191 */       Via.getManager().getProviders().use(AckSequenceProvider.class, (Provider)new BukkitAckSequenceProvider(plugin));
/* 192 */       (new BlockBreakListener(plugin)).register();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean hasGetHandMethod() {
/*     */     try {
/* 198 */       PlayerInteractEvent.class.getDeclaredMethod("getHand", new Class[0]);
/* 199 */       Material.class.getMethod("getEquipmentSlot", new Class[0]);
/* 200 */       return true;
/* 201 */     } catch (NoSuchMethodException e) {
/* 202 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void unload() {
/* 208 */     for (BukkitTask task : this.tasks) {
/* 209 */       task.cancel();
/*     */     }
/* 211 */     this.tasks.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\bukkit\platform\BukkitViaLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */