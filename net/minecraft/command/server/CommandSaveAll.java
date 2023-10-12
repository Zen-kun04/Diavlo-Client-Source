/*    */ package net.minecraft.command.server;
/*    */ 
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommand;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.ChatComponentTranslation;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.util.IProgressUpdate;
/*    */ import net.minecraft.world.MinecraftException;
/*    */ import net.minecraft.world.WorldServer;
/*    */ 
/*    */ public class CommandSaveAll extends CommandBase {
/*    */   public String getCommandName() {
/* 16 */     return "save-all";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 21 */     return "commands.save.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 26 */     MinecraftServer minecraftserver = MinecraftServer.getServer();
/* 27 */     sender.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.save.start", new Object[0]));
/*    */     
/* 29 */     if (minecraftserver.getConfigurationManager() != null)
/*    */     {
/* 31 */       minecraftserver.getConfigurationManager().saveAllPlayerData();
/*    */     }
/*    */ 
/*    */     
/*    */     try {
/* 36 */       for (int i = 0; i < minecraftserver.worldServers.length; i++) {
/*    */         
/* 38 */         if (minecraftserver.worldServers[i] != null) {
/*    */           
/* 40 */           WorldServer worldserver = minecraftserver.worldServers[i];
/* 41 */           boolean flag = worldserver.disableLevelSaving;
/* 42 */           worldserver.disableLevelSaving = false;
/* 43 */           worldserver.saveAllChunks(true, (IProgressUpdate)null);
/* 44 */           worldserver.disableLevelSaving = flag;
/*    */         } 
/*    */       } 
/*    */       
/* 48 */       if (args.length > 0 && "flush".equals(args[0]))
/*    */       {
/* 50 */         sender.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.save.flushStart", new Object[0]));
/*    */         
/* 52 */         for (int j = 0; j < minecraftserver.worldServers.length; j++) {
/*    */           
/* 54 */           if (minecraftserver.worldServers[j] != null) {
/*    */             
/* 56 */             WorldServer worldserver1 = minecraftserver.worldServers[j];
/* 57 */             boolean flag1 = worldserver1.disableLevelSaving;
/* 58 */             worldserver1.disableLevelSaving = false;
/* 59 */             worldserver1.saveChunkData();
/* 60 */             worldserver1.disableLevelSaving = flag1;
/*    */           } 
/*    */         } 
/*    */         
/* 64 */         sender.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.save.flushEnd", new Object[0]));
/*    */       }
/*    */     
/* 67 */     } catch (MinecraftException minecraftexception) {
/*    */       
/* 69 */       notifyOperators(sender, (ICommand)this, "commands.save.failed", new Object[] { minecraftexception.getMessage() });
/*    */       
/*    */       return;
/*    */     } 
/* 73 */     notifyOperators(sender, (ICommand)this, "commands.save.success", new Object[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandSaveAll.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */