package rip.diavlo.base.api.command;

import rip.diavlo.base.utils.MinecraftUtil;

public interface Command extends MinecraftUtil {
  String[] getAliases();
  
  void execute(String[] paramArrayOfString) throws CommandExecutionException;
  
  String getUsage();
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\api\command\Command.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */