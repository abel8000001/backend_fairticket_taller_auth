## Nombre y variables
Abel Santiago Garcia Ortiz

- Puerto de Hash: CryptoHasher
- Base Path: /api/access
- Claim JWT: "permission"
- Propiedad YAML: jwt.signing.secret / jwt.signing.ttl

## Comandos curl

### 1) Registro

```bash
curl -X POST http://localhost:6001/api/access/register \
	-H "Content-Type: application/json" \
	-d '{
		"name": "Organizador Demo",
		"email": "organizer@fairticket.com",
		"role": "ORGANIZER",
		"password": "password"
	}'
```

### 2) Login

```bash
curl -X POST http://localhost:6001/api/access/login \
	-H "Content-Type: application/json" \
	-d '{
		"email": "organizer@fairticket.com",
		"password": "password"
	}'
```

### 3) Acceso con token (autenticado)

```bash
curl -X GET "http://localhost:6001/api/users" \
	-H "Authorization: Bearer TOKEN_AQUI"
```

### 4) Acceso denegado (403)

Registrar y hacer login con un usuario BUYER, luego intentar crear evento. No debe funcionar porque solo ORGANIZER puede hacer POST en /api/events

```bash
curl -X POST http://localhost:6001/api/events \
	-H "Content-Type: application/json" \
	-H "Authorization: Bearer TOKEN_BUYER_AQUI" \
	-d '{
		"name": "Concierto Test",
		"description": "Evento de prueba",
		"venue": "Coliseo",
		"eventDate": "2026-12-20T20:00:00",
		"saleStartDate": "2026-10-01T08:00:00",
		"saleEndDate": "2026-12-15T23:59:00",
		"category": "MUSIC",
		"maxTicketsPerUser": 4,
		"totalTickets": 1000,
		"organizerId": "00000000-0000-0000-0000-000000000000"
	}'
```