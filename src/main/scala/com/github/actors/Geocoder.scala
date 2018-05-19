package com.github.actors

import akka.actor.Actor
import com.github.utils.SolrSearch

class Geocoder extends Actor {

  val solrSearch = new SolrSearch()

  def receive()={

    case zip:String => {
      val coordinates = solrSearch.getCoordinates(zip)
      sender() ! coordinates
    }

  }

}
