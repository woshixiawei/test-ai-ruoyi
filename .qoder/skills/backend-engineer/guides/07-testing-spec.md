# 后端测试规范

- 版本号: v2.0
- 适用范围: 所有后端 server 模块
- 约束力: **强制**

---

> 四层架构各层详细规范详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)

---

## 1. 测试文件组织
```
src/test/java/com/example/project/server/
├── adapter/                          # 适配层测试
│   └── controller/
│       └── UserControllerTest.java
├── application/                      # 应用层测试
│   └── service/
│       └── UserAppServiceTest.java
├── domain/                           # 领域层测试
│   └── service/
│       └── OrderDomainServiceTest.java
└── infrastructure/                   # 基础设施层测试
    └── repository/
        └── UserRepositoryImplTest.java
```

## 2. 单元测试规范
```java
/**
 * 订单领域服务单元测试
 */
@ExtendWith(MockitoExtension.class)
class OrderDomainServiceTest {

	@InjectMocks
	private OrderDomainService orderDomainService;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private InventoryDomainService inventoryDomainService;

	@Nested
	@DisplayName("提交订单")
	class SubmitOrderTests {

		@Test
		@DisplayName("正常流程：库存充足，提交成功")
		void shouldSubmitOrderSuccessfullyWhenStockSufficient() {
			// Given
			Long userId = 1L;
			List<OrderItem> items = List.of(
					new OrderItem(1001L, new BigDecimal("99.99"), 2),
					new OrderItem(1002L, new BigDecimal("199.99"), 1)
			);
			when(inventoryDomainService.checkStock(1001L, 2)).thenReturn(true);
			when(inventoryDomainService.checkStock(1002L, 1)).thenReturn(true);

			// When
			OrderDO order = orderDomainService.submitOrder(userId, items);

			// Then
			assertThat(order).isNotNull();
			assertThat(order.getUserId()).isEqualTo(userId);
			assertThat(order.getItems()).hasSize(2);
			verify(inventoryDomainService).decreaseStock(1001L, 2);
			verify(inventoryDomainService).decreaseStock(1002L, 1);
			verify(orderRepository).save(any(OrderDO.class));
		}

		@Test
		@DisplayName("异常流程：库存不足，抛出业务异常")
		void shouldThrowExceptionWhenStockInsufficient() {
			// Given
			Long userId = 1L;
			List<OrderItem> items = List.of(
					new OrderItem(1001L, new BigDecimal("99.99"), 100)
			);
			when(inventoryDomainService.checkStock(1001L, 100)).thenReturn(false);

			// When & Then
			assertThatThrownBy(() -> orderDomainService.submitOrder(userId, items))
					.isInstanceOf(BusinessException.class)
					.hasMessageContaining("库存不足");

			verify(inventoryDomainService, never()).decreaseStock(any(), anyInt());
			verify(orderRepository, never()).save(any());
		}
	}

	@Nested
	@DisplayName("计算订单金额")
	class CalculateAmountTests {

		@Test
		@DisplayName("正常计算多个商品总金额")
		void shouldCalculateTotalAmountCorrectly() {
			List<OrderItem> items = List.of(
					new OrderItem(1L, new BigDecimal("10.00"), 3),
					new OrderItem(2L, new BigDecimal("25.50"), 2)
			);

			BigDecimal amount = orderDomainService.calculateTotalAmount(items);

			assertThat(amount).isEqualByComparingTo(new BigDecimal("81.00"));
		}

		@Test
		@DisplayName("空列表应返回0")
		void shouldReturnZeroForEmptyList() {
			BigDecimal amount = orderDomainService.calculateTotalAmount(List.of());
			assertThat(amount).isEqualByComparingTo(BigDecimal.ZERO);
		}
	}
}
```

## 3. 集成测试规范
```java
/**
 * 用户仓储集成测试
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class UserRepositoryImplTest {

	@Container
	static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
			.withDatabaseName("test_db")
			.withUsername("test")
			.withPassword("test");

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("保存用户后应能查询到")
	void shouldFindUserAfterSave() {
		// Given
		UserDO user = new UserDO();
		user.setUserName("张三");
		user.setEmail(new Email("zhangsan@example.com"));
		user.setPhone(new Phone("13800138000"));
		user.setStatus(UserStatus.ACTIVE);
		user.setCreateTime(LocalDateTime.now());

		// When
		userRepository.save(user);

		// Then
		Optional<UserDO> found = userRepository.findById(user.getId());
		assertThat(found).isPresent();
		assertThat(found.get().getUserName()).isEqualTo("张三");
	}
}
```

## 4. 覆盖率要求
| 类型 | 覆盖率要求 | 说明 |
|------|-----------|------|
| 行覆盖率 | ≥ 80% | 核心业务逻辑 ≥ 90% |
| 分支覆盖率 | ≥ 70% | |
| 领域层 | 必须测试 | 所有业务方法 |
| 应用层 | 建议测试 | 事务、编排逻辑 |
| 基础设施层 | 建议测试 | Repository 可用集成测试 |

---

> **交叉引用**：
> - 四层架构各层详细规范详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)
> - 异常体系详见 [06-exception-and-logging.md](./06-exception-and-logging.md)
