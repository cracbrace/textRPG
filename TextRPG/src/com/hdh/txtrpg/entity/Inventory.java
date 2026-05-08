package com.hdh.txtrpg.entity;


import java.util.ArrayList;
import java.util.List;

//背包类：管理道具列表
public class Inventory {
    //声明时用接口，实现时用具体实例类
    private List<Item> items = new ArrayList<>() ;

    //添加道具
    public void addItem (Item item) {       //传入接口的实例对象
        items.add(item);
        System.out.println("📦 获得道具：【" + item.getName() + "】");
    }
    //使用道具
    public void useItem (int index,Character user) {
        if (index >= 0 && index < items.size()) {
            Item itemToUse = items.get(index);
            itemToUse.use(user);
            items.remove(index);// 使用后从背包中移除
            System.out.println("✅ 使用道具：【" + itemToUse.getName() + "】");
        } else {
            System.out.println("❌ 道具索引无效！");
        }
    }

    // 展示所有道具
    public void displayItems() {
        if (items.isEmpty()) {
            System.out.println("📦 背包为空！");
            return;
        }
        System.out.println("===== 背包道具 =====");
        for (int i = 0; i < items.size(); i++) {
            System.out.println(i + ". " + items.get(i).getName());
        }
    }

    //获取道具列表长度
    public int getItemCount() {
        return items.size();
    }
}
