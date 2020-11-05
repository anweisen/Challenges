# Challenges
Free minecraft challenges plugin to play with friends

## Information
For more information you can visit our [SpigotMC-Site](https://www.spigotmc.org/resources/80548/), our [Website](https://coding-area.net) or our [Discord-Server](https://discord.gg/74Ay5zF)

## Own challenges
You can simply add your own challenges into our plugins on your own server following these steps:

### Setup
Download the newest version of our plugin from our [SpigotMC-Site](https://www.spigotmc.org/resources/80548/) and add the plugin as library to your project.
You don't want to export the library with your plugin afterwards, because you have the challenge plugin already on you server.

### Development
#### General
A challenge always has to implement [AbstractChallenge](https://github.com/anweisen/Challenges/blob/master/src/main/java/net/codingarea/challengesplugin/challengetypes/AbstractChallenge.java). <br>
Each [AbstractChallenge](https://github.com/anweisen/Challenges/blob/master/src/main/java/net/codingarea/challengesplugin/challengetypes/AbstractChallenge.java) has
- <code>handleClick(ChallengeEditEvent)</code>, this will be called when the challenge was clicked in the menu
- <code>getItem()</code>, the returned <code>ItemStack</code> will be used to display the challenge in the menu
- <code>getActivationItem()</code> the returned <code>ItemStack</code> will be displayed unter the challenge item in the menu to display the current state, updated on every click
- <code>setValues(int)</code>, this will be called to set the challenge values when a player loads its settings
- <code>toValue()</code>, the returned value will be saved into the database when a player save's its settings
- <code>getMenu()</code>, the challenge will be displayed in the menu of the returned <code>MenuType</code>

You can call <code>Challenges.timerIsStarted()</code>, which returns a <code>boolean</code>, to check if the challenge timer is started,

#### Creating challenges
##### Challenge types
There are a few preset types of challenges you may use:
- [Setting](https://github.com/anweisen/Challenges/blob/master/src/main/java/net/codingarea/challengesplugin/challengetypes/Setting.java)
  - This type can either be activated or disabled <br>
    This can be checked using the *protected boolean* <code>enabled</code>
- [Modifier](https://github.com/anweisen/Challenges/blob/master/src/main/java/net/codingarea/challengesplugin/challengetypes/Modifier.java)
  - This type has an *integer value* <br>
    This can be checked using the *protected int* <code>value</code>
  - It also has a *protected int* <code>minValue</code> and <code>maxValue</code> <br>
    The user can only set the value using the menu between the given values
- [Challenge](https://github.com/anweisen/Challenges/blob/master/src/main/java/net/codingarea/challengesplugin/challengetypes/Challenge.java)
  - This type can either be activated or disabled <br>
    This can be checked using the *protected boolean* <code>enabled</code>
  - This type also has an *protected int* <code>nextActivationSeconds</code> <br>
    When this value hits <code>0</code> the method <code>onTimeActivation</code> will be called <br>
    You have to manually set this value
- [AdvancedChallenge](https://github.com/anweisen/Challenges/blob/master/src/main/java/net/codingarea/challengesplugin/challengetypes/AdvancedChallenge.java)
  - This is a sub class of [Challenge](https://github.com/anweisen/Challenges/blob/master/src/main/java/net/codingarea/challengesplugin/challengetypes/Challenge.java) <br>
    This type has an *integer value* <code>value</code> like [Modifier](https://github.com/anweisen/Challenges/blob/master/src/main/java/net/codingarea/challengesplugin/challengetypes/Modifier.java) <br>
    <code>enabled</code> will be <code>false</code> if <code>value</code> is <code>0</code>
  - It also has a *protected int* <code>countUp</code> (default is <code>1</code>) <br>
    This value will be added or removed to/from the <code>value</code> integer
- [Goal](https://github.com/anweisen/Challenges/blob/master/src/main/java/net/codingarea/challengesplugin/challengetypes/Goal.java)
  - This type has a *protected boolean* <code>isCurrentGoal</code> <br>
  - The result of the *abstract* method <code>getWinners</code>, which is a List with Players, will be displayed in the chat when the challenge ends <br>
    You can manually end the challenge calling <code>ServerManager.simulateChallengeEnd(null, ChallengeEndCause.PLAYER_CHALLENGE_GOAL_REACHED)</code> when somebody finishes the current goal.
  - This type has a *protected ChallengeScoreboard* <code>scoreboard</code> which may be used to display the progress of each user  
- [AdvancedGoal](https://github.com/anweisen/Challenges/blob/master/src/main/java/net/codingarea/challengesplugin/challengetypes/AdvancedGoal.java)
  - This is as sub class if [Goal](https://github.com/anweisen/Challenges/blob/master/src/main/java/net/codingarea/challengesplugin/challengetypes/Goal.java) which is similar to [AdvancedChallenge](https://github.com/anweisen/Challenges/blob/master/src/main/java/net/codingarea/challengesplugin/challengetypes/AdvancedChallenge.java) is with [Challenge](https://github.com/anweisen/Challenges/blob/master/src/main/java/net/codingarea/challengesplugin/challengetypes/Challenge.java) <br>
    This type has an *integer value* <code>value</code> like [Modifier](https://github.com/anweisen/Challenges/blob/master/src/main/java/net/codingarea/challengesplugin/challengetypes/Modifier.java) <br>
    <code>enabled</code> will be <code>false</code> if <code>value</code> is <code>0</code>
  - It also has a *protected int* <code>countUp</code> (default is <code>1</code>) <br>
    This value will be added or removed to/from the <code>value</code> integer

##### Registering challenges
###### Main class
```java
public class ExamplePlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        ChallengeLoader.register(new ExampleChallenge(), this);
    }

}
```
###### Plugin yml
```yaml
depends:
- Challenges
```


##### Challenge examples

```java
public class ExampleSetting extends Setting implements Listener {
            
    public ExampleSetting() {
        // The setting will be displayed in the menu called 'Challenges'
        super(MenuType.CHALLENGES); 
    }

    @Override
    public void onEnabled(final @NotNull ChallengeEditEvent event) {
        Bukkit.broadcastMessage("The test setting was enabled");
    }

    @Override    
    public void onDisable(final @NotNull ChallengeEditEvent event) {
        Bukkit.broadcastMessage("The test setting was disabled");
    }

    @NotNull
    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.PAPER, "§fPaper").build();
    }

    @EventHandler
    public void onDamage(final @NotNull EntityDamageEvent event) {

        // Do nothing if the setting is not enabled or the timer is not started
        if (!enabled || !Challenges.timerIsStarted()) return;

        if (!(event.getEntity() instanceof Player)) return;
    
        Player player = (Player) event.getEntity();
        player.damage(player.getHealth());

    }

}
```

