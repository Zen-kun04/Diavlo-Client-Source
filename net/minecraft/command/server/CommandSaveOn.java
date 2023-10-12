/*    */ package net.minecraft.command.server;
/*    */ 
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommand;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.world.WorldServer;
/*    */ 
/*    */ public class CommandSaveOn
/*    */   extends CommandBase {
/*    */   public String getCommandName() {
/* 13 */     return "save-on";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 18 */     return "commands.save-on.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 23 */     MinecraftServer minecraftserver = MinecraftServer.getServer();
/* 24 */     boolean flag = false;
/*    */     
/* 26 */     for (int i = 0; i < minecraftserver.worldServers.length; i++) {
/*    */       
/* 28 */       if (minecraftserver.worldServers[i] != null) {
/*    */         
/* 30 */         WorldServer worldserver = minecraftserver.worldServers[i];
/*    */         
/* 32 */         if (worldserver.disableLevelSaving) {
/*    */           
/* 34 */           worldserver.disableLevelSaving = false;
/* 35 */           flag = true;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 40 */     if (flag) {
/*    */       
/* 42 */       notifyOperators(sender, (ICommand)this, "commands.save.enabled", new Object[0]);
/*    */     }
/*    */     else {
/*    */       
/* 46 */       throw new CommandException("commands.save-on.alreadyOn", new Object[0]);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandSaveOn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */