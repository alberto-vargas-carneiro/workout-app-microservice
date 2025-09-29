# Solução: 401 Unauthorized entre Microserviços

## ❌ **Problema Identificado:**
```
Erro ao buscar exercício 3: [401] during [GET] to [http://localhost:8080/exercises/3]
```

O `ExerciseClient` está fazendo chamadas **SEM o token JWT**, causando erro 401.

## ✅ **Solução Implementada:**

### **1. FeignJwtRequestInterceptor**
```java
@Component
public class FeignJwtRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) auth.getPrincipal();
            template.header("Authorization", "Bearer " + jwt.getTokenValue());
        }
    }
}
```

### **2. FeignConfig**
```java
@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor jwtRequestInterceptor() {
        return new FeignJwtRequestInterceptor();
    }
}
```

### **3. Clients Atualizados**
```java
@FeignClient(name = "exercise-service", 
             url = "http://localhost:8080", 
             configuration = FeignConfig.class)
public interface ExerciseClient { ... }

@FeignClient(name = "user-service", 
             url = "http://localhost:8082", 
             configuration = FeignConfig.class)
public interface UserClient { ... }
```

## 🔄 **Como Funciona Agora:**

```
1. Cliente → workout-service (com JWT)
2. WorkoutService.findById() → SecurityContext tem o JWT
3. ExerciseClient.findById() → Interceptor pega JWT do SecurityContext
4. HTTP call → exercise-service (COM JWT no header)
5. Exercise-service valida JWT → 200 OK
```

## 🎯 **Fluxo de Propagação do Token:**

1. **Usuário** envia: `Authorization: Bearer <TOKEN>`
2. **Workout-service** valida token via Spring Security
3. **SecurityContext** armazena o JWT
4. **Feign Interceptor** pega JWT do SecurityContext
5. **Todas as chamadas** Feign incluem: `Authorization: Bearer <TOKEN>`
6. **Exercise/User services** recebem o token e validam

## ✅ **Benefícios:**

- **Automático**: Todos os Feign clients propagam o token
- **Transparente**: Não precisa passar token manualmente
- **Seguro**: Mantém autenticação em toda a cadeia
- **Consistente**: Funciona para todos os microserviços

## 🧪 **Teste:**

```bash
# 1. Obter token:
POST http://localhost:8082/oauth2/token

# 2. Usar no workout-service (deve funcionar sem 401):
GET http://localhost:8081/workouts/1
Authorization: Bearer <TOKEN>
```

**Agora o token será propagado automaticamente para todos os microserviços!** 🎉