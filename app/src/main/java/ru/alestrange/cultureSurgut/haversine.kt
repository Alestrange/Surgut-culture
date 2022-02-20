package ru.alestrange.cultureSurgut

class Haversine(private val lat: Double, private val lon: Double) {

        companion object {
            const val earthRadiusKm: Double = 6372.8
        }

        /**
         * Haversine formula. Giving great-circle distances between two points on a sphere from their longitudes and latitudes.
         * It is a special case of a more general formula in spherical trigonometry, the law of haversines, relating the
         * sides and angles of spherical "triangles".
         *
         * https://rosettacode.org/wiki/Haversine_formula#Java
         *
         * @return Distance in kilometers
         */
        fun haversine(destination: Haversine): Double {
            val dLat = Math.toRadians(destination.lat - this.lat);
            val dLon = Math.toRadians(destination.lon - this.lon);
            val originLat = Math.toRadians(this.lat);
            val destinationLat = Math.toRadians(destination.lat);

            val a = Math.pow(Math.sin(dLat / 2), 2.toDouble()) + Math.pow(Math.sin(dLon / 2), 2.toDouble()) * Math.cos(originLat) * Math.cos(destinationLat);
            val c = 2 * Math.asin(Math.sqrt(a));
            return earthRadiusKm * c;
        }
}