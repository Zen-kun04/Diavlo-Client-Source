/*    */ package net.minecraft.command.server;
/*    */ 
/*    */ import com.google.common.collect.Lists;
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
/*    */ public class CommandOp
/*    */   extends CommandBase {
/*    */   public String getCommandName() {
/* 17 */     return "op";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 22 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 27 */     return "commands.op.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 32 */     if (args.length == 1 && args[0].length() > 0) {
/*    */       
/* 34 */       MinecraftServer minecraftserver = MinecraftServer.getServer();
/* 35 */       GameProfile gameprofile = minecraftserver.getPlayerProfileCache().getGameProfileForUsername(args[0]);
/*    */       
/* 37 */       if (gameprofile == null)
/*    */       {
/* 39 */         throw new CommandException("commands.op.failed", new Object[] { args[0] });
/*    */       }
/*    */ 
/*    */       
/* 43 */       minecraftserver.getConfigurationManager().addOp(gameprofile);
/* 44 */       notifyOperators(sender, (ICommand)this, "commands.op.success", new Object[] { args[0] });
/*    */     
/*    */     }
/*    */     else {
/*    */       
/* 49 */       throw new WrongUsageException("commands.op.usage", new Object[0]);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 55 */     if (args.length == 1) {
/*    */       
/* 57 */       String s = args[args.length - 1];
/* 58 */       List<String> list = Lists.newArrayList();
/*    */       
/* 60 */       for (GameProfile gameprofile : MinecraftServer.getServer().getGameProfiles()) {
/*    */         
/* 62 */         if (!MinecraftServer.getServer().getConfigurationManager().canSendCommands(gameprofile) && doesStringStartWith(s, gameprofile.getName()))
/*    */         {
/* 64 */           list.add(gameprofile.getName());
/*    */         }
/*    */       } 
/*    */       
/* 68 */       return list;
/*    */     } 
/*    */ 
/*    */     
/* 72 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandOp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */