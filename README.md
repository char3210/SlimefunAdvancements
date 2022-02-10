# SlimefunAdvancements

a slimefun addon that lets you do things like complete and configure advancements

[download here](https://thebusybiscuit.github.io/builds/qwertyuioplkjhgfd/SlimefunAdvancements/main/)

## Configuration

The configuration files can be found in your `plugins/SFAdvancements/` folder.

### groups.yml

Each item in the yml represents an advancement group, where the key is the key of the group.<br>
The key is used to refer to the group in `advancements.yml`.<br>
Each group has a `display`, which is an item. It should be an item representation.

### Item Representation

You can represent an item in a few ways.<br>
1. just a string, the id of the item (either [vanilla material](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html) or [slimefun item id](https://sf-items.walshy.dev/))
2. an object with a string `type` (the id of the item), optional string `name`, and optional string list `lore`.
3. a serialized representaion of the item

You can generate representations of an item by holding the item in your hand in-game and typing `/sfa dumpitem`.<br>
The results will be displayed in console.

Examples of #1 in `groups.yml`
```yaml
my_cool_group:
  display: NETHER_STAR

my_other_group:
  display: ELECTRIC_MOTOR
```

Examples of #2 in `groups.yml`
```yaml
basic:
  display:
    type: SLIME_BALL
    name: "&fBasic"
    lore:
      - "&7&oThe core spirit of Slimefun."

electric:
  display:
    type: REDSTONE
    name: "&eElectric"
    lore:
      - "&7&oThe center of civilization."
```

Example of #3 in `groups.yml`
```yaml
hi:
  display:
    ==: org.bukkit.inventory.ItemStack
    v: 2865
    type: IRON_INGOT
    meta:
      ==: ItemMeta
      meta-type: UNSPECIFIC
      display-name: '{"extra":[{"bold":false,"italic":false,"underlined":false,"strikethrough":false,"obfuscated":false,"color":"aqua","text":"Zinc
        Ingot"}],"text":""}'
      PublicBukkitValues:
        slimefun:slimefun_item: ZINC_INGOT
```

### advancements.yml

This is where all your advancements will go.<br>
Each item represents an advancement, where the key is the key of the advancement.<br>
(It is stored as a NamespacedKey `sfadvancements:<key>`)<br>
An advancement contains a group, display, name, criteria, and optional rewards.<br>

The group is the id defined in `groups.yml`.

The display is an item, represented as described in Item Representation.

The name is what will appear in chat when someone completes the advancement.

(the display and name support color codes with `&`)

The criteria are a section, where each item is a criterion and the key is the criterion key.

#### criterion

The key of the criterion should be unique per advancement (but may share keys in different advancements).<br>
The string itself doesn't matter, but if no name is specified, it will use the key.<br>
Each criterion has a name, type, and optional other parameters.

The name is what appears in the gui for progress. 

The type is the type of criterion. By default, these are the default criterion types:
- `consume`
  - for eating items
  - has an item parameter `item`, which is the item to consume
  - note that this only works for vanilla consumption, NOT exotic garden fruits for example, use `interact` for that
  - has an integer parameter `amount`, the number of items to consume
- `interact`
  - for right-clicking items
  - has an item parameter `item`, which is the item to be right-clicked
  - has an integer parameter `amount`, the number of times to interact
- `inventory`
  - for having an item in an inventory
  - has an item parameter `item`, the item to have in the inventory
  - this does not check amount, it will trigger when a player has a single item (amount coming soon)
- `multiblock`
  - for interacting with a slimefun multiblock
  - has a string parameter `multiblock`, which is the slimefun item id of the multiblock
- `place`
  - for placing blocks
  - has an item parameter `item`, which is the item to place down
  - has an integer parameter `amount`, which is the number of items to place
  - note that there is no protection against players repeatedly breaking and replacing the block, so for most items just use one for the amount
- `research`
  - for completing a research
  - has a string parameter `research`, which is the namespaced key of the research
    - namespaced keys have the format "plugin:key", so for slimefun researches, it is "slimefun:research"
      - ex: "slimefun:ender_talismans"

#### rewards

The rewards have a different section for different types.<br>
(for now, the only type is `commands`)<br>
The name of the section determines the type of reward.

Reward types:
- command
  - is a string list, with each line being a command to run
  - you can refer to the name of the player that completed the advancement via `%p%`

## Permissions

`sfa.command.<command name>`: allows the user to use the command

## Custom Criteria (developers)

see [api.md](https://github.com/qwertyuioplkjhgfd/SlimefunAdvancements/blob/main/api.md)

## TODO:
- ~~criteria system~~
  - ~~inventory criteria~~
  - craft criteria (soon, see [Slimefun/Slimefun4#3439](https://github.com/Slimefun/Slimefun4/pull/3439))
  - ~~interact criteria~~
      - ~~place criteria~~
  - ~~research criteria~~
- ~~configurability~~
- rewards
- add advancements
- ~~permissions~~
- load default advancements (from other plugins)
- better readme, .github, ~~builds page~~
- tree
- advancements api (crazy)
- cheat menu
- docs
