# Custom Criteria

you can look at a [reference implementation](https://github.com/qwertyuioplkjhgfd/ExampleCriteria)

## Adding SFA as a dependency
so the first thing you want to do is add SFA as a dependency<br>
in your `pom.xml`, add jitpack as a repository (if you haven't already) and add the SFA dependency<br>
you should maybe use the latest commit

```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>com.github.slimefun</groupId>
            <artifactId>slimefun4</artifactId>
            <version>RC-30</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.github.qwertyuioplkjhgfd</groupId>
            <artifactId>SlimefunAdvancements</artifactId>
            <version>bb966ea180</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
```

then in your `plugin.yml`, add `SFAdvancements` as a dependency
```yaml
depend: [ Slimefun, SFAdvancements ]
```

## Extending Criterion

Create a class that extends `me.char321.sfadvancements.api.criteria.Criterion`<br>
Add a constructor that calls a super constructor.<br>
Choose one that accepts a name parameter please<br>
Amount defaults to one, it's the number of times the criterion needs to be performed by a player to complete it.<br>
You can then add any extra parameters, fields, and getters that you want your criterion requirements to hold.<br>
Criterion objects are per yml object as defined in advancements.yml

## Implementing CriterionCompleter

Then you want to have a CriterionCompleter that completes your criterion type.<br>
Create a class that implements `me.char321.sfadvancements.core.criteria.completer.CriterionCompleter`<br>
`getCriterionClass()` should return the class of the criterion you just created.<br>
`register(Criterion)` is called whenever a criterion is loaded of type `getCriterionClass()`,
so store the incoming criteria in a collection or map or something.

This is where you would complete your criteria.<br>
You can add a listener or just a method that is called elsewhere whenever someone performs your criteria.<br>
Then, just call `Criterion#perform(Player p)`, and that will increment the player's progress for that criteria (and complete it if it was completed).

## Registering 

To register your completer, just create a new instance and call `register()` (without any parameters).

To register your criteria type (and make it configurable), call `CriteriaTypes.putType(String, Function<ConfigurationSection, Criterion>)`.

The string is the type of criteria that is specified in `advancements.yml`:
```yaml
myadvancement:
  ...
  criteria:
    mycriterion:
      name: ...
      type: <this string>
      ...
```

The function is what turns a configuration section to a criterion object.<br>
It refers to the `mycriterion` section in the example above.<br>
This way, you can customize how your criterion is configured.<br>

The function will likely look something like this:
```java
config -> {
    String id = config.getName();
    
    // optional
    int amount = config.getInt("amount");
    if (amount == 0) { // if no amount is specified
        amount = 1;
    }
    
    // boilerplate
    String name = config.getString("name");
    if(name == null) {
        name = id;
    }
    
    // this is where you can add custom configurability
    double chance = config.getDouble("chance");
    // if you want to get itemstacks, you can use ConfigUtils.getItem(config, path)
    
    // return a criterion from the config
    return new RandomCriterion(id, amount, name, chance);
}
```

## Adding your own default advancements

Adding your own advancements are very simple. <br>
Simply include files called `sfgroups.yml` and `sfadvancements.yml` which define your custom groups and advancements, respectively.<br>
It is recommended that any groups you use in `sfadvancements.yml` are defined in `sfgroups.yml`, because if the group does not exist, the advancement will not appear.<br>
Now, to add these advancements , just type `/sfa import <plugin name>`, and they should be imported!<br>
(Any groups and advancements that have the same key as an existing group/advancement will not be imported.)

And that's it!

Once again, you can always look at a [reference implementation](https://github.com/qwertyuioplkjhgfd/ExampleCriteria). <br>
If you're still confused, feel free to ask in the [Slimefun Addon Community discord server](https://discord.gg/SqD3gg5SAU).