package com.hdh.txtrpg;

import com.hdh.txtrpg.engine.BattleEngine;
import com.hdh.txtrpg.entity.*;
import com.hdh.txtrpg.util.MapConstant;

import java.util.Scanner;

import static com.hdh.txtrpg.util.DirectionConstant.getDirByKey;

public class MainGame {

    // 游戏全局变量
    private Player player;
    private Location currentLocation;
    private Scanner scanner;
    private GameWorld world;  // 新增：持有GameWorld引用，用于地图操作

    public static void main(String[] args) {
        MainGame game = new MainGame();
        game.play();
    }

    public MainGame() {
        // 1. 初始化玩家
        player = new Player("勇者",100,80,20,5);
        player.addSkill(new Fireball("小火球", 35, 5));
        player.addSkill(new Heal("治疗术", 6, 35));
        player.addSkill(new Blitz("闪击",25,3));

        // 2. 初始化世界
        world = new GameWorld();
        currentLocation = world.getStartLocation();

        scanner = new Scanner(System.in);
    }

    public void play() {
        printWelcome();

        boolean finished = false;
        while (!finished) {
            // 每次循环开始，检查当前位置是否有敌人触发战斗
            checkEncounter();
            checkEvent();

            if (!player.isAlive()) {
                System.out.println("❌ 胜败乃兵家常事，大侠请重新来过。");
                break;
            }

            // 打印当前场景信息
            System.out.println("\n-----------------------------------------");
            System.out.println("📍 当前位置: " + currentLocation.getName() + " | 坐标: " + (world != null ? world.getPlayerCoord() : "未知"));
            System.out.println("📝 " + currentLocation.getDescription());
            System.out.println("🚪 " + currentLocation.getExitString());
            if (currentLocation.hasItem()) {
                System.out.println("✨ 地上好像有一个物品，输入 'take' 捡起。");
            }
            if (currentLocation.hasNpc()) {
                System.out.println("👤 " + currentLocation.getNpc().getName() + " 站在这里。输入 'talk' 进行对话。");
            }
            System.out.println("-----------------------------------------");
            System.out.print("> ");
            String input = scanner.nextLine();
            finished = processCommand(input);
        }
        System.out.println("感谢游玩！");
    }

    private void printWelcome() {
        System.out.println("熟悉的天花板...");
        System.out.println("输入 'go [direction]' 或直接输入 w/a/s/d 移动");
        System.out.println("输入 'm' 显示地图 | 'status' 查看状态 | 'bag' 打开背包");
        System.out.println("输入 'take' 捡起物品 | 'talk' 与NPC对话 | 'quit' 退出游戏");
    }

    // 处理探索模式的命令
    private boolean processCommand(String input) {
        String[] words = input.trim().split("\\s+");//匹配一个或多个空白字符
        String command = words[0].toLowerCase();

        switch (command) {
            case "quit":
                return true; // 结束游戏
            case "help":
                printWelcome();
                break;
            case "status":
                player.displayStatus();
                break;
            case "m":
                showMap();
                break;
            case "w":
            case "a":
            case "s":
            case "d":
                goRoom(command);
                break;
            case "go":
                if (words.length > 1) {
                    goRoom(words[1]); // 修改：移动逻辑关联地图
                } else {
                    System.out.println("去哪里？(请输入 w/a/s/d)");
                }
                break;
            case "take":
                takeItem();
                break;
            case "talk":
                talkWithNpc();
                break;
            case "bag":
            case "inventory":
                player.getInventory().displayItems();
                if (player.getInventory().getItemCount() > 0) {
                    System.out.println("输入道具编号使用，或输入 back 返回上一级");
                    String s = scanner.nextLine().trim();
                    if (s.equalsIgnoreCase("back") || s.equalsIgnoreCase("b")) {
                        break;
                    }
                    try {
                        int itemIndex = Integer.parseInt(s);
                        player.getInventory().useItem(itemIndex, player);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ 输入无效，请输入数字或 back 返回上一级！");
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("❌ 道具编号不存在，请重新输入！");
                    }
                }
                break;
            default:
                System.out.println("无法识别的指令。");
        }
        return false;
    }

