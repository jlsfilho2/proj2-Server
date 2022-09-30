# proj2-Server

Para consumo da aplicação Evita.
Endpoit: http://whm.joao1866.c41.integrator.host:9206

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
	"tipo":"CLIENTE"
}
```

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
	"tipo":"PRESTADOR"
}
```
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
   "tipo": "PRESTADOR",
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
## manipulação de solicitacações /solicitacao

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
### editar
PUT:
```
{
      "id":1,
      "status" :"CONCLUIDO"
}
```
### consultar
GET: conforme o exemplo: http://whm.joao1866.c41.integrator.host:9206/solicitaca?dataInicio=21-09-2022 00:00
por dataInicio, dataFim, dataInicio e dataFim, userRequisitanteId, userRequisitadoId e status

