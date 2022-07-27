<img src="img/logo.png" alt="Armor Render Lib" height="100" />
<hr />

[![GitHub license](https://img.shields.io/github/license/CleverNucleus/ArmorRenderLib?style=flat-square)](https://github.com/CleverNucleus/ArmorRenderLib/blob/main/LICENSE)
[![GitHub stars](https://img.shields.io/github/stars/CleverNucleus/ArmorRenderLib?style=flat-square)](https://github.com/CleverNucleus/ArmorRenderLib/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/CleverNucleus/ArmorRenderLib?style=flat-square)](https://github.com/CleverNucleus/ArmorRenderLib/network)
[![GitHub issues](https://img.shields.io/github/issues/CleverNucleus/ArmorRenderLib?style=flat-square)](https://github.com/CleverNucleus/ArmorRenderLib/issues)

Armor Render Lib is a lightweight extension library to Fabric API's fabric-rendering-v1 [ArmorRenderer](https://github.com/FabricMC/fabric/tree/1.18.2/fabric-rendering-v1). Since the Fabric API implementation is quite abstract, a more targeted implementation is needed for some specific use cases. These use cases are present across more than one of my mods, so in the spirit of code reuse this library was created. It is robust, using only a few very targeted mixins and is fully compatible with Cosmetic Armor and GeckoLib.

### Use

Armor Render Lib adds armor render layers. These are objects containing a dynamic texture location, color and glint boolean that render armor for an item (or items). They are roughly equivalent to Fabric API's [ArmorRenderer#renderPart](https://github.com/FabricMC/fabric/blob/f14603e8624d4cb192846321c429cc00c9ef6f55/fabric-rendering-v1/src/main/java/net/fabricmc/fabric/api/client/rendering/v1/ArmorRenderer.java#L69), but the texture, color and glint can be dynamically specified based on the `ItemStack`, `LivingEntity` and `EquipmentSlot`.

They should be registered like so:

```java
public class ExampleMod implements ClientModInitializer {
    private static ArmorRenderProvider render(ItemStack stack, LivingEntity entity, EquipmentSlot slot) {
        // Dynamic texture path
        String texture = "examplemod:textures/model/armor/example_chestplate.png";
        
        // Dynamic color
        int color = 0xFF00FF;
        
        return data -> data.accept(texture, color, stack.hasGlint());
    }
    
    @Override
    public void onInitializeClient() {
        ArmorRenderLib.register(ExampleMod::render, Items.IRON_CHESTPLATE);
    }
}
```

### Notes

- An item can have multiple different armor render layers registered to it.
- Multiple items can have the same armor render layer registered to them.
- Registering a render layer to a vanilla item (or other armor item) overrides the default armor rendering. 
  - This means that the texture, color and/or enchantment glint of vanilla armor can be modified.