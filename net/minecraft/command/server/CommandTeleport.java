/*     */ package net.minecraft.command.server;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.command.CommandBase;
/*     */ import net.minecraft.command.CommandException;
/*     */ import net.minecraft.command.ICommand;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.command.WrongUsageException;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.play.server.S08PacketPlayerPosLook;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class CommandTeleport
/*     */   extends CommandBase {
/*     */   public String getCommandName() {
/*  21 */     return "tp";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  26 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  31 */     return "commands.tp.usage";
/*     */   }
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*     */     Entity entity;
/*  36 */     if (args.length < 1)
/*     */     {
/*  38 */       throw new WrongUsageException("commands.tp.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  42 */     int i = 0;
/*     */ 
/*     */     
/*  45 */     if (args.length != 2 && args.length != 4 && args.length != 6) {
/*     */       
/*  47 */       EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/*     */     }
/*     */     else {
/*     */       
/*  51 */       entity = getEntity(sender, args[0]);
/*  52 */       i = 1;
/*     */     } 
/*     */     
/*  55 */     if (args.length != 1 && args.length != 2) {
/*     */       
/*  57 */       if (args.length < i + 3)
/*     */       {
/*  59 */         throw new WrongUsageException("commands.tp.usage", new Object[0]);
/*     */       }
/*  61 */       if (entity.worldObj != null)
/*     */       {
/*  63 */         int lvt_5_2_ = i + 1;
/*  64 */         CommandBase.CoordinateArg commandbase$coordinatearg = parseCoordinate(entity.posX, args[i], true);
/*  65 */         CommandBase.CoordinateArg commandbase$coordinatearg1 = parseCoordinate(entity.posY, args[lvt_5_2_++], 0, 0, false);
/*  66 */         CommandBase.CoordinateArg commandbase$coordinatearg2 = parseCoordinate(entity.posZ, args[lvt_5_2_++], true);
/*  67 */         CommandBase.CoordinateArg commandbase$coordinatearg3 = parseCoordinate(entity.rotationYaw, (args.length > lvt_5_2_) ? args[lvt_5_2_++] : "~", false);
/*  68 */         CommandBase.CoordinateArg commandbase$coordinatearg4 = parseCoordinate(entity.rotationPitch, (args.length > lvt_5_2_) ? args[lvt_5_2_] : "~", false);
/*     */         
/*  70 */         if (entity instanceof EntityPlayerMP) {
/*     */           
/*  72 */           Set<S08PacketPlayerPosLook.EnumFlags> set = EnumSet.noneOf(S08PacketPlayerPosLook.EnumFlags.class);
/*     */           
/*  74 */           if (commandbase$coordinatearg.func_179630_c())
/*     */           {
/*  76 */             set.add(S08PacketPlayerPosLook.EnumFlags.X);
/*     */           }
/*     */           
/*  79 */           if (commandbase$coordinatearg1.func_179630_c())
/*     */           {
/*  81 */             set.add(S08PacketPlayerPosLook.EnumFlags.Y);
/*     */           }
/*     */           
/*  84 */           if (commandbase$coordinatearg2.func_179630_c())
/*     */           {
/*  86 */             set.add(S08PacketPlayerPosLook.EnumFlags.Z);
/*     */           }
/*     */           
/*  89 */           if (commandbase$coordinatearg4.func_179630_c())
/*     */           {
/*  91 */             set.add(S08PacketPlayerPosLook.EnumFlags.X_ROT);
/*     */           }
/*     */           
/*  94 */           if (commandbase$coordinatearg3.func_179630_c())
/*     */           {
/*  96 */             set.add(S08PacketPlayerPosLook.EnumFlags.Y_ROT);
/*     */           }
/*     */           
/*  99 */           float f = (float)commandbase$coordinatearg3.func_179629_b();
/*     */           
/* 101 */           if (!commandbase$coordinatearg3.func_179630_c())
/*     */           {
/* 103 */             f = MathHelper.wrapAngleTo180_float(f);
/*     */           }
/*     */           
/* 106 */           float f1 = (float)commandbase$coordinatearg4.func_179629_b();
/*     */           
/* 108 */           if (!commandbase$coordinatearg4.func_179630_c())
/*     */           {
/* 110 */             f1 = MathHelper.wrapAngleTo180_float(f1);
/*     */           }
/*     */           
/* 113 */           if (f1 > 90.0F || f1 < -90.0F) {
/*     */             
/* 115 */             f1 = MathHelper.wrapAngleTo180_float(180.0F - f1);
/* 116 */             f = MathHelper.wrapAngleTo180_float(f + 180.0F);
/*     */           } 
/*     */           
/* 119 */           entity.mountEntity((Entity)null);
/* 120 */           ((EntityPlayerMP)entity).playerNetServerHandler.setPlayerLocation(commandbase$coordinatearg.func_179629_b(), commandbase$coordinatearg1.func_179629_b(), commandbase$coordinatearg2.func_179629_b(), f, f1, set);
/* 121 */           entity.setRotationYawHead(f);
/*     */         }
/*     */         else {
/*     */           
/* 125 */           float f2 = (float)MathHelper.wrapAngleTo180_double(commandbase$coordinatearg3.func_179628_a());
/* 126 */           float f3 = (float)MathHelper.wrapAngleTo180_double(commandbase$coordinatearg4.func_179628_a());
/*     */           
/* 128 */           if (f3 > 90.0F || f3 < -90.0F) {
/*     */             
/* 130 */             f3 = MathHelper.wrapAngleTo180_float(180.0F - f3);
/* 131 */             f2 = MathHelper.wrapAngleTo180_float(f2 + 180.0F);
/*     */           } 
/*     */           
/* 134 */           entity.setLocationAndAngles(commandbase$coordinatearg.func_179628_a(), commandbase$coordinatearg1.func_179628_a(), commandbase$coordinatearg2.func_179628_a(), f2, f3);
/* 135 */           entity.setRotationYawHead(f2);
/*     */         } 
/*     */         
/* 138 */         notifyOperators(sender, (ICommand)this, "commands.tp.success.coordinates", new Object[] { entity.getName(), Double.valueOf(commandbase$coordinatearg.func_179628_a()), Double.valueOf(commandbase$coordinatearg1.func_179628_a()), Double.valueOf(commandbase$coordinatearg2.func_179628_a()) });
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 143 */       Entity entity1 = getEntity(sender, args[args.length - 1]);
/*     */       
/* 145 */       if (entity1.worldObj != entity.worldObj)
/*     */       {
/* 147 */         throw new CommandException("commands.tp.notSameDimension", new Object[0]);
/*     */       }
/*     */ 
/*     */       
/* 151 */       entity.mountEntity((Entity)null);
/*     */       
/* 153 */       if (entity instanceof EntityPlayerMP) {
/*     */         
/* 155 */         ((EntityPlayerMP)entity).playerNetServerHandler.setPlayerLocation(entity1.posX, entity1.posY, entity1.posZ, entity1.rotationYaw, entity1.rotationPitch);
/*     */       }
/*     */       else {
/*     */         
/* 159 */         entity.setLocationAndAngles(entity1.posX, entity1.posY, entity1.posZ, entity1.rotationYaw, entity1.rotationPitch);
/*     */       } 
/*     */       
/* 162 */       notifyOperators(sender, (ICommand)this, "commands.tp.success", new Object[] { entity.getName(), entity1.getName() });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 170 */     return (args.length != 1 && args.length != 2) ? null : getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUsernameIndex(String[] args, int index) {
/* 175 */     return (index == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandTeleport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */