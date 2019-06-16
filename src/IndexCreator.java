import java.io.*;
import java.util.*;

/**
 * Classe geradora de arquivo de indice.
 * Algumas definições:
 * <p>
 * - Nivel de arquivo: este numero estara no nome do arquivo, representando o grau do merge ja realizado nele.
 * Ou seja, se um arquivo ainda nao foi mesclado a outro, seu nivel sera 0. Ao realizar o merge entre dois arquivos,
 * um novo sera gerado com 1 nivel acima dos arquivos lidos.
 * <p>
 * - forceMerge: A regra aplicada ao metodo de merge criado nesta classe define que 2 arquivos de mesmo nivel serao
 * juntados em um terceiro com 1 nivel acima. Porem, a ultima operaçao a ser realizada de merge nao garante
 * essa regra pois os arquivos criados podem ainda nao estar na configuraçao necessaria. Sendo assim, o forceMerge
 * e responsavel por permitir que o merge seja realizado sem considerar essa regra.
 */
public class IndexCreator {

    /**
     * Quantidade de linhas a serem lidas por bloco de ordenaçao.
     */
    private static final long LINE_PER_FILE = 1000;

    /**
     * Programa principal responsavel por gerar o arquivo de indice ordenado.
     *
     * @param args Use <Caminho_do_Arquivo_de_Bolsa>.
     */
    public static void main(String[] args) {
        // Validando argumentos.
        if (args.length != 1) {
            System.err.println("[ERRO] - Argumentos inconrretos: Use <Caminho_do_Arquivo_de_Bolsa>");
            System.exit(1);
        }

        // Inicializando as variaveis.
        String filePath = args[0];
        String line;
        String[] columns;
        String nis;
        long position;

        // Abrindo arquivo de bolsa para leitura.
        RandomAccessFile bagFile = openReadingFile(filePath);
        if (bagFile != null) {
            List<IndexItem> indexItemList = new ArrayList<>();
            try {
                System.out.println("[INFO] - Lendo arquivo de bolsa.");
                bagFile.readLine();
                long t1 = System.currentTimeMillis();
                while (bagFile.getFilePointer() < bagFile.length()) {
                    position = bagFile.getFilePointer();
                    line = bagFile.readLine();
                    columns = line.split("\t");
                    nis = columns[7];

                    IndexItem indexItem = new IndexItem(nis, position);
                    indexItemList.add(indexItem);

                    // Realizando o merge entre os arquivos temporarios ja criados, caso seja possivel.
                    boolean lastLine = bagFile.getFilePointer() == bagFile.length();
                    if (indexItemList.size() % LINE_PER_FILE == 0 || lastLine) {
                        createFileAndMerge(indexItemList, lastLine);
                        indexItemList.clear();
                    }
                }
                bagFile.close();
                long readingDuration = System.currentTimeMillis() - t1;
                System.out.println("[INFO] - Leitura concluida em " + readingDuration + " milissegundos.");

                System.out.println("[INFO] - Movendo arquivo de indice para a pasta padrao e deletando a pasta temporaria.");
                File path = new File("src/temp");
                File indexFile = path.listFiles()[0];
                indexFile.renameTo(new File("src/index.csv"));
                path.deleteOnExit();

                System.out.println("[INFO] - Arquivo de indice criado com sucesso.");
            } catch (IOException e) {
                System.err.println("[ERRO] - Erro ao tentar ler o arquivo: " + e.getMessage());
            }
        }
    }

    /**
     * Cria um arquivo de temporario indice ordenado com {@link #LINE_PER_FILE} linhas e realiza o merge,
     * caso seja possivel.
     * <p>
     * Para fazer Merge entre os arquivos, e preciso que haja na pasta temporaria pelo menos um arquivo.
     * O Merge ira mesclar arquivos que possuem o mesmo nivel, ou seja, a mesma quantidade de linhas. Isso so nao
     * acontecera quando a ultima linha do arquivo de bolsas for lido. Neste caso, sera feito um merge forçado, que
     * se trata de um merge feito unindo todos os arquivos temporarios, independente dos seus niveis.
     *
     * @param indexItemList Lista de itens para o arquivo de indice.
     * @param forceMerge    <true> se for desejado forçar o merge.
     * @throws IOException
     */
    private static void createFileAndMerge(List<IndexItem> indexItemList, boolean forceMerge) throws IOException {
        File path = new File("src/temp");
        if (!path.exists()) {
            path.mkdir();
        }
        File[] fileList = path.listFiles();
        long level = 0;
        long fileId = getIdFileGivenLevel(fileList, level);

        // Cria arquivo
        FileOutputStream newFile = createWritingFile(
                "src/temp/ExternalMergeSort_" + level + "_" + fileId + "_.csv");
        Collections.sort(indexItemList);
        for (IndexItem i : indexItemList) {
            newFile.write(i.toString().getBytes());
        }
        newFile.close();

        // Posso fazer createFileAndMerge se a quantidade de arquivos for maior que 1.
        fileList = path.listFiles();
        if (fileList.length > 1) {
            performMerge(fileList, forceMerge);
        }
    }

    /**
     * Realiza o merge, caso seja possivel. Os seguintes passos sao realizados:
     * - Verificar se tem merge para fazer. O merge e feito de 2 em 2 arquivos, preferencialmente que estejam no
     * mesmo nivel, ou seja, possuamm o mesmo numero de linhas.
     * - Caso tenha, faça.
     * - Ao final do merge, verifique se a nova configuraçao da pasta temporaria permite realizar um novo merge.
     * - Caso tenha, refaça os processos acima novamente. Esta funçao e recursiva. Seu objetivo e entregar um merge
     * tao pequeno quanto possivel.
     *
     * @param fileList   Lista de arquivos da pasta temporaria.
     * @param forceMerge <true> se for necessario realizar o merge desconsiderando os niveis entre os arquivos.
     * @throws IOException
     */
    private static void performMerge(File[] fileList, boolean forceMerge) throws IOException {
        // Verificando se tem chance de ocorrer merge. O Merge pode ocorrer caso haja arquivos de mesmo nivel ou caso o
        // forceMerge for verdadeiro.
        boolean hasMergeToDo = forceMerge;
        if (!hasMergeToDo) {
            for (File file1 : fileList) {
                for (File file2 : fileList) {
                    if (file1.exists() && file2.exists() && !file1.getName().equals(file2.getName()) &&
                            (isAtSameLevel(file1, file2))) {
                        hasMergeToDo = true;
                        break;
                    }
                }
                if (hasMergeToDo) {
                    break;
                }
            }
        }

        // Se tem merge pra fazer, faça.
        if (hasMergeToDo) {
            List<File> filesToDelete = new ArrayList<>();
            for (File firstFile : fileList) {
                if (firstFile.exists() && !filesToDelete.contains(firstFile)) {
                    for (File secondFile : fileList) {
                        if (secondFile.exists() && !filesToDelete.contains(secondFile) &&
                                !firstFile.getName().equals(secondFile.getName()) &&
                                (isAtSameLevel(firstFile, secondFile) || forceMerge)) {
                            List<IndexItem> mergeIndexItemList = new ArrayList<>();

                            RandomAccessFile firstAccessFile = openReadingFile(firstFile);
                            mergeIndexItemList.addAll(convertFileToIndexList(firstAccessFile));
                            firstAccessFile.close();
                            filesToDelete.add(firstFile);

                            RandomAccessFile secondAccessFile = openReadingFile(secondFile);
                            mergeIndexItemList.addAll(convertFileToIndexList(secondAccessFile));
                            secondAccessFile.close();
                            filesToDelete.add(secondFile);

                            Collections.sort(mergeIndexItemList);

                            long level;
                            if (forceMerge) {
                                long levelOfFirstFile = getLevel(firstFile) + 1;
                                long levelOfSecondFile = getLevel(secondFile) + 1;
                                level = levelOfFirstFile > levelOfSecondFile ? levelOfFirstFile : levelOfSecondFile;
                            } else {
                                level = getLevel(firstFile) + 1;
                            }
                            long fileId = getIdFileGivenLevel(fileList, level);
                            String filePath = "src/temp/ExternalMergeSort_"
                                    + level + "_" + fileId + "_.csv";
                            FileOutputStream outFile = createWritingFile(filePath);
                            for (IndexItem indexItem : mergeIndexItemList) {
                                outFile.write(indexItem.toString().getBytes());
                            }
                            outFile.close();

                            break;
                        }
                    }
                }
            }

            // Apagando os arquivos ja usados.
            for (File file : filesToDelete) {
                if (file.exists()) {
                    file.delete();
                }
            }

            // Ja que teve merge pra fazer, vou tentar fazer novamente.
            File path = new File("src/temp");
            if (path.listFiles().length > 1) {
                performMerge(path.listFiles(), forceMerge);
            }
        }

        // Se o codigo chegou aqui, e porque nao tem mais merge pra fazer.
    }

    /**
     * Verifica se os arquivos estao no mesmo nivel.
     *
     * @param file1 Arquivo 1.
     * @param file2 Arquivo 2.
     * @return <true>, se os arquivos estiverem no mesmo nivel.
     */
    private static boolean isAtSameLevel(File file1, File file2) {
        String name1 = file1.getName();
        String[] name1Colums = name1.split("_");
        String level1 = name1Colums[1];

        String name2 = file2.getName();
        String[] name2Colums = name2.split("_");
        String level2 = name2Colums[1];

        return level1.equals(level2);
    }

    /**
     * Captura o nivel de um dado arquivo.
     *
     * @param file Arquivo de onde deve ser abstraido o nivel.
     * @return Valor do nivel do arquivo dado.
     */
    private static long getLevel(File file) {
        String name = file.getName();
        String[] name1Colums = name.split("_");
        return Long.parseLong(name1Colums[1]);
    }

    /**
     * Gera um ID para um novo arquivo, a fim de evitar repetiçao na criaçao de arquivos.
     *
     * @param files Lista de arquivos existentes.
     * @param level Nivel do arquivo. Isso e capturado porque a possibilidade de repetiçao entre arquivos so ocorre
     *              quando eles estao no mesmo nivel.
     * @return ID do arquivo a ser criado.
     */
    private static long getIdFileGivenLevel(File[] files, long level) {
        long currentId = -1;
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                String[] name1Colums = name.split("_");
                long foundLevel = Long.parseLong(name1Colums[1]);
                long foundId = Long.parseLong(name1Colums[2]);
                if (foundLevel == level && foundId >= currentId) {
                    currentId = foundId;
                }
            }
        }
        return currentId + 1;
    }

    /**
     * Abre arquivo para leitura dado seu caminho.
     *
     * @param filePath Caminho do arquivo.
     * @return Instância de leitura de arquivo.
     */
    private static RandomAccessFile openReadingFile(String filePath) {
        RandomAccessFile inFile = null;
        try {
            inFile = new RandomAccessFile(filePath, "r");
        } catch (FileNotFoundException e) {
            System.err.println("[INFO] - Erro ao tentar encontrar o arquivo: " + e.getMessage());
            System.exit(2);
        }
        return inFile;
    }

    /**
     * Abre arquivo para leitura dado seu caminho.
     *
     * @param file Arquivo.
     * @return Instância de leitura de arquivo.
     * @throws IOException
     */
    private static RandomAccessFile openReadingFile(File file) {
        RandomAccessFile inFile = null;
        try {
            if (file.exists()) {
                inFile = new RandomAccessFile(file, "r");
            }
        } catch (FileNotFoundException e) {
            System.err.println("[INFO] - Erro ao tentar encontrar o arquivo: " + e.getMessage());
            System.exit(2);
        }
        return inFile;
    }

    /**
     * Cria arquivo para escrita.
     *
     * @param filePath Caminho do arquivos de escrita.
     * @return Instância de escrita de arquivo.
     * @throws IOException
     */
    private static FileOutputStream createWritingFile(String filePath) {
        FileOutputStream outFile = null;
        try {
            File file = new File(filePath);
            outFile = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            System.err.println("[ERRO] - Erro ao tentar criar o arquivo de indice: " + e.getMessage());
            System.exit(3);
        }
        return outFile;
    }

    /**
     * Converte um arquivo de indice para uma lista de indice.
     *
     * @param file Arquivo de indice.
     * @return Lista de indice.
     */
    private static List<IndexItem> convertFileToIndexList(RandomAccessFile file) throws IOException {
        List<IndexItem> indexItemList = new ArrayList<>();

        if (file.length() > 0) {
            String line = file.readLine();
            while (file.getFilePointer() < file.length()) {
                String[] column = line.split("\t");

                String nis = column[0];
                long position = Long.parseLong(column[1]);
                IndexItem indexItem = new IndexItem(nis, position);
                indexItemList.add(indexItem);
                line = file.readLine();
            }

            String[] column = line.split("\t");
            String nis = column[0];
            long position = Long.parseLong(column[1]);
            IndexItem indexItem = new IndexItem(nis, position);
            indexItemList.add(indexItem);
        }

        return indexItemList;
}
}
