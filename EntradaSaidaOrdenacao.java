import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

final class EntradaSaidaOrdenacao {
    private static final Scanner TECLADO = new Scanner(System.in);

    private EntradaSaidaOrdenacao() {
    }

    static byte[] lerVetor() {
        int tamanho = lerInteiro("Quantidade de elementos: ", 1, Integer.MAX_VALUE);
        byte[] vetor = new byte[tamanho];
        int opcao = lerInteiro("Preencher manualmente (1) ou aleatoriamente (2)? ", 1, 2);

        if (opcao == 1) {
            for (int indice = 0; indice < vetor.length; indice++) {
                vetor[indice] = (byte) lerInteiro(
                        "Elemento " + indice + " (-128 a 127): ", Byte.MIN_VALUE, Byte.MAX_VALUE);
            }
        } else {
            Random aleatorio = new Random();
            for (int indice = 0; indice < vetor.length; indice++) {
                vetor[indice] = (byte) (aleatorio.nextInt(256) + Byte.MIN_VALUE);
            }
        }

        return vetor;
    }

    static void imprimirResultado(byte[] vetor) {
        int opcao = lerInteiro("Imprimir vetor completo (1) ou um trecho (2)? ", 1, 2);

        if (opcao == 1) {
            System.out.println(Arrays.toString(vetor));
            return;
        }

        int inicio = lerInteiro("Índice inicial (0 a " + (vetor.length - 1) + "): ",
                0, vetor.length - 1);
        int fim = lerInteiro("Índice final inclusivo (" + inicio + " a "
                + (vetor.length - 1) + "): ", inicio, vetor.length - 1);
        System.out.println(Arrays.toString(Arrays.copyOfRange(vetor, inicio, fim + 1)));
    }

    private static int lerInteiro(String mensagem, int minimo, int maximo) {
        while (true) {
            System.out.print(mensagem);
            System.out.flush();
            if (!TECLADO.hasNextLine()) {
                throw new IllegalStateException("A entrada foi encerrada antes do fim do programa.");
            }
            String entrada = TECLADO.nextLine().trim();

            try {
                int valor = Integer.parseInt(entrada);
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
            } catch (NumberFormatException erro) {
                System.out.println("Digite um número inteiro válido.");
                continue;
            }

            System.out.println("Digite um valor entre " + minimo + " e " + maximo + ".");
        }
    }
}