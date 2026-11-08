============================================================
  FocusMate —— 项目架构说明（ARCHITECTURE.txt） （write by AI）
============================================================

【一句话】
  番茄钟 + 任务管理器。控制台起步，逐步长成带窗口、
  能存数据库、能联网的应用。

【怎么读这份文件】
  - 目录树里每个文件后面都写了"职责"，写代码前先对号入座
  - 阶段标记 [S1]~[S8] 表示这个文件在第几阶段启用
  - 没到那个阶段，文件可以先空着，别提前写


============================================================
一、分层结构（这是整个项目的骨架）
============================================================

  UI 层        只管"跟用户打交道"：读输入、打印、画窗口
     ↓ 调用
  Service 层   只管"业务规则"：倒计时、增删任务、统计
     ↓ 调用
  Storage 层   只管"存取数据"：写文件、查数据库
     ↓ 使用
  Model 层     只管"数据长什么样"：Task、FocusSession

  另外：
  Exception 层  表达"业务出错"的异常类
  Util 层       无状态的工具函数（校验、格式化）
  App           入口，负责把上面各层"组装"起来

  铁律：上层可以调下层，下层绝不能反过来调上层。
  例：Service 不碰 System.out；UI 不写 SQL。


============================================================
二、目录树模板 （下面是完整目标结构，[S2]~[S8] 的文件尚未创建）
============================================================
'''text
FocusMate/
├── ARCHITECTURE.txt               ← 本文件
├── .gitignore                     ← 忽略 target/、.idea/、data/
├── pom.xml                        ← 用 Maven 时才有；阶段一可无
│
├── src/
│   ├── main/java/com/focusmate/
│   │   │
│   │   ├── App.java               [S1] 程序入口 main()，只做组装和启动
│   │   │
│   │   ├── model/                 ← 数据模型（纯数据，不干活）
│   │   │   ├── Task.java          [S2] 任务：id、标题、状态、优先级、创建时间
│   │   │   └── FocusSession.java  [S3] 一次专注记录：开始时间、时长、关联任务
│   │   │
│   │   ├── service/               ← 业务逻辑
│   │   │   ├── PomodoroService.java  [S1] 倒计时（带回调，不直接打印）
│   │   │   └── TaskService.java      [S2] 任务增删改查、过滤、统计
│   │   │
│   │   ├── storage/               ← 存储（先文件，后数据库）
│   │   │   ├── TaskRepository.java      [S2] 存储接口（泛型可抽）
│   │   │   ├── FileTaskStorage.java     [S2] CSV/JSON 文件实现
│   │   │   └── DbTaskStorage.java       [S6] SQLite 实现
│   │   │
│   │   ├── ui/                    ← 界面
│   │   │   ├── ConsoleUI.java     [S1] 控制台：横幅、问时长、显示倒计时
│   │   │   └── SwingMainFrame.java[S4] 窗口：任务表、环形进度条、按钮
│   │   │
│   │   ├── util/                  ← 工具（纯函数，零副作用）
│   │   │   ├── DurationValidator.java [S1] 校验"分钟数"输入
│   │   │   └── TimeFormatter.java     [S1] 秒 → "mm:ss"
│   │   │
│   │   ├── exception/             ← 自定义业务异常
│   │   │   ├── InvalidDurationException.java [S1] 时长不合法
│   │   │   └── StorageException.java         [S2] 存储读写失败
│   │   │
│   │   ├── net/                   ← 网络通信
│   │   │   ├── SyncServer.java    [S7] 服务端：监听、处理请求
│   │   │   └── SyncClient.java    [S7] 客户端：发请求、收结果
│   │   │
│   │   └── plugin/                ← 反射 + 注解
│   │       ├── Command.java       [S8] 注解 @Command("today")
│   │       └── CommandRouter.java [S8] 扫描注解，自动注册命令
│   │
│   └── test/java/com/focusmate/   ← 单元测试，包结构跟 main 一致
│       ├── util/
│       │   └── DurationValidatorTest.java  [S1] 5 条输入用例
│       └── service/
│           └── PomodoroServiceTest.java    [S1] 可选，先测校验器即可
│
├── data/                          ← 运行时数据（.gitignore 里排除）
│   ├── tasks.csv                  [S2] 任务文件
│   └── focusmate.db               [S6] SQLite 数据库
│
└── docs/                          ← 笔记、设计草稿（可选）
    └── notes.md
'''

============================================================
三、各文件的"一句话职责"（写代码前对号入座）
============================================================

  App.java            组装各层并启动。不写业务、不写校验。
  ConsoleUI.java      打印 + 读输入 + 展示。不判断合不合法。
  SwingMainFrame.java 窗口与绘制。不在 EDT 外碰 UI。
  PomodoroService     给定分钟数，每秒回调一次剩余时间。不打印。
  TaskService         任务的增删改查与统计。不碰 SQL/文件。
  *Repository         定义"怎么存"的接口。不写实现。
  *Storage            实现接口，真读真写。不写业务。
  DurationValidator   字符串 → 合法分钟数；非法抛异常。零副作用。
  TimeFormatter       秒 → "mm:ss"。纯函数。
  *Exception          携带"为什么错"的文案。
  *Server / *Client   协议处理。不写业务规则。
  *Command / Router   反射注册命令。不写命令本身。


============================================================
四、命名规范（从第一天就守）
============================================================

  包 package      全小写，不缩写         com.focusmate.service
  类 class        大驼峰，名词            PomodoroService
  接口 interface  大驼峰                 TaskRepository
  枚举 enum       大驼峰，值全大写        TaskStatus.TODO
  方法 method     小驼峰，动词开头        validateDuration()
  变量 variable   小驼峰，说清含义        remainingSeconds
  常量 constant   全大写 + 下划线         MAX_DURATION_MINUTES
  异常类          *Exception 结尾         InvalidDurationException
  测试类          *Test 结尾              DurationValidatorTest
  文件名          与 public 类同名         PomodoroService.java

  ⚠ Java 区分大小写，文件名/类名大小写必须完全一致。


============================================================
五、变量放哪（作用域最小化）
============================================================

  局部变量   临时结果、这一轮用完就丢   raw、minutes、remainingSeconds
  成员变量   这个类反复要用的家当       scanner、service 引用
  静态常量   全类共用、不会变的值       MIN/MAX 分钟数

  原则：能放局部，就别放成员；能放成员，就别放静态。


============================================================
六、阶段路线图（每个阶段"能跑起来"再进下一阶段）
============================================================

  [S1] 命令行番茄钟       异常处理、String、time 类库
       验收：输入 25 倒计时到 0；abc/-5/0 不崩、重问

  [S2] 任务 + 文件持久化   IO、枚举
       验收：关掉程序再开，任务还在

  [S3] 集合/泛型/流        List/Map、Comparator、Stream、Optional
       验收：能过滤、排序、统计今日专注总时长

  [S4] Swing 界面 + 绘图   组件、布局、事件、Graphics2D
       验收：窗口里有任务表和环形进度条

  [S5] 多线程/并发         Executor、ScheduledExecutor、原子类
       验收：计时在后台走，UI 不卡；自动保存

  [S6] 数据库              JDBC、SQL、DAO、事务
       验收：数据从 CSV 迁到 SQLite，功能不变

  [S7] 网络通信            Socket、线程池、自定义协议
       验收：另一台设备能查到今日记录

  [S8] 反射 + 注解         Class、Method、@Retention(RUNTIME)
       验收：加个 @Command 注解就能注册新命令


============================================================
七、代码规范清单（贴墙）
============================================================

  [ ] 一个类一个职责，方法 ≤ 30 行
  [ ] 变量名说清意图，不用 a/t/x
  [ ] 异常要么处理，要么往上抛，禁止空 catch
  [ ] 公共方法写一行 Javadoc
  [ ] 不用魔法数字，抽成常量
  [ ] 流/连接用 try-with-resources
  [ ] 每完成一个能跑的功能，git commit 一次


============================================================
八、当前进度
============================================================

  当前阶段：S1 -> S2
  已完成：S1
  下一步：S2
  待办  ：偷懒
============================================================