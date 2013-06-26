package net.maddeya

import akka.actor.{Props, Actor, ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object First {
  // needed for '?' below
  implicit val timeout = Timeout(5.seconds)
  val system = ActorSystem("PlaneSimulation")
  val plane = system.actorOf(Props[Plane], "Plane")
  
  def main(args: Array[String]) {
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
    system.scheduler.scheduleOnce(3.seconds) {
      control ! ControlSurfaces.StickBack(0.5f)
    }
    
    // Level out
    system.scheduler.scheduleOnce(4.seconds) {
      control ! ControlSurfaces.StickBack(0f)
    }
    
    // Shut down
    system.scheduler.scheduleOnce(5.seconds) {
      system.shutdown()
    }
  }
}



/*
object First extends App {

  val system = akka.actor.ActorSystem("PlaneSimulation")

  val lead = system.actorOf(Props(
    new LeadFlightAttendant with AttendantCreationPolicy),
    system.settings.config.getString(
      "zzz.akka.avionics.flightcrew.leadAttendantName"))

  Thread.sleep(2000)

  system.shutdown()
}
*/