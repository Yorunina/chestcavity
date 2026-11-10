package net.tigereye.chestcavity.chestcavities.instance;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 上一次正常胸腔重算的库存快照；查询不读取库存，无结算写入不刷新。 */
public final class OrganQueryIndex {
    private final Map<String, List<Slot>> items = new HashMap<>();
    private final Map<Integer, String> slotTypes = new HashMap<>();
    private final Map<String, Integer> tagCounts = new HashMap<>();

    private record Slot(int index, String type, int count) {
        private boolean matches(boolean installedOnly, String slotType) {
            return (!installedOnly || !"container_slot".equals(type))
                    && (slotType == null || slotType.equals(type));
        }
    }

    public void rebuild(ChestCavityInventory inventory, InventoryTypeData inventoryType) {
        items.clear();
        slotTypes.clear();
        tagCounts.clear();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            String slotType = inventoryType.getSlotType(i);
            slotTypes.put(i, slotType);
            ItemStack stack = inventory.getItem(i);
            if (stack.isEmpty()) continue;
            String itemId = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString();
            int count = stack.getCount();
            items.computeIfAbsent(itemId, ignored -> new ArrayList<>()).add(new Slot(i, slotType, count));
            stack.getTags().forEach(tag -> tagCounts.merge(tag.location().toString(), count, Integer::sum));
        }
    }

    /** installedOnly 排除 container_slot；slotType 为 null 时不限制槽位类型。 */
    public boolean hasItem(String itemId, boolean installedOnly, String slotType) {
        return findItem(itemId, installedOnly, slotType) >= 0;
    }

    /** 返回首个匹配格号，不创建位置列表；未找到返回 -1。 */
    public int findItem(String itemId, boolean installedOnly, String slotType) {
        for (Slot slot : items.getOrDefault(itemId, List.of())) {
            if (slot.matches(installedOnly, slotType)) return slot.index();
        }
        return -1;
    }

    /** 返回升序格号的新列表，首项与全库存 find 的查找顺序一致。 */
    public List<Integer> getItemSlots(String itemId, boolean installedOnly, String slotType) {
        List<Integer> result = new ArrayList<>();
        for (Slot slot : items.getOrDefault(itemId, List.of())) {
            if (slot.matches(installedOnly, slotType)) result.add(slot.index());
        }
        return result;
    }

    public int getItemSlotCount(String itemId, boolean installedOnly, String slotType) {
        int count = 0;
        for (Slot slot : items.getOrDefault(itemId, List.of())) {
            if (slot.matches(installedOnly, slotType)) count++;
        }
        return count;
    }

    public int getItemCount(String itemId, boolean installedOnly, String slotType) {
        int count = 0;
        for (Slot slot : items.getOrDefault(itemId, List.of())) {
            if (slot.matches(installedOnly, slotType)) count += slot.count();
        }
        return count;
    }

    public boolean hasItemAt(String itemId, int index) {
        for (Slot slot : items.getOrDefault(itemId, List.of())) {
            if (slot.index() == index) return true;
        }
        return false;
    }

    public String getSlotType(int index) {
        return slotTypes.getOrDefault(index, "");
    }

    /** 全部库存的标签件数，包含 container_slot；tagId 不带 #。 */
    public int getTagCount(String tagId) {
        return tagCounts.getOrDefault(tagId, 0);
    }
}
