// Aguarda o HTML carregar completamente
document.addEventListener('DOMContentLoaded', () => {

    // --- ELEMENTOS DO HTML ---
    const form = document.getElementById('form-rota');
    const partidaInput = document.getElementById('ponto-partida');
    const destinoInput = document.getElementById('ponto-destino');
    const partidaSugestoes = document.getElementById('partida-sugestoes');
    const destinoSugestoes = document.getElementById('destino-sugestoes');
    const perfilSelect = document.createElement('input'); // input oculto para guardar valor
    perfilSelect.type = 'hidden';
    perfilSelect.value = 'pedestrian'; // padrão
    form.appendChild(perfilSelect);
    const buscarButton = document.getElementById('btn-buscar-rota');
    const summaryBox = document.getElementById('rota-summary-box');

    // --- PONTOS DE API ---
    const GEO_API_BASE = 'https://nominatim.openstreetmap.org/search';
    const ROTA_API = '/rotas/calcular';
    const PONTOS_API = '/pontos-acessibilidade';

    // --- ÍCONE PERSONALIZADO ---
    const accessibleIcon = L.icon({
        iconUrl: 'https://cdn-icons-png.flaticon.com/512/107/107798.png',
        iconSize: [25, 25], iconAnchor: [12, 25], popupAnchor: [0, -25]
    });

    // --- CONFIGURAÇÃO DO MAPA ---
    const map = L.map('map').setView([-26.9187, -49.066], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);

    let rotaAtualLayer = null;
    let coordsPartidaSelecionada = null;
    let coordsDestinoSelecionada = null;

    // --- LÓGICA PRINCIPAL ---
    carregarPontosDeAcessibilidade();
    setupAutocomplete(partidaInput, partidaSugestoes, (coords) => coordsPartidaSelecionada = coords);
    setupAutocomplete(destinoInput, destinoSugestoes, (coords) => coordsDestinoSelecionada = coords);
    form.addEventListener('submit', onFormSubmit);

    // --- BOTÕES DE PERFIL ---
    const profileButtons = document.querySelectorAll('.profile-btn');
    profileButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            profileButtons.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            perfilSelect.value = btn.dataset.value;
        });
    });

    // --- FUNÇÕES ---

    async function carregarPontosDeAcessibilidade() {
        try {
            const response = await fetch(PONTOS_API);
            if (!response.ok) throw new Error(`Não foi possível carregar os pontos (Status: ${response.status})`);
            const pontosAcessibilidade = await response.json();

            if (!pontosAcessibilidade || pontosAcessibilidade.length === 0) {
                console.warn("Nenhum ponto de acessibilidade encontrado.");
                return;
            }

            const markersLayer = L.layerGroup();
            pontosAcessibilidade.forEach(item => {
                const ponto = item.ponto;
                const tipoAcesso = item.tipoAcessibilidade;
                if (ponto && ponto.latitude && ponto.longitude) {
                    const marker = L.marker([ponto.latitude, ponto.longitude], { icon: accessibleIcon });
                    const nome = ponto.nome || 'Ponto de Acessibilidade';
                    const tipo = tipoAcesso ? tipoAcesso.tipo : 'Acessível';
                    marker.bindPopup(`<b>${nome}</b><br>${tipo}`);
                    markersLayer.addLayer(marker);
                }
            });
            markersLayer.addTo(map);
            console.log(`Carregados ${pontosAcessibilidade.length} pontos de acessibilidade.`);
        } catch (error) {
            console.error("Erro ao carregar pontos:", error);
            mostrarMensagem(error.message, 'error');
        }
    }

    async function onFormSubmit(event) {
        event.preventDefault();
        buscarButton.disabled = true;
        buscarButton.textContent = 'Calculando rota...';
        summaryBox.style.display = 'none';

        if (rotaAtualLayer) map.removeLayer(rotaAtualLayer);

        try {
            if (!coordsPartidaSelecionada) throw new Error('Selecione um ponto de partida da lista.');
            if (!coordsDestinoSelecionada) throw new Error('Selecione um ponto de destino da lista.');

            const pedidoRota = {
                usuarioId: "6902889dcd6a0a23937c55d2",
                perfil: perfilSelect.value,
                coordenadas: {
                    latInicio: parseFloat(coordsPartidaSelecionada.lat),
                    lonInicio: parseFloat(coordsPartidaSelecionada.lon),
                    latFim: parseFloat(coordsDestinoSelecionada.lat),
                    lonFim: parseFloat(coordsDestinoSelecionada.lon)
                }
            };

            const dadosRotaString = await calcularRotaBackend(pedidoRota);
            const rotaValhalla = JSON.parse(dadosRotaString);

            console.log("Retorno do backend:", rotaValhalla);

            if (rotaValhalla?.trip?.legs?.[0]?.shape) {
                const pontosDecodificados = decodePolyline(rotaValhalla.trip.legs[0].shape);

                let corDaRota = 'blue';
                if (perfilSelect.value === 'wheelchair') corDaRota = '#006400';
                if (perfilSelect.value === 'pedestrian_avoid_stairs') corDaRota = '#FF8C00';

                rotaAtualLayer = L.polyline(pontosDecodificados, { color: corDaRota, weight: 6 }).addTo(map);
                map.fitBounds(rotaAtualLayer.getBounds());

                mostrarMensagem("Rota calculada com sucesso!", 'success');
            } else {
                throw new Error('Não foi possível encontrar uma rota entre esses pontos.');
            }

        } catch (error) {
            mostrarMensagem(error.message, 'error');
            console.error("Erro detalhado:", error);
        } finally {
            buscarButton.disabled = false;
            buscarButton.textContent = 'Buscar Rota';
        }
    }

    function mostrarMensagem(mensagem, tipo = 'warning') {
        summaryBox.textContent = mensagem;
        summaryBox.className = `rota-summary ${tipo}`;
        summaryBox.style.display = 'block';
    }

    async function calcularRotaBackend(pedido) {
        const response = await fetch(ROTA_API, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(pedido)
        });

        const responseBodyText = await response.text();

        if (!response.ok) {
            try {
                const erroJson = JSON.parse(responseBodyText);
                throw new Error(`Erro no servidor: ${erroJson.message || 'Erro de validação'}`);
            } catch (e) {
                console.error("Erro não JSON:", responseBodyText);
                throw new Error(`Servidor falhou (Status: ${response.status})`);
            }
        }

        if (!responseBodyText) throw new Error('Resposta vazia do servidor.');

        return responseBodyText;
    }

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
                    console.error("Erro no autocomplete:", error);
                    containerSugestoes.innerHTML = '';
                }
            }, 300);
        });

        document.addEventListener('click', (e) => {
            if (!containerSugestoes.contains(e.target) && e.target !== input) {
                containerSugestoes.innerHTML = '';
            }
        });
    }

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
        return await response.json();
    }

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
