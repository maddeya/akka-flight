package net.maddeya

import akka.actor.{ Props, Actor, ActorRef, ActorSystem }
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class BadShakespeareanActor extends Actor {
  def receive = {
    case "Good Morning!" =>
      println("Him: Forsooth 'tis the 'morn, but mourneth for thou doest I do!")
    case "You're terrible!" =>
      println("Him: Yup!")
  }
}

object ShakeMain {
  val system = ActorSystem("BadShakesperean")
  val actor = system.actorOf(Props[BadShakespeareanActor], "Shake")
  
  def send(msg: String): Unit = {
    println(s"Me: $msg")
    actor ! msg
    Thread.sleep(100)
  }
  
  def main(args: Array[String]): Unit = {
    send("Good Morning!")
    send("You're terrible!")
    system.shutdown()
  }
}


/*
object First extends App {
  // needed for '?' below
  implicit val timeout = Timeout(5.seconds)
  val system = ActorSystem("PlaneSimulation")
  val plane = system.actorOf(Props[Plane], "Plane")

  // Grab the controls
  val control = Await.result((plane ? Plane.GiveMeControl).mapTo[ActorRef], 5.seconds)
  
  // Takeoff!
  system.scheduler.scheduleOnce(200.millis) {
    control ! ControlSurfaces.StickBack(1f)
  }

  // Level out
  system.scheduler.scheduleOnce(1.seconds) {
    control ! ControlSurfaces.StickBack(0f)
  }

  // Climb
  system.scheduler.scheduleOnce(1.seconds) {
    control ! ControlSurfaces.StickBack(1f)
  }

  // Level out
  system.scheduler.scheduleOnce(10.seconds) {
    control ! ControlSurfaces.StickBack(0f)
  }

  // Shut down
  system.scheduler.scheduleOnce(5.seconds) {
    system.shutdown()
  }
}
*/

/*
object First extends App {

  val system = akka.actor.ActorSystem("PlaneSimulation")

  val lead = system.actorOf(Props(
    new LeadFlightAttendant with AttendantCreationPolicy),
    system.settings.config.getString(
      "net.maddeya.flightcrew.leadAttendantName"))

  Thread.sleep(2000)

  system.shutdown()
}
*/