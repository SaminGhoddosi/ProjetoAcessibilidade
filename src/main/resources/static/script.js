// Aguarda o HTML carregar completamente
document.addEventListener('DOMContentLoaded', () => {

    // --- ELEMENTOS DO HTML ---
    const form = document.getElementById('form-rota');
    const partidaInput = document.getElementById('ponto-partida');
    const destinoInput = document.getElementById('ponto-destino');
    const partidaSugestoes = document.getElementById('partida-sugestoes');
    const destinoSugestoes = document.getElementById('destino-sugestoes');
    const perfilSelect = document.getElementById('perfil-usuario');
    const buscarButton = document.getElementById('btn-buscar-rota');

    // --- PONTOS DE API ---
    const GEO_API_BASE = 'https://nominatim.openstreetmap.org/search';
    const ROTA_API = '/rotas/calcular'; // URL Correta!

    // --- CONFIGURAÇÃO DO MAPA ---
    // (Se o mapa sumiu, foi porque o script quebrou antes de chegar aqui)
    const map = L.map('map').setView([-26.9187, -49.066], 13); // Blumenau
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);

    let rotaAtualLayer = null;
    let coordsPartidaSelecionada = null;
    let coordsDestinoSelecionada = null;

    // --- LÓGICA DE AUTOCOMPLETAR ---
    setupAutocomplete(partidaInput, partidaSugestoes, (coords) => {
        coordsPartidaSelecionada = coords;
    });
    setupAutocomplete(destinoInput, destinoSugestoes, (coords) => {
        coordsDestinoSelecionada = coords;
    });

    /**
     * Função que cria a lógica de autocompletar
     */
    function setupAutocomplete(input, containerSugestoes, onSelect) {
        let debounceTimer;
        input.addEventListener('input', () => {
            clearTimeout(debounceTimer);
            const query = input.value;
            if (query.length < 3) {
                containerSugestoes.innerHTML = '';
                onSelect(null);
                return;
            }
            debounceTimer = setTimeout(async () => {
                try {
                    const sugestoes = await geocodificar(query);
                    mostrarSugestoes(sugestoes, containerSugestoes, input, onSelect);
                } catch (error) {
                    console.error("Erro no autocompletar:", error);
                    containerSugestoes.innerHTML = ''; // Limpa em caso de erro
                }
            }, 300);
        });

        // Fecha a lista se clicar fora
        document.addEventListener('click', (e) => {
            if (!containerSugestoes.contains(e.target) && e.target !== input) {
                 containerSugestoes.innerHTML = '';
            }
        });
    }

    /**
     * Mostra as sugestões (lista) na tela
     */
    function mostrarSugestoes(sugestoes, container, input, onSelect) {
        container.innerHTML = '';
        if (!sugestoes || sugestoes.length === 0) return;

        sugestoes.forEach(sugestao => {
            const itemDiv = document.createElement('div');
            itemDiv.className = 'autocomplete-item';
            itemDiv.textContent = sugestao.display_name;
            itemDiv.addEventListener('click', () => {
                input.value = sugestao.display_name;
                onSelect({ lat: sugestao.lat, lon: sugestao.lon });
                container.innerHTML = '';
            });
            container.appendChild(itemDiv);
        });
    }

    // --- EVENTO PRINCIPAL ---
    form.addEventListener('submit', async (event) => {
        event.preventDefault();
        buscarButton.disabled = true;
        buscarButton.textContent = 'Calculando rota...';
        if (rotaAtualLayer) {
            map.removeLayer(rotaAtualLayer);
        }

        try {
            if (!coordsPartidaSelecionada) throw new Error('Ponto de partida inválido. Selecione um da lista.');
            if (!coordsDestinoSelecionada) throw new Error('Ponto de destino inválido. Selecione um da lista.');

            // JSON correto para o CalcularRotaDTO
            const pedidoRota = {
                usuarioId: "6902889dcd6a0a23937c55d2",
                perfil: perfilSelect.value,
                coordenadas: {
                    latInicio: coordsPartidaSelecionada.lat,
                    lonInicio: coordsPartidaSelecionada.lon,
                    latFim: coordsDestinoSelecionada.lat,
                    lonFim: coordsDestinoSelecionada.lon
                }
            };

            const dadosRotaString = await calcularRotaBackend(pedidoRota);
            const rotaValhalla = JSON.parse(dadosRotaString); // <-- Cuidado aqui, se a string for vazia, vai dar erro

            if (rotaValhalla && rotaValhalla.trip && rotaValhalla.trip.legs[0] && rotaValhalla.trip.legs[0].shape) {
                const pontosDecodificados = decodePolyline(rotaValhalla.trip.legs[0].shape);
                rotaAtualLayer = L.polyline(pontosDecodificados, { color: 'blue', weight: 6 }).addTo(map);
                map.fitBounds(rotaAtualLayer.getBounds());
            } else {
                // Pode ser que o Valhalla simplesmente não achou rota (ex: ilhas)
                throw new Error('Não foi possível encontrar uma rota acessível entre esses dois pontos.');
            }

        } catch (error) {
             // Mostra erros de validação, erros de rede, ou erros de JSON.parse
            alert(error.message);
            console.error("Erro detalhado:", error); // Loga o erro completo no console
        } finally {
            buscarButton.disabled = false;
            buscarButton.textContent = 'Buscar Rota';
        }
    });

    // --- FUNÇÕES AUXILIARES ---

    async function geocodificar(endereco) {
        const BLUMENAU_VIEWBOX = '-49.204,-26.822,-49.006,-26.978';
        const params = new URLSearchParams({
            q: endereco,
            format: 'json',
            countrycodes: 'br',
            viewbox: BLUMENAU_VIEWBOX
        });
        const response = await fetch(`${GEO_API_BASE}?${params.toString()}`);
        if (!response.ok) throw new Error('Erro na API de geocodificação.');
        return await response.json(); // Retorna a LISTA de sugestões
    }

    /**
     * Envia o pedido de rota para o backend
     * (CORRIGIDO para tratar o 'Unexpected token <')
     */
    async function calcularRotaBackend(pedido) {
        const response = await fetch(ROTA_API, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(pedido)
        });

        // Lê a resposta SEMPRE como TEXTO primeiro
        const responseBody = await response.text();

        if (!response.ok) {
            // Se falhou, tenta ver se o TEXTO é um JSON de erro
            try {
                const erroJson = JSON.parse(responseBody);
                throw new Error(`Erro no servidor: ${erroJson.message || 'Erro de validação'}`);
            } catch (e) {
                // Se NÃO for JSON (provavelmente HTML de erro do Spring)
                console.error("O servidor enviou um erro que não é JSON:", responseBody);
                throw new Error(`O servidor falhou (Status: ${response.status}). Verifique o log do IntelliJ.`);
            }
        }

        // Se deu tudo certo (200 OK), retorna a STRING da rota
        // Verifica se a string não está vazia antes de retornar
        if (!responseBody) {
             throw new Error('O servidor retornou uma resposta vazia.');
        }
        return responseBody;
    }

    /**
     * Decodifica o formato polyline6 do Valhalla
     */
    function decodePolyline(str, precision) {
        let index = 0, lat = 0, lng = 0, coordinates = [], shift = 0, result = 0, byte = null,
            latitude_change, longitude_change,
            factor = Math.pow(10, precision || 6);
        while (index < str.length) {
            byte = null; shift = 0; result = 0;
            do {
                byte = str.charCodeAt(index++) - 63;
                result |= (byte & 0x1f) << shift;
                shift += 5;
            } while (byte >= 0x20);
            latitude_change = ((result & 1) ? ~(result >> 1) : (result >> 1));
            shift = result = 0;
            do {
                byte = str.charCodeAt(index++) - 63;
                result |= (byte & 0x1f) << shift;
                shift += 5;
            } while (byte >= 0x20);
            longitude_change = ((result & 1) ? ~(result >> 1) : (result >> 1));
            lat += latitude_change;
            lng += longitude_change;
            coordinates.push([lat / factor, lng / factor]);
        }
        return coordinates;
    }
});