# Solução para JWT sem UserId - Consulta via HTTP

## 🎯 **Problema Identificado**

- O token JWT contém apenas `username` (email) no claim
- O `userId` é gerado via `@GeneratedValue(strategy = GenerationType.IDENTITY)`
- Não está presente no token JWT

## ✅ **Solução Implementada**

### 1. **UserClient - Feign para consultar User Service**

```java
@FeignClient(name = "user-service", url = "${user-service.url:http://localhost:8080}")
public interface UserClient {
    @GetMapping("/users/by-email")
    UserDTO getUserByEmail(@RequestParam("email") String email);
}
```

### 2. **UserService - Com Cache para Performance**

```java
@Service
public class UserService {
    @Autowired
    private UserClient userClient;
    
    @Cacheable(value = "userIds", key = "#email")
    public Long getUserIdByEmail(String email) {
        UserDTO user = getUserByEmail(email);
        return user != null ? user.getId() : null;
    }
}
```

### 3. **SecurityUtils - Atualizado para usar HTTP**

```java
@Component
public class SecurityUtils {
    private static UserService userService;
    
    public static Long getAuthenticatedUserId() {
        // 1. Tenta primeiro o claim "sub"
        // 2. Se não encontrou, pega o email do claim "username"
        // 3. Faz chamada HTTP para user-service via UserClient
        // 4. Retorna o userId com cache
    }
}
```

### 4. **Configurações Adicionadas**

**application.properties:**
```properties
user-service.url=${USER_SERVICE_URL:http://localhost:8080}
```

**WorkoutServiceApplication.java:**
```java
@SpringBootApplication
@EnableFeignClients
@EnableCaching  // <- Adicionado para cache
```

## 🚀 **Como Funciona o Fluxo**

1. **Requisição chega** com Bearer Token
2. **Spring Security valida** o token JWT
3. **SecurityUtils.getAuthenticatedUserId()** é chamado
4. **Extrai email** do claim `username`
5. **UserService.getUserIdByEmail()** faz chamada HTTP
6. **Cache** armazena o resultado (evita múltiplas chamadas)
7. **Retorna o userId** para validação

## 📊 **Vantagens desta Abordagem**

### ✅ **Performance**
- **Cache local** evita múltiplas chamadas HTTP
- **@Cacheable** do Spring Boot gerencia automaticamente

### ✅ **Resiliência**
- **Try/catch** trata falhas do user-service
- **Não quebra** o fluxo se serviço estiver offline

### ✅ **Escalabilidade**
- **Cache por email** - alta taxa de hit
- **Feign** com load balancer automático

### ✅ **Manutenibilidade**
- **Separação clara** de responsabilidades
- **Fácil de testar** e mockar

## 🧪 **Como Testar**

### 1. **Endpoint necessário no User Service:**
```java
@GetMapping("/users/by-email")
public UserDTO getUserByEmail(@RequestParam String email) {
    User user = userRepository.findByEmail(email);
    return new UserDTO(user.getId(), user.getEmail(), user.getName());
}
```

### 2. **Teste no Postman:**
```bash
GET http://localhost:8081/workouts/1
Authorization: Bearer <TOKEN_COM_EMAIL>
```

### 3. **Logs esperados:**
```
Cache MISS: Buscando usuário por email alex@gmail.com
UserClient: GET http://localhost:8080/users/by-email?email=alex@gmail.com
Cache HIT: Retornando userId=123 para alex@gmail.com
```

## 🔧 **Configuração de Cache (Opcional)**

Para configuração avançada de cache:

```java
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setCacheNames(Arrays.asList("users", "userIds"));
        return cacheManager;
    }
}
```

**Esta é a solução mais robusta e eficiente para microserviços!** 🎉