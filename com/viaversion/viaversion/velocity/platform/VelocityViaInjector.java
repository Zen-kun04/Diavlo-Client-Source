/*     */ package com.viaversion.viaversion.velocity.platform;
/*     */ 
/*     */ import com.velocitypowered.api.network.ProtocolVersion;
/*     */ import com.viaversion.viaversion.VelocityPlugin;
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.platform.ViaInjector;
/*     */ import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.IntLinkedOpenHashSet;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.IntSortedSet;
/*     */ import com.viaversion.viaversion.libs.gson.JsonObject;
/*     */ import com.viaversion.viaversion.util.ReflectionUtil;
/*     */ import com.viaversion.viaversion.velocity.handlers.VelocityChannelInitializer;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import java.lang.reflect.Method;
/*     */ import org.jetbrains.annotations.Nullable;
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
/*     */ 
/*     */ public class VelocityViaInjector
/*     */   implements ViaInjector
/*     */ {
/*  36 */   public static final Method GET_PLAYER_INFO_FORWARDING_MODE = getPlayerInfoForwardingModeMethod();
/*     */   @Nullable
/*     */   private static Method getPlayerInfoForwardingModeMethod() {
/*     */     try {
/*  40 */       return Class.forName("com.velocitypowered.proxy.config.VelocityConfiguration").getMethod("getPlayerInfoForwardingMode", new Class[0]);
/*  41 */     } catch (NoSuchMethodException|ClassNotFoundException e) {
/*  42 */       e.printStackTrace();
/*  43 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private ChannelInitializer getInitializer() throws Exception {
/*  48 */     Object connectionManager = ReflectionUtil.get(VelocityPlugin.PROXY, "cm", Object.class);
/*  49 */     Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getServerChannelInitializer");
/*  50 */     return (ChannelInitializer)ReflectionUtil.invoke(channelInitializerHolder, "get");
/*     */   }
/*     */   
/*     */   private ChannelInitializer getBackendInitializer() throws Exception {
/*  54 */     Object connectionManager = ReflectionUtil.get(VelocityPlugin.PROXY, "cm", Object.class);
/*  55 */     Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getBackendChannelInitializer");
/*  56 */     return (ChannelInitializer)ReflectionUtil.invoke(channelInitializerHolder, "get");
/*     */   }
/*     */ 
/*     */   
/*     */   public void inject() throws Exception {
/*  61 */     Via.getPlatform().getLogger().info("Replacing channel initializers; you can safely ignore the following two warnings.");
/*     */     
/*  63 */     Object connectionManager = ReflectionUtil.get(VelocityPlugin.PROXY, "cm", Object.class);
/*  64 */     Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getServerChannelInitializer");
/*  65 */     ChannelInitializer originalInitializer = getInitializer();
/*  66 */     channelInitializerHolder.getClass().getMethod("set", new Class[] { ChannelInitializer.class
/*  67 */         }).invoke(channelInitializerHolder, new Object[] { new VelocityChannelInitializer(originalInitializer, false) });
/*     */     
/*  69 */     Object backendInitializerHolder = ReflectionUtil.invoke(connectionManager, "getBackendChannelInitializer");
/*  70 */     ChannelInitializer backendInitializer = getBackendInitializer();
/*  71 */     backendInitializerHolder.getClass().getMethod("set", new Class[] { ChannelInitializer.class
/*  72 */         }).invoke(backendInitializerHolder, new Object[] { new VelocityChannelInitializer(backendInitializer, true) });
/*     */   }
/*     */ 
/*     */   
/*     */   public void uninject() {
/*  77 */     Via.getPlatform().getLogger().severe("ViaVersion cannot remove itself from Velocity without a reboot!");
/*     */   }
/*     */ 
/*     */   
/*     */   public int getServerProtocolVersion() throws Exception {
/*  82 */     return getLowestSupportedProtocolVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public IntSortedSet getServerProtocolVersions() throws Exception {
/*  87 */     int lowestSupportedProtocolVersion = getLowestSupportedProtocolVersion();
/*     */     
/*  89 */     IntLinkedOpenHashSet intLinkedOpenHashSet = new IntLinkedOpenHashSet();
/*  90 */     for (ProtocolVersion version : ProtocolVersion.SUPPORTED_VERSIONS) {
/*  91 */       if (version.getProtocol() >= lowestSupportedProtocolVersion) {
/*  92 */         intLinkedOpenHashSet.add(version.getProtocol());
/*     */       }
/*     */     } 
/*  95 */     return (IntSortedSet)intLinkedOpenHashSet;
/*     */   }
/*     */   
/*     */   public static int getLowestSupportedProtocolVersion() {
/*     */     try {
/* 100 */       if (GET_PLAYER_INFO_FORWARDING_MODE != null && ((Enum)GET_PLAYER_INFO_FORWARDING_MODE
/* 101 */         .invoke(VelocityPlugin.PROXY.getConfiguration(), new Object[0]))
/* 102 */         .name().equals("MODERN")) {
/* 103 */         return ProtocolVersion.v1_13.getVersion();
/*     */       }
/* 105 */     } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException illegalAccessException) {}
/*     */     
/* 107 */     return ProtocolVersion.MINIMUM_VERSION.getProtocol();
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonObject getDump() {
/* 112 */     JsonObject data = new JsonObject();
/*     */     try {
/* 114 */       data.addProperty("currentInitializer", getInitializer().getClass().getName());
/* 115 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/* 118 */     return data;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\velocity\platform\VelocityViaInjector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */