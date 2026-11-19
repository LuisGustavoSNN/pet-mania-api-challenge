# Pet Mania API

API para cadastrar, consultar e editar pets, feita em Java com Spring Boot e Spring Data JPA. A aplicação também inclui uma página para acessar essas operações pelo navegador.

## Como executar

Use Java 21 ou superior. Clone o repositório e entre na pasta do projeto.

```bash
git clone https://github.com/LuisGustavoSNN/pet-mania-api-challenge.git
cd pet-mania-api-challenge
```

No Windows, execute `.\mvnw.cmd spring-boot:run`. No Linux ou macOS, execute `./mvnw spring-boot:run`.

A página abre em `http://localhost:8080` e a API fica em `http://localhost:8080/api/pets`. Por padrão, os dados ficam em um arquivo H2. Para usar MySQL, ative o perfil `mysql` e configure a conexão em `src/main/resources/application-mysql.properties`.

Para executar os testes, use `.\mvnw.cmd test` no Windows ou `./mvnw test` no Linux ou macOS.

## Endpoints da API

| Método | Rota | Operação | Resposta |
| --- | --- | --- | --- |
| GET | `/api/pets` | Listar pets | `200 OK` |
| GET | `/api/pets/{id}` | Buscar um pet | `200 OK` ou `404 Not Found` |
| POST | `/api/pets` | Cadastrar um pet | `201 Created` ou `400 Bad Request` |
| PUT | `/api/pets/{id}` | Editar um pet | `200 OK`, `400 Bad Request` ou `404 Not Found` |

### Exemplo de cadastro

Envie o JSON para `POST /api/pets` com `Content-Type: application/json`.

```json
{
  "nome": "Rex",
  "especie": "Cachorro",
  "raca": "Labrador",
  "idade": 3,
  "sexo": "Macho",
  "porte": "Grande",
  "personalidade": "Dócil e brincalhão",
  "foto": "",
  "disponivel": true
}
```

## Diagnóstico e Correções Realizadas (Passo 2)

Durante a análise inicial do projeto, foram identificados diversos problemas de sintaxe, estrutura e regras de negócio no ecossistema Spring Boot/JPA. Abaixo estão as principais correções efetuadas:

### 1. Entidade (`Pet.java`)

* **Ajuste de Pacote:** Correção na sintaxe do `package`.
* **Mapeamento de Chave Primária:** O atributo `nome` estava incorretamente anotado com `@Id` e `@GeneratedValue`. Foi criado o campo `Long id` para atuar como chave primária autoincrementada.
* **Mapeamento JPA:** Adicionada a anotação `@Entity` e corrigida a sintaxe da anotação `@Table(name = "tb_pet")`.
* **Construtor Padrão:** Inclusão do construtor sem argumentos (`public Pet() {}`), requisito obrigatório do JPA.
* **Tipagem de Atributos e Encapsulamento:** Correção de incompatividades nos métodos *Getters* e *Setters* (ex: idade declarada como `int` com getter retornando `String`).

### 2. Repositório (`PetRepository.java`)

* **Generics e Tipos Wrapper:** Substituição da herança `CrudRepository<Pet, int>` por `JpaRepository<Pet, Long>`, corrigindo a utilização de tipo primitivo no ID.

### 3. Controller (`PetController.java`)

* **Anotações de Mapeamento:** Correção de erros ortográficos (`@RequestMapping`) e remoção da rota de dentro do `@RestController`.
* **Readequação de Verbos HTTP:**

  * Reorganização dos métodos HTTP (`@GetMapping` para listagem e `@PostMapping` para criação).
  * Adição da anotação `@RequestBody` no método de criação de pets.
* **Implementação de Endpoints Faltantes:**

  * Implementado o endpoint de busca por ID (`GET /api/pets/{id}`).
* **Configuração de CORS:** Adicionada a anotação `@CrossOrigin` para permitir a comunicação com o front-end web.

## Testes no Postman

### Cadastro

<img width="1911" height="1006" alt="Captura de tela 2026-11-19 084608" src="https://github.com/user-attachments/assets/0a76a9db-335f-4040-80c7-5f068dce1d31" />

### Listagem

<img width="1919" height="1005" alt="Captura de tela 2026-11-19 084953" src="https://github.com/user-attachments/assets/00aaa522-dbd4-405d-bd53-69337f14efc4" />
