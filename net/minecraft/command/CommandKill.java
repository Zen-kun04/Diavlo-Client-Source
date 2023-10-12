/*    */ package net.minecraft.command;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ public class CommandKill
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName() {
/* 13 */     return "kill";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 18 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 23 */     return "commands.kill.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 28 */     if (args.length == 0) {
/*    */       
/* 30 */       EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/* 31 */       entityPlayerMP.onKillCommand();
/* 32 */       notifyOperators(sender, this, "commands.kill.successful", new Object[] { entityPlayerMP.getDisplayName() });
/*    */     }
/*    */     else {
/*    */       
/* 36 */       Entity entity = getEntity(sender, args[0]);
/* 37 */       entity.onKillCommand();
/* 38 */       notifyOperators(sender, this, "commands.kill.successful", new Object[] { entity.getDisplayName() });
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isUsernameIndex(String[] args, int index) {
/* 44 */     return (index == 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 49 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandKill.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */