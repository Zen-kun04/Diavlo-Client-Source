/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ 
/*     */ public class CommandParticle
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName() {
/*  14 */     return "particle";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  19 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  24 */     return "commands.particle.usage";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  29 */     if (args.length < 8)
/*     */     {
/*  31 */       throw new WrongUsageException("commands.particle.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  35 */     boolean flag = false;
/*  36 */     EnumParticleTypes enumparticletypes = null;
/*     */     
/*  38 */     for (EnumParticleTypes enumparticletypes1 : EnumParticleTypes.values()) {
/*     */       
/*  40 */       if (enumparticletypes1.hasArguments()) {
/*     */         
/*  42 */         if (args[0].startsWith(enumparticletypes1.getParticleName())) {
/*     */           
/*  44 */           flag = true;
/*  45 */           enumparticletypes = enumparticletypes1;
/*     */           
/*     */           break;
/*     */         } 
/*  49 */       } else if (args[0].equals(enumparticletypes1.getParticleName())) {
/*     */         
/*  51 */         flag = true;
/*  52 */         enumparticletypes = enumparticletypes1;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  57 */     if (!flag)
/*     */     {
/*  59 */       throw new CommandException("commands.particle.notFound", new Object[] { args[0] });
/*     */     }
/*     */ 
/*     */     
/*  63 */     String s = args[0];
/*  64 */     Vec3 vec3 = sender.getPositionVector();
/*  65 */     double d6 = (float)parseDouble(vec3.xCoord, args[1], true);
/*  66 */     double d0 = (float)parseDouble(vec3.yCoord, args[2], true);
/*  67 */     double d1 = (float)parseDouble(vec3.zCoord, args[3], true);
/*  68 */     double d2 = (float)parseDouble(args[4]);
/*  69 */     double d3 = (float)parseDouble(args[5]);
/*  70 */     double d4 = (float)parseDouble(args[6]);
/*  71 */     double d5 = (float)parseDouble(args[7]);
/*  72 */     int i = 0;
/*     */     
/*  74 */     if (args.length > 8)
/*     */     {
/*  76 */       i = parseInt(args[8], 0);
/*     */     }
/*     */     
/*  79 */     boolean flag1 = false;
/*     */     
/*  81 */     if (args.length > 9 && "force".equals(args[9]))
/*     */     {
/*  83 */       flag1 = true;
/*     */     }
/*     */     
/*  86 */     World world = sender.getEntityWorld();
/*     */     
/*  88 */     if (world instanceof WorldServer) {
/*     */       
/*  90 */       WorldServer worldserver = (WorldServer)world;
/*  91 */       int[] aint = new int[enumparticletypes.getArgumentCount()];
/*     */       
/*  93 */       if (enumparticletypes.hasArguments()) {
/*     */         
/*  95 */         String[] astring = args[0].split("_", 3);
/*     */         
/*  97 */         for (int j = 1; j < astring.length; j++) {
/*     */ 
/*     */           
/*     */           try {
/* 101 */             aint[j - 1] = Integer.parseInt(astring[j]);
/*     */           }
/* 103 */           catch (NumberFormatException var29) {
/*     */             
/* 105 */             throw new CommandException("commands.particle.notFound", new Object[] { args[0] });
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 110 */       worldserver.spawnParticle(enumparticletypes, flag1, d6, d0, d1, i, d2, d3, d4, d5, aint);
/* 111 */       notifyOperators(sender, this, "commands.particle.success", new Object[] { s, Integer.valueOf(Math.max(i, 1)) });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 119 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, EnumParticleTypes.getParticleNames()) : ((args.length > 1 && args.length <= 4) ? func_175771_a(args, 1, pos) : ((args.length == 10) ? getListOfStringsMatchingLastWord(args, new String[] { "normal", "force" }) : null));
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandParticle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */