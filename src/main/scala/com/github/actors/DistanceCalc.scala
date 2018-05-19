package com.github.actors

import akka.actor.{Actor, ActorSystem, Props}
import com.github.{Distance, Geocoordinates}
import akka.pattern.ask
import akka.util.Timeout
import com.github.utils.DistanceCalculator

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class DistanceCalc extends Actor {

  implicit val timeout: Timeout = Timeout(10 seconds)

  val actorSystem = ActorSystem("Distance-Calculation")
  val geoCoder = actorSystem.actorOf(Props[Geocoder], "Geocoder-Actor")
  val distanceCalculator = new DistanceCalculator()

  def receive()= {

    case distance: Distance => {

      val result = for {
        source <- (geoCoder ? distance.source_zip).mapTo[Geocoordinates]
        destination <- (geoCoder ? distance.destination_zip).mapTo[Geocoordinates]
      }
        yield distanceCalculator.getDistance(source, destination)

      sender() ! Await.result(result, timeout.duration)

    }

  }

}
