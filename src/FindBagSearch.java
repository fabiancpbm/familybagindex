import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Classe de execuçao para busca de bolsa usando o arquivo de indice criado.
 */
public class FindBagSearch {

    /**
     * Realiza a busca de de um cadastro de bolsa familia de acordo com o NIS pssado e exibe o resultado na saida.
     *
     * @param args Use <Arquivo de indice> <Arquivo de bolsa> <nis>
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("[ERRO] - Erro de argumento: Use <Arquivo de indice> <Arquivo de bolsa> <nis>");
            System.exit(1);
        }
        String indexFilePath = args[0];
        String bagFilePath = args[1];
        String nis = args[2];
        if (nis.length() != 14) {
            System.err.println("[ERRO] - O Valor de NIS deve conter 14 digitos.");
            System.exit(2);
        }

        RandomAccessFile indexFile = null;
        RandomAccessFile bagFile = null;
        try {
            indexFile = new RandomAccessFile(indexFilePath, "r");
            bagFile = new RandomAccessFile(bagFilePath, "r");
        } catch (FileNotFoundException e) {
            System.err.println("[ERRO] - Erro ao tentar encontrar o arquivo: " + e.getMessage());
            System.exit(3);
        }

        String line = null;
        long bagPosition = -1;
        try {
            bagPosition = getBagDataPosition(indexFile, nis);
            bagFile.seek(bagPosition);
            line = bagFile.readLine();
        } catch (IOException e) {
            System.err.println("[ERRO] - Erro ao tentar ler o arquivo: " + e.getMessage());
            System.exit(2);
        }

        if (line != null) {
            String[] coluns = line.split("\t");
            FamilyBag familyBag = new FamilyBag(coluns[0], coluns[1], coluns[2], coluns[3], coluns[4],
                    coluns[5], coluns[6], coluns[7], coluns[8], coluns[9], coluns[10], coluns[11]);
            System.out.println(familyBag);
        }
    }

    /**
     * Captura o valor de posiçao da informaçao no arquivo de bolsa usando o arquivo de indice e o NIS.
     *
     * @param indexFile Arquivo de indice.
     * @param nis       NIS.
     * @return valor de posiçao desejada.
     * @throws IOException
     */
    private static long getBagDataPosition(RandomAccessFile indexFile, String nis) throws IOException {
        long initialPosition = indexFile.getFilePointer();
        long finalPosition = indexFile.length();
        return binarySearch(initialPosition, finalPosition, indexFile, nis);
    }

    /**
     * Realiza a busca binaria no arquico de indice recursivamente ate encontrar o NIS desejado.
     *
     * @param initialPosition Posiçao inicial.
     * @param finalPosition Posiçao final.
     * @param indexFile Arquivo de indice.
     * @param nis NIS
     * @return valor de posiçao desejada.
     * @throws IOException
     */
    private static long binarySearch(long initialPosition, long finalPosition, RandomAccessFile indexFile, String nis) throws IOException {
        long middlePosition = (initialPosition + finalPosition) / 2;
        indexFile.seek(middlePosition);

        indexFile.readLine();
        String line = indexFile.readLine();
        String[] column = line.split("\t");
        String foundNis = column[0];

        // NIS solicitado menor que o NIS encontrado na arvore.
        if (nis.compareTo(foundNis) < 0) {
            return binarySearch(initialPosition, middlePosition, indexFile, nis);
        } else if (nis.compareTo(foundNis) > 0) {
            return binarySearch(middlePosition + 1, finalPosition, indexFile, nis);
        }

        return Long.parseLong(column[1]);
    }

}
