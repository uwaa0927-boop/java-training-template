# JPA Entity 実装ガイド

## 基本的なEntityの構造

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String name;
    
    @Column(unique = true)
    private String email;
    
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

## アノテーション詳細

### @GeneratedValue の戦略

- IDENTITY: データベースのAUTO_INCREMENT使用（MySQL推奨）
- SEQUENCE: シーケンスを使用（PostgreSQL等）
- TABLE: 専用テーブルで管理
- AUTO: データベースに応じて自動選択

### @Column の主要属性

- name: カラム名（Javaのフィールド名と異なる場合）
- nullable: NULL許可（デフォルトtrue）
- unique: ユニーク制約
- length: 文字列の最大長
- precision, scale: 数値の精度

### リレーションシップ

#### @OneToMany（1対多）
@Entity
public class Parent {
    @Id
    private Long id;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Child> children;
}

#### @ManyToOne（多対1）
@Entity
public class Child {
    @Id
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;
}

## Lombokとの組み合わせ

@Entity
@Data                    // getter/setter/toString/equals/hashCode
@NoArgsConstructor      // 引数なしコンストラクタ
@AllArgsConstructor     // 全フィールドコンストラクタ
@Builder                // ビルダーパターン
public class Sample {
    @Id
    private Long id;
    private String name;
}

### 使い方
Sample sample = Sample.builder()
    .id(1L)
    .name("Test")
    .build();
