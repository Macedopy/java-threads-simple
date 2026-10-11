import java.util.Arrays;
import java.util.logging.Logger;

public class MergeSortParalelo {
    private static final Logger LOG = Logger.getLogger(MergeSortParalelo.class.getName());

    private static final int NUMERO_DE_PROCESSADORES = Runtime.getRuntime().availableProcessors();
    private static final int NUMERO_DE_THREADS_ORDENADORAS = NUMERO_DE_PROCESSADORES - 1;

    private static byte[] mergeSortRecursivo(byte[] vetor) {
        if (vetor == null || vetor.length <= 1) {
            return vetor == null ? new byte[0] : Arrays.copyOf(vetor, vetor.length);
        }

        int meio = vetor.length / 2;
        byte[] esquerda = mergeSortRecursivo(Arrays.copyOfRange(vetor, 0, meio));
        byte[] direita = mergeSortRecursivo(Arrays.copyOfRange(vetor, meio, vetor.length));
        return MergeSort.mergeSort(esquerda, direita);
    }

    static class ThreadOrdenadora extends Thread {
        private final byte[] parte;
        private byte[] resultado;
        private RuntimeException falha;

        ThreadOrdenadora(int indice, byte[] parte) {
            super("Ordenadora-" + (indice + 1));
            this.parte = parte;
        }

        @Override
        public void run() {
            try {
                LOG.info(getName() + " iniciada com " + parte.length + " elementos.");
                resultado = mergeSortRecursivo(parte);
                LOG.info(getName() + " finalizou a ordenação.");
            } catch (RuntimeException erro) {
                falha = erro;
            }
        }
    }

    static class ThreadJuntadora extends Thread {
        private final byte[] esquerda;
        private final byte[] direita;
        private byte[] resultado;
        private RuntimeException falha;

        ThreadJuntadora(int rodada, int indice, byte[] esquerda, byte[] direita) {
            super("Juntadora-R" + rodada + "-" + indice);
            this.esquerda = esquerda;
            this.direita = direita;
        }

        @Override
        public void run() {
            try {
                LOG.info(getName() + " juntando " + esquerda.length + " + " + direita.length
                        + " elementos.");
                resultado = MergeSort.mergeSort(esquerda, direita);
            } catch (RuntimeException erro) {
                falha = erro;
            }
        }
    }

    private static void aguardar(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException erro) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("A execução foi interrompida.", erro);
        }
    }

    static byte[] ordenacaoParalela(byte[] entrada) {
        byte[] vetor = entrada == null ? new byte[0] : Arrays.copyOf(entrada, entrada.length);
        LOG.info("Iniciando ordenação paralela de " + vetor.length + " elementos com "
                + NUMERO_DE_PROCESSADORES + " processadores disponíveis.");

        if (NUMERO_DE_THREADS_ORDENADORAS == 0) {
            LOG.warning("Há somente um processador; a ordenação será feita na thread principal.");
            return mergeSortRecursivo(vetor);
        }

        ThreadOrdenadora[] ordenadoras = new ThreadOrdenadora[NUMERO_DE_THREADS_ORDENADORAS];
        for (int indice = 0; indice < NUMERO_DE_THREADS_ORDENADORAS; indice++) {
            int inicio = indice * vetor.length / NUMERO_DE_THREADS_ORDENADORAS;
            int fim = (indice + 1) * vetor.length / NUMERO_DE_THREADS_ORDENADORAS;
            byte[] parte = Arrays.copyOfRange(vetor, inicio, fim);
            ordenadoras[indice] = new ThreadOrdenadora(indice, parte);
            ordenadoras[indice].start();
        }

        byte[][] resultados = new byte[ordenadoras.length][];
        RuntimeException falhaOrdenadora = null;
        for (int indice = 0; indice < ordenadoras.length; indice++) {
            aguardar(ordenadoras[indice]);
            if (falhaOrdenadora == null) {
                falhaOrdenadora = ordenadoras[indice].falha;
            }
            resultados[indice] = ordenadoras[indice].resultado;
        }
        if (falhaOrdenadora != null) {
            throw new IllegalStateException("Falha em uma thread ordenadora.", falhaOrdenadora);
        }

        int rodada = 1;
        while (resultados.length > 1) {
            ThreadJuntadora[] juntadoras = new ThreadJuntadora[resultados.length / 2];
            byte[][] proximosResultados = new byte[(resultados.length + 1) / 2][];
            LOG.info("Rodada de merge " + rodada + ": " + juntadoras.length
                    + " thread(s) juntadora(s).");

            for (int indice = 0; indice < juntadoras.length; indice++) {
                juntadoras[indice] = new ThreadJuntadora(
                        rodada, indice + 1, resultados[indice * 2], resultados[indice * 2 + 1]);
                juntadoras[indice].start();
            }

            if (resultados.length % 2 != 0) {
                proximosResultados[proximosResultados.length - 1] = resultados[resultados.length - 1];
            }

            RuntimeException falhaJuntadora = null;
            for (int indice = 0; indice < juntadoras.length; indice++) {
                aguardar(juntadoras[indice]);
                if (falhaJuntadora == null) {
                    falhaJuntadora = juntadoras[indice].falha;
                }
                proximosResultados[indice] = juntadoras[indice].resultado;
            }
            if (falhaJuntadora != null) {
                throw new IllegalStateException("Falha em uma thread juntadora da rodada " + rodada + ".",
                        falhaJuntadora);
            }

            resultados = proximosResultados;
            rodada++;
        }

        LOG.info("Ordenação paralela finalizada.");
        return resultados[0];
    }

}