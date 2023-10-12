/*    */ package net.minecraft.command;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ public class CommandXP
/*    */   extends CommandBase {
/*    */   public String getCommandName() {
/* 12 */     return "xp";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 17 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 22 */     return "commands.xp.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 27 */     if (args.length <= 0)
/*    */     {
/* 29 */       throw new WrongUsageException("commands.xp.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 33 */     String s = args[0];
/* 34 */     boolean flag = (s.endsWith("l") || s.endsWith("L"));
/*    */     
/* 36 */     if (flag && s.length() > 1)
/*    */     {
/* 38 */       s = s.substring(0, s.length() - 1);
/*    */     }
/*    */     
/* 41 */     int i = parseInt(s);
/* 42 */     boolean flag1 = (i < 0);
/*    */     
/* 44 */     if (flag1)
/*    */     {
/* 46 */       i *= -1;
/*    */     }
/*    */     
/* 49 */     EntityPlayerMP entityPlayerMP = (args.length > 1) ? getPlayer(sender, args[1]) : getCommandSenderAsPlayer(sender);
/*    */     
/* 51 */     if (flag) {
/*    */       
/* 53 */       sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, ((EntityPlayer)entityPlayerMP).experienceLevel);
/*    */       
/* 55 */       if (flag1)
/*    */       {
/* 57 */         entityPlayerMP.addExperienceLevel(-i);
/* 58 */         notifyOperators(sender, this, "commands.xp.success.negative.levels", new Object[] { Integer.valueOf(i), entityPlayerMP.getName() });
/*    */       }
/*    */       else
/*    */       {
/* 62 */         entityPlayerMP.addExperienceLevel(i);
/* 63 */         notifyOperators(sender, this, "commands.xp.success.levels", new Object[] { Integer.valueOf(i), entityPlayerMP.getName() });
/*    */       }
/*    */     
/*    */     } else {
/*    */       
/* 68 */       sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, ((EntityPlayer)entityPlayerMP).experienceTotal);
/*    */       
/* 70 */       if (flag1)
/*    */       {
/* 72 */         throw new CommandException("commands.xp.failure.widthdrawXp", new Object[0]);
/*    */       }
/*    */       
/* 75 */       entityPlayerMP.addExperience(i);
/* 76 */       notifyOperators(sender, this, "commands.xp.success", new Object[] { Integer.valueOf(i), entityPlayerMP.getName() });
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 83 */     return (args.length == 2) ? getListOfStringsMatchingLastWord(args, getAllUsernames()) : null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String[] getAllUsernames() {
/* 88 */     return MinecraftServer.getServer().getAllUsernames();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isUsernameIndex(String[] args, int index) {
/* 93 */     return (index == 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandXP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */