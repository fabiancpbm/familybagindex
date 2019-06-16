/**
 * Bolsa familia.
 */
public class FamilyBag {

    /**
     * Unidade federativa.
     */
    private String uf;

    /**
     * Codigo SIAFI do municipio.
     */
    private String siafiCityCode;

    /**
     * Nome do municipio.
     */
    private String cityName;

    /**
     * Codigo da funçao.
     */
    private String functionCode;

    /**
     * Codigo de subfunçao.
     */
    private String subFunctionCode;

    /**
     * Codigo do programa.
     */
    private String programCode;

    /**
     * Codigo de açao.
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
     * Fonte-finalidade. Por exemplo: CAIXA-Programa Bolsa familia.
     */
    private String fontAndFinality;

    /**
     * Valor.
     */
    private String value;

    /**
     * Mes e ano.
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
                "\nCodigo SIAFI do municipio: '" + siafiCityCode + '\'' +
                "\nNome do municipio: '" + cityName + '\'' +
                "\nCodigo da funçao: '" + functionCode + '\'' +
                "\nCodigo de subfunçao: '" + subFunctionCode + '\'' +
                "\nCodigo do programa: '" + programCode + '\'' +
                "\nCodigo de açao: '" + actionCode + '\'' +
                "\nNIS do favorecido: '" + favoredNis + '\'' +
                "\nNome do favorecido: '" + favoredName + '\'' +
                "\nFonte-finalidade: '" + fontAndFinality + '\'' +
                "\nValor: '" + value + '\'' +
                "\nMes/ano: '" + monthAndYear + '\'';
    }
}
