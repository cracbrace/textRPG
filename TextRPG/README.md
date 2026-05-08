# TextRPG

## 概述
- 文本 RPG，支持探索、战斗、NPC 对话与随机地图生成
- 地图采用网格结构，边缘为不可通行的山脉，保证至少一条从新手村到魔王城堡的通路

## 运行
- 编译：`javac -d bin -sourcepath src src/com/hdh/txtrpg/MainGame.java`
- 运行：`java -cp bin com.hdh.txtrpg.MainGame`
- 流程演示：`javac -d bin -sourcepath src src/com/hdh/txtrpg/demo/FlowDemo.java && java -cp bin com.hdh.txtrpg.demo.FlowDemo`

## 指令
- 移动：`w/a/s/d` 或 `go north/south/west/east`
- 对话：`talk`（在新手村与“老村长”交互）
- 战斗：遇敌进入战斗界面，支持攻击、背包、逃跑

## 架构
- 地图生成：`map` 模块（生成器接口与默认实现）
  - MapGenerator 接口：解耦生成策略
  - DefaultMapGenerator：默认随机生成，保证连通性
  - PathFinder：连通性检查
- 世界管理：`entity/GameWorld.java` 使用生成器产出地图与地点坐标
- 常量：`util/MapConstant.java` 定义地图尺寸与符号

## 扩展
- 可新增不同的 MapGenerator 实现（如迷宫、预设地图）
- 可添加更多地点类型与事件系统
- 可将坐标映射替换为持久化数据结构以支持存档
