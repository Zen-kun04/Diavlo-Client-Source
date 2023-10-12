/*    */ package net.minecraft.command;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.ChatComponentTranslation;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.world.WorldSettings;
/*    */ 
/*    */ public class CommandGameMode
/*    */   extends CommandBase {
/*    */   public String getCommandName() {
/* 15 */     return "gamemode";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 20 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 25 */     return "commands.gamemode.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 30 */     if (args.length <= 0)
/*    */     {
/* 32 */       throw new WrongUsageException("commands.gamemode.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 36 */     WorldSettings.GameType worldsettings$gametype = getGameModeFromCommand(sender, args[0]);
/* 37 */     EntityPlayerMP entityPlayerMP = (args.length >= 2) ? getPlayer(sender, args[1]) : getCommandSenderAsPlayer(sender);
/* 38 */     entityPlayerMP.setGameType(worldsettings$gametype);
/* 39 */     ((EntityPlayer)entityPlayerMP).fallDistance = 0.0F;
/*    */     
/* 41 */     if (sender.getEntityWorld().getGameRules().getBoolean("sendCommandFeedback"))
/*    */     {
/* 43 */       entityPlayerMP.addChatMessage((IChatComponent)new ChatComponentTranslation("gameMode.changed", new Object[0]));
/*    */     }
/*    */     
/* 46 */     ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("gameMode." + worldsettings$gametype.getName(), new Object[0]);
/*    */     
/* 48 */     if (entityPlayerMP != sender) {
/*    */       
/* 50 */       notifyOperators(sender, this, 1, "commands.gamemode.success.other", new Object[] { entityPlayerMP.getName(), chatComponentTranslation });
/*    */     }
/*    */     else {
/*    */       
/* 54 */       notifyOperators(sender, this, 1, "commands.gamemode.success.self", new Object[] { chatComponentTranslation });
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected WorldSettings.GameType getGameModeFromCommand(ICommandSender p_71539_1_, String p_71539_2_) throws CommandException, NumberInvalidException {
/* 61 */     return (!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.SURVIVAL.getName()) && !p_71539_2_.equalsIgnoreCase("s")) ? ((!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.CREATIVE.getName()) && !p_71539_2_.equalsIgnoreCase("c")) ? ((!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.ADVENTURE.getName()) && !p_71539_2_.equalsIgnoreCase("a")) ? ((!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.SPECTATOR.getName()) && !p_71539_2_.equalsIgnoreCase("sp")) ? WorldSettings.getGameTypeById(parseInt(p_71539_2_, 0, (WorldSettings.GameType.values()).length - 2)) : WorldSettings.GameType.SPECTATOR) : WorldSettings.GameType.ADVENTURE) : WorldSettings.GameType.CREATIVE) : WorldSettings.GameType.SURVIVAL;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 66 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, new String[] { "survival", "creative", "adventure", "spectator" }) : ((args.length == 2) ? getListOfStringsMatchingLastWord(args, getListOfPlayerUsernames()) : null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String[] getListOfPlayerUsernames() {
/* 71 */     return MinecraftServer.getServer().getAllUsernames();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isUsernameIndex(String[] args, int index) {
/* 76 */     return (index == 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandGameMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */