/*    */ package net.minecraft.command;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ public class CommandServerKick
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName() {
/* 12 */     return "kick";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 17 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 22 */     return "commands.kick.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 27 */     if (args.length > 0 && args[0].length() > 1) {
/*    */       
/* 29 */       EntityPlayerMP entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(args[0]);
/* 30 */       String s = "Kicked by an operator.";
/* 31 */       boolean flag = false;
/*    */       
/* 33 */       if (entityplayermp == null)
/*    */       {
/* 35 */         throw new PlayerNotFoundException();
/*    */       }
/*    */ 
/*    */       
/* 39 */       if (args.length >= 2) {
/*    */         
/* 41 */         s = getChatComponentFromNthArg(sender, args, 1).getUnformattedText();
/* 42 */         flag = true;
/*    */       } 
/*    */       
/* 45 */       entityplayermp.playerNetServerHandler.kickPlayerFromServer(s);
/*    */       
/* 47 */       if (flag)
/*    */       {
/* 49 */         notifyOperators(sender, this, "commands.kick.success.reason", new Object[] { entityplayermp.getName(), s });
/*    */       }
/*    */       else
/*    */       {
/* 53 */         notifyOperators(sender, this, "commands.kick.success", new Object[] { entityplayermp.getName() });
/*    */       }
/*    */     
/*    */     }
/*    */     else {
/*    */       
/* 59 */       throw new WrongUsageException("commands.kick.usage", new Object[0]);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 65 */     return (args.length >= 1) ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandServerKick.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */