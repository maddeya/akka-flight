package net.maddeya

import akka.actor.{ Props, ActorSystem }
import akka.testkit.{ TestKit, TestActorRef, ImplicitSender }
import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import com.typesafe.config.ConfigFactory

object TestFlightAttendant {
  def apply() = new FlightAttendant with AttendantResponsiveness {
    val maxResponseTimeMS = 1
  }
}

class FlightAttendantSpec extends TestKit(ActorSystem("FlightAttendantSpec",
  ConfigFactory.parseString("akka.scheduler.tickduration = 1ms")))
  with ImplicitSender
  with WordSpec
  with ShouldMatchers {
  import FlightAttendant._
  "FlightAttendant" should {
    "get a drink when asked" in {
      val a = TestActorRef(Props(TestFlightAttendant()))
      a ! GetDrink("Soda")
      expectMsg(Drink("Soda"))
    }
  }
}