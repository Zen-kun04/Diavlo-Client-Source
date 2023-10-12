/*     */ package net.optifine.reflect;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.optifine.Log;
/*     */ 
/*     */ 
/*     */ public class ReflectorMethod
/*     */   implements IResolvable
/*     */ {
/*     */   private ReflectorClass reflectorClass;
/*     */   private String targetMethodName;
/*     */   private Class[] targetMethodParameterTypes;
/*     */   private boolean checked;
/*     */   private Method targetMethod;
/*     */   
/*     */   public ReflectorMethod(ReflectorClass reflectorClass, String targetMethodName) {
/*  19 */     this(reflectorClass, targetMethodName, (Class[])null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ReflectorMethod(ReflectorClass reflectorClass, String targetMethodName, Class[] targetMethodParameterTypes) {
/*  24 */     this.reflectorClass = null;
/*  25 */     this.targetMethodName = null;
/*  26 */     this.targetMethodParameterTypes = null;
/*  27 */     this.checked = false;
/*  28 */     this.targetMethod = null;
/*  29 */     this.reflectorClass = reflectorClass;
/*  30 */     this.targetMethodName = targetMethodName;
/*  31 */     this.targetMethodParameterTypes = targetMethodParameterTypes;
/*  32 */     ReflectorResolver.register(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Method getTargetMethod() {
/*  37 */     if (this.checked)
/*     */     {
/*  39 */       return this.targetMethod;
/*     */     }
/*     */ 
/*     */     
/*  43 */     this.checked = true;
/*  44 */     Class oclass = this.reflectorClass.getTargetClass();
/*     */     
/*  46 */     if (oclass == null)
/*     */     {
/*  48 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  54 */       if (this.targetMethodParameterTypes == null) {
/*     */         
/*  56 */         Method[] amethod = getMethods(oclass, this.targetMethodName);
/*     */         
/*  58 */         if (amethod.length <= 0) {
/*     */           
/*  60 */           Log.log("(Reflector) Method not present: " + oclass.getName() + "." + this.targetMethodName);
/*  61 */           return null;
/*     */         } 
/*     */         
/*  64 */         if (amethod.length > 1) {
/*     */           
/*  66 */           Log.warn("(Reflector) More than one method found: " + oclass.getName() + "." + this.targetMethodName);
/*     */           
/*  68 */           for (int i = 0; i < amethod.length; i++) {
/*     */             
/*  70 */             Method method = amethod[i];
/*  71 */             Log.warn("(Reflector)  - " + method);
/*     */           } 
/*     */           
/*  74 */           return null;
/*     */         } 
/*     */         
/*  77 */         this.targetMethod = amethod[0];
/*     */       }
/*     */       else {
/*     */         
/*  81 */         this.targetMethod = getMethod(oclass, this.targetMethodName, this.targetMethodParameterTypes);
/*     */       } 
/*     */       
/*  84 */       if (this.targetMethod == null) {
/*     */         
/*  86 */         Log.log("(Reflector) Method not present: " + oclass.getName() + "." + this.targetMethodName);
/*  87 */         return null;
/*     */       } 
/*     */ 
/*     */       
/*  91 */       this.targetMethod.setAccessible(true);
/*  92 */       return this.targetMethod;
/*     */     
/*     */     }
/*  95 */     catch (Throwable throwable) {
/*     */       
/*  97 */       throwable.printStackTrace();
/*  98 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/* 106 */     return this.checked ? ((this.targetMethod != null)) : ((getTargetMethod() != null));
/*     */   }
/*     */ 
/*     */   
/*     */   public Class getReturnType() {
/* 111 */     Method method = getTargetMethod();
/* 112 */     return (method == null) ? null : method.getReturnType();
/*     */   }
/*     */ 
/*     */   
/*     */   public void deactivate() {
/* 117 */     this.checked = true;
/* 118 */     this.targetMethod = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object call(Object... params) {
/* 123 */     return Reflector.call(this, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean callBoolean(Object... params) {
/* 128 */     return Reflector.callBoolean(this, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public int callInt(Object... params) {
/* 133 */     return Reflector.callInt(this, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public float callFloat(Object... params) {
/* 138 */     return Reflector.callFloat(this, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public double callDouble(Object... params) {
/* 143 */     return Reflector.callDouble(this, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public String callString(Object... params) {
/* 148 */     return Reflector.callString(this, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object call(Object param) {
/* 153 */     return Reflector.call(this, new Object[] { param });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean callBoolean(Object param) {
/* 158 */     return Reflector.callBoolean(this, new Object[] { param });
/*     */   }
/*     */ 
/*     */   
/*     */   public int callInt(Object param) {
/* 163 */     return Reflector.callInt(this, new Object[] { param });
/*     */   }
/*     */ 
/*     */   
/*     */   public float callFloat(Object param) {
/* 168 */     return Reflector.callFloat(this, new Object[] { param });
/*     */   }
/*     */ 
/*     */   
/*     */   public double callDouble(Object param) {
/* 173 */     return Reflector.callDouble(this, new Object[] { param });
/*     */   }
/*     */ 
/*     */   
/*     */   public String callString1(Object param) {
/* 178 */     return Reflector.callString(this, new Object[] { param });
/*     */   }
/*     */ 
/*     */   
/*     */   public void callVoid(Object... params) {
/* 183 */     Reflector.callVoid(this, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Method getMethod(Class cls, String methodName, Class[] paramTypes) {
/* 188 */     Method[] amethod = cls.getDeclaredMethods();
/*     */     
/* 190 */     for (int i = 0; i < amethod.length; i++) {
/*     */       
/* 192 */       Method method = amethod[i];
/*     */       
/* 194 */       if (method.getName().equals(methodName)) {
/*     */         
/* 196 */         Class[] aclass = method.getParameterTypes();
/*     */         
/* 198 */         if (Reflector.matchesTypes(paramTypes, aclass))
/*     */         {
/* 200 */           return method;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 205 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Method[] getMethods(Class cls, String methodName) {
/* 210 */     List<Method> list = new ArrayList();
/* 211 */     Method[] amethod = cls.getDeclaredMethods();
/*     */     
/* 213 */     for (int i = 0; i < amethod.length; i++) {
/*     */       
/* 215 */       Method method = amethod[i];
/*     */       
/* 217 */       if (method.getName().equals(methodName))
/*     */       {
/* 219 */         list.add(method);
/*     */       }
/*     */     } 
/*     */     
/* 223 */     Method[] amethod1 = list.<Method>toArray(new Method[list.size()]);
/* 224 */     return amethod1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resolve() {
/* 229 */     Method method = getTargetMethod();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\reflect\ReflectorMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */