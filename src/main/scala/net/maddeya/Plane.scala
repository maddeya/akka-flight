package net.maddeya

import akka.actor.{ Props, Actor, ActorRef, ActorLogging }

object Plane {
  // Returns the control surface to the Actor that
  // asks for them
  case object GiveMeControl
  // How we respond to the GiveMeControl message
  case class Controls(controls: ActorRef)
}

class Plane extends Actor with ActorLogging {
  import Altimeter._
  import Plane._
  import EventSource._
  
  override def preStart(): Unit = {
    altimeter ! RegisterListener(self)
  }

  val altimeter = context.actorOf(Props[Altimeter], "Altimeter")
  val controls = context.actorOf(Props(new ControlSurfaces(altimeter)), "ControlSurfaces")

  def receive = {
    case GiveMeControl =>
      log info ("Plane giving control.")
      sender ! controls
    case AltitudeUpdate(altitude) =>
      log info (s"Altitude is now $altitude")
  }
}