# EasyMob 🛣️

[![Java](https://img.shields.io/badge/Java-17-red.svg)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0-green.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-5.0-green.svg)](https://www.mongodb.com/)
[![Docker](https://img.shields.io/badge/Docker-24.0-blue.svg)](https://www.docker.com/)

Um sistema de roteamento inteligente que sugere rotas personalizadas conforme a necessidade do usuário, integrando com o motor de roteamento Valhalla e considerando critérios de acessibilidade.

## Sobre o Projeto

**EasyMob** é uma solução inovadora de roteamento urbano que considera diferentes necessidades de mobilidade, oferecendo rotas otimizadas com foco em acessibilidade.
**Contexto:** Sistema desenvolvido para facilitar a mobilidade urbana com foco em acessibilidade, utilizando tecnologias modernas e integração com serviços especializados.

## Estrutura do Projeto

```
ProjetoAcessibilidade/
├── 📁 controller/                 # Controladores da API
│   ├── AuthController.java        # Autenticação de usuários
│   ├── PontoController.java       # Gestão de pontos
│   ├── RotaController.java        # Cálculo de rotas
│   ├── RotasFavoritasController.java # Gestão de rotas favoritas
│   ├── UsuarioController.java     # Gestão de usuários
│   └── (Outros controllers)
├── 📁 dto/                        # Data Transfer Objects
├── 📁 model/                      # Entidades do domínio
├── 📁 repository/                 # Camada de acesso a dados
├── 📁 service/                    # Lógica de negócio
├── 📁 security/                   # Configurações de segurança
├── 📁 config/                     # Configurações da aplicação
├── 📁 exceptions/                 # Tratamento de exceções
├── 📁 utils/                      # Utilitários e helpers
└── ProjetoAcessibilidadeApplication.java # Classe principal
```

## Tecnologias e Conceitos Implementados

### Core Framework
- **Spring Boot 3** - Framework principal
- **Java 17** - Linguagem de programação
- **MongoDB** - Banco de dados NoSQL
- **Maven** - Gerenciamento de dependências

### Integração e APIs Externas
- **Valhalla Routing Engine** - Motor de roteamento com OpenStreetMap
- **Docker** - Containerização do serviço Valhalla
- **RESTful APIs** - Integração com serviços externos

### Segurança e Autenticação
- **Spring Security** - Framework de segurança
- **JWT (JSON Web Tokens)** - Autenticação stateless
- **Bearer Authentication** - Controle de acesso

### Arquitetura e Padrões
- **MVC Pattern** - Model-View-Controller
- **Repository Pattern** - Abstração da camada de dados
- **Dependency Injection** - Inversão de controle
- **DTO Pattern** - Separação entre modelos de domínio e transferência
- **Service Layer** - Separação de responsabilidades

### Documentação e Qualidade
- **Global Exception Handling** - Tratamento centralizado de erros
- **Input Validation** - Validação de dados de entrada
- **RESTful Principles** - Princípios de design REST

## Funcionalidades

###  Autenticação e Autorização
- **Autenticação JWT** com tokens seguros
- **Gestão de usuários** e perfis
- **Proteção de endpoints** com Spring Security

###  Sistema de Roteamento
- **Cálculo de rotas personalizadas** baseado no Valhalla
- **Rotas otimizadas** considerando acessibilidade
- **Matriz de tempo + distância** para múltiplos pontos
- **Isochronas** - áreas de alcance temporal
- **Otimização de trajetos** (Travelling Salesman)

###  Gestão de Pontos de Interesse
- **CRUD completo** de pontos
- **Categorização** por tipos de acessibilidade
- **Geolocalização** e metadados
- **Integração** com sistema de rotas

###  Recursos de Usuário
- **Rotas favoritas** - salvamento e gestão
- **Histórico de rotas** - consulta de trajetos anteriores
- **Preferências pessoais** - configurações de acessibilidade

###  Sistema de Feedback
- **Avaliação de rotas** - feedback dos usuários
- **Comentários e sugestões** - melhoria contínua
- **Análise de qualidade** das rotas sugeridas

## Configuração e Instalação

### Pré-requisitos
- Java 17 JDK
- Maven 3.6+
- MongoDB 5.0+
- Docker e Docker Compose

### Configuração do Valhalla com Docker

1. **Suba o serviço Valhalla**:
   ```bash
   docker-compose up -d valhalla
   ```

2. **Configure a conexão** no `application.properties`:
   ```properties
   valhalla.api.url=http://localhost:8002
   spring.data.mongodb.uri=mongodb://localhost:27017/easymob
   jwt.secret=sua-chave-secreta-jwt-aqui
   ```

3. **Execute a aplicação**:
   ```bash
   mvn spring-boot:run
   ```

## Uso da API

### Autenticação

1. **Login**:
   ```http
   POST /api/auth/login
   Content-Type: application/json

   {
     "email": "usuario@exemplo.com",
     "senha": "senha123"
   }
   ```

### Exemplos de Requisições

**Calcular rota (requer autenticação)**:
```http
POST /api/rota/calcular
Authorization: Bearer {seu-jwt-token}
Content-Type: application/json

{
  "origem": "-27.5935,-48.5585",
  "destino": "-27.5945,-48.5478",
  "preferencias": {
    "acessibilidade": true,
    "evitarEscadas": true,
    "rampasObrigatorias": false
  }
}
```

**Listar pontos de acessibilidade**:
```http
GET /api/pontos
Authorization: Bearer {seu-jwt-token}
```

**Adicionar rota aos favoritos**:
```http
POST /api/rotas-favoritas
Authorization: Bearer {seu-jwt-token}
Content-Type: application/json

{
  "nome": "Casa-Trabalho",
  "rotaId": "507f1f77bcf86cd799439011",
  "usuarioId": "507f1f77bcf86cd799439012"
}
```

## Segurança

- **JWT Authentication** com Spring Security
- **Proteção de endpoints** sensíveis
- **Validação de dados** com Bean Validation
- **Global Exception Handling** para tratamento seguro de erros
- **CORS** configurado para frontend

## Arquitetura

### Padrões de Design
- **MVC Pattern** - Separação clara de responsabilidades
- **Repository Pattern** - Abstração do MongoDB
- **Dependency Injection** - Injeção do Spring Framework
- **DTO Pattern** - Isolamento dos modelos de API
- **Service Layer** - Centralização da lógica de negócio

### Camadas da Aplicação
```
ProjetoAcessibilidade/
├── Controller/     # Endpoints da API REST
├── Service/        # Lógica de negócio e regras
├── Repository/     # Acesso ao MongoDB
├── Model/          # Entidades de domínio
├── DTO/           # Objetos de transferência
├── Security/       # Configurações de autenticação
└── Config/         # Configurações da aplicação
```

## Módulos Implementados

### Navegação e Roteamento
- `RotaController` - Cálculo e gestão de rotas
- `RotasFavoritasController` - Rotas salvas dos usuários
- `PontoController` - Pontos de interesse e acessibilidade

### Gestão de Usuários
- `UsuarioController` - CRUD de usuários
- `AuthController` - Autenticação e autorização

### Sistema de Apoio
- `FeedbackController` - Avaliações e comentários
- `TipoAcessibilidadeController` - Categorias de acessibilidade
- `TipoPontoController` - Classificação de pontos

## Integração Valhalla

### Funcionalidades do Motor de Roteamento
- **Roteamento multimodal** - pedestre, veicular, bicicleta
- **Cálculo de matriz** - tempo e distância entre múltiplos pontos
- **Isochronas** - áreas alcançáveis em determinado tempo
- **Map Matching** - ajuste de trajetos ao mapa
- **Otimização TSP** - Travelling Salesman Problem

### Configuração Docker
```yaml
valhalla:
  image: ghcr.io/gis-ops/docker-valhalla/valhalla:latest
  ports:
    - "8002:8002"
  volumes:
    - ./valhalla_data:/custom_files
```

## Aprendizados

### Conceitos Dominados
- **Spring Boot 3** e desenvolvimento de APIs REST
- **Integração com APIs externas** (Valhalla)
- **MongoDB** e padrão Repository
- **Spring Security** com JWT
- **Docker** e containerização de serviços
- **Global Exception Handling**
- **Validação de dados** com Bean Validation

### Habilidades Desenvolvidas
- Integração com motores de roteamento especializados
- Desenvolvimento de sistemas de geolocalização
- Gestão de dados espaciais com MongoDB
- Containerização de serviços com Docker
- Desenvolvimento de APIs com foco em acessibilidade

### Competências Técnicas
- **Backend:** Spring Boot, RESTful APIs, MVC Pattern
- **Banco de Dados:** MongoDB, Geospatial Queries
- **Integração:** Valhalla API, Docker, REST Clients
- **Segurança:** JWT, Spring Security, Authentication
- **Ferramentas:** Maven, Docker, Postman

---

**🛣️ Desenvolvido com 💙 para facilitar a mobilidade urbana acessível**
