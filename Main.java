import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            byte[] vetor = EntradaSaidaOrdenacao.lerVetor();
            LOG.info("Vetor recebido com " + vetor.length + " elementos.");

            long inicioSequencial = System.nanoTime();
            byte[] resultadoSequencial = MergeSortSequencial.ordenar(vetor);
            long tempoSequencial = System.nanoTime() - inicioSequencial;

            long inicioParalelo = System.nanoTime();
            byte[] resultadoParalelo = MergeSortParalelo.ordenacaoParalela(vetor);
            long tempoParalelo = System.nanoTime() - inicioParalelo;

            if (!Arrays.equals(resultadoSequencial, resultadoParalelo)) {
                throw new IllegalStateException("As versões sequencial e paralela produziram resultados diferentes.");
            }

            LOG.info("As versões sequencial e paralela produziram o mesmo vetor ordenado.");
            System.out.printf("Tempo sequencial: %.3f ms (%d ns)%n",
                    tempoSequencial / 1_000_000.0, tempoSequencial);
            System.out.printf("Tempo paralelo:   %.3f ms (%d ns)%n",
                    tempoParalelo / 1_000_000.0, tempoParalelo);
            EntradaSaidaOrdenacao.imprimirResultado(resultadoParalelo);
        } catch (RuntimeException erro) {
            LOG.log(Level.SEVERE, "Não foi possível concluir a comparação.", erro);
            System.err.println("Erro: " + erro.getMessage());
        }
    }
}