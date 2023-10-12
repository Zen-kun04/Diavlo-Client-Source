/*     */ package net.minecraft.command;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.scoreboard.IScoreObjectiveCriteria;
/*     */ import net.minecraft.scoreboard.Score;
/*     */ import net.minecraft.scoreboard.ScoreObjective;
/*     */ import net.minecraft.scoreboard.Scoreboard;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ 
/*     */ public class CommandTrigger
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName() {
/*  18 */     return "trigger";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  23 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  28 */     return "commands.trigger.usage";
/*     */   }
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*     */     EntityPlayerMP entityplayermp;
/*  33 */     if (args.length < 3)
/*     */     {
/*  35 */       throw new WrongUsageException("commands.trigger.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  41 */     if (sender instanceof EntityPlayerMP) {
/*     */       
/*  43 */       entityplayermp = (EntityPlayerMP)sender;
/*     */     }
/*     */     else {
/*     */       
/*  47 */       Entity entity = sender.getCommandSenderEntity();
/*     */       
/*  49 */       if (!(entity instanceof EntityPlayerMP))
/*     */       {
/*  51 */         throw new CommandException("commands.trigger.invalidPlayer", new Object[0]);
/*     */       }
/*     */       
/*  54 */       entityplayermp = (EntityPlayerMP)entity;
/*     */     } 
/*     */     
/*  57 */     Scoreboard scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
/*  58 */     ScoreObjective scoreobjective = scoreboard.getObjective(args[0]);
/*     */     
/*  60 */     if (scoreobjective != null && scoreobjective.getCriteria() == IScoreObjectiveCriteria.TRIGGER) {
/*     */       
/*  62 */       int i = parseInt(args[2]);
/*     */       
/*  64 */       if (!scoreboard.entityHasObjective(entityplayermp.getName(), scoreobjective))
/*     */       {
/*  66 */         throw new CommandException("commands.trigger.invalidObjective", new Object[] { args[0] });
/*     */       }
/*     */ 
/*     */       
/*  70 */       Score score = scoreboard.getValueFromObjective(entityplayermp.getName(), scoreobjective);
/*     */       
/*  72 */       if (score.isLocked())
/*     */       {
/*  74 */         throw new CommandException("commands.trigger.disabled", new Object[] { args[0] });
/*     */       }
/*     */ 
/*     */       
/*  78 */       if ("set".equals(args[1])) {
/*     */         
/*  80 */         score.setScorePoints(i);
/*     */       }
/*     */       else {
/*     */         
/*  84 */         if (!"add".equals(args[1]))
/*     */         {
/*  86 */           throw new CommandException("commands.trigger.invalidMode", new Object[] { args[1] });
/*     */         }
/*     */         
/*  89 */         score.increseScore(i);
/*     */       } 
/*     */       
/*  92 */       score.setLocked(true);
/*     */       
/*  94 */       if (entityplayermp.theItemInWorldManager.isCreative())
/*     */       {
/*  96 */         notifyOperators(sender, this, "commands.trigger.success", new Object[] { args[0], args[1], args[2] });
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 103 */       throw new CommandException("commands.trigger.invalidObjective", new Object[] { args[0] });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 110 */     if (args.length == 1) {
/*     */       
/* 112 */       Scoreboard scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
/* 113 */       List<String> list = Lists.newArrayList();
/*     */       
/* 115 */       for (ScoreObjective scoreobjective : scoreboard.getScoreObjectives()) {
/*     */         
/* 117 */         if (scoreobjective.getCriteria() == IScoreObjectiveCriteria.TRIGGER)
/*     */         {
/* 119 */           list.add(scoreobjective.getName());
/*     */         }
/*     */       } 
/*     */       
/* 123 */       return getListOfStringsMatchingLastWord(args, list.<String>toArray(new String[list.size()]));
/*     */     } 
/*     */ 
/*     */     
/* 127 */     return (args.length == 2) ? getListOfStringsMatchingLastWord(args, new String[] { "add", "set" }) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */