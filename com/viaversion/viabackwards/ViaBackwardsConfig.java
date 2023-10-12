/*    */ package com.viaversion.viabackwards;
/*    */ 
/*    */ import com.viaversion.viabackwards.api.ViaBackwardsConfig;
/*    */ import com.viaversion.viaversion.util.Config;
/*    */ import java.io.File;
/*    */ import java.net.URL;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ViaBackwardsConfig
/*    */   extends Config
/*    */   implements ViaBackwardsConfig
/*    */ {
/*    */   private boolean addCustomEnchantsToLore;
/*    */   private boolean addTeamColorToPrefix;
/*    */   private boolean fix1_13FacePlayer;
/*    */   private boolean alwaysShowOriginalMobName;
/*    */   private boolean fix1_13FormattedInventoryTitles;
/*    */   private boolean handlePingsAsInvAcknowledgements;
/*    */   
/*    */   public ViaBackwardsConfig(File configFile) {
/* 37 */     super(configFile);
/*    */   }
/*    */ 
/*    */   
/*    */   public void reloadConfig() {
/* 42 */     super.reloadConfig();
/* 43 */     loadFields();
/*    */   }
/*    */   
/*    */   private void loadFields() {
/* 47 */     this.addCustomEnchantsToLore = getBoolean("add-custom-enchants-into-lore", true);
/* 48 */     this.addTeamColorToPrefix = getBoolean("add-teamcolor-to-prefix", true);
/* 49 */     this.fix1_13FacePlayer = getBoolean("fix-1_13-face-player", false);
/* 50 */     this.fix1_13FormattedInventoryTitles = getBoolean("fix-formatted-inventory-titles", true);
/* 51 */     this.alwaysShowOriginalMobName = getBoolean("always-show-original-mob-name", true);
/* 52 */     this.handlePingsAsInvAcknowledgements = getBoolean("handle-pings-as-inv-acknowledgements", false);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean addCustomEnchantsToLore() {
/* 57 */     return this.addCustomEnchantsToLore;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean addTeamColorTo1_13Prefix() {
/* 62 */     return this.addTeamColorToPrefix;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFix1_13FacePlayer() {
/* 67 */     return this.fix1_13FacePlayer;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean fix1_13FormattedInventoryTitle() {
/* 72 */     return this.fix1_13FormattedInventoryTitles;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean alwaysShowOriginalMobName() {
/* 77 */     return this.alwaysShowOriginalMobName;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean handlePingsAsInvAcknowledgements() {
/* 82 */     return (this.handlePingsAsInvAcknowledgements || Boolean.getBoolean("com.viaversion.handlePingsAsInvAcknowledgements"));
/*    */   }
/*    */ 
/*    */   
/*    */   public URL getDefaultConfigURL() {
/* 87 */     return getClass().getClassLoader().getResource("assets/viabackwards/config.yml");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void handleConfig(Map<String, Object> map) {}
/*    */ 
/*    */   
/*    */   public List<String> getUnsupportedOptions() {
/* 96 */     return Collections.emptyList();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\ViaBackwardsConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */