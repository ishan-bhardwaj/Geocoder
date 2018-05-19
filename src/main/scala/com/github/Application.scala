package com.github

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, parameters, path, post}
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import actors.{DistanceCalc, Geocoder}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.io.StdIn

object Application {

  val actorSystem = ActorSystem("Geocoder-Actor-System")
  val geocoderActor = actorSystem.actorOf(Props[Geocoder], "Gecoder")
  val distanceCalcActor = actorSystem.actorOf(Props[DistanceCalc], "Distance-Calculator")

  def start () = {

    implicit val actorSystem = ActorSystem()
    implicit val executionContexts = actorSystem.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val timeout: Timeout = Timeout(10 seconds)

    val route =
      path("getCoordinates"){
        post{
          parameters('zip){
            (zip)=>{
              println(s"Request received to determine geo-coorindates for zip - $zip")
              val future = (geocoderActor ? zip).mapTo[Geocoordinates]
              val result = Await.result(future, timeout.duration).toString
              println(s"Result - $result")
              complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, result))
            }
          }
        }
      } ~
        path("getDistance"){
          post{
            parameters('source,'destination){
              (source, destination)=>{
                println(s"Request received to calculate distance between - $source and $destination")
                val future = (distanceCalcActor ? Distance(source, destination)).mapTo[Double]
                val result = Await.result(future, timeout.duration).toString
                println(s"Result - $result")
                complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, result))
              }
            }
          }
        }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    bindingFuture.failed.foreach { ex =>
      println(ex, "Failed to bind to localhost:8000")
    }

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop")
    StdIn.readLine()
    bindingFuture.flatMap(_.unbind()).onComplete(_ => actorSystem.terminate())

  }

}

case class Geocoordinates (latitude:Double, longitude:Double)
case class Distance (source_zip:String, destination_zip:String)
