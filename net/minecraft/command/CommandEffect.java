/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ 
/*     */ public class CommandEffect
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName() {
/*  15 */     return "effect";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  20 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  25 */     return "commands.effect.usage";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  30 */     if (args.length < 2)
/*     */     {
/*  32 */       throw new WrongUsageException("commands.effect.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  36 */     EntityLivingBase entitylivingbase = getEntity(sender, args[0], EntityLivingBase.class);
/*     */     
/*  38 */     if (args[1].equals("clear")) {
/*     */       
/*  40 */       if (entitylivingbase.getActivePotionEffects().isEmpty())
/*     */       {
/*  42 */         throw new CommandException("commands.effect.failure.notActive.all", new Object[] { entitylivingbase.getName() });
/*     */       }
/*     */ 
/*     */       
/*  46 */       entitylivingbase.clearActivePotions();
/*  47 */       notifyOperators(sender, this, "commands.effect.success.removed.all", new Object[] { entitylivingbase.getName() });
/*     */     } else {
/*     */       int i;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  56 */         i = parseInt(args[1], 1);
/*     */       }
/*  58 */       catch (NumberInvalidException numberinvalidexception) {
/*     */         
/*  60 */         Potion potion = Potion.getPotionFromResourceLocation(args[1]);
/*     */         
/*  62 */         if (potion == null)
/*     */         {
/*  64 */           throw numberinvalidexception;
/*     */         }
/*     */         
/*  67 */         i = potion.id;
/*     */       } 
/*     */       
/*  70 */       int j = 600;
/*  71 */       int l = 30;
/*  72 */       int k = 0;
/*     */       
/*  74 */       if (i >= 0 && i < Potion.potionTypes.length && Potion.potionTypes[i] != null) {
/*     */         
/*  76 */         Potion potion1 = Potion.potionTypes[i];
/*     */         
/*  78 */         if (args.length >= 3) {
/*     */           
/*  80 */           l = parseInt(args[2], 0, 1000000);
/*     */           
/*  82 */           if (potion1.isInstant())
/*     */           {
/*  84 */             j = l;
/*     */           }
/*     */           else
/*     */           {
/*  88 */             j = l * 20;
/*     */           }
/*     */         
/*  91 */         } else if (potion1.isInstant()) {
/*     */           
/*  93 */           j = 1;
/*     */         } 
/*     */         
/*  96 */         if (args.length >= 4)
/*     */         {
/*  98 */           k = parseInt(args[3], 0, 255);
/*     */         }
/*     */         
/* 101 */         boolean flag = true;
/*     */         
/* 103 */         if (args.length >= 5 && "true".equalsIgnoreCase(args[4]))
/*     */         {
/* 105 */           flag = false;
/*     */         }
/*     */         
/* 108 */         if (l > 0)
/*     */         {
/* 110 */           PotionEffect potioneffect = new PotionEffect(i, j, k, false, flag);
/* 111 */           entitylivingbase.addPotionEffect(potioneffect);
/* 112 */           notifyOperators(sender, this, "commands.effect.success", new Object[] { new ChatComponentTranslation(potioneffect.getEffectName(), new Object[0]), Integer.valueOf(i), Integer.valueOf(k), entitylivingbase.getName(), Integer.valueOf(l) });
/*     */         }
/* 114 */         else if (entitylivingbase.isPotionActive(i))
/*     */         {
/* 116 */           entitylivingbase.removePotionEffect(i);
/* 117 */           notifyOperators(sender, this, "commands.effect.success.removed", new Object[] { new ChatComponentTranslation(potion1.getName(), new Object[0]), entitylivingbase.getName() });
/*     */         }
/*     */         else
/*     */         {
/* 121 */           throw new CommandException("commands.effect.failure.notActive", new Object[] { new ChatComponentTranslation(potion1.getName(), new Object[0]), entitylivingbase.getName() });
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 126 */         throw new NumberInvalidException("commands.effect.notFound", new Object[] { Integer.valueOf(i) });
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 134 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, getAllUsernames()) : ((args.length == 2) ? getListOfStringsMatchingLastWord(args, Potion.getPotionLocations()) : ((args.length == 5) ? getListOfStringsMatchingLastWord(args, new String[] { "true", "false" }) : null));
/*     */   }
/*     */ 
/*     */   
/*     */   protected String[] getAllUsernames() {
/* 139 */     return MinecraftServer.getServer().getAllUsernames();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUsernameIndex(String[] args, int index) {
/* 144 */     return (index == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */