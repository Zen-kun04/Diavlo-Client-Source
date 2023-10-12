/*     */ package net.minecraft.command;
/*     */ import com.google.gson.JsonParseException;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S45PacketTitle;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentProcessor;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import org.apache.commons.lang3.exception.ExceptionUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class CommandTitle extends CommandBase {
/*  17 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */   
/*     */   public String getCommandName() {
/*  21 */     return "title";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  26 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  31 */     return "commands.title.usage";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  36 */     if (args.length < 2)
/*     */     {
/*  38 */       throw new WrongUsageException("commands.title.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  42 */     if (args.length < 3) {
/*     */       
/*  44 */       if ("title".equals(args[1]) || "subtitle".equals(args[1]))
/*     */       {
/*  46 */         throw new WrongUsageException("commands.title.usage.title", new Object[0]);
/*     */       }
/*     */       
/*  49 */       if ("times".equals(args[1]))
/*     */       {
/*  51 */         throw new WrongUsageException("commands.title.usage.times", new Object[0]);
/*     */       }
/*     */     } 
/*     */     
/*  55 */     EntityPlayerMP entityplayermp = getPlayer(sender, args[0]);
/*  56 */     S45PacketTitle.Type s45packettitle$type = S45PacketTitle.Type.byName(args[1]);
/*     */     
/*  58 */     if (s45packettitle$type != S45PacketTitle.Type.CLEAR && s45packettitle$type != S45PacketTitle.Type.RESET) {
/*     */       
/*  60 */       if (s45packettitle$type == S45PacketTitle.Type.TIMES) {
/*     */         
/*  62 */         if (args.length != 5)
/*     */         {
/*  64 */           throw new WrongUsageException("commands.title.usage", new Object[0]);
/*     */         }
/*     */ 
/*     */         
/*  68 */         int i = parseInt(args[2]);
/*  69 */         int j = parseInt(args[3]);
/*  70 */         int k = parseInt(args[4]);
/*  71 */         S45PacketTitle s45packettitle2 = new S45PacketTitle(i, j, k);
/*  72 */         entityplayermp.playerNetServerHandler.sendPacket((Packet)s45packettitle2);
/*  73 */         notifyOperators(sender, this, "commands.title.success", new Object[0]);
/*     */       } else {
/*     */         IChatComponent ichatcomponent;
/*  76 */         if (args.length < 3)
/*     */         {
/*  78 */           throw new WrongUsageException("commands.title.usage", new Object[0]);
/*     */         }
/*     */ 
/*     */         
/*  82 */         String s = buildString(args, 2);
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/*  87 */           ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
/*     */         }
/*  89 */         catch (JsonParseException jsonparseexception) {
/*     */           
/*  91 */           Throwable throwable = ExceptionUtils.getRootCause((Throwable)jsonparseexception);
/*  92 */           throw new SyntaxErrorException("commands.tellraw.jsonException", new Object[] { (throwable == null) ? "" : throwable.getMessage() });
/*     */         } 
/*     */         
/*  95 */         S45PacketTitle s45packettitle1 = new S45PacketTitle(s45packettitle$type, ChatComponentProcessor.processComponent(sender, ichatcomponent, (Entity)entityplayermp));
/*  96 */         entityplayermp.playerNetServerHandler.sendPacket((Packet)s45packettitle1);
/*  97 */         notifyOperators(sender, this, "commands.title.success", new Object[0]);
/*     */       } 
/*     */     } else {
/* 100 */       if (args.length != 2)
/*     */       {
/* 102 */         throw new WrongUsageException("commands.title.usage", new Object[0]);
/*     */       }
/*     */ 
/*     */       
/* 106 */       S45PacketTitle s45packettitle = new S45PacketTitle(s45packettitle$type, (IChatComponent)null);
/* 107 */       entityplayermp.playerNetServerHandler.sendPacket((Packet)s45packettitle);
/* 108 */       notifyOperators(sender, this, "commands.title.success", new Object[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 115 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : ((args.length == 2) ? getListOfStringsMatchingLastWord(args, S45PacketTitle.Type.getNames()) : null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUsernameIndex(String[] args, int index) {
/* 120 */     return (index == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandTitle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */