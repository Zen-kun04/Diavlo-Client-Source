/*     */ package net.minecraft.command;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S19PacketEntityStatus;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.world.GameRules;
/*     */ 
/*     */ public class CommandGameRule extends CommandBase {
/*     */   public String getCommandName() {
/*  15 */     return "gamerule";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  20 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  25 */     return "commands.gamerule.usage";
/*     */   }
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*     */     String s2;
/*  30 */     GameRules gamerules = getGameRules();
/*  31 */     String s = (args.length > 0) ? args[0] : "";
/*  32 */     String s1 = (args.length > 1) ? buildString(args, 1) : "";
/*     */     
/*  34 */     switch (args.length) {
/*     */       
/*     */       case 0:
/*  37 */         sender.addChatMessage((IChatComponent)new ChatComponentText(joinNiceString((Object[])gamerules.getRules())));
/*     */         return;
/*     */       
/*     */       case 1:
/*  41 */         if (!gamerules.hasRule(s))
/*     */         {
/*  43 */           throw new CommandException("commands.gamerule.norule", new Object[] { s });
/*     */         }
/*     */         
/*  46 */         s2 = gamerules.getString(s);
/*  47 */         sender.addChatMessage((new ChatComponentText(s)).appendText(" = ").appendText(s2));
/*  48 */         sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, gamerules.getInt(s));
/*     */         return;
/*     */     } 
/*     */     
/*  52 */     if (gamerules.areSameType(s, GameRules.ValueType.BOOLEAN_VALUE) && !"true".equals(s1) && !"false".equals(s1))
/*     */     {
/*  54 */       throw new CommandException("commands.generic.boolean.invalid", new Object[] { s1 });
/*     */     }
/*     */     
/*  57 */     gamerules.setOrCreateGameRule(s, s1);
/*  58 */     func_175773_a(gamerules, s);
/*  59 */     notifyOperators(sender, this, "commands.gamerule.success", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void func_175773_a(GameRules rules, String p_175773_1_) {
/*  65 */     if ("reducedDebugInfo".equals(p_175773_1_)) {
/*     */       
/*  67 */       byte b0 = (byte)(rules.getBoolean(p_175773_1_) ? 22 : 23);
/*     */       
/*  69 */       for (EntityPlayerMP entityplayermp : MinecraftServer.getServer().getConfigurationManager().getPlayerList())
/*     */       {
/*  71 */         entityplayermp.playerNetServerHandler.sendPacket((Packet)new S19PacketEntityStatus((Entity)entityplayermp, b0));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/*  78 */     if (args.length == 1)
/*     */     {
/*  80 */       return getListOfStringsMatchingLastWord(args, getGameRules().getRules());
/*     */     }
/*     */ 
/*     */     
/*  84 */     if (args.length == 2) {
/*     */       
/*  86 */       GameRules gamerules = getGameRules();
/*     */       
/*  88 */       if (gamerules.areSameType(args[0], GameRules.ValueType.BOOLEAN_VALUE))
/*     */       {
/*  90 */         return getListOfStringsMatchingLastWord(args, new String[] { "true", "false" });
/*     */       }
/*     */     } 
/*     */     
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private GameRules getGameRules() {
/* 100 */     return MinecraftServer.getServer().worldServerForDimension(0).getGameRules();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandGameRule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */