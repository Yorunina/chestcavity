# Branch Info

> Warning! If you use this branch, you will be treated knowing that contains lots of testing content and unfix bugs by
> default. Please DO NOT post all these bugs to the author or the issue forum unless you know how to fix or the version
> you use is an already released version.

# Chest Cavity

Chest Cavity lets you use a 'chest opener' to access a secondary inventory. There are some items already in it, but
those probably aren't important so you can take them out to make room for other stuff\*.

It also adds a variety of drops that are similar to the junk you discarded from your extra storage.

*Side effects may include shortness of breath, inhibited healing, inability to fight off infection, vulnerability to
physical harm, weakness, loss of nutrition, difficulty walking or running, and sacrifice to Aztec gods.*

## Requirements

Requires Minecraft 1.16.x and Fabric API.

Also uses Onyx Studio's Cardinal Components.

## KubeJS organ catalogue filters

The organ catalogue exposes a client-side KubeJS event for configuring the
item tags shown in its rightmost dropdown:

```js
// kubejs/client_scripts/chestcavity.js
ChestCavityEvents.organHoloFilterTags(event => {
  event.addTag('forge:ores', 'Ores')
  event.addTag('c:ingots', 'Ingots')
})
```

The first argument is an item tag id. Items in the catalogue are shown when
they belong to the selected tag. The label is optional; when omitted, the tag
id is used. `event.clear()` and `event.removeTag('namespace:tag')` can be used
to edit the list before the dropdown is built.

## Images

![2_2_1 Organs](https://user-images.githubusercontent.com/12503726/99457027-2cd66c00-28df-11eb-9036-44a90c9e2b4c.png)

![2_2_1 Basic Crafting](https://user-images.githubusercontent.com/12503726/99456311-0d8b0f00-28de-11eb-83d2-923cfb350691.png)

![2_2_1 Cooking](https://user-images.githubusercontent.com/12503726/99456319-111e9600-28de-11eb-95e3-5be74ab77381.png)

![2_2_1 Sausage](https://user-images.githubusercontent.com/12503726/99456341-167be080-28de-11eb-9f44-04e412aa41dd.png)

![Example Chest Cavities](https://user-images.githubusercontent.com/12503726/99141626-fc05e680-2601-11eb-8437-a07a02188096.png)

