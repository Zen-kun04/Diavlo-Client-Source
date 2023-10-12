/*    */ package net.minecraft.command;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.ChatComponentTranslation;
/*    */ import net.minecraft.world.WorldSettings;
/*    */ 
/*    */ public class CommandDefaultGameMode
/*    */   extends CommandGameMode
/*    */ {
/*    */   public String getCommandName() {
/* 12 */     return "defaultgamemode";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 17 */     return "commands.defaultgamemode.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 22 */     if (args.length <= 0)
/*    */     {
/* 24 */       throw new WrongUsageException("commands.defaultgamemode.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 28 */     WorldSettings.GameType worldsettings$gametype = getGameModeFromCommand(sender, args[0]);
/* 29 */     setGameType(worldsettings$gametype);
/* 30 */     notifyOperators(sender, this, "commands.defaultgamemode.success", new Object[] { new ChatComponentTranslation("gameMode." + worldsettings$gametype.getName(), new Object[0]) });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setGameType(WorldSettings.GameType gameMode) {
/* 36 */     MinecraftServer minecraftserver = MinecraftServer.getServer();
/* 37 */     minecraftserver.setGameType(gameMode);
/*    */     
/* 39 */     if (minecraftserver.getForceGamemode())
/*    */     {
/* 41 */       for (EntityPlayerMP entityplayermp : MinecraftServer.getServer().getConfigurationManager().getPlayerList()) {
/*    */         
/* 43 */         entityplayermp.setGameType(gameMode);
/* 44 */         entityplayermp.fallDistance = 0.0F;
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandDefaultGameMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */