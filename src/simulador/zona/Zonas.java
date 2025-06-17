package simulador.zona;

import simulador.configuracao.configuracao;

/**
 * Fornece instâncias pré-configuradas de zonas da cidade.
 */
public class Zonas {

    /**
     * Cria a zona Sul com parâmetros de geração de lixo.
     *
     * @return Zona Sul configurada
     */
    public static Zona zonaSul() {
        return new Zona("Sul", configuracao.LIXO_MIN_SUL, configuracao.LIXO_MAX_SUL);
    }

    /**
     * Cria a zona Norte com parâmetros de geração de lixo.
     *
     * @return Zona Norte configurada
     */
    public static Zona zonaNorte() {
        return new Zona("Norte", configuracao.LIXO_MIN_NORTE, configuracao.LIXO_MAX_NORTE);
    }

    /**
     * Cria a zona Centro com parâmetros de geração de lixo.
     *
     * @return Zona Centro configurada
     */
    public static Zona zonaCentro() {
        return new Zona("Centro", configuracao.LIXO_MIN_CENTRO, configuracao.LIXO_MAX_CENTRO);
    }

    /**
     * Cria a zona Leste com parâmetros de geração de lixo.
     *
     * @return Zona Leste configurada
     */
    public static Zona zonaLeste() {
        return new Zona("Leste", configuracao.LIXO_MIN_LESTE, configuracao.LIXO_MAX_LESTE);
    }

    /**
     * Cria a zona Sudeste com parâmetros de geração de lixo.
     *
     * @return Zona Sudeste configurada
     */
    public static Zona zonaSudeste() {
        return new Zona("Sudeste", configuracao.LIXO_MIN_SUDESTE, configuracao.LIXO_MAX_SUDESTE);
    }
}