import java.util.Arrays;

public class MergeSort {

    static byte[] mergeSort(byte[] A, byte[] B) {
        byte[] C = new byte[A.length + B.length];

        int indiceDoVetorA = 0;
        int indiceDoVetorB = 0;
        int indiceDoVetorC = 0;

        while (indiceDoVetorA < A.length && indiceDoVetorB < B.length) {
            if (A[indiceDoVetorA] <= B[indiceDoVetorB]) {
                C[indiceDoVetorC] = A[indiceDoVetorA];
                indiceDoVetorA++;
            } else {
                C[indiceDoVetorC] = B[indiceDoVetorB];
                indiceDoVetorB++;
            }
            indiceDoVetorC++;
        }

        while (indiceDoVetorA < A.length) {
            C[indiceDoVetorC] = A[indiceDoVetorA];
            indiceDoVetorA++;
            indiceDoVetorC++;
        }

        while (indiceDoVetorB < B.length) {
            C[indiceDoVetorC] = B[indiceDoVetorB];
            indiceDoVetorB++;
            indiceDoVetorC++;
        }

        return C;
    }

    public static void main(String[] args) {
        byte[] A = {1, 4, 7, 10};
        byte[] B = {2, 3, 6, 11, 12};

        byte[] C = mergeSort(A, B);

        System.out.println(Arrays.toString(C));
    }
}
