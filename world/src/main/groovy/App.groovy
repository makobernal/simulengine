package simulengine

import org.jgroups.JChannel
import org.jgroups.Message
import org.jgroups.ReceiverAdapter
import org.jgroups.View

class AppMain {

    static void main(String[] args) {
        new AppRunner(args).run()
    }
}

class AppRunner {

    OptionAccessor options
    CliBuilder cli

    AppCore appCore = new AppCore()

    AppRunner(String[] args) {
        cli = new CliBuilder(usage: 'simulengined [options]', width: 120)
        cli.header = 'Runs a simulengine daemon instance with random PID'
        options = cli.parse(args)
    }

    void run() {

        if (!options) { //options is null, there has been a problem parsing args or options and usage has been printed
            return
        }

        if (options.arguments().size() != 0) {// we accept no arguments
            cli.usage()     //print usage and return
            return
        }

        try {
            appCore.init()
            appCore.runWorld()
        } catch (Exception e) { // Here we could be catching any checked exception thrown by the AppCore class
            println "Exception of type: ${e.class.canonicalName}"
            println "Exception with message: ${e.getMessage()}"
            return
        }
    }
}

class AppCore extends ReceiverAdapter {

    JChannel channel = new JChannel().setReceiver(this)
    boolean running = false
    boolean alive = true
    def world = new World()

    void init() {
        channel.connect('simulengine_messages')
    }

    void runWorld() {
        while (alive) {
            if (running) {
                world.step()

            }
            sleep 1000
        }
    }

    void receive(Message msg) {
        println "$msg.src:$msg.object"
        String action = msg.object
        if (action == 'destroy') {
            alive = false
        } else if (action == 'start') {
            running = true
        } else if (action == 'stop') {
            running = false
        } else if (action == 'step') {
            if (!running) {
                world.step()
            } else {
                println 'Please stop the world first'
            }
        } else if (action == 'reset') {
            if (!running) {
                world = new World()
            } else {
                println 'Please stop the world first'
            }
        } else {
            println "action $action not valid"
        }
    }


    void viewAccepted(View view) {
        println "Cmdr has connected: $view"
    }

}

class World {

    int counter = 0

    void step() {
        counter++
        println "Counter is at: $counter"
    }
}

