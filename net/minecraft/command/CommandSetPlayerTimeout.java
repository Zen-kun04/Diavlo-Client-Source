/*    */ package net.minecraft.command;
/*    */ 
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ public class CommandSetPlayerTimeout
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName() {
/*  9 */     return "setidletimeout";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 14 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 19 */     return "commands.setidletimeout.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 24 */     if (args.length != 1)
/*    */     {
/* 26 */       throw new WrongUsageException("commands.setidletimeout.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 30 */     int i = parseInt(args[0], 0);
/* 31 */     MinecraftServer.getServer().setPlayerIdleTimeout(i);
/* 32 */     notifyOperators(sender, this, "commands.setidletimeout.success", new Object[] { Integer.valueOf(i) });
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandSetPlayerTimeout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */