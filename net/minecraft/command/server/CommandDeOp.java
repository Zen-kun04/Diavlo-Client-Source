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
/*    */ public class CommandDeOp
/*    */   extends CommandBase {
/*    */   public String getCommandName() {
/* 16 */     return "deop";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 21 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 26 */     return "commands.deop.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 31 */     if (args.length == 1 && args[0].length() > 0) {
/*    */       
/* 33 */       MinecraftServer minecraftserver = MinecraftServer.getServer();
/* 34 */       GameProfile gameprofile = minecraftserver.getConfigurationManager().getOppedPlayers().getGameProfileFromName(args[0]);
/*    */       
/* 36 */       if (gameprofile == null)
/*    */       {
/* 38 */         throw new CommandException("commands.deop.failed", new Object[] { args[0] });
/*    */       }
/*    */ 
/*    */       
/* 42 */       minecraftserver.getConfigurationManager().removeOp(gameprofile);
/* 43 */       notifyOperators(sender, (ICommand)this, "commands.deop.success", new Object[] { args[0] });
/*    */     
/*    */     }
/*    */     else {
/*    */       
/* 48 */       throw new WrongUsageException("commands.deop.usage", new Object[0]);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 54 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getOppedPlayerNames()) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandDeOp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */