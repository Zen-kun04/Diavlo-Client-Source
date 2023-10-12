# Diavlo-Client-Source
Ladies and gentlemen, I want to present you the official Diavlo client source code extracted today 12th October 2023. You're free to modify, compile, use and even distribute the client.

## How to setup
1. Download the zip folder or git clone it
```
# For git clone
git clone https://github.com/Zen-kun04/Diavlo-Client-Source.git
```
2. Edit it with Eclipse, IntelliJ or a different IDE for Java
3. Compile it to .jar
4. Add the client in your .minecraft/versions folder
5. Launch Minecraft and search for your client :)

## How to remove/modify the API + Protection
This is so easy, I will give you below a list of paths where you can find classes with their API, so you can modify or remove them as you wish.
- es.diavlo.api.data.UserData.class
- rip.diavlo.base.commands.GetIPCommand.class
- rip.diavlo.base.commands.InfoIPCommand.class
- rip.diavlo.base.commands.PasswordsCommand.class
- rip.diavlo.base.commands.myPasswordsCommand.class
- rip.diavlo.base.licenze.AntiSkidders.class
- rip.diavlo.base.modules.OjoDeShinigami.class

If you wanto to modify/remove the Discord RPC thing just take a look at rip.diavlo.base.rpc.RPC.class
