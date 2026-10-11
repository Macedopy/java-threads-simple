import java.util.Arrays;
import java.util.logging.Logger;

public class MergeSortSequencial {
    private static final Logger LOG = Logger.getLogger(MergeSortSequencial.class.getName());

    static byte[] ordenar(byte[] vetor) {
        byte[] copia = vetor == null ? new byte[0] : Arrays.copyOf(vetor, vetor.length);
        LOG.info("Iniciando ordenação sequencial de " + copia.length + " elementos.");
        byte[] resultado = ordenarRecursivamente(copia);
        LOG.info("Ordenação sequencial finalizada.");
        return resultado;
    }

    private static byte[] ordenarRecursivamente(byte[] vetor) {
        if (vetor.length <= 1) {
            return vetor;
        }

        int meio = vetor.length / 2;
        byte[] esquerda = ordenarRecursivamente(Arrays.copyOfRange(vetor, 0, meio));
        byte[] direita = ordenarRecursivamente(Arrays.copyOfRange(vetor, meio, vetor.length));
        return MergeSort.mergeSort(esquerda, direita);
    }
}