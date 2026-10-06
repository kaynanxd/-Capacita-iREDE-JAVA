# TaskManager

Aplicação desktop para cadastro e acompanhamento de tarefas, desenvolvida em Java com JavaFX, FXML, JDBC, SQLite, Generics e JUnit.

## Requisitos

- JDK 21
- Maven 3.9 ou superior

## Executar

```bash
mvn clean javafx:run
```

O banco `taskmanager.db` é criado automaticamente na primeira execução.

## Testar

```bash
mvn test
```

Os testes do DAO usam um banco SQLite em memória e não alteram o banco da aplicação.

## Funcionalidades

- cadastrar e editar tarefas;
- listar tarefas em uma tabela;
- concluir ou reabrir uma tarefa;
- remover com confirmação;
- validar título obrigatório e limite de 100 caracteres;
- persistir os dados em SQLite.

## Organização

- `model`: entidade `Tarefa`;
- `repository`: contrato e coleção genéricos;
- `database`: conexão e criação da tabela;
- `dao`: operações JDBC;
- `service`: validações e regras da aplicação;
- `controller`: integração entre JavaFX e serviço;
- `resources`: telas FXML e estilo CSS.

