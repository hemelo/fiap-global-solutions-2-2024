package org.global.console.utils;

public class MathUtils {

    // Método para calcular a distância entre dois pontos usando a fórmula de Haversine
    public static double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Raio da Terra em quilômetros
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Retorna a distância em quilômetros
    }
}
