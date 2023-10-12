/*     */ package net.minecraft.scoreboard;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
/*     */ import net.minecraft.network.play.server.S3CPacketUpdateScore;
/*     */ import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
/*     */ import net.minecraft.network.play.server.S3EPacketTeams;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ 
/*     */ public class ServerScoreboard
/*     */   extends Scoreboard {
/*     */   private final MinecraftServer scoreboardMCServer;
/*  19 */   private final Set<ScoreObjective> field_96553_b = Sets.newHashSet();
/*     */   
/*     */   private ScoreboardSaveData scoreboardSaveData;
/*     */   
/*     */   public ServerScoreboard(MinecraftServer mcServer) {
/*  24 */     this.scoreboardMCServer = mcServer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_96536_a(Score p_96536_1_) {
/*  29 */     super.func_96536_a(p_96536_1_);
/*     */     
/*  31 */     if (this.field_96553_b.contains(p_96536_1_.getObjective()))
/*     */     {
/*  33 */       this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers((Packet)new S3CPacketUpdateScore(p_96536_1_));
/*     */     }
/*     */     
/*  36 */     markSaveDataDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_96516_a(String p_96516_1_) {
/*  41 */     super.func_96516_a(p_96516_1_);
/*  42 */     this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers((Packet)new S3CPacketUpdateScore(p_96516_1_));
/*  43 */     markSaveDataDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_178820_a(String p_178820_1_, ScoreObjective p_178820_2_) {
/*  48 */     super.func_178820_a(p_178820_1_, p_178820_2_);
/*  49 */     this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers((Packet)new S3CPacketUpdateScore(p_178820_1_, p_178820_2_));
/*  50 */     markSaveDataDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setObjectiveInDisplaySlot(int p_96530_1_, ScoreObjective p_96530_2_) {
/*  55 */     ScoreObjective scoreobjective = getObjectiveInDisplaySlot(p_96530_1_);
/*  56 */     super.setObjectiveInDisplaySlot(p_96530_1_, p_96530_2_);
/*     */     
/*  58 */     if (scoreobjective != p_96530_2_ && scoreobjective != null)
/*     */     {
/*  60 */       if (func_96552_h(scoreobjective) > 0) {
/*     */         
/*  62 */         this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers((Packet)new S3DPacketDisplayScoreboard(p_96530_1_, p_96530_2_));
/*     */       }
/*     */       else {
/*     */         
/*  66 */         sendDisplaySlotRemovalPackets(scoreobjective);
/*     */       } 
/*     */     }
/*     */     
/*  70 */     if (p_96530_2_ != null)
/*     */     {
/*  72 */       if (this.field_96553_b.contains(p_96530_2_)) {
/*     */         
/*  74 */         this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers((Packet)new S3DPacketDisplayScoreboard(p_96530_1_, p_96530_2_));
/*     */       }
/*     */       else {
/*     */         
/*  78 */         func_96549_e(p_96530_2_);
/*     */       } 
/*     */     }
/*     */     
/*  82 */     markSaveDataDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addPlayerToTeam(String player, String newTeam) {
/*  87 */     if (super.addPlayerToTeam(player, newTeam)) {
/*     */       
/*  89 */       ScorePlayerTeam scoreplayerteam = getTeam(newTeam);
/*  90 */       this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers((Packet)new S3EPacketTeams(scoreplayerteam, Arrays.asList(new String[] { player }, ), 3));
/*  91 */       markSaveDataDirty();
/*  92 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removePlayerFromTeam(String p_96512_1_, ScorePlayerTeam p_96512_2_) {
/* 102 */     super.removePlayerFromTeam(p_96512_1_, p_96512_2_);
/* 103 */     this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers((Packet)new S3EPacketTeams(p_96512_2_, Arrays.asList(new String[] { p_96512_1_ }, ), 4));
/* 104 */     markSaveDataDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onScoreObjectiveAdded(ScoreObjective scoreObjectiveIn) {
/* 109 */     super.onScoreObjectiveAdded(scoreObjectiveIn);
/* 110 */     markSaveDataDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onObjectiveDisplayNameChanged(ScoreObjective p_96532_1_) {
/* 115 */     super.onObjectiveDisplayNameChanged(p_96532_1_);
/*     */     
/* 117 */     if (this.field_96553_b.contains(p_96532_1_))
/*     */     {
/* 119 */       this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers((Packet)new S3BPacketScoreboardObjective(p_96532_1_, 2));
/*     */     }
/*     */     
/* 122 */     markSaveDataDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onScoreObjectiveRemoved(ScoreObjective p_96533_1_) {
/* 127 */     super.onScoreObjectiveRemoved(p_96533_1_);
/*     */     
/* 129 */     if (this.field_96553_b.contains(p_96533_1_))
/*     */     {
/* 131 */       sendDisplaySlotRemovalPackets(p_96533_1_);
/*     */     }
/*     */     
/* 134 */     markSaveDataDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void broadcastTeamCreated(ScorePlayerTeam playerTeam) {
/* 139 */     super.broadcastTeamCreated(playerTeam);
/* 140 */     this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers((Packet)new S3EPacketTeams(playerTeam, 0));
/* 141 */     markSaveDataDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendTeamUpdate(ScorePlayerTeam playerTeam) {
/* 146 */     super.sendTeamUpdate(playerTeam);
/* 147 */     this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers((Packet)new S3EPacketTeams(playerTeam, 2));
/* 148 */     markSaveDataDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_96513_c(ScorePlayerTeam playerTeam) {
/* 153 */     super.func_96513_c(playerTeam);
/* 154 */     this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers((Packet)new S3EPacketTeams(playerTeam, 1));
/* 155 */     markSaveDataDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_96547_a(ScoreboardSaveData p_96547_1_) {
/* 160 */     this.scoreboardSaveData = p_96547_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void markSaveDataDirty() {
/* 165 */     if (this.scoreboardSaveData != null)
/*     */     {
/* 167 */       this.scoreboardSaveData.markDirty();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Packet> func_96550_d(ScoreObjective p_96550_1_) {
/* 173 */     List<Packet> list = Lists.newArrayList();
/* 174 */     list.add(new S3BPacketScoreboardObjective(p_96550_1_, 0));
/*     */     
/* 176 */     for (int i = 0; i < 19; i++) {
/*     */       
/* 178 */       if (getObjectiveInDisplaySlot(i) == p_96550_1_)
/*     */       {
/* 180 */         list.add(new S3DPacketDisplayScoreboard(i, p_96550_1_));
/*     */       }
/*     */     } 
/*     */     
/* 184 */     for (Score score : getSortedScores(p_96550_1_))
/*     */     {
/* 186 */       list.add(new S3CPacketUpdateScore(score));
/*     */     }
/*     */     
/* 189 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_96549_e(ScoreObjective p_96549_1_) {
/* 194 */     List<Packet> list = func_96550_d(p_96549_1_);
/*     */     
/* 196 */     for (EntityPlayerMP entityplayermp : this.scoreboardMCServer.getConfigurationManager().getPlayerList()) {
/*     */       
/* 198 */       for (Packet packet : list)
/*     */       {
/* 200 */         entityplayermp.playerNetServerHandler.sendPacket(packet);
/*     */       }
/*     */     } 
/*     */     
/* 204 */     this.field_96553_b.add(p_96549_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Packet> func_96548_f(ScoreObjective p_96548_1_) {
/* 209 */     List<Packet> list = Lists.newArrayList();
/* 210 */     list.add(new S3BPacketScoreboardObjective(p_96548_1_, 1));
/*     */     
/* 212 */     for (int i = 0; i < 19; i++) {
/*     */       
/* 214 */       if (getObjectiveInDisplaySlot(i) == p_96548_1_)
/*     */       {
/* 216 */         list.add(new S3DPacketDisplayScoreboard(i, p_96548_1_));
/*     */       }
/*     */     } 
/*     */     
/* 220 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendDisplaySlotRemovalPackets(ScoreObjective p_96546_1_) {
/* 225 */     List<Packet> list = func_96548_f(p_96546_1_);
/*     */     
/* 227 */     for (EntityPlayerMP entityplayermp : this.scoreboardMCServer.getConfigurationManager().getPlayerList()) {
/*     */       
/* 229 */       for (Packet packet : list)
/*     */       {
/* 231 */         entityplayermp.playerNetServerHandler.sendPacket(packet);
/*     */       }
/*     */     } 
/*     */     
/* 235 */     this.field_96553_b.remove(p_96546_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public int func_96552_h(ScoreObjective p_96552_1_) {
/* 240 */     int i = 0;
/*     */     
/* 242 */     for (int j = 0; j < 19; j++) {
/*     */       
/* 244 */       if (getObjectiveInDisplaySlot(j) == p_96552_1_)
/*     */       {
/* 246 */         i++;
/*     */       }
/*     */     } 
/*     */     
/* 250 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\scoreboard\ServerScoreboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */