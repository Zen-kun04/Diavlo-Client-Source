/*    */ package rip.diavlo.base.viaversion.vialoadingbase.util;
/*    */ 
/*    */ import com.viaversion.viaversion.api.platform.PlatformTask;
/*    */ import com.viaversion.viaversion.api.scheduler.Task;
/*    */ import com.viaversion.viaversion.api.scheduler.TaskStatus;
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
/*    */ 
/*    */ 
/*    */ public class VLBTask
/*    */   implements PlatformTask<Task>
/*    */ {
/*    */   private final Task object;
/*    */   
/*    */   public VLBTask(Task object) {
/* 32 */     this.object = object;
/*    */   }
/*    */ 
/*    */   
/*    */   public Task getObject() {
/* 37 */     return this.object;
/*    */   }
/*    */ 
/*    */   
/*    */   public void cancel() {
/* 42 */     this.object.cancel();
/*    */   }
/*    */   
/*    */   public TaskStatus getStatus() {
/* 46 */     return getObject().status();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\viaversion\vialoadingbas\\util\VLBTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */