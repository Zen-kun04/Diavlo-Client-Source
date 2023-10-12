/*     */ package net.minecraft.command;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.scoreboard.Score;
/*     */ import net.minecraft.scoreboard.ScoreObjective;
/*     */ import net.minecraft.scoreboard.Scoreboard;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class CommandResultStats {
/*  15 */   private static final int NUM_RESULT_TYPES = (Type.values()).length;
/*  16 */   private static final String[] STRING_RESULT_TYPES = new String[NUM_RESULT_TYPES];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  22 */   private String[] entitiesID = STRING_RESULT_TYPES;
/*  23 */   private String[] objectives = STRING_RESULT_TYPES;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommandStatScore(final ICommandSender sender, Type resultTypeIn, int scorePoint) {
/*  28 */     String s = this.entitiesID[resultTypeIn.getTypeID()];
/*     */     
/*  30 */     if (s != null) {
/*     */       String s1;
/*  32 */       ICommandSender icommandsender = new ICommandSender()
/*     */         {
/*     */           public String getName()
/*     */           {
/*  36 */             return sender.getName();
/*     */           }
/*     */           
/*     */           public IChatComponent getDisplayName() {
/*  40 */             return sender.getDisplayName();
/*     */           }
/*     */           
/*     */           public void addChatMessage(IChatComponent component) {
/*  44 */             sender.addChatMessage(component);
/*     */           }
/*     */           
/*     */           public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
/*  48 */             return true;
/*     */           }
/*     */           
/*     */           public BlockPos getPosition() {
/*  52 */             return sender.getPosition();
/*     */           }
/*     */           
/*     */           public Vec3 getPositionVector() {
/*  56 */             return sender.getPositionVector();
/*     */           }
/*     */           
/*     */           public World getEntityWorld() {
/*  60 */             return sender.getEntityWorld();
/*     */           }
/*     */           
/*     */           public Entity getCommandSenderEntity() {
/*  64 */             return sender.getCommandSenderEntity();
/*     */           }
/*     */           
/*     */           public boolean sendCommandFeedback() {
/*  68 */             return sender.sendCommandFeedback();
/*     */           }
/*     */           
/*     */           public void setCommandStat(CommandResultStats.Type type, int amount) {
/*  72 */             sender.setCommandStat(type, amount);
/*     */           }
/*     */         };
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  79 */         s1 = CommandBase.getEntityName(icommandsender, s);
/*     */       }
/*  81 */       catch (EntityNotFoundException var11) {
/*     */         return;
/*     */       } 
/*     */ 
/*     */       
/*  86 */       String s2 = this.objectives[resultTypeIn.getTypeID()];
/*     */       
/*  88 */       if (s2 != null) {
/*     */         
/*  90 */         Scoreboard scoreboard = sender.getEntityWorld().getScoreboard();
/*  91 */         ScoreObjective scoreobjective = scoreboard.getObjective(s2);
/*     */         
/*  93 */         if (scoreobjective != null)
/*     */         {
/*  95 */           if (scoreboard.entityHasObjective(s1, scoreobjective)) {
/*     */             
/*  97 */             Score score = scoreboard.getValueFromObjective(s1, scoreobjective);
/*  98 */             score.setScorePoints(scorePoint);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void readStatsFromNBT(NBTTagCompound tagcompound) {
/* 107 */     if (tagcompound.hasKey("CommandStats", 10)) {
/*     */       
/* 109 */       NBTTagCompound nbttagcompound = tagcompound.getCompoundTag("CommandStats");
/*     */       
/* 111 */       for (Type commandresultstats$type : Type.values()) {
/*     */         
/* 113 */         String s = commandresultstats$type.getTypeName() + "Name";
/* 114 */         String s1 = commandresultstats$type.getTypeName() + "Objective";
/*     */         
/* 116 */         if (nbttagcompound.hasKey(s, 8) && nbttagcompound.hasKey(s1, 8)) {
/*     */           
/* 118 */           String s2 = nbttagcompound.getString(s);
/* 119 */           String s3 = nbttagcompound.getString(s1);
/* 120 */           setScoreBoardStat(this, commandresultstats$type, s2, s3);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeStatsToNBT(NBTTagCompound tagcompound) {
/* 128 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*     */     
/* 130 */     for (Type commandresultstats$type : Type.values()) {
/*     */       
/* 132 */       String s = this.entitiesID[commandresultstats$type.getTypeID()];
/* 133 */       String s1 = this.objectives[commandresultstats$type.getTypeID()];
/*     */       
/* 135 */       if (s != null && s1 != null) {
/*     */         
/* 137 */         nbttagcompound.setString(commandresultstats$type.getTypeName() + "Name", s);
/* 138 */         nbttagcompound.setString(commandresultstats$type.getTypeName() + "Objective", s1);
/*     */       } 
/*     */     } 
/*     */     
/* 142 */     if (!nbttagcompound.hasNoTags())
/*     */     {
/* 144 */       tagcompound.setTag("CommandStats", (NBTBase)nbttagcompound);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setScoreBoardStat(CommandResultStats stats, Type resultType, String entityID, String objectiveName) {
/* 150 */     if (entityID != null && entityID.length() != 0 && objectiveName != null && objectiveName.length() != 0) {
/*     */       
/* 152 */       if (stats.entitiesID == STRING_RESULT_TYPES || stats.objectives == STRING_RESULT_TYPES) {
/*     */         
/* 154 */         stats.entitiesID = new String[NUM_RESULT_TYPES];
/* 155 */         stats.objectives = new String[NUM_RESULT_TYPES];
/*     */       } 
/*     */       
/* 158 */       stats.entitiesID[resultType.getTypeID()] = entityID;
/* 159 */       stats.objectives[resultType.getTypeID()] = objectiveName;
/*     */     }
/*     */     else {
/*     */       
/* 163 */       removeScoreBoardStat(stats, resultType);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void removeScoreBoardStat(CommandResultStats resultStatsIn, Type resultTypeIn) {
/* 169 */     if (resultStatsIn.entitiesID != STRING_RESULT_TYPES && resultStatsIn.objectives != STRING_RESULT_TYPES) {
/*     */       
/* 171 */       resultStatsIn.entitiesID[resultTypeIn.getTypeID()] = null;
/* 172 */       resultStatsIn.objectives[resultTypeIn.getTypeID()] = null;
/* 173 */       boolean flag = true;
/*     */       
/* 175 */       for (Type commandresultstats$type : Type.values()) {
/*     */         
/* 177 */         if (resultStatsIn.entitiesID[commandresultstats$type.getTypeID()] != null && resultStatsIn.objectives[commandresultstats$type.getTypeID()] != null) {
/*     */           
/* 179 */           flag = false;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 184 */       if (flag) {
/*     */         
/* 186 */         resultStatsIn.entitiesID = STRING_RESULT_TYPES;
/* 187 */         resultStatsIn.objectives = STRING_RESULT_TYPES;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAllStats(CommandResultStats resultStatsIn) {
/* 194 */     for (Type commandresultstats$type : Type.values())
/*     */     {
/* 196 */       setScoreBoardStat(this, commandresultstats$type, resultStatsIn.entitiesID[commandresultstats$type.getTypeID()], resultStatsIn.objectives[commandresultstats$type.getTypeID()]);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Type
/*     */   {
/* 202 */     SUCCESS_COUNT(0, "SuccessCount"),
/* 203 */     AFFECTED_BLOCKS(1, "AffectedBlocks"),
/* 204 */     AFFECTED_ENTITIES(2, "AffectedEntities"),
/* 205 */     AFFECTED_ITEMS(3, "AffectedItems"),
/* 206 */     QUERY_RESULT(4, "QueryResult");
/*     */     
/*     */     final int typeID;
/*     */     
/*     */     final String typeName;
/*     */     
/*     */     Type(int id, String name) {
/* 213 */       this.typeID = id;
/* 214 */       this.typeName = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getTypeID() {
/* 219 */       return this.typeID;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getTypeName() {
/* 224 */       return this.typeName;
/*     */     }
/*     */ 
/*     */     
/*     */     public static String[] getTypeNames() {
/* 229 */       String[] astring = new String[(values()).length];
/* 230 */       int i = 0;
/*     */       
/* 232 */       for (Type commandresultstats$type : values())
/*     */       {
/* 234 */         astring[i++] = commandresultstats$type.getTypeName();
/*     */       }
/*     */       
/* 237 */       return astring;
/*     */     }
/*     */ 
/*     */     
/*     */     public static Type getTypeByName(String name) {
/* 242 */       for (Type commandresultstats$type : values()) {
/*     */         
/* 244 */         if (commandresultstats$type.getTypeName().equals(name))
/*     */         {
/* 246 */           return commandresultstats$type;
/*     */         }
/*     */       } 
/*     */       
/* 250 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandResultStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */