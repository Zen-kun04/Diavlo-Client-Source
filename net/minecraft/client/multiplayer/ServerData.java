/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ 
/*     */ public class ServerData
/*     */ {
/*     */   public String serverName;
/*     */   public String serverIP;
/*     */   public String populationInfo;
/*     */   public String serverMOTD;
/*     */   public long pingToServer;
/*  14 */   public int version = 47;
/*  15 */   public String gameVersion = "1.8.9";
/*     */   public boolean field_78841_f;
/*     */   public String playerList;
/*  18 */   private ServerResourceMode resourceMode = ServerResourceMode.PROMPT;
/*     */   
/*     */   private String serverIcon;
/*     */   private boolean lanServer;
/*     */   
/*     */   public ServerData(String name, String ip, boolean isLan) {
/*  24 */     this.serverName = name;
/*  25 */     this.serverIP = ip;
/*  26 */     this.lanServer = isLan;
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound getNBTCompound() {
/*  31 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  32 */     nbttagcompound.setString("name", this.serverName);
/*  33 */     nbttagcompound.setString("ip", this.serverIP);
/*     */     
/*  35 */     if (this.serverIcon != null)
/*     */     {
/*  37 */       nbttagcompound.setString("icon", this.serverIcon);
/*     */     }
/*     */     
/*  40 */     if (this.resourceMode == ServerResourceMode.ENABLED) {
/*     */       
/*  42 */       nbttagcompound.setBoolean("acceptTextures", true);
/*     */     }
/*  44 */     else if (this.resourceMode == ServerResourceMode.DISABLED) {
/*     */       
/*  46 */       nbttagcompound.setBoolean("acceptTextures", false);
/*     */     } 
/*     */     
/*  49 */     return nbttagcompound;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerResourceMode getResourceMode() {
/*  54 */     return this.resourceMode;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setResourceMode(ServerResourceMode mode) {
/*  59 */     this.resourceMode = mode;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ServerData getServerDataFromNBTCompound(NBTTagCompound nbtCompound) {
/*  64 */     ServerData serverdata = new ServerData(nbtCompound.getString("name"), nbtCompound.getString("ip"), false);
/*     */     
/*  66 */     if (nbtCompound.hasKey("icon", 8))
/*     */     {
/*  68 */       serverdata.setBase64EncodedIconData(nbtCompound.getString("icon"));
/*     */     }
/*     */     
/*  71 */     if (nbtCompound.hasKey("acceptTextures", 1)) {
/*     */       
/*  73 */       if (nbtCompound.getBoolean("acceptTextures"))
/*     */       {
/*  75 */         serverdata.setResourceMode(ServerResourceMode.ENABLED);
/*     */       }
/*     */       else
/*     */       {
/*  79 */         serverdata.setResourceMode(ServerResourceMode.DISABLED);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/*  84 */       serverdata.setResourceMode(ServerResourceMode.PROMPT);
/*     */     } 
/*     */     
/*  87 */     return serverdata;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBase64EncodedIconData() {
/*  92 */     return this.serverIcon;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBase64EncodedIconData(String icon) {
/*  97 */     this.serverIcon = icon;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnLAN() {
/* 102 */     return this.lanServer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void copyFrom(ServerData serverDataIn) {
/* 107 */     this.serverIP = serverDataIn.serverIP;
/* 108 */     this.serverName = serverDataIn.serverName;
/* 109 */     setResourceMode(serverDataIn.getResourceMode());
/* 110 */     this.serverIcon = serverDataIn.serverIcon;
/* 111 */     this.lanServer = serverDataIn.lanServer;
/*     */   }
/*     */   
/*     */   public enum ServerResourceMode
/*     */   {
/* 116 */     ENABLED("enabled"),
/* 117 */     DISABLED("disabled"),
/* 118 */     PROMPT("prompt");
/*     */     
/*     */     private final IChatComponent motd;
/*     */ 
/*     */     
/*     */     ServerResourceMode(String name) {
/* 124 */       this.motd = (IChatComponent)new ChatComponentTranslation("addServer.resourcePack." + name, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     public IChatComponent getMotd() {
/* 129 */       return this.motd;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\multiplayer\ServerData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */