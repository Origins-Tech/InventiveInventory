# Inventive Inventory
**[Discord](https://discord.gg/uycTMfCsfc) | [GitHub](https://github.com/InventiveMods/inventive-inventory) | [Modrinth](https://modrinth.com/mod/inventive-inventory) | [Curseforge](https://www.curseforge.com/minecraft/mc-mods/inventive-inventory)**

Experience seamless inventory management in Minecraft with Inventive Inventory, your one-stop solution for sorting, auto-refilling, profiles and slot-locking.

**⚙️ -> Configurable Option**

**DISCLAIMER: Future features will only find their way into 1.21 version and above! Bugs will still be fixed in older versions!**

## Features 🚀💡
### Sorting 🔀🔠
There's a mess in your inventory? No problem, a simple key press (Default: R)⚙️ will it get organized.

The sorting function is aware of the cursor’s position.  
This means that if you’re in a container and hover over your inventory, the inventory will be sorted instead of the container.  
In all other scenarios, the container will be sorted.

By pressing the Advanced Operation Key you can keep your cursor stack while sorting. ⚙️
> This feature is compatible with all types of containers.

### Locked Slots 🔒🚫
You can lock slots in your inventory by holding the Advanced Operation Key (Default: Alt) and clicking on the slot.  
Items in such a slot will not be affected by sorting, automatic refilling or profile loading. ⚙️ (for every feature seperatley)

Additionally, picked-up items or quick-moved won't be placed in such a slot either and will rather get dropped. ⚙️

### Profiles 📋📂
Save your current hotbar and offhand state and load it anytime back.

<details>
  <summary>Profile Screen (Radial Menu)</summary>
  
By pressing and holding 'V'⚙️ you can open the Profile Screen where you can create, load, overwrite or delete profiles.  
- Create: Just hover over '+' and release 'V'⚙️. A Profile should be created. (Optionally, you can press ALT additionally to name your profile)
- Overwrite: Hover over an existing profile, hold Left CTRL and release 'V'⚙️ to overwrite this profile.
- Delete: Hover over an existing profile, hold ALT and release 'V'⚙️ to delete this profile.

</details>

<details>
<summary>Commands</summary>

- Save: ```/inventive_inventory profiles create <name> [keybinding]```
- Load: ```/inventive_inventory profiles load <profile>```
- Delete: ```/inventive_inventory profiles delete <profile>```

</details>

<details>
<summary>Keybinds</summary>

You can bind customizable keys to your profiles for loading.
- Loading: Loading Key (Default: ALT)⚙️ + [Profile Key (Default: 1-3)⚙️]

Alternatively, you can activate Fast Load where you don't have to press the loading key. ⚙️

</details>

View and update your created profiles in the Profile Tab of the Config Screen.  
You can access the Config Screen with this command ```/inventive_inventory config``` or via [ModMenu](https://modrinth.com/mod/modmenu)

### Automatic Refilling 🔄📦
Replenishes your current stack with items from your inventory.  
Useful for building with many materials or just to always have some food ready.  
Works with buckets, bowls and bottles as well as tools.  
You can press the Advanced Operation Key (Default: ALT)⚙️ to prevent the stack from refilling.⚙️  

**If you have more feature suggestions or found a bug, join our [Discord](https://discord.gg/uycTMfCsfc) or open an issue on the [GitHub Repository](https://github.com/InventiveMods/inventive-inventory/issues)!**
