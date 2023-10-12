/*    */ package net.minecraft.command;
/*    */ 
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.world.storage.WorldInfo;
/*    */ 
/*    */ public class CommandToggleDownfall
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName() {
/* 10 */     return "toggledownfall";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 15 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 20 */     return "commands.downfall.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 25 */     toggleDownfall();
/* 26 */     notifyOperators(sender, this, "commands.downfall.success", new Object[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void toggleDownfall() {
/* 31 */     WorldInfo worldinfo = (MinecraftServer.getServer()).worldServers[0].getWorldInfo();
/* 32 */     worldinfo.setRaining(!worldinfo.isRaining());
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandToggleDownfall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */