/*    */ package rip.diavlo.base.commands;
/*    */ 
/*    */ import es.diavlo.api.data.UserData;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.datatransfer.StringSelection;
/*    */ import java.util.TreeSet;
/*    */ import rip.diavlo.base.api.command.CommandExecutionException;
/*    */ import rip.diavlo.base.api.command.CommandVIP;
/*    */ import rip.diavlo.base.utils.ChatUtil;
/*    */ import rip.diavlo.base.utils.TabUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class getVIPSCommand
/*    */   implements CommandVIP
/*    */ {
/*    */   private UserData diavloUser;
/*    */   private TreeSet<String> vips;
/*    */   
/*    */   public String[] getAliases() {
/* 28 */     return new String[] { "getvips" };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(String[] arguments) throws CommandExecutionException {
/* 35 */     this.diavloUser = UserData.getInstance();
/*    */     
/* 37 */     this.vips = new TreeSet<>();
/*    */     
/* 39 */     if (this.diavloUser == null) {
/* 40 */       ChatUtil.print("§4§lERROR: §c§lTienes que logearte en Diavlo para realizar este comando.", true);
/*    */       
/*    */       return;
/*    */     } 
/* 44 */     if (mc.isSingleplayer()) {
/* 45 */       ChatUtil.print(".getvips solo se puede usar en §c§lMULTIJUGADOR", true);
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/*    */     try {
/* 51 */       this.vips = (TreeSet<String>)TabUtils.getVIPPlayers();
/*    */       
/* 53 */       ChatUtil.print("§6§lSe han copiado §c§l" + this.vips.size() + "§6§l nicks en el portapapeles.", true);
/* 54 */       StringSelection stringSelection = new StringSelection(String.join(" , ", (Iterable)this.vips));
/* 55 */       Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
/*    */     }
/* 57 */     catch (Exception exception) {}
/*    */     
/* 59 */     this.vips = new TreeSet<>();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUsage() {
/* 67 */     return " || Copia en el portapapeles los usuarios VIP y STAFF conectados al servidor.";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\getVIPSCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */