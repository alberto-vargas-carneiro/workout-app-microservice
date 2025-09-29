# Configuração JWT para Microserviços - Guia de Teste

## ✅ Configuração Implementada

A aplicação agora está configurada para JWT/Bearer Token com as seguintes características:

### 1. **Dependências Adicionadas**
- `spring-boot-starter-oauth2-resource-server` - Para suporte JWT
- `spring-cloud-starter-openfeign` - Para comunicação entre microserviços

### 2. **SecurityConfig**
- Desabilitou autenticação Basic/Form Login
- Habilitou OAuth2 Resource Server com JWT
- Configurou endpoints públicos (H2 Console, Actuator)
- Stateless (sem sessões)

### 3. **SecurityUtils**
- Extrai `userId` do claim `sub` do token JWT
- Verifica role de admin através das authorities
- Funciona com tokens JWT validados pelo Spring Security

### 4. **Propriedades**
```properties
security.jwt.secret-key=${JWT_SECRET_KEY:my-secret-key-that-is-at-least-256-bits-long-for-jwt-signing}
server.port=8081
```

## 🧪 Como Testar no Postman

### Passo 1: Obter Token JWT
Primeiro, você precisa obter um token JWT do seu `user-service`:

```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "user@email.com", 
  "password": "123456"
}
```

### Passo 2: Configurar Authorization no Postman
1. Na aba **Authorization** da requisição
2. Selecione **Type: Bearer Token**
3. Cole o token JWT obtido no campo **Token**

### Passo 3: Testar Endpoints do Workout Service
```http
GET http://localhost:8081/workouts/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 🔧 Estrutura do Token JWT Esperado

O token deve conter:
```json
{
  "sub": "123",          // User ID (obrigatório)
  "authorities": ["ROLE_USER", "ROLE_ADMIN"],  // Roles
  "exp": 1695123456,     // Expiration
  "iat": 1695123456      // Issued at
}
```

## 🛠 Configuração de Ambiente

### Desenvolvimento
```bash
JWT_SECRET_KEY=my-secret-key-that-is-at-least-256-bits-long-for-jwt-signing
```

### Produção
```bash
JWT_SECRET_KEY=your-production-secret-key-minimum-256-bits
```

## ❌ Erros Comuns e Soluções

### 401 Unauthorized
- **Causa**: Token inválido ou expirado
- **Solução**: Gere novo token no user-service

### 403 Forbidden  
- **Causa**: Token válido mas sem permissão
- **Solução**: Verifique se o userId do token corresponde ao recurso

### Token não encontrado
- **Causa**: Header Authorization não enviado
- **Solução**: Configurar Bearer Token no Postman

## 🔗 Comunicação entre Microserviços

O **mesmo token JWT** deve ser propagado entre microserviços:
- User Service (porta 8080) - Gera tokens
- Workout Service (porta 8081) - Valida tokens  
- Exercise Service (porta 8082) - Valida tokens

Cada microserviço deve usar a **mesma chave secreta** para validar tokens JWT.