import java.util.Objects;

/**
 * Modelo de item de um arquivo de indice.
 */
public class IndexItem implements Comparable<IndexItem> {

    /**
     * NIS.
     */
    private String nis;

    /**
     * Posiçao das informações no de bolsa.
     */
    private long position;

    /**
     * Construtor.
     *
     * @param nis      {@link #nis}
     * @param position {@link #position}
     */
    public IndexItem(String nis, long position) {
        this.nis = nis;
        this.position = position;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexItem indexItem = (IndexItem) o;
        return Objects.equals(nis, indexItem.nis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nis);
    }

    @Override
    public String toString() {
        return nis + "\t" + position + "\n";
    }

    @Override
    public int compareTo(IndexItem o) {
        return this.nis.compareTo(o.nis);
    }
}
