/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import net.minecraft.network.INetHandler;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*     */ import net.minecraft.scoreboard.Team;
/*     */ 
/*     */ public class S3EPacketTeams implements Packet<INetHandlerPlayClient> {
/*  14 */   private String name = "";
/*  15 */   private String displayName = "";
/*  16 */   private String prefix = "";
/*  17 */   private String suffix = "";
/*     */   
/*     */   private String nameTagVisibility;
/*     */   private int color;
/*     */   private Collection<String> players;
/*     */   private int action;
/*     */   private int friendlyFlags;
/*     */   
/*     */   public S3EPacketTeams() {
/*  26 */     this.nameTagVisibility = Team.EnumVisible.ALWAYS.internalName;
/*  27 */     this.color = -1;
/*  28 */     this.players = Lists.newArrayList();
/*     */   }
/*     */ 
/*     */   
/*     */   public S3EPacketTeams(ScorePlayerTeam teamIn, int actionIn) {
/*  33 */     this.nameTagVisibility = Team.EnumVisible.ALWAYS.internalName;
/*  34 */     this.color = -1;
/*  35 */     this.players = Lists.newArrayList();
/*  36 */     this.name = teamIn.getRegisteredName();
/*  37 */     this.action = actionIn;
/*     */     
/*  39 */     if (actionIn == 0 || actionIn == 2) {
/*     */       
/*  41 */       this.displayName = teamIn.getTeamName();
/*  42 */       this.prefix = teamIn.getColorPrefix();
/*  43 */       this.suffix = teamIn.getColorSuffix();
/*  44 */       this.friendlyFlags = teamIn.func_98299_i();
/*  45 */       this.nameTagVisibility = (teamIn.getNameTagVisibility()).internalName;
/*  46 */       this.color = teamIn.getChatFormat().getColorIndex();
/*     */     } 
/*     */     
/*  49 */     if (actionIn == 0)
/*     */     {
/*  51 */       this.players.addAll(teamIn.getMembershipCollection());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public S3EPacketTeams(ScorePlayerTeam teamIn, Collection<String> playersIn, int actionIn) {
/*  57 */     this.nameTagVisibility = Team.EnumVisible.ALWAYS.internalName;
/*  58 */     this.color = -1;
/*  59 */     this.players = Lists.newArrayList();
/*     */     
/*  61 */     if (actionIn != 3 && actionIn != 4)
/*     */     {
/*  63 */       throw new IllegalArgumentException("Method must be join or leave for player constructor");
/*     */     }
/*  65 */     if (playersIn != null && !playersIn.isEmpty()) {
/*     */       
/*  67 */       this.action = actionIn;
/*  68 */       this.name = teamIn.getRegisteredName();
/*  69 */       this.players.addAll(playersIn);
/*     */     }
/*     */     else {
/*     */       
/*  73 */       throw new IllegalArgumentException("Players cannot be null/empty");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void readPacketData(PacketBuffer buf) throws IOException {
/*  79 */     this.name = buf.readStringFromBuffer(16);
/*  80 */     this.action = buf.readByte();
/*     */     
/*  82 */     if (this.action == 0 || this.action == 2) {
/*     */       
/*  84 */       this.displayName = buf.readStringFromBuffer(32);
/*  85 */       this.prefix = buf.readStringFromBuffer(16);
/*  86 */       this.suffix = buf.readStringFromBuffer(16);
/*  87 */       this.friendlyFlags = buf.readByte();
/*  88 */       this.nameTagVisibility = buf.readStringFromBuffer(32);
/*  89 */       this.color = buf.readByte();
/*     */     } 
/*     */     
/*  92 */     if (this.action == 0 || this.action == 3 || this.action == 4) {
/*     */       
/*  94 */       int i = buf.readVarIntFromBuffer();
/*     */       
/*  96 */       for (int j = 0; j < i; j++)
/*     */       {
/*  98 */         this.players.add(buf.readStringFromBuffer(40));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 105 */     buf.writeString(this.name);
/* 106 */     buf.writeByte(this.action);
/*     */     
/* 108 */     if (this.action == 0 || this.action == 2) {
/*     */       
/* 110 */       buf.writeString(this.displayName);
/* 111 */       buf.writeString(this.prefix);
/* 112 */       buf.writeString(this.suffix);
/* 113 */       buf.writeByte(this.friendlyFlags);
/* 114 */       buf.writeString(this.nameTagVisibility);
/* 115 */       buf.writeByte(this.color);
/*     */     } 
/*     */     
/* 118 */     if (this.action == 0 || this.action == 3 || this.action == 4) {
/*     */       
/* 120 */       buf.writeVarIntToBuffer(this.players.size());
/*     */       
/* 122 */       for (String s : this.players)
/*     */       {
/* 124 */         buf.writeString(s);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void processPacket(INetHandlerPlayClient handler) {
/* 131 */     handler.handleTeams(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 136 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayName() {
/* 141 */     return this.displayName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPrefix() {
/* 146 */     return this.prefix;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSuffix() {
/* 151 */     return this.suffix;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getPlayers() {
/* 156 */     return this.players;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAction() {
/* 161 */     return this.action;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFriendlyFlags() {
/* 166 */     return this.friendlyFlags;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getColor() {
/* 171 */     return this.color;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNameTagVisibility() {
/* 176 */     return this.nameTagVisibility;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S3EPacketTeams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */