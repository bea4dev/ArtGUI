## ArtGUI
インベントリGUIをエレガントに記述するためのフレームワーク

#### 機能
* ボタン配置
* ボタン追加
* 通常アイテムの追加
* 通常アイテムのみでのシリアライズ
* 通常アイテムのみでのデシリアライズ
* 戻るボタン
* ページ機能
* ページ移動ボタン
* ページフレーム
* GUI作成の視覚的表現
* 非同期ボタン設置


#### maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependencies>
    <dependency>
        <groupId>com.github.Be4rJP</groupId>
        <artifactId>ArtGUI</artifactId>
        <version>v1.0.2</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

#### Example

拡張インベントリ的なサンプル
<blockquote class="twitter-tweet"><p lang="ja" dir="ltr">こんな感じのGUIが数十行で定義できるようになった <a href="https://t.co/SmKygahBIM">pic.twitter.com/SmKygahBIM</a></p>&mdash; ベあﾞ (@_Be4_) <a href="https://twitter.com/_Be4_/status/1482273402568450048?ref_src=twsrc%5Etfw">January 15, 2022</a></blockquote> <script async src="https://platform.twitter.com/widgets.js" charset="utf-8"></script>

```java
public final class ExamplePlugin extends JavaPlugin {
    
    private static ArtGUI artGUI;
    
    @Override
    public void onEnable() {
        // Plugin startup logic
        
        //ArtGUIのインスタンスをプラグイン起動時に作成
        artGUI = new ArtGUI(this);
        
        //テスト用のイベントリスナーの登録
        Bukkit.getPluginManager().registerEvents(new TestListener(), this);
    }
    
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    
    /**
     * 他のクラスからArtGUIのインスタンスを取得できるようにしておく
     * @return ArtGUIのインスタンス
     */
    public static ArtGUI getArtGUI() {
        return artGUI;
    }
}
```

```java
public class TestListener implements Listener {
    
    //プレイヤー別のGUI内のアイテムデータ
    private Map<Player, String> serializedDataMap = new ConcurrentHashMap<>();
    
    /**
     * プレイヤーが腕を振った時に呼び出される
     * @param e PlayerAnimationEvent
     */
    @EventHandler
    public void onClick(PlayerAnimationEvent e) {
        Player player = e.getPlayer();
        
        //プレイヤーがしゃがんでいるかどうか
        if (!player.isSneaking()) return;
        
        //Artistクラスのインスタンスを作成
        //GUIの大きさと全てのページに配置するボタンを定義する
        //ここで定義したボタンは全てのページで表示されます
        Artist artist = new Artist(() -> {
            
            //nullを指定すると空白になりアイテムを配置したりできるようになる
            ArtButton V = null;
            //ボタンを作成
            ArtButton G = new ArtButton(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name("&a").build());
            
            //ページ移動用ボタンを作成
            PageNextButton N = new PageNextButton(new ItemBuilder(Material.ARROW).name("&r次のページ &7[{NextPage}/{MaxPage}]").build());
            //最後のページを開いている時に表示するボタンを設定する
            //ここでは次のページを開放するためのボタンを設定します
            N.setAlternativeButton(new ArtButton(new ItemBuilder(Material.EMERALD).name("&a&n次のページを開放").build())
                    .listener((event, menu) -> {
                        menu.addPage();
                        menu.getArtMenu().nextPage(player);
                    }));
            
            //ページ移動用ボタンを作成
            PageBackButton P = new PageBackButton(new ItemBuilder(Material.ARROW).name("&r前のページ &7[{PreviousPage}/{MaxPage}]").build());
            //戻るボタンを作成
            //もしこのGUIを開く前に別のGUIを開いていた場合はそのGUIに戻ります
            MenuBackButton B = new MenuBackButton(new ItemBuilder(Material.OAK_DOOR).name("&r{PreviousName}&7に戻る").build());
            
            //現在のページを表示するボタンを作成
            //ReplaceableButtonを継承したボタンの名前は特定の文字列が置き換わるようになります
            //詳細はReplaceNameManagerを参照
            ReplaceableButton I = new ReplaceableButton(new ItemBuilder(Material.NAME_TAG).name("&7現在のページ&r[{CurrentPage}/{MaxPage}]").build());
            
            //配列として視覚的に表記
            //配列の長さは必ず9の倍数である必要があります
            return new ArtButton[]{
                    V, V, V, V, V, V, V, G, G,
                    V, V, V, V, V, V, V, G, N,
                    V, V, V, V, V, V, V, G, I,
                    V, V, V, V, V, V, V, G, P,
                    V, V, V, V, V, V, V, G, G,
                    V, V, V, V, V, V, V, G, B,
            };
        });
        
        //GUIを作成
        ArtMenu artMenu = artist.createMenu(ExamplePlugin.getArtGUI(), "&nテストGUI&r [{CurrentPage}/{MaxPage}]");
        
        //非同期でアイテムを配置
        //GUIを開くたびに実行されます
        artMenu.asyncCreate(menu -> {
    
            //ページ1のスロット2にボタンを配置
            menu.setButton(1, 2, new ArtButton(new ItemBuilder(Material.BARRIER).name("&aボターン").build()).listener((event, menu1) -> {
                player.sendMessage("今ボターンを押しましたね！あなた！");
            }));
            
            //アイテムのデータを保存した後かどうか確認
            if (serializedDataMap.containsKey(player)){
                //保存されていればデータからアイテムを復元
                menu.deserializeItems(serializedDataMap.get(player));
            }
        });
        
        //GUIを閉じた時の動作を定義
        artMenu.onClose((event, menu) -> {
            //GUI内のアイテムのみのデータを保存
            serializedDataMap.put(player, menu.serializeItems());
        });
        
        //GUIを開く
        artMenu.open(player);
    }
}
```