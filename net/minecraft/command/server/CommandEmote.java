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
/*    */ 
/*    */ public class CommandEmote
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName() {
/* 18 */     return "me";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 23 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 28 */     return "commands.me.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 33 */     if (args.length <= 0)
/*    */     {
/* 35 */       throw new WrongUsageException("commands.me.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 39 */     IChatComponent ichatcomponent = getChatComponentFromNthArg(sender, args, 0, !(sender instanceof net.minecraft.entity.player.EntityPlayer));
/* 40 */     MinecraftServer.getServer().getConfigurationManager().sendChatMsg((IChatComponent)new ChatComponentTranslation("chat.type.emote", new Object[] { sender.getDisplayName(), ichatcomponent }));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 46 */     return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandEmote.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */