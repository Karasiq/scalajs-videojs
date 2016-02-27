package com.karasiq.scalajstest.backend

import java.util.concurrent.TimeUnit

import akka.actor._
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import spray.can.Http
import spray.http.MediaTypes
import spray.routing.HttpService

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object TestApp extends App {
  def startup(): Unit = {
    implicit val timeout = Timeout(20 seconds)

    implicit val actorSystem = ActorSystem("scalajs-test-server")

    Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
      override def run(): Unit = {
        Await.result(actorSystem.terminate(), FiniteDuration(5, TimeUnit.MINUTES))
      }
    }))

    val service = actorSystem.actorOf(Props[AppHandler], "webService")
    IO(Http) ? Http.Bind(service, interface = "localhost", port = 9000)
  }

  final class AppHandler extends Actor with HttpService {
    override def receive: Actor.Receive = runRoute {
      get {
        compressResponse() {
          // Index page
          (pathSingleSlash & respondWithMediaType(MediaTypes.`text/html`)) {
            getFromResource("webapp/index.html")
          } ~
            // Other resources
            getFromResourceDirectory("webapp")
        }
      }
    }

    override def actorRefFactory: ActorRefFactory = context
  }

  startup()
}