    // 新增：显示地图（控制台打印二维字符数组）
    private void showMap() {
        System.out.println("\n======= 当前地图 =======");
        char[][] map = world.getMapGrid();
        // 打印地图网格
        for (int y = 0; y < MapConstant.MAP_HEIGHT; y++) {
            for (int x = 0; x < MapConstant.MAP_WIDTH; x++) {
                System.out.print(map[y][x] + " "); // 加空格让地图更清晰
            }
            System.out.println(); // 换行
        }
        // 打印地图图例（参考文档示例）
        System.out.println("\n图例：");
        System.out.println("@ - 你（玩家） | V - 新手村 | F - 迷雾森林 | C - 黑暗洞穴 | T - 魔王城堡 | ? - 事件");
        System.out.println("# - 山脉（不可通行） | . - 路径（可通行）");
        System.out.println("=======================\n");
    }

    // 移动逻辑
    private void goRoom(String direction) {
        // 尝试从 DirectionConstant 获取全称（如果输入的是 w/a/s/d）
        String fullDirection = direction;
        if (direction.length() == 1) {
            String mappedDir = getDirByKey(direction.charAt(0));
            if (mappedDir != null) {
                fullDirection = mappedDir;
            }
        }

        // 调用GameWorld的movePlayer方法，判断是否移动成功
        boolean moveSuccess = world.movePlayer(direction);
        if (moveSuccess) {
            // 移动成功：根据玩家所在坐标刷新场景（道路不触发遭遇战）
            Location locAt = world.getLocationAtPlayer();
            if (locAt != null) {
                currentLocation = locAt;
            } else {
                currentLocation = new Location("道路", "一条普通的道路，通向未知的地方。");
            }
            
            // 使用处理过的全称显示
            System.out.println("go " + fullDirection);
        } else {
            // 移动失败：提示不可通行
            System.out.println("🚫 前方地势凶险，不可通行！！");
        }
    }

    // 捡东西逻辑
    private void takeItem() {
        Item item = currentLocation.takeItem();
        if (item != null) {
            player.getInventory().addItem(item);
        } else {
            System.out.println("这里没有什么可以捡的。");
        }
    }

    // NPC对话逻辑
    private void talkWithNpc() {
        if (currentLocation.hasNpc()) {
            NPC npc = currentLocation.getNpc();
            System.out.println("\n[" + npc.getName() + "] 说：");
            System.out.println("“" + npc.getDialogue() + "”");
            // 洞穴女神：授予一次性技能
            if ("女神".equals(npc.getName())) {
                String godSkillName = "神技：不尽斩";
                if (!player.hasSkill(godSkillName)) {
                    player.addSkill(new InfinitySlash(godSkillName, 100, 12));
                    npc.setDialogue("愿光明常伴你前行。");
                } else {
                    System.out.println("女神微笑示意：好好运用你的神技吧。");
                }
            }
        } else {
            System.out.println("这里没有人可以对话。");
        }
    }

    // 遭遇战检查
    private void checkEncounter() {
        Location locAt = world.getLocationAtPlayer();
        if (locAt == null) return; // 在道路上不触发遭遇
        Enemy enemy = locAt.getEnemy();
        if (enemy != null) {
            System.out.println("\n⚠️ 突然！一只 " + enemy.getName() + " 挡住了你的去路！");

            // 调用你之前写好的战斗引擎
            BattleEngine battle = new BattleEngine(player, enemy);
            battle.startBattle();
            if (battle.isEscaped()) {
                world.escapeToLastSafePosition();
                Location afterEscape = world.getLocationAtPlayer();
                currentLocation = (afterEscape != null) ? afterEscape : new Location("道路", "一条普通的道路，通向未知的地方。");
                System.out.println("你成功脱离战斗，撤退到安全位置。");
            } else if (player.isAlive() && !enemy.isAlive()) {
                System.out.println("你战胜了敌人，现在的区域安全了。");
                locAt.clearEnemy();
            }
        }
    }

    // 事件检查与触发
    private void checkEvent() {
        Event evt = world.getEventAtPlayer();
        if (evt != null) {
            evt.play(player, scanner);
            world.consumeEventAtPlayer();
        }
    }

}
