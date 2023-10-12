/*    */ package rip.diavlo.base.commands;
/*    */ 
/*    */ import es.diavlo.api.data.UserData;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import rip.diavlo.base.api.command.CommandExecutionException;
/*    */ import rip.diavlo.base.api.command.CommandVIP;
/*    */ import rip.diavlo.base.utils.ChatUtil;
/*    */ import rip.diavlo.base.utils.MSTimer;
/*    */ 
/*    */ public final class BanAllCommand
/*    */   implements CommandVIP
/*    */ {
/*    */   private UserData diavloUser;
/*    */   
/*    */   public String[] getAliases() {
/* 16 */     return new String[] { "banall" };
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] arguments) throws CommandExecutionException {
/* 21 */     this.diavloUser = UserData.getInstance();
/* 22 */     if (mc.isSingleplayer()) {
/* 23 */       ChatUtil.print(".banall solo se puede usar en §c§lMULTIJUGADOR", true);
/*    */       return;
/*    */     } 
/* 26 */     if (this.diavloUser == null) {
/* 27 */       ChatUtil.print("§4§lERROR: §c§lTienes que logearte en Diavlo para realizar este comando.", true);
/*    */       return;
/*    */     } 
/* 30 */     MSTimer t = new MSTimer();
/* 31 */     for (EntityPlayer p : mc.theWorld.playerEntities) {
/* 32 */       ChatUtil.print("Ejecutando BanAll...", true);
/* 33 */       while (!t.hasTimePassed(350L));
/* 34 */       t.reset();
/* 35 */       if (!p.getName().equals(mc.thePlayer.getName())) {
/* 36 */         mc.thePlayer.sendChatMessage("/ban " + p.getName() + " HACKED | Siguenos en telegram t.me/diavlodb - discord.gg/programadores");
/*    */       }
/* 38 */       ChatUtil.print("BanAll ejecutado", true);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUsage() {
/* 44 */     return " || Banea a todos los usuarios del server. [OP]";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\BanAllCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */