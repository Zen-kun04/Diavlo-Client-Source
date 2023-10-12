/*    */ package rip.diavlo.base.commands;
/*    */ 
/*    */ import es.diavlo.api.data.UserData;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import rip.diavlo.base.api.command.CommandExecutionException;
/*    */ import rip.diavlo.base.api.command.CommandVIP;
/*    */ import rip.diavlo.base.utils.ChatUtil;
/*    */ 
/*    */ public final class KickAllCommand
/*    */   implements CommandVIP
/*    */ {
/*    */   private UserData diavloUser;
/*    */   
/*    */   public String[] getAliases() {
/* 15 */     return new String[] { "kickall" };
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(String[] arguments) throws CommandExecutionException {
/* 21 */     this.diavloUser = UserData.getInstance();
/*    */     
/* 23 */     if (mc.isSingleplayer()) {
/* 24 */       ChatUtil.print(".kickall solo se puede usar en §c§lMULTIJUGADOR", true);
/*    */       
/*    */       return;
/*    */     } 
/* 28 */     if (this.diavloUser == null) {
/* 29 */       ChatUtil.print("§4§lERROR: §c§lTienes que logearte en Diavlo para realizar este comando.", true);
/*    */       
/*    */       return;
/*    */     } 
/* 33 */     ChatUtil.print("§a§lEjecutando KickAll...", true);
/*    */     
/* 35 */     for (EntityPlayer p : mc.theWorld.playerEntities) {
/*    */ 
/*    */       
/* 38 */       if (!p.getName().equals(mc.thePlayer.getName())) {
/* 39 */         mc.thePlayer.sendChatMessage("/kick " + p.getName() + " HACKED | Siguenos en telegram t.me/diavlodb - discord.gg/programadores");
/*    */       }
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 45 */     ChatUtil.print("§a§lKickAll ejectuado.", true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUsage() {
/* 52 */     return " || Expulsa a todos los usuarios del server. [OP]";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\KickAllCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */