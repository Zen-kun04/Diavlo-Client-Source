/*    */ package net.minecraft.command.server;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.Date;
/*    */ import java.util.List;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommand;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.management.UserListBansEntry;
/*    */ import net.minecraft.server.management.UserListEntry;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ public class CommandBanPlayer extends CommandBase {
/*    */   public String getCommandName() {
/* 19 */     return "ban";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 24 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 29 */     return "commands.ban.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canCommandSenderUseCommand(ICommandSender sender) {
/* 34 */     return (MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().isLanServer() && super.canCommandSenderUseCommand(sender));
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 39 */     if (args.length >= 1 && args[0].length() > 0) {
/*    */       
/* 41 */       MinecraftServer minecraftserver = MinecraftServer.getServer();
/* 42 */       GameProfile gameprofile = minecraftserver.getPlayerProfileCache().getGameProfileForUsername(args[0]);
/*    */       
/* 44 */       if (gameprofile == null)
/*    */       {
/* 46 */         throw new CommandException("commands.ban.failed", new Object[] { args[0] });
/*    */       }
/*    */ 
/*    */       
/* 50 */       String s = null;
/*    */       
/* 52 */       if (args.length >= 2)
/*    */       {
/* 54 */         s = getChatComponentFromNthArg(sender, args, 1).getUnformattedText();
/*    */       }
/*    */       
/* 57 */       UserListBansEntry userlistbansentry = new UserListBansEntry(gameprofile, (Date)null, sender.getName(), (Date)null, s);
/* 58 */       minecraftserver.getConfigurationManager().getBannedPlayers().addEntry((UserListEntry)userlistbansentry);
/* 59 */       EntityPlayerMP entityplayermp = minecraftserver.getConfigurationManager().getPlayerByUsername(args[0]);
/*    */       
/* 61 */       if (entityplayermp != null)
/*    */       {
/* 63 */         entityplayermp.playerNetServerHandler.kickPlayerFromServer("You are banned from this server.");
/*    */       }
/*    */       
/* 66 */       notifyOperators(sender, (ICommand)this, "commands.ban.success", new Object[] { args[0] });
/*    */     
/*    */     }
/*    */     else {
/*    */       
/* 71 */       throw new WrongUsageException("commands.ban.usage", new Object[0]);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 77 */     return (args.length >= 1) ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandBanPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */