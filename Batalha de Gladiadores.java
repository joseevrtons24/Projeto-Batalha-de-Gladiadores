public class Batalha {

    public static void main(String[] args) {
        
        System.out.println(" INÍCIO DA PREPARAÇÃO DE COMBATE ");

        // 1) Grupo COmpleto (Sorteio)
        Gladiador[] grupoGladiadores = new Gladiador[5];
        grupoGladiadores[0] = new Gladiador("Leonard");
        grupoGladiadores[1] = new Gladiador("Bradd");
        grupoGladiadores[2] = new Gladiador("Frank");
        grupoGladiadores[3] = new Gladiador("Robert");
        grupoGladiadores[4] = new Gladiador("Antony"); 

        // 2) Sorteio aleatório de gladiadores 
        
        int index1 = (int) (Math.random() * 5); // Sorteia o primeiro
        int index2; 

            while (true) {
            index2 = (int) (Math.random() * 5);
            if (index1 != index2) {
                break; 
            }
        }
        Gladiador g1 = grupoGladiadores[index1]; // Gladiador escolhido 1
        Gladiador g2 = grupoGladiadores[index2]; // Gladiador escolhido 2
        
        // 3) ATRIBUIÇÃO DE EQUIPAMENTO ( Ranking de Luta)
        
        // G1 sempre recebe o equipamento mais forte (Ouro/Tentáculo)
        g1.atribuirArmadura("Ouro");
        Arma arma1 = new Arma("Tentáculo de Espinhos");
        g1.armaAtual = arma1; 

        // G2 sempre recebe o equipamento mais fraco (Alumínio/Lança)
        g2.atribuirArmadura("Alumínio");
        Arma arma2 = new Arma("Lança");
        g2.armaAtual = arma2;
        
        // Identifica se o G2 é o Gladiador Mais Fraco (Antony)
        boolean g2EhMaisFraco = g2.nome.equals("Antony");

        Arena arena = new Arena(1000);

        System.out.println("\n INICIANDO COMBATE: " + g1.nome + " vs " + g2.nome + " ");
        g1.mostrarStatus();
        g2.mostrarStatus();
        arena.exibirStatus();

        // 4) Combate
        int turno = 1;
        while (g1.vida > 0 && g2.vida > 0) {
            
            System.out.println("\n RODADA " + turno + " ");
            
            // a) G1 ataca
            System.out.println(" " + g1.nome + " ataca " + g2.nome + " com " + g1.armaAtual.nome);
            g2.receberAtaque(g1.armaAtual, false); // Nao derruba
            
            // Checar grito/cura após o dano
            checarEventoGrito(g1, g2);

            // b) G2 ataca
        if (g2.vida > 0) {
                
                // Extra: gladiador mais fraco pode aplicar sequência de golpes
                if (g2EhMaisFraco == true) {
                    System.out.println("🔥 " + g2.nome + " (Mais Fraco) aplica SEQUÊNCIA DE GOLPES!");
                    // O golpe mais fraco derruba, aplicando dano fixo de 3 golpes
                    g1.receberAtaque(g2.armaAtual, true); 
                } else {
                    System.out.println(" " + g2.nome + " revida o ataque em " + g1.nome + " com " + g2.armaAtual.nome);
                    g1.receberAtaque(g2.armaAtual, false); 
                }
            }
            
            // Checar grito/cura após o revide
            checarEventoGrito(g2, g1); 

            // Atualização da torcida
            boolean g1Vivo = (g1.vida > 0);
            boolean g2Vivo = (g2.vida > 0);
            arena.atualizar(g1Vivo, g2Vivo);

            // Mostrar o status
            g1.mostrarStatus();
            g2.mostrarStatus();

            turno = turno + 1;

            if (turno > 30) {
                System.out.println("O juiz encerrou a luta por tempo.");
                break;
            }
        }

        // Fim do Jogo
        System.out.println("\n=== FIM DA LUTA ===");
        if (g1.vida > 0) { System.out.println("VENCEDOR: " + g1.nome); } 
        else if (g2.vida > 0) { System.out.println("VENCEDOR: " + g2.nome); } 
        else { System.out.println("EMPATE."); }
    }
    
    // Modo auxiliar de cura
    // Atacante: Quem causou o dano | Defensor: Quem recebeu o dano e pode gritar
    public static void checarEventoGrito(Gladiador atacante, Gladiador defensor) {
        // regra: Faltando 2 vidas para morrer (vidas <= 2)
        if (defensor.vida <= 2 && defensor.vida > 0) {
            System.out.println(" " + defensor.nome + " grita! Torcida pode jogar itens!");
            
            double sorteio = Math.random(); 

            if (sorteio < 0.3) { 
                System.out.println("TORCIDA JOGA POÇÃO MÁGICA (+3 Vidas e Arma Forte)");
                
                // Aleatoriedade de quem pega (60% para o que gritou / 40% para o outro)
                if (Math.random() < 0.6) {
                    defensor.pegarPocao(); 
                } else {
                    atacante.pegarPocao(); 
                }
            } else if (sorteio < 0.6) { 
                System.out.println("TORCIDA JOGA ERVA (+2 Vidas)");
                 
                 if (Math.random() < 0.6) {
                    defensor.coletarErva(2); // Erva (Gritou)
                } else {
                    atacante.coletarErva(2); // Erva (Atacante)
                }
            }
        }
    }
}

// Classe Arma
class Arma {
    String nome;
    int dano; // Força de destruição (1 a 5)

    Arma(String nomeDaArma) {
        this.nome = nomeDaArma;
        
        if (this.nome.equals("Tentáculo de Espinhos")) this.dano = 5;
        else if (this.nome.equals("Clava de Espinhos")) this.dano = 4;
        else if (this.nome.equals("Espada")) this.dano = 3;
        else if (this.nome.equals("Arco e flecha")) this.dano = 2;
        else if (this.nome.equals("Lança")) this.dano = 1;
        else this.dano = 1;
    }
}

// Classe Gladiador
class Gladiador {
    String nome;
    int vida;
    boolean temArmadura;
    String nomeArmadura;
    Arma armaAtual; 

    // Construtor Simples
    Gladiador(String nomeGladiador) {
        this.nome = nomeGladiador;
        this.vida = 5; 
        this.temArmadura = false;
        this.nomeArmadura = "Sem Armadura";
        this.armaAtual = null; 
    }
    
    // Atribuição de Armadura
    void atribuirArmadura(String tipoArmadura) {
        this.temArmadura = true;
        this.nomeArmadura = tipoArmadura;
    }

    // Método para retirar vidas (Recebe a Arma e o status de derrubado)
    void receberAtaque(Arma armaInimiga, boolean derrubado) {
        if (this.vida <= 0) { return; }

        int danoQueVaiLevar; 
        int defesaArmadura = 0;

        // Regra da sequência de golpes (quando derrubado)
        if (derrubado == true) {
            danoQueVaiLevar = 3; // 3 golpes, 3 vidas retiradas
            defesaArmadura = 0;
            System.out.println("   (Gladiador derrubado! Recebe 3 golpes)");
        } else {
            // Defesa normal
            if (this.temArmadura == true) {
                 defesaArmadura = 2;
            }
            
            danoQueVaiLevar = armaInimiga.dano - defesaArmadura;
        }

        if (danoQueVaiLevar < 0) {
            danoQueVaiLevar = 0;
        }

        this.vida = this.vida - danoQueVaiLevar;

        if (this.vida < 0) {
            this.vida = 0;
        }

        System.out.println(" (Dano sofrido: " + danoQueVaiLevar + " / Vidas restantes: " + this.vida + ")");
    }

    // Poção Mágica (+3 vidas e Arma Forte)
    void pegarPocao() {
        int cura = 3;
        this.vida = this.vida + cura;
        
        if (this.vida > 5) { this.vida = 5; }
        
        // Arma fica mais forte (+1 dano)
        if (this.armaAtual != null) {
            this.armaAtual.dano = this.armaAtual.dano + 1; 
            System.out.println(" POÇÃO: " + this.nome + " curou 3 vidas e arma mais forte (Dano: " + this.armaAtual.dano + ")");
        } else {
             System.out.println(" POÇÃO: " + this.nome + " curou 3 vidas.");
        }
    }
    
    // Erva (Recupera 2 vidas)
    void coletarErva(int curaBase) {
        int cura = curaBase;
        this.vida = this.vida + cura;

        if (this.vida > 5) { this.vida = 5; }
        
        System.out.println(" ERVA: " + this.nome + " curou 2 vidas. Total: " + this.vida);
    }
    
    // Mostrar status
    void mostrarStatus() {
        String statusVida;
        if (this.vida > 0) { statusVida = "VIVO"; } 
        else { statusVida = "MORTO"; }
        
        String danoArmaAtual = "N/D";
        if (this.armaAtual != null) {
            danoArmaAtual = String.valueOf(this.armaAtual.dano);
        }
        
        System.out.println(" " + this.nome + " [Arma Dano: " + danoArmaAtual + "] - Vidas: " + this.vida + " (" + this.nomeArmadura + ") | Status: " + statusVida);
    }
}

class Arena {
    int numeroTorcedores;
    boolean felizes;

    Arena(int qtdInicial) {
        this.numeroTorcedores = qtdInicial;
        this.felizes = true;
    }

    void atualizar(boolean g1Vivo, boolean g2Vivo) {
        if (g1Vivo == true && g2Vivo == true) {
            this.felizes = false;
            int genteQueSaiu = this.numeroTorcedores / 4; 
            this.numeroTorcedores = this.numeroTorcedores - genteQueSaiu;
            System.out.println(" (Arena: Torcida triste " + genteQueSaiu + " saíram / Total: " + this.numeroTorcedores + ")");
        } else {
            this.felizes = true;
            int genteQueChegou = this.numeroTorcedores / 10; 
            this.numeroTorcedores = this.numeroTorcedores + genteQueChegou;
            System.out.println("   (Arena: Torcida feliz! Chegaram " + genteQueChegou + " pessoas / Total: " + this.numeroTorcedores + ")");
        }
    }
    
    void exibirStatus() {
        String humor;
        if (this.felizes == true) { humor = "Contentes"; } 
        else { humor = "Tristes"; }
        System.out.println(" [ARENA STATUS] Torcedores: " + this.numeroTorcedores + " | Humor: " + humor);
    }
}