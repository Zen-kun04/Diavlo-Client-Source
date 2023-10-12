/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S29PacketSoundEffect;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ public class CommandPlaySound
/*     */   extends CommandBase {
/*     */   public String getCommandName() {
/*  14 */     return "playsound";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  19 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  24 */     return "commands.playsound.usage";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  29 */     if (args.length < 2)
/*     */     {
/*  31 */       throw new WrongUsageException(getCommandUsage(sender), new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  35 */     int i = 0;
/*  36 */     String s = args[i++];
/*  37 */     EntityPlayerMP entityplayermp = getPlayer(sender, args[i++]);
/*  38 */     Vec3 vec3 = sender.getPositionVector();
/*  39 */     double d0 = vec3.xCoord;
/*     */     
/*  41 */     if (args.length > i)
/*     */     {
/*  43 */       d0 = parseDouble(d0, args[i++], true);
/*     */     }
/*     */     
/*  46 */     double d1 = vec3.yCoord;
/*     */     
/*  48 */     if (args.length > i)
/*     */     {
/*  50 */       d1 = parseDouble(d1, args[i++], 0, 0, false);
/*     */     }
/*     */     
/*  53 */     double d2 = vec3.zCoord;
/*     */     
/*  55 */     if (args.length > i)
/*     */     {
/*  57 */       d2 = parseDouble(d2, args[i++], true);
/*     */     }
/*     */     
/*  60 */     double d3 = 1.0D;
/*     */     
/*  62 */     if (args.length > i)
/*     */     {
/*  64 */       d3 = parseDouble(args[i++], 0.0D, 3.4028234663852886E38D);
/*     */     }
/*     */     
/*  67 */     double d4 = 1.0D;
/*     */     
/*  69 */     if (args.length > i)
/*     */     {
/*  71 */       d4 = parseDouble(args[i++], 0.0D, 2.0D);
/*     */     }
/*     */     
/*  74 */     double d5 = 0.0D;
/*     */     
/*  76 */     if (args.length > i)
/*     */     {
/*  78 */       d5 = parseDouble(args[i], 0.0D, 1.0D);
/*     */     }
/*     */     
/*  81 */     double d6 = (d3 > 1.0D) ? (d3 * 16.0D) : 16.0D;
/*  82 */     double d7 = entityplayermp.getDistance(d0, d1, d2);
/*     */     
/*  84 */     if (d7 > d6) {
/*     */       
/*  86 */       if (d5 <= 0.0D)
/*     */       {
/*  88 */         throw new CommandException("commands.playsound.playerTooFar", new Object[] { entityplayermp.getName() });
/*     */       }
/*     */       
/*  91 */       double d8 = d0 - entityplayermp.posX;
/*  92 */       double d9 = d1 - entityplayermp.posY;
/*  93 */       double d10 = d2 - entityplayermp.posZ;
/*  94 */       double d11 = Math.sqrt(d8 * d8 + d9 * d9 + d10 * d10);
/*     */       
/*  96 */       if (d11 > 0.0D) {
/*     */         
/*  98 */         d0 = entityplayermp.posX + d8 / d11 * 2.0D;
/*  99 */         d1 = entityplayermp.posY + d9 / d11 * 2.0D;
/* 100 */         d2 = entityplayermp.posZ + d10 / d11 * 2.0D;
/*     */       } 
/*     */       
/* 103 */       d3 = d5;
/*     */     } 
/*     */     
/* 106 */     entityplayermp.playerNetServerHandler.sendPacket((Packet)new S29PacketSoundEffect(s, d0, d1, d2, (float)d3, (float)d4));
/* 107 */     notifyOperators(sender, this, "commands.playsound.success", new Object[] { s, entityplayermp.getName() });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 113 */     return (args.length == 2) ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : ((args.length > 2 && args.length <= 5) ? func_175771_a(args, 2, pos) : null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUsernameIndex(String[] args, int index) {
/* 118 */     return (index == 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandPlaySound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */