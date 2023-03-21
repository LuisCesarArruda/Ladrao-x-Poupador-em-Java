package algoritmo;

public class Poupador extends ProgramaPoupador {
	int ambience[][] = new int[30][30]; // Matriz do ambiente para computar pesos em espaÃ§os visitados
	int bankWay[][] = new int[30][30];
	double probMatrix[][] = { { 0.10, 0.25, 0.50, 0.25, 0.10 }, { 0.25, 0.50, 1, 0.50, 0.25 }, { 0.50, 1, 1, 1, 0.50 },
			{ 0.25, 0.50, 1, 0.50, 0.25 }, { 0.10, 0.25, 0.50, 0.25, 0.10 } };
	//matriz de probabilidades medidas em distÃ¢ncia do centro (posiÃ§Ã£o atual do agente na dada jogada)
	// locais jÃ visitados
	int matrizVisao[][] = new int[5][5]; 
	int matrizOlfato[][] = new int[3][3];
	// possibilidades de movimentos no
	// campo,
	// levando em conta o valor do peso dos
	// pisos proximos, presentes na
	// pespectiva
	// do agente.
	int medidaDesempenho = 0;
	int posi = 0;
	int last = 0;
	java.awt.Point localBanco;

	// TODO Montando a Matriz VisÃ‘Æ’o
	// TABELA DE CODIGOS DE VALORES DA MATRIZ VISÃ�Â£O

	// CÃ‘â€œdigo || Significado
	// -2 Sem visÃ‘Æ’o para o local
	// -1 Fora do ambiente (mundo exterior)
	// 0 CÃ‘â€°lula vazia (sem nenhum agente)
	// 1 Parede
	// 3 Banco
	// 4 Moeda
	// 5 Pastinha do Poder
	// 100 Poupador
	// 200 LadrÃ‘Æ’o

	// Percorrendo o vetor de visÃ‘Æ’o e armazenando em uma matriz, a qual Ã‘â€°
	// populada
	// da seguinte maneira: o primeiro for conta as linhas e o segundo as
	// colunas
	// saindo das seguintes formas: 0 1 2 3 4 forma a primeira linha depois 5 6
	// 7 8
	// 9 formando a segunda linha e assim atÃ‘â€° o 24Ã�Å¡ valor. Montando a matriz
	// do
	// valor visÃ‘Æ’o
	// o valor de k+j, exemplificando fica o seguinte na primeira linha sera 0;
	// 1;
	// 2; 3; 4; (monta a primeira linha da matriz) e pega os valores 0,1,2,3,4
	// do
	// vetor que Ã‘â€° retornado pelo sensor.getVisaoIdentificacao() e na segunda
	// linha
	// passa a ser o valor seguinte 5,6,7,8,9 atraves da variavel int cont ela
	// armazena a ultima posiÃ‘â€¡Ã‘Æ’o e percorrer os valores do vetor de tamanho 25
	// (contando de 0 a 24).

	// OBS: Atraves do valor na matriz podemos escolher qual "item" ir atras,
	// andar
	// atÃ‘â€° ele, qual tomada de decisÃ‘Æ’o podendo settar a posiÃ‘â€¡Ã‘Æ’o
	// (setLocation)
	// pegando o x + o numero da linha e o y+ o numero da coluna da matriz visao
	// com
	// os x e y da location do agente.

	public int[][] getMatrizVisaoAtual() {

		int matrizVisaoAux[][] = new int[5][5];
		int cont = 0;
		for (int k = 0; k < 5; k++) {
			for (int j = 0; j < 5; j++) {
				if (cont == 24) {
					matrizVisaoAux[k][j] = 123;
				} else {
					matrizVisaoAux[k][j] = sensor.getVisaoIdentificacao()[cont];
					// System.out.println(sensor.getVisaoIdentificacao()[cont]+":"+descricaoCodigo(sensor.getVisaoIdentificacao()[cont]));
					cont++;
				}
			}
		}
		return matrizVisaoAux;
	}

	public String descricaoCodigo(int codigo) {
		switch (codigo) {
		case -2:
			return "sem visão para o local";
		case -1:
			return "Fora do ambiente(mundo exterior)";
		case 0:
			return "CÃlula vazia(sem nenhum agente)";
		case 1:
			return "Parede";
		case 3:
			return "Banco";
		case 4:
			return "Moeda";
		case 5:
			return "Partilha do Poder";
		case 100:
			return "Poupador";
		case 200:
			return "LadrÃo";

		default:
			return "deu em nada";
		}
	}

	public String descricaoOlfato(int codigo) {
		switch (codigo) {
		case 0:
			return "sem nenhuma Marca";
		case 1:
			return "Com marca de cheiro gerada a uma unidade de tempo atras";
		case 2:
			return "Com marca de cheiro gerada a duas(2) unidades de tempo passado";
		case 3:
			return "Com marca de cheiro gerada a trÃ‘Å s(3) unidades de tempo passado";
		case 4:
			return "Com marca de cheiro gerada a quatro(4) unidades de tempo passado";
		case 5:
			return "Com marca de cheiro gerada a cinco(5) unidades de tempo passado";
		default:
			return "deu em nada";
		}
	}

	// segue o mesmo modelo da matriz anterior da visao porem com um campo
	// de
	// dados menores
	public int[][] getMatrizOlfatoPoupador() {
		int matrizOlfatoPoupadorAux[][] = new int[3][3];
		int cont = 0;
		for (int k = 0; k < 3; k++) {
			for (int j = 0; j < 3; j++) {
				if (cont == 8) {
					matrizOlfatoPoupadorAux[k][j] = 123;
				} else {
					matrizOlfatoPoupadorAux[k][j] = sensor.getAmbienteOlfatoPoupador()[cont];
					// System.out.println(sensor.getAmbienteOlfatoPoupador()[cont]+":"+descricaoOlfato(sensor.getAmbienteOlfatoPoupador()[cont])+"
					// de poupador");
					cont++;
				}
			}
		}
		return matrizOlfatoPoupadorAux;
	}

	// segue o mesmo modelo da matriz anterior da visao porem com um campo
	// de
	// dados menores
	public int[][] getMatrizOlfatoLadrao() {
		int matrizOlfatoLadraoAux[][] = new int[3][3];
		int cont = 0;
		for (int k = 0; k < 3; k++) {
			for (int j = 0; j < 3; j++) {
				if (cont == 8) {
					matrizOlfatoLadraoAux[k][j] = 123;
				} else {
					matrizOlfatoLadraoAux[k][j] = sensor.getAmbienteOlfatoLadrao()[cont];
					// System.out.println(sensor.getAmbienteOlfatoLadrao()[cont]+":"+descricaoOlfato(sensor.getAmbienteOlfatoLadrao()[cont])+"
					// de LadrÃo");
					cont++;
				}
			}
		}
		return matrizOlfatoLadraoAux;
	}

	public int acao() {
		
		return Evaluation(getMatrizVisaoAtual(), getMatrizOlfatoPoupador(), getMatrizOlfatoLadrao());
	}

	public int[][] bankWayUp(){
		int banc = (int) Math.random();
		
		if (banc == sensor.getNumeroDeMoedas()){

		}
		
		
		return null;
	}
	
	public Integer calcularOlfLad(int codigo) { // Fornecer pesos baseados no
												// olfato por ladrÃ‘â€¢es do agente
												// na dada
												// jogada
		switch (codigo) {
		case 0:
			return 0;
		case 1:
			return -10;
		case 2:
			return -20;
		case 3:
			return -30;
		case 4:
			return -40;
		case 5:
			return -50;
		default:
			return 0;
		}
	}


	public Integer calcularVisao(int codigo) {
		// Fornecer pesos baseados na
		// visao do agente na dada
		// jogada
		switch (codigo) {
		case -2: // sem visÃ‘Æ’o
			return -10;
		case -1: // fora do ambiente
			return -30;
		case 0: // casa vazia
			return 100;
		case 1: // parede
			return -30;
		case 3: // banco
			 return (100 * sensor.getNumeroDeMoedas() );

		case 4: // moeda
			return 300;
		case 5: // pastilha do poder
			if (sensor.getNumeroDeMoedas() >= 5) {
				return sensor.getNumeroDeMoedas() * 3;
			} else {
				return -10;
			}
		case 100: // poupador
			return -60;

		default: // ladrao
			if (200 <= codigo || codigo >= 300) {
				return -10 * (sensor.getNumeroDeMoedas() + 1);
			} else {
				return -100;
			}
		}
	}

	// no metodo Evaluation os vetores de visao olfatoPoupador e
	// OlfatoLadrao
	// sÃ‘Æ’o passados como parametros para poder ser criado a percepÃ‘â€¡Ã‘Æ’o do
	// agente.

	public int Evaluation(int[][] matrizVisao, int[][] matrizOlfatoPoupador, int[][] matrizOlfatoLadrao) { // mÃ©todo de tomada de decisÃ£o
		ambience[sensor.getPosicao().x][sensor.getPosicao().y] += 1;
		int[][] visibleAmb = visibleAmbGen();

	
		int[] pos = new int[4]; // vetor para contabilizar as possibilidades aferidas durante a execução deste metodo
		int[] vetVisao = sensor.getVisaoIdentificacao(); // armazenar vetor de visão do poupador na jogada atual
		int[] vetOlf = sensor.getAmbienteOlfatoLadrao(); // armazenar vetor de olfato do poupador na jogada atual
		localBanco = controle.Constantes.posicaoBanco; // Armazenar a localização do banco
		
		int max = 0;
		
		// server para guardar os pontos ja visitados		 

		// contabilizar possibilidades com a soma dos pesos
		// (calibrados no metodo calcularUtilidade) em cada direção do agente

		// contabilizar possibilidade para o lado de cima (inclui todas as
		// posiçoes superiores ao agente)

		pos[0] += (int) (calcularVisao(vetVisao[0]) * 0.10 + calcularVisao(vetVisao[1]) * 0.25
				+ calcularVisao(vetVisao[2]) * 0.50 + calcularVisao(vetVisao[3]) * 0.25
				+ calcularVisao(vetVisao[4]) * 0.10 + calcularVisao(vetVisao[5]) * 0.25
				+ calcularVisao(vetVisao[6]) * 0.50 + calcularVisao(vetVisao[7]) * 0.75
				+ calcularVisao(vetVisao[8]) * 0.50 + calcularVisao(vetVisao[9]) * 0.25); // cima

		// contabilizar possibilidade para o lado direito (inclui todas as
		// posições a direita do agente)
		pos[2] += (int) (calcularVisao(vetVisao[4]) * 0.10 + calcularVisao(vetVisao[9]) * 0.25
				+ calcularVisao(vetVisao[13]) * 0.50 + calcularVisao(vetVisao[18]) * 0.25
				+ calcularVisao(vetVisao[23]) * 0.10 + calcularVisao(vetVisao[3]) * 0.25
				+ calcularVisao(vetVisao[8]) * 0.50 + calcularVisao(vetVisao[12]) * 0.75
				+ calcularVisao(vetVisao[17]) * 0.50 + calcularVisao(vetVisao[22]) * 0.25); // direita

		// contabilizar possibilidade para o lado de baixo (inclui todas as
		// posições inferiores ao agente)
		pos[1] += (int) (calcularVisao(vetVisao[19]) * 0.10 + calcularVisao(vetVisao[20]) * 0.25
				+ calcularVisao(vetVisao[21]) * 0.50 + calcularVisao(vetVisao[22]) * 0.25
				+ calcularVisao(vetVisao[23]) * 0.10 + calcularVisao(vetVisao[18]) * 0.25
				+ calcularVisao(vetVisao[17]) * 0.50 + calcularVisao(vetVisao[16]) * 0.75
				+ calcularVisao(vetVisao[15]) * 0.50 + calcularVisao(vetVisao[14]) * 0.25); // baixo

		// contabilizar possibilidade para o lado esquerdo (inclui todas as
		// posições a esquerda do agente)
		pos[3] += (int) (calcularVisao(vetVisao[0]) * 0.10 + calcularVisao(vetVisao[5]) * 0.25
				+ calcularVisao(vetVisao[10]) * 0.50 + calcularVisao(vetVisao[14]) * 0.25
				+ calcularVisao(vetVisao[19]) * 0.10 + calcularVisao(vetVisao[1]) * 0.25
				+ calcularVisao(vetVisao[6]) * 0.50 + calcularVisao(vetVisao[11]) * 0.75
				+ calcularVisao(vetVisao[15]) * 0.50 + calcularVisao(vetVisao[20]) * 0.25);// esquerda

		// contabilizar olfato sob possibilidades aferidas pela visão
		pos[0] += (int) (calcularOlfLad(vetOlf[0]) + calcularOlfLad(vetOlf[1]) + calcularOlfLad(vetOlf[2])); // cima
		pos[2] += (int) (calcularOlfLad(vetOlf[2]) + calcularOlfLad(vetOlf[4]) + calcularOlfLad(vetOlf[7])); // direita
		pos[1] += (int) (calcularOlfLad(vetOlf[3]) + calcularOlfLad(vetOlf[6]) + calcularOlfLad(vetOlf[7])); // baixo
		pos[3] += (int) (calcularOlfLad(vetOlf[0]) + calcularOlfLad(vetOlf[3]) + calcularOlfLad(vetOlf[5])); // esquerda

		
		pos[0] += (int) (Math.random() *4);
		pos[1] += (int) (Math.random() *4);
		pos[2] += (int) (Math.random() *4);
		pos[3] += (int) (Math.random() *4);
		

		//fazar o agente ir ao banco
		if(localBanco.x > sensor.getPosicao().x && localBanco.y > sensor.getPosicao().y){
			
		}

		for (int n = 0; n < 4; n++) { // for (loop) para comparar os valores do menor peso
			// compando o valor atual do comparador com o valor de pos[n]
			if (max < pos[n]) {
				max = pos[n];
				posi = n;
			}
		}


		// trecho para verificar o vetor de possibilidades o qual Ã‘â€° populado
		// por
		// valores
		// dos indices do vetor pos os quais tiveram valores iguais de
		// comparação e
		// escolher um valor atraves de uma roleta de porcentagem dividio em
		// cima,baixo,esquerda,direita tendo cada valor do vetor (valor para o
		// agente
		// andar) 25% de chance de ser selecionada.

		// serve pra comparar o valores do vetor, para verificar se o vetor esta



		System.out.print(pos[0] + ";");
		System.out.print(pos[1] + ";");
		System.out.print(pos[2] + ";");
		System.out.print(pos[3] + ";");
		System.out.println(posi);
		System.out.println(ambience[sensor.getPosicao().x][sensor.getPosicao().y]);
		System.out.println();

		return direcionar(posi);
	}

	public int[][] visibleAmbGen() { // computabilizar area visivel do agente em
										// uma matriz para facilitar a
										// ponderação no metodo evaluation

		int visibleAmb[][] = new int[5][5]; //matriz de tamanho menor responsavel por fornecer area visivel dos pesos dos espaços visitados

		int x = -2;
		int y = -2;

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				try {
					visibleAmb[j][i] = ambience[sensor.getPosicao().x + x][sensor.getPosicao().y + y];
				} catch (IndexOutOfBoundsException e) {
					 visibleAmb[j][i] = calcularVisao(-1);
				}
				y++;
			}
			x++;
			y = -2;
		}
		
		return visibleAmb;

	}

	public int direcionar(int posi) {
		switch (posi) {
		case 0:
			return 1;// cima
		case 1:
			return 2;// baixo;
		case 2:
			return 3;// direita;
		case 3:
			return 4;// esquerda
		default:
			return 0;
		}
	}

	public int cima() {
		return (int) (0.3 * 5);
	}

	public int baixo() {
		return (int) (0.5 * 5);
	}

	public int direita() {
		return (int) (0.7 * 5);
	}

	public int esquerda() {
		return (int) (0.9 * 5);
	}
}