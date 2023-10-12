/*    */ package net.minecraft.command.server;
/*    */ 
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommand;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ public class CommandStop
/*    */   extends CommandBase {
/*    */   public String getCommandName() {
/* 12 */     return "stop";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 17 */     return "commands.stop.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 22 */     if ((MinecraftServer.getServer()).worldServers != null)
/*    */     {
/* 24 */       notifyOperators(sender, (ICommand)this, "commands.stop.start", new Object[0]);
/*    */     }
/*    */     
/* 27 */     MinecraftServer.getServer().initiateShutdown();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandStop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */