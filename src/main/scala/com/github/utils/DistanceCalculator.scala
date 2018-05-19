package com.github.utils

import com.github.Geocoordinates

import scala.math._

class DistanceCalculator {

  val R = 6372.8 //radius in km

  def getDistance(coords1: Geocoordinates, coords2: Geocoordinates): Double = {
    val xLat = coords1.latitude
    val xLon = coords1.longitude
    val yLat = coords2.latitude
    val yLon = coords2.longitude
    val dLat = (yLat - xLat).toRadians
    val dLon = (yLon - xLon).toRadians
    val a = pow(sin(dLat / 2), 2) + pow(sin(dLon / 2), 2) * cos(xLat.toRadians) * cos(yLat.toRadians)
    val c = 2 * asin(sqrt(a))
    R * c
  }

}
