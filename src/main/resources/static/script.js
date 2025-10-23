const calcularBtn = document.getElementById('calcularBtn');
const resultadoDiv = document.getElementById('resultado');

calcularBtn.addEventListener('click', () => {
    const latInicio = document.getElementById('latInicio').value;
    const lonInicio = document.getElementById('lonInicio').value;
    const latFim = document.getElementById('latFim').value;
    const lonFim = document.getElementById('lonFim').value;
    const perfil = document.getElementById('perfil').value; // Pega o perfil selecionado
    const usuarioId = document.getElementById('usuarioId').value; // Pega o ID do usuário

    resultadoDiv.textContent = 'Calculando...';

    // Monta o corpo da requisição para o backend
    const requestData = {
        // Ajuste os nomes dos campos aqui para corresponder EXATAMENTE
        // ao que o DTO do seu backend espera no endpoint /rotas/calcular
        usuarioId: parseInt(usuarioId), // Converte para número se necessário
        perfil: perfil, // Envia o perfil selecionado
        coordenadas: { // Supondo que você tenha um objeto para coordenadas
            latInicio: parseFloat(latInicio),
            lonInicio: parseFloat(lonInicio),
            latFim: parseFloat(latFim),
            lonFim: parseFloat(lonFim)
        }
        // Se o seu DTO for diferente, ajuste a estrutura acima!
    };

    // Faz a chamada para a API do backend
    fetch('/rotas/calcular', { // Certifique-se que este é o endpoint correto
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            // Se precisar de autenticação, adicione o header Authorization aqui
            // 'Authorization': 'Bearer SEU_TOKEN_JWT'
        },
        body: JSON.stringify(requestData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Erro na API: ${response.status} ${response.statusText}`);
        }
        return response.json(); // Pega a resposta JSON da API
    })
    .then(data => {
        // Exibe a resposta JSON completa formatada no HTML
        resultadoDiv.textContent = JSON.stringify(data, null, 2); // O 'null, 2' formata o JSON

        // --- Extra: Mostrar informações básicas ---
        if (data.trip && data.trip.summary) {
            const tempoSegundos = data.trip.summary.time;
            const distanciaKm = data.trip.summary.length;
            const tempoMinutos = Math.round(tempoSegundos / 60);
            resultadoDiv.textContent += `\n\n--- Resumo ---`;
            resultadoDiv.textContent += `\nTempo estimado: ${tempoMinutos} minutos`;
            resultadoDiv.textContent += `\nDistância: ${distanciaKm.toFixed(2)} km`;
        }
         if (data.trip && data.trip.legs && data.trip.legs[0].maneuvers && data.trip.legs[0].maneuvers.length > 0) {
             resultadoDiv.textContent += `\n\n--- Primeira Instrução ---`;
             resultadoDiv.textContent += `\n${data.trip.legs[0].maneuvers[0].instruction}`;
         }
        // --- Fim do Extra ---

    })
    .catch(error => {
        console.error('Erro ao calcular rota:', error);
        resultadoDiv.textContent = `Erro ao calcular rota:\n${error.message}\n\nVerifique o console para mais detalhes e se o backend está rodando.`;
    });
});