package simulengine

import org.beryx.textio.TextIO
import org.beryx.textio.TextIoFactory
import org.jgroups.JChannel
import org.jgroups.Message

class AppMain {

    static void main(String[] args) {
        new AppRunner().run()
    }
}

class AppRunner {

    def actions = ['start', 'stop', 'step', 'reset', 'destroy']

    JChannel channel = new JChannel()
    TextIO textIO = TextIoFactory.textIO

    void run() {
        channel.connect('simulengine_messages')

        while (true) {
            String action = textIO.newStringInputReader().withPossibleValues(actions).withDefaultValue("step").read('World control:')

            channel.send(new Message(null, action))

            if (action == 'destroy') break

        }

        channel.close()
    }
}


