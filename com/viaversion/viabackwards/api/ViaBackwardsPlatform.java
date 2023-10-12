/*     */ package com.viaversion.viabackwards.api;
/*     */ 
/*     */ import com.viaversion.viabackwards.ViaBackwards;
/*     */ import com.viaversion.viabackwards.ViaBackwardsConfig;
/*     */ import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_10to1_11.Protocol1_10To1_11;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.Protocol1_11_1To1_12;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_11to1_11_1.Protocol1_11To1_11_1;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_12_1to1_12_2.Protocol1_12_1To1_12_2;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_12to1_12_1.Protocol1_12To1_12_1;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_13_1to1_13_2.Protocol1_13_1To1_13_2;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.Protocol1_13_2To1_14;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_13to1_13_1.Protocol1_13To1_13_1;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_14_1to1_14_2.Protocol1_14_1To1_14_2;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_14_2to1_14_3.Protocol1_14_2To1_14_3;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_14_3to1_14_4.Protocol1_14_3To1_14_4;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_14_4to1_15.Protocol1_14_4To1_15;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_14to1_14_1.Protocol1_14To1_14_1;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_15_1to1_15_2.Protocol1_15_1To1_15_2;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.Protocol1_15_2To1_16;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_15to1_15_1.Protocol1_15To1_15_1;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_16_1to1_16_2.Protocol1_16_1To1_16_2;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_16_2to1_16_3.Protocol1_16_2To1_16_3;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_16_3to1_16_4.Protocol1_16_3To1_16_4;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_16_4to1_17.Protocol1_16_4To1_17;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_16to1_16_1.Protocol1_16To1_16_1;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_17_1to1_18.Protocol1_17_1To1_18;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_17to1_17_1.Protocol1_17To1_17_1;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.Protocol1_18_2To1_19;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_18to1_18_2.Protocol1_18To1_18_2;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_19_1to1_19_3.Protocol1_19_1To1_19_3;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_19_3to1_19_4.Protocol1_19_3To1_19_4;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_19_4to1_20.Protocol1_19_4To1_20;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_19to1_19_1.Protocol1_19To1_19_1;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_20to1_20_2.Protocol1_20To1_20_2;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_9_1_2to1_9_3_4.Protocol1_9_1_2To1_9_3_4;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_9_4to1_10.Protocol1_9_4To1_10;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_9to1_9_1.Protocol1_9To1_9_1;
/*     */ import com.viaversion.viabackwards.utils.VersionInfo;
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.ProtocolManager;
/*     */ import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
/*     */ import com.viaversion.viaversion.update.Version;
/*     */ import java.io.File;
/*     */ import java.util.Arrays;
/*     */ import java.util.logging.Logger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ViaBackwardsPlatform
/*     */ {
/*     */   public static final String MINIMUM_VV_VERSION = "4.8.1";
/*     */   
/*     */   default void init(File dataFolder) {
/*  75 */     ViaBackwardsConfig config = new ViaBackwardsConfig(new File(dataFolder, "config.yml"));
/*  76 */     config.reloadConfig();
/*     */     
/*  78 */     ViaBackwards.init(this, (ViaBackwardsConfig)config);
/*     */     
/*  80 */     if (isOutdated()) {
/*  81 */       disable();
/*     */       
/*     */       return;
/*     */     } 
/*  85 */     Via.getManager().getSubPlatforms().add(VersionInfo.getImplementationVersion());
/*     */     
/*  87 */     getLogger().info("Loading translations...");
/*  88 */     TranslatableRewriter.loadTranslatables();
/*     */     
/*  90 */     getLogger().info("Registering protocols...");
/*  91 */     ProtocolManager protocolManager = Via.getManager().getProtocolManager();
/*  92 */     protocolManager.registerProtocol((Protocol)new Protocol1_9To1_9_1(), ProtocolVersion.v1_9, ProtocolVersion.v1_9_1);
/*  93 */     protocolManager.registerProtocol((Protocol)new Protocol1_9_1_2To1_9_3_4(), Arrays.asList(new Integer[] { Integer.valueOf(ProtocolVersion.v1_9_1.getVersion()), Integer.valueOf(ProtocolVersion.v1_9_2.getVersion()) }, ), ProtocolVersion.v1_9_3.getVersion());
/*  94 */     protocolManager.registerProtocol((Protocol)new Protocol1_9_4To1_10(), ProtocolVersion.v1_9_3, ProtocolVersion.v1_10);
/*     */     
/*  96 */     protocolManager.registerProtocol((Protocol)new Protocol1_10To1_11(), ProtocolVersion.v1_10, ProtocolVersion.v1_11);
/*  97 */     protocolManager.registerProtocol((Protocol)new Protocol1_11To1_11_1(), ProtocolVersion.v1_11, ProtocolVersion.v1_11_1);
/*     */     
/*  99 */     protocolManager.registerProtocol((Protocol)new Protocol1_11_1To1_12(), ProtocolVersion.v1_11_1, ProtocolVersion.v1_12);
/* 100 */     protocolManager.registerProtocol((Protocol)new Protocol1_12To1_12_1(), ProtocolVersion.v1_12, ProtocolVersion.v1_12_1);
/* 101 */     protocolManager.registerProtocol((Protocol)new Protocol1_12_1To1_12_2(), ProtocolVersion.v1_12_1, ProtocolVersion.v1_12_2);
/*     */     
/* 103 */     protocolManager.registerProtocol((Protocol)new Protocol1_12_2To1_13(), ProtocolVersion.v1_12_2, ProtocolVersion.v1_13);
/* 104 */     protocolManager.registerProtocol((Protocol)new Protocol1_13To1_13_1(), ProtocolVersion.v1_13, ProtocolVersion.v1_13_1);
/* 105 */     protocolManager.registerProtocol((Protocol)new Protocol1_13_1To1_13_2(), ProtocolVersion.v1_13_1, ProtocolVersion.v1_13_2);
/*     */     
/* 107 */     protocolManager.registerProtocol((Protocol)new Protocol1_13_2To1_14(), ProtocolVersion.v1_13_2, ProtocolVersion.v1_14);
/* 108 */     protocolManager.registerProtocol((Protocol)new Protocol1_14To1_14_1(), ProtocolVersion.v1_14, ProtocolVersion.v1_14_1);
/* 109 */     protocolManager.registerProtocol((Protocol)new Protocol1_14_1To1_14_2(), ProtocolVersion.v1_14_1, ProtocolVersion.v1_14_2);
/* 110 */     protocolManager.registerProtocol((Protocol)new Protocol1_14_2To1_14_3(), ProtocolVersion.v1_14_2, ProtocolVersion.v1_14_3);
/* 111 */     protocolManager.registerProtocol((Protocol)new Protocol1_14_3To1_14_4(), ProtocolVersion.v1_14_3, ProtocolVersion.v1_14_4);
/*     */     
/* 113 */     protocolManager.registerProtocol((Protocol)new Protocol1_14_4To1_15(), ProtocolVersion.v1_14_4, ProtocolVersion.v1_15);
/* 114 */     protocolManager.registerProtocol((Protocol)new Protocol1_15To1_15_1(), ProtocolVersion.v1_15, ProtocolVersion.v1_15_1);
/* 115 */     protocolManager.registerProtocol((Protocol)new Protocol1_15_1To1_15_2(), ProtocolVersion.v1_15_1, ProtocolVersion.v1_15_2);
/*     */     
/* 117 */     protocolManager.registerProtocol((Protocol)new Protocol1_15_2To1_16(), ProtocolVersion.v1_15_2, ProtocolVersion.v1_16);
/* 118 */     protocolManager.registerProtocol((Protocol)new Protocol1_16To1_16_1(), ProtocolVersion.v1_16, ProtocolVersion.v1_16_1);
/* 119 */     protocolManager.registerProtocol((Protocol)new Protocol1_16_1To1_16_2(), ProtocolVersion.v1_16_1, ProtocolVersion.v1_16_2);
/* 120 */     protocolManager.registerProtocol((Protocol)new Protocol1_16_2To1_16_3(), ProtocolVersion.v1_16_2, ProtocolVersion.v1_16_3);
/* 121 */     protocolManager.registerProtocol((Protocol)new Protocol1_16_3To1_16_4(), ProtocolVersion.v1_16_3, ProtocolVersion.v1_16_4);
/*     */     
/* 123 */     protocolManager.registerProtocol((Protocol)new Protocol1_16_4To1_17(), ProtocolVersion.v1_16_4, ProtocolVersion.v1_17);
/* 124 */     protocolManager.registerProtocol((Protocol)new Protocol1_17To1_17_1(), ProtocolVersion.v1_17, ProtocolVersion.v1_17_1);
/*     */     
/* 126 */     protocolManager.registerProtocol((Protocol)new Protocol1_17_1To1_18(), ProtocolVersion.v1_17_1, ProtocolVersion.v1_18);
/* 127 */     protocolManager.registerProtocol((Protocol)new Protocol1_18To1_18_2(), ProtocolVersion.v1_18, ProtocolVersion.v1_18_2);
/*     */     
/* 129 */     protocolManager.registerProtocol((Protocol)new Protocol1_18_2To1_19(), ProtocolVersion.v1_18_2, ProtocolVersion.v1_19);
/* 130 */     protocolManager.registerProtocol((Protocol)new Protocol1_19To1_19_1(), ProtocolVersion.v1_19, ProtocolVersion.v1_19_1);
/* 131 */     protocolManager.registerProtocol((Protocol)new Protocol1_19_1To1_19_3(), ProtocolVersion.v1_19_1, ProtocolVersion.v1_19_3);
/* 132 */     protocolManager.registerProtocol((Protocol)new Protocol1_19_3To1_19_4(), ProtocolVersion.v1_19_3, ProtocolVersion.v1_19_4);
/*     */     
/* 134 */     protocolManager.registerProtocol((Protocol)new Protocol1_19_4To1_20(), ProtocolVersion.v1_19_4, ProtocolVersion.v1_20);
/* 135 */     protocolManager.registerProtocol((Protocol)new Protocol1_20To1_20_2(), ProtocolVersion.v1_20, ProtocolVersion.v1_20_2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Logger getLogger();
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean isOutdated() {
/* 146 */     String vvVersion = Via.getPlatform().getPluginVersion();
/* 147 */     if (vvVersion != null && (new Version(vvVersion)).compareTo(new Version("4.8.1--")) < 0) {
/* 148 */       getLogger().severe("================================");
/* 149 */       getLogger().severe("YOUR VIAVERSION IS OUTDATED");
/* 150 */       getLogger().severe("PLEASE USE VIAVERSION 4.8.1 OR NEWER");
/* 151 */       getLogger().severe("LINK: https://ci.viaversion.com/");
/* 152 */       getLogger().severe("VIABACKWARDS WILL NOW DISABLE");
/* 153 */       getLogger().severe("================================");
/* 154 */       return true;
/*     */     } 
/* 156 */     return false;
/*     */   }
/*     */   
/*     */   void disable();
/*     */   
/*     */   File getDataFolder();
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\api\ViaBackwardsPlatform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */