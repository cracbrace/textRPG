package com.hdh.txtrpg.engine;

import com.hdh.txtrpg.entity.Character;
import com.hdh.txtrpg.entity.Enemy;
import com.hdh.txtrpg.entity.Player;
import com.hdh.txtrpg.entity.Skill;
import com.hdh.txtrpg.entity.DragonBreath;
import com.hdh.txtrpg.entity.ShadowClaw;

import java.util.Scanner;

public class BattleEngine {
    private Character player;
    private Character enemy;
    private Scanner scanner;
    private static final double CRIT_RATE = 0.15;  // 15%暴击率
    private static final double MISS_RATE = 0.10;  // 10%闪避率
    private static final int CRIT_MULTIPLIER = 2;  // 暴击倍数
    private static final double ESCAPE_RATE = 0.5; // 50%逃跑成功率
    private boolean isEscaped = false; // 是否已逃跑
    private Skill bossBreath;
    private Skill bossClaw;
    private boolean bossPhase2 = false;
    private int dragonMaxHp = -1;

    //构造方法：初始化战斗双方
    public BattleEngine(Character player, Character enemy) {
        this.player = player;
        this.enemy = enemy;
        this.scanner = new Scanner(System.in);
        if ("绝望黑龙".equals(enemy.getName())) {
            bossBreath = new DragonBreath();
            bossClaw = new ShadowClaw();
            dragonMaxHp = 280; // 与 Enemy.createDragon 保持一致
        }
    }

    //开始战斗
    public void startBattle() {
        System.out.println("=== 战斗开始！ ===");
        System.out.println("对手：" + enemy.getName() + "\n");
        while (player.isAlive() && enemy.isAlive()) {
            playTurn();// 玩家回合
            
            // 检查是否逃跑成功
            if (isEscaped) {
                break;
            }

            if(!enemy.isAlive()) break; // 敌人死亡，结束战斗
            enemyTurn();// 敌人回合
        }

        //战斗结束
        endBattle();
    }

    //玩家回合
    public void playTurn() {
        System.out.println("=== 你的回合！ ===");
        System.out.println("输入指令：attack（普攻）| skill（技能）| item（道具）| escape（逃跑）| status（状态）| quit（退出）");
        String input = scanner.nextLine().trim().toLowerCase();

        switch (input) {
            case "attack":
                performAttack(player, enemy, true);
                break;
            
            case "escape":
                attemptEscape();
                break;

            case "status":
                player.displayStatus();
                playTurn();
                break;

            case "skill":
                ((Player) player).displaySkills();
                // 提示玩家可以输入 back 返回
                System.out.println("输入技能编号使用，或输入 back 返回上一级");
                // 读取整行输入
                String sk = scanner.nextLine().trim();
                scanner.nextLine(); // 清空之前输入留下的换行符
                // 判断是否触发返回指令
                if (sk.equalsIgnoreCase("back") || sk.equalsIgnoreCase("b")) {
                    System.out.println("返回战斗主菜单...");
                    break;
                }
                // 尝试解析技能编号
                try {
                    int skillIndex = Integer.parseInt(sk);
                    ((Player) player).useSkill(skillIndex, enemy);
                } catch (NumberFormatException e) {
                    System.out.println("❌ 输入无效，请输入数字或 back 返回！");
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("❌ 技能编号不存在，请重新输入！");
                }
                break;

            case "item":
                ((Player) player).getInventory().displayItems();
                // 提示玩家可以输入 back 返回
                System.out.println("输入道具编号使用，或输入 back 返回上一级");
                // 读取整行输入（避免 nextInt 导致的输入不兼容问题）
                String s = scanner.nextLine().trim();
                scanner.nextLine(); // 清空之前输入留下的换行符
                // 判断是否是返回指令
                if (s.equalsIgnoreCase("back") || s.equalsIgnoreCase("b")) {
                    System.out.println("返回战斗主菜单...");
                    break; // 直接跳出当前 case，回到上一级菜单
                }
                // 尝试解析道具编号
                try {
                    int itemIndex = Integer.parseInt(s);
                    ((Player) player).getInventory().useItem(itemIndex, player);
                } catch (NumberFormatException e) {
                    System.out.println("❌ 输入无效，请输入数字或 back 返回上一级菜单！");
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("❌ 道具编号不存在，请重新输入！");
                }
                break;

            case "quit":
                System.out.println("游戏结束！");
                System.exit(0);
                break;

            default:
                System.out.println("无效的指令！请重新输入。");
                playTurn();
                break;
        }
    }

    // 尝试逃跑
    private void attemptEscape() {
        if (Math.random() < ESCAPE_RATE) {
            System.out.println("🏃‍♂️ 你抓住了敌人的破绽，成功逃跑了！");
            isEscaped = true;
        } else {
            System.out.println("❌ 逃跑失败！敌人拦住了你的去路。");
        }
    }

    //敌人回合
    public void enemyTurn() {
        System.out.println("\n=== 对手的回合！ ===");
        Enemy e = (Enemy) enemy;
        e.displayStatus();
        if ("绝望黑龙".equals(enemy.getName()) && bossBreath != null && bossClaw != null) {
            if (!bossPhase2 && dragonMaxHp > 0 && enemy.getHp() <= dragonMaxHp / 2) {
                bossPhase2 = true;
                System.out.println("💢 绝望黑龙发出震天怒吼，鳞片燃起深红光芒，进入【二阶段】！");
                bossBreath = new DragonBreath((int)(70 * 1.5), 12);
                bossClaw = new ShadowClaw((int)(45 * 1.5), 8, 8);
            }
            double roll = Math.random();
            if (bossPhase2) {
                if (roll < 0.65) {
                    bossBreath.apply(enemy, player);
                } else {
                    bossClaw.apply(enemy, player);
                }
            } else {
                if (roll < 0.6) {
                    bossBreath.apply(enemy, player);
                } else if (roll < 0.9) {
                    bossClaw.apply(enemy, player);
                } else {
                    performAttack(enemy, player, false);
                }
            }
        } else {
            performAttack(enemy, player, false);
        }
    }

    //执行攻击逻辑（包含暴击和闪避）
    private void performAttack(Character attacker, Character defender, boolean isPlayerAttacking) {
        // 检查是否闪避
        if (Math.random() < MISS_RATE) {
            System.out.println(attacker.getName() + "的攻击被" + defender.getName() + "闪避了！");
            return;
        }

        // 计算基础伤害
        int baseDamage = Math.max(attacker.getAttack() - defender.getDefense(), 1);

        // 检查是否暴击
        boolean isCrit = Math.random() < CRIT_RATE;
        int finalDamage = baseDamage;
        String attackDescription = "";

        if (isCrit) {
            finalDamage = baseDamage * CRIT_MULTIPLIER;
            attackDescription = "【暴击】";
        }

        // 执行伤害
        int defenderHpBefore = defender.getHp();
        defender.takeDamage(finalDamage);
        int actualDamage = defenderHpBefore - defender.getHp();

        // 显示攻击结果
        if (isPlayerAttacking) {
            System.out.println("你攻击了" + defender.getName() + attackDescription +
                    "，造成了" + actualDamage + "点伤害！");
        } else {
            System.out.println(attacker.getName() + "攻击了你" + attackDescription +
                    "，造成了" + actualDamage + "点伤害！");
        }
    }

    //战斗结束
    public void endBattle() {
        System.out.println("\n=== 战斗结束！ ===");
        
        if (isEscaped) {
            System.out.println("💨 你虽然逃跑了，但至少保住了性命。");
            return;
        }

        if (player.isAlive()) {
            System.out.println("🎉 你击败了" + enemy.getName() + "！");
            // 如果是Player对象，则给予经验奖励
            if (player instanceof Player && enemy instanceof Enemy) {
                Enemy e = (Enemy) enemy;
                Player currentPlayer = (Player) player;
                currentPlayer.gainExp(e.getExpReward());
            }
            if ("绝望黑龙".equals(enemy.getName())) {
                printFinale();
                System.exit(0);
            }
        } else {
            System.out.println("💀 你被" + enemy.getName() + "击败了...");
        }
    }

    private void printFinale() {
        printWithPause("\n████████████████████████████████████", 350);
        printWithPause("          终章：光明重临", 350);
        printWithPause("████████████████████████████████████\n", 350);
        printWithPause("天际的阴霾被撕裂，金色的曙光洒落大地。", 350);
        printWithPause("城堡的黑影在光辉中碎裂，灰烬随风远去。", 350);
        printWithPause("远处的村民们举起火把与旗帜，为你的凯旋欢呼。", 350);
        printWithPause("你收回最后一斩的余势，利刃在光中渐渐黯淡。", 350);
        printWithPause("女神的低语自风而起：勇气与慈悲，皆为真正的力量。", 350);
        printWithPause("", 350);
        printWithPause("大地复明，河流重唱，钟声回荡在每一个角落。", 350);
        printWithPause("你的名字与光同在，被铭刻进这片世界的春与秋。", 350);
        printWithPause("", 350);
        printWithPause("—— 旅途在此告一段，但传说仍在继续 ——", 1000);
        printWithPause("", 350);
        printWithPause("感谢你的冒险", 350);
    }

    private void printWithPause(String text, int ms) {
        System.out.println(text);
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }

    public boolean isEscaped() {
        return isEscaped;
    }
}

