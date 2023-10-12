/*    */ package net.minecraft.command.server;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.List;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommand;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ public class CommandPardonPlayer
/*    */   extends CommandBase {
/*    */   public String getCommandName() {
/* 16 */     return "pardon";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 21 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 26 */     return "commands.unban.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canCommandSenderUseCommand(ICommandSender sender) {
/* 31 */     return (MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().isLanServer() && super.canCommandSenderUseCommand(sender));
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 36 */     if (args.length == 1 && args[0].length() > 0) {
/*    */       
/* 38 */       MinecraftServer minecraftserver = MinecraftServer.getServer();
/* 39 */       GameProfile gameprofile = minecraftserver.getConfigurationManager().getBannedPlayers().isUsernameBanned(args[0]);
/*    */       
/* 41 */       if (gameprofile == null)
/*    */       {
/* 43 */         throw new CommandException("commands.unban.failed", new Object[] { args[0] });
/*    */       }
/*    */ 
/*    */       
/* 47 */       minecraftserver.getConfigurationManager().getBannedPlayers().removeEntry(gameprofile);
/* 48 */       notifyOperators(sender, (ICommand)this, "commands.unban.success", new Object[] { args[0] });
/*    */     
/*    */     }
/*    */     else {
/*    */       
/* 53 */       throw new WrongUsageException("commands.unban.usage", new Object[0]);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 59 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().getKeys()) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandPardonPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */