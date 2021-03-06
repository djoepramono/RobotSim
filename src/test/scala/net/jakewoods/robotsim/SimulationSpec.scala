package net.jakewoods.robotsim

class SimulationSpec extends UnitSpec {
  describe("Simulation") {
    it("should satisfy 'Example A' in PROBLEM.md") {
      val simulation = Simulation
        .create(5, 5)
        .step(Place(0, 0, North))
        .step(Move)
        .step(Report)

      assert(simulation.messages == List("0,1,NORTH"))
    }

    it("should satisfy 'Example B' in PROBLEM.md") {
      val simulation = Simulation
        .create(5, 5)
        .step(Place(0, 0, North))
        .step(Left)
        .step(Report)

      assert(simulation.messages == List("0,0,WEST"))
    }

    it("should satisfy 'Example C' in PROBLEM.md") {
      val simulation = Simulation
        .create(5, 5)
        .step(Place(1, 2, East))
        .step(Move)
        .step(Move)
        .step(Left)
        .step(Move)
        .step(Report)

      assert(simulation.messages == List("3,3,NORTH"))
    }

    describe("when created") {
      it("should not have a robot") {
        val simulation = Simulation.create(5, 5)

        assert(simulation.robot == None)
      }
    }

    describe("when it has no robot") {
      it("should ignore all commands other than Place") {
        val commands = Seq(Move, Left, Right, Report)

        val originalSim = Simulation.create(5, 5)
        commands.foreach(command => {
          val simulation = originalSim.step(command)

          // Commands that are ignored should return an identical simulation!
          assert(simulation == originalSim)
        })
      }

      it("should place a new robot when given the Place command") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(1, 1, North))

        assert(simulation.robot.isDefined)
      }

      it("should place the new robot correctly when given the Place command") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(1, 2, North))

        assert(simulation.robot == Some(Robot(1, 2, North)))
      }

      it("should not place a robot out of bounds") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(10, 10, North))

        assert(simulation.robot.isEmpty)
      }
    }

    describe("when it has a robot") {
      it("the robot should change places when given the Place command") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(1,1,North))
          .step(Place(3,3,South))

        assert(simulation.robot == Some(Robot(3,3,South)))
      }

      it("the robot should turn left when given the Left command") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(1,1,North))
          .step(Left)

        assert(simulation.robot == Some(Robot(1, 1, West)))
      }

      it("the robot should turn right when given the Right command") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(1,1,North))
          .step(Right)

        assert(simulation.robot == Some(Robot(1, 1, East)))
      }

      it("should move up one square when given the Move command and facing NORTH") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(2, 2, North))
          .step(Move)

        assert(simulation.robot == Some(Robot(2, 3, North)))
      }

      it("should move down one square when given the Move command and facing SOUTH") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(2, 2, South))
          .step(Move)

        assert(simulation.robot == Some(Robot(2, 1, South)))
      }

      it("should move left one square when given the Move command and facing WEST") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(2, 2, West))
          .step(Move)

        assert(simulation.robot == Some(Robot(1, 2, West)))
      }

      it("should move right one square when given the Move command and facing EAST") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(2, 2, East))
          .step(Move)

        assert(simulation.robot == Some(Robot(3, 2, East)))
      }

      it("it should return a message containing the robots details when given the Report command") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(1, 1, North))
          .step(Report)

        assert(simulation.messages == List("1,1,NORTH"))
      }

      it("should not repeat messages when preventing the robot from falling off the table") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(0, 0, South))
          .step(Report)
          .step(Move)

        assert(simulation.robot == Some(Robot(0, 0, South)))
        assert(simulation.messages.isEmpty)
      }

      it("should not fall off the NORTH end of the table") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(4, 4, North))
          .step(Move)
          .step(Move) // This robot is tenacious!

        assert(simulation.robot == Some(Robot(4, 4, North)))
      }

      it("should not fall off the SOUTH end of the table") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(0, 0, South))
          .step(Move)
          .step(Move)

        assert(simulation.robot == Some(Robot(0, 0, South)))
      }

      it("should not fall off the EAST end of the table") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(4, 2, East))
          .step(Move)
          .step(Move)

        assert(simulation.robot == Some(Robot(4, 2, East)))
      }

      it("should not fall off the WEST end of the table") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(0, 2, West))
          .step(Move)
          .step(Move)

        assert(simulation.robot == Some(Robot(0, 2, West)))
      }
    }
  }
}
