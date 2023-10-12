/*     */ package net.minecraft.command.server;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.command.CommandBase;
/*     */ import net.minecraft.command.CommandException;
/*     */ import net.minecraft.command.ICommand;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.command.WrongUsageException;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.effect.EntityLightningBolt;
/*     */ import net.minecraft.nbt.JsonToNBT;
/*     */ import net.minecraft.nbt.NBTException;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class CommandSummon
/*     */   extends CommandBase {
/*     */   public String getCommandName() {
/*  25 */     return "summon";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  30 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  35 */     return "commands.summon.usage";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  40 */     if (args.length < 1)
/*     */     {
/*  42 */       throw new WrongUsageException("commands.summon.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  46 */     String s = args[0];
/*  47 */     BlockPos blockpos = sender.getPosition();
/*  48 */     Vec3 vec3 = sender.getPositionVector();
/*  49 */     double d0 = vec3.xCoord;
/*  50 */     double d1 = vec3.yCoord;
/*  51 */     double d2 = vec3.zCoord;
/*     */     
/*  53 */     if (args.length >= 4) {
/*     */       
/*  55 */       d0 = parseDouble(d0, args[1], true);
/*  56 */       d1 = parseDouble(d1, args[2], false);
/*  57 */       d2 = parseDouble(d2, args[3], true);
/*  58 */       blockpos = new BlockPos(d0, d1, d2);
/*     */     } 
/*     */     
/*  61 */     World world = sender.getEntityWorld();
/*     */     
/*  63 */     if (!world.isBlockLoaded(blockpos))
/*     */     {
/*  65 */       throw new CommandException("commands.summon.outOfWorld", new Object[0]);
/*     */     }
/*  67 */     if ("LightningBolt".equals(s)) {
/*     */       
/*  69 */       world.addWeatherEffect((Entity)new EntityLightningBolt(world, d0, d1, d2));
/*  70 */       notifyOperators(sender, (ICommand)this, "commands.summon.success", new Object[0]);
/*     */     } else {
/*     */       Entity entity2;
/*     */       
/*  74 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  75 */       boolean flag = false;
/*     */       
/*  77 */       if (args.length >= 5) {
/*     */         
/*  79 */         IChatComponent ichatcomponent = getChatComponentFromNthArg(sender, args, 4);
/*     */ 
/*     */         
/*     */         try {
/*  83 */           nbttagcompound = JsonToNBT.getTagFromJson(ichatcomponent.getUnformattedText());
/*  84 */           flag = true;
/*     */         }
/*  86 */         catch (NBTException nbtexception) {
/*     */           
/*  88 */           throw new CommandException("commands.summon.tagError", new Object[] { nbtexception.getMessage() });
/*     */         } 
/*     */       } 
/*     */       
/*  92 */       nbttagcompound.setString("id", s);
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  97 */         entity2 = EntityList.createEntityFromNBT(nbttagcompound, world);
/*     */       }
/*  99 */       catch (RuntimeException var19) {
/*     */         
/* 101 */         throw new CommandException("commands.summon.failed", new Object[0]);
/*     */       } 
/*     */       
/* 104 */       if (entity2 == null)
/*     */       {
/* 106 */         throw new CommandException("commands.summon.failed", new Object[0]);
/*     */       }
/*     */ 
/*     */       
/* 110 */       entity2.setLocationAndAngles(d0, d1, d2, entity2.rotationYaw, entity2.rotationPitch);
/*     */       
/* 112 */       if (!flag && entity2 instanceof EntityLiving)
/*     */       {
/* 114 */         ((EntityLiving)entity2).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity2)), (IEntityLivingData)null);
/*     */       }
/*     */       
/* 117 */       world.spawnEntityInWorld(entity2);
/* 118 */       Entity entity = entity2;
/*     */       
/* 120 */       for (NBTTagCompound nbttagcompound1 = nbttagcompound; entity != null && nbttagcompound1.hasKey("Riding", 10); nbttagcompound1 = nbttagcompound1.getCompoundTag("Riding")) {
/*     */         
/* 122 */         Entity entity1 = EntityList.createEntityFromNBT(nbttagcompound1.getCompoundTag("Riding"), world);
/*     */         
/* 124 */         if (entity1 != null) {
/*     */           
/* 126 */           entity1.setLocationAndAngles(d0, d1, d2, entity1.rotationYaw, entity1.rotationPitch);
/* 127 */           world.spawnEntityInWorld(entity1);
/* 128 */           entity.mountEntity(entity1);
/*     */         } 
/*     */         
/* 131 */         entity = entity1;
/*     */       } 
/*     */       
/* 134 */       notifyOperators(sender, (ICommand)this, "commands.summon.success", new Object[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 142 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, EntityList.getEntityNameList()) : ((args.length > 1 && args.length <= 4) ? func_175771_a(args, 1, pos) : null);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandSummon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */