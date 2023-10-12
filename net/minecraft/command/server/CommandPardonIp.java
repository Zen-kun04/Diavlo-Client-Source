/*    */ package net.minecraft.command.server;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.regex.Matcher;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommand;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.SyntaxErrorException;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ public class CommandPardonIp
/*    */   extends CommandBase {
/*    */   public String getCommandName() {
/* 17 */     return "pardon-ip";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 22 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canCommandSenderUseCommand(ICommandSender sender) {
/* 27 */     return (MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isLanServer() && super.canCommandSenderUseCommand(sender));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 32 */     return "commands.unbanip.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 37 */     if (args.length == 1 && args[0].length() > 1) {
/*    */       
/* 39 */       Matcher matcher = CommandBanIp.field_147211_a.matcher(args[0]);
/*    */       
/* 41 */       if (matcher.matches())
/*    */       {
/* 43 */         MinecraftServer.getServer().getConfigurationManager().getBannedIPs().removeEntry(args[0]);
/* 44 */         notifyOperators(sender, (ICommand)this, "commands.unbanip.success", new Object[] { args[0] });
/*    */       }
/*    */       else
/*    */       {
/* 48 */         throw new SyntaxErrorException("commands.unbanip.invalid", new Object[0]);
/*    */       }
/*    */     
/*    */     } else {
/*    */       
/* 53 */       throw new WrongUsageException("commands.unbanip.usage", new Object[0]);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 59 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getBannedIPs().getKeys()) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandPardonIp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */