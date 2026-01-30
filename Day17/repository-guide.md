# Spring Data JPA Repository実装ガイド

## Repositoryの階層

Repository (マーカーインターフェース)
↓
CrudRepository (基本的なCRUD)
↓
PagingAndSortingRepository (ページング・ソート)
↓
JpaRepository (JPA固有機能) ← 通常はこれを使う

## JpaRepositoryの基本

public interface UserRepository extends JpaRepository<User, Long> {
    // これだけで以下のメソッドが使える
    // - save(User)
    // - findById(Long)
    // - findAll()
    // - delete(User)
    // - count()
    // など
}

## メソッド名によるクエリ自動生成

### 基本パターン

| メソッド名 | 生成されるSQL |
|-----------|--------------|
| findByName(String name) | WHERE name = ? |
| findByNameAndAge(String name, int age) | WHERE name = ? AND age = ? |
| findByNameOrAge(String name, int age) | WHERE name = ? OR age = ? |
| findByAgeLessThan(int age) | WHERE age < ? |
| findByAgeGreaterThan(int age) | WHERE age > ? |
| findByNameLike(String name) | WHERE name LIKE ? |
| findByNameContaining(String name) | WHERE name LIKE %?% |
| findByNameStartingWith(String name) | WHERE name LIKE ?% |
| findByNameEndingWith(String name) | WHERE name LIKE %? |
| findByAgeIn(List<Integer> ages) | WHERE age IN (?) |
| findByNameOrderByAgeDesc(String name) | WHERE name = ? ORDER BY age DESC |

### 複雑なクエリ

// ページング
Page<User> findByAge(int age, Pageable pageable);

// トップN件取得
List<User> findTop10ByOrderByCreatedAtDesc();

// カウント
long countByAge(int age);

// 存在確認
boolean existsByEmail(String email);

// 削除
void deleteByAge(int age);

### @queryアノテーションの使用

#### JPQLクエリ

@Query("SELECT u FROM User u WHERE u.age >= :age")
List<User> findAdults(@Param("age") int age);

@Query("SELECT u FROM User u WHERE u.name LIKE %:keyword%")
List<User> searchByName(@Param("keyword") String keyword);

#### ネイティブクエリ

@Query(value = "SELECT * FROM users WHERE age >= ?1", nativeQuery = true)
List<User> findAdultsNative(int age);

#### 更新クエリ

@Modifying
@Transactional
@Query("UPDATE User u SET u.status = :status WHERE u.age >= :age")
int updateStatus(@Param("status") String status, @Param("age") int age);

## トランザクション

@Transactional
public void updateUser(Long id, String name) {
    User user = userRepository.findById(id).orElseThrow();
    user.setName(name);
    // saveを呼ばなくても自動的に更新される（Dirty Checking）
}

## ベストプラクティス

1.メソッド名は長くなりすぎないように
- 複雑な条件は@queryを使う

2.Optionalの活用
- findByIdはOptionalを返す
- orElseThrow()でnullチェック不要

3.ページングの活用
- 大量データはPageableを使う

4.N+1問題の回避
- @entitygraphや@queryでfetch jyoinを使う
