/*    */ package net.minecraft.command.server;
/*    */ 
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommand;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.world.WorldSettings;
/*    */ 
/*    */ public class CommandPublishLocalServer
/*    */   extends CommandBase {
/*    */   public String getCommandName() {
/* 13 */     return "publish";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 18 */     return "commands.publish.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 23 */     String s = MinecraftServer.getServer().shareToLAN(WorldSettings.GameType.SURVIVAL, false);
/*    */     
/* 25 */     if (s != null) {
/*    */       
/* 27 */       notifyOperators(sender, (ICommand)this, "commands.publish.started", new Object[] { s });
/*    */     }
/*    */     else {
/*    */       
/* 31 */       notifyOperators(sender, (ICommand)this, "commands.publish.failed", new Object[0]);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandPublishLocalServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */