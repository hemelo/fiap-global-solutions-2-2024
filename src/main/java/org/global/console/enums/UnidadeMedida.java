package org.global.console.enums;

public enum UnidadeMedida {
    ;

    public static UnidadeMedida fromValue(String unidadeStr) {

        for (UnidadeMedida unidade : UnidadeMedida.values()) {
            if (unidade.name().equalsIgnoreCase(unidadeStr)) {
                return unidade;
            }
        }

        return null;
    }
}
