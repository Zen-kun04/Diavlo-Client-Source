/*    */ package rip.diavlo.base.commands;
/*    */ 
/*    */ import es.diavlo.api.data.UserData;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import rip.diavlo.base.api.command.CommandExecutionException;
/*    */ import rip.diavlo.base.api.command.CommandVIP;
/*    */ import rip.diavlo.base.utils.ChatUtil;
/*    */ 
/*    */ public class SVSDataCommand
/*    */   implements CommandVIP {
/* 11 */   private Minecraft mc = Minecraft.getMinecraft();
/*    */   
/*    */   private UserData diavloUser;
/*    */   
/*    */   public static boolean isGetData = false;
/* 16 */   public static String fileData = "";
/*    */ 
/*    */   
/*    */   public String[] getAliases() {
/* 20 */     return new String[] { "svs" };
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args) throws CommandExecutionException {
/* 25 */     this.diavloUser = UserData.getInstance();
/*    */     
/* 27 */     if (this.diavloUser == null) {
/* 28 */       ChatUtil.print("§4§lERROR: §c§lTienes que logearte en Diavlo para realizar este comando.", true);
/*    */       
/*    */       return;
/*    */     } 
/* 32 */     if (args.length != 3) {
/* 33 */       ChatUtil.print(".svs <archivo> <on/off>", true);
/*    */       
/*    */       return;
/*    */     } 
/*    */     try {
/* 38 */       isGetData = args[2].equalsIgnoreCase("on");
/*    */       
/* 40 */       if (args[2].equalsIgnoreCase("on")) {
/* 41 */         ChatUtil.print("§a§lObtencion de datos mediante ServerSigns activado.", true);
/* 42 */         isGetData = true;
/* 43 */         fileData = args[1];
/*    */       } else {
/* 45 */         ChatUtil.print("§c§lObtencion de datos mediante ServerSigns desactivado.", true);
/* 46 */         isGetData = false;
/*    */       } 
/* 48 */     } catch (Exception x) {
/* 49 */       ChatUtil.print("§4§lHa ocurrido algun error durante la ejecución.", true);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUsage() {
/* 57 */     return " <on/off> || Activa o desactiva la obtencion de datos del servidor mediante ServerSigns.";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\SVSDataCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */