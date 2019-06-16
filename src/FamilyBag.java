/**
 * Bolsa família.
 */
public class FamilyBag {

    /**
     * Unidade federativa.
     */
    private String uf;

    /**
     * Código SIAFI do município.
     */
    private String siafiCityCode;

    /**
     * Nome do município.
     */
    private String cityName;

    /**
     * Código da função.
     */
    private String functionCode;

    /**
     * Código de subfunção.
     */
    private String subFunctionCode;

    /**
     * Código do programa.
     */
    private String programCode;

    /**
     * Código de ação.
     */
    private String actionCode;

    /**
     * NIS do favorecido.
     */
    private String favoredNis;

    /**
     * Nome do favorecido.
     */
    private String favoredName;

    /**
     * Fonte-finalidade. Por exemplo: CAIXA-Programa Bolsa família.
     */
    private String fontAndFinality;

    /**
     * Valor.
     */
    private String value;

    /**
     * Mês e ano.
     */
    private String monthAndYear;

    /**
     * Construtor.
     *
     * @param uf              {@link #uf}
     * @param siafiCityCode   {@link #siafiCityCode}
     * @param cityName        {@link #cityName}
     * @param functionCode    {@link #functionCode}
     * @param subFunctionCode {@link #subFunctionCode}
     * @param programCode     {@link #programCode}
     * @param actionCode      {@link #actionCode}
     * @param favoredNis      {@link #favoredNis}
     * @param favoredName     {@link #favoredName}
     * @param fontAndFinality {@link #fontAndFinality}
     * @param value           {@link #value}
     * @param monthAndYear    {@link #monthAndYear}
     */
    public FamilyBag(String uf, String siafiCityCode, String cityName, String functionCode, String subFunctionCode,
                     String programCode, String actionCode, String favoredNis, String favoredName,
                     String fontAndFinality, String value, String monthAndYear) {
        this.uf = uf;
        this.siafiCityCode = siafiCityCode;
        this.cityName = cityName;
        this.functionCode = functionCode;
        this.subFunctionCode = subFunctionCode;
        this.programCode = programCode;
        this.actionCode = actionCode;
        this.favoredNis = favoredNis;
        this.favoredName = favoredName;
        this.fontAndFinality = fontAndFinality;
        this.value = value;
        this.monthAndYear = monthAndYear;
    }

    @Override
    public String toString() {
        return "----------------------" +
                "\nDados de bolsa" +
                "\n----------------------" +
                "\nUnidade federativa: '" + uf + '\'' +
                "\nCódigo SIAFI do município: '" + siafiCityCode + '\'' +
                "\nNome do município: '" + cityName + '\'' +
                "\nCódigo da função: '" + functionCode + '\'' +
                "\nCódigo de subfunção: '" + subFunctionCode + '\'' +
                "\nCódigo do programa: '" + programCode + '\'' +
                "\nCódigo de ação: '" + actionCode + '\'' +
                "\nNIS do favorecido: '" + favoredNis + '\'' +
                "\nNome do favorecido: '" + favoredName + '\'' +
                "\nFonte-finalidade: '" + fontAndFinality + '\'' +
                "\nValor: '" + value + '\'' +
                "\nMês/ano: '" + monthAndYear + '\'';
    }
}
