/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.profiler.Profiler;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class EntityAITasks
/*     */ {
/*  12 */   private static final Logger logger = LogManager.getLogger();
/*  13 */   private List<EntityAITaskEntry> taskEntries = Lists.newArrayList();
/*  14 */   private List<EntityAITaskEntry> executingTaskEntries = Lists.newArrayList();
/*     */   private final Profiler theProfiler;
/*     */   private int tickCount;
/*  17 */   private int tickRate = 3;
/*     */ 
/*     */   
/*     */   public EntityAITasks(Profiler profilerIn) {
/*  21 */     this.theProfiler = profilerIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addTask(int priority, EntityAIBase task) {
/*  26 */     this.taskEntries.add(new EntityAITaskEntry(priority, task));
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeTask(EntityAIBase task) {
/*  31 */     Iterator<EntityAITaskEntry> iterator = this.taskEntries.iterator();
/*     */     
/*  33 */     while (iterator.hasNext()) {
/*     */       
/*  35 */       EntityAITaskEntry entityaitasks$entityaitaskentry = iterator.next();
/*  36 */       EntityAIBase entityaibase = entityaitasks$entityaitaskentry.action;
/*     */       
/*  38 */       if (entityaibase == task) {
/*     */         
/*  40 */         if (this.executingTaskEntries.contains(entityaitasks$entityaitaskentry)) {
/*     */           
/*  42 */           entityaibase.resetTask();
/*  43 */           this.executingTaskEntries.remove(entityaitasks$entityaitaskentry);
/*     */         } 
/*     */         
/*  46 */         iterator.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdateTasks() {
/*  53 */     this.theProfiler.startSection("goalSetup");
/*     */     
/*  55 */     if (this.tickCount++ % this.tickRate == 0) {
/*     */       
/*  57 */       Iterator<EntityAITaskEntry> iterator = this.taskEntries.iterator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  66 */       while (iterator.hasNext()) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  71 */         EntityAITaskEntry entityaitasks$entityaitaskentry = iterator.next();
/*  72 */         boolean flag = this.executingTaskEntries.contains(entityaitasks$entityaitaskentry);
/*     */         
/*  74 */         if (flag)
/*     */         {
/*     */ 
/*     */ 
/*     */           
/*  79 */           if (!canUse(entityaitasks$entityaitaskentry) || !canContinue(entityaitasks$entityaitaskentry)) {
/*     */             
/*  81 */             entityaitasks$entityaitaskentry.action.resetTask();
/*  82 */             this.executingTaskEntries.remove(entityaitasks$entityaitaskentry);
/*     */           } else {
/*     */             continue;
/*     */           } 
/*     */         }
/*  87 */         if (canUse(entityaitasks$entityaitaskentry) && entityaitasks$entityaitaskentry.action.shouldExecute())
/*     */         {
/*  89 */           entityaitasks$entityaitaskentry.action.startExecuting();
/*  90 */           this.executingTaskEntries.add(entityaitasks$entityaitaskentry);
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/*  96 */       Iterator<EntityAITaskEntry> iterator1 = this.executingTaskEntries.iterator();
/*     */       
/*  98 */       while (iterator1.hasNext()) {
/*     */         
/* 100 */         EntityAITaskEntry entityaitasks$entityaitaskentry1 = iterator1.next();
/*     */         
/* 102 */         if (!canContinue(entityaitasks$entityaitaskentry1)) {
/*     */           
/* 104 */           entityaitasks$entityaitaskentry1.action.resetTask();
/* 105 */           iterator1.remove();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 110 */     this.theProfiler.endSection();
/* 111 */     this.theProfiler.startSection("goalTick");
/*     */     
/* 113 */     for (EntityAITaskEntry entityaitasks$entityaitaskentry2 : this.executingTaskEntries)
/*     */     {
/* 115 */       entityaitasks$entityaitaskentry2.action.updateTask();
/*     */     }
/*     */     
/* 118 */     this.theProfiler.endSection();
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canContinue(EntityAITaskEntry taskEntry) {
/* 123 */     boolean flag = taskEntry.action.continueExecuting();
/* 124 */     return flag;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canUse(EntityAITaskEntry taskEntry) {
/* 129 */     for (EntityAITaskEntry entityaitasks$entityaitaskentry : this.taskEntries) {
/*     */       
/* 131 */       if (entityaitasks$entityaitaskentry != taskEntry) {
/*     */         
/* 133 */         if (taskEntry.priority >= entityaitasks$entityaitaskentry.priority) {
/*     */           
/* 135 */           if (!areTasksCompatible(taskEntry, entityaitasks$entityaitaskentry) && this.executingTaskEntries.contains(entityaitasks$entityaitaskentry))
/*     */           {
/* 137 */             return false; } 
/*     */           continue;
/*     */         } 
/* 140 */         if (!entityaitasks$entityaitaskentry.action.isInterruptible() && this.executingTaskEntries.contains(entityaitasks$entityaitaskentry))
/*     */         {
/* 142 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 147 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean areTasksCompatible(EntityAITaskEntry taskEntry1, EntityAITaskEntry taskEntry2) {
/* 152 */     return ((taskEntry1.action.getMutexBits() & taskEntry2.action.getMutexBits()) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   class EntityAITaskEntry
/*     */   {
/*     */     public EntityAIBase action;
/*     */     public int priority;
/*     */     
/*     */     public EntityAITaskEntry(int priorityIn, EntityAIBase task) {
/* 162 */       this.priority = priorityIn;
/* 163 */       this.action = task;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAITasks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */