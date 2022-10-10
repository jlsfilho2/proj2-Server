# proj2-Server

Para consumo da aplicação Evita.
Endpoit: http://whm.joao1866.c41.integrator.host:9206

## Configuração da aplicação
a aplicação buscará suas configurações na pasta /appservers/private/springboot/properties, no arquivo application.properties

a configuração padrão é :
```
server.error.include-message=always
spring.mvc.locale=pt_BR

spring.datasource.driver=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/oauth
spring.datasource.username=<>
spring.datasource.password=<>

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

logging.file.max-size=3MB
logging.file.name=${logging.file.path}/Evita.log
logging.file.path=/private/springboot/logs/
logging.pattern.dateformat=dd/MM/yy HH:mm:ss
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%
logging.level.root=INFO
server.port=${port:8080}

hibernate.hbm2ddl.auto=create
hibernate.show_sql=true
hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
hibernate.connection.url=jdbc:postgresql://localhost:5432/<>
```

## manipulação de Usuários /usuario
### criar
POST:
```
{
	
	"nome":"Marja",
	"userId":"marja321",
	"pass":"teste456",
	"email" :"marja@hotmail.com",
	"enderecos":[{
		"uf":"RS",
		"cep":"90130180",
		"complemento":"casa",
		"logradouro":"alguma rua",
		"cidade":"Porto Alegre",
		"bairro":"centro",
		"numero": 567
		}],
	"categorias" : [{
		"categoria":"MANUTENCAO_ELETRICA",
		"valor":10.50
		},
		{
		"categoria":"MANUTENCAO_HIDRAULICA",
		"valor":15.50
		}],
	"cliente":true
}
```
São categorias válidas: MANUTENCAO_ELETRICA, MANUTENCAO_HIDRAULICA, HIGIENE_PESSOAL, LIMPEZA, CUIDADOS
### editar
PUT:
```
{
	"id":1,
	"nome":"Marja",
	"userId":"marja321",
	"pass":"teste789",
	"enderecos":[{
		"id":1,
		"uf":"RS",
		"cep":"90130180",
		"complemento":"casa",
		"logradouro":"alguma rua",
		"cidade":"Porto Alegre",
		"bairro":"Cidade Baixa",
		"numero": 567
		}],
	"cliente":true
}
```
Obs: ao se passar um array vazio para edição, ocorre a exclusão dos registros, isso ocorre para as propriedades 'endereço' e 'categorias';

### consultar
GET: conforme exemplo http://whm.joao1866.c41.integrator.host:9206/usuario?userId=marja321
por userId ou email
```

{
   "id": 1,
   "nome": "Marja",
   "email": "marja@hotmail.com",
   "userId": "marja321",
   "enderecos": [   {
      "id": 1,
      "uf": "RS",
      "cep": "90130180",
      "complemento": "casa",
      "logradouro": "alguma rua",
      "cidade": "Porto Alegre",
      "bairro": "Cidade Baixa",
      "numero": 567
   }],
   "cliente": true,
   "categorias":    [
            {
         "id": 1,
         "categoria": "MANUTENCAO_HIDRAULICA",
         "valor": 15.5
      },
            {
         "id": 2,
         "categoria": "MANUTENCAO_ELETRICA",
         "valor": 10.5
      }
   ]
}
```

### catálogo de usuários
GET: conforme exemplo http://whm.joao1866.c41.integrator.host:9206/usuarios?categoria=MANUTENCAO_ELETRICA&bairro=centro
por categoria e UF, cidade e bairro
```
[{
   "id": 2,
   "nome": "Marja",
   "email": "marja@hotmail.com",
   "userId": "marja321",
   "enderecos": [   {
      "id": 2,
      "uf": "RS",
      "cep": "90130180",
      "complemento": "casa",
      "logradouro": "alguma rua",
      "cidade": "Porto Alegre",
      "bairro": "centro",
      "numero": 567
   }],
   "cliente": true,
   "categorias":    [
            {
         "id": 3,
         "categoria": "MANUTENCAO_ELETRICA",
         "valor": 10.5
      },
            {
         "id": 4,
         "categoria": "MANUTENCAO_HIDRAULICA",
         "valor": 15.5
      }
   ]
}]
```
## manipulação de solicitações /solicitacao

### criar
POST:
```
{
	
	"enderecoRequisitante" : {
		"id":1
		},
	"userRequisitado" : {
		"id":2
		},
	"status" :"AGENDADO",
	"inicio":"28-09-2022 11:55",
	"fim":"29-09-2022 12:55",
	"categoria": "MANUTENCAO_HIDRAULICA"
}
```
Obs: são status válidos INICIADO, AGENDADO, CANCELADO e CONCLUIDO

### editar
PUT:
```
{
      "id":1,
      "status" :"CONCLUIDO"
}
```
### consultar
GET: conforme o exemplo: http://whm.joao1866.c41.integrator.host:9206/solicitacao?dataInicio=21-09-2022 00:00
por dataInicio, dataFim, dataInicio e dataFim, userRequisitanteId, userRequisitadoId e status

## manipulção de avaliações /avaliacao

### criar
POST:
```
{
	"solicitacao":{
	    "id":1
	},
	"nota":10,
	"comentario":"meu primeiro comentario"
}
```
### edição
PUT:

```
{
   "id":1,
   "nota":10,
   "comentario":"comentário editado"	
}
```

### consultar
GET: conforme o exemplo: http://whm.joao1866.c41.integrator.host:9206/avaliacao?userRequisitanteId=2
por dataInicio, dataFim, dataInicio e dataFim, userRequisitanteId e userRequisitadoId

## Outros métodos

### login
POST para http://whm.joao1866.c41.integrator.host:9206/loggin
```
{
	"email":"heleninha2@hotmail.com.br",
	"senha":"teste789"
}
```
Obs: caso as entradas sejam válidas, serão retornados as informações do usuário.
