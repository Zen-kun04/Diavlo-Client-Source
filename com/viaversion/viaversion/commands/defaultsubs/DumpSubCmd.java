/*    */ package com.viaversion.viaversion.commands.defaultsubs;
/*    */ 
/*    */ import com.viaversion.viaversion.api.command.ViaCommandSender;
/*    */ import com.viaversion.viaversion.api.command.ViaSubCommand;
/*    */ import com.viaversion.viaversion.util.DumpUtil;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DumpSubCmd
/*    */   extends ViaSubCommand
/*    */ {
/*    */   public String name() {
/* 28 */     return "dump";
/*    */   }
/*    */ 
/*    */   
/*    */   public String description() {
/* 33 */     return "Dump information about your server, this is helpful if you report bugs.";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean execute(ViaCommandSender sender, String[] args) {
/* 38 */     DumpUtil.postDump(sender.getUUID()).whenComplete((url, e) -> {
/*    */           if (e != null) {
/*    */             sender.sendMessage("§4" + e.getMessage());
/*    */             
/*    */             return;
/*    */           } 
/*    */           sender.sendMessage("§2We've made a dump with useful information, report your issue and provide this url: " + url);
/*    */         });
/* 46 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\commands\defaultsubs\DumpSubCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */