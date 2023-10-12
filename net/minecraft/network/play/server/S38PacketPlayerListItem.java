/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.INetHandler;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.world.WorldSettings;
/*     */ 
/*     */ public class S38PacketPlayerListItem implements Packet<INetHandlerPlayClient> {
/*     */   private Action action;
/*  19 */   private final List<AddPlayerData> players = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public S38PacketPlayerListItem(Action actionIn, EntityPlayerMP... players) {
/*  27 */     this.action = actionIn;
/*     */     
/*  29 */     for (EntityPlayerMP entityplayermp : players)
/*     */     {
/*  31 */       this.players.add(new AddPlayerData(entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.theItemInWorldManager.getGameType(), entityplayermp.getTabListDisplayName()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public S38PacketPlayerListItem(Action actionIn, Iterable<EntityPlayerMP> players) {
/*  37 */     this.action = actionIn;
/*     */     
/*  39 */     for (EntityPlayerMP entityplayermp : players)
/*     */     {
/*  41 */       this.players.add(new AddPlayerData(entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.theItemInWorldManager.getGameType(), entityplayermp.getTabListDisplayName()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void readPacketData(PacketBuffer buf) throws IOException {
/*  47 */     this.action = (Action)buf.readEnumValue(Action.class);
/*  48 */     int i = buf.readVarIntFromBuffer();
/*     */     
/*  50 */     for (int j = 0; j < i; j++) {
/*     */       int l, i1;
/*  52 */       GameProfile gameprofile = null;
/*  53 */       int k = 0;
/*  54 */       WorldSettings.GameType worldsettings$gametype = null;
/*  55 */       IChatComponent ichatcomponent = null;
/*     */       
/*  57 */       switch (this.action) {
/*     */         
/*     */         case ADD_PLAYER:
/*  60 */           gameprofile = new GameProfile(buf.readUuid(), buf.readStringFromBuffer(16));
/*  61 */           l = buf.readVarIntFromBuffer();
/*  62 */           i1 = 0;
/*     */           
/*  64 */           for (; i1 < l; i1++) {
/*     */             
/*  66 */             String s = buf.readStringFromBuffer(32767);
/*  67 */             String s1 = buf.readStringFromBuffer(32767);
/*     */             
/*  69 */             if (buf.readBoolean()) {
/*     */               
/*  71 */               gameprofile.getProperties().put(s, new Property(s, s1, buf.readStringFromBuffer(32767)));
/*     */             }
/*     */             else {
/*     */               
/*  75 */               gameprofile.getProperties().put(s, new Property(s, s1));
/*     */             } 
/*     */           } 
/*     */           
/*  79 */           worldsettings$gametype = WorldSettings.GameType.getByID(buf.readVarIntFromBuffer());
/*  80 */           k = buf.readVarIntFromBuffer();
/*     */           
/*  82 */           if (buf.readBoolean())
/*     */           {
/*  84 */             ichatcomponent = buf.readChatComponent();
/*     */           }
/*     */           break;
/*     */ 
/*     */         
/*     */         case UPDATE_GAME_MODE:
/*  90 */           gameprofile = new GameProfile(buf.readUuid(), (String)null);
/*  91 */           worldsettings$gametype = WorldSettings.GameType.getByID(buf.readVarIntFromBuffer());
/*     */           break;
/*     */         
/*     */         case UPDATE_LATENCY:
/*  95 */           gameprofile = new GameProfile(buf.readUuid(), (String)null);
/*  96 */           k = buf.readVarIntFromBuffer();
/*     */           break;
/*     */         
/*     */         case UPDATE_DISPLAY_NAME:
/* 100 */           gameprofile = new GameProfile(buf.readUuid(), (String)null);
/*     */           
/* 102 */           if (buf.readBoolean())
/*     */           {
/* 104 */             ichatcomponent = buf.readChatComponent();
/*     */           }
/*     */           break;
/*     */ 
/*     */         
/*     */         case REMOVE_PLAYER:
/* 110 */           gameprofile = new GameProfile(buf.readUuid(), (String)null);
/*     */           break;
/*     */       } 
/* 113 */       this.players.add(new AddPlayerData(gameprofile, k, worldsettings$gametype, ichatcomponent));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 119 */     buf.writeEnumValue(this.action);
/* 120 */     buf.writeVarIntToBuffer(this.players.size());
/*     */     
/* 122 */     for (AddPlayerData s38packetplayerlistitem$addplayerdata : this.players) {
/*     */       
/* 124 */       switch (this.action) {
/*     */         
/*     */         case ADD_PLAYER:
/* 127 */           buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
/* 128 */           buf.writeString(s38packetplayerlistitem$addplayerdata.getProfile().getName());
/* 129 */           buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getProfile().getProperties().size());
/*     */           
/* 131 */           for (Property property : s38packetplayerlistitem$addplayerdata.getProfile().getProperties().values()) {
/*     */             
/* 133 */             buf.writeString(property.getName());
/* 134 */             buf.writeString(property.getValue());
/*     */             
/* 136 */             if (property.hasSignature()) {
/*     */               
/* 138 */               buf.writeBoolean(true);
/* 139 */               buf.writeString(property.getSignature());
/*     */               
/*     */               continue;
/*     */             } 
/* 143 */             buf.writeBoolean(false);
/*     */           } 
/*     */ 
/*     */           
/* 147 */           buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getGameMode().getID());
/* 148 */           buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getPing());
/*     */           
/* 150 */           if (s38packetplayerlistitem$addplayerdata.getDisplayName() == null) {
/*     */             
/* 152 */             buf.writeBoolean(false);
/*     */             
/*     */             continue;
/*     */           } 
/* 156 */           buf.writeBoolean(true);
/* 157 */           buf.writeChatComponent(s38packetplayerlistitem$addplayerdata.getDisplayName());
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case UPDATE_GAME_MODE:
/* 163 */           buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
/* 164 */           buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getGameMode().getID());
/*     */ 
/*     */         
/*     */         case UPDATE_LATENCY:
/* 168 */           buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
/* 169 */           buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getPing());
/*     */ 
/*     */         
/*     */         case UPDATE_DISPLAY_NAME:
/* 173 */           buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
/*     */           
/* 175 */           if (s38packetplayerlistitem$addplayerdata.getDisplayName() == null) {
/*     */             
/* 177 */             buf.writeBoolean(false);
/*     */             
/*     */             continue;
/*     */           } 
/* 181 */           buf.writeBoolean(true);
/* 182 */           buf.writeChatComponent(s38packetplayerlistitem$addplayerdata.getDisplayName());
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case REMOVE_PLAYER:
/* 188 */           buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void processPacket(INetHandlerPlayClient handler) {
/* 195 */     handler.handlePlayerListItem(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<AddPlayerData> getEntries() {
/* 200 */     return this.players;
/*     */   }
/*     */ 
/*     */   
/*     */   public Action getAction() {
/* 205 */     return this.action;
/*     */   }
/*     */   public S38PacketPlayerListItem() {}
/*     */   
/*     */   public String toString() {
/* 210 */     return Objects.toStringHelper(this).add("action", this.action).add("entries", this.players).toString();
/*     */   }
/*     */   
/*     */   public enum Action
/*     */   {
/* 215 */     ADD_PLAYER,
/* 216 */     UPDATE_GAME_MODE,
/* 217 */     UPDATE_LATENCY,
/* 218 */     UPDATE_DISPLAY_NAME,
/* 219 */     REMOVE_PLAYER;
/*     */   }
/*     */ 
/*     */   
/*     */   public class AddPlayerData
/*     */   {
/*     */     private final int ping;
/*     */     private final WorldSettings.GameType gamemode;
/*     */     private final GameProfile profile;
/*     */     private final IChatComponent displayName;
/*     */     
/*     */     public AddPlayerData(GameProfile profile, int pingIn, WorldSettings.GameType gamemodeIn, IChatComponent displayNameIn) {
/* 231 */       this.profile = profile;
/* 232 */       this.ping = pingIn;
/* 233 */       this.gamemode = gamemodeIn;
/* 234 */       this.displayName = displayNameIn;
/*     */     }
/*     */ 
/*     */     
/*     */     public GameProfile getProfile() {
/* 239 */       return this.profile;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getPing() {
/* 244 */       return this.ping;
/*     */     }
/*     */ 
/*     */     
/*     */     public WorldSettings.GameType getGameMode() {
/* 249 */       return this.gamemode;
/*     */     }
/*     */ 
/*     */     
/*     */     public IChatComponent getDisplayName() {
/* 254 */       return this.displayName;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 259 */       return Objects.toStringHelper(this).add("latency", this.ping).add("gameMode", this.gamemode).add("profile", this.profile).add("displayName", (this.displayName == null) ? null : IChatComponent.Serializer.componentToJson(this.displayName)).toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S38PacketPlayerListItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */