/*    */ package net.minecraft.command.server;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.ChatComponentTranslation;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ public class CommandBroadcast
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName() {
/* 17 */     return "say";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 22 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 27 */     return "commands.say.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 32 */     if (args.length > 0 && args[0].length() > 0) {
/*    */       
/* 34 */       IChatComponent ichatcomponent = getChatComponentFromNthArg(sender, args, 0, true);
/* 35 */       MinecraftServer.getServer().getConfigurationManager().sendChatMsg((IChatComponent)new ChatComponentTranslation("chat.type.announcement", new Object[] { sender.getDisplayName(), ichatcomponent }));
/*    */     }
/*    */     else {
/*    */       
/* 39 */       throw new WrongUsageException("commands.say.usage", new Object[0]);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 45 */     return (args.length >= 1) ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandBroadcast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */